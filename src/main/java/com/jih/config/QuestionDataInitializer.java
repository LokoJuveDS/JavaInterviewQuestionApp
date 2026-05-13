package com.jih.config;

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
        service.addQuestion("Question 1", "Answer 1");
        service.addQuestion("Question 2", "Answer 2");
    }
}