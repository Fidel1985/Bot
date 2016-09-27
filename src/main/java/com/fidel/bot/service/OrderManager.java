package com.fidel.bot.service;

import com.fidel.bot.dto.OrderDTO;
import com.fidel.bot.jpa.OrderRepository;
import com.fidel.bot.jpa.entity.Orders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class OrderManager {

    @Autowired
    private OrderRepository orderRepository;

    @Transactional
    public Set<Orders> saveOrders(Set<OrderDTO> orderDTOs) {
        Set<Orders> ordersSet = orderDTOs.stream().map(Orders::from).collect(Collectors.toSet());
        orderRepository.save(ordersSet);
        return ordersSet;
    }
}
