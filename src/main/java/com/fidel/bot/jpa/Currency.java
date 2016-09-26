package com.fidel.bot.jpa;

import com.google.common.base.MoreObjects;

public class Currency {
    private String name;
    private String available;
    private String orders;

    public java.lang.String getName() {
        return name;
    }

    public void setName(java.lang.String name) {
        this.name = name;
    }

    public java.lang.String getAvailable() {
        return available;
    }

    public void setAvailable(java.lang.String available) {
        this.available = available;
    }

    public java.lang.String getOrders() {
        return orders;
    }

    public void setOrders(java.lang.String orders) {
        this.orders = orders;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("name", name)
                .add("available", available)
                .add("orders", orders)
                .toString();
    }
}
