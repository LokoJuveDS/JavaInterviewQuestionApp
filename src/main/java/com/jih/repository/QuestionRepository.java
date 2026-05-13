package com.jih.repository;

import com.jih.model.QuestionAnswerDto;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

@Repository
public class QuestionRepository {
    private int questionCounter = 1;
    private final Map<Integer, QuestionAnswerDto> questions = new LinkedHashMap<>();

    public void save(String question, String answer) {
        QuestionAnswerDto dto = new QuestionAnswerDto(questionCounter, question, answer);
        questions.put(questionCounter++, dto);
    }

    public Map<Integer, QuestionAnswerDto> findAll() {
        return Collections.unmodifiableMap(questions);
    }
}
