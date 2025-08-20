import java.util.Scanner;

public class FirstNonRepeatingChar {
    
    // Method to get length of string without using length()
    static int getLength(String s) {
        int count = 0;
        try {
            while (true) {
                s.charAt(count);
                count++;
            }
        } catch (Exception e) { //changed
        }

        
        return count;
    }

    // Method to find first non-repeating character
    static char findFirstNonRepeating(String s) {
        int len = getLength(s);
        int[] freq = new int[256];  // ASCII table size

        // Step 1: Count frequency of each character
        for (int i = 0; i < len; i++) {
            char ch = s.charAt(i);
            freq[ch]++;
        }

        // Step 2: Find first character with frequency == 1
        for (int i = 0; i < len; i++) {
            char ch = s.charAt(i);
            if (freq[ch] == 1) {
                return ch;
            }
        }

        return '\0'; // If no non-repeating character
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter a string: ");
        String text = sc.nextLine();

        char result = findFirstNonRepeating(text);

        if (result == '\0') {
            System.out.println("No non-repeating character found.");
        } else {
            System.out.println("First non-repeating character: " + result);
        }
    }
}
