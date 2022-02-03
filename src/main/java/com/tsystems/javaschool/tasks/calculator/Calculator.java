package com.tsystems.javaschool.tasks.calculator;

import java.text.DecimalFormat;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Calculator {

    private Stack<Double> values = new Stack<Double>();
    private Stack<String> operators = new Stack<String>();

    /**
     * Evaluate statement represented as string.
     *
     * @param statement mathematical statement containing digits, '.' (dot) as decimal mark,
     *                  parentheses, operations signs '+', '-', '*', '/'<br>
     *                  Example: <code>(1 + 38) * 4.5 - 1 / 2.</code>
     * @return string value containing result of evaluation or null if statement is invalid
     */
    public String evaluate(String statement) {
        if(!isValid(statement)) return null;

        String[] splitStatement =
                statement.split("(?<=[\\d.])(?=[^\\d.])|(?<=[^\\d.])(?=[\\d.])|(?=[()])|(?<=[()])");

        try {
            for (String elem : splitStatement) {
                if (isDigit(elem)) {
                    values.push(Double.parseDouble(elem));
                } else if (isOperator(elem)) {
                    if (operators.empty() || getOperatorPriority(elem) > getOperatorPriority(operators.peek())) {
                        operators.push(elem);
                    } else {
                        while (!operators.empty() && getOperatorPriority(elem) <= getOperatorPriority(operators.peek()))
                            runOperation(operators.pop());
                        operators.push(elem);
                    }
                } else if (isBracket(elem)) {
                    if (elem.equals("(")) {
                        operators.push(elem);
                    } else {
                        while (!operators.empty() && isOperator(operators.peek()))
                            runOperation(operators.pop());
                        if (!operators.empty() && operators.peek().equals("("))
                            operators.pop();
                    }
                }
            }

            while (!operators.empty() && isOperator(operators.peek())) {
                runOperation(operators.pop());
            }
        } catch (IllegalArgumentException e) {
            return null;
        }

        return formatResult(values.pop());
    }

    private String formatResult(Double result) {
        DecimalFormat df = new DecimalFormat("0.####");
        return df.format(result).replaceAll(",",".");
    }

    private void runOperation(String o) throws IllegalArgumentException {
        double y = values.pop();
        double x = values.pop();
        double result = 0;

        if (y == 0 && o.equals("/"))
            throw new IllegalArgumentException();

        switch (o) {
            case "+": result = x + y; break;
            case "-": result = x - y; break;
            case "*": result = x * y; break;
            case "/": result = x / y; break;
        }

        values.push(result);
    }

    private int getOperatorPriority(String o) {
        if (o.equals("*") || o.equals("/")) return 2;
        if (o.equals("+") || o.equals("-")) return 1;
        return 0;
    }

    private boolean isDigit(String s) {
        return !(s.equals("+") ||
                s.equals("-") ||
                s.equals("*") ||
                s.equals("/") ||
                s.equals("(") ||
                s.equals(")"));
    }

    private boolean isOperator(String s) {
        return (s.equals("+") ||
                s.equals("-") ||
                s.equals("*") ||
                s.equals("/"));
    }

    private boolean isBracket(String s) {
        return (s.equals("(") ||
                s.equals(")"));
    }

    private boolean isValid(String expression) {
        if (expression == null || expression.equals("")) return false;

        //comma and multiple operation symbols, dots
        Pattern pattern1 = Pattern.compile("[+/*-\\.]{2,}|,");
        Matcher matcher1 = pattern1.matcher(expression);
        if (matcher1.find()) return false;

        // opening and closing "(" && ")"
        Pattern pattern3 = Pattern.compile("\\([\\d+/*-\\.]*\\)");
        Matcher matcher3 = pattern3.matcher(expression);
        do {
            expression = matcher3.replaceAll("");
            matcher3 = pattern3.matcher(expression);
        } while (matcher3.find());
        return expression.matches("[\\d+/*-\\.]*");
    }

}
