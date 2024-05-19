package com.assessment.exercise3.services.impl;

import com.assessment.exercise3.entities.Stock;
import com.assessment.exercise3.exceptions.StockAlreadyExistsException;
import com.assessment.exercise3.exceptions.StockNotFoundException;
import com.assessment.exercise3.exceptions.StocksNotAvailableException;
import com.assessment.exercise3.payloads.ApiResponse;
import com.assessment.exercise3.payloads.StockDto;
import com.assessment.exercise3.payloads.StockResponseDto;
import com.assessment.exercise3.repositories.StockRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StockServiceImplTest {
    @Mock
    private StockRepository stockRepository;
    @InjectMocks
    private StockServiceImpl stockService;

    private Stock stock;

    @BeforeEach
    void setUp() {
        stock = new Stock();
        stock.setId(1L);
        stock.setName("Facebook");
        stock.setCurrentPrice(200.0);
    }

    @Test
    void getAllStocks() {
        Page<Stock> stockPage = new PageImpl<>(List.of(stock));

        int pageNo = 0;
        int pageSize = 5;
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        when(stockRepository.findAll(pageable)).thenReturn(stockPage);

        ResponseEntity<ApiResponse<List<StockResponseDto>>> response = stockService.getAllStocks(pageNo, pageSize);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        ApiResponse<List<StockResponseDto>> apiResponse = response.getBody();

        assertThat(apiResponse).isNotNull();
        assertThat(apiResponse.getStatusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(apiResponse.getMessage()).isEqualTo("Successful");

        List<StockResponseDto> stockResponseDtos = apiResponse.getData();

        assertThat(stockResponseDtos)
                .isNotNull()
                .isInstanceOf(List.class)
                .isNotEmpty();
        assertThat(stockResponseDtos.size()).isEqualTo(1);
        assertThat(stockResponseDtos.get(0).id()).isEqualTo(stock.getId());
    }

    @Test
    void shouldThrowStocksNotAvailableExceptionWhenThereIsNoStockAvailable() {
        int pageNo = 0;
        int pageSize = 5;
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        when(stockRepository.findAll(pageable)).thenReturn(new PageImpl<>(new ArrayList<>()));

        assertThrows(StocksNotAvailableException.class, ()-> stockService.getAllStocks(pageNo, pageSize));
    }

    @Test
    void getStock() {
        String stockName = "Facebook";

        when(stockRepository.findByName(stockName)).thenReturn(Optional.of(stock));

        ResponseEntity<ApiResponse<StockResponseDto>> response = stockService.getStock(stockName);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        ApiResponse<StockResponseDto> apiResponse = response.getBody();

        assertThat(apiResponse).isNotNull();
        assertThat(apiResponse.getStatusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(apiResponse.getMessage()).isEqualTo("Successful");

        StockResponseDto stockResponseDto = apiResponse.getData();

        assertThat(stockResponseDto)
                .isNotNull()
                .isInstanceOf(StockResponseDto.class);
        assertThat(stockResponseDto.id()).isEqualTo(stock.getId());
        assertThat(stockResponseDto.name()).isEqualTo(stock.getName());
        assertThat(stockResponseDto.currentPrice()).isEqualTo(stock.getCurrentPrice());
    }

    @Test
    void shouldThrowStockNotFoundExceptionWhenStockWithStockNameIsNotFound() {
        String stockName = "Google";

        when(stockRepository.findByName(stockName)).thenReturn(Optional.empty());

        assertThrows(StockNotFoundException.class, ()-> stockService.getStock(stockName));
    }

    @Test
    void updateStock() {
        String stockName = "Facebook";
        StockDto stockDto = new StockDto(stockName, 250.0);

        when(stockRepository.findByName(stockName)).thenReturn(Optional.of(stock));
        when(stockRepository.save(any(Stock.class))).thenReturn(stock);

        ResponseEntity<ApiResponse<StockResponseDto>> response = stockService.updateStock(stockName, stockDto);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        ApiResponse<StockResponseDto> apiResponse = response.getBody();

        assertThat(apiResponse).isNotNull();
        assertThat(apiResponse.getStatusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(apiResponse.getMessage()).isEqualTo("Successful");

        StockResponseDto stockResponseDto = apiResponse.getData();

        assertThat(stockResponseDto)
                .isNotNull()
                .isInstanceOf(StockResponseDto.class);
        assertThat(stockResponseDto.id()).isEqualTo(stock.getId());
        assertThat(stockResponseDto.name()).isEqualTo(stock.getName());
        assertThat(stockResponseDto.currentPrice()).isNotEqualTo(200.0);
        assertThat(stockResponseDto.currentPrice()).isEqualTo(250.0);
    }

    @Test
    void shouldThrowStockNotFoundExceptionWhenStockWithStockNameIsNotFoundForUpdate() {
        String stockName = "Google";
        StockDto stockDto = new StockDto(stockName, 250.0);

        when(stockRepository.findByName(stockName)).thenReturn(Optional.empty());

        assertThrows(StockNotFoundException.class, ()-> stockService.updateStock(stockName, stockDto));
    }

    @Test
    void createStock() {
        StockDto stockDto = new StockDto("Facebook", 200.0);

        when(stockRepository.findByName(stockDto.name())).thenReturn(Optional.empty());
        when(stockRepository.save(any(Stock.class))).thenReturn(stock);

        ResponseEntity<ApiResponse<StockResponseDto>> response = stockService.createStock(stockDto);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        ApiResponse<StockResponseDto> apiResponse = response.getBody();

        assertThat(apiResponse).isNotNull();
        assertThat(apiResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(apiResponse.getMessage()).isEqualTo("Successful");

        StockResponseDto stockResponseDto = apiResponse.getData();

        assertThat(stockResponseDto)
                .isNotNull()
                .isInstanceOf(StockResponseDto.class);
        assertThat(stockResponseDto.id()).isEqualTo(stock.getId());
        assertThat(stockResponseDto.name()).isEqualTo(stock.getName());
        assertThat(stockResponseDto.currentPrice()).isEqualTo(stock.getCurrentPrice());
    }

    @Test
    void shouldThrowStockAlreadyExistsWhenCreatingStockThatAlreadyExists() {
        StockDto stockDto = new StockDto("Facebook", 200.0);

        when(stockRepository.findByName(stockDto.name())).thenReturn(Optional.of(stock));

        assertThrows(StockAlreadyExistsException.class, ()-> stockService.createStock(stockDto));
    }
}