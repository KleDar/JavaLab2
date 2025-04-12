package org.example;
import java.util.Map;
import java.util.HashMap;
import java.util.Stack;
import java.util.EmptyStackException;
import java.util.Stack;

/**
 * Класс для разбора и вычисления математических выражений.
 */
public class ExpressionEvaluator {

    // Приоритеты операторов
    private static final Map<Character, Integer> OPERATOR_PRECEDENCE = new HashMap<>();
    // Поддерживаемые функции
    private static final Map<String, Integer> FUNCTION_ARITY = new HashMap<>();
    private static final Map<String, Double> CONSTANTS = new HashMap<>();
    static {
        OPERATOR_PRECEDENCE.put('^', 4);
        OPERATOR_PRECEDENCE.put('*', 3);
        OPERATOR_PRECEDENCE.put('/', 3);
        OPERATOR_PRECEDENCE.put('+', 2);
        OPERATOR_PRECEDENCE.put('-', 2);

        // Функции (имя -> количество аргументов)
        FUNCTION_ARITY.put("sin", 1);
        FUNCTION_ARITY.put("cos", 1);
        FUNCTION_ARITY.put("tan", 1);
        FUNCTION_ARITY.put("asin", 1);
        FUNCTION_ARITY.put("acos", 1);
        FUNCTION_ARITY.put("atan", 1);
        FUNCTION_ARITY.put("sqrt", 1);
        FUNCTION_ARITY.put("log", 1);  // десятичный логарифм
        FUNCTION_ARITY.put("ln", 1);
        FUNCTION_ARITY.put("exp", 1);  // экспонента
        FUNCTION_ARITY.put("abs", 1);

        // Константы
        CONSTANTS.put("pi", Math.PI);
        CONSTANTS.put("e", Math.E);
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

    private static boolean isLetter(char c) {
        return Character.isLetter(c);
    }

    /**
     * Проверяет, является ли строка именем функции
     */
    private static boolean isFunction(String token) {
        return FUNCTION_ARITY.containsKey(token);
    }
    private static boolean isConstant(String token) {
        return CONSTANTS.containsKey(token);
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

        Stack<String> stack = new Stack<>();
        StringBuilder output = new StringBuilder();
        boolean expectOperand = true;

        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);

            if (c == ' ') continue;

            // Обработка чисел
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

            // Обработка функций и переменных
            if (isLetter(c)) {
                if (!expectOperand) {
                    throw new IllegalArgumentException("Ожидается оператор");
                }

                StringBuilder token = new StringBuilder();
                while (i < expression.length() && isLetter(expression.charAt(i))) {
                    token.append(expression.charAt(i));
                    i++;
                }
                i--;

                String tokenStr = token.toString();

                if (isFunction(tokenStr)) {
                    stack.push(tokenStr);
                    // Проверяем наличие скобок после функции
                    if (i + 1 >= expression.length() || expression.charAt(i + 1) != '(') {
                        throw new IllegalArgumentException("После функции " + tokenStr + " ожидается '('");
                    }
                } else if (isConstant(tokenStr)) {
                    output.append(CONSTANTS.get(tokenStr)).append(' ');
                    expectOperand = false;
                } else {
                    throw new IllegalArgumentException("Неизвестный идентификатор: " + tokenStr);
                }
                continue;
            }

            // Обработка скобок
            if (c == '(') {
                if (!expectOperand) {
                    throw new IllegalArgumentException("Ожидается оператор");
                }
                stack.push("(");
                expectOperand = true;
                continue;
            }

            if (c == ')') {
                if (expectOperand) {
                    throw new IllegalArgumentException("Ожидается операнд");
                }

                while (!stack.isEmpty() && !stack.peek().equals("(")) {
                    output.append(stack.pop()).append(' ');
                }

                if (stack.isEmpty()) {
                    throw new IllegalArgumentException("Несогласованные скобки");
                }

                stack.pop(); // Удаляем "("

                // Если на вершине стека функция - переносим в выход
                if (!stack.isEmpty() && isFunction(stack.peek())) {
                    output.append(stack.pop()).append(' ');
                }

                expectOperand = false;
                continue;
            }

            // Обработка операторов
            if (isOperator(c)) {
                // Обработка унарного минуса
                if (c == '-' && expectOperand) {
                    output.append("0 ");
                    stack.push("-");
                    continue;
                }

                if (expectOperand) {
                    throw new IllegalArgumentException("Ожидается операнд");
                }

                // Для оператора ^ правоассоциативность
                while (!stack.isEmpty() && !stack.peek().equals("(") &&
                        (OPERATOR_PRECEDENCE.get(c) < OPERATOR_PRECEDENCE.get(stack.peek().charAt(0)) ||
                                (c != '^' && OPERATOR_PRECEDENCE.get(c) == OPERATOR_PRECEDENCE.get(stack.peek().charAt(0))))) {
                    output.append(stack.pop()).append(' ');
                }

                stack.push(String.valueOf(c));
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

        while (!stack.isEmpty()) {
            String op = stack.pop();
            if (op.equals("(")) {
                throw new IllegalArgumentException("Несогласованные скобки");
            }
            output.append(op).append(' ');
        }

        return output.toString().trim().split("\\s+");
    }

    public static double evaluatePostfix(String[] postfix) throws IllegalArgumentException {
        Stack<Double> stack = new Stack<>();

        try {
            for (String token : postfix) {
                if (token.length() == 1 && isOperator(token.charAt(0))) {
                    // Обработка операторов
                    char op = token.charAt(0);
                    double right = stack.pop();
                    double left = stack.pop();

                    switch (op) {
                        case '+': stack.push(left + right); break;
                        case '-': stack.push(left - right); break;
                        case '*': stack.push(left * right); break;
                        case '/':
                            if (right == 0) throw new IllegalArgumentException("Деление на ноль");
                            stack.push(left / right);
                            break;
                        case '^': stack.push(Math.pow(left, right)); break;
                    }
                } else if (isFunction(token)) {
                    // Обработка функций
                    double arg = stack.pop();
                    switch (token) {
                        case "sin": stack.push(Math.sin(arg)); break;
                        case "cos": stack.push(Math.cos(arg)); break;
                        case "tan": stack.push(Math.tan(arg)); break;
                        case "asin": stack.push(Math.asin(arg)); break;
                        case "acos": stack.push(Math.acos(arg)); break;
                        case "atan": stack.push(Math.atan(arg)); break;
                        case "sqrt":
                            if (arg < 0) throw new IllegalArgumentException("Корень из отрицательного числа");
                            stack.push(Math.sqrt(arg));
                            break;
                        case "log":
                            if (arg <= 0) throw new IllegalArgumentException("Логарифм неположительного числа");
                            stack.push(Math.log10(arg));
                            break;
                        case "ln":
                            if (arg <= 0) throw new IllegalArgumentException("Логарифм неположительного числа");
                            stack.push(Math.log(arg));
                            break;
                        case "exp": stack.push(Math.exp(arg)); break;
                        case "abs": stack.push(Math.abs(arg)); break;
                    }
                } else {
                    // Обработка чисел
                    try {
                        stack.push(Double.parseDouble(token));
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException("Недопустимое число: " + token);
                    }
                }
            }

            if (stack.size() != 1) {
                throw new IllegalArgumentException("Некорректное выражение");
            }

            return stack.pop();
        } catch (EmptyStackException e) {
            throw new IllegalArgumentException("Некорректное выражение: недостаточно операндов");
        }
    }

    public static double evaluate(String expression) throws IllegalArgumentException {
        if (expression == null || expression.trim().isEmpty()) {
            throw new IllegalArgumentException("Пустое выражение");
        }

        String[] postfix = infixToPostfix(expression);
        return evaluatePostfix(postfix);
    }
}
