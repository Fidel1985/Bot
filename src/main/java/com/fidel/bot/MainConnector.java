package com.fidel.bot;

import java.util.List;

import com.fidel.bot.exception.EmptyResponseException;
import com.fidel.bot.exception.PlaceOrderException;
import com.fidel.bot.exception.InvalidSymbolsPairException;
import com.fidel.bot.jpa.*;
import com.fidel.bot.service.RequestManager;
import com.fidel.bot.service.Parser;
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
    private RequestManager requestManager;

    @Autowired
    private Parser jsonParser;

    @Scheduled(initialDelayString = "${configuration.delay:10}000", fixedDelayString = "${configuration.schedule:1000}000")
    public void scheduledTask() {

        Type type = Type.BUY;
        Pair pair = Pair.ETHUSD;
        double lot = 0.5;
        double price = 11.0;
        double priceStep = 0.5;
        double spread = 0.004; // 2% for buy and 2% for sell
        double plannedProfit = 0.02;

        try {
            Balance balance = obtainBalance();
            System.out.println(balance);
            Order order = placeOrder(type, pair, lot, price);
            System.out.println(order);
            List<Order> openOrders = obtainOpenOrders(type, pair);
            System.out.println(openOrders);
            System.out.println("cancel order = " + cancelOrder(order.getId()));
            Ticker ticker = obtainTicker(pair);
            System.out.println(ticker);

        } catch (PlaceOrderException | EmptyResponseException | ParseException | InvalidSymbolsPairException e) {
            LOG.error(e.getMessage());
            application.initiateShutdown(1);
        }
    }

    private Balance obtainBalance() throws EmptyResponseException, ParseException {
        return jsonParser.parseBalance(requestManager.balance());
    }

    private Order placeOrder(Type type, Pair pair, double lot, double price) throws EmptyResponseException, PlaceOrderException, ParseException {
        return jsonParser.parseOrder(requestManager.place_order(type.getValue(), lot, price, pair.getValue()), type, pair);
    }

    private Boolean cancelOrder(long orderId) throws EmptyResponseException {
        return (Boolean) requestManager.cancel_order(orderId);
    }

    private Ticker obtainTicker(Pair pair) throws EmptyResponseException, InvalidSymbolsPairException, ParseException {
        return jsonParser.parseTicker(requestManager.ticker(pair.getValue()), pair);
    }

    private List<Order> obtainOpenOrders(Type type, Pair pair) throws EmptyResponseException, InvalidSymbolsPairException,
            ParseException, PlaceOrderException {
        return jsonParser.parseOpenOrders(requestManager.open_orders(pair.getValue()), type, pair);
    }

}
