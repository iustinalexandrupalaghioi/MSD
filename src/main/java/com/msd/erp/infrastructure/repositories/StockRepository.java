package com.msd.erp.infrastructure.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.msd.erp.domain.Article;
import com.msd.erp.domain.Stock;

import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Long> {

    Optional<Stock> findByArticle(Article article);

    Optional<Stock> findByArticle_Articleid(Long articleId);
}
