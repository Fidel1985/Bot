package com.fidel.bot;

import com.fidel.bot.enumeration.Operation;
import com.fidel.bot.enumeration.Pair;
import com.fidel.bot.exception.EmptyResponseException;
import com.fidel.bot.exception.PlaceOrderException;
import com.fidel.bot.exception.InvalidSymbolsPairException;
import com.fidel.bot.service.StrategyService;
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
    private StrategyService strategyService;

    @Scheduled(initialDelayString = "${configuration.delay:10}000", fixedDelayString = "${configuration.schedule:1000}000")
    public void scheduledTask() {
        Operation operation = Operation.BUY; // on descend
        Pair pair = Pair.ETHUSD;
        double lot = 0.5;
        double price = 11.0;
        double step = 0.5;
        double spread = 0.004; // 2% for buy and 2% for sell
        double plannedProfit = 0.02;


        try {
            strategyService.staticStrategy(operation, pair, lot, price, step, plannedProfit);
        } catch (PlaceOrderException | EmptyResponseException | ParseException | InvalidSymbolsPairException | InterruptedException e) {
            LOG.error(e.getMessage());
            application.initiateShutdown(1);
        }
    }

}
