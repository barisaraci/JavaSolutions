package monopoly.domains;

public class Color {
	
	private int r, g, b;
	
	public Color(int r, int g, int b) {
		this.r = r;
		this.g = g;
		this.b = b;
	}
	
	/**
	 * @EFFECTS: Returns the R value of color
	 */
	public int getR() {
		return r;
	}
	
	/**
	 * @EFFECTS: Returns the G value of color
	 */
	public int getG() {
		return g;
	}
	
	/**
	 * @EFFECTS: Returns the B value of color
	 */
	public int getB() {
		return b;
	}
	
	@Override
	public String toString() {
		return "Color [r=" + r + ", g=" + g + ", b=" + b + "]";
	}

	public boolean repOk() {
		if ((r <= 255 || r >= 0) && (g <= 255 || g >= 0) && (b <= 255 || b >= 0)) {
			return true;
		}
		return false;
	}

}
