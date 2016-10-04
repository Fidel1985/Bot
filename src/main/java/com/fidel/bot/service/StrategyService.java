package com.fidel.bot.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

import com.fidel.bot.dto.BalanceDTO;
import com.fidel.bot.dto.OrderDTO;
import com.fidel.bot.dto.TickerDTO;
import com.fidel.bot.enumeration.Operation;
import com.fidel.bot.enumeration.Pair;
import com.fidel.bot.enumeration.Status;
import com.fidel.bot.exception.EmptyResponseException;
import com.fidel.bot.exception.InvalidParamsException;
import com.fidel.bot.exception.InvalidSymbolsPairException;
import com.fidel.bot.exception.PlaceOrderException;
import com.fidel.bot.jpa.entity.Grid;
import com.fidel.bot.jpa.entity.Order;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class StrategyService {
    private static final Logger LOG = LoggerFactory.getLogger(StrategyService.class);
    private static final BigDecimal RANGE_COEFF = new BigDecimal(0.95);

    @Autowired
    private RequestController requestController;

    @Autowired
    private ParserService parserService;

    @Autowired
    private GridService gridService;

    @Autowired
    private OrderService orderService;

    @Value("${configuration.schedule}")
    private Long schedule;

    @Async
    public void staticStrategy(Operation operation, Pair pair, double amount, BigDecimal step, BigDecimal spread, BigDecimal plannedProfit)
            throws PlaceOrderException, EmptyResponseException, ParseException, InvalidSymbolsPairException, InterruptedException,
            InvalidParamsException, java.text.ParseException {
        List<Grid> gridItems = gridService.getGrid(operation, pair);
        while (true) {
            TickerDTO tickerDTO = obtainTicker(pair);
            LOG.info("{}", tickerDTO);
            checkFirstOrdersDone(tickerDTO.getLast(), operation, pair);
            checkConverseOrdersDone(tickerDTO.getLast(), operation, pair);
            for (Grid item : gridItems) {
                if (isOrderPlacingAllowed(operation, pair, item.getPrice(), tickerDTO.getLast(), step)) {
                    OrderDTO orderDTO = placeOrder(operation, pair, amount, item.getPrice());
                    orderDTO.setSpread(spread);
                    orderDTO.setProfit(plannedProfit);
                    LOG.info("placed {}", orderDTO);
                    orderService.saveOrder(orderDTO);
                    LOG.info("{}", obtainBalance());
                } else {
                    LOG.debug("Cannot perform order placing with price {} and step {}", item.getPrice(), step);
                }
            }
            Thread.sleep(schedule);
        }
    }

    private boolean isOrderPlacingAllowed(Operation operation, Pair pair, BigDecimal price, BigDecimal lastPrice, BigDecimal step)
            throws EmptyResponseException, InvalidSymbolsPairException, ParseException, PlaceOrderException {
        List<Order> nonConversedOrders = orderService.findByClosedFalseAndPair(pair);
        boolean isRangeOccupied = nonConversedOrders.stream().anyMatch(x -> (price.compareTo(x.getPrice().subtract(RANGE_COEFF.multiply(step))) > 0
                        && price.compareTo(x.getPrice().add(RANGE_COEFF.multiply(step))) < 0));
        if(operation == Operation.BUY) {
            return !(price.compareTo(lastPrice) >= 0 || isRangeOccupied);
        } else {
            return !(price.compareTo(lastPrice) <= 0 || isRangeOccupied);
        }
    }

    private void checkFirstOrdersDone(BigDecimal lastPrice, Operation operation, Pair pair)
            throws EmptyResponseException, ParseException, PlaceOrderException, InvalidParamsException, java.text.ParseException {
        List<Order> openOrders = orderService.findByDoneDateIsNull();
        List<Order> doneOrders;
        if(operation == Operation.BUY) {
            doneOrders = openOrders.stream().filter(x -> x.getPrice().compareTo(lastPrice) >= 0
                    && Pair.valueOf(x.getPair()) == pair).collect(Collectors.toList());
        } else {
            doneOrders = openOrders.stream().filter(x -> x.getPrice().compareTo(lastPrice) <= 0
                    && Pair.valueOf(x.getPair()) == pair).collect(Collectors.toList());
        }
        for (Order doneOrder : doneOrders) {
            if (isOrderDone(doneOrder.getId())) { // server verifying
                BigDecimal conversePrice;
                if(operation == Operation.BUY) {
                    conversePrice = (doneOrder.getPrice().
                            multiply(new BigDecimal(1).add(doneOrder.getProfit().add(doneOrder.getSpread())))).setScale(4, RoundingMode.FLOOR);
                  } else {
                    //conversePrice = Math.floor((doneOrder.getPrice() * (1 - doneOrder.getProfit() - doneOrder.getSpread())) * 10000) / 10000;
                    conversePrice = (doneOrder.getPrice().
                            multiply(new BigDecimal(1).subtract(doneOrder.getProfit().subtract(doneOrder.getSpread())))).setScale(4, RoundingMode.FLOOR);
                }
                operation = switchOperation(operation);
                OrderDTO converseOrder = placeOrder(operation, Pair.valueOf(doneOrder.getPair()),
                        doneOrder.getAmount(), conversePrice);
                LOG.info("placed {}", converseOrder);
                LOG.info("{}", obtainBalance());
                orderService.makeDoneFirstOrder(doneOrder.getId(), converseOrder.getId(), converseOrder.getCreateDate(), converseOrder.getPrice());
            }
        }
    }

    private void checkConverseOrdersDone(BigDecimal lastPrice, Operation operation, Pair pair) throws EmptyResponseException, ParseException {
        List<Order> openOrders = orderService.findByClosedFalseAndConverseIdNotNull();
        List<Order> doneOrders;
        if(operation == Operation.BUY) {
            doneOrders = openOrders.stream().filter(x -> x.getConversePrice().compareTo(lastPrice) <= 0
                    && Pair.valueOf(x.getPair()) == pair).collect(Collectors.toList());
        } else {
            doneOrders = openOrders.stream().filter(x -> x.getConversePrice().compareTo(lastPrice) >= 0
                    && Pair.valueOf(x.getPair()) == pair).collect(Collectors.toList());
        }
        for (Order doneOrder : doneOrders) {
            if (isOrderDone(doneOrder.getConverseId())) { // server verifying
                orderService.makeDoneConverseOrder(doneOrder.getId());
                LOG.info("converseOrder closed");
            }
        }
    }

    private Operation switchOperation(Operation operation) {
        return operation == Operation.BUY ? Operation.SELL : Operation.BUY;
    }

    private boolean isOrderDone(long id) throws EmptyResponseException, ParseException {
        return parserService.parseOrderStatus(requestController.get_order(id)) == Status.DONE;
        //return parserService.parseOrderStatus(requestController.get_order(id)) == Status.ACTIVE;
    }

    private BalanceDTO obtainBalance() throws EmptyResponseException, ParseException {
        return parserService.parseBalance(requestController.balance());
    }

    private OrderDTO placeOrder(Operation operation, Pair pair, double amount, BigDecimal price)
            throws EmptyResponseException, PlaceOrderException, ParseException, InvalidParamsException, java.text.ParseException {
        return parserService.parseOrder(requestController.place_order(operation.getValue(), amount, price, pair.getValue()), operation, pair);
    }

    private Boolean cancelOrder(long orderId) throws EmptyResponseException {
        return (Boolean) requestController.cancel_order(orderId);
    }

    private TickerDTO obtainTicker(Pair pair) throws EmptyResponseException, InvalidSymbolsPairException, ParseException, java.text.ParseException {
        return parserService.parseTicker(requestController.ticker(pair.getValue()), pair);
    }

    private List<OrderDTO> obtainOpenOrders(Operation operation, Pair pair) throws EmptyResponseException,
            InvalidSymbolsPairException, ParseException, PlaceOrderException, java.text.ParseException {
        return parserService.parseOpenOrders(requestController.open_orders(pair.getValue()), operation, pair);
    }
}
