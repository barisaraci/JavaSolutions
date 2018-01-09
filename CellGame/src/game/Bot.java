package game;

import java.util.Random;

public class Bot {
	private int moveCd, ally, baseID, organNumber;
	private boolean canMove;
	private Random random = new Random();
	private int difficulty;
	private boolean isDead;
	
	public Bot (int difficulty, int ally, int baseID) {
		this.difficulty = difficulty;
		this.ally = ally;
		this.baseID = baseID;
		isDead = false;
	}
	
	public void makeMove() {
		if(moveCd <= 0) {
			canMove = true;
			moveCd = 100 + random.nextInt((10 - difficulty + 1) * 20);
		} else {
			canMove = false;
			moveCd--;
		}
	}
	
	public boolean getMove() {
		return canMove;
	}

	public int getAlly() {
		return ally;
	}

	public int getBaseID() {
		return baseID;
	}

	public void setBaseID(int baseID) {
		this.baseID = baseID;
	}

	public boolean getDead() {
		return isDead;
	}

	public void setDead(boolean isDead) {
		this.isDead = isDead;
	}

	public int getOrganNumber() {
		return organNumber;
	}

	public void setOrganNumber(int organNumber) {
		this.organNumber = organNumber;
	}
	
}
