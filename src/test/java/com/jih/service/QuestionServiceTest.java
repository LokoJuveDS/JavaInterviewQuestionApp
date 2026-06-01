package com.jih.service;

import com.jih.dto.QuestionCreateRequest;
import com.jih.dto.QuestionDto;
import com.jih.exception.ResourceNotFoundException;
import com.jih.model.entity.Category;
import com.jih.model.entity.Question;
import com.jih.repository.CategoryRepository;
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
    private CategoryRepository categoryRepository;

    @Mock
    private QuestionRepository questionRepository;

    @InjectMocks
    private QuestionService questionService;

    @Test
    void shouldCreateQuestion() {
        // given
        Category category = category(1L, "category 1");
        QuestionCreateRequest request = createQuestionCreateRequest(
                "question 1",
                "answer 1",
                "language 1",
                1L
        );

        when(categoryRepository.findById(request.categoryId())).thenReturn(Optional.of(category));
        when(questionRepository.save(any(Question.class)))
                .thenAnswer(invocation -> {
                    Question question = invocation.getArgument(0);
                    question.setId(10L);
                    return question;
                });

        // when
        QuestionDto result = questionService.create(request);

        // then
        verify(categoryRepository).findById(request.categoryId());
        verify(questionRepository).save(any(Question.class));

        assertQuestionDto(result, 10L, "question 1", "answer 1",
                "language 1", 1L, "category 1");
    }

    @Test
    void shouldThrowExceptionWhenCategoryNotFoundOnCreate() {
        // given
        QuestionCreateRequest request = createQuestionCreateRequest(
                "question 1",
                "answer 1",
                "language 1",
                999L
        );

        when(categoryRepository.findById(request.categoryId()))
                .thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> questionService.create(request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Category not found with id: 999");

        verify(questionRepository, never()).save(any(Question.class));
    }

    @Test
    void shouldFindQuestionById() {
        // given
        Question existingQuestion = question(2L, "question 2", "answer 2",
                "language 2", category(2L, "category 2"));
        when(questionRepository.findById(2L)).thenReturn(Optional.of(existingQuestion));

        // when
        QuestionDto result = questionService.findById(2L);

        // then
        assertQuestionDto(result, 2L, "question 2", "answer 2",
                "language 2", 2L, "category 2");
    }

    @Test
    void shouldThrowExceptionWhenQuestionNotFound() {
        // given
        when(questionRepository.findById(1L)).thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> questionService.findById(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Question not found with id: 1");
    }

    @Test
    void shouldReturnAllQuestions() {
        // given
        Question question1 = question(1L, "question 1", "answer 1",
                "language 1", category(1L, "category 1"));

        Question question2 = question(2L, "question 2", "answer 2",
                "language 2", category(2L, "category 2"));

        when(questionRepository.findAll()).thenReturn(List.of(question1, question2));

        // when
        List<QuestionDto> result = questionService.findAll();

        // then
        assertThat(result).hasSize(2);
        assertQuestionDto(result.getFirst(), 1L, "question 1", "answer 1",
                "language 1", 1L, "category 1");
        assertQuestionDto(result.get(1), 2L, "question 2", "answer 2",
                "language 2", 2L, "category 2");
    }

    @Test
    void shouldUpdateQuestion() {
        // given
        Question existingQuestion = question(2L, "old question", "old answer",
                "old language", category(1L, "old category"));
        Category newCategory = category(2L, "new category 2");
        QuestionCreateRequest updateRequest = createQuestionCreateRequest(
                "new question 2",
                "new answer 2",
                "new language 2",
                2L
        );

        when(questionRepository.findById(2L)).thenReturn(Optional.of(existingQuestion));
        when(categoryRepository.findById(updateRequest.categoryId())).thenReturn(Optional.of(newCategory));
        when(questionRepository.save(any(Question.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // when
        QuestionDto result = questionService.update(2L, updateRequest);

        // then
        verify(categoryRepository).findById(updateRequest.categoryId());
        verify(questionRepository).save(any(Question.class));

        assertQuestionDto(result, 2L, "new question 2", "new answer 2",
                "new language 2", 2L, "new category 2");
    }

    @Test
    void shouldDeleteQuestion() {
        // given
        Question question = question(2L, "question 2", "answer 2",
                "language 2", category(2L, "category 2"));

        when(questionRepository.findById(2L)).thenReturn(Optional.of(question));

        // when
        questionService.delete(2L);

        // then
        verify(questionRepository).delete(question);
    }

    @Test
    void shouldNotDeleteQuestionWhenQuestionNotFound() {
        // given
        when(questionRepository.findById(1L)).thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> questionService.delete(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Question not found with id: 1");

        verify(questionRepository, never()).delete(any(Question.class));
    }

    private void assertQuestionDto(
            QuestionDto result,
            Long expectedId,
            String expectedQuestion,
            String expectedAnswer,
            String expectedLanguage,
            Long expectedCategoryId,
            String expectedCategoryName
    ) {
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(expectedId);
        assertThat(result.question()).isEqualTo(expectedQuestion);
        assertThat(result.answer()).isEqualTo(expectedAnswer);
        assertThat(result.language()).isEqualTo(expectedLanguage);
        assertThat(result.categoryId()).isEqualTo(expectedCategoryId);
        assertThat(result.categoryName()).isEqualTo(expectedCategoryName);
    }

    private QuestionCreateRequest createQuestionCreateRequest(
            String question,
            String answer,
            String language,
            Long categoryId
    ) {
        return new QuestionCreateRequest(question, answer, language, categoryId);
    }

    private Question question(
            Long id,
            String questionText,
            String answer,
            String language,
            Category category
    ) {
        Question question = new Question();
        question.setId(id);
        question.setQuestion(questionText);
        question.setAnswer(answer);
        question.setLanguage(language);
        question.setCategory(category);
        return question;
    }

    private Category category(Long id, String name) {
        Category category = new Category();
        category.setId(id);
        category.setName(name);
        return category;
    }
}
