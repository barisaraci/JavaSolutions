
public class Tent extends Entity {
	
	public Tent(int x, int y, boolean isVisible, boolean isAnswer) {
		this.x = x;
		this.y = y;
		type = 'T';
		this.isVisible = isVisible;
		this.isAnswer = isAnswer;
	}
	
	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}

}
