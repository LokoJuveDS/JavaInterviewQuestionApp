package com.jih.service;

import com.jih.model.QuestionCreateRequest;
import com.jih.model.QuestionDto;
import com.jih.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository repository;

    public QuestionDto create(QuestionCreateRequest request) {
        return repository.save(request.question(), request.answer());
    }

    public QuestionDto findById(Long id) {
        return repository.findById(id);
    }

    public List<QuestionDto> findAll() {
        return repository.findAll();
    }

    public QuestionDto update(Long id, QuestionCreateRequest request) {
        return repository.update(id, request.question(), request.answer());
    }

    public void delete(Long id) {
        repository.delete(id);
    }
}
