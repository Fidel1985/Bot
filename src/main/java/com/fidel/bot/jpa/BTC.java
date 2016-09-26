package com.fidel.bot.jpa;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.base.MoreObjects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BTC {
    private String BTC;
    private Value value;

    public BTC(String BTC, Value value) {
        this.BTC = BTC;
        this.value = value;
    }

    public String getBTC() {
        return BTC;
    }

    public void setBTC(String BTC) {
        this.BTC = BTC;
    }

    public Value getValue() {
        return value;
    }

    public void setValue(Value value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("BTC", BTC)
                .add("value", value)
                .toString();
    }
}
