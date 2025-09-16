import java.util.*;

public class EmailAnalyzer {
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.println("Enter number of emails:");
            int n = scanner.nextInt();
            scanner.nextLine();

            String[] emails = new String[n];
            for (int i = 0; i < n; i++) {
                System.out.print("Enter email " + (i + 1) + ": ");
                emails[i] = scanner.nextLine();
            }

            analyzeEmails(emails);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }

    public static void analyzeEmails(String[] emails) {
        int validCount = 0, invalidCount = 0;
        Map<String, Integer> domainCount = new HashMap<>();
        int totalUsernameLength = 0;

        System.out.printf("%-25s %-15s %-15s %-15s %-10s\n", "Email", "Username", "Domain", "Extension", "Valid?");
        
        for (String email : emails) {
            boolean valid = validateEmail(email);
            if (!valid) {
                System.out.printf("%-25s %-15s %-15s %-15s %-10s\n", email, "-", "-", "-", "Invalid");
                invalidCount++;
                continue;
            }

            String username = email.substring(0, email.indexOf('@'));
            String domainFull = email.substring(email.indexOf('@') + 1);
            String domainName = domainFull.contains(".") ? domainFull.substring(0, domainFull.lastIndexOf('.')) : domainFull;
            String extension = domainFull.contains(".") ? domainFull.substring(domainFull.lastIndexOf('.') + 1) : "";

            System.out.printf("%-25s %-15s %-15s %-15s %-10s\n", email, username, domainName, extension, "Valid");

            validCount++;
            totalUsernameLength += username.length();
            domainCount.put(domainName, domainCount.getOrDefault(domainName, 0) + 1);
        }

        System.out.println("\nStatistics:");
        System.out.println("Valid Emails: " + validCount);
        System.out.println("Invalid Emails: " + invalidCount);
        if (validCount > 0) {
            System.out.println("Average Username Length: " + (totalUsernameLength / validCount));
            String commonDomain = domainCount.entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .get().getKey();
            System.out.println("Most Common Domain: " + commonDomain);
        }
    }

    public static boolean validateEmail(String email) {
        int at = email.indexOf('@');
        int lastAt = email.lastIndexOf('@');
        if (at == -1 || at != lastAt) return false;
        if (email.indexOf('.', at) == -1) return false;
        if (at == 0 || at == email.length() - 1) return false;
        return true;
    }
}
