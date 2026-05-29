package com.jih.service;

import com.jih.dto.QuestionCreateRequest;
import com.jih.dto.QuestionDto;
import com.jih.exception.QuestionNotFoundException;
import com.jih.model.entity.Question;
import com.jih.repository.QuestionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuestionServiceTest {

    @Mock
    private QuestionRepository questionRepository;

    @InjectMocks
    private QuestionService questionService;

    @Test
    void shouldCreateQuestion() {
        // given
        QuestionCreateRequest request = new QuestionCreateRequest("question 1", "answer 1");

        when(questionRepository.save(any(Question.class))).thenReturn(existingQuestion());

        // when
        QuestionDto result = questionService.create(request);

        // then
        verify(questionRepository).save(any(Question.class));
        assertQuestionDto(result, 1L, "question 1", "answer 1");
    }

    @Test
    void shouldFindQuestionById() {
        // given
        when(questionRepository.findById(1L)).thenReturn(Optional.of(existingQuestion()));

        // when
        QuestionDto result = questionService.findById(1L);

        // then
        assertQuestionDto(result, 1L, "question 1", "answer 1");
    }

    @Test
    void shouldThrowExceptionWhenQuestionNotFound() {
        // given
        when(questionRepository.findById(1L)).thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> questionService.findById(1L))
                .isInstanceOf(QuestionNotFoundException.class)
                .hasMessage("Question not found with id: 1");
    }

    @Test
    void shouldReturnAllQuestions() {
        // given
        Question question1 = new Question();
        question1.setId(1L);
        question1.setQuestion("question 1");
        question1.setAnswer("answer 1");

        Question question2 = new Question();
        question2.setId(2L);
        question2.setQuestion("question 2");
        question2.setAnswer("answer 2");

        when(questionRepository.findAll())
                .thenReturn(List.of(question1, question2));

        // when
        List<QuestionDto> result = questionService.findAll();

        // then
        assertThat(result).hasSize(2);
        assertQuestionDto(result.getFirst(), 1L, "question 1", "answer 1");
        assertQuestionDto(result.get(1), 2L, "question 2", "answer 2");
    }

    @Test
    void shouldUpdateQuestion() {
        // given
        QuestionCreateRequest request = new QuestionCreateRequest("new question 1", "new answer 1");

        when(questionRepository.findById(1L)).thenReturn(Optional.of(existingQuestion()));
        when(questionRepository.save(any(Question.class))).thenReturn(updatedQuestion());

        // when
        QuestionDto result = questionService.update(1L, request);

        // then
        verify(questionRepository).save(any(Question.class));
        assertQuestionDto(result, 1L, "new question 1", "new answer 1");
    }

    @Test
    void shouldDeleteQuestion() {
        // given
        Question question = existingQuestion();

        when(questionRepository.findById(1L)).thenReturn(Optional.of(question));

        // when
        questionService.delete(1L);

        // then
        verify(questionRepository).delete(question);
    }

    @Test
    void shouldNotDeleteQuestionWhenQuestionNotFound() {
        // given
        when(questionRepository.findById(1L)).thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> questionService.delete(1L))
                .isInstanceOf(QuestionNotFoundException.class)
                .hasMessage("Question not found with id: 1");

        verify(questionRepository, never()).delete(any(Question.class));
    }

    private Question existingQuestion() {
        Question question = new Question();
        question.setId(1L);
        question.setQuestion("question 1");
        question.setAnswer("answer 1");
        return question;
    }

    private Question updatedQuestion() {
        Question question = new Question();
        question.setId(1L);
        question.setQuestion("new question 1");
        question.setAnswer("new answer 1");
        return question;
    }

    private void assertQuestionDto(
            QuestionDto result,
            Long expectedId,
            String expectedQuestion,
            String expectedAnswer
    ) {
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(expectedId);
        assertThat(result.question()).isEqualTo(expectedQuestion);
        assertThat(result.answer()).isEqualTo(expectedAnswer);
    }
}
