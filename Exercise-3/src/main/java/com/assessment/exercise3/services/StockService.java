package com.assessment.exercise3.services;

import com.assessment.exercise3.payloads.ApiResponse;
import com.assessment.exercise3.payloads.StockDto;
import com.assessment.exercise3.payloads.StockResponseDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface StockService {
    ResponseEntity<ApiResponse<List<StockResponseDto>>> getAllStocks(int pageNo, int pageSize);
    ResponseEntity<ApiResponse<StockResponseDto>> getStock(String stockName);
    ResponseEntity<ApiResponse<StockResponseDto>> updateStock(String stockName, StockDto stockDto);
    ResponseEntity<ApiResponse<StockResponseDto>> createStock(StockDto stockDto);
}
