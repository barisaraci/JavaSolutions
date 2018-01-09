package entities;

import java.awt.Color;

import game.Main;

public class Organ {
	
	private Color color;
	
	private int x, y, radius, power, maxPower, ally, organCd, isSelected, difficulty;
	private String type;
	
	public Organ(int x, int y) {
		type = "main";
		maxPower = 40;
		power = maxPower / 2;
		radius = maxPower * Main.height / 300;
		this.x = x;
		this.y = y;
		ally = 1;
		color = new Color(139 / 255f, 175 / 255f, 98 / 255f, 1);
	}
	
	public Organ(String type, int x, int y, int ally, int difficulty) {
		this.type = type;
		if(this.type == "small") {
			maxPower = 10;
		} else if(this.type == "medium") {
			maxPower = 20;
		} else if(this.type == "large") {
			maxPower = 30;
		} else if(this.type == "enemymain") {
			maxPower = 40;
		}
		power = maxPower / 2;
		radius = maxPower * Main.height / 300;
		this.x = x;
		this.y = y;
		this.ally = ally;
		this.difficulty = difficulty;
		if(this.ally == 0) {
			color = new Color(194 / 255f, 182 / 255f, 162 / 255f, 1);
		} else if(this.ally == 1) {
			color = new Color(139 / 255f, 175 / 255f, 98 / 255f, 1);
		} else if(this.ally == 2) {
			color = new Color(215 / 255f, 117 / 255f, 117 / 255f, 1);
		} else if(this.ally == 3) {
			color = new Color(64 / 255f, 105 / 255f, 202 / 255f, 1);
		} else if(this.ally == 4) {
			color = new Color(203 / 255f, 195 / 255f, 84 / 255f, 1);
		} else if(this.ally == 5) {
			color = new Color(75 / 255f, 179 / 255f, 144 / 255f, 1);
		}
	}
	
	public void increasePower() {
		if (organCd <= 0 && power < maxPower) {
			power++;
			if(ally == 1) {
				organCd = 100 - maxPower;
			} else {
				organCd = 120 - maxPower - difficulty * 3;
			}
		} else {
			organCd--;
		}
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getR() {
		return radius;
	}
	
	public void setSelected(int isSelected) {
		this.isSelected = isSelected;
	}
	
	public int getSelected() {
		return isSelected;
	}
	
	public void setAlly(int ally) {
		this.ally = ally;
	}
	
	public int getAlly() {
		return ally;
	}
	
	public void setPower(int power) {
		this.power = power;
	}
	
	public int getPower() {
		return power;
	}
	
	public int getMaxPower() {
		return maxPower;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	public Color getColor() {
		return color;
	}
	
	public String getType() {
		return type;
	}
 
}
