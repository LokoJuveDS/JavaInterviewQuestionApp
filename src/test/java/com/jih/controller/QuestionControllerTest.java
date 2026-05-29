package com.jih.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jih.dto.QuestionCreateRequest;
import com.jih.dto.QuestionDto;
import com.jih.exception.QuestionNotFoundException;
import com.jih.service.QuestionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(QuestionController.class)
class QuestionControllerTest {

    private static final String BASE_URL = "/api/v1/questions";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private QuestionService questionService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldFindQuestionById() throws Exception {
        // given
        QuestionDto question = new QuestionDto(1L, "question 1", "answer 1");

        when(questionService.findById(1L)).thenReturn(question);

        // then
        mockMvc.perform(get(BASE_URL + "/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.question").value("question 1"))
                .andExpect(jsonPath("$.answer").value("answer 1"));
    }

    @Test
    void shouldReturn404WhenQuestionNotFound() throws Exception {
        // given
        when(questionService.findById(1L))
                .thenThrow(new QuestionNotFoundException(1L));

        // then
        mockMvc.perform(get(BASE_URL + "/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Question not found with id: 1"))
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void shouldReturnAllQuestions() throws Exception {
        // given
        List<QuestionDto> questions = List.of(
                new QuestionDto(1L, "question 1", "answer 1"),
                new QuestionDto(2L, "question 2", "answer 2")
        );

        when(questionService.findAll()).thenReturn(questions);

        // then
        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].question").value("question 1"))
                .andExpect(jsonPath("$[0].answer").value("answer 1"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].question").value("question 2"))
                .andExpect(jsonPath("$[1].answer").value("answer 2"));
    }

    @Test
    void shouldCreateQuestion() throws Exception {
        // given
        QuestionCreateRequest request =
                new QuestionCreateRequest("question 1", "answer 1");

        QuestionDto response =
                new QuestionDto(1L, "question 1", "answer 1");

        when(questionService.create(request)).thenReturn(response);

        // then
        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.question").value("question 1"))
                .andExpect(jsonPath("$.answer").value("answer 1"));
    }

    @Test
    void shouldReturn400WhenQuestionIsBlank() throws Exception {
        // given
        QuestionCreateRequest request =
                new QuestionCreateRequest("", "answer 1");

        // then
        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(questionService, never()).create(any(QuestionCreateRequest.class));
    }

    @Test
    void shouldReturn400WhenAnswerIsBlank() throws Exception {
        // given
        QuestionCreateRequest request =
                new QuestionCreateRequest("question 1", "");

        // then
        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(questionService, never()).create(any(QuestionCreateRequest.class));
    }

    @Test
    void shouldUpdateQuestion() throws Exception {
        // given
        QuestionCreateRequest request =
                new QuestionCreateRequest("new question", "new answer");

        QuestionDto response =
                new QuestionDto(1L, "new question", "new answer");

        when(questionService.update(1L, request)).thenReturn(response);

        // then
        mockMvc.perform(put(BASE_URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.question").value("new question"))
                .andExpect(jsonPath("$.answer").value("new answer"));
    }

    @Test
    void shouldDeleteQuestion() throws Exception {
        // then
        mockMvc.perform(delete(BASE_URL + "/1"))
                .andExpect(status().isNoContent());

        verify(questionService).delete(1L);
    }
}
