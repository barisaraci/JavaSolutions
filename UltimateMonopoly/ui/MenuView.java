package monopoly.ui;

import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import monopoly.MonopolyGame;

public class MenuView extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	private MonopolyGame game;
	
	public MenuView(MonopolyGame game) {
		this.game = game;
		init();
	}
	
	private void init() {
		setLayout(new FlowLayout());

		JLabel label = new JLabel("How many players?");
		add(label);
		
		String numPlayers[] = new String[MonopolyGame.MAX_PLAYERS - 1];
		for (int i = 2; i <= MonopolyGame.MAX_PLAYERS; i++) {
			numPlayers[i - 2] = Integer.toString(i);
		}
		
		JComboBox<String> dimensionList = new JComboBox<>(numPlayers);
		add(dimensionList);
		
		JButton buttonStart = new JButton("Start New Game");
		buttonStart.addActionListener(e -> game.startNewGame(Integer.valueOf((String) dimensionList.getSelectedItem())));
		add(buttonStart);
		
		JButton buttonLoadGame = new JButton("Load Game");
		buttonLoadGame.addActionListener(e -> game.loadGame());
		add(buttonLoadGame);
		
		
	}

}
