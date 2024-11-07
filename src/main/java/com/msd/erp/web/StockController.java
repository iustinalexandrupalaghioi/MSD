package com.msd.erp.web;

import com.msd.erp.domain.Article;
import com.msd.erp.domain.Stock;
import com.msd.erp.application.services.StockService;
import com.msd.erp.application.views.StockUpdateDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/stocks")
public class StockController {

    @Autowired
    private StockService stockService;

    // Get all stocks
    @GetMapping
    public ResponseEntity<List<Stock>> getAllStocks() {
        List<Stock> stocks = stockService.findAllStocks();
        return new ResponseEntity<>(stocks, HttpStatus.OK);
    }

    // Get stock by article id
    @GetMapping("/article/{id}")
    public ResponseEntity<Stock> getStockByArticleId(@PathVariable Long id) {
        Optional<Stock> stock = stockService.findByArticleId(id);
        return stock.map(s -> new ResponseEntity<>(s, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Create new stock
    @PostMapping("/create")
    public ResponseEntity<Stock> createStock(@RequestBody Article article) {
        Stock createdStock = stockService.createStock(article);
        return new ResponseEntity<>(createdStock, HttpStatus.CREATED);
    }

    // Update stock by article ID
    @PutMapping("/update/{articleId}")
    public ResponseEntity<Stock> updateStock(@PathVariable Long articleId, @RequestBody StockUpdateDTO stockUpdateDTO) {
        Optional<Stock> existingStockOptional = stockService.findByArticleId(articleId);

        if (existingStockOptional.isPresent()) {
            Stock existingStock = existingStockOptional.get();

            // Incoming quantity
            if (stockUpdateDTO.getIncomingQuantity() != null) {
                existingStock.setIncomingQuantity(
                        existingStock.getIncomingQuantity() + stockUpdateDTO.getIncomingQuantity());
            }

            // rented quantity
            if (stockUpdateDTO.getRentedQuantity() != null) {
                existingStock.setRentedQuantity(existingStock.getRentedQuantity() + stockUpdateDTO.getRentedQuantity());
                existingStock.setAvailableQuantity(
                        existingStock.getAvailableQuantity() - stockUpdateDTO.getRentedQuantity());
            }

            // transfer from incoming to available (receive)
            if (stockUpdateDTO.getReceivedQuantity() != null) {
                existingStock.setIncomingQuantity(
                        existingStock.getIncomingQuantity() - stockUpdateDTO.getReceivedQuantity());
                existingStock.setAvailableQuantity(
                        existingStock.getAvailableQuantity() + stockUpdateDTO.getReceivedQuantity());
            }

            // transfer from rented to available (return)
            if (stockUpdateDTO.getReturnedQuantity() != null) {
                existingStock
                        .setRentedQuantity(existingStock.getRentedQuantity() - stockUpdateDTO.getReturnedQuantity());
                existingStock
                        .setAvailableQuantity(
                                existingStock.getAvailableQuantity() + stockUpdateDTO.getReturnedQuantity());
            }

            stockService.updateCalculatedQuantities(existingStock);
            Stock updatedStock = stockService.save(existingStock);
            return new ResponseEntity<>(updatedStock, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Delete stock if article is deleted
    @DeleteMapping("/delete/article/{articleId}")
    public ResponseEntity<Void> deleteStockByArticleId(@PathVariable Long articleId) {
        stockService.deleteStockByArticleId(articleId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
