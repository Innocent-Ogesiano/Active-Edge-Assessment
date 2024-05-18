package com.assessment.exercise3.loader;

import com.assessment.exercise3.entities.Stock;
import com.assessment.exercise3.repositories.StockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class AppLoader implements CommandLineRunner {
    private final StockRepository stockRepository;

    @Override
    public void run(String... args) {
        List<Stock> stockList = Arrays.asList(
                new Stock("Amazon", 100.0),
                new Stock("Google", 150.0),
                new Stock("Facebook", 200.0)
        );
        stockList.forEach(stock -> {
            Optional<Stock> optionalStock = stockRepository.findByName(stock.getName());
            if (optionalStock.isEmpty()) {
                stockRepository.save(stock);
            } else
                log.info("Stock Already exists");
        });
        log.info("Stocks loaded successfully");
    }
}
