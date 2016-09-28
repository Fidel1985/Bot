package com.fidel.bot.dto;

import java.sql.Timestamp;

import com.fidel.bot.enumeration.Operation;
import com.fidel.bot.enumeration.Pair;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

public class OrderDTO {
    private long id;
    private Pair pair;
    private double amount;
    private double price;
    private double pending;
    private Timestamp createDate;
    private boolean complete;
    private Operation operation;

    public Pair getPair() {
        return pair;
    }

    public void setPair(Pair pair) {
        this.pair = pair;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getPending() {
        return pending;
    }

    public void setPending(double pending) {
        this.pending = pending;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderDTO orderDTO = (OrderDTO) o;
        return id == orderDTO.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("pair", pair)
                .add("amount", amount)
                .add("price", price)
                .add("pending", pending)
                .add("id", id)
                .add("createDate", createDate)
                .add("complete", complete)
                .add("operation", operation)
                .toString();
    }
}
