package com.fidel.bot.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fidel.bot.enumeration.Operation;
import com.fidel.bot.enumeration.Pair;
import com.fidel.bot.enumeration.Status;
import com.fidel.bot.exception.InvalidSymbolsPairException;
import com.fidel.bot.exception.PlaceOrderException;
import com.fidel.bot.dto.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ParserService {
    private static final Logger LOG = LoggerFactory.getLogger(ParserService.class);

    public BalanceDTO parseBalance(Object object) throws ParseException {
        BalanceDTO balanceDTO = new BalanceDTO();

        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(object.toString());
        balanceDTO.setTimestamp(Long.parseLong((String) jsonObject.get("timestamp")));
        balanceDTO.setUsername((String) jsonObject.get("username"));

        JSONObject currency = (JSONObject) jsonParser.parse(jsonObject.get("USD").toString());
        balanceDTO.getUsd().setAvailable((String) currency.get("available"));
        balanceDTO.getUsd().setOrders((String) currency.get("orders"));

        currency = (JSONObject) jsonParser.parse(jsonObject.get("BTC").toString());
        balanceDTO.getBtc().setAvailable((String) currency.get("available"));
        balanceDTO.getBtc().setOrders((String) currency.get("orders"));

        currency = (JSONObject) jsonParser.parse(jsonObject.get("ETH").toString());
        balanceDTO.getEth().setAvailable((String) currency.get("available"));
        balanceDTO.getEth().setOrders((String) currency.get("orders"));

        return balanceDTO;
    }

    public OrderDTO parseOrder(Object object, Operation operation, Pair pair) throws ParseException, PlaceOrderException {
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(object.toString());
        if (jsonObject.get("error") != null) {
            throw new PlaceOrderException((String) jsonObject.get("error"));
        }
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setPair(pair);
        orderDTO.setAmount(Double.parseDouble((String) jsonObject.get("amount")));
        orderDTO.setPrice(Double.parseDouble((String) jsonObject.get("price")));
        orderDTO.setPending(Double.parseDouble((String) jsonObject.get("pending")));
        orderDTO.setId(Long.parseLong((String) jsonObject.get("id")));
        try {
            orderDTO.setCreateDate(new Timestamp((Long) jsonObject.get("time")));
        } catch (ClassCastException e) {
            orderDTO.setCreateDate(new Timestamp(Long.parseLong((String) jsonObject.get("time")))); // parsing open_orders time in String format
        }
        orderDTO.setComplete((Boolean) jsonObject.getOrDefault("complete", false));
        orderDTO.setOperation(operation);
        return orderDTO;
    }

    public Status parseOrderStatus(Object object) throws ParseException {
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(object.toString());
        String status = (String) jsonObject.get("status");
        return Arrays.stream(Status.values()).filter(x->x.getValue().equals(status)).findFirst().orElse(null);
    }

    public TickerDTO parseTicker(Object object, Pair pair) throws ParseException, InvalidSymbolsPairException{
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(object.toString());
        if (jsonObject.get("error") != null) {
            throw new InvalidSymbolsPairException((String) jsonObject.get("error"));
        }
        TickerDTO tickerDTO = new TickerDTO();
        tickerDTO.setPair(pair);
        tickerDTO.setVolume((String) jsonObject.get("volume"));
        tickerDTO.setVolume30d((String) jsonObject.get("volume30d"));
        tickerDTO.setHigh(Double.parseDouble((String) jsonObject.get("high")));
        tickerDTO.setLast(Double.parseDouble((String) jsonObject.get("last")));
        tickerDTO.setLow(Double.parseDouble((String) jsonObject.get("low")));
        tickerDTO.setAsk((Double) jsonObject.get("ask"));
        tickerDTO.setBid((Double) jsonObject.get("bid"));
        tickerDTO.setTimestamp(Long.parseLong((String) jsonObject.get("timestamp")));
        return tickerDTO;
    }

    public List<OrderDTO> parseOpenOrders(Object object, Operation operation, Pair pair) throws ParseException, InvalidSymbolsPairException, PlaceOrderException {
        JSONParser jsonParser = new JSONParser();
        List<OrderDTO> orders = new ArrayList<>();
        try {
            JSONArray jsonArray = (JSONArray) jsonParser.parse(object.toString());
            for(Object o: jsonArray) {
                orders.add(parseOrder(o, operation, pair));
            }
        } catch (ClassCastException e) {
            JSONObject jsonObject = (JSONObject) jsonParser.parse(object.toString());
            if (jsonObject.get("error") != null) {
                throw new InvalidSymbolsPairException((String) jsonObject.get("error"));
            }
        }
        //Arrays.stream(jsonArray.toArray()).map(x -> parseOrder(x, operation, pair)).collect(Collectors.toList()); TODO find out exception handling
        return orders;
    }
}
