package com.yegorp.jiq.repository;

import com.yegorp.jiq.model.QuestionAnswerDto;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class QuestionAnswerRepository {
    private static QuestionAnswerRepository instance;
    private static int questionCounter = 1;
    private static final Map<Integer, QuestionAnswerDto> questions = new LinkedHashMap<>();

    {
        save(new QuestionAnswerDto("Question 1", "Answer 1"));
        save(new QuestionAnswerDto("Question 2", "Answer 2"));
    }

    private QuestionAnswerRepository() {
    }

    public static QuestionAnswerRepository getInstance() {
        if (instance == null) {
            instance = new QuestionAnswerRepository();
        }
        return instance;
    }

    public void save(QuestionAnswerDto dto) {
        dto.setNumber(questionCounter);
        questions.put(questionCounter++, dto);
    }

    public Map<Integer, QuestionAnswerDto> getAllQuestions() {
        return Collections.unmodifiableMap(questions);
    }
}
