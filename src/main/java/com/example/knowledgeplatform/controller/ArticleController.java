package com.example.knowledgeplatform.controller;

import com.example.knowledgeplatform.entity.Article;
import com.example.knowledgeplatform.entity.User;
import com.example.knowledgeplatform.service.ArticleService;
import com.example.knowledgeplatform.service.AIService;
import com.example.knowledgeplatform.service.UserService;
import com.example.knowledgeplatform.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/articles")
@CrossOrigin(origins = "*")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private AIService aiService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping
    public ResponseEntity<?> createArticle(@RequestBody Map<String, String> articleData,
            @RequestHeader("Authorization") String token) {
        try {
            String email = jwtUtil.extractEmail(token.replace("Bearer ", ""));
            User author = userService.findByEmail(email);
            Article article = articleService.createArticle(
                    articleData.get("title"),
                    articleData.get("category"),
                    articleData.get("content"),
                    articleData.get("tags"),
                    author);
            return ResponseEntity.ok(article);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Error creating article"));
        }
    }

    @GetMapping
    public List<Article> getAllArticles() {
        return articleService.getAllArticles();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getArticle(@PathVariable Long id) {
        Article article = articleService.getArticleById(id);
        return article != null ? ResponseEntity.ok(article) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateArticle(@PathVariable Long id, @RequestBody Map<String, String> articleData,
            @RequestHeader("Authorization") String token) {
        try {
            Article existingArticle = articleService.getArticleById(id);
            if (existingArticle == null)
                return ResponseEntity.notFound().build();

            String email = jwtUtil.extractEmail(token.replace("Bearer ", ""));
            if (!existingArticle.getAuthor().getEmail().equals(email)) {
                return ResponseEntity.status(403).body(Map.of("message", "You can only edit your own articles"));
            }

            Article updated = articleService.updateArticle(id, articleData.get("title"), articleData.get("category"),
                    articleData.get("content"), articleData.get("tags"));
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Error updating article"));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteArticle(@PathVariable Long id, @RequestHeader("Authorization") String token) {
        try {
            Article existingArticle = articleService.getArticleById(id);
            if (existingArticle == null)
                return ResponseEntity.notFound().build();

            String email = jwtUtil.extractEmail(token.replace("Bearer ", ""));
            if (!existingArticle.getAuthor().getEmail().equals(email)) {
                return ResponseEntity.status(403).body(Map.of("message", "You can only delete your own articles"));
            }

            articleService.deleteArticle(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Error deleting article"));
        }
    }

    @GetMapping("/search/{query}")
    public List<Article> searchArticles(@PathVariable String query) {
        return articleService.searchArticles(query);
    }

    @PostMapping("/ai/improve")
    public ResponseEntity<?> improveContent(@RequestBody Map<String, String> data) {
        String improved = aiService.improveContent(data.get("content"));
        return ResponseEntity.ok(Map.of("improved", improved));
    }

    @PostMapping("/ai/tags")
    public ResponseEntity<?> suggestTags(@RequestBody Map<String, String> data) {
        String tags = aiService.suggestTags(data.get("content"));
        return ResponseEntity.ok(Map.of("tags", tags.split(",")));
    }

    @PostMapping("/ai/title")
    public ResponseEntity<?> suggestTitle(@RequestBody Map<String, String> data) {
        String title = aiService.suggestTitle(data.get("content"));
        return ResponseEntity.ok(Map.of("title", title));
    }

    @PostMapping("/ai/summary")
    public ResponseEntity<?> generateSummary(@RequestBody Map<String, String> data) {
        String summary = aiService.generateSummary(data.get("content"));
        return ResponseEntity.ok(Map.of("summary", summary));
    }
}