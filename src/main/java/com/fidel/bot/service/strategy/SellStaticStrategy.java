package com.fidel.bot.service.strategy;

import java.math.BigDecimal;

import com.fidel.bot.enumeration.Operation;
import com.fidel.bot.enumeration.Pair;
import com.fidel.bot.jpa.entity.Order;

public class SellStaticStrategy extends AbstractStaticStrategy {

    public SellStaticStrategy(Pair pair, BigDecimal step, BigDecimal spread, BigDecimal plannedProfit) {
        this.pair = pair;
        this.step = step;
        this.spread = spread;
        this.plannedProfit = plannedProfit;
    }

    @Override
    public boolean priceCorrectSide(BigDecimal price, BigDecimal lastPrice) {
        return price.compareTo(lastPrice) >= 0;
    }

    @Override
    public BigDecimal getMultiplier(Order order) {
        return order.getPrice().multiply(new BigDecimal(1).subtract(order.getProfit().subtract(order.getSpread())));
    }

    @Override
    public Operation getOperation() {
        return Operation.SELL;
    }

    @Override
    public Operation getReverseOperation() {
        return Operation.BUY;
    }

}
