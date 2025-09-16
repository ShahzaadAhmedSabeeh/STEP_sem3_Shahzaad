import java.util.*;

public class TextCaseConverter {
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.println("Enter text:");
            String text = scanner.nextLine();

            String upper = toUpperCaseASCII(text);
            String lower = toLowerCaseASCII(text);
            String title = toTitleCaseASCII(text);

            System.out.printf("%-20s %-20s %-20s\n", "Original", "Uppercase", "Lowercase");
            System.out.printf("%-20s %-20s %-20s\n", text, upper, lower);
            System.out.println("Title Case: " + title);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }

    public static String toUpperCaseASCII(String text) {
        StringBuilder sb = new StringBuilder();
        for (char c : text.toCharArray()) {
            if (c >= 'a' && c <= 'z') {
                sb.append((char)(c - 32));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static String toLowerCaseASCII(String text) {
        StringBuilder sb = new StringBuilder();
        for (char c : text.toCharArray()) {
            if (c >= 'A' && c <= 'Z') {
                sb.append((char)(c + 32));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static String toTitleCaseASCII(String text) {
        String lower = toLowerCaseASCII(text);
        StringBuilder sb = new StringBuilder();
        boolean capitalize = true;
        for (char c : lower.toCharArray()) {
            if (capitalize && c >= 'a' && c <= 'z') {
                sb.append((char)(c - 32));
                capitalize = false;
            } else {
                sb.append(c);
            }
            if (c == ' ') capitalize = true;
        }
        return sb.toString();
    }
}
