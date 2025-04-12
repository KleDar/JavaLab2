package org.example;


import org.junit.jupiter.api.Test;


import java.util.Map;
import java.util.HashMap;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class ExpressionEvaluatorTest {
    @Test
    void testAddition() {
        Map<String, Double> variables = new HashMap<>();
        assertEquals(4, ExpressionEvaluator.evaluate("2+2", variables), 0.0001);
    }

    @Test
    void testSubtraction() {
        Map<String, Double> variables = new HashMap<>();
        assertEquals(5, ExpressionEvaluator.evaluate("10-5", variables), 0.0001);
    }

    @Test
    void testMultiplication() {
        Map<String, Double> variables = new HashMap<>();
        assertEquals(12, ExpressionEvaluator.evaluate("3*4", variables), 0.0001);
    }

    @Test
    void testDivision() {
        Map<String, Double> variables = new HashMap<>();
        assertEquals(4, ExpressionEvaluator.evaluate("20/5", variables), 0.0001);
    }

    @Test
    void testExponentiation() {
        Map<String, Double> variables = new HashMap<>();
        assertEquals(8, ExpressionEvaluator.evaluate("2^3", variables), 0.0001);
    }

    @Test
    void testOperatorPrecedence1() {
        Map<String, Double> variables = new HashMap<>();
        assertEquals(11, ExpressionEvaluator.evaluate("3 + 4 * 2", variables), 0.0001);
    }

    @Test
    void testOperatorPrecedence2() {
        Map<String, Double> variables = new HashMap<>();
        assertEquals(14, ExpressionEvaluator.evaluate("(3 + 4) * 2", variables), 0.0001);
    }

    @Test
    void testComplexExpression1() {
        Map<String, Double> variables = new HashMap<>();
        assertEquals(9, ExpressionEvaluator.evaluate("2 + 3 * 4 - 5", variables), 0.0001);
    }

    @Test
    void testComplexExpression2() {
        Map<String, Double> variables = new HashMap<>();
        assertEquals(14, ExpressionEvaluator.evaluate("2 * (3 + 4)", variables), 0.0001);
    }

    @Test
    void testComplexExpression3() {
        Map<String, Double> variables = new HashMap<>();
        assertEquals(10, ExpressionEvaluator.evaluate("2 * 3 + 4", variables), 0.0001);
    }

    @Test
    void testSinZero() {
        Map<String, Double> variables = new HashMap<>();
        assertEquals(0, ExpressionEvaluator.evaluate("sin(0)", variables), 0.0001);
    }

    @Test
    void testCosZero() {
        Map<String, Double> variables = new HashMap<>();
        assertEquals(1, ExpressionEvaluator.evaluate("cos(0)", variables), 0.0001);
    }

    @Test
    void testTanZero() {
        Map<String, Double> variables = new HashMap<>();
        assertEquals(0, ExpressionEvaluator.evaluate("tan(0)", variables), 0.0001);
    }

    @Test
    void testSqrt() {
        Map<String, Double> variables = new HashMap<>();
        assertEquals(2, ExpressionEvaluator.evaluate("sqrt(4)", variables), 0.0001);
    }

    @Test
    void testAbs() {
        Map<String, Double> variables = new HashMap<>();
        assertEquals(5, ExpressionEvaluator.evaluate("abs(-5)", variables), 0.0001);
    }

    @Test
    void testLog() {
        Map<String, Double> variables = new HashMap<>();
        assertEquals(2, ExpressionEvaluator.evaluate("log(100)", variables), 0.0001);
    }

    @Test
    void testLn() {
        Map<String, Double> variables = new HashMap<>();
        assertEquals(1, ExpressionEvaluator.evaluate("ln(e)", variables), 0.0001);
    }

    @Test
    void testExp() {
        Map<String, Double> variables = new HashMap<>();
        assertEquals(1, ExpressionEvaluator.evaluate("exp(0)", variables), 0.0001);
    }

    @Test
    void testSinPiOver2() {
        Map<String, Double> variables = new HashMap<>();
        assertEquals(1, ExpressionEvaluator.evaluate("sin(pi/2)", variables), 0.0001);
    }

    @Test
    void testCosPi() {
        Map<String, Double> variables = new HashMap<>();
        assertEquals(-1, ExpressionEvaluator.evaluate("cos(pi)", variables), 0.0001);
    }


    @Test
    void testVariables() {
        Map<String, Double> variables = new HashMap<>();
        variables.put("x", 5.0);
        variables.put("y", 3.0);

        assertEquals(8.0, ExpressionEvaluator.evaluate("x + y", variables), 0.0001);
        assertEquals(15.0, ExpressionEvaluator.evaluate("x * y", variables), 0.0001);
        assertEquals(2.0, ExpressionEvaluator.evaluate("x - y", variables), 0.0001);
    }

    @Test
    void testConstants() {
        assertEquals(Math.PI, ExpressionEvaluator.evaluate("pi", new HashMap<>()), 0.0001);
        assertEquals(Math.E, ExpressionEvaluator.evaluate("e", new HashMap<>()), 0.0001);
        assertEquals(2 * Math.PI, ExpressionEvaluator.evaluate("2*pi", new HashMap<>()), 0.0001);
    }

    @Test
    void testEmptyExpression() {
        assertThrows(IllegalArgumentException.class, () -> {
            ExpressionEvaluator.evaluate("", new HashMap<>());
        });
    }

    @Test
    void testWhitespaceExpression() {
        assertThrows(IllegalArgumentException.class, () -> {
            ExpressionEvaluator.evaluate("   ", new HashMap<>());
        });
    }

    @Test
    void testIncompleteExpression1() {
        assertThrows(IllegalArgumentException.class, () -> {
            ExpressionEvaluator.evaluate("2 + ", new HashMap<>());
        });
    }

    @Test
    void testIncompleteExpression2() {
        assertThrows(IllegalArgumentException.class, () -> {
            ExpressionEvaluator.evaluate("* 5", new HashMap<>());
        });
    }

    @Test
    void testInvalidFunctionCall1() {
        assertThrows(IllegalArgumentException.class, () -> {
            ExpressionEvaluator.evaluate("sin", new HashMap<>());
        });
    }

    @Test
    void testInvalidFunctionCall2() {
        assertThrows(IllegalArgumentException.class, () -> {
            ExpressionEvaluator.evaluate("sin 5", new HashMap<>());
        });
    }

    @Test
    void testIncompleteVariableExpression() {
        assertThrows(IllegalArgumentException.class, () -> {
            ExpressionEvaluator.evaluate("x +", new HashMap<>());
        });
    }

    @Test
    void testInvalidNumberFormat() {
        assertThrows(IllegalArgumentException.class, () -> {
            ExpressionEvaluator.evaluate("2..3", new HashMap<>());
        });
    }

    @Test
    void testDivisionByZero() {
        assertThrows(IllegalArgumentException.class, () -> {
            ExpressionEvaluator.evaluate("5 / 0", new HashMap<>());
        });
    }

    @Test
    void testSqrtOfNegative() {
        assertThrows(IllegalArgumentException.class, () -> {
            ExpressionEvaluator.evaluate("sqrt(-1)", new HashMap<>());
        });
    }

    @Test
    void testLogOfZero() {
        assertThrows(IllegalArgumentException.class, () -> {
            ExpressionEvaluator.evaluate("log(0)", new HashMap<>());
        });
    }

    @Test
    void testLnOfNegative() {
        assertThrows(IllegalArgumentException.class, () -> {
            ExpressionEvaluator.evaluate("ln(-1)", new HashMap<>());
        });
    }

    @Test
    void testUnmatchedParenthesis1() {
        assertThrows(IllegalArgumentException.class, () -> {
            ExpressionEvaluator.evaluate("2 + (3 * 4", new HashMap<>());
        });
    }

    @Test
    void testUnmatchedParenthesis2() {
        assertThrows(IllegalArgumentException.class, () -> {
            ExpressionEvaluator.evaluate("2 + 3) * 4", new HashMap<>());
        });
    }

    @Test
    void testUnknownFunction() {
        assertThrows(IllegalArgumentException.class, () -> {
            ExpressionEvaluator.evaluate("unknown(5)", new HashMap<>());
        });
    }

    @Test
    void testInvalidOperator() {
        assertThrows(IllegalArgumentException.class, () -> {
            ExpressionEvaluator.evaluate("2 $ 3", new HashMap<>());
        });
    }

    @Test
    void testInvalidVariableName() {
        assertThrows(IllegalArgumentException.class, () -> {
            ExpressionEvaluator.evaluate("123var + 5", new HashMap<>());
        });
    }


    @Test
    void testComplexExpression() {
        Map<String, Double> variables = new HashMap<>();
        variables.put("a", 2.0);
        variables.put("b", 3.0);
        variables.put("c", 4.0);

        String expression = "(a + b) * c - sqrt(b^2) / log(a^b) + sin(pi/2)";
        double expected = (2.0 + 3.0) * 4.0 - Math.sqrt(Math.pow(3.0, 2)) / Math.log10(Math.pow(2.0, 3.0)) + Math.sin(Math.PI / 2);

        assertEquals(expected, ExpressionEvaluator.evaluate(expression, variables), 0.0001);
    }

    @Test
    void testUnaryMinus() {
        assertEquals(-5.0, ExpressionEvaluator.evaluate("-5", new HashMap<>()), 0.0001);
        assertEquals(3.0, ExpressionEvaluator.evaluate("5 + -2", new HashMap<>()), 0.0001);
        assertEquals(-15.0, ExpressionEvaluator.evaluate("5 * -3", new HashMap<>()), 0.0001);
    }
}


