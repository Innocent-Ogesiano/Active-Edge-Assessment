package com.assessment.exercise3.services.impl;

import com.assessment.exercise3.entities.Stock;
import com.assessment.exercise3.exceptions.StockAlreadyExistsException;
import com.assessment.exercise3.exceptions.StockNotFoundException;
import com.assessment.exercise3.exceptions.StocksNotAvailableException;
import com.assessment.exercise3.payloads.ApiResponse;
import com.assessment.exercise3.payloads.StockDto;
import com.assessment.exercise3.payloads.StockResponseDto;
import com.assessment.exercise3.repositories.StockRepository;
import com.assessment.exercise3.services.StockService;
import com.assessment.exercise3.utils.AppUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.assessment.exercise3.utils.AppUtil.mapStockToStockResponseDto;

@Service
@RequiredArgsConstructor
public class StockServiceImpl implements StockService {
    private final StockRepository stockRepository;

    @Override
    public ResponseEntity<ApiResponse<List<StockResponseDto>>> getAllStocks(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Stock> stockPage = stockRepository.findAll(pageable);
        if (stockPage.hasContent()) {
            List<StockResponseDto> stockResponseDtos = stockPage.get()
                    .map(AppUtil::mapStockToStockResponseDto)
                    .toList();
            ApiResponse<List<StockResponseDto>> apiResponse = new ApiResponse<>(HttpStatus.OK.value(), "Success", stockResponseDtos);
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        }
        throw new StocksNotAvailableException("Stocks not available at the moment");
    }

    @Override
    public ResponseEntity<ApiResponse<StockResponseDto>> getStock(String stockName) {
        Stock stock = stockRepository.findByName(stockName)
                .orElseThrow(()-> new StockNotFoundException("Stock not found"));
        StockResponseDto stockResponseDto = mapStockToStockResponseDto(stock);

        ApiResponse<StockResponseDto> apiResponse = new ApiResponse<>(HttpStatus.OK.value(), "Success", stockResponseDto);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ApiResponse<StockResponseDto>> updateStock(String stockName, StockDto stockDto) {
        Stock stock = stockRepository.findByName(stockName)
                .orElseThrow(()-> new StockNotFoundException("Stock not found"));
        return saveStockAndReturnStockResponseDto(stockDto, stock);
    }

    private ResponseEntity<ApiResponse<StockResponseDto>> saveStockAndReturnStockResponseDto(StockDto stockDto, Stock stock) {
        stock.setName(stockDto.name());
        stock.setCurrentPrice(stockDto.currentPrice());
        stock = stockRepository.save(stock);

        StockResponseDto stockResponseDto = mapStockToStockResponseDto(stock);
        ApiResponse<StockResponseDto> apiResponse = new ApiResponse<>(HttpStatus.OK.value(), "Success", stockResponseDto);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ApiResponse<StockResponseDto>> createStock(StockDto stockDto) {
        stockRepository.findByName(stockDto.name())
                .ifPresent(stock -> {
                    throw new StockAlreadyExistsException("Stock Already exists");
                });
        Stock stock = new Stock();
        return saveStockAndReturnStockResponseDto(stockDto, stock);
    }
}
