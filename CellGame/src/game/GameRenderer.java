package game;

import game.InputHandler;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class GameRenderer extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	private GameWorld world;
    private InputHandler input;
    private Graphics g;
    private BufferedImage gameScreen;
    private BufferedImage[] number = new BufferedImage[10];
	
	private boolean isTouch;
	
	public void setup() {
		g = getGraphics();
		input = new InputHandler(this);
        gameScreen = new BufferedImage(Main.width, Main.height, BufferedImage.TYPE_INT_RGB);
        
        for(int i = 0; i < 10; i++) {
        	try {
				number[i] = ImageIO.read(new File("images/n" + i + ".png"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        
        Toolkit toolkit = Toolkit.getDefaultToolkit();
	    Point hotSpot = new Point(0,0);
	    BufferedImage cursorImage = new BufferedImage(1, 1, BufferedImage.TRANSLUCENT); 
	    Cursor invisibleCursor = toolkit.createCustomCursor(cursorImage, hotSpot, "InvisibleCursor");
	    setCursor(invisibleCursor);
	}
	
	public void worldSetup(int enemyNumber, int difficulty, int variation) {
		world = new GameWorld(enemyNumber, difficulty, variation);
	}
	
	public void render() {
		world.update();
		renderGame();
	}
	
	private void renderGame() {
		Graphics gr = gameScreen.getGraphics();
        
        gr.setColor(new Color(132 / 255f, 140 / 255f, 154 / 255f, 1)); 
        gr.fillRect(0, 0, Main.width, Main.height);
        
		for(int i = 0; i < world.getOrgans().size(); i++) {
			if(world.getOrgans().get(i).getSelected() == 1) {
				gr.setColor(new Color(121 / 255f, 238 / 255f, 62 / 255f));
				gr.fillOval(world.getOrgans().get(i).getX() - world.getOrgans().get(i).getR() - world.getOrgans().get(i).getR() / 10, world.getOrgans().get(i).getY() - world.getOrgans().get(i).getR() - world.getOrgans().get(i).getR() / 10, world.getOrgans().get(i).getR() * 2 + world.getOrgans().get(i).getR() / 5, world.getOrgans().get(i).getR() * 2 + world.getOrgans().get(i).getR() / 5);
			} else if(world.getOrgans().get(i).getSelected() == 2) {
				gr.setColor(new Color(238 / 255f, 62 / 255f, 62 / 255f));
				gr.fillOval(world.getOrgans().get(i).getX() - world.getOrgans().get(i).getR() - world.getOrgans().get(i).getR() / 10, world.getOrgans().get(i).getY() - world.getOrgans().get(i).getR() - world.getOrgans().get(i).getR() / 10, world.getOrgans().get(i).getR() * 2 + world.getOrgans().get(i).getR() / 5, world.getOrgans().get(i).getR() * 2 + world.getOrgans().get(i).getR() / 5);
			}
			gr.setColor(world.getOrgans().get(i).getColor());
			gr.fillOval(world.getOrgans().get(i).getX() - world.getOrgans().get(i).getR(), world.getOrgans().get(i).getY() - world.getOrgans().get(i).getR(), world.getOrgans().get(i).getR() * 2, world.getOrgans().get(i).getR() * 2);
		}
		
		for(int i = 0; i < world.getCells().size(); i++) {
			gr.setColor(world.getCells().get(i).getColor());
			gr.fillOval((int) world.getCells().get(i).getX() - (int) world.getCells().get(i).getR(), (int) world.getCells().get(i).getY() - (int) world.getCells().get(i).getR(), (int) world.getCells().get(i).getR() * 2, (int) world.getCells().get(i).getR() * 2);
		}
		
		for(int i = 0; i < world.getOrgans().size(); i++) {
			for(int j = 0; j < String.valueOf(world.getOrgans().get(i).getPower()).length(); j++) {
				gr.drawImage(number[Character.digit(String.valueOf(world.getOrgans().get(i).getPower()).charAt(j), 10)], world.getOrgans().get(i).getX() - (String.valueOf(world.getOrgans().get(i).getPower()).length() * Main.height * world.getOrgans().get(i).getMaxPower() / 500) / 2 + Main.height * world.getOrgans().get(i).getMaxPower() * j / 600, world.getOrgans().get(i).getY() - Main.height * world.getOrgans().get(i).getMaxPower() / 600, Main.height * world.getOrgans().get(i).getMaxPower() / 500, Main.height * world.getOrgans().get(i).getMaxPower() / 300, null);
			}
		}
		
		if(input.isMouseButtonDown(1)) {
			for(int i = 0; i < world.getOrgans().size(); i++) {
				if(world.getOrgans().get(i).getAlly() != 1) { world.getOrgans().get(i).setSelected(0); }
				if(world.distance(input.getX(), input.getY(), world.getOrgans().get(i).getX(), world.getOrgans().get(i).getY()) <= world.getOrgans().get(i).getR()) {
					if(world.getOrgans().get(i).getAlly() == 1) {
						world.getOrgans().get(i).setSelected(1);
						isTouch = true;
					} else if(isTouch) {
						world.getOrgans().get(i).setSelected(2);
					}
				} else {
					if(isTouch) {
						if(world.getOrgans().get(i).getSelected() == 1) {
							gr.setColor(new Color(121 / 255f, 238 / 255f, 62 / 255f, 1));
							gr.drawLine(world.getOrgans().get(i).getX(), world.getOrgans().get(i).getY(), input.getX(), input.getY());
						} else {
							world.getOrgans().get(i).setSelected(0);
						}
					}
				}
			}
		} else if(input.isMouseButtonDown(3)) {
			for(int i = 0; i < world.getOrgans().size(); i++) {
				if(world.getOrgans().get(i).getAlly() == 1) {
					world.getOrgans().get(i).setSelected(1);
				} else {
					world.getOrgans().get(i).setSelected(0);
				}
				for(int j = 0; j < world.getOrgans().size(); j++) {
					if(world.distance(input.getX(), input.getY(), world.getOrgans().get(j).getX(), world.getOrgans().get(j).getY()) <= world.getOrgans().get(j).getR()) {
						world.getOrgans().get(j).setSelected(2);
						if(world.getOrgans().get(j).getAlly() == 1) {
							world.getOrgans().get(j).setSelected(0);
						}
						world.attack(j, 1);
					}
				}
			}
		} else {
			if(isTouch) {
				for(int i = 0; i < world.getOrgans().size(); i++) {
					if(world.distance(input.getX(), input.getY(), world.getOrgans().get(i).getX(), world.getOrgans().get(i).getY()) <= world.getOrgans().get(i).getR()) {
						if(world.getOrgans().get(i).getAlly() == 1) {
							world.getOrgans().get(i).setSelected(0);
						}
						world.attack(i, 1);
					}
				}
			}
			isTouch = false;
			for(int i = 0; i < world.getOrgans().size(); i++) {
				world.getOrgans().get(i).setSelected(0);
			}
		}
		
		gr.setColor(Color.BLACK);
        gr.fillOval(input.getX() - 5, input.getY() - 5, 10, 10);
        
		g.drawImage(gameScreen, 0, 0, this);
	}

}
