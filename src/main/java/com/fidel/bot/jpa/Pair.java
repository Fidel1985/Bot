package com.fidel.bot.jpa;

public enum Pair {
    BTCUSD("BTC/USD"), ETHUSD("ETH/USD"), ETHBTC("ETH/BTC");
    private String value;

    Pair(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
