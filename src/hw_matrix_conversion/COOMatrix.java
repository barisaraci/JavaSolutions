package hw_matrix_conversion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class COOMatrix {
    private int numRows;
    private int numCols;
    private List<COOElement> elements;

    public COOMatrix(int numRows, int numCols) {
        this.numRows = numRows;
        this.numCols = numCols;
        elements = new ArrayList<COOElement>();
    }

    public void addElement(int rowIndex, int colIndex, double value) {
        elements.add(new COOElement(rowIndex, colIndex, value));
    }

    public void normalize() {
        Collections.sort(elements);     
    }

    public int getNumRows() {
        return numRows;
    }
    
    public int getNumCols() {
        return numCols;
    }
    
    public int getNumElements() {
        return elements.size();
    }
    
    public List<COOElement> getElements() {
        return elements;
    }
    
    public String toString(){
    	String head = Integer.toString(numRows) + " " + Integer.toString(numCols) + " " + Integer.toString(elements.size()) +  
    			      System.lineSeparator();
    	
    	String matrixContents = "";
    	for (COOElement cooElement : elements) {
    		matrixContents += cooElement.toString() + System.lineSeparator();
		}
    	
    	return head + matrixContents;
    }
}
