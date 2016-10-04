package com.fidel.bot.service.strategy;

import java.math.BigDecimal;

import com.fidel.bot.enumeration.Operation;
import com.fidel.bot.enumeration.Pair;
import com.fidel.bot.jpa.entity.Order;

public class BuyStaticStrategy extends AbstractStaticStrategy {

    public BuyStaticStrategy(Pair pair, BigDecimal step) {
        this.pair = pair;
        this.step = step;
    }

    @Override
    public boolean priceCorrectSide(BigDecimal price, BigDecimal lastPrice) {
        return price.compareTo(lastPrice) <= 0;
    }

    @Override
    public BigDecimal getMultiplier(Order doneOrder) {
        return doneOrder.getPrice().multiply(new BigDecimal(1).add(doneOrder.getProfit().add(doneOrder.getSpread())));
    }

    @Override
    public Operation getOperation(){
        return Operation.SELL;
    }
}
