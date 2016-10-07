package com.fidel.bot.dto;

import java.math.BigDecimal;

import com.fidel.bot.enumeration.Operation;
import com.fidel.bot.enumeration.Pair;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

public class GridDTO {

    @NotBlank(message = "Please, enter operation")
    @Length(max = 49)
    private String operation;

    @NotBlank(message = "Please, enter trading pair")
    @Length(max = 49)
    private String pair;

    @NotBlank(message = "Please, enter lot size")
    private double amount;

    @NotBlank(message = "Please, enter price")
    private BigDecimal price;

    @NotBlank(message = "Please, enter step")
    private BigDecimal step;

    @NotBlank(message = "Please, enter number of orders")
    private int depth;

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getPair() {
        return pair;
    }

    public void setPair(String pair) {
        this.pair = pair;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getStep() {
        return step;
    }

    public void setStep(BigDecimal step) {
        this.step = step;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }
}
