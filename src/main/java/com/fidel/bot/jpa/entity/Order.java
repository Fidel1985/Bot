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

    @Column(name = "converse_id")
    private Long converseId;

    @Column(name = "pair")
    private String pair;

    @Column(name = "operation")
    private String operation;

    @Column(name = "amount")
    private double amount;

    @Column(name = "price")
    private double price;

    @Column(name = "converse_price")
    private double conversePrice;

    @Column(name = "create_date")
    private Timestamp createDate;

    @Column(name = "done_date")
    private Timestamp doneDate;

    @Column(name = "close_date")
    private Timestamp closeDate;

    @Column(name = "closed")
    private boolean closed;

    @Column(name = "spread")
    private double spread;

    @Column(name = "profit")
    private double profit;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getConverseId() {
        return converseId;
    }

    public void setConverseId(Long converseId) {
        this.converseId = converseId;
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

    public double getConversePrice() {
        return conversePrice;
    }

    public void setConversePrice(double conversePrice) {
        this.conversePrice = conversePrice;
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

    public double getSpread() {
        return spread;
    }

    public void setSpread(double spread) {
        this.spread = spread;
    }

    public double getProfit() {
        return profit;
    }

    public void setProfit(double profit) {
        this.profit = profit;
    }

    public static Order from(OrderDTO dto) {
        Order order = new Order();
        order.setId(dto.getId());
        order.setPair(dto.getPair().toString());
        order.setOperation(dto.getOperation().toString());
        order.setAmount(dto.getAmount());
        order.setPrice(dto.getPrice());
        order.setCreateDate(dto.getCreateDate());
        order.setClosed(dto.isComplete());
        order.setSpread(dto.getSpread());
        order.setProfit(dto.getProfit());
        return order;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equal(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("converseId", converseId)
                .add("pair", pair)
                .add("operation", operation)
                .add("amount", amount)
                .add("price", price)
                .add("converse_price", conversePrice)
                .add("createDate", createDate)
                .add("doneDate", doneDate)
                .add("closeDate", closeDate)
                .add("closed", closed)
                .add("spread", spread)
                .add("profit", profit)
                .toString();
    }
}
