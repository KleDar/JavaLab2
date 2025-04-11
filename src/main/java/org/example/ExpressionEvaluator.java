package org.example;
import java.util.Map;
import java.util.HashMap;
import java.util.Stack;
import java.util.EmptyStackException;

/**
 * Класс для разбора и вычисления математических выражений.
 */
public class ExpressionEvaluator {

    // Приоритеты операторов
    private static final Map<Character, Integer> OPERATOR_PRECEDENCE = new HashMap<>();
    static {
        OPERATOR_PRECEDENCE.put('^', 4);
        OPERATOR_PRECEDENCE.put('*', 3);
        OPERATOR_PRECEDENCE.put('/', 3);
        OPERATOR_PRECEDENCE.put('+', 2);
        OPERATOR_PRECEDENCE.put('-', 2);
    }

    /**
     * Проверяет, является ли символ оператором
     * @param c проверяемый символ
     * @return true если символ является оператором, иначе false
     */
    private static boolean isOperator(char c) {
        return OPERATOR_PRECEDENCE.containsKey(c);
    }

    /**
     * Проверяет, является ли символ цифрой или точкой
     * @param c проверяемый символ
     * @return true если символ является цифрой или точкой, иначе false
     */
    private static boolean isDigitOrDot(char c) {
        return Character.isDigit(c) || c == '.';
    }

    /**
     * Преобразует инфиксное выражение в постфиксную форму (ОПН)
     * @param expression математическое выражение в инфиксной форме
     * @return массив строк, представляющий выражение в постфиксной форме
     * @throws IllegalArgumentException если выражение содержит синтаксические ошибки
     */
    public static String[] infixToPostfix(String expression) throws IllegalArgumentException {
        if (expression == null || expression.trim().isEmpty()) {
            throw new IllegalArgumentException("Пустое выражение");
        }

        Stack<Character> operatorStack = new Stack<>();
        StringBuilder output = new StringBuilder();
        boolean expectOperand = true;

        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);

            if (c == ' ') continue;

            if (isDigitOrDot(c)) {
                if (!expectOperand) {
                    throw new IllegalArgumentException("Ожидается оператор");
                }

                while (i < expression.length() && isDigitOrDot(expression.charAt(i))) {
                    output.append(expression.charAt(i));
                    i++;
                }
                output.append(' ');
                i--;
                expectOperand = false;
                continue;
            }

            if (c == '(') {
                if (!expectOperand) {
                    throw new IllegalArgumentException("Ожидается оператор");
                }
                operatorStack.push(c);
                expectOperand = true;
                continue;
            }

            if (c == ')') {
                if (expectOperand) {
                    throw new IllegalArgumentException("Ожидается операнд");
                }

                while (!operatorStack.isEmpty() && operatorStack.peek() != '(') {
                    output.append(operatorStack.pop()).append(' ');
                }

                if (operatorStack.isEmpty()) {
                    throw new IllegalArgumentException("Несогласованные скобки");
                }

                operatorStack.pop();
                expectOperand = false;
                continue;
            }

            if (isOperator(c)) {
                // Обработка унарного минуса
                if (c == '-' && expectOperand) {
                    output.append("0 ");
                    operatorStack.push('-');
                    continue;
                }

                if (expectOperand) {
                    throw new IllegalArgumentException("Ожидается операнд");
                }

                // Для оператора ^ правоассоциативность
                while (!operatorStack.isEmpty() && operatorStack.peek() != '(' &&
                        (OPERATOR_PRECEDENCE.get(c) < OPERATOR_PRECEDENCE.get(operatorStack.peek()) ||
                                (c != '^' && OPERATOR_PRECEDENCE.get(c) == OPERATOR_PRECEDENCE.get(operatorStack.peek())))) {
                    output.append(operatorStack.pop()).append(' ');
                }

                operatorStack.push(c);
                expectOperand = true;
                continue;
            }

            throw new IllegalArgumentException("Недопустимый символ: " + c);
        }

        if (expectOperand && output.length() == 0) {
            throw new IllegalArgumentException("Пустое выражение");
        }

        if (expectOperand) {
            throw new IllegalArgumentException("Неполное выражение");
        }

        while (!operatorStack.isEmpty()) {
            char op = operatorStack.pop();
            if (op == '(') {
                throw new IllegalArgumentException("Несогласованные скобки");
            }
            output.append(op).append(' ');
        }

        return output.toString().trim().split("\\s+");
    }


}
