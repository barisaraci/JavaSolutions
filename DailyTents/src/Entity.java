
public abstract class Entity {
	
	protected int x, y;
	protected char type;
	
	protected boolean isVisible, isAnswer;
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public char getType() {
		return type;
	}
	
	public boolean isVisible() {
		return isVisible;
	}
	
	public boolean isAnswer() {
		return isAnswer;
	}

	public void setVisible(boolean isVisible) {
		
	}
	
}
