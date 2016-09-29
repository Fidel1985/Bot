package com.fidel.bot;

import com.fidel.bot.enumeration.Operation;
import com.fidel.bot.enumeration.Pair;
import com.fidel.bot.exception.EmptyResponseException;
import com.fidel.bot.exception.InvalidSymbolsPairException;
import com.fidel.bot.exception.PlaceOrderException;
import com.fidel.bot.service.StrategyService;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class MainController {
    private static final Logger LOG = LoggerFactory.getLogger(MainController.class);

    @Autowired
    private Application application;

    @Autowired
    private StrategyService strategyService;

    @RequestMapping("/")
    public String index() {
/*        try {
            strategyService.staticStrategy(Operation.BUY, Pair.ETHBTC, 0.5, 11, 0.5, 3);
        } catch (PlaceOrderException | EmptyResponseException | ParseException | InvalidSymbolsPairException | InterruptedException e) {
            LOG.error(e.getMessage());
            application.initiateShutdown(1);
        }*/
        return "Greetings from Spring Boot!";
    }

}