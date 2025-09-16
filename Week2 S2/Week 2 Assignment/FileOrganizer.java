import java.util.*;
import java.text.SimpleDateFormat;

public class FileOrganizer {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        try {
            System.out.println("Enter number of filenames:");
            int n = Integer.parseInt(sc.nextLine());
            String[] filenames = new String[n];
            for (int i = 0; i < n; i++) {
                System.out.print("File " + (i+1) + ": ");
                filenames[i] = sc.nextLine();
            }
            List<FileInfo> infos = new ArrayList<>();
            for (String fn : filenames) infos.add(parseFileName(fn));

            Map<String, List<FileInfo>> categories = categorize(infos);
            Map<String, String> suggestions = generateNewNames(categories);

            // display report
            System.out.printf("%-30s %-12s %-30s\n", "Original Name", "Category", "Suggested New Name");
            System.out.println("-----------------------------------------------------------------------------------");
            for (String orig : suggestions.keySet()) {
                FileInfo fi = findByOriginal(infos, orig);
                System.out.printf("%-30s %-12s %-30s\n", orig, fi.category, suggestions.get(orig));
            }
            System.out.println("\nCategory counts:");
            for (String cat : categories.keySet()) {
                System.out.println(cat + ": " + categories.get(cat).size());
            }

            System.out.println("\nBatch rename commands (simulated):");
            for (String orig : suggestions.keySet()) {
                System.out.println("mv \"" + orig + "\" \"" + suggestions.get(orig) + "\"");
            }
        } finally {
            sc.close();
        }
    }

    static class FileInfo {
        String original;
        String name;
        String extension;
        String category;
    }

    // Parse filename into name and extension without split()
    public static FileInfo parseFileName(String file) {
        FileInfo fi = new FileInfo();
        fi.original = file;
        int lastDot = file.lastIndexOf('.');
        if (lastDot == -1 || lastDot == 0 || lastDot == file.length() - 1) {
            fi.name = file;
            fi.extension = "";
        } else {
            fi.name = file.substring(0, lastDot);
            fi.extension = file.substring(lastDot + 1).toLowerCase();
        }
        fi.category = "Unknown";
        return fi;
    }

    // Categorize by extension
    public static Map<String, List<FileInfo>> categorize(List<FileInfo> infos) {
        Map<String, List<FileInfo>> map = new LinkedHashMap<>();
        // categories
        String[] docs = {"txt","doc","docx","pdf","odt"};
        String[] images = {"jpg","jpeg","png","gif","bmp","svg"};
        String[] code = {"java","py","c","cpp","js","cs","rb","php"};
        String[] audio = {"mp3","wav","ogg","m4a"};
        String[] video = {"mp4","mov","avi","mkv"};
        for (FileInfo fi : infos) {
            String ext = fi.extension;
            if (contains(docs, ext)) fi.category = "Document";
            else if (contains(images, ext)) fi.category = "Image";
            else if (contains(code, ext)) fi.category = "Code";
            else if (contains(audio, ext)) fi.category = "Audio";
            else if (contains(video, ext)) fi.category = "Video";
            else if (ext.equals("")) fi.category = "NoExt";
            else fi.category = "Other";
            map.computeIfAbsent(fi.category, k -> new ArrayList<>()).add(fi);
        }
        return map;
    }

    private static boolean contains(String[] arr, String s) {
        for (String x : arr) if (x.equalsIgnoreCase(s)) return true;
        return false;
    }

    private static FileInfo findByOriginal(List<FileInfo> infos, String orig) {
        for (FileInfo fi : infos) if (fi.original.equals(orig)) return fi;
        return null;
    }

    // Generate new names using category + date + sequence
    public static Map<String, String> generateNewNames(Map<String, List<FileInfo>> categories) {
        Map<String, String> suggestions = new LinkedHashMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String date = sdf.format(new Date());
        for (String cat : categories.keySet()) {
            List<FileInfo> list = categories.get(cat);
            int seq = 1;
            Set<String> used = new HashSet<>();
            for (FileInfo fi : list) {
                String extPart = fi.extension.isEmpty() ? "" : "." + fi.extension;
                String base = cat + "_" + date + "_" + seq;
                String candidate = base + extPart;
                while (used.contains(candidate)) {
                    seq++;
                    base = cat + "_" + date + "_" + seq;
                    candidate = base + extPart;
                }
                used.add(candidate);
                suggestions.put(fi.original, candidate);
                seq++;
            }
        }
        return suggestions;
    }
}
