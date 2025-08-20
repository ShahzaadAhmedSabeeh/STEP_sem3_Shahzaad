import java.util.Scanner;

public class SplitText {
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

    static String[] customSplit(String s) {
        int length = getLength(s);
        int spaces = 1;
        for (int i = 0; i < length; i++) {
            if (s.charAt(i) == ' ') spaces++;
        }
        int[] spaceIndex = new int[spaces + 1];
        int idx = 0;
        spaceIndex[idx++] = -1;
        for (int i = 0; i < length; i++) {
            if (s.charAt(i) == ' ') spaceIndex[idx++] = i;
        }
        spaceIndex[idx] = length;
        String[] words = new String[spaces];
        for (int i = 0; i < spaces; i++) {
            String word = "";
            for (int j = spaceIndex[i] + 1; j < spaceIndex[i + 1]; j++) {
                word += s.charAt(j);
            }
            words[i] = word;
        }
        return words;
    }

    static boolean compareArrays(String[] a, String[] b) {
        if (a.length != b.length) return false;
        for (int i = 0; i < a.length; i++) {
            if (!a[i].equals(b[i])) return false;
        }
        return true;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String text = sc.nextLine();
        String[] builtInSplit = text.split(" ");
        String[] customSplit = customSplit(text);
        boolean result = compareArrays(builtInSplit, customSplit);
        System.out.println("Built-in Split:");
        for (String w : builtInSplit) System.out.println(w);
        System.out.println("Custom Split:");
        for (String w : customSplit) System.out.println(w);
        System.out.println("Comparison Result: " + result);
    }
}
