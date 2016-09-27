package com.fidel.bot.service;

import com.fidel.bot.exception.EmptyResponseException;
import com.fidel.bot.exception.PlaceOrderException;
import com.fidel.bot.jpa.*;
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
        Order order = new Order();
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(object.toString());
        if (jsonObject.get("error") != null) {
            throw new PlaceOrderException((String) jsonObject.get("error"));
        }
        order.setPair(pair);
        order.setAmount(Double.parseDouble((String) jsonObject.get("amount")));
        order.setPrice(Double.parseDouble((String) jsonObject.get("price")));
        order.setPending(Double.parseDouble((String) jsonObject.get("pending")));
        order.setId(Long.parseLong((String) jsonObject.get("id")));
        order.setTime((Long) jsonObject.get("time"));
        order.setComplete((Boolean) jsonObject.get("complete"));
        order.setType(type);
        return order;
    }

    public Ticker parseTicker(Object object, Pair pair) {
        Ticker ticker = new Ticker();
        try {
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(object.toString());
            ticker.setPair(pair);
            ticker.setVolume((String) jsonObject.get("volume"));
            ticker.setVolume30d((String) jsonObject.get("volume30d"));
            ticker.setHigh(Double.parseDouble((String) jsonObject.get("high")));
            ticker.setLast(Double.parseDouble((String) jsonObject.get("last")));
            ticker.setLow(Double.parseDouble((String) jsonObject.get("low")));
            ticker.setAsk((Double) jsonObject.get("ask"));
            ticker.setBid((Double) jsonObject.get("bid"));
            ticker.setTimestamp(Long.parseLong((String) jsonObject.get("timestamp")));

        } catch (ParseException e) {
            LOG.error("Fail to parse order JSON {}", e.getMessage());
        }

        return ticker;
    }
}
