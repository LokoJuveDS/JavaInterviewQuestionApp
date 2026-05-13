package com.jih.service;

import com.jih.model.QuestionAnswerDto;
import com.jih.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository repository;

    public void addQuestion(String question, String answer) {
        repository.save(question, answer);
    }

    public Map<Integer, QuestionAnswerDto> findAll() {
        return repository.findAll();
    }
}
