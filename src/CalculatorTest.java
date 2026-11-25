// Тест без Junit
public class CalculatorTest {
    public static void main(String[] args) {
        //Корректные выражения
        test("2+3*4", 14);
        test("(2+3)*4", 20);
        test("10/2-3", 2);
        test("2*(3+4)-5", 9);
        test(" 2 + 2 ", 4);
        test("--2", 2);
        test("+2", 2);
        test("-2", -2);
        test("-(2+3)", -5);
        test(" 16 / 2 + (9 - 3) * 2 - 2 + (1-2 + (1+2)) ", 20);
        //Некорректные выражения
        testError("10/0");
        testError("2+");
        testError("(2+3");
        testError("2+a");

        System.out.println("All tests passed!");
    }
    private static void test(String expression, int expected) {
        int actual = Calculator.evaluate(expression);
        if (actual != expected) {
            throw new AssertionError(expression + ": expected " + expected + ", was " + actual);
        }
        System.out.println("[PASS] " + expression + " = " + actual);
    }
    private static void testError(String expression) {
        try {
            Calculator.evaluate(expression);
            throw new AssertionError(expression + ": expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            System.out.println("[PASS] " + expression + " : " + e.getMessage());
        }
    }
}
