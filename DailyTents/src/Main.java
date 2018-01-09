
import javax.swing.JFrame;

public class Main {

	public static void main(String[] args) {
		Main game = new Main();
		game.run();
	}
	
	private void run() {
		JFrame frame = new JFrame("Daily Tents");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 100);
        
        Board board = new Board();
        BoardController controller = new BoardController(board, frame);
        
        frame.add(controller.getMenuView());
        frame.setVisible(true);
	}

}
