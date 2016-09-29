package com.fidel.bot.service;

import java.text.DecimalFormat;
import java.util.List;
import java.util.stream.Collectors;

import com.fidel.bot.dto.BalanceDTO;
import com.fidel.bot.dto.OrderDTO;
import com.fidel.bot.dto.TickerDTO;
import com.fidel.bot.enumeration.Operation;
import com.fidel.bot.enumeration.Pair;
import com.fidel.bot.enumeration.Status;
import com.fidel.bot.exception.EmptyResponseException;
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
            throws PlaceOrderException, EmptyResponseException, ParseException, InvalidSymbolsPairException, InterruptedException {

        TickerDTO tickerDTO = obtainTicker(pair);
        LOG.info("{}", tickerDTO);
        checkBuyOrdersDone(tickerDTO.getLast());
        checkSellOrdersDone(tickerDTO.getLast());
        List<OrderDTO> openOrders = obtainOpenOrders(operation, pair);
        for (int i = 0; i < depth; i++) {
            if (isBuyOrderPlacingAllowed(openOrders, price, tickerDTO.getLast(), step)) {
                OrderDTO orderDTO = placeOrder(operation, pair, amount, price);
                orderDTO.setSpread(spread);
                orderDTO.setProfit(plannedProfit);
                LOG.info("placed {}", orderDTO);
                orderService.saveOrder(orderDTO);
            } else {
                LOG.debug("Cannot perform order placing with price {} and step {}", price, step);
            }
            price -= step;
        }
        LOG.info("{}", obtainBalance());
    }

    private boolean isBuyOrderPlacingAllowed(List<OrderDTO> openOrders, double price, double lastPrice, double step)
            throws EmptyResponseException, InvalidSymbolsPairException, ParseException, PlaceOrderException {
        return !(price > (lastPrice + 0.00000001) || openOrders.stream().
                anyMatch(x -> (price > (x.getPrice() - 0.95 * step)) && (price < (x.getPrice() + 0.95 * step))));
    }

    private void checkBuyOrdersDone(double lastPrice) throws EmptyResponseException, ParseException, PlaceOrderException {
        List<Order> openOrders = orderService.findAll().stream().filter(x -> x.getDoneDate() == null).collect(Collectors.toList());
        List<Order> doneOrders = openOrders.stream().filter(x -> x.getPrice() >= lastPrice).collect(Collectors.toList());
        //List<Order> doneOrders = openOrders.stream().filter(x -> x.getPrice() < lastPrice).collect(Collectors.toList()); //TODO rm after test
        for (Order doneOrder : doneOrders) {
            if (isOrderDone(doneOrder.getId())) { // server verifying
                double sellPrice = Math.floor((doneOrder.getPrice() *
                        (1 + doneOrder.getProfit() + doneOrder.getSpread())) * 100000000) / 100000000;
                OrderDTO sellOrder = placeOrder(Operation.SELL, Pair.valueOf(doneOrder.getPair()),
                        doneOrder.getAmount(), sellPrice);

                LOG.info("placed {}", sellOrder);
                LOG.info("{}", obtainBalance());
                orderService.makeDoneBuyOrder(doneOrder.getId(), sellOrder.getId(), sellOrder.getCreateDate());
                //orderService.makeDoneBuyOrder(doneOrder.getId(), doneOrder.getId(), doneOrder.getCreateDate());//TODO rm after test
                //LOG.info("sellOrder placed");
            }
        }
    }

    private void checkSellOrdersDone(double lastPrice) throws EmptyResponseException, ParseException {
        List<Order> openOrders = orderService.findAll().stream()
                .filter(x -> x.getConverseId() != null && !x.isClosed()).collect(Collectors.toList());
        List<Order> doneOrders = openOrders.stream()
                .filter(x -> (x.getPrice() + x.getProfit()) <= lastPrice).collect(Collectors.toList());
        for (Order doneOrder : doneOrders) {
            if (isOrderDone(doneOrder.getConverseId())) { // server verifying
                orderService.makeDoneSellOrder(doneOrder.getId());
                LOG.info("sellOrder closed");
            }
        }
    }

    private boolean isOrderDone(long id) throws EmptyResponseException, ParseException {
        return parserService.parseOrderStatus(requestController.get_order(id)) == Status.DONE;
        //return parserService.parseOrderStatus(requestController.get_order(id))==Status.ACTIVE;//TODO rm after test
    }

    private BalanceDTO obtainBalance() throws EmptyResponseException, ParseException {
        return parserService.parseBalance(requestController.balance());
    }

    private OrderDTO placeOrder(Operation operation, Pair pair, double amount, double price)
            throws EmptyResponseException, PlaceOrderException, ParseException {
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
