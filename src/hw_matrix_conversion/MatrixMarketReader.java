package hw_matrix_conversion;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/*
 * author: Obi-Wan Kenobi
 * modified by: Harubyy
 */

public class MatrixMarketReader {
    private int numRows, numCols, numElements;
    private COOMatrix matrix;
    private Scanner scanner;

    public MatrixMarketReader(String fileName) throws FileNotFoundException {
        scanner = new Scanner(new File(fileName));
        readHeader();		
        initializeMatrix();
        readElements();
        scanner.close();
    }

    private void readHeader() {
        String line;
        // Eat up comment lines
        do {
            line = scanner.nextLine();
        } while (isCommentLine(line));

        Scanner headerLineScanner = new Scanner(line);
        numRows = headerLineScanner.nextInt();
        numCols = headerLineScanner.nextInt();
        numElements = headerLineScanner.nextInt();
        headerLineScanner.close();
    }

    private static boolean isCommentLine(String line) {
        return line.startsWith("%");
    }

    private void initializeMatrix() {
        matrix = new COOMatrix(numRows, numCols);
    }

    private void readElements() {
        for(int i = 0; i < numElements; i++) {
            readMatrixElement(scanner.nextLine());
        }
    }

    private void readMatrixElement(String line) {
        Scanner lineScanner = new Scanner(line);
        int rowIndex = lineScanner.nextInt();
        int colIndex = lineScanner.nextInt();
        double value = lineScanner.nextDouble();
        // indexing starts from 1 in the MM file. Hence the -1.
        if (value != 0) {
            matrix.addElement(rowIndex-1, colIndex-1, value);
        }
        lineScanner.close();
    }

    public COOMatrix getCOOMatrix() {
        return matrix;
    }
}
