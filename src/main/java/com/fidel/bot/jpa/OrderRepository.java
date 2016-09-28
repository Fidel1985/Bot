package com.fidel.bot.jpa;

import com.fidel.bot.jpa.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
