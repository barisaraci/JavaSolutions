package monopoly.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import monopoly.MonopolyGame;
import monopoly.domains.Controller;
import monopoly.domains.entities.Player;
import monopoly.domains.squares.GameSquare;
import monopoly.domains.squares.PropertySquare;

public class GameView extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	private Controller controller;
	private ArrayList<JButton> buttons0 = new ArrayList<>(); // properties on floor0
	private ArrayList<JButton> buttons1 = new ArrayList<>(); // properties on floor1
	private ArrayList<JButton> buttons2 = new ArrayList<>(); // properties on floor2
	private ArrayList<JLabel> moneyLabels = new ArrayList<>();
	private Box boxDice, boxPropertyOptions, boxAction, boxMoneys;
	private JLabel labelTurn, labelAction, labelPlayerAssets, labelSelectedProperty;
	private JTextPane logPane;
	private JButton buttonSaveGame;
	private Style styleGreen;
	private BotUI botUI;
	
	public GameView(Controller controller, BotUI botUI) {
		this.controller = controller;
		this.botUI = botUI;
		setLayout(new GridBagLayout());
		createBoard();
	}
	
	private void createBoard() {
		GridBagConstraints gbc = new GridBagConstraints();
		
		// squares floor 0
		for (int pos = 0; pos < 56; pos++) {
			JButton squareButton = new JButton();
			initSquareButton(0, pos, squareButton);
			initSquarePos(0, pos, gbc);
			buttons0.add(squareButton);
			add(squareButton, gbc);
		}
		
		// squares floor 1
		for (int pos = 0; pos < 40; pos++) {
			JButton squareButton = new JButton();
			initSquareButton(1, pos, squareButton);
			initSquarePos(1, pos, gbc);
			buttons1.add(squareButton);
			add(squareButton, gbc);
		}
		
		// squares floor 2
		for (int pos = 0; pos < 24; pos++) {
			JButton squareButton = new JButton();
			initSquareButton(2, pos, squareButton);
			initSquarePos(2, pos, gbc);
			buttons2.add(squareButton);
			add(squareButton, gbc);
		}
		
		// player assets panel
		
		boxMoneys = Box.createVerticalBox();
		boxMoneys.setMinimumSize(new Dimension(MonopolyGame.GAME_RIGHT_PANEL_SIZE, 1));
		boxMoneys.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1, true));
		
		for (int i = 0; i < controller.getNumPlayers(); i++) {
			EmojiPanel emojiPanel = new EmojiPanel();
			JLabel labelMoney = new JLabel("Player" + i + ": $3200");
			labelMoney.setAlignmentX(Component.CENTER_ALIGNMENT);
			labelMoney.setVerticalAlignment(SwingConstants.CENTER);
			emojiPanel.add(labelMoney);
			JLabel botLabel = new JLabel("<html><div style='text-align: center; font-size: 16; color: #A7A735'>" + MonopolyGame.EMOJI_DEFAULT + "</div></html>");
			emojiPanel.setLabel(botLabel);
			botUI.attachPanel(emojiPanel);
			emojiPanel.add(botLabel);
			moneyLabels.add(labelMoney);
			boxMoneys.add(emojiPanel);
		}
		
		gbc.gridx = 15;
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.gridwidth = 1;
		gbc.gridheight = 1 + controller.getNumPlayers() / 2;
		add(boxMoneys, gbc);
		
		// roll the dice panel
		
		labelTurn = new JLabel("Its player" + controller.getTurn() + "'s turn.");
		labelTurn.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		JButton buttonDice = new JButton("Roll the dice");
		buttonDice.setAlignmentX(Component.CENTER_ALIGNMENT);
		buttonDice.addActionListener(e -> controller.onButtonDiceClick());
		
		boxDice = Box.createVerticalBox();
		boxDice.setMinimumSize(new Dimension(MonopolyGame.GAME_RIGHT_PANEL_SIZE, 1));
		boxDice.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1, true));
		boxDice.add(Box.createVerticalStrut(4));
		boxDice.add(labelTurn);
		boxDice.add(buttonDice);
		
		gbc.gridx = 15;
		gbc.gridy = 1 + controller.getNumPlayers() / 2;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		add(boxDice, gbc);
		
		// property options panel
		
		JLabel labelPropertyInfo = new JLabel("Click on a property to select it.");
		labelPropertyInfo.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		labelSelectedProperty = new JLabel("<html><font color='red'>'none'</font></html>");
		labelSelectedProperty.setHorizontalAlignment(SwingConstants.CENTER);
		labelSelectedProperty.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		JButton buttonUpgrade = new JButton("Upgrade");
		buttonUpgrade.addActionListener(e -> controller.onButtonUpgradeClick());
		
		JButton buttonMortgage = new JButton("Sell");
		buttonMortgage.addActionListener(e -> controller.onButtonMortgageClick());
		
		//JButton buttonCancel = new JButton("Cancel");
		//buttonCancel.addActionListener(e -> controller.onButtonNegativeClick());
		
		JPanel panelPropertyButtons = new JPanel();
		panelPropertyButtons.add(buttonUpgrade);
		panelPropertyButtons.add(buttonMortgage);
		//panelPropertyButtons.add(buttonCancel);
		
		boxPropertyOptions = Box.createVerticalBox();
		boxPropertyOptions.setMinimumSize(new Dimension(MonopolyGame.GAME_RIGHT_PANEL_SIZE, 1));
		boxPropertyOptions.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1, true));
		boxPropertyOptions.add(Box.createVerticalStrut(20));
		boxPropertyOptions.add(labelPropertyInfo);
		boxPropertyOptions.add(labelSelectedProperty);
		boxPropertyOptions.add(panelPropertyButtons);
		
		gbc.gridx = 15;
		gbc.gridy = 3 + controller.getNumPlayers() / 2;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.gridwidth = 1;
		gbc.gridheight = 2;
		add(boxPropertyOptions, gbc);
		//boxPropertyOptions.setVisible(false);
		
		// action panel
		
		labelAction = new JLabel();
		labelAction.setAlignmentX(Component.CENTER_ALIGNMENT);
		labelAction.setHorizontalAlignment(SwingConstants.CENTER);
		
		JButton buttonPositive = new JButton("Yes");
		buttonPositive.addActionListener(e -> controller.onButtonPositiveClick());
		
		JButton buttonNegative = new JButton("No");
		buttonNegative.addActionListener(e -> controller.onButtonNegativeClick());
		
		JPanel panelActionButtons = new JPanel();
		panelActionButtons.add(buttonPositive);
		panelActionButtons.add(buttonNegative);
		
		boxAction = Box.createVerticalBox();
		boxAction.setMinimumSize(new Dimension(MonopolyGame.GAME_RIGHT_PANEL_SIZE, 1));
		boxAction.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1, true));
		boxAction.add(Box.createVerticalStrut(20));
		boxAction.add(labelAction);
		boxAction.add(panelActionButtons);
	    
	    gbc.gridx = 15;
		gbc.gridy = 1 + controller.getNumPlayers() / 2;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.gridwidth = 1;
		gbc.gridheight = 2;
		add(boxAction, gbc);
		boxAction.setVisible(false);
		
		// save button
		
		buttonSaveGame = new JButton();
		buttonSaveGame.setText("Save Game");
		buttonSaveGame.addActionListener(e -> controller.saveGame());
		
		gbc.gridx = 15;
		gbc.gridy = 10;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		add(buttonSaveGame, gbc);
		
		// log pane
		
		logPane = new JTextPane();
		logPane.setEditable(false);
		logPane.setBackground(Color.BLACK);
		styleGreen = logPane.addStyle("green", null);
        StyleConstants.setForeground(styleGreen, Color.GREEN);
		JScrollPane logScrollPane = new JScrollPane(logPane);
		logScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		gbc.gridx = 15;
		gbc.gridy = 11;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.gridwidth = 1;
		gbc.gridheight = 4;
		add(logScrollPane, gbc);
		
		controller.setView(this); // view ready
	}
	
	public void updatePlayerTurn() {
		labelTurn.setText("Its player" + controller.getTurn() + "'s turn.");
		
		boxAction.setVisible(false);
		boxPropertyOptions.setVisible(true);
		boxDice.setVisible(true);
	}
	
	public void updateAuctionInfo() {
		labelAction.setText("<html>Its player" + controller.getBidTurn() + "'s turn to bid. Current price is " + controller.getPropertyPrice() + ". <br>Do you want to raise the price of " + controller.getAuctionProperty().getName() + " $" + controller.getBidPrice() +"?</html>");
	}
	
	public void updateSaleInfo() {
		labelAction.setText("<html>Its player" + controller.getSaleTurn() + "'s turn to decide. <br> Do you want to buy " + controller.getSaleProperty().getName() + " for $" + controller.getSalePrice() +"?</html>");
	}
	
	public void updatePlayerAssets(int playerId) {
		if (controller.getBoard().getPlayer(playerId).isBankrupt()) {
			moneyLabels.get(playerId).setText("Player" + playerId + " went bankrupt");
		} else {
			moneyLabels.get(playerId).setText("Player" + playerId + ": $" + controller.getBoard().getPlayer(playerId).getMoney());
		}
	}
	
	public void activateBuyMenu(String name, int price) {
		labelAction.setText("Do you want to buy " + name + "for $" + price + " ?");
		boxDice.setVisible(false);
		//boxPropertyOptions.setVisible(false);
		boxAction.setVisible(true);
	}
	
	/*public void activateUpgradeMenu() {
		boxDice.setVisible(false);
		boxAction.setVisible(false);
		boxPropertyOptions.setVisible(true);
		labelSelectedProperty.setText("<html><font color='red'>'none'</font></html>");
	}*/

	public void activateAuction() {
		boxDice.setVisible(false);
		boxPropertyOptions.setVisible(false);
		boxAction.setVisible(true);
	}
	
	public void activateSale() {
		boxDice.setVisible(false);
		boxPropertyOptions.setVisible(false);
		boxAction.setVisible(true);
	}
	
	private void initSquareButton(int floor, int pos, JButton button) {
		button.setOpaque(true);
		button.setMinimumSize(new Dimension(MonopolyGame.GAME_SQUARE_SIZE, MonopolyGame.GAME_SQUARE_SIZE));
		button.setMaximumSize(new Dimension(MonopolyGame.GAME_SQUARE_SIZE, MonopolyGame.GAME_SQUARE_SIZE));
		button.setPreferredSize(new Dimension(MonopolyGame.GAME_SQUARE_SIZE, MonopolyGame.GAME_SQUARE_SIZE));
		button.setHorizontalAlignment(SwingConstants.CENTER);
		button.setVerticalAlignment(SwingConstants.CENTER);
		Border border = BorderFactory.createLineBorder(Color.BLACK, 1);
		button.setBorder(border);
		GameSquare square = controller.getBoard().getSquare(floor, pos);
		button.addActionListener(e -> selectSquare(square));
		if (square != null) {
			Color color = new Color(square.getColor().getR(), square.getColor().getG(), square.getColor().getB());
			button.setBackground(color);
			button.setText(square.getLabelText());
			if (square instanceof PropertySquare) {
				((PropertySquare) square).setHex(Integer.toHexString(color.getRGB()).substring(2));
			}
		} else {
			button.setText(Integer.toString(pos));
		}
	}
	
	private void selectSquare(GameSquare square) {
		labelSelectedProperty.setText("<html><font color='red'>'" + square.getName() + "'</font></html>");
		controller.selectSquare(square);
	}
	
	private void initSquarePos(int floor, int pos, GridBagConstraints gbc) {
		Point point = posToCoords(floor, pos);
		gbc.gridx = point.x;
		gbc.gridy = point.y;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.weightx = 1;
		gbc.weighty = 1;
	}
	
	public void updateTextOnSquare(int floor, int pos) {
		if (floor == 0) 
			buttons0.get(pos).setText(controller.getBoard().getSquare(floor, pos).getLabelText());
		else if (floor == 1) 
			buttons1.get(pos).setText(controller.getBoard().getSquare(floor, pos).getLabelText());
		else if (floor == 2) 
			buttons2.get(pos).setText(controller.getBoard().getSquare(floor, pos).getLabelText());
	}
	
	public void updatePlayerAssets(Player player) {
		labelPlayerAssets.setText("Money: " + player.getMoney());
	}
	
	public void print(String text) {
		StyledDocument doc = logPane.getStyledDocument();
		try {
			doc.insertString(doc.getLength(), text + "\n", styleGreen);
		} catch (BadLocationException e) {
			e.printStackTrace();
		} 
	}
	
	public void gameOver(int winnerId) {
		boxMoneys.setVisible(true);
		boxDice.setVisible(false);
		boxPropertyOptions.setVisible(false);
		boxAction.setVisible(false);
		buttonSaveGame.setVisible(false);
		print("player" + winnerId + " has won the game!");
		JOptionPane.showMessageDialog(null, "player" + winnerId + " has won the game!", "Winner", JOptionPane.INFORMATION_MESSAGE);
	}
	
	public void showError(String text) {
		JOptionPane.showMessageDialog(null, text, "Error", JOptionPane.ERROR_MESSAGE);
	}
	
	public void showSelectMessage(int type) {
		if (type == 1) {
			JOptionPane.showMessageDialog(null, "Select a property to put it up for auction.", "Select", JOptionPane.INFORMATION_MESSAGE);
		} else if (type == 2) {
			JOptionPane.showMessageDialog(null, "Select a property to remove houses.", "Select", JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
	private Point posToCoords(int floor, int pos) {
		int x = 0, y = 0;
		
		if (floor == 0) {
			if (pos >= 0 && pos <= 14) {
				x = 0;
				y = 14 - pos;
			} else if (pos >= 15 && pos <= 28) {
				x = pos - 14;
				y = 0;
			} else if (pos >= 29 && pos <= 42) {
				x = 14;
				y = 14 - (42 - pos);
			} else if (pos >= 43 && pos <= 55) {
				x = 56 - pos;
				y = 14;
			}
		} else if (floor == 1) {
			if (pos >= 0 && pos <= 10) {
				x = (12 - pos);
				y = 12;
			} else if (pos >= 11 && pos <= 20) {
				x = 2;
				y = (22 - pos);
			} else if (pos >= 21 && pos <= 30) {
				x = 12 - (30 - pos);
				y = 2;
			} else if (pos >= 31 && pos <= 39) {
				x = 12;
				y = 11 - (39 - pos);
			}
		} else if (floor == 2) {
			if (pos >= 0 && pos <= 6) {
				x = 10;
				y = 4 + pos;
			} else if (pos >= 7 && pos <= 12) {
				x = 4 + (12 - pos);
				y = 10;
			} else if (pos >= 13 && pos <= 18) {
				x = 4;
				y = 4 + (18 - pos);
			} else if (pos >= 19 && pos <= 23) {
				x = 9 - (23 - pos);
				y = 4;
			}
		}
		
		return new Point(x, y);
	}

}
