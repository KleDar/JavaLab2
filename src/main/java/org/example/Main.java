package org.example;
import org.example.ExpressionEvaluator;
//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        String[] expressions = {
                "3 + 4 * 2 / (1 - 5)",
                "2 * (3 + 4^2)",
                "2 * ((3 + 4) * 2)",
                "10.5 + 2 * 3.5",
                "3 + * 4",  // Некорректное выражение
                "3 + 4)",   // Несогласованные скобки
                "3 + "      // Некорректное выражение
        };

        for (String expr : expressions) {
            try {
                System.out.println("Выражение: " + expr);
                double result = ExpressionEvaluator.evaluate(expr);
                System.out.println("Результат: " + result);
            } catch (IllegalArgumentException e) {
                System.out.println("Ошибка: " + e.getMessage());
            }
            System.out.println();
        }

    }
}