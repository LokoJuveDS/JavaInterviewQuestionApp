package com.jih.controller;

import com.jih.model.QuestionAnswerDto;
import com.jih.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Map;
import java.util.Scanner;

@Component
@RequiredArgsConstructor
public class QuestionCliController implements CommandLineRunner {
    private final QuestionService service;

    private static final String INTRO = """
            Enter command:
                - add  - add a new question and answer
                - list - display all saved questions and answers
                - exit - close the application""";
    private static final String ADD = "add";
    private static final String LIST = "list";
    private static final String EXIT = "exit";
    private static final String MENU = "menu";
    private static final String BACK_TO_MENU_HINT = "Enter 'menu' to go back.";

    @Override
    public void run(String... args) {
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.println(INTRO);
                String action = scanner.nextLine().trim().toLowerCase(Locale.ROOT);

                switch (action) {
                    case ADD -> addQuestion(scanner);
                    case LIST -> listQuestions();
                    case EXIT -> {
                        System.out.println("Application terminated");
                        return;
                    }
                    default -> {
                        System.out.println("Unknown command");
                        System.out.println();
                    }
                }
            }
        }
    }

    private void addQuestion(Scanner scanner) {
        String question = readLineOrMenu(scanner, "Enter question:");
        if (question == null) return;

        String answer = readLineOrMenu(scanner, "Enter answer:");
        if (answer == null) return;

        service.addQuestion(question, answer);
        System.out.println();
        System.out.println("Question saved successfully");
        System.out.println();
    }

    private String readLineOrMenu(Scanner scanner, String prompt) {
        System.out.println();
        System.out.println(BACK_TO_MENU_HINT);
        System.out.println();
        System.out.println(prompt);
        String text = scanner.nextLine().trim();
        return text.equalsIgnoreCase(MENU) ? null : text;
    }

    private void listQuestions() {
        Map<Integer, QuestionAnswerDto> allQuestions = service.findAll();

        if (allQuestions.isEmpty()) {
            System.out.println("Question list is empty. Use 'add' command to create one.");
            return;
        }

        System.out.println("Total questions: " + allQuestions.size());
        allQuestions.values().forEach(
                dto -> {
                    System.out.println(dto.number() + ") " + dto.question());
                    System.out.println("   Answer: " + dto.answer());
                    System.out.println();
                }
        );
    }
}
