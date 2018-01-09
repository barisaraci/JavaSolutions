
import javax.swing.JFrame;

public class BoardController {
	
	private Board board;
    private BoardView boardView;
    private MenuView menuView;
    private JFrame frame;
    
    private boolean isDevModeActive;
    
    private final int[] DIMENSIONS = {8, 12, 16, 20};

    public BoardController(Board board, JFrame frame) {
        this.board = board;
        this.frame = frame;
        
        boardView = new BoardView(board.getDimension(), this, isDevModeActive);
        menuView = new MenuView(this);
    }

    public void buttonClicked(int row, int col) {
    	if (board.isTentCreated(col, row))
    		boardView.updateButtonAsTent(row, col);
    }
    
    public void create(int indexOfDimensionArray, boolean isDevModeActive) {
    	this.isDevModeActive = isDevModeActive;
    	board.create(DIMENSIONS[indexOfDimensionArray]);
    	boardView = new BoardView(board.getDimension(), this, isDevModeActive);
    	
    	frame.getContentPane().removeAll();
    	frame.add(boardView);
    	frame.getContentPane().revalidate();
    	frame.setSize(DIMENSIONS[indexOfDimensionArray] * 50, DIMENSIONS[indexOfDimensionArray] * 50);
    }
    
    public void restart() {
    	frame.getContentPane().removeAll();
    	board.restart();
    	boardView = new BoardView(board.getDimension(), this, isDevModeActive);
    	frame.add(boardView);
    	frame.getContentPane().revalidate();
    }

    public BoardView getBoardView() {
        return boardView;
    }
    
    public MenuView getMenuView() {
        return menuView;
    }
    
    public Board getModel() {
        return board;
    }
}
