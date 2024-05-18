package com.assessment.exercise3.controllers;

import com.assessment.exercise3.payloads.ApiResponse;
import com.assessment.exercise3.payloads.StockDto;
import com.assessment.exercise3.payloads.StockResponseDto;
import com.assessment.exercise3.services.StockService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stocks")
public class StockController {
    private final StockService stockService;

    @PostMapping("")
    public ResponseEntity<ApiResponse<StockResponseDto>> createStock(@Valid @RequestBody StockDto stockDto) {
        return stockService.createStock(stockDto);
    }

    @PutMapping("/{stockName}")
    public ResponseEntity<ApiResponse<StockResponseDto>> updateStock(@PathVariable String stockName,@Valid @RequestBody StockDto stockDto) {
        return stockService.updateStock(stockName, stockDto);
    }

    @GetMapping("")
    public ResponseEntity<ApiResponse<List<StockResponseDto>>> getAllStocks(@RequestParam int pageNo, @RequestParam int pageSize) {
        return stockService.getAllStocks(pageNo, pageSize);
    }

    @GetMapping("/{stockName}")
    public ResponseEntity<ApiResponse<StockResponseDto>> getStock(@PathVariable String stockName) {
        return stockService.getStock(stockName);
    }
}
