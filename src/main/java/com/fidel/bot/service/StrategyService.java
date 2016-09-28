package com.fidel.bot.service;

import java.util.List;

import com.fidel.bot.dto.BalanceDTO;
import com.fidel.bot.dto.OrderDTO;
import com.fidel.bot.dto.TickerDTO;
import com.fidel.bot.enumeration.Operation;
import com.fidel.bot.enumeration.Pair;
import com.fidel.bot.enumeration.Status;
import com.fidel.bot.exception.EmptyResponseException;
import com.fidel.bot.exception.InvalidSymbolsPairException;
import com.fidel.bot.exception.PlaceOrderException;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StrategyService {
    private static final Logger LOG = LoggerFactory.getLogger(StrategyService.class);
    private int counter = 0;

    @Autowired
    private RequestController requestController;

    @Autowired
    private ParserService parserService;

    @Autowired
    private OrderService orderService;

    public void staticStrategy(Operation operation, Pair pair, double lot, double price, double step, double plannedProfit)
            throws PlaceOrderException, EmptyResponseException, ParseException, InvalidSymbolsPairException, InterruptedException {

        //Object object = requestController.get_order(3602496171L);
        //Status status = parserService.parseOrderStatus(object);


        TickerDTO tickerDTO = obtainTicker(pair);
        //LOG.info(tickerDTO.toString());
        if (price <= tickerDTO.getLast()) {
            OrderDTO buyOrder = placeOrder(Operation.BUY, pair, lot, price);
            //LOG.info("Placed " + buyOrder.toString());
            if (buyOrder.getPrice() >= tickerDTO.getLast()) {
                if (isOrderDone(buyOrder.getId())) {
                    orderService.saveOrder(buyOrder);
                    OrderDTO sellOrder = placeOrder(Operation.SELL, pair, lot, price*(1+plannedProfit));
                    while(true){
                        if(isOrderDone(sellOrder.getId())) {
                            orderService.closeOrder(buyOrder.getId());
                            break;
                        }
                        wait(1000);
                    }

                }
            }
        }


        /*BalanceDTO balanceDTO = obtainBalance();
        System.out.println(balanceDTO);
        List<OrderDTO> openOrderDTOs = obtainOpenOrders(operation, pair);
        orderService.saveOrders(openOrderDTOs);
        System.out.println(openOrderDTOs);
        System.out.println("cancel orderDTO = " + cancelOrder(orderDTO.getId()));*/

    }

    private boolean isOrderDone(long id) throws EmptyResponseException, ParseException {
        return parserService.parseOrderStatus(requestController.get_order(id))==Status.DONE;
    }

    private BalanceDTO obtainBalance() throws EmptyResponseException, ParseException {
        return parserService.parseBalance(requestController.balance());
    }

    private OrderDTO placeOrder(Operation operation, Pair pair, double lot, double price) throws EmptyResponseException, PlaceOrderException, ParseException {
        return parserService.parseOrder(requestController.place_order(operation.getValue(), lot, price, pair.getValue()), operation, pair);
    }

    private Boolean cancelOrder(long orderId) throws EmptyResponseException {
        return (Boolean) requestController.cancel_order(orderId);
    }

    private TickerDTO obtainTicker(Pair pair) throws EmptyResponseException, InvalidSymbolsPairException, ParseException {
        return parserService.parseTicker(requestController.ticker(pair.getValue()), pair);
    }

    private List<OrderDTO> obtainOpenOrders(Operation operation, Pair pair) throws EmptyResponseException, InvalidSymbolsPairException,
            ParseException, PlaceOrderException {
        return parserService.parseOpenOrders(requestController.open_orders(pair.getValue()), operation, pair);
    }
}
