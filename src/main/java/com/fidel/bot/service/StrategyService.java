package com.fidel.bot.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
import com.fidel.bot.jpa.entity.Grid;
import com.fidel.bot.jpa.entity.Order;
import com.fidel.bot.service.strategy.AbstractStaticStrategy;
import com.fidel.bot.service.strategy.BuyStaticStrategy;
import com.fidel.bot.service.strategy.SellStaticStrategy;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class StrategyService {
    private static final Logger LOG = LoggerFactory.getLogger(StrategyService.class);

    @Autowired
    private
    ParsedOrderService parsedOrderService;

    @Value("${configuration.schedule}")
    private Long schedule;

    @Async
    public void staticStrategy(Operation operation, Pair pair, BigDecimal step, BigDecimal spread, BigDecimal plannedProfit)
            throws PlaceOrderException, EmptyResponseException, ParseException, InvalidSymbolsPairException, InterruptedException,
            InvalidParamsException, java.text.ParseException {

        AbstractStaticStrategy strategy;
        if(operation == Operation.BUY) {
            strategy = new BuyStaticStrategy(pair, step, spread, plannedProfit);
        } else {
            strategy = new SellStaticStrategy(pair, step, spread, plannedProfit);
        }

        while (true) {
            TickerDTO tickerDTO = parsedOrderService.obtainTicker(pair);
            LOG.info("{}", tickerDTO);
            strategy.createOrderGrid();
            strategy.setLastPrice(tickerDTO.getLast());
            strategy.checkFirstOrdersDone();
            strategy.checkConverseOrdersDone();
            Thread.sleep(schedule);
        }
    }

}
