import java.util.*;

public class TextCalculator {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        try {
            System.out.println("Enter expression(s). Enter a blank line to stop.");
            List<String> expressions = new ArrayList<>();
            while (true) {
                String line = sc.nextLine();
                if (line.trim().isEmpty()) break;
                expressions.add(line);
            }
            for (String expr : expressions) {
                System.out.println("Expression: " + expr);
                if (!isValidExpression(expr)) {
                    System.out.println("Invalid expression format.");
                    continue;
                }
                StringBuilder steps = new StringBuilder();
                double result = evaluateWithParentheses(expr, steps);
                System.out.println("Steps:\n" + steps.toString());
                System.out.println("Result: " + result);
                System.out.println("-------------------------------------------------");
            }
        } finally {
            sc.close();
        }
    }

    // Validate acceptable characters and parentheses balance
    public static boolean isValidExpression(String expr) {
        int bal = 0;
        boolean lastWasOp = true; // disallow starting with op except unary minus handled later
        for (int i = 0; i < expr.length(); i++) {
            char c = expr.charAt(i);
            if (c == ' ') continue;
            if (c == '(') { bal++; lastWasOp = true; }
            else if (c == ')') { bal--; if (bal < 0) return false; lastWasOp = false; }
            else if (isOperator(c)) {
                // allow unary minus when lastWasOp==true and c=='-'
                if (!lastWasOp && c!='+') lastWasOp = true;
                else if (lastWasOp && c=='-') { /* unary minus allowed */ }
                else if (lastWasOp && c!='-') return false;
            } else if (Character.isDigit(c) || c=='.') {
                lastWasOp = false;
            } else return false;
        }
        return bal == 0;
    }

    private static boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }

    // Recursively evaluate parentheses using innermost first
    public static double evaluateWithParentheses(String expr, StringBuilder steps) {
        String s = expr;
        while (true) {
            int close = s.indexOf(')');
            if (close == -1) break;
            int open = s.lastIndexOf('(', close);
            if (open == -1) throw new IllegalArgumentException("Mismatched parentheses");
            String inner = s.substring(open + 1, close);
            double innerVal = evaluateSimple(inner, steps);
            steps.append(String.format("Evaluated (%s) -> %s\n", inner, formatDouble(innerVal)));
            s = s.substring(0, open) + formatDouble(innerVal) + s.substring(close + 1);
        }
        double result = evaluateSimple(s, steps);
        return result;
    }

    // Evaluate expression without parentheses: handle * / first, then + -
    public static double evaluateSimple(String expr, StringBuilder steps) {
        // parse numbers and operators
        List<Double> numbers = new ArrayList<>();
        List<Character> ops = new ArrayList<>();
        int i = 0;
        String trimmed = expr.trim();
        while (i < trimmed.length()) {
            char c = trimmed.charAt(i);
            if (c == ' ') { i++; continue; }
            // parse number (with possible unary + or -)
            int start = i;
            if ((c == '+' || c == '-') && (i == 0 || isOperator(trimmed.charAt(i - 1)) || trimmed.charAt(i - 1) == '(')) {
                i++; // unary sign included
                while (i < trimmed.length() && (Character.isDigit(trimmed.charAt(i)) || trimmed.charAt(i) == '.')) i++;
            } else if (Character.isDigit(c) || c == '.') {
                while (i < trimmed.length() && (Character.isDigit(trimmed.charAt(i)) || trimmed.charAt(i) == '.')) i++;
            } else if (isOperator(c)) {
                // operator between numbers
                ops.add(c);
                i++;
                continue;
            } else {
                throw new IllegalArgumentException("Invalid character in expression: " + c);
            }
            String numStr = trimmed.substring(start, i);
            double val = Double.parseDouble(numStr);
            numbers.add(val);
            // after number, if an operator follows, record it
            while (i < trimmed.length() && trimmed.charAt(i) == ' ') i++;
            if (i < trimmed.length() && isOperator(trimmed.charAt(i))) {
                ops.add(trimmed.charAt(i));
                i++;
            }
        }

        // First pass: handle * and /
        for (int idx = 0; idx < ops.size(); ) {
            char op = ops.get(idx);
            if (op == '*' || op == '/') {
                double a = numbers.get(idx);
                double b = numbers.get(idx + 1);
                double res = (op == '*') ? (a * b) : (a / b);
                numbers.set(idx, res);
                numbers.remove(idx + 1);
                ops.remove(idx);
                steps.append(String.format("Performed %s %c %s = %s\n", formatDouble(a), op, formatDouble(b), formatDouble(res)));
            } else {
                idx++;
            }
        }

        // Second pass: handle + and - left to right
        double acc = numbers.get(0);
        for (int idx = 0; idx < ops.size(); idx++) {
            char op = ops.get(idx);
            double b = numbers.get(idx + 1);
            double prev = acc;
            if (op == '+') acc = acc + b;
            else acc = acc - b;
            steps.append(String.format("Performed %s %c %s = %s\n", formatDouble(prev), op, formatDouble(b), formatDouble(acc)));
        }
        return acc;
    }

    private static String formatDouble(double d) {
        if (d == (long) d) return String.format("%d", (long) d);
        else return String.format("%.6f", d).replaceAll("0+$", "").replaceAll("\\.$", "");
    }
}
