package com.example.springmongovector.services;


import com.example.springmongovector.model.Answer;
import com.example.springmongovector.model.Question;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
public class AIServiceImpl implements AIService {

    final ChatModel chatModel;
    final VectorStore vectorStore;

    public AIServiceImpl(ChatModel chatModel, VectorStore vectorStore) {
        this.chatModel = chatModel;
        this.vectorStore = vectorStore;
    }

    @Value("classpath:/templates/rag-prompt-template.st")
    private Resource ragPromptTemplate;


    @Override
    public Answer getAnswer(Question question) {
        List<Document> documents = vectorStore.similaritySearch(SearchRequest.builder()
                .query(question.question()).topK(5).build());
        List<String> contentList = documents.stream().map(Document::getText).toList();

        PromptTemplate promptTemplate = new PromptTemplate(ragPromptTemplate);
        Message userMessage = promptTemplate.createMessage(Map.of("input", question.question(), "documents",
                String.join("\n", contentList)));;
        ChatResponse response = chatModel.call(new Prompt(List.of(userMessage)));

        return new Answer(response.getResult().getOutput().getText());
    }
}
