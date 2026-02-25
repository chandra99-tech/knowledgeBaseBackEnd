package com.example.knowledgeplatform.service;

import org.springframework.stereotype.Service;

@Service
public class AIService {

    public String improveContent(String content) {
        // Mock AI improvement
        return content + "\n\n[AI Improved: Added clarity and structure]";
    }

    public String generateSummary(String content) {
        // Mock summary
        return content.length() > 120 ? content.substring(0, 120) + "..." : content;
    }

    public String suggestTags(String content) {
        // Mock tags
        return "tech,AI,backend";
    }

    public String suggestTitle(String content) {
        // Mock title
        return "Improved Title: " + (content.contains(".") ? content.split("\\.")[0] : content);
    }
}