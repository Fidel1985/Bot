package com.fidel.bot;

import com.fidel.bot.jpa.Balance;
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

        Balance balance = jsonParser.parseBalance(accountManager.balance());
        System.out.println("balance " + balance.toString());

        Object object = accountManager.place_order("buy", 0.7, 11.0, "ETH/USD");

        System.out.println("balance " + balance.toString());

    }

}
