import java.util.*;

public class SubstringReplacer {
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.println("Enter main text:");
            String mainText = scanner.nextLine();
            System.out.println("Enter substring to find:");
            String toFind = scanner.nextLine();
            System.out.println("Enter replacement substring:");
            String toReplace = scanner.nextLine();

            String manualResult = manualReplace(mainText, toFind, toReplace);
            String builtInResult = mainText.replace(toFind, toReplace);

            boolean isSame = compareWithBuiltIn(manualResult, builtInResult);

            System.out.println("\nOriginal Text: " + mainText);
            System.out.println("Manual Replace Result: " + manualResult);
            System.out.println("Built-in Replace Result: " + builtInResult);
            System.out.println("Match with built-in method? " + isSame);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }

    // Method to manually replace substrings
    public static String manualReplace(String text, String find, String replace) {
        if (find.isEmpty()) return text; // avoid infinite loop
        StringBuilder result = new StringBuilder();
        int i = 0;
        while (i < text.length()) {
            if (i + find.length() <= text.length() && text.substring(i, i + find.length()).equals(find)) {
                result.append(replace);
                i += find.length();
            } else {
                result.append(text.charAt(i));
                i++;
            }
        }
        return result.toString();
    }

    // Method to compare results with built-in replace()
    public static boolean compareWithBuiltIn(String manual, String builtIn) {
        return manual.equals(builtIn);
    }
}