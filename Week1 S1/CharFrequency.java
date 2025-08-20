import java.util.Scanner;

public class CharFrequency {

    static int[][] findFrequency(String text) {
        int[] freq = new int[256];
        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            freq[ch]++;
        }

        int count = 0;
        for (int i = 0; i < text.length(); i++) {
            if (freq[text.charAt(i)] > 0) {
                count++;
                freq[text.charAt(i)] = -freq[text.charAt(i)];
            }
        }

        int[][] result = new int[count][2];
        int index = 0;
        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            if (freq[ch] < 0) {
                result[index][0] = ch;
                result[index][1] = -freq[ch];
                freq[ch] = 0;
                index++;
            }
        }
        return result;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter a string: ");
        String input = sc.nextLine();

        int[][] freqArray = findFrequency(input);

        System.out.println("Character Frequencies:");
        for (int i = 0; i < freqArray.length; i++) {
            System.out.println((char) freqArray[i][0] + " -> " + freqArray[i][1]);
        }

        sc.close();
    }
}

