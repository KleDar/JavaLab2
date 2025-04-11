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

    /**
     * Вычисляет значение выражения в постфиксной форме (ОПН)
     * @param postfix выражение в постфиксной форме (массив токенов)
     * @return результат вычисления
     * @throws IllegalArgumentException если выражение некорректно
     */
    public static double evaluatePostfix(String[] postfix) throws IllegalArgumentException {
        Stack<Double> stack = new Stack<>();

        try {
            for (String token : postfix) {
                if (token.length() == 1 && isOperator(token.charAt(0))) {
                    // Если токен - оператор, выполняем операцию
                    char op = token.charAt(0);
                    double right = stack.pop();
                    double left = stack.pop();

                    switch (op) {
                        case '+':
                            stack.push(left + right);
                            break;
                        case '-':
                            stack.push(left - right);
                            break;
                        case '*':
                            stack.push(left * right);
                            break;
                        case '/':
                            if (right == 0) {
                                throw new IllegalArgumentException("Деление на ноль");
                            }
                            stack.push(left / right);
                            break;
                        case '^':
                            stack.push(Math.pow(left, right));
                            break;
                    }
                } else {
                    // Если токен - число, добавляем в стек
                    try {
                        stack.push(Double.parseDouble(token));
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException("Недопустимое число: " + token);
                    }
                }
            }

            // В стеке должен остаться ровно один элемент - результат
            if (stack.size() != 1) {
                throw new IllegalArgumentException("Некорректное выражение");
            }

            return stack.pop();
        } catch (EmptyStackException e) {
            throw new IllegalArgumentException("Некорректное выражение: недостаточно операндов");
        }
    }

    /**
     * Вычисляет значение математического выражения
     * @param expression строка с математическим выражением
     * @return результат вычисления
     * @throws IllegalArgumentException если выражение некорректно
     */
    public static double evaluate(String expression) throws IllegalArgumentException {
        if (expression == null || expression.trim().isEmpty()) {
            throw new IllegalArgumentException("Пустое выражение");
        }

        // Преобразуем в постфиксную форму
        String[] postfix = infixToPostfix(expression);

        // Вычисляем значение
        return evaluatePostfix(postfix);
    }
}
