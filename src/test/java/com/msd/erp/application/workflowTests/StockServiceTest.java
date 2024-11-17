package com.msd.erp.application.workflowTests;

import com.msd.erp.application.computations.StockCalculation;
import com.msd.erp.application.services.StockService;
import com.msd.erp.application.validations.DomainValidationService;
import com.msd.erp.application.views.StockUpdateDTO;
import com.msd.erp.domain.Article;
import com.msd.erp.domain.Stock;
import com.msd.erp.infrastructure.repositories.StockRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;

class StockServiceTest {

    @Mock
    private StockRepository stockRepository;

    @Mock
    private DomainValidationService validationService;

    @InjectMocks
    private StockService stockService;

    private Stock existingStock;
    private Article mockArticle;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mockArticle = new Article();
        mockArticle.setArticleid(1L);

        existingStock = new Stock();
        existingStock.setArticle(mockArticle);
        existingStock.setIncomingQuantity(10);
        existingStock.setRentedQuantity(5);
        existingStock.setAvailableQuantity(15);
        existingStock.setTechnicalQuantity(StockCalculation.calculateTechnicalQuantity(
                existingStock.getIncomingQuantity(),
                existingStock.getRentedQuantity(),
                existingStock.getAvailableQuantity()));
    }

    @Test
    void save_ShouldValidateAndSaveStock() {
        when(stockRepository.save(existingStock)).thenReturn(existingStock);

        Stock savedStock = stockService.save(existingStock);

        verify(validationService, times(1)).validateEntity(existingStock);
        verify(stockRepository, times(1)).save(existingStock);

        assertEquals(existingStock.getIncomingQuantity(), savedStock.getIncomingQuantity());
        assertEquals(existingStock.getRentedQuantity(), savedStock.getRentedQuantity());
        assertEquals(existingStock.getAvailableQuantity(), savedStock.getAvailableQuantity());
        assertEquals(existingStock.getTechnicalQuantity(), savedStock.getTechnicalQuantity());
    }

    @Test
    void findAllStocks_ShouldReturnAllStocks() {
        stockService.findAllStocks();
        verify(stockRepository, times(1)).findAll();
    }

    @Test
    void findByArticle_ShouldReturnStockIfExists() {
        when(stockRepository.findByArticle(mockArticle)).thenReturn(Optional.of(existingStock));

        Optional<Stock> foundStock = stockService.findByArticle(mockArticle);

        assertTrue(foundStock.isPresent());
        assertEquals(existingStock, foundStock.get());
        verify(stockRepository, times(1)).findByArticle(mockArticle);
    }

    @Test
    void findByArticleId_ShouldReturnStockIfExists() {
        when(stockRepository.findByArticle_Articleid(1L)).thenReturn(Optional.of(existingStock));

        Optional<Stock> foundStock = stockService.findByArticleId(1L);

        assertTrue(foundStock.isPresent());
        assertEquals(existingStock, foundStock.get());
        verify(stockRepository, times(1)).findByArticle_Articleid(1L);
    }

    @Test
    void processStockUpdate_ShouldHandleIncomingQuantityUpdate() {
        StockUpdateDTO stockUpdateDTO = new StockUpdateDTO();
        stockUpdateDTO.setIncomingQuantity(5);

        when(stockRepository.save(existingStock)).thenReturn(existingStock);

        Stock updatedStock = stockService.processStockUpdate(existingStock, stockUpdateDTO);

        assertEquals(15, updatedStock.getIncomingQuantity());
        assertEquals(5, updatedStock.getRentedQuantity());
        assertEquals(15, updatedStock.getAvailableQuantity());
        assertEquals(35, updatedStock.getTechnicalQuantity());
        verify(stockRepository, times(1)).save(existingStock);
    }

    @Test
    void processStockUpdate_ShouldHandleRentedQuantityUpdate() {
        StockUpdateDTO stockUpdateDTO = new StockUpdateDTO();
        stockUpdateDTO.setRentedQuantity(3);

        when(stockRepository.save(existingStock)).thenReturn(existingStock);

        Stock updatedStock = stockService.processStockUpdate(existingStock, stockUpdateDTO);

        assertEquals(10, updatedStock.getIncomingQuantity());
        assertEquals(8, updatedStock.getRentedQuantity());
        assertEquals(12, updatedStock.getAvailableQuantity());
        assertEquals(30, updatedStock.getTechnicalQuantity());
        verify(stockRepository, times(1)).save(existingStock);
    }

    @Test
    void processStockUpdate_ShouldHandleReceivedQuantityUpdate() {
        StockUpdateDTO stockUpdateDTO = new StockUpdateDTO();
        stockUpdateDTO.setReceivedQuantity(5);

        when(stockRepository.save(existingStock)).thenReturn(existingStock);

        Stock updatedStock = stockService.processStockUpdate(existingStock, stockUpdateDTO);

        assertEquals(5, updatedStock.getIncomingQuantity());
        assertEquals(5, updatedStock.getRentedQuantity());
        assertEquals(20, updatedStock.getAvailableQuantity());
        assertEquals(30, updatedStock.getTechnicalQuantity());
        verify(stockRepository, times(1)).save(existingStock);
    }

    @Test
    void processStockUpdate_ShouldHandleReturnedQuantityUpdate() {
        StockUpdateDTO stockUpdateDTO = new StockUpdateDTO();
        stockUpdateDTO.setReturnedQuantity(2);

        when(stockRepository.save(existingStock)).thenReturn(existingStock);

        Stock updatedStock = stockService.processStockUpdate(existingStock, stockUpdateDTO);

        assertEquals(10, updatedStock.getIncomingQuantity());
        assertEquals(3, updatedStock.getRentedQuantity());
        assertEquals(17, updatedStock.getAvailableQuantity());
        assertEquals(30, updatedStock.getTechnicalQuantity());
        verify(stockRepository, times(1)).save(existingStock);
    }

    @Test
    void processStockUpdate_ShouldRecalculateTechnicalQuantityCorrectly() {
        StockUpdateDTO stockUpdateDTO = new StockUpdateDTO();
        stockUpdateDTO.setIncomingQuantity(5);

        when(stockRepository.save(existingStock)).thenReturn(existingStock);

        Stock updatedStock = stockService.processStockUpdate(existingStock, stockUpdateDTO);

        int expectedTechnicalQuantity = StockCalculation.calculateTechnicalQuantity(
                updatedStock.getIncomingQuantity(),
                updatedStock.getRentedQuantity(),
                updatedStock.getAvailableQuantity());

        assertEquals(expectedTechnicalQuantity, updatedStock.getTechnicalQuantity());
        assertEquals(15, updatedStock.getIncomingQuantity());
        assertEquals(5, updatedStock.getRentedQuantity());
        assertEquals(15, updatedStock.getAvailableQuantity());
        verify(stockRepository, times(1)).save(existingStock);
    }

    @Test
    void deleteStockByArticleId_ShouldDeleteStock() {
        when(stockRepository.findByArticle_Articleid(1L)).thenReturn(Optional.of(existingStock));

        stockService.deleteStockByArticleId(1L);

        verify(stockRepository, times(1)).delete(existingStock);
    }

    @Test
    void deleteStockByArticleId_ShouldDoNothingIfStockNotFound() {
        when(stockRepository.findByArticle_Articleid(1L)).thenReturn(Optional.empty());

        stockService.deleteStockByArticleId(1L);

        verify(stockRepository, never()).delete(any(Stock.class));
    }
}
