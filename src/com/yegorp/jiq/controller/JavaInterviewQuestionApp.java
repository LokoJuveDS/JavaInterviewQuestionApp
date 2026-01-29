package com.yegorp.jiq.controller;

import com.yegorp.jiq.model.QuestionAnswerDto;
import com.yegorp.jiq.service.QuestionAnswerService;

import java.util.Map;
import java.util.Scanner;

public class JavaInterviewQuestionApp {
    private final static QuestionAnswerService service = new QuestionAnswerService();
    private static final String INTRO = """
            Введите команду:
                - add - добавить новый вопрос и ответ
                - list - вывести в консоль список вопросов и ответов
                - exit - завершение работы программы""";
    private static final String ADD = "add";
    private static final String LIST = "list";
    private static final String EXIT = "exit";
    private static final String MENU = "menu";
    private static final String BACK_TO_MENU_HINT = "Чтобы вернуться в меню, введите 'menu'.";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println(INTRO);
            String action = scanner.nextLine().trim().toLowerCase();
            switch (action) {
                case ADD:
                    addQuestion(scanner);
                    break;
                case LIST:
                    listQuestions();
                    break;
                case EXIT:
                    return;
                default:
                    System.out.println("Неизвестная команда");
            }
        }
    }

    private static String readLineOrMenu(Scanner scanner, String prompt) {
        System.out.println(BACK_TO_MENU_HINT);
        System.out.println(prompt);
        String text = scanner.nextLine().trim();
        return text.equalsIgnoreCase(MENU) ? null : text;
    }

    private static void addQuestion(Scanner scanner) {
        String question = readLineOrMenu(scanner, "Введите вопрос:");
        if (question == null) return;

        String answer = readLineOrMenu(scanner, "Введите ответ:");
        if (answer == null) return;

        service.addQuestion(new QuestionAnswerDto(question, answer));
        System.out.println("Вопрос успешно сохранён");
    }

    private static void listQuestions() {
        Map<Integer, QuestionAnswerDto> allQuestions = service.getAllQuestions();

        if (allQuestions.isEmpty()) {
            System.out.println("Список пуст. Добавьте вопрос командой '\\add'\\.");
        }

        System.out.println("Количество вопросов: " + allQuestions.size());
        allQuestions.values().forEach(
                dto -> {
                    System.out.println(dto.getNumber() + ") " + dto.getQuestion());
                    System.out.println("   Ответ:" + dto.getAnswer());
                    System.out.println();
                }
        );
    }
}
