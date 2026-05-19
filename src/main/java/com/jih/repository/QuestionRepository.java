package com.jih.repository;

import com.jih.model.QuestionDto;
import org.springframework.stereotype.Repository;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Repository
public class QuestionRepository {
    private long questionCounter = 1;
    private final Map<Long, QuestionDto> questions = new LinkedHashMap<>();

    public QuestionDto save(String question, String answer) {
        QuestionDto dto = new QuestionDto(questionCounter, question, answer);
        questions.put(questionCounter++, dto);
        return dto;
    }

    public QuestionDto findById(Long id) {
        if (questions.containsKey(id)) {
            return questions.get(id);
        } else {
            throw new RuntimeException("Question not found with id: " + id);
        }
    }

    public List<QuestionDto> findAll() {
        return List.copyOf(questions.values());
    }

    public QuestionDto update(Long id, String question, String answer) {
        findById(id);

        QuestionDto updatedDto = new QuestionDto(id, question, answer);
        questions.put(id, updatedDto);

        return updatedDto;
    }

    public void delete(Long id) {
        findById(id);
        questions.remove(id);
    }
}
