package monopoly;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import monopoly.domains.Controller;
import monopoly.domains.ControllerObserver;
import monopoly.ui.BotUI;
import monopoly.ui.GameView;
import monopoly.ui.MenuView;

public class MonopolyGame {
	
	public static final String TITLE = "Ultimate Monopoly";
	public static final int MAX_PLAYERS = 8; // min is 2 by default
	
	public static final int GAME_WINDOW_SIZE_X = 1800;
	public static final int GAME_WINDOW_SIZE_Y = 900;
	public static final int GAME_SQUARE_SIZE = 96;
	public static final int GAME_RIGHT_PANEL_SIZE = 350;
	public static final int INITIAL_MONEY = 3200;
	
	public static final int TOTAL_NUMBER_OF_PROPERTIES[] = {30, 22, 12};
	public static final int TOTAL_NUMBER_OF_SQUARES[] = {56, 40, 24};
	
	public static final String EMOJI_DEFAULT = "(PLAYING)";
	public static final String EMOJI_RENT = "(PAID RENT)";
	public static final String EMOJI_GAIN = "(GAINED MONEY)";
	public static final String EMOJI_JAIL = "(WENT JAIL)";
	public static final String EMOJI_BANKRUPT = "(WENT BANKRUPT)";
	public static final String EMOJI_AFK = "(AFK)";
	public static final String EMOJI_WIN = "(WON)";
	
	private Controller controller;
	private JFrame frame;
	private GameView gameView;
	private MenuView menuView;
	private static BotUI botUI;
	
	public static enum Type {
		Go, Property, Transport, Special, Card, Tax
	}

	public static void main(String[] args) {
		MonopolyGame game = new MonopolyGame();
		game.run();
	}
	
	private void run() {
		menuView = new MenuView(this);
		frame = new JFrame();
		frame.setTitle(MonopolyGame.TITLE);
		frame.setSize(512, 80);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(menuView);
		frame.setVisible(true);
	}
	
	public void startNewGame(int numPlayers) {
		controller = new Controller(numPlayers);
		botUI = new BotUI();
		new ControllerObserver(controller, botUI).run();
		gameView = new GameView(controller, botUI);
    	frame.getContentPane().removeAll();
    	frame.add(gameView);
    	frame.getContentPane().revalidate();
    	frame.setSize(MonopolyGame.GAME_WINDOW_SIZE_X, MonopolyGame.GAME_WINDOW_SIZE_Y);
	}
	
	public void loadGame() {
		try {
			FileInputStream fileInputStream = new FileInputStream(new File("save.mnply"));
			InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, Charset.forName("UTF-8"));
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

			int numPlayers = Integer.valueOf(bufferedReader.readLine().split(" ")[0]);
			
			controller = new Controller(numPlayers);
			botUI = new BotUI();
			new ControllerObserver(controller, botUI);
			gameView = new GameView(controller, botUI);
			controller.loadGame();
	    	frame.getContentPane().removeAll();
	    	frame.add(gameView);
	    	frame.getContentPane().revalidate();
	    	frame.setSize(MonopolyGame.GAME_WINDOW_SIZE_X, MonopolyGame.GAME_WINDOW_SIZE_Y);
	    	
			bufferedReader.close();
		} catch(FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "The save file could not be found.", "Error", JOptionPane.ERROR_MESSAGE);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static BotUI getBotUI() {
		return botUI;
	}
	
}
