package com.fidel.bot;

import javax.validation.Valid;

import java.math.BigDecimal;

import com.fidel.bot.dto.GridDTO;
import com.fidel.bot.enumeration.Operation;
import com.fidel.bot.enumeration.Pair;
import com.fidel.bot.exception.EmptyResponseException;
import com.fidel.bot.exception.InvalidParamsException;
import com.fidel.bot.exception.InvalidSymbolsPairException;
import com.fidel.bot.exception.PlaceOrderException;
import com.fidel.bot.service.GridService;
import com.fidel.bot.service.StrategyService;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@Controller
public class MainController {
    private static final Logger LOG = LoggerFactory.getLogger(MainController.class);

    @Autowired
    private Application application;

    @Autowired
    private GridService gridService;

    @Autowired
    private StrategyService strategyService;

    @RequestMapping("/")
    public String index() {
        return "Greetings from Spring Boot!";
    }

    @RequestMapping(value = "/static", method = RequestMethod.GET)
    public String getStrategyPage() {
        return "static_strategy";
    }

    @RequestMapping(value = "/static", method = RequestMethod.POST)
    public String setStrategy(@ModelAttribute("gridDTO") @Valid GridDTO gridDTO, BindingResult bindingResult, Model model) {
        if (!model.containsAttribute("gridDTO")) {
            model.addAttribute("gridDTO", new GridDTO());
        }

        /*passwordChangeValidator.validate(passwordChangeDTO, bindingResult);
        if(bindingResult.hasErrors()){
            model.addAttribute("passwordChangeDTO", passwordChangeDTO);
            return "change_password";
        }*/



        return "static_strategy";
    }


    @RequestMapping("/static/btc")
    public String staticStrategyBTC() {
        Operation operation = Operation.SELL; // on ascend
        Pair pair = Pair.BTCUSD;
        double amount = 0.016;
        BigDecimal price = new BigDecimal(616.0001);
        BigDecimal step = new BigDecimal(15);
        int depth = 2;

        return start(operation, pair, amount, price, step, depth);
    }

    @RequestMapping("/static/eth")
    public String staticStrategyETH() {
        Operation operation = Operation.BUY; // on descend
        Pair pair = Pair.ETHUSD;
        double amount = 0.5;
        BigDecimal price = new BigDecimal(13.26000001);
        BigDecimal step = new BigDecimal(0.5);
        int depth = 2;

        return start(operation, pair, amount, price, step, depth);
    }

    private String start(Operation operation, Pair pair, double amount, BigDecimal price, BigDecimal step, int depth) {
        BigDecimal spread = new BigDecimal(0.004); // 0.2% for buy and 0.2% for sell
        BigDecimal plannedProfit = new BigDecimal(0.02);

        gridService.createGrid(operation, pair, amount, price, step, depth);

        try {
            strategyService.staticStrategy(operation, pair, amount, step, spread, plannedProfit);
        } catch (InvalidParamsException | EmptyResponseException | InvalidSymbolsPairException e) {
            LOG.warn(e.getMessage());
        }
        catch (PlaceOrderException | ParseException | InterruptedException | java.text.ParseException e) {
            LOG.error(e.getMessage());
            application.initiateShutdown(1);
        }
        return "strategy " + pair + " initialized";
    }
}