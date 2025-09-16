import java.util.*;

public class SpellChecker {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        try {
            System.out.println("Enter a sentence:");
            String sentence = sc.nextLine();

            System.out.println("Enter dictionary words separated by commas (e.g. hello,world,java):");
            String dictLine = sc.nextLine();
            String[] dictionary = parseCommaSeparated(dictLine);

            String[] words = splitIntoWords(sentence);
            System.out.printf("%-20s %-20s %-10s\n", "Original Word", "Suggestion", "Distance");
            System.out.println("--------------------------------------------------------");
            for (String w : words) {
                if (w.length() == 0) continue;
                String cleaned = w.toLowerCase();
                if (isExactInDictionary(cleaned, dictionary)) {
                    System.out.printf("%-20s %-20s %-10s\n", w, "-", "0 (Correct)");
                } else {
                    Suggestion s = findClosest(cleaned, dictionary);
                    if (s.distance <= 2) {
                        System.out.printf("%-20s %-20s %-10d\n", w, s.word, s.distance);
                    } else {
                        System.out.printf("%-20s %-20s %-10s\n", w, "No good suggestion", String.valueOf(s.distance));
                    }
                }
            }
        } finally {
            sc.close();
        }
    }

    // Split sentence into words without using split()
    public static String[] splitIntoWords(String sentence) {
        List<String> list = new ArrayList<>();
        int n = sentence.length();
        int start = -1;
        for (int i = 0; i < n; i++) {
            char c = sentence.charAt(i);
            if (Character.isLetterOrDigit(c) || c == '\'' || c == '-') {
                if (start == -1) start = i;
            } else {
                if (start != -1) {
                    list.add(sentence.substring(start, i));
                    start = -1;
                }
            }
        }
        if (start != -1) list.add(sentence.substring(start));
        return list.toArray(new String[0]);
    }

    // Check exact presence (case-insensitive)
    public static boolean isExactInDictionary(String word, String[] dict) {
        for (String d : dict) if (d.equalsIgnoreCase(word)) return true;
        return false;
    }

    // Find closest suggestion from dictionary using Levenshtein distance
    public static Suggestion findClosest(String word, String[] dict) {
        int best = Integer.MAX_VALUE;
        String bestWord = null;
        for (String d : dict) {
            int dist = levenshteinDistance(word, d.toLowerCase());
            if (dist < best) {
                best = dist;
                bestWord = d;
            }
        }
        return new Suggestion(bestWord == null ? "" : bestWord, best == Integer.MAX_VALUE ? -1 : best);
    }

    // Levenshtein distance (insertion/deletion/substitution) implemented with arrays
    public static int levenshteinDistance(String a, String b) {
        int n = a.length(), m = b.length();
        if (n == 0) return m;
        if (m == 0) return n;
        int[] prev = new int[m + 1];
        int[] cur = new int[m + 1];
        for (int j = 0; j <= m; j++) prev[j] = j;
        for (int i = 1; i <= n; i++) {
            cur[0] = i;
            for (int j = 1; j <= m; j++) {
                int cost = (a.charAt(i - 1) == b.charAt(j - 1)) ? 0 : 1;
                cur[j] = Math.min(Math.min(cur[j - 1] + 1, prev[j] + 1), prev[j - 1] + cost);
            }
            int[] tmp = prev; prev = cur; cur = tmp;
        }
        return prev[m];
    }

    // Utility: parse comma separated dictionary
    public static String[] parseCommaSeparated(String line) {
        List<String> list = new ArrayList<>();
        int start = 0;
        for (int i = 0; i < line.length(); i++) {
            if (line.charAt(i) == ',') {
                String part = line.substring(start, i).trim();
                if (!part.isEmpty()) list.add(part);
                start = i + 1;
            }
        }
        String last = line.substring(start).trim();
        if (!last.isEmpty()) list.add(last);
        return list.toArray(new String[0]);
    }

    static class Suggestion {
        String word;
        int distance;
        Suggestion(String w, int d) { word = w; distance = d; }
    }
}
