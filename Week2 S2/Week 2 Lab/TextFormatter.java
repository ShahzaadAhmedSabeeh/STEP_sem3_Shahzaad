import java.util.*;

public class TextFormatter {
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        try {
            System.out.println("Enter text:");
            String text = scanner.nextLine();
            System.out.println("Enter line width:");
            int width = scanner.nextInt();

            String[] words = splitWords(text);
            System.out.println("\nJustified Text:");
            justifyText(words, width);
            
            System.out.println("\nCenter Aligned Text:");
            centerText(words, width);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }

    public static String[] splitWords(String text) {
        List<String> words = new ArrayList<>();
        int start = 0;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == ' ') {
                if (start < i) words.add(text.substring(start, i));
                start = i + 1;
            }
        }
        if (start < text.length()) words.add(text.substring(start));
        return words.toArray(new String[0]);
    }

    public static void justifyText(String[] words, int width) {
        List<String> lines = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        int count = 0;

        for (String word : words) {
            if (count + word.length() + (current.length() == 0 ? 0 : 1) <= width) {
                if (current.length() > 0) current.append(" ");
                current.append(word);
                count += word.length() + 1;
            } else {
                lines.add(current.toString());
                current = new StringBuilder(word);
                count = word.length();
            }
        }
        if (current.length() > 0) lines.add(current.toString());

        for (String line : lines) {
            if (line.length() == width) {
                System.out.println(line);
                continue;
            }
            String[] ws = line.split(" ");
            if (ws.length == 1) {
                System.out.println(line);
                continue;
            }
            int extraSpaces = width - line.length();
            int spacesBetween = extraSpaces / (ws.length - 1);
            int remainder = extraSpaces % (ws.length - 1);

            StringBuilder justified = new StringBuilder();
            for (int i = 0; i < ws.length; i++) {
                justified.append(ws[i]);
                if (i < ws.length - 1) {
                    justified.append(" ");
                    for (int j = 0; j < spacesBetween; j++) justified.append(" ");
                    if (remainder > 0) {
                        justified.append(" ");
                        remainder--;
                    }
                }
            }
            System.out.println(justified);
        }
    }

    public static void centerText(String[] words, int width) {
        StringBuilder line = new StringBuilder();
        for (String word : words) {
            if (line.length() + word.length() + 1 > width) {
                printCenter(line.toString(), width);
                line = new StringBuilder(word);
            } else {
                if (line.length() > 0) line.append(" ");
                line.append(word);
            }
        }
        if (line.length() > 0) printCenter(line.toString(), width);
    }

    private static void printCenter(String line, int width) {
        int padding = (width - line.length()) / 2;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < padding; i++) sb.append(" ");
        sb.append(line);
        System.out.println(sb);
    }
}
