package game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.Timer;

public class Main extends JFrame {

	private static final long serialVersionUID = 1L;

	private static GameRenderer game;
	private static UIRenderer ui;

	private final int fps = 60;

	public static final int width = 1280;
	public static final int height = 720;

	private static int enemyNumber, difficulty, variation;

	public static boolean isGameRunning;

	public static void main(String[] args) {
		Main main = new Main();
		main.run();
	}

	private void run() {
		setup();
		
		// thread method
		
		/*while(true) {
			long time = System.currentTimeMillis();
			
			if(isGameRunning) {
				update();
			}
			
			time = (1000 / fps) - (System.currentTimeMillis() - time);
			
			if(time > 0) {
				try {
					Thread.sleep(time);
				} catch(Exception e) { }
			}
		}*/
		
		// timer method
		
		new Timer((int) 1000 / fps, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(isGameRunning) {
					update();
				}
			}
		}).start();

	}

	private void setup() {
		setTitle("CS102 Project");
		setSize(1280, 720);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(null);

		ui = new UIRenderer();
		ui.setSize(width, height);
		ui.setLocation(0, 0);
		add(ui);

		game = new GameRenderer();
		game.setSize(width, height);
		game.setLocation(0, 0);
		add(game);

		setVisible(true);

		isGameRunning = false;
	}

	private void update() {
		game.render();
	}

	public static void setGame(int enemyNumber1, int difficulty1, int variation1) {
		enemyNumber = enemyNumber1;
		difficulty = difficulty1;
		variation = variation1;
	}

	public static void resetGame() {
		game.setup();
		game.worldSetup(enemyNumber, difficulty, variation);
		game.setVisible(true);
	}

	public static void gameOver(String winner) {
		isGameRunning = false;
		ui.endScreen(winner);
		game.setVisible(false);
	}

}
