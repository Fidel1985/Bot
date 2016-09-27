package com.fidel.bot.jpa;

public enum Type {
    BUY("buy"), SELL("sell");
    private String value;

    Type(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
