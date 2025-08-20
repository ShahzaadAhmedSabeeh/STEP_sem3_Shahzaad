import java.util.Scanner;

public class BMICalculator {
    static String[] computeBMI(double weight, double heightCm) {
        double heightM = heightCm / 100.0;
        double bmi = weight / (heightM * heightM);
        String status;
        if (bmi <= 18.4) status = "Underweight";
        else if (bmi <= 24.9) status = "Normal";
        else if (bmi <= 39.9) status = "Overweight";
        else status = "Obese";
        return new String[]{String.valueOf(weight), String.valueOf(heightCm),
                String.format("%.1f", bmi), status};
    }

    static String[][] processAll(double[][] arr) {
        String[][] result = new String[arr.length][4];
        for (int i = 0; i < arr.length; i++) {
            result[i] = computeBMI(arr[i][0], arr[i][1]);
        }
        return result;
    }

    static void display(String[][] arr) {
        System.out.printf("%-12s %-12s %-8s %-12s\n", "Weight(kg)", "Height(cm)", "BMI", "Status");
        for (String[] row : arr) {
            System.out.printf("%-12s %-12s %-8s %-12s\n", row[0], row[1], row[2], row[3]);
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        double[][] data = new double[10][2];
        for (int i = 0; i < 10; i++) {
            data[i][0] = sc.nextDouble();
            data[i][1] = sc.nextDouble();
        }
        String[][] result = processAll(data);
        display(result);
    }
}
