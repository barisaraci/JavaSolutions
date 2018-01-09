package entities;

import game.Main;

import java.awt.Color;

public class Cell {
	
	private Color color;
	
	private float x, y;
	private double moveDistX, moveDistY;
	private int radius, attackID, ally;
	private boolean canHarm, canRemove;
	
	public Cell(int x, int y, int attackID, int organPower, int ally, Color color) {
		radius = Main.height / 150;
		this.x = x;
		this.y = y;
		this.ally = ally;
		if(this.ally == 1) {
			this.color = new Color(121 / 255f, 238 / 255f, 62 / 255f, 1);
		} else if(this.ally == 2){
			this.color = new Color(238 / 255f, 62 / 255f, 62 / 255f, 1);
		} else {
			this.color = color;
		}
		this.attackID = attackID;
	}
	
	public void move(float x, float y, int r) {
		double radian = Math.atan2(y - this.y, x - this.x);
		
		moveDistX = 5 * Math.cos(radian);
		moveDistY = 5 * Math.sin(radian);
		
		if(distance(this.x, this.y, x, y) <= r) {
			canHarm = true;
		} else {
			this.x += moveDistX;
			this.y += moveDistY;
		}
	}
	
	public int getAlly() {
		return ally;
	}
	
	public void setX(float x) {
		this.x = x;
	}
	
	public float getX() {
		return x;
	}
	
	public void setY(float y) {
		this.y = y;
	}
	
	public float getY() {
		return y;
	}
	
	public float getR() {
		return radius;
	}
	
	public int getAttackID() {
		return attackID;
	}
	
	public Color getColor() {
		return color;
	}
	
	public boolean getHarm() {
		return canHarm;
	}
	
	public boolean isCanRemove() {
		return canRemove;
	}

	public void setCanRemove(boolean canRemove) {
		this.canRemove = canRemove;
	}

	public double distance(float par1, float par2, float par3, float par4) {
		return Math.sqrt(Math.pow((par3 - par1), 2) + Math.pow((par4 - par2), 2));
	}
	
}
