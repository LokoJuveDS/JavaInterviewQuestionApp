package com.yegorp.jiq.service;

import com.yegorp.jiq.model.QuestionAnswerDto;
import com.yegorp.jiq.repository.QuestionAnswerRepository;

import java.util.Map;

public class QuestionAnswerService {
    private final QuestionAnswerRepository repository = QuestionAnswerRepository.getInstance();

    public void addQuestion(QuestionAnswerDto dto) {
        repository.save(dto);
    }

    public Map<Integer, QuestionAnswerDto> getAllQuestions() {
        return repository.getAllQuestions();
    }
}
