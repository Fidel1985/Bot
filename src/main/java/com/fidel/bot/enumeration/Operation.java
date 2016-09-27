package com.fidel.bot.enumeration;

public enum Operation {
    BUY("buy"), SELL("sell");
    private String value;

    Operation(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
