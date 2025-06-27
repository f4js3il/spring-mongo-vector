package com.example.springmongovector.controllers;



import com.example.springmongovector.model.Answer;
import com.example.springmongovector.model.Question;
import com.example.springmongovector.services.AIService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class QuestionController {

    private final AIService aiService;

    public QuestionController(AIService aiService) {
        this.aiService = aiService;
    }

    @PostMapping("/ask")
    public Answer askQuestion(@RequestBody Question question) {
        return aiService.getAnswer(question);
    }

}
