package com.jih.service;

import com.jih.model.QuestionCreateRequest;
import com.jih.model.QuestionDto;
import com.jih.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository repository;

    public QuestionDto create(QuestionCreateRequest request) {
        QuestionDto createdQuestion = repository.save(request.question(), request.answer());

        log.info("Created question with id {}", createdQuestion.id());

        return createdQuestion;
    }

    public QuestionDto findById(Long id) {
        return repository.findById(id);
    }

    public List<QuestionDto> findAll() {
        return repository.findAll();
    }

    public QuestionDto update(Long id, QuestionCreateRequest request) {
        QuestionDto updatedQuestion = repository.update(id, request.question(), request.answer());

        log.info("Updated question with id {}", id);

        return updatedQuestion;
    }

    public void delete(Long id) {
        repository.delete(id);

        log.info("Deleted question with id {}", id);
    }
}
