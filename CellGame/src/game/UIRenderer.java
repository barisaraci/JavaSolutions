package game;

import java.awt.Desktop;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class UIRenderer extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	private JPanel mainMenuPanel, generatePanel, endPanel;
	private JLabel title, enemyNumber, difficultyNumber, variationNumber, enemyExp, difficultyExp, variationExp, winnerExp, winner;
	private JTextField enemyTF, difficultyTF, variationTF;
	private JButton playButton, generateButton, backButton, playSameButton, playAgainButton;
	
	private boolean isVisible;
    
    public UIRenderer() {
    	setLayout(null);
    	
    	title = new JLabel("PIXEL WARS");
    	title.setLocation(400, 60);
    	title.setSize(480, 60);
    	title.setFont(new Font("Serif", Font.PLAIN, 80));
    	
    	// main menu panel
    	
    	mainMenuPanel = new JPanel();
    	mainMenuPanel.setSize(Main.width, Main.height);
    	mainMenuPanel.setLocation(0, 0);
    	mainMenuPanel.setLayout(null);
    	
    	playButton = new JButton();
    	playButton.setText("play");
    	playButton.setSize(400, 100);
    	playButton.setLocation(440, 350);
    	playButton.setFont(new Font("Serif", Font.PLAIN, 40));
    	playButton.repaint();
    	playButton.addActionListener(new ButtonHandler(0));
    	
    	// generate panel
    	
    	generatePanel = new JPanel();
    	generatePanel.setSize(Main.width, Main.height);
    	generatePanel.setLocation(0, 0);
    	generatePanel.setLayout(null);
    	
    	enemyNumber = new JLabel("enemy number: ");
    	enemyNumber.setSize(300, 100);
    	enemyNumber.setLocation(440, 265);
    	enemyNumber.setFont(new Font("Serif", Font.PLAIN, 30));
    	
    	enemyTF = new JTextField();
    	enemyTF.setSize(40, 40);
    	enemyTF.setLocation(710, 295);
    	
    	enemyExp = new JLabel("1-4");
    	enemyExp.setSize(300, 100);
    	enemyExp.setLocation(770, 260);
    	enemyExp.setFont(new Font("Serif", Font.PLAIN, 30));
    	
    	difficultyNumber = new JLabel("difficulty number: ");
    	difficultyNumber.setSize(300, 100);
    	difficultyNumber.setLocation(440, 315);
    	difficultyNumber.setFont(new Font("Serif", Font.PLAIN, 30));
    	
    	difficultyTF = new JTextField();
    	difficultyTF.setSize(40, 40);
    	difficultyTF.setLocation(710, 345);
    	
    	difficultyExp = new JLabel("1-10");
    	difficultyExp.setSize(300, 100);
    	difficultyExp.setLocation(770, 315);
    	difficultyExp.setFont(new Font("Serif", Font.PLAIN, 30));
    	
    	variationNumber = new JLabel("variation number: ");
    	variationNumber.setSize(300, 100);
    	variationNumber.setLocation(440, 365);
    	variationNumber.setFont(new Font("Serif", Font.PLAIN, 30));
    	
    	variationTF = new JTextField();
    	variationTF.setSize(40, 40);
    	variationTF.setLocation(710, 395);
    	
    	variationExp = new JLabel("1-50");
    	variationExp.setSize(300, 100);
    	variationExp.setLocation(770, 365);
    	variationExp.setFont(new Font("Serif", Font.PLAIN, 30));
    	
    	generateButton = new JButton();
    	generateButton.setText("generate game");
    	generateButton.setSize(200, 50);
    	generateButton.setLocation(660, 600);
    	generateButton.repaint();
    	generateButton.addActionListener(new ButtonHandler(3));
    	
    	backButton = new JButton();
    	backButton.setText("back");
    	backButton.setSize(200, 50);
    	backButton.setLocation(400, 600);
    	backButton.repaint();
    	backButton.addActionListener(new ButtonHandler(1));
    	
    	// end game panel
    	
    	endPanel = new JPanel();
    	endPanel.setSize(Main.width, Main.height);
    	endPanel.setLocation(0, 0);
    	endPanel.setLayout(null);
    	
    	winnerExp = new JLabel("game over, winner is");
    	winnerExp.setSize(400, 100);
    	winnerExp.setLocation(470, 200);
    	winnerExp.setFont(new Font("Serif", Font.PLAIN, 30));
    	
    	winner = new JLabel("yellow");
    	winner.setSize(300, 100);
    	winner.setLocation(725, 200);
    	winner.setFont(new Font("Serif", Font.PLAIN, 30));
    	
    	playSameButton = new JButton();
    	playSameButton.setText("play the same settings");
    	playSameButton.setSize(200, 50);
    	playSameButton.setLocation(535, 380);
    	playSameButton.repaint();
    	playSameButton.addActionListener(new ButtonHandler(4));
    	
    	playAgainButton = new JButton();
    	playAgainButton.setText("play again");
    	playAgainButton.setSize(200, 50);
    	playAgainButton.setLocation(535, 480);
    	playAgainButton.repaint();
    	playAgainButton.addActionListener(new ButtonHandler(5));
    	
    	mainMenuPanel.add(playButton);
    	mainMenuPanel.setVisible(true);
    	
    	generatePanel.add(enemyNumber);
    	generatePanel.add(enemyTF);
    	generatePanel.add(enemyExp);
    	generatePanel.add(difficultyNumber);
    	generatePanel.add(difficultyTF);
    	generatePanel.add(difficultyExp);
    	generatePanel.add(variationNumber);
    	generatePanel.add(variationTF);
    	generatePanel.add(variationExp);
    	generatePanel.add(generateButton);
    	generatePanel.add(backButton);
    	generatePanel.setVisible(false);
    	
    	endPanel.add(winnerExp);
    	endPanel.add(winner);
    	endPanel.add(playSameButton);
    	endPanel.add(playAgainButton);
    	endPanel.setVisible(false);
    	
    	add(title);
    	add(mainMenuPanel);
    	add(generatePanel);
    	add(endPanel);
    	setVisible(true);
    }
    
    class ButtonHandler implements ActionListener {
    	private int type;
    	
        public ButtonHandler(int type) {
        	this.type = type;
        }

        public void actionPerformed(ActionEvent event) {
        	if(type == 0) { // single-player button
        		mainMenuPanel.setVisible(false);
        		generatePanel.setVisible(true);
        	} else if(type == 1) { // back button
        		generatePanel.setVisible(false);
        		mainMenuPanel.setVisible(true);
        	} else if(type == 3) { // generate level button
        		if(enemyTF.getText().matches("\\d+")) {
        			if(Integer.parseInt(enemyTF.getText()) >= 1 && Integer.parseInt(enemyTF.getText()) <= 4) {
        				if(difficultyTF.getText().matches("\\d+")) {
                			if(Integer.parseInt(difficultyTF.getText()) >= 1 && Integer.parseInt(difficultyTF.getText()) <= 10) {
                				if(variationTF.getText().matches("\\d+")) {
                        			if(Integer.parseInt(variationTF.getText()) >= 1 && Integer.parseInt(variationTF.getText()) <= 150000) {
                        				Main.setGame(Integer.parseInt(enemyTF.getText()), Integer.parseInt(difficultyTF.getText()),
                        						Integer.parseInt(variationTF.getText()));
                        				Main.resetGame();
                                		Main.isGameRunning = true;
                                		setVisible(false);
                        			} else {
                        				JOptionPane.showMessageDialog(null, "Variation number should be between 1-50.");
                        			}
                        		} else {
                        			JOptionPane.showMessageDialog(null, "Variation number should be a number.");
                        		}
                			} else {
                				JOptionPane.showMessageDialog(null, "Difficulty number should be between 1-10.");
                			}
                		} else {
                			JOptionPane.showMessageDialog(null, "Difficulty number should be a number.");
                		}
        			} else {
        				JOptionPane.showMessageDialog(null, "Enemy number should be between 1-4.");
        			}
        		} else {
        			JOptionPane.showMessageDialog(null, "Enemy number should be a number.");
        		}
        	} else if(type == 4) { // play same button
        		Main.resetGame();
        		Main.isGameRunning = true;
        		setVisible(false);
        	} else if(type == 5) { // play again button
        		generatePanel.setVisible(true);
        		endPanel.setVisible(false);
        	}
        }
    }
    
    public void endScreen(String winner1) {
    	winner.setText(winner1);
    	setVisible(true);
    	endPanel.setVisible(true);
    	generatePanel.setVisible(false);
    }

	public boolean getVisible() {
		return isVisible;
	}
	
	public void openWebpage(URI uri) {
	    Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
	    if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
	        try {
	            desktop.browse(uri);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	}

	public void openWebpage(URL url) {
	    try {
	        openWebpage(url.toURI());
	    } catch (URISyntaxException e) {
	        e.printStackTrace();
	    }
	}

}
