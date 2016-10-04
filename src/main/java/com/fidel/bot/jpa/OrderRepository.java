package com.fidel.bot.jpa;

import java.util.List;

import com.fidel.bot.jpa.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByClosedFalseAndPair(String pair);
    List<Order> findByClosedFalseAndPairAndConverseIdNotNull(String pair);
    List<Order> findByDoneDateNullAndPair(String pair);
}
