package com.fidel.bot.dto;

import com.google.common.base.MoreObjects;

public class CurrencyDTO {
    private String available;
    private String orders;

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
                .add("available", available)
                .add("orders", orders)
                .toString();
    }
}
