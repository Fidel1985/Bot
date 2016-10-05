package com.fidel.bot.service.strategy;

import java.math.BigDecimal;

import com.fidel.bot.enumeration.Operation;
import com.fidel.bot.jpa.entity.Order;
import org.springframework.stereotype.Component;

@Component
public class SellStaticStrategy extends AbstractStaticStrategy {

    @Override
    public boolean priceRelativelyLastPrice(BigDecimal price) {
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
