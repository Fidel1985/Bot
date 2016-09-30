package com.fidel.bot.service;

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
import com.fidel.bot.jpa.entity.Order;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StrategyService {
    private static final Logger LOG = LoggerFactory.getLogger(StrategyService.class);

    @Autowired
    private RequestController requestController;

    @Autowired
    private ParserService parserService;

    @Autowired
    private OrderService orderService;

    public void staticStrategy(Operation operation, Pair pair, double amount, Double price, double step, int depth, double spread, double plannedProfit)
            throws PlaceOrderException, EmptyResponseException, ParseException, InvalidSymbolsPairException, InterruptedException, InvalidParamsException {
        TickerDTO tickerDTO = obtainTicker(pair);
        LOG.info("{}", tickerDTO);
        checkFirstOrdersDone(tickerDTO.getLast(), operation);
        checkConverseOrdersDone(tickerDTO.getLast(), operation);
        List<OrderDTO> openOrders = obtainOpenOrders(operation, pair);
        for (int i = 0; i < depth; i++) {
            if (isOrderPlacingAllowed(openOrders, operation, price, tickerDTO.getLast(), step)) {
                OrderDTO orderDTO = placeOrder(operation, pair, amount, price);
                orderDTO.setSpread(spread);
                orderDTO.setProfit(plannedProfit);
                LOG.info("placed {}", orderDTO);
                orderService.saveOrder(orderDTO);
                LOG.info("{}", obtainBalance());
            } else {
                LOG.debug("Cannot perform order placing with price {} and step {}", price, step);
            }
            if(operation == Operation.BUY){
                price -= step;
            } else {
                price += step;
            }
        }
    }

    private boolean isOrderPlacingAllowed(List<OrderDTO> openOrders, Operation operation, double price, double lastPrice, double step)
            throws EmptyResponseException, InvalidSymbolsPairException, ParseException, PlaceOrderException {
        boolean isRangeOccupied = openOrders.stream().anyMatch(x -> (price > (x.getPrice() - 0.95 * step)) && (price < (x.getPrice() + 0.95 * step)));
        if(operation == Operation.BUY) {
            return !((price > lastPrice + 0.0001) || isRangeOccupied);
        } else {
            return !((price < lastPrice - 0.0001) || isRangeOccupied);
        }
    }

    private void checkFirstOrdersDone(double lastPrice, Operation operation)
            throws EmptyResponseException, ParseException, PlaceOrderException, InvalidParamsException {
        List<Order> openOrders = orderService.findAll().stream().filter(x -> x.getDoneDate() == null).collect(Collectors.toList());
        List<Order> doneOrders;
        if(operation == Operation.BUY) {
            doneOrders = openOrders.stream().filter(x -> x.getPrice() > lastPrice + 0.0001).collect(Collectors.toList());
        } else {
            doneOrders = openOrders.stream().filter(x -> x.getPrice() < lastPrice - 0.0001).collect(Collectors.toList());
        }
        for (Order doneOrder : doneOrders) {
            if (isOrderDone(doneOrder.getId())) { // server verifying
                double conversePrice;
                if(operation == Operation.BUY) {
                    conversePrice = Math.floor((doneOrder.getPrice() * (1 + doneOrder.getProfit() + doneOrder.getSpread())) * 10000) / 10000;
                  } else {
                    conversePrice = Math.floor((doneOrder.getPrice() * (1 - doneOrder.getProfit() - doneOrder.getSpread())) * 10000) / 10000;
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

    private void checkConverseOrdersDone(double lastPrice, Operation operation) throws EmptyResponseException, ParseException {
        List<Order> openOrders = orderService.findAll().stream()
                .filter(x -> x.getConverseId() != null && !x.isClosed()).collect(Collectors.toList());
        List<Order> doneOrders;
        if(operation == Operation.BUY) {
            doneOrders = openOrders.stream().filter(x -> x.getConversePrice() < lastPrice - 0.0001).collect(Collectors.toList());
        } else {
            doneOrders = openOrders.stream().filter(x -> x.getConversePrice() > lastPrice + 0.0001).collect(Collectors.toList());
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

    private OrderDTO placeOrder(Operation operation, Pair pair, double amount, double price)
            throws EmptyResponseException, PlaceOrderException, ParseException, InvalidParamsException {
        return parserService.parseOrder(requestController.place_order(operation.getValue(), amount, price, pair.getValue()), operation, pair);
    }

    private Boolean cancelOrder(long orderId) throws EmptyResponseException {
        return (Boolean) requestController.cancel_order(orderId);
    }

    private TickerDTO obtainTicker(Pair pair) throws EmptyResponseException, InvalidSymbolsPairException, ParseException {
        return parserService.parseTicker(requestController.ticker(pair.getValue()), pair);
    }

    private List<OrderDTO> obtainOpenOrders(Operation operation, Pair pair) throws EmptyResponseException,
            InvalidSymbolsPairException, ParseException, PlaceOrderException {
        return parserService.parseOpenOrders(requestController.open_orders(pair.getValue()), operation, pair);
    }
}
