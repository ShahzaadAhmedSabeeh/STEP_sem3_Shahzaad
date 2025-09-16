import java.util.*;

public class CSVProcessor {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        try {
            System.out.println("Enter CSV-like data (multiple lines). End with a blank line:");
            StringBuilder input = new StringBuilder();
            while (true) {
                String line = sc.nextLine();
                if (line.trim().isEmpty()) break;
                input.append(line).append("\n");
            }
            List<List<String>> table = parseCSV(input.toString());
            // clean & validate
            List<List<String>> clean = cleanAndValidate(table);
            // analyze numeric columns where possible
            AnalysisResult res = analyze(clean);
            // format output
            System.out.println(formatTable(clean));
            System.out.println("\n--- Summary Report ---");
            System.out.println("Total records: " + (clean.size() - 1)); // excluding header
            for (int i = 0; i < res.numericIdx.size(); i++) {
                int col = res.numericIdx.get(i);
                System.out.printf("Col %d min=%.2f max=%.2f avg=%.2f\n",
                        col, res.mins.get(i), res.maxs.get(i), res.avgs.get(i));
            }
            System.out.println("Completeness: " + String.format("%.2f%%", res.completeness * 100.0));
            if (!res.qualityIssues.isEmpty()) {
                System.out.println("Data quality issues:");
                for (String q : res.qualityIssues) System.out.println("- " + q);
            }
        } finally {
            sc.close();
        }
    }

    // Parse CSV-like text without split(), handle quoted fields that may contain commas
    public static List<List<String>> parseCSV(String text) {
        List<List<String>> rows = new ArrayList<>();
        int i = 0;
        int n = text.length();
        List<String> currentRow = new ArrayList<>();
        StringBuilder curField = new StringBuilder();
        boolean inQuotes = false;
        while (i < n) {
            char c = text.charAt(i);
            if (c == '"') {
                if (inQuotes && i + 1 < n && text.charAt(i + 1) == '"') {
                    // escaped quote
                    curField.append('"');
                    i += 2;
                    continue;
                } else {
                    inQuotes = !inQuotes;
                    i++;
                    continue;
                }
            }
            if (!inQuotes && c == ',') {
                currentRow.add(curField.toString());
                curField.setLength(0);
                i++;
                continue;
            }
            if (!inQuotes && (c == '\n' || c == '\r')) {
                // handle CRLF
                if (i + 1 < n && c == '\r' && text.charAt(i + 1) == '\n') i++;
                currentRow.add(curField.toString());
                curField.setLength(0);
                rows.add(currentRow);
                currentRow = new ArrayList<>();
                i++;
                continue;
            }
            curField.append(c);
            i++;
        }
        // last field/row
        if (curField.length() > 0 || !currentRow.isEmpty()) {
            currentRow.add(curField.toString());
            rows.add(currentRow);
        }
        return rows;
    }

    // Trim fields and simple validation
    public static List<List<String>> cleanAndValidate(List<List<String>> table) {
        List<List<String>> cleaned = new ArrayList<>();
        int maxCols = 0;
        for (List<String> r : table) maxCols = Math.max(maxCols, r.size());
        for (List<String> r : table) {
            List<String> row = new ArrayList<>();
            for (int i = 0; i < maxCols; i++) {
                String val = i < r.size() ? r.get(i).trim() : "";
                row.add(val);
            }
            cleaned.add(row);
        }
        return cleaned;
    }

    // Analyze numeric columns (min, max, average), count quality issues
    static class AnalysisResult {
        List<Integer> numericIdx = new ArrayList<>();
        List<Double> mins = new ArrayList<>();
        List<Double> maxs = new ArrayList<>();
        List<Double> avgs = new ArrayList<>();
        double completeness;
        List<String> qualityIssues = new ArrayList<>();
    }

    public static AnalysisResult analyze(List<List<String>> table) {
        AnalysisResult res = new AnalysisResult();
        if (table.isEmpty()) return res;
        int rows = table.size();
        int cols = table.get(0).size();
        // assume header row present
        int dataRows = rows - 1;
        int filledCells = 0;
        for (int c = 0; c < cols; c++) {
            boolean numeric = true;
            double sum = 0; int count = 0;
            double min = Double.POSITIVE_INFINITY, max = Double.NEGATIVE_INFINITY;
            for (int r = 1; r < rows; r++) {
                String v = table.get(r).get(c);
                if (!v.isEmpty()) filledCells++;
                if (v.isEmpty()) { numeric = numeric && false; continue; }
                if (isNumericString(v)) {
                    double val = Double.parseDouble(v);
                    sum += val; count++;
                    if (val < min) min = val;
                    if (val > max) max = val;
                } else {
                    numeric = false;
                    // continue checking to gather quality issues
                }
            }
            if (numeric && count > 0) {
                res.numericIdx.add(c);
                res.mins.add(min);
                res.maxs.add(max);
                res.avgs.add(sum / count);
            } else {
                // check for missing/invalid
                for (int r = 1; r < rows; r++) {
                    String v = table.get(r).get(c);
                    if (v.isEmpty()) res.qualityIssues.add("Missing value at row " + (r+1) + " col " + (c+1));
                }
            }
        }
        int totalCells = (rows - 1) * cols;
        res.completeness = totalCells == 0 ? 1.0 : ((double) filledCells) / totalCells;
        return res;
    }

    // Check numeric using ASCII values (no regex)
    public static boolean isNumericString(String s) {
        int dots = 0;
        if (s.length() == 0) return false;
        int start = (s.charAt(0) == '+' || s.charAt(0) == '-') ? 1 : 0;
        for (int i = start; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '.') {
                dots++;
                if (dots > 1) return false;
                continue;
            }
            if (!(c >= '0' && c <= '9')) return false;
        }
        return true;
    }

    // Format table using StringBuilder
    public static String formatTable(List<List<String>> table) {
        StringBuilder sb = new StringBuilder();
        if (table.isEmpty()) return "";
        int cols = table.get(0).size();
        int[] widths = new int[cols];
        for (List<String> r : table) {
            for (int c = 0; c < cols; c++) widths[c] = Math.max(widths[c], r.get(c).length());
        }
        // header border
        sb.append("+");
        for (int w : widths) { for (int i = 0; i < w + 2; i++) sb.append("-"); sb.append("+"); }
        sb.append("\n");
        for (int r = 0; r < table.size(); r++) {
            List<String> row = table.get(r);
            sb.append("|");
            for (int c = 0; c < cols; c++) {
                String v = row.get(c);
                sb.append(" ").append(padRight(v, widths[c])).append(" |");
            }
            sb.append("\n");
            // header separator after first row
            if (r == 0) {
                sb.append("+");
                for (int w : widths) { for (int i = 0; i < w + 2; i++) sb.append("="); sb.append("+"); }
                sb.append("\n");
            } else {
                sb.append("+");
                for (int w : widths) { for (int i = 0; i < w + 2; i++) sb.append("-"); sb.append("+"); }
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    private static String padRight(String s, int w) {
        StringBuilder sb = new StringBuilder(s);
        while (sb.length() < w) sb.append(' ');
        return sb.toString();
    }
}
