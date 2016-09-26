package com.fidel.bot.service;

import com.fidel.bot.jpa.Balance;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class Parser {
    private static final Logger LOG = LoggerFactory.getLogger(Parser.class);

    public Balance parseBalance(Object object) {

        Balance balance = new Balance();
        try {
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(object.toString());
            balance.setTimestamp((String) jsonObject.get("timestamp"));
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

        } catch (ParseException e) {
            LOG.error("Fail to parse balance JSON {}", e.getMessage());
        }

        return balance;
    }
}
