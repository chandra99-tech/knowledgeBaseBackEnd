package com.example.knowledgeplatform.service;

import com.example.knowledgeplatform.entity.Article;
import com.example.knowledgeplatform.entity.User;
import com.example.knowledgeplatform.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    public Article createArticle(String title, String category, String content, String tags, User author) {
        Article article = new Article();
        article.setTitle(title);
        article.setCategory(category);
        article.setContent(content);
        article.setTags(tags);
        article.setAuthor(author);
        return articleRepository.save(article);
    }

    public List<Article> getAllArticles() {
        return articleRepository.findAll();
    }

    public Article getArticleById(Long id) {
        return articleRepository.findById(id).orElse(null);
    }

    public List<Article> getArticlesByAuthor(Long authorId) {
        return articleRepository.findByAuthorId(authorId);
    }

    public Article updateArticle(Long id, String title, String category, String content, String tags) {
        Article article = articleRepository.findById(id).orElse(null);
        if (article != null) {
            article.setTitle(title);
            article.setCategory(category);
            article.setContent(content);
            article.setTags(tags);
            article.setUpdatedAt(LocalDateTime.now());
            return articleRepository.save(article);
        }
        return null;
    }

    public void deleteArticle(Long id) {
        articleRepository.deleteById(id);
    }

    public List<Article> searchArticles(String query) {
        return articleRepository.searchArticles(query);
    }
}