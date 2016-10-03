package com.fidel.bot.jpa;


import java.util.List;

import com.fidel.bot.jpa.entity.Grid;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GridRepository extends JpaRepository<Grid, Long> {
    List<Grid> findByOperationAndPair(String operation, String pair);
}
