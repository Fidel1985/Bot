package com.fidel.bot.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import com.fidel.bot.enumeration.Operation;
import com.fidel.bot.enumeration.Pair;
import com.fidel.bot.jpa.GridRepository;
import com.fidel.bot.jpa.entity.Grid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class GridService {

    @Autowired
    private GridRepository gridRepository;

    @Transactional
    public void createGrid(Operation operation, Pair pair, double amount, BigDecimal price, BigDecimal step, int depth) {
        deleteGrid(operation, pair);
        for (int i = 0; i < depth; i++) {
            Grid gridItem = new Grid();
            gridItem.setPair(pair.toString());
            gridItem.setOperation(operation.toString());
            gridItem.setAmount(amount);
            gridItem.setPrice(price.setScale(8, BigDecimal.ROUND_HALF_UP));
            gridItem.setCreateDate(new Timestamp(System.currentTimeMillis()));
            gridRepository.save(gridItem);
            if (operation == Operation.BUY) {
                price = price.subtract(step);
            } else {
                price = price.add(step);
            }
        }
    }

    public List<Grid> getGrid(Operation operation, Pair pair) {
        return gridRepository.findByOperationAndPair(operation.toString(), pair.toString());
    }


    @Transactional
    private void deleteGrid(Operation operation, Pair pair) {
        gridRepository.delete(gridRepository.findByOperationAndPair(operation.toString(), pair.toString()));
    }
}
