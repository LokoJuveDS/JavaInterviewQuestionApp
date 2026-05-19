package com.jih.config;

import com.jih.model.QuestionCreateRequest;
import com.jih.service.QuestionService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuestionDataInitializer {

    private final QuestionService service;

    @PostConstruct
    public void init() {
        service.create(new QuestionCreateRequest("Question 1", "Answer 1"));
        service.create(new QuestionCreateRequest("Question 2", "Answer 2"));
    }
}