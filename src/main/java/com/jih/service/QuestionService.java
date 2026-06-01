package com.jih.service;

import com.jih.dto.QuestionCreateRequest;
import com.jih.dto.QuestionDto;
import com.jih.exception.ResourceNotFoundException;
import com.jih.model.entity.Question;
import com.jih.repository.CategoryRepository;
import com.jih.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public QuestionDto create(QuestionCreateRequest request) {
        Question question = createEntity(request);

        Question saved = questionRepository.save(question);

        log.info("Created question with id {}", saved.getId());

        return toDto(saved);
    }

    public QuestionDto findById(Long id) {
        return toDto(findQuestionByIdOrThrow(id));
    }

    public List<QuestionDto> findAll() {
        return questionRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Transactional
    public QuestionDto update(Long id, QuestionCreateRequest request) {
        Question question = findQuestionByIdOrThrow(id);
        question.setQuestion(request.question());
        question.setAnswer(request.answer());
        question.setLanguage(request.language());
        question.setCategory(categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category", request.categoryId())));

        Question saved = questionRepository.save(question);

        log.info("Updated question with id {}", saved.getId());

        return toDto(saved);
    }

    @Transactional
    public void delete(Long id) {
        Question question = findQuestionByIdOrThrow(id);
        questionRepository.delete(question);

        log.info("Deleted question with id {}", id);
    }

    private Question findQuestionByIdOrThrow(Long id) {
        return questionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Question", id));
    }

    private Question createEntity(QuestionCreateRequest request) {
        Question question = new Question();
        question.setQuestion(request.question());
        question.setAnswer(request.answer());
        question.setLanguage(request.language());
        question.setCategory(categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category", request.categoryId())));
        return question;
    }

    private QuestionDto toDto(Question question) {
        return new QuestionDto(
                question.getId(),
                question.getQuestion(),
                question.getAnswer(),
                question.getLanguage(),
                question.getCategory().getId(),
                question.getCategory().getName(),
                question.getCreatedAt(),
                question.getUpdatedAt()
        );
    }
}

