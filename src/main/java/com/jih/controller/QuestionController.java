package com.jih.controller;

import com.jih.model.QuestionCreateRequest;
import com.jih.model.QuestionDto;
import com.jih.service.QuestionService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/questions")
@RequiredArgsConstructor
@Slf4j
public class QuestionController {

    private final QuestionService questionService;

    @GetMapping("/{id}")
    @Operation(summary = "Get a question by ID")
    public ResponseEntity<QuestionDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(questionService.findById(id));
    }

    @GetMapping
    @Operation(summary = "Get all questions")
    public ResponseEntity<List<QuestionDto>> findAll() {
        return ResponseEntity.ok(questionService.findAll());
    }

    @PostMapping
    @Operation(summary = "Create a new question")
    public ResponseEntity<QuestionDto> create(@Valid @RequestBody QuestionCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(questionService.create(request));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing question")
    public ResponseEntity<QuestionDto> update(@PathVariable Long id, @Valid @RequestBody QuestionCreateRequest request) {
        return ResponseEntity.ok(questionService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a question")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        questionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
