package org.example;
import java.util.Map;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Locale;

public class Main {
    public static void main(String[] args) {
        Locale.setDefault(Locale.US);
        Scanner scanner = new Scanner(System.in);
        scanner.useLocale(Locale.US);

        System.out.println("Калькулятор выражений с поддержкой переменных и функций");
        System.out.println("Примеры выражений: 'x + y', 'sin(a) + cos(b)', '2*pi*r'");
        System.out.println("Для выхода введите 'exit'");

        while (true) {
            System.out.print("\nВведите выражение: ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("exit")) {
                break;
            }

            try {

                Map<String, Double> variables = new HashMap<>();
                String[] postfix = ExpressionEvaluator.infixToPostfix(input);
                for (String token : postfix) {
                    if (token.startsWith("VAR:")) {
                        String varName = token.substring(4);
                        if (!variables.containsKey(varName)) {
                            System.out.print("Введите значение переменной " + varName + ": ");
                            double value = scanner.nextDouble();
                            scanner.nextLine();
                            variables.put(varName, value);
                        }
                    }
                }

                double result = ExpressionEvaluator.evaluate(input, variables);
                System.out.println("Результат: " + result);
            } catch (IllegalArgumentException e) {
                System.out.println("Ошибка: " + e.getMessage());
            }
        }

        System.out.println("Работа калькулятора завершена");
        scanner.close();
    }
}