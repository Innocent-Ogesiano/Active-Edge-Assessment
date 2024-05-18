package com.assessment.exercise3.repositories;

import com.assessment.exercise3.entities.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Long> {
    Optional<Stock> findByName(String stockName);
}
