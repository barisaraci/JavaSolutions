package hw_matrix_conversion;

import java.util.ArrayList;

public class CSSMatrix {

	private COOMatrix cooMatrix;

	private ArrayList<Integer> rowIndices = new ArrayList<Integer>();
	private ArrayList<Integer> colPointers = new ArrayList<Integer>();
	private ArrayList<Double> values = new ArrayList<Double>();
	
	private int numRows, numCols, numValues, colPtr = 1;

	public CSSMatrix(COOMatrix cooMatrix) {
		this.cooMatrix = cooMatrix;
		numRows = cooMatrix.getNumRows();
		numCols = cooMatrix.getNumCols();
		numValues = cooMatrix.getNumElements();
		convert();
	}

	private void convert() {
		// this could be done in O(n), if we could change the compareTo method inside COOElement
		int currentCol = 0, currentRow = 0;
		
		while (currentCol < numCols) {
			colPointers.add(colPtr);
			while (currentRow < numRows) {
				checkElementForIndex(currentRow, currentCol);
				currentRow++;
			}
			currentRow = 0;
			currentCol++;
		}
		
		colPointers.add(cooMatrix.getNumElements() + 1); // last col pointer
	}
	
	private void checkElementForIndex(int row, int col) {
		for (COOElement element : cooMatrix.getElements()) {
			if(element.getRowIndex() == row && element.getColIndex() == col) {
				colPtr++;
				rowIndices.add(element.getRowIndex() + 1); // + 1, since the index starts from 1
				values.add(element.getValue());
			}
		}
	}
	
	public String toString() {
		String head = "";
		
		head += numRows + " " + numCols + " " + numValues + System.lineSeparator();
		
		head = getStringOfMatrix(head);
		
		return head;
	}
	
	private String getStringOfMatrix (String head) {
		head += "rows = [";
		for(int rowIndice : rowIndices) {
			head += rowIndice + ", ";
		}
		head = head.substring(0, head.length() - 2); // remove the last comma, alternatively we can use traditional loop instead of for-each loop to get rid of this line
		head += "]" + System.lineSeparator();
		
		head += "cols = [";
		for(int colPtr : colPointers) {
			head += colPtr + ", ";
		}
		head = head.substring(0, head.length() - 2); // remove the last comma
		head += "]" + System.lineSeparator();
		
		head += "vals = [";
		for(double value : values) {
			head += value + ", ";
		}
		head = head.substring(0, head.length() - 2); // remove the last comma
		head += "]" + System.lineSeparator();
		
		return head;
	}

}
