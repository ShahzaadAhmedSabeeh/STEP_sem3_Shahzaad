import java.util.Scanner;

public class StringLengthFinder {

    public static int getStringLength(String str) {
        int count = 0;
        try {
            while (true) {
                str.charAt(count); 
                count++;
            }
        } catch (StringIndexOutOfBoundsException e) {
        }
        return count;
    } //comment

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        
        System.out.print("Enter a string: ");
        String input = sc.next(); 
        
        int manualLength = getStringLength(input);
        
        int builtinLength = input.length();
        
        System.out.println("Manual Length: " + manualLength);
        System.out.println("Built-in Length: " + builtinLength);
        
        sc.close();
    }
}
