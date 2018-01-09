package monopoly.domains.squares;

import java.util.ArrayList;

import monopoly.domains.Color;
import monopoly.domains.Controller;
import monopoly.domains.entities.Player;

public class PropertySquare extends GameSquare {
	
	private int ownerId = -1, upgradeLevel;
	private int prices[] = new int[3]; // other prices in order of; buy, mortgage, upgrade
	private int rents[] = new int[7]; // rent prices in order of; default, 1 house, 2 houses, 3 houses, 4 houses, hotel, skyscraper
	private String colorHex;
	
	public PropertySquare(String name, Color color, int floor, int pos, int prices[], int rents[], Controller controller) {
		super(name, controller, floor, pos);
		this.color = color;
		this.prices = prices;
		this.rents = rents;
		updateLabelText();
	}
	
	public void updateLabelText() {
		String nameText = name;
		String ownerText = (ownerId == -1) ? "<font color='red'>(unclaimed)</font>" : "<font color='red'>(player" + ownerId + ")</font>";
		String upgradeText = "(" + upgradeLevel + "*)";
		String playersOnSquareText = "";
		if (players.size() > 0) {
			playersOnSquareText = "<font color='blue'>";
			for (Player player : players)
				playersOnSquareText += player.getId() + " ";
			playersOnSquareText += "</font>";
		}
		
		this.labelText = "<html><div style='text-align: center; font-size: 12; color: #000000'>" + nameText + "<br>" + ownerText + " " + upgradeText + "<br>" + playersOnSquareText + "</div></html>";
	}
	
	public void onPlayerLands(Player player) {
		super.onPlayerLands(player);
		
		if (ownerId == -1) {
			controller.setBuyMenu(name, prices[0]);
		} else if (ownerId == player.getId()) {
			player.checkUpgradeMenu(this);
		} else {
			player.payRent(ownerId, rents[upgradeLevel]);
			controller.giveTurnToNextPlayer();
		}
	}
	
	public void onPlayerLeaves(Player player) {
		super.onPlayerLeaves(player);
	}
	
	public ArrayList<PropertySquare> getPropertyPairs() {
		ArrayList<PropertySquare> properties = new ArrayList<>();
		for (GameSquare square: controller.getBoard().getSquares(floor)) {
			if (square instanceof PropertySquare) {
				PropertySquare propertySquare = ((PropertySquare) square);
				if (propertySquare.getHex().equals(colorHex)) {
					properties.add(propertySquare);
				}
			}
		}
		
		return properties;
	}
	
	public void upgrade() {
		upgradeLevel++;
		updateLabelText();
		controller.updateTextOnSquare(floor, pos);
	}
	
	public void downgrade() {
		if (upgradeLevel != 0) {
			upgradeLevel--;
			updateLabelText();
			controller.updateTextOnSquare(floor, pos);
		}
	}
	
	public int getUpgradeLevel() {
		return upgradeLevel;
	}
	
	public void setUpgradeLevel(int upgradeLevel) {
		this.upgradeLevel = upgradeLevel;
	}
	
	public int getPrice() {
		return prices[0];
	}
	
	public int getMortgagePrice() {
		return prices[1];
	}
	
	public int getUpgradePrice() {
		return prices[2];
	}
	
	public void setOwner(int id) {
		ownerId = id;
		updateLabelText();
		controller.updateTextOnSquare(floor, pos);
	}
	
	public int getOwner() {
		return ownerId;
	}
	
	public String getHex() {
		return colorHex;
	}
	
	public void setHex(String hex) {
		colorHex = hex;
	}

}
