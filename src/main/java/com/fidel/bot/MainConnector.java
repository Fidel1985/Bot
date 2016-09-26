package com.fidel.bot;

import com.fidel.bot.jpa.*;
import com.fidel.bot.service.AccountManager;
import com.fidel.bot.service.Parser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MainConnector {
    private static final Logger LOG = LoggerFactory.getLogger(MainConnector.class);

    @Autowired
    private AccountManager accountManager;

    @Autowired
    private Parser jsonParser;

    @Scheduled(initialDelayString = "${configuration.delay:10}000", fixedDelayString = "${configuration.schedule:1000}000")
    public void checkBalance() {

        Object object = accountManager.balance();
        Balance balance = jsonParser.parseBalance(object);
        System.out.println(balance);
        Object object2 = accountManager.place_order("buy", 0.7, 11.0, "ETH/USD");
        Order order = jsonParser.parseOrder(object2, Type.BUY, Pair.ETHUSD);
        System.out.println(order);
        accountManager.cancel_order(order.getId());
        Object object3 = accountManager.ticker("ETH/USD");
        Ticker ticker = jsonParser.parseTicker(object3, Pair.ETHUSD);
        System.out.println(ticker);
    }

}
