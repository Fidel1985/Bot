package com.fidel.bot.jpa.entity;

import com.fidel.bot.dto.OrderDTO;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;


@Entity
@Table(name = "orders")
public class Order {

    @Id
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "pair")
    private String pair;

    @Column(name = "operation")
    private String operation;

    @Column(name = "amount")
    private double amount;

    @Column(name = "price")
    private double price;

    @Column(name = "pending")
    private double pending;

    @Column(name = "create_date")
    private Timestamp createDate;

    @Column(name = "done_date")
    private Timestamp doneDate;

    @Column(name = "close_date")
    private Timestamp closeDate;

    @Column(name = "closed")
    private boolean closed;

    @Column(name = "profit")
    private double profit;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPair() {
        return pair;
    }

    public void setPair(String pair) {
        this.pair = pair;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
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

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public Timestamp getDoneDate() {
        return doneDate;
    }

    public void setDoneDate(Timestamp doneDate) {
        this.doneDate = doneDate;
    }

    public Timestamp getCloseDate() {
        return closeDate;
    }

    public void setCloseDate(Timestamp closeDate) {
        this.closeDate = closeDate;
    }

    public boolean isClosed() {
        return closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    public double getProfit() {
        return profit;
    }

    public void setProfit(double profit) {
        this.profit = profit;
    }

    public static Order from(OrderDTO dto) {
        Order orders = new Order();
        orders.setId(dto.getId());
        orders.setPair(dto.getPair().toString());
        orders.setOperation(dto.getOperation().toString());
        orders.setAmount(dto.getAmount());
        orders.setPrice(dto.getPrice());
        orders.setPending(dto.getPending());
        orders.setCreateDate(dto.getCreateDate());
        orders.setDoneDate(new Timestamp(System.currentTimeMillis()));
        orders.setClosed(false);

        return orders;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order orders = (Order) o;
        return Objects.equal(id, orders.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("pair", pair)
                .add("operation", operation)
                .add("amount", amount)
                .add("price", price)
                .add("pending", pending)
                .add("createDate", createDate)
                .add("closeDate", doneDate)
                .add("releaseDate", closeDate)
                .add("released", closed)
                .add("profit", profit)
                .toString();
    }
}
