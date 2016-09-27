package com.fidel.bot.service;

import java.util.ArrayList;
import java.util.List;

import com.fidel.bot.exception.InvalidSymbolsPairException;
import com.fidel.bot.exception.PlaceOrderException;
import com.fidel.bot.jpa.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class Parser {
    private static final Logger LOG = LoggerFactory.getLogger(Parser.class);

    public Balance parseBalance(Object object) throws ParseException {
        Balance balance = new Balance();

        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(object.toString());
        balance.setTimestamp(Long.parseLong((String) jsonObject.get("timestamp")));
        balance.setUsername((String) jsonObject.get("username"));

        JSONObject currency = (JSONObject) jsonParser.parse(jsonObject.get("USD").toString());
        balance.getUsd().setName("USD");
        balance.getUsd().setAvailable((String) currency.get("available"));
        balance.getUsd().setOrders((String) currency.get("orders"));

        currency = (JSONObject) jsonParser.parse(jsonObject.get("BTC").toString());
        balance.getBtc().setName("BTC");
        balance.getBtc().setAvailable((String) currency.get("available"));
        balance.getBtc().setOrders((String) currency.get("orders"));

        currency = (JSONObject) jsonParser.parse(jsonObject.get("ETH").toString());
        balance.getEth().setName("ETH");
        balance.getEth().setAvailable((String) currency.get("available"));
        balance.getEth().setOrders((String) currency.get("orders"));

        return balance;
    }

    public Order parseOrder(Object object, Type type, Pair pair) throws ParseException, PlaceOrderException {
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(object.toString());
        if (jsonObject.get("error") != null) {
            throw new PlaceOrderException((String) jsonObject.get("error"));
        }
        Order order = new Order();
        order.setPair(pair);
        order.setAmount(Double.parseDouble((String) jsonObject.get("amount")));
        order.setPrice(Double.parseDouble((String) jsonObject.get("price")));
        order.setPending(Double.parseDouble((String) jsonObject.get("pending")));
        order.setId(Long.parseLong((String) jsonObject.get("id")));
        try {
            order.setTime((Long) jsonObject.get("time"));
        } catch (ClassCastException e) {
            order.setTime(Long.parseLong((String) jsonObject.get("time"))); // parsing open_orders time in String format
        }
        order.setComplete((Boolean) jsonObject.getOrDefault("complete", false));
        order.setType(type);
        return order;
    }

    public Ticker parseTicker(Object object, Pair pair) throws ParseException, InvalidSymbolsPairException{
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(object.toString());
        if (jsonObject.get("error") != null) {
            throw new InvalidSymbolsPairException((String) jsonObject.get("error"));
        }
        Ticker ticker = new Ticker();
        ticker.setPair(pair);
        ticker.setVolume((String) jsonObject.get("volume"));
        ticker.setVolume30d((String) jsonObject.get("volume30d"));
        ticker.setHigh(Double.parseDouble((String) jsonObject.get("high")));
        ticker.setLast(Double.parseDouble((String) jsonObject.get("last")));
        ticker.setLow(Double.parseDouble((String) jsonObject.get("low")));
        ticker.setAsk((Double) jsonObject.get("ask"));
        ticker.setBid((Double) jsonObject.get("bid"));
        ticker.setTimestamp(Long.parseLong((String) jsonObject.get("timestamp")));
        return ticker;
    }

    public List<Order> parseOpenOrders(Object object, Type type, Pair pair) throws ParseException, InvalidSymbolsPairException, PlaceOrderException {
        JSONParser jsonParser = new JSONParser();
        List<Order> orderList = new ArrayList<>();
        try {
            JSONArray jsonArray = (JSONArray) jsonParser.parse(object.toString());
            for(Object o: jsonArray) {
                orderList.add(parseOrder(o, type, pair));
            }
        } catch (ClassCastException e) {
            JSONObject jsonObject = (JSONObject) jsonParser.parse(object.toString());
            if (jsonObject.get("error") != null) {
                throw new InvalidSymbolsPairException((String) jsonObject.get("error"));
            }
        }
        //Arrays.stream(jsonArray.toArray()).map(x -> parseOrder(x, type, pair)).collect(Collectors.toList()); TODO find out exception handling
        return orderList;
    }
}
