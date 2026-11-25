import java.util.Stack;

public class Calculator {
    public static int evaluate(String expression) {
        if (expression == null || expression.isBlank()) {
            throw new IllegalArgumentException("Empty expression");
        }
        Stack<Integer> values = new Stack<>();
        Stack<Character> operators = new Stack<>();
        int i = 0;
        boolean expectOperand = true;
        while (i < expression.length()) {
            char c = expression.charAt(i);
            if (Character.isWhitespace(c)) {
                i++;
                continue;
            }
            if (Character.isDigit(c)) {
                if (!expectOperand) {
                    throw new IllegalArgumentException("Invalid expression");
                }
                int start = i;
                while (i < expression.length() && Character.isDigit(expression.charAt(i))) {
                    i++;
                }
                try {
                    values.push(Integer.parseInt(expression.substring(start, i)));
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid number");
                }
                expectOperand = false;
                continue;
            }
            if (c == '(') {
                if (!expectOperand) {
                    throw new IllegalArgumentException("Invalid expression");
                }
                operators.push(c);
            }
            else if (c == ')') {
                if (expectOperand) {
                    throw new IllegalArgumentException("Invalid expression");
                }
                while (!operators.isEmpty() && operators.peek() != '(') {
                    apply(values, operators);
                }
                if (operators.isEmpty()) {
                    throw new IllegalArgumentException("Invalid brackets");
                }
                operators.pop();
                while (!operators.isEmpty() && isUnaryOperator(operators.peek())) {
                    apply(values, operators);
                }
            }
            else if (isOperator(c)) {
                if (expectOperand) {
                    if (c == '-') {
                        operators.push('u');
                    } else if (c == '+') {
                        operators.push('p');
                    } else {
                        throw new IllegalArgumentException("Invalid expression");
                    }
                }
                else {
                    while (!operators.isEmpty() && operators.peek() != '(' && priority(operators.peek()) >= priority(c)) {
                        apply(values, operators);
                    }
                    operators.push(c);
                    expectOperand = true;
                }
            }
            else {
                throw new IllegalArgumentException("Invalid character: " + c);
            }
            i++;
        }
        if (expectOperand) {
            throw new IllegalArgumentException("Invalid expression");
        }
        while (!operators.isEmpty()) {
            if (operators.peek() =='(') {
                throw new IllegalArgumentException("Invalid brackets");
            }
            apply(values, operators);
        }
        if (values.size() != 1) {
            throw new IllegalArgumentException("Invalid expression");
        }
        return values.pop();
    }
    private static void apply(Stack<Integer> values, Stack<Character> operators) {
        if (operators.isEmpty()) {
            throw new IllegalArgumentException("Invalid expression");
        }
        char op = operators.pop();
        if (isUnaryOperator(op)) {
            if (values.isEmpty()) {
                throw new IllegalArgumentException("Invalid expression");
            }
            int value = values.pop();
            if (op == 'u') {
                values.push(-value);
            } else {
                values.push(value);
            }
            return;
        }
        if (values.size() < 2) {
            throw new IllegalArgumentException("Invalid expression");
        }
        int b = values.pop();
        int a = values.pop();
        switch (op) {
            case '+' -> values.push(a + b);
            case '-' -> values.push(a - b);
            case '*' -> values.push(a * b);
            case '/' -> {
                if (b == 0) {
                    throw new IllegalArgumentException("Division by zero");
                }
                values.push(a / b);
            }
            default -> throw new IllegalArgumentException("Unknown operator");
        }
    }
    private static boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }
    // u - unar minus, p - plus
    private static boolean isUnaryOperator(char c) {
        return c == 'u' || c == 'p';
    }
    private static int priority(char op) {
        if (isUnaryOperator(op)) {
            return 3;
        }
        if (op == '*' || op == '/') {
            return 2;
        }
        if (op == '+' || op == '-') {
            return 1;
        }
        return 0;
    }
}