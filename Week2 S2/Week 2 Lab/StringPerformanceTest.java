import java.util.*;

public class StringPerformanceTest {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.print("Enter number of iterations: ");
            int iterations = scanner.nextInt();

            testPerformance(iterations);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }

    public static void testPerformance(int iterations) {
        long timeString = testStringConcat(iterations);
        long timeBuilder = testStringBuilder(iterations);
        long timeBuffer = testStringBuffer(iterations);

        System.out.printf("%-20s %-20s\n", "Method", "Time (ms)");
        System.out.printf("%-20s %-20d\n", "String (+)", timeString);
        System.out.printf("%-20s %-20d\n", "StringBuilder", timeBuilder);
        System.out.printf("%-20s %-20d\n", "StringBuffer", timeBuffer);
    }

    public static long testStringConcat(int iterations) {
        long start = System.currentTimeMillis();
        String s = "";
        for (int i = 0; i < iterations; i++) {
            s += "a";
        }
        long end = System.currentTimeMillis();
        return end - start;
    }

    public static long testStringBuilder(int iterations) {
        long start = System.currentTimeMillis();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < iterations; i++) {
            sb.append("a");
        }
        long end = System.currentTimeMillis();
        return end - start;
    }

    public static long testStringBuffer(int iterations) {
        long start = System.currentTimeMillis();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < iterations; i++) {
            sb.append("a");
        }
        long end = System.currentTimeMillis();
        return end - start;
    }
}
