package com.fidel.bot.service.strategy;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

import com.fidel.bot.dto.OrderDTO;
import com.fidel.bot.enumeration.Operation;
import com.fidel.bot.enumeration.Pair;
import com.fidel.bot.exception.EmptyResponseException;
import com.fidel.bot.exception.InvalidParamsException;
import com.fidel.bot.exception.InvalidSymbolsPairException;
import com.fidel.bot.exception.PlaceOrderException;
import com.fidel.bot.jpa.entity.Grid;
import com.fidel.bot.jpa.entity.Order;
import com.fidel.bot.service.GridService;
import com.fidel.bot.service.OrderService;
import com.fidel.bot.service.ParsedOrderService;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractStaticStrategy {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractStaticStrategy.class);
    private static final BigDecimal RANGE_COEFF = new BigDecimal(0.95);
    private Pair pair;
    private BigDecimal step;
    private BigDecimal spread;
    private BigDecimal plannedProfit;
    protected BigDecimal lastPrice;

    @Autowired
    private ParsedOrderService parsedOrderService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private GridService gridService;

    public abstract boolean priceRelativelyLastPrice(BigDecimal price);
    public abstract BigDecimal getMultiplier(Order order);
    public abstract Operation getOperation();
    public abstract Operation getReverseOperation();

    public void initFields(Pair pair, BigDecimal step, BigDecimal spread, BigDecimal plannedProfit) {
        this.pair = pair;
        this.step = step;
        this.spread = spread;
        this.plannedProfit = plannedProfit;
    }

    public void createOrderGrid() throws EmptyResponseException, InvalidSymbolsPairException,
            ParseException, PlaceOrderException, InvalidParamsException, java.text.ParseException {
        List<Grid> gridItems = gridService.getGrid(getOperation(), pair);
        for (Grid item : gridItems) {
            if (isOrderPlacingAllowed(item.getPrice())) {
                OrderDTO orderDTO = parsedOrderService.placeOrder(getOperation(), pair, item.getAmount(), item.getPrice());
                orderDTO.setSpread(spread);
                orderDTO.setProfit(plannedProfit);
                LOG.info("placed {}", orderDTO);
                orderService.saveOrder(orderDTO);
                LOG.info("{}", parsedOrderService.obtainBalance());
            } else {
                LOG.debug("Cannot perform order placing with price {} and step {}", item.getPrice(), step);
            }
        }
    }

    public void checkFirstOrdersDone() throws EmptyResponseException, ParseException, PlaceOrderException,
            InvalidParamsException, java.text.ParseException {
        List<Order> openOrders = orderService.findByDoneDateNullAndPair(pair);
        List<Order> doneOrders = openOrders.stream().filter(x -> !priceRelativelyLastPrice(x.getPrice())).collect(Collectors.toList());

        for (Order doneOrder : doneOrders) {
            if (parsedOrderService.isOrderDone(doneOrder.getId())) { // server verifying
                BigDecimal conversePrice = (getMultiplier(doneOrder)).setScale(4, RoundingMode.FLOOR);
                OrderDTO converseOrder = parsedOrderService.placeOrder(getReverseOperation(), Pair.valueOf(doneOrder.getPair()),
                        doneOrder.getAmount(), conversePrice);
                LOG.info("placed {}", converseOrder);
                LOG.info("{}", parsedOrderService.obtainBalance());
                orderService.makeDoneFirstOrder(doneOrder.getId(), converseOrder.getId(), converseOrder.getCreateDate(), converseOrder.getPrice());
            }
        }
    }

    public void checkConverseOrdersDone() throws EmptyResponseException, ParseException {
        List<Order> openOrders = orderService.findByClosedFalseAndPairAndConverseIdNotNull(pair);
        List<Order> doneOrders = openOrders.stream().filter(x -> priceRelativelyLastPrice(x.getConversePrice())).collect(Collectors.toList());
        for (Order doneOrder : doneOrders) {
            if (parsedOrderService.isOrderDone(doneOrder.getConverseId())) { // server verifying
                orderService.makeDoneConverseOrder(doneOrder.getId());
                LOG.info("converseOrder closed {}", doneOrder);
            }
        }
    }

    private boolean isOrderPlacingAllowed(BigDecimal price) throws EmptyResponseException, InvalidSymbolsPairException,
            ParseException, PlaceOrderException {
        List<Order> nonConversedOrders = orderService.findByClosedFalseAndPair(pair);
        boolean isRangeOccupied = nonConversedOrders.stream().anyMatch(x ->
                (price.compareTo(x.getPrice().subtract(RANGE_COEFF.multiply(step))) > 0
                && price.compareTo(x.getPrice().add(RANGE_COEFF.multiply(step))) < 0));
        return priceRelativelyLastPrice(price) && !isRangeOccupied;
    }

    public BigDecimal getLastPrice() {
        return lastPrice;
    }

    public void setLastPrice(BigDecimal lastPrice) {
        this.lastPrice = lastPrice;
    }
}
