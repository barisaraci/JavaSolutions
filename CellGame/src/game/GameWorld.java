package game;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

import entities.Cell;
import entities.Organ;

public class GameWorld {
	
	private Random random;
	
	private ArrayList<Organ> organs = new ArrayList<Organ>();
	private ArrayList<Cell> cells = new ArrayList<Cell>();
	private ArrayList<Bot> bots = new ArrayList<Bot>();
	private int enemyNumber, difficulty, variation;
	
	private boolean firstAttack;
	
	private int basePos[][] = {
			{Main.height / 6, Main.height - Main.height / 5}, 
			{Main.width - Main.height / 6, Main.height / 6},
			{Main.width - Main.height / 6, Main.height - Main.height / 5},
			{Main.height / 6, Main.height / 6},
			{Main.width / 2, Main.height / 2}
	};
	
	public GameWorld(int enemyNumber, int difficulty, int variation) {
		random = new Random();
		this.enemyNumber = enemyNumber;
		this.difficulty = difficulty;
		this.variation = variation;
		firstAttack = false;
		create();
	}

	private void create() {
		int[][] poses = shuffleArray(basePos);
		for(int i = 0; i < enemyNumber + 1; i++) {
			Organ main;
			if(i == 0) {
				main = new Organ(poses[i][0], poses[i][1]);
			} else {
				main = new Organ("enemymain", poses[i][0], poses[i][1], i + 1, difficulty);
				Bot bot = new Bot(difficulty, i + 1, i); // bu difficulty hepsi için farklý olsun
				bots.add(bot);
			}
			organs.add(main);
		}
		for(int i = 0; i < variation; i++) {
			int typeRand = random.nextInt(8);
			int r;
			String type;
			if(typeRand < 1) {
				type = "large";
				r = Main.height / 300 * 30;
			} else if(typeRand < 3) {
				type = "medium";
				r = Main.height / 300 * 20;
			} else {
				type = "small";
				r = Main.height / 300 * 10;
			}
			int x = r * 3 / 2 + random.nextInt(Main.width - r * 4);
			int y = r * 3 / 2 + random.nextInt(Main.height - r * 4);
			int tryTime2 = 0;
			while(true) {
				tryTime2++;
				boolean isCreatable = true;
				for(int j = 0; j < organs.size(); j++) {
					if(distance(organs.get(j).getX(), organs.get(j).getY(), x, y) < r * 2 + organs.get(j).getR()) {
						isCreatable = false;
					}
				}
				if(isCreatable) {
					Organ organ = new Organ(type, x, y, 0, difficulty);
					organs.add(organ);
					break;
				} else {
					x = r * 3 / 2 + random.nextInt(Main.width - r * 4);
					y = r * 3 / 2 + random.nextInt(Main.height - r * 4);
				}
				if(tryTime2 >= 50000) { break; }
			}
		}
	}
	
	public void update() {
		updateOrgans();
		updateCells();
		if(firstAttack) { updateAI(); }
		checkGameStatus();
	}
	
	private void updateOrgans() {
		for(int i = 0; i < organs.size(); i++) {
			if(organs.get(i).getAlly() != 0) {
				organs.get(i).increasePower();
			}
		}
	}
	
	private void updateCells() {
		for(int i = 0; i < cells.size(); i++) {
			for(int j = 0; j < cells.size(); j++) {
				if(i != j) {
					if(distance(cells.get(i).getX(), cells.get(i).getY(), cells.get(j).getX(), cells.get(j).getY()) <= cells.get(i).getR() * 2) {
						if(cells.get(i).getAlly() == cells.get(j).getAlly()) {
							float dX = cells.get(i).getX() - cells.get(j).getX();
							float dY = cells.get(i).getY() - cells.get(j).getY();
							float degree = (float) Math.atan2(dY, dX);
							
							cells.get(i).setX(cells.get(i).getX() + (float) (1 * Math.cos(degree)));
							cells.get(i).setY(cells.get(i).getY() + (float) (1 * Math.sin(degree)));
							cells.get(j).setX(cells.get(j).getX() - (float) (1 * Math.cos(degree)));
							cells.get(j).setY(cells.get(j).getY() - (float) (1 * Math.sin(degree)));
						} else {
							if(cells.get(i) != null && cells.get(j) != null) { cells.get(i).setCanRemove(true); cells.get(j).setCanRemove(true);}
						}
					}
				}
			}
			if(cells.get(i).getHarm()) {
				if(cells.get(i).getAlly() == 1) {
					if(organs.get(cells.get(i).getAttackID()).getPower() <= 0) {
						organs.get(cells.get(i).getAttackID()).setAlly(1);
						organs.get(cells.get(i).getAttackID()).setColor(new Color(139 / 255f, 175 / 255f, 98 / 255f, 1));
					}
					if(organs.get(cells.get(i).getAttackID()).getAlly() == 1) {
						organs.get(cells.get(i).getAttackID()).setPower(organs.get(cells.get(i).getAttackID()).getPower() + 1);
					} else {
						organs.get(cells.get(i).getAttackID()).setPower(organs.get(cells.get(i).getAttackID()).getPower() - 1);
					}
				} else {
					if(organs.get(cells.get(i).getAttackID()).getPower() <= 0) {
						organs.get(cells.get(i).getAttackID()).setAlly(cells.get(i).getAlly());
						if(cells.get(i).getAlly() == 2) {
							organs.get(cells.get(i).getAttackID()).setColor(new Color(215 / 255f, 117 / 255f, 117 / 255f, 1));
						} else {
							organs.get(cells.get(i).getAttackID()).setColor(cells.get(i).getColor());
						}
					}
					if(organs.get(cells.get(i).getAttackID()).getAlly() == cells.get(i).getAlly()) {
						organs.get(cells.get(i).getAttackID()).setPower(organs.get(cells.get(i).getAttackID()).getPower() + 1);
					} else {
						organs.get(cells.get(i).getAttackID()).setPower(organs.get(cells.get(i).getAttackID()).getPower() - 1);
					}
				}
				cells.remove(i);
			} else {
				cells.get(i).move(organs.get(cells.get(i).getAttackID()).getX(), organs.get(cells.get(i).getAttackID()).getY(), organs.get(cells.get(i).getAttackID()).getR());
			}
		}
		for(int i = 0; i < cells.size(); i++) {
			if(cells.get(i).isCanRemove()) {
				cells.remove(i);
			}
		}
	}
	
	// player
	public void attack(int attackID, int attackerID) {
		if(!firstAttack) { firstAttack = true; }
		for(int i = 0; i < organs.size(); i++) {
			if(organs.get(i).getSelected() == 1 && organs.get(i).getAlly() == 1) {
				for(int j = 0; j < organs.get(i).getPower() / 2; j++) {
					Cell cell = new Cell(organs.get(i).getX(), organs.get(i).getY(), attackID, organs.get(i).getPower(), 1, organs.get(i).getColor());
					cells.add(cell);
				}
				organs.get(i).setPower(organs.get(i).getPower() - organs.get(i).getPower() / 2);
			}
		}
	}
	
	// bot
	public void attack(int attackID, int attackerID, ArrayList<Integer> nearestEnemies, int attackTime) {
		if(difficulty <= 7) {
			for(int i = 0; i < organs.size(); i++) {
				if(organs.get(i).getAlly() == organs.get(attackerID).getAlly()) {
					for(int j = 0; j < organs.get(i).getPower() / 2; j++) {
						Cell cell = new Cell(organs.get(i).getX(), organs.get(i).getY(), attackID, organs.get(i).getPower(), organs.get(attackerID).getAlly(), organs.get(i).getColor());
						cells.add(cell);
					}
					organs.get(i).setPower(organs.get(i).getPower() - organs.get(i).getPower() / 2);
				}
			}
		} else {
			for(int i = 0; i < organs.size(); i++) {
				int cellNumber = 0;
				if(organs.get(i).getAlly() == organs.get(attackerID).getAlly()) {
					for(int k = 0; k < attackTime; k++) {
						for(int j = 0; j < organs.get(i).getPower() * 3 / 4 / attackTime; j++) {
							if(k < nearestEnemies.size()) {
								Cell cell = new Cell(organs.get(i).getX(), organs.get(i).getY(), nearestEnemies.get(nearestEnemies.size() - 1 - k), organs.get(i).getPower(), organs.get(attackerID).getAlly(), organs.get(i).getColor());
								cells.add(cell);
								cellNumber++;
							}
						}
					}
					organs.get(i).setPower(organs.get(i).getPower() - cellNumber);
				}
			}
		}
	}
	
	// difficulty 7 nin üzerindeyse daha zeki olsunlar
	private void updateAI() {
		for(int i = 0; i < bots.size(); i++) {
			bots.get(i).setOrganNumber(0);
			for(int j = 0; j < organs.size(); j++) {
				if(organs.get(j).getAlly() == bots.get(i).getAlly()) {
					bots.get(i).setOrganNumber(bots.get(i).getOrganNumber() + 1);
				}
			}
		}
		
		for(int j = 0; j < bots.size(); j++) {
			bots.get(j).makeMove();
			if(bots.get(j).getMove()) {
				int botMainID = -1;
				for(int i = 0; i < organs.size(); i++) {
					if(organs.get(i).getAlly() == bots.get(j).getAlly() && organs.get(bots.get(j).getBaseID()).getAlly() == bots.get(j).getAlly()) {
						botMainID = i;
					}
				}
				int enemyMaxPower = 0;
				if(botMainID == -1) {
					for(int i = 0; i < organs.size(); i++) {
						if(organs.get(i).getAlly() == bots.get(j).getAlly()) {
							if(organs.get(i).getPower() >= enemyMaxPower) {
								enemyMaxPower = organs.get(i).getPower();
								botMainID = i;
								bots.get(j).setBaseID(i);
							}
						}
					}
				}
				if(botMainID != -1) {
					bots.get(j).setBaseID(botMainID);
					int nearestEnemyID = -1;
					double minDist = 2000;
					ArrayList<Integer> nearestEnemies = new ArrayList<Integer>();
					for(int i = 0; i < organs.size(); i++) {
						if(organs.get(i).getAlly() != bots.get(j).getAlly()) {
							double dist = distance(organs.get(botMainID).getX(), organs.get(botMainID).getY(), organs.get(i).getX(), organs.get(i).getY());
							if(dist <= minDist) {
								minDist = dist;
								nearestEnemies.add(i);
								nearestEnemyID = i;
							}
						}
					}
					if(nearestEnemyID != -1) {
						attack(nearestEnemyID, botMainID, nearestEnemies, bots.get(j).getOrganNumber() / 5 + 1); 
					}
				} else {
					 bots.get(j).setDead(true);
				}
			}
		}
	}
	
	private void checkGameStatus() {
		boolean isPlayerLiving = false;
		for(int i = 0; i < organs.size(); i++) {
			if(organs.get(i).getAlly() == 1) {
				isPlayerLiving = true;
			}
		}
		if(isPlayerLiving) {
			int livingBotNumber = 0;
			for(int i = 0; i < bots.size(); i++) {
				if(bots.get(i).getDead()) {
					livingBotNumber++;
				}
			}
			if(livingBotNumber == bots.size()) {
				Main.gameOver("you.");
			}
		} else {
			int livingBotNumber = 0;
			for(int i = 0; i < bots.size(); i++) {
				if(bots.get(i).getDead()) {
					livingBotNumber++;
				}
			}
			String botColors[] = {"red.", "blue.", "yellow.", "cyan."};
			String winner = null;
			if(livingBotNumber == bots.size() - 1) {
				for(int i = 0; i < bots.size(); i++) {
					if(!bots.get(i).getDead()) {
						winner = botColors[i];
					}
				}
				Main.gameOver(winner);
			}
		}
	}
	
	public ArrayList<Organ> getOrgans() {
		return organs;
	}
	
	public ArrayList<Cell> getCells() {
		return cells;
	}
	
	public double distance(float par1, float par2, float par3, float par4) {
		return Math.sqrt(Math.pow((par3 - par1), 2) + Math.pow((par4 - par2), 2));
	}
	
	private int[][] shuffleArray(int[][] array) {
		for(int i = 0; i < array.length; i++) {
		    int randomPosition = random.nextInt(array.length);
		    int oldX = array[i][0];
		    int oldY = array[i][1];
		    array[i][0] = array[randomPosition][0];
		    array[i][1] = array[randomPosition][1];
		    array[randomPosition][0] = oldX;
		    array[randomPosition][1] = oldY;
		}
		return array;
	}
	
	public int getEnemyNumber() {
		return enemyNumber;
	}

}
