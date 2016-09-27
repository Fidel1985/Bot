package com.fidel.bot.jpa.entity;

import com.fidel.bot.dto.OrderDTO;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;


@Entity
@Table(name = "orders")
public class Orders {

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
    @Type(type = "date")
    private Date createDate;

    @Column(name = "close_date")
    @Type(type = "date")
    private Date closeDate;

    @Column(name = "release_date")
    @Type(type = "date")
    private Date releaseDate;

    @Column(name = "released")
    private boolean released;

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

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getCloseDate() {
        return closeDate;
    }

    public void setCloseDate(Date closeDate) {
        this.closeDate = closeDate;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public boolean isReleased() {
        return released;
    }

    public void setReleased(boolean released) {
        this.released = released;
    }

    public double getProfit() {
        return profit;
    }

    public void setProfit(double profit) {
        this.profit = profit;
    }

    public static Orders from(OrderDTO dto) {
        Orders orders = new Orders();
        orders.setId(dto.getId());
        orders.setPair(dto.getPair().toString());
        orders.setOperation(dto.getOperation().toString());
        orders.setAmount(dto.getAmount());
        orders.setPrice(dto.getPrice());
        orders.setPending(dto.getPending());
        orders.setCreateDate(new Date(dto.getCreateDate()));
        orders.setCloseDate(new Date());
        orders.setReleased(false);

        return orders;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Orders orders = (Orders) o;
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
                .add("closeDate", closeDate)
                .add("releaseDate", releaseDate)
                .add("released", released)
                .add("profit", profit)
                .toString();
    }
}
