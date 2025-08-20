import java.util.Scanner;

public class UniqueCharacters {
    static int getLength(String s) {
        int count = 0;
        try {
            while (true) {
                s.charAt(count);
                count++;
            }
        } catch (Exception e) {
        }
        return count;
    }

    static char[] findUnique(String s) {
        int len = getLength(s);
        char[] temp = new char[len];
        int uniqueCount = 0;

        for (int i = 0; i < len; i++) {
            char current = s.charAt(i);
            boolean found = false;
            for (int j = 0; j < uniqueCount; j++) {
                if (temp[j] == current) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                temp[uniqueCount] = current;
                uniqueCount++;
            }
        }

        char[] unique = new char[uniqueCount];
        for (int i = 0; i < uniqueCount; i++) {
            unique[i] = temp[i];
        }
        return unique;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String text = sc.nextLine();
        char[] result = findUnique(text);
        System.out.println("Unique Characters:");
        for (char c : result) {
            System.out.print(c + " ");
        }
    }
}
