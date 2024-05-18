package com.assessment.exercise3.utils;

import com.assessment.exercise3.entities.Stock;
import com.assessment.exercise3.payloads.StockDto;
import com.assessment.exercise3.payloads.StockResponseDto;

public class AppUtil {

    private AppUtil() {}

    public static Stock mapStockDtoToStock (StockDto stockDto) {
        Stock stock = new Stock();
        stock.setName(stockDto.name());
        stock.setCurrentPrice(stockDto.currentPrice());
        return stock;
    }

    public static StockResponseDto mapStockToStockResponseDto (Stock stock) {
        return new StockResponseDto(stock.getId(), stock.getName(), stock.getCurrentPrice());
    }
}
