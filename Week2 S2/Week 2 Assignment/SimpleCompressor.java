import java.util.*;

public class SimpleCompressor {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        try {
            System.out.println("Enter text to compress:");
            String text = sc.nextLine();

            // count frequencies
            CharFreq cf = countFrequencies(text);
            // build mapping (short codes for frequent chars)
            Mapping map = createMapping(cf.chars, cf.freqs);
            // compress
            String compressed = compressText(text, map);
            // decompress
            String decompressed = decompressText(compressed, map);

            // display analysis
            System.out.println("Character Frequency:");
            System.out.printf("%-8s %-8s\n", "Char", "Freq");
            for (int i = 0; i < cf.chars.length; i++) {
                System.out.printf("%-8s %-8d\n", readableChar(cf.chars[i]), cf.freqs[i]);
            }
            System.out.println("\nMapping (char -> code):");
            for (int i = 0; i < map.chars.length; i++) {
                System.out.printf("%-8s -> %s\n", readableChar(map.chars[i]), map.codes[i]);
            }
            System.out.println("\nOriginal length (chars): " + text.length());
            System.out.println("Compressed representation length (chars): " + compressed.length());
            System.out.println("Compression ratio (compressed/original): " +
                    String.format("%.2f", (double) compressed.length() / Math.max(1, text.length())));
            System.out.println("\nCompressed string:\n" + compressed);
            System.out.println("\nDecompressed string:\n" + decompressed);
            System.out.println("\nDecompression valid? " + decompressed.equals(text));
        } finally {
            sc.close();
        }
    }

    // Count frequencies without HashMap; returns unique characters and freqs
    static class CharFreq { char[] chars; int[] freqs; }
    public static CharFreq countFrequencies(String text) {
        List<Character> chars = new ArrayList<>();
        List<Integer> freqs = new ArrayList<>();
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            int idx = indexOfChar(chars, c);
            if (idx == -1) { chars.add(c); freqs.add(1); }
            else freqs.set(idx, freqs.get(idx) + 1);
        }
        CharFreq cf = new CharFreq();
        cf.chars = new char[chars.size()];
        cf.freqs = new int[freqs.size()];
        for (int i = 0; i < chars.size(); i++) {
            cf.chars[i] = chars.get(i);
            cf.freqs[i] = freqs.get(i);
        }
        return cf;
    }

    private static int indexOfChar(List<Character> list, char c) {
        for (int i = 0; i < list.size(); i++) if (list.get(i) == c) return i;
        return -1;
    }

    // Create mapping: assign short numeric codes (1,2,3,...) but order by frequency (most freq -> smallest code)
    static class Mapping { char[] chars; String[] codes; }
    public static Mapping createMapping(char[] chars, int[] freqs) {
        int n = chars.length;
        // create index array
        Integer[] idx = new Integer[n];
        for (int i = 0; i < n; i++) idx[i] = i;
        Arrays.sort(idx, (a, b) -> Integer.compare(freqs[b], freqs[a])); // descending freq

        Mapping m = new Mapping();
        m.chars = new char[n];
        m.codes = new String[n];
        for (int rank = 0; rank < n; rank++) {
            int originalIndex = idx[rank];
            m.chars[rank] = chars[originalIndex];
            // code as rank+1 but converted to a small symbolic code (e.g., "#1", "#2") to avoid ambiguous concatenation
            m.codes[rank] = "#" + (rank + 1);
        }
        return m;
    }

    // Compress by replacing each char with its code using StringBuilder
    public static String compressText(String text, Mapping map) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            int idx = findIndex(map.chars, c);
            if (idx == -1) return ""; // error
            sb.append(map.codes[idx]);
        }
        return sb.toString();
    }

    // Decompress by scanning codes; codes start with '#' so we can parse easily
    public static String decompressText(String compressed, Mapping map) {
        StringBuilder out = new StringBuilder();
        int i = 0;
        while (i < compressed.length()) {
            char c = compressed.charAt(i);
            if (c == '#') {
                int j = i + 1;
                while (j < compressed.length() && Character.isDigit(compressed.charAt(j))) j++;
                String num = compressed.substring(i, j); // "#<digits>"
                int idx = findCodeIndex(map.codes, num);
                if (idx == -1) return ""; // invalid
                out.append(map.chars[idx]);
                i = j;
            } else {
                // safety: if mapping used different scheme
                i++;
            }
        }
        return out.toString();
    }

    private static int findIndex(char[] arr, char c) {
        for (int i = 0; i < arr.length; i++) if (arr[i] == c) return i;
        return -1;
    }

    private static int findCodeIndex(String[] codes, String code) {
        for (int i = 0; i < codes.length; i++) if (codes[i].equals(code)) return i;
        return -1;
    }

    private static String readableChar(char c) {
        if (c == ' ') return "' '";
        if (c == '\n') return "\\n";
        if (c == '\t') return "\\t";
        return String.valueOf(c);
    }
}
