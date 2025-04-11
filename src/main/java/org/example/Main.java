package org.example;
import org.example.ExpressionEvaluator;
//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        String[] testExpressions = {
                "3 + 4 * 2 / (1 - 5)",  // Пример из алгоритма Дейкстры
                "2 * (3 + 4)",           // Выражение со скобками
                "10.5 + 2 * 3.5",         // С десятичными числами
                "3 + 4 * 2",              // Без скобок
                "( ( 5 + 3 ) * 2 )",      // Вложенные скобки
                "-5 + 3 * -2",             // С унарными минусами
                "3",                      // Просто число
                "3 + 4 * 2 / (1 - 5)^2"   // С дополнительным оператором (для демонстрации ошибки)
        };

        System.out.println("Тестирование преобразования в постфиксную форму:");
        System.out.println("=============================================");

        for (String expr : testExpressions) {
            try {
                System.out.println("Инфиксная форма: " + expr);
                String[] postfix = ExpressionEvaluator.infixToPostfix(expr);
                System.out.println("Постфиксная форма: " + String.join(" ", postfix));
                System.out.println();
            } catch (IllegalArgumentException e) {
                System.out.println("Ошибка преобразования: " + e.getMessage());
                System.out.println();
            }
        }

        // Дополнительные тесты с заведомо ошибочными выражениями
        String[] invalidExpressions = {
                "3 + * 4",      // Некорректный оператор
                "3 + 4)",       // Несогласованные скобки
                "(3 + 4",       // Несогласованные скобки
                "3 + 4a",       // Недопустимый символ
                "3 + ",         // Неполное выражение
                ""              // Пустая строка
        };

        System.out.println("\nТестирование обработки ошибок:");
        System.out.println("============================");

        for (String expr : invalidExpressions) {
            try {
                System.out.println("Инфиксная форма: " + expr);
                String[] postfix = ExpressionEvaluator.infixToPostfix(expr);
                System.out.println("Постфиксная форма: " + String.join(" ", postfix));
                System.out.println();
            } catch (IllegalArgumentException e) {
                System.out.println("Ожидаемая ошибка: " + e.getMessage());
                System.out.println();
            }
            }
    }
}