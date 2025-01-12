package com.msd.erp.application.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.msd.erp.application.computations.StockCalculation;
import com.msd.erp.application.validations.DomainValidationService;
import com.msd.erp.application.views.StockUpdateDTO;
import com.msd.erp.domain.Article;
import com.msd.erp.domain.Stock;
import com.msd.erp.infrastructure.repositories.StockRepository;

import java.util.List;
import java.util.Optional;

@Service
public class StockService {

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private DomainValidationService validationService;

    public List<Stock> findAllStocks() {
        return stockRepository.findAll();
    }

    public Optional<Stock> findByArticle(Article article) {
        return stockRepository.findByArticle(article);
    }

    public Optional<Stock> findByArticleId(Long articleId) {
        return stockRepository.findByArticle_Articleid(articleId);
    }

    @Transactional
    public Stock createStock(Article article) {
        Stock stock = new Stock();
        stock.setArticle(article);

        // Save and return the created stock
        return stockRepository.save(stock);
    }

    @Transactional
    public Stock processStockUpdate(Stock existingStock, StockUpdateDTO stockUpdateDTO) {
        if (stockUpdateDTO.getIncomingQuantity() != null) {
            existingStock
                    .setIncomingQuantity(existingStock.getIncomingQuantity() + stockUpdateDTO.getIncomingQuantity());
        }

        if (stockUpdateDTO.getRentedQuantity() != null) {
            existingStock.setRentedQuantity(existingStock.getRentedQuantity() + stockUpdateDTO.getRentedQuantity());
            existingStock
                    .setAvailableQuantity(existingStock.getAvailableQuantity() - stockUpdateDTO.getRentedQuantity());
        }

        if (stockUpdateDTO.getReceivedQuantity() != null) {
            existingStock
                    .setIncomingQuantity(existingStock.getIncomingQuantity() - stockUpdateDTO.getReceivedQuantity());
            existingStock
                    .setAvailableQuantity(existingStock.getAvailableQuantity() + stockUpdateDTO.getReceivedQuantity());
        }

        if (stockUpdateDTO.getReturnedQuantity() != null) {
            existingStock.setRentedQuantity(existingStock.getRentedQuantity() - stockUpdateDTO.getReturnedQuantity());
            existingStock
                    .setAvailableQuantity(existingStock.getAvailableQuantity() + stockUpdateDTO.getReturnedQuantity());
        }

        if (stockUpdateDTO.getAvailableQuantity() != null) {
            existingStock
                    .setAvailableQuantity(existingStock.getAvailableQuantity() + stockUpdateDTO.getAvailableQuantity());
        }

        if (stockUpdateDTO.getSoldQuantity() != null) {
            existingStock
                    .setAvailableQuantity(existingStock.getAvailableQuantity() - stockUpdateDTO.getSoldQuantity());
        }

        if (stockUpdateDTO.getCanceledQuantity() != null) {
            existingStock
                    .setIncomingQuantity(existingStock.getIncomingQuantity() - stockUpdateDTO.getCanceledQuantity());
        }

        updateCalculatedQuantities(existingStock);
        return stockRepository.save(existingStock);
    }

    public void updateCalculatedQuantities(Stock stock) {
        stock.setTechnicalQuantity(StockCalculation.calculateTechnicalQuantity(stock.getIncomingQuantity(),
                stock.getRentedQuantity(), stock.getAvailableQuantity()));
        stock.setAvailableQuantity(StockCalculation.calculateAvailableQuantity(stock.getTechnicalQuantity(),
                stock.getIncomingQuantity(), stock.getRentedQuantity()));
    }

    public Stock save(Stock stock) {
        validationService.validateEntity(stock);
        return stockRepository.save(stock);
    }

    @Transactional
    public void deleteStockByArticleId(Long articleId) {
        Optional<Stock> stock = stockRepository.findByArticle_Articleid(articleId);
        stock.ifPresent(stockRepository::delete);
    }
}
