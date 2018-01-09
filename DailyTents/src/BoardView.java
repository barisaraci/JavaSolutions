
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

@SuppressWarnings("serial")
public class BoardView extends JPanel {
	
    private JButton[][] buttons;
    
    private BoardController controller;
    private int dimension;
    private boolean isDevModeActive;
    
    private final Color BUTTON_BACKGROUND = new Color(178, 255, 127);
    
    private final String DIRECTORY_IMAGE_TENT = "images//tent.png";
    private final String DIRECTORY_IMAGE_TENT_ANSWER = "images//tent_answer.png";
    private final String DIRECTORY_IMAGE_TREE = "images//tree.png";
    private final String DIRECTORY_IMAGE_RESTART = "images//restart.png";
    
    private final char TENT = 'T';
	private final char TREE = 'E';
    

    public BoardView(int dimension, final BoardController controller, boolean isDevModeActive) {
        this.dimension = dimension;
        this.controller = controller;
        this.isDevModeActive = isDevModeActive;
        
        setBackground(Color.WHITE);
        setLayout(new GridLayout(dimension + 1, dimension + 1));
        addRestartButton();
		addBoard();
    }
    
    private void addRestartButton() {
    	JButton restartButton = new JButton();
    	restartButton.setBorder(BorderFactory.createEmptyBorder());
    	restartButton.setBackground(Color.WHITE);
    	restartButton.setIcon(new ImageIcon(DIRECTORY_IMAGE_RESTART));
        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.restart();
            }
        });
        add(restartButton);
    }
    
    private void addBoard() {
    	buttons = new JButton[dimension][dimension];
        for (int i = 0; i < dimension; i++) {
			addNumberLabel(false, i);
		}
        for (int col = 0; col < dimension; col++) {
        	addNumberLabel(true, col);
            for (int row = 0; row < dimension; row++) {
                addButton(row, col);
            }
        }
    }
    
    private void addNumberLabel(boolean isRow, int index) {
    	if(!isRow) {
	    	JLabel labelColNumber = new JLabel(Integer.toString(controller.getModel().getRowNumbers()[index]), SwingConstants.CENTER);
			labelColNumber.setForeground(Color.BLUE);
			add(labelColNumber);
    	} else {
			JLabel labelRowNumber = new JLabel(Integer.toString(controller.getModel().getColNumbers()[index]), SwingConstants.CENTER);
	    	labelRowNumber.setForeground(Color.BLUE);
			add(labelRowNumber);
    	}
    }
    
    private void addButton(int row, int col) {
    	JButton button = new JButton();
        button.setBackground(BUTTON_BACKGROUND);
        if (controller.getModel().getEntityMatrix()[col][row] != null && (isDevModeActive || controller.getModel().getEntityMatrix()[col][row].isVisible())) {
        	if(controller.getModel().getEntityMatrix()[col][row].getType() == TREE) {
        		button.setIcon(new ImageIcon(DIRECTORY_IMAGE_TREE));
        	} else if(controller.getModel().getEntityMatrix()[col][row].getType() == TENT && controller.getModel().getEntityMatrix()[col][row].isAnswer()) {
        		button.setIcon(new ImageIcon(DIRECTORY_IMAGE_TENT_ANSWER));
        	}
        }
        add(button);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controller.buttonClicked(row, col);                  
            }
        });
        buttons[col][row] = button;
    }

    public void updateButtonAsTent(int row, int col) {
        buttons[col][row].setIcon(new ImageIcon(DIRECTORY_IMAGE_TENT));
    }
}
