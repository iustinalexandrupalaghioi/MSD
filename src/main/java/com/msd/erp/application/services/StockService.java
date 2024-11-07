package com.msd.erp.application.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.msd.erp.application.computations.StockCalculation;
import com.msd.erp.domain.Article;
import com.msd.erp.domain.Stock;
import com.msd.erp.infrastructure.repositories.StockRepository;

import java.util.List;
import java.util.Optional;

@Service
public class StockService {

    @Autowired
    private StockRepository stockRepository;

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
    public Stock updateStock(Article article, Integer incomingQuantity, Integer rentedQuantity,
            Integer availableQuantity) {
        Optional<Stock> existingStock = stockRepository.findByArticle(article);

        if (existingStock.isPresent()) {
            Stock stock = existingStock.get();
            stock.setIncomingQuantity(incomingQuantity);
            stock.setRentedQuantity(rentedQuantity);
            stock.setAvailableQuantity(availableQuantity);
            updateCalculatedQuantities(stock);
            return stockRepository.save(stock);
        } else {
            Stock stock;
            stock = new Stock();
            stock.setArticle(article);

            stock.setAvailableQuantity(0);
            stock.setIncomingQuantity(0);
            stock.setRentedQuantity(0);
            stock.setTechnicalQuantity(0);
            return stockRepository.save(stock);
        }
    }

    public void updateCalculatedQuantities(Stock stock) {
        stock.setTechnicalQuantity(StockCalculation.calculateTechnicalQuantity(stock.getIncomingQuantity(),
                stock.getRentedQuantity(), stock.getAvailableQuantity()));
        stock.setAvailableQuantity(StockCalculation.calculateAvailableQuantity(stock.getTechnicalQuantity(),
                stock.getIncomingQuantity(), stock.getRentedQuantity()));
    }

    public Stock save(Stock stock) {
        return stockRepository.save(stock);
    }

    @Transactional
    public void deleteStockByArticleId(Long articleId) {
        Optional<Stock> stock = stockRepository.findByArticle_Articleid(articleId);
        stock.ifPresent(stockRepository::delete);
    }
}
