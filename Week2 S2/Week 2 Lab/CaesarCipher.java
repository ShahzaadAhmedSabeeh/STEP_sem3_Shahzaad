import java.util.*;

public class CaesarCipher {
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.println("Enter text:");
            String text = scanner.nextLine();
            System.out.println("Enter shift value:");
            int shift = scanner.nextInt();

            String encrypted = encrypt(text, shift);
            String decrypted = decrypt(encrypted, shift);

            System.out.println("Original Text: " + text);
            System.out.println("Encrypted Text: " + encrypted);
            System.out.println("Decrypted Text: " + decrypted);
            System.out.println("Decryption correct? " + text.equals(decrypted));

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }

    public static String encrypt(String text, int shift) {
        StringBuilder sb = new StringBuilder();
        for (char c : text.toCharArray()) {
            if (c >= 'A' && c <= 'Z') {
                sb.append((char) ((c - 'A' + shift) % 26 + 'A'));
            } else if (c >= 'a' && c <= 'z') {
                sb.append((char) ((c - 'a' + shift) % 26 + 'a'));
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static String decrypt(String text, int shift) {
        return encrypt(text, 26 - (shift % 26));
    }
}
