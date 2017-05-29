package hw_matrix_conversion;

public class COOElement implements Comparable<COOElement> {
    int rowIndex;
   	int colIndex;
    double value;

    COOElement(int rowIndex, int colIndex, double value) {
        this.rowIndex = rowIndex;
        this.colIndex = colIndex;
        this.value = value;
    }

    public int getRowIndex() {
		return rowIndex;
	}

	public int getColIndex() {
		return colIndex;
	}

	public double getValue() {
		return value;
	}

    @Override
    // Comparison according to the row-major order
    public int compareTo(COOElement other) {
        if (this.rowIndex < other.rowIndex) {
            return -1;
        } else if (this.rowIndex > other.rowIndex) {
            return 1;
        } else if (this.colIndex < other.colIndex) {
            return -1;          
        } else if (this.colIndex > other.colIndex) {
            return 1;
        } else {
            return 0;
        }
    }
    
    public String toString(){
    	return Integer.toString(rowIndex) + " " + Integer.toString(colIndex) + " " + Double.toString(value);
    }
}
