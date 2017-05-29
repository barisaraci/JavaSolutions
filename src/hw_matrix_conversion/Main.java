package hw_matrix_conversion;

import java.io.IOException;
import java.util.Scanner;

public class Main {
	
    public static void main(String[] args) throws IOException {
        String fileName = getFileNameFromUser();
        MatrixMarketReader reader = new MatrixMarketReader(fileName);
        COOMatrix cooMatrix = reader.getCOOMatrix();
        cooMatrix.normalize();
        
        CSSMatrix cssMatrix = new CSSMatrix(cooMatrix);
        System.out.println(cssMatrix);
    }

    public static String getFileNameFromUser() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the Matrix Market file name: ");
        String line = scanner.nextLine();
        scanner.close();
        return line;
    }
    
}
