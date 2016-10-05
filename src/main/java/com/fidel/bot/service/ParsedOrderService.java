package com.fidel.bot.service;

import java.math.BigDecimal;
import java.util.List;

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
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ParsedOrderService {

    @Autowired
    private RequestService requestService;

    @Autowired
    private ParserService parserService;

    public boolean isOrderDone(long id) throws EmptyResponseException, ParseException {
        return parserService.parseOrderStatus(requestService.get_order(id)) == Status.DONE;
    }

    public BalanceDTO obtainBalance() throws EmptyResponseException, ParseException {
        return parserService.parseBalance(requestService.balance());
    }

    public OrderDTO placeOrder(Operation operation, Pair pair, double amount, BigDecimal price)
            throws EmptyResponseException, PlaceOrderException, ParseException, InvalidParamsException, java.text.ParseException {
        return parserService.parseOrder(requestService.place_order(operation.getValue(), amount, price, pair.getValue()), operation, pair);
    }

    public Boolean cancelOrder(long orderId) throws EmptyResponseException {
        return (Boolean) requestService.cancel_order(orderId);
    }

    public TickerDTO obtainTicker(Pair pair) throws EmptyResponseException, InvalidSymbolsPairException, ParseException, java.text.ParseException {
        return parserService.parseTicker(requestService.ticker(pair.getValue()), pair);
    }

    public List<OrderDTO> obtainOpenOrders(Operation operation, Pair pair) throws EmptyResponseException,
            InvalidSymbolsPairException, ParseException, PlaceOrderException, java.text.ParseException {
        return parserService.parseOpenOrders(requestService.open_orders(pair.getValue()), operation, pair);
    }
}
