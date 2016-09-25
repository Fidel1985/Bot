package com.fidel.bot.controller.jpa;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.base.MoreObjects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Balance {
    private String available;
    private String orders;
    private String bonus;

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

    public String getBonus() {
        return bonus;
    }

    public void setBonus(String bonus) {
        this.bonus = bonus;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("available", available)
                .add("orders", orders)
                .add("bonus", bonus)
                .toString();
    }
}
