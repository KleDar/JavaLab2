package org.example;
import java.util.Map;
import java.util.HashMap;
import org.example.ExpressionEvaluator;
//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        String[] testExpressions = {
                "2 + 2",                          // Базовая арифметика
                "3 * (4 + 5)",                     // Скобки
                "sin(0) + cos(0)",                 // Тригонометрия
                "exp(1) + tan(0)",                 // Экспонента и тригонометрия
                "sqrt(16) + abs(-5)",              // Корень и модуль
                "log(100) + ln(e^2)",              // Логарифмы
                "2 * sin(pi/4)^2",                 // Использование pi
                "e^(ln(5))",                       // Комбинация e и ln
                "3 + * 4",                         // Некорректное выражение
                "sqrt(-1)",                        // Ошибка вычисления
                "unknown(5)",                      // Неизвестная функция
                "2 * (3 + 4"                       // Незакрытая скобка
        };

        System.out.println("ТЕСТИРОВАНИЕ КАЛЬКУЛЯТОРА");
        System.out.println("========================");
        //System.out.println("Доступные константы: " + ExpressionEvaluator.CONSTANTS.keySet());
        //System.out.println("Доступные функции: " + FUNCTION_ARITY.keySet() + "\n");

        for (String expr : testExpressions) {
            try {
                System.out.println("\nВыражение: " + expr);

                // Вычисляем результат (константы будут заменены автоматически в infixToPostfix)
                double result = ExpressionEvaluator.evaluate(expr);
                System.out.println("Результат: " + result);

                // Дополнительно выводим постфиксную форму
                String[] postfix = ExpressionEvaluator.infixToPostfix(expr);
                System.out.println("Постфиксная форма: " + String.join(" ", postfix));
            } catch (IllegalArgumentException e) {
                System.out.println("ОШИБКА: " + e.getMessage());
            }
        }

        // Дополнительные тесты граничных случаев
        String[] edgeCases = {
                "",                                    // Пустая строка
                "   ",                                 // Только пробелы
                "2.5.3",                               // Неправильное число
                "sin()",                               // Пустые аргументы
                "sin(pi",                              // Незакрытая скобка
                "5 + + 5",                             // Двойной оператор
                "10e-2",                               // Научная нотация (не поддерживается)
                "max(5,3)"                             // Функция с несколькими аргументами
        };

        System.out.println("\nТЕСТИРОВАНИЕ ГРАНИЧНЫХ СЛУЧАЕВ");
        System.out.println("=============================");

        for (String expr : edgeCases) {
            try {
                System.out.println("\nВыражение: '" + expr + "'");
                double result = ExpressionEvaluator.evaluate(expr);
                System.out.println("Результат: " + result);
            } catch (IllegalArgumentException e) {
                System.out.println("ОШИБКА: " + e.getMessage());
            }
        }
    }
}