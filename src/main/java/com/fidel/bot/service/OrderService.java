package com.fidel.bot.service;

import com.fidel.bot.dto.OrderDTO;
import com.fidel.bot.jpa.OrderRepository;
import com.fidel.bot.jpa.entity.Order;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Transactional
    public List<Order> saveOrders(List<OrderDTO> orderDTOs) {
        List<Order> ordersSet = orderDTOs.stream().map(Order::from).collect(Collectors.toList());
        return orderRepository.save(ordersSet);
    }

    @Transactional
    public Order saveOrder(OrderDTO orderDTO) {
        return orderRepository.save(Order.from(orderDTO));
    }

    @Transactional
    public void makeDoneFirstOrder(long id, long converseId, Timestamp doneDate, double conversePrice) {
        Order order = orderRepository.findOne(id);
        order.setConverseId(converseId);
        order.setDoneDate(doneDate);
        order.setConversePrice(conversePrice);
        orderRepository.save(order);
    }

    @Transactional
    public void makeDoneConverseOrder(long id) {
        Order order = orderRepository.findOne(id);
        order.setClosed(true);
        order.setCloseDate(new Timestamp(System.currentTimeMillis()));
        orderRepository.save(order);
    }

    @Transactional
    public void closeOrder(long id) {
        Order order = orderRepository.findOne(id);
        order.setClosed(true);
        orderRepository.save(order);
    }

    public List<Order> findAll() {
        return orderRepository.findAll();
    }
}
