import java.util.*;

public class PasswordTool {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        try {
            System.out.println("Enter number of passwords to analyze:");
            int n = Integer.parseInt(sc.nextLine());
            String[] passwords = new String[n];
            for (int i = 0; i < n; i++) {
                System.out.print("Password " + (i + 1) + ": ");
                passwords[i] = sc.nextLine();
            }

            System.out.printf("%-20s %-6s %-8s %-8s %-6s %-12s %-6s %-10s\n",
                    "Password", "Len", "Upper", "Lower", "Digits", "SpecialChars", "Score", "Strength");
            System.out.println("---------------------------------------------------------------------------------------");

            for (String p : passwords) {
                Analysis a = analyzePassword(p);
                System.out.printf("%-20s %-6d %-8d %-8d %-6d %-12d %-6d %-10s\n",
                        maskForDisplay(p), a.length, a.upper, a.lower, a.digits, a.special, a.score, a.strength);
            }

            System.out.println("\nGenerate a strong password? Enter desired length (e.g. 12), or 0 to skip:");
            int len = Integer.parseInt(sc.nextLine());
            if (len > 0) {
                String gen = generateStrongPassword(len);
                System.out.println("Generated strong password: " + gen);
                System.out.println("Analysis of generated password:");
                Analysis ga = analyzePassword(gen);
                System.out.printf("Score: %d, Strength: %s\n", ga.score, ga.strength);
            }
        } finally {
            sc.close();
        }
    }

    static class Analysis {
        int length, upper, lower, digits, special, score;
        String strength;
    }

    // Analyze password using ASCII categories
    public static Analysis analyzePassword(String p) {
        Analysis a = new Analysis();
        a.length = p.length();
        for (int i = 0; i < p.length(); i++) {
            int code = p.charAt(i);
            if (code >= 65 && code <= 90) a.upper++;
            else if (code >= 97 && code <= 122) a.lower++;
            else if (code >= 48 && code <= 57) a.digits++;
            else if (code >= 33 && code <= 126) a.special++; // printable specials
        }
        int score = 0;
        if (a.length > 8) score += 2 * (a.length - 8);
        int types = 0;
        if (a.upper > 0) { score += 10; types++; }
        if (a.lower > 0) { score += 10; types++; }
        if (a.digits > 0) { score += 10; types++; }
        if (a.special > 0) { score += 10; types++; }
        // Simple pattern checks
        String lower = p.toLowerCase();
        if (lower.contains("123") || lower.contains("abc") || lower.contains("qwerty")) score -= 10;
        if (p.matches(".*(.)\\1{3,}.*")) score -= 5; // repeated char penalty

        a.score = Math.max(0, score);
        if (a.score <= 20) a.strength = "Weak";
        else if (a.score <= 50) a.strength = "Medium";
        else a.strength = "Strong";
        return a;
    }

    // Generate strong password meeting categories
    public static String generateStrongPassword(int length) {
        if (length < 4) length = 4; // ensure room for each category
        Random rnd = new Random();
        String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lower = "abcdefghijklmnopqrstuvwxyz";
        String digits = "0123456789";
        String special = "!@#$%^&*()-_=+[]{};:,.<>/?";
        StringBuilder sb = new StringBuilder();

        // ensure at least one from each category
        sb.append(upper.charAt(rnd.nextInt(upper.length())));
        sb.append(lower.charAt(rnd.nextInt(lower.length())));
        sb.append(digits.charAt(rnd.nextInt(digits.length())));
        sb.append(special.charAt(rnd.nextInt(special.length())));

        String all = upper + lower + digits + special;
        for (int i = 4; i < length; i++) {
            sb.append(all.charAt(rnd.nextInt(all.length())));
        }

        // shuffle
        char[] arr = sb.toString().toCharArray();
        for (int i = 0; i < arr.length; i++) {
            int j = rnd.nextInt(arr.length);
            char tmp = arr[i]; arr[i] = arr[j]; arr[j] = tmp;
        }
        return new String(arr);
    }

    private static String maskForDisplay(String p) {
        if (p.length() <= 4) return p.replaceAll(".", "*");
        StringBuilder sb = new StringBuilder();
        sb.append(p.substring(0, 2));
        for (int i = 2; i < p.length() - 2; i++) sb.append('*');
        sb.append(p.substring(p.length() - 2));
        return sb.toString();
    }
}
