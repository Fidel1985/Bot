package com.fidel.bot;

import java.util.List;
import java.util.Set;

import com.fidel.bot.enumeration.Operation;
import com.fidel.bot.enumeration.Pair;
import com.fidel.bot.exception.EmptyResponseException;
import com.fidel.bot.exception.PlaceOrderException;
import com.fidel.bot.exception.InvalidSymbolsPairException;
import com.fidel.bot.dto.*;
import com.fidel.bot.jpa.OrderRepository;
import com.fidel.bot.service.OrderManager;
import com.fidel.bot.service.RequestController;
import com.fidel.bot.service.ParserService;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MainConnector {
    private static final Logger LOG = LoggerFactory.getLogger(MainConnector.class);

    @Autowired
    private Application application;

    @Autowired
    private RequestController requestController;

    @Autowired
    private ParserService parserService;

    @Autowired
    private OrderManager orderManager;

    @Scheduled(initialDelayString = "${configuration.delay:10}000", fixedDelayString = "${configuration.schedule:1000}000")
    public void scheduledTask() {

        Operation operation = Operation.BUY;
        Pair pair = Pair.ETHUSD;
        double lot = 0.5;
        double price = 11.0;
        double priceStep = 0.5;
        double spread = 0.004; // 2% for buy and 2% for sell
        double plannedProfit = 0.02;

        try {
            BalanceDTO balanceDTO = obtainBalance();
            System.out.println(balanceDTO);
            OrderDTO orderDTO = placeOrder(operation, pair, lot, price);
            System.out.println(orderDTO);
            Set<OrderDTO> openOrderDTOs = obtainOpenOrders(operation, pair);
            orderManager.saveOrders(openOrderDTOs);
            System.out.println(openOrderDTOs);
            System.out.println("cancel orderDTO = " + cancelOrder(orderDTO.getId()));
            TickerDTO tickerDTO = obtainTicker(pair);
            System.out.println(tickerDTO);

        } catch (PlaceOrderException | EmptyResponseException | ParseException | InvalidSymbolsPairException e) {
            LOG.error(e.getMessage());
            application.initiateShutdown(1);
        }
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

    private Set<OrderDTO> obtainOpenOrders(Operation operation, Pair pair) throws EmptyResponseException, InvalidSymbolsPairException,
            ParseException, PlaceOrderException {
        return parserService.parseOpenOrders(requestController.open_orders(pair.getValue()), operation, pair);
    }

}
