package com.msd.erp.infrastructure.repositories;

import com.msd.erp.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    @Query("SELECT a FROM Article a WHERE a.name = :name")
    Optional<Article> findByName(@Param("name") String name);

    @Query("SELECT a FROM Article a WHERE a.categoryid = :categoryId")
    List<Article> findByCategoryid(@Param("categoryId") Long categoryId);
}
