package com.msd.erp.application.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.msd.erp.application.validations.DomainValidationService;
import com.msd.erp.domain.Article;
import com.msd.erp.infrastructure.repositories.ArticleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final DomainValidationService validationService;

    @Transactional
    public Article createArticle(Article article) {
        validationService.validateEntity(article);
        return articleRepository.save(article);
    }

    public List<Article> getAllArticles() {
        return articleRepository.findAll();
    }

    public Optional<Article> getArticleById(Long id) {
        return articleRepository.findById(id);
    }

    @Transactional
    public Optional<Article> updateArticle(Long id, Article updatedArticle) {
        return articleRepository.findById(id).map(existingArticle -> {
            existingArticle.setName(updatedArticle.getName());
            existingArticle.setDescription(updatedArticle.getDescription());
            existingArticle.setPrice(updatedArticle.getPrice());
            existingArticle.setUm(updatedArticle.getUm());
            existingArticle.setCategoryid(updatedArticle.getCategoryid());
            existingArticle.setVatid(updatedArticle.getVatid());

            validationService.validateEntity(existingArticle);
            return articleRepository.save(existingArticle);
        });
    }

    @Transactional
    public void deleteArticle(Long id) {
        if (articleRepository.existsById(id)) {
            articleRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Article with ID " + id + " does not exist.");
        }
    }

    public boolean articleExists(Long id) {
        return articleRepository.existsById(id);
    }

    public Optional<Article> getArticleByName(String name) {
        return articleRepository.findByName(name);
    }

    public List<Article> getArticlesByCategory(Long categoryId) {
        return articleRepository.findByCategoryid(categoryId);
    }
}
