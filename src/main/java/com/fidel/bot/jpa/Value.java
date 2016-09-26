package com.fidel.bot.jpa;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.base.MoreObjects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Value {

    private String available;
    private String orders;

    public Value(String available, String orders) {
        this.available = available;
        this.orders = orders;
    }

    public String getAvailable() {
        return available;
    }

    public void setAvailable(String available) {
        this.available = available;
    }

    public String getOrders() {
        return orders;
    }

    public void setOrders(String orders) {
        this.orders = orders;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("available", available)
                .add("orders", orders)
                .toString();
    }
}
