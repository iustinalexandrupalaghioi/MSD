package com.msd.erp.application.workflowTests;
import com.msd.erp.application.services.ArticleService;
import com.msd.erp.application.validations.DomainValidationService;
import com.msd.erp.domain.Article;
import com.msd.erp.infrastructure.repositories.ArticleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ArticleServiceTest {

    private ArticleService articleService;
    private ArticleRepository articleRepository;
    private DomainValidationService validationService;

    @BeforeEach
    void setUp() {
        articleRepository = mock(ArticleRepository.class);
        validationService = mock(DomainValidationService.class);
        articleService = new ArticleService(articleRepository, validationService);
    }

    @Test
    void createArticle_ShouldValidateAndSaveArticle() {
        Article mockArticle = new Article();
        mockArticle.setName("Test Article");

        when(articleRepository.save(mockArticle)).thenReturn(mockArticle);

        Article createdArticle = articleService.createArticle(mockArticle);

        verify(validationService).validateEntity(mockArticle);
        verify(articleRepository).save(mockArticle);
        assertEquals("Test Article", createdArticle.getName());
    }

    @Test
    void getAllArticles_ShouldReturnAllArticles() {
        Article article1 = new Article();
        Article article2 = new Article();
        when(articleRepository.findAll()).thenReturn(List.of(article1, article2));

        List<Article> articles = articleService.getAllArticles();

        verify(articleRepository).findAll();
        assertEquals(2, articles.size());
    }

    @Test
    void getArticleById_ShouldReturnArticle_WhenArticleExists() {
        Article mockArticle = new Article();
        mockArticle.setArticleid(1L);

        when(articleRepository.findById(1L)).thenReturn(Optional.of(mockArticle));

        Optional<Article> article = articleService.getArticleById(1L);

        verify(articleRepository).findById(1L);
        assertTrue(article.isPresent());
        assertEquals(1L, article.get().getArticleid());
    }

    @Test
    void getArticleById_ShouldReturnEmptyOptional_WhenArticleDoesNotExist() {
        when(articleRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Article> article = articleService.getArticleById(1L);

        verify(articleRepository).findById(1L);
        assertFalse(article.isPresent());
    }

    @Test
    void updateArticle_ShouldUpdateAndValidateArticle_WhenArticleExists() {
        Article existingArticle = new Article();
        existingArticle.setArticleid(1L);
        existingArticle.setName("Old Name");

        Article updatedArticle = new Article();
        updatedArticle.setName("New Name");

        when(articleRepository.findById(1L)).thenReturn(Optional.of(existingArticle));
        when(articleRepository.save(any(Article.class))).thenReturn(existingArticle);

        Optional<Article> result = articleService.updateArticle(1L, updatedArticle);

        verify(validationService).validateEntity(existingArticle);
        verify(articleRepository).save(existingArticle);

        assertTrue(result.isPresent());
        assertEquals("New Name", result.get().getName());
    }

    @Test
    void updateArticle_ShouldReturnEmptyOptional_WhenArticleDoesNotExist() {
        Article updatedArticle = new Article();
        updatedArticle.setName("New Name");

        when(articleRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Article> result = articleService.updateArticle(1L, updatedArticle);

        verify(articleRepository).findById(1L);
        assertFalse(result.isPresent());
    }

    @Test
    void deleteArticle_ShouldDeleteArticle_WhenArticleExists() {
        when(articleRepository.existsById(1L)).thenReturn(true);

        articleService.deleteArticle(1L);

        verify(articleRepository).deleteById(1L);
    }

    @Test
    void deleteArticle_ShouldThrowException_WhenArticleDoesNotExist() {
        when(articleRepository.existsById(1L)).thenReturn(false);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> articleService.deleteArticle(1L)
        );

        assertEquals("Article with ID 1 does not exist.", exception.getMessage());
        verify(articleRepository, never()).deleteById(anyLong());
    }

    @Test
    void articleExists_ShouldReturnTrue_WhenArticleExists() {
        when(articleRepository.existsById(1L)).thenReturn(true);

        boolean exists = articleService.articleExists(1L);

        verify(articleRepository).existsById(1L);
        assertTrue(exists);
    }

    @Test
    void articleExists_ShouldReturnFalse_WhenArticleDoesNotExist() {
        when(articleRepository.existsById(1L)).thenReturn(false);

        boolean exists = articleService.articleExists(1L);

        verify(articleRepository).existsById(1L);
        assertFalse(exists);
    }

    @Test
    void getArticleByName_ShouldReturnArticle_WhenNameMatches() {
        Article mockArticle = new Article();
        mockArticle.setName("Test Article");

        when(articleRepository.findByName("Test Article")).thenReturn(Optional.of(mockArticle));

        Optional<Article> article = articleService.getArticleByName("Test Article");

        verify(articleRepository).findByName("Test Article");
        assertTrue(article.isPresent());
        assertEquals("Test Article", article.get().getName());
    }

    @Test
    void getArticlesByCategory_ShouldReturnArticlesForCategory() {
        Article article1 = new Article();
        Article article2 = new Article();

        when(articleRepository.findByCategoryid(1L)).thenReturn(List.of(article1, article2));

        List<Article> articles = articleService.getArticlesByCategory(1L);

        verify(articleRepository).findByCategoryid(1L);
        assertEquals(2, articles.size());
    }
}
