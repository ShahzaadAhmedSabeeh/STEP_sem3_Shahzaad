import java.util.Scanner;

public class ShortestLongestWord {
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

    static String[][] wordsWithLength(String[] words) {
        String[][] result = new String[words.length][2];
        for (int i = 0; i < words.length; i++) {
            result[i][0] = words[i];
            result[i][1] = String.valueOf(getLength(words[i]));
        }
        return result;
    }

    static int[] findShortestLongest(String[][] arr) {
        int min = Integer.parseInt(arr[0][1]);
        int max = Integer.parseInt(arr[0][1]);
        int minIndex = 0, maxIndex = 0;
        for (int i = 1; i < arr.length; i++) {
            int len = Integer.parseInt(arr[i][1]);
            if (len < min) {
                min = len;
                minIndex = i;
            }
            if (len > max) {
                max = len;
                maxIndex = i;
            }
        }
        return new int[]{minIndex, maxIndex};
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String text = sc.nextLine();
        String[] words = customSplit(text);
        String[][] result = wordsWithLength(words);
        int[] indexes = findShortestLongest(result);
        System.out.println("Word\tLength");
        for (String[] row : result) {
            System.out.println(row[0] + "\t" + Integer.parseInt(row[1]));
        }
        System.out.println("Shortest Word: " + result[indexes[0]][0] + " (" + result[indexes[0]][1] + ")");
        System.out.println("Longest Word: " + result[indexes[1]][0] + " (" + result[indexes[1]][1] + ")");
    }
}
