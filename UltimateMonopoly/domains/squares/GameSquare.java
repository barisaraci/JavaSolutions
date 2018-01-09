package monopoly.domains.squares;

import java.util.ArrayList;
import java.util.Iterator;

import monopoly.domains.Color;
import monopoly.domains.Controller;
import monopoly.domains.entities.Player;

public class GameSquare {
	
	protected Controller controller;
	protected String name, labelText;
	protected Color color;
	protected int floor, pos;
	protected ArrayList<Player> players = new ArrayList<>(); // players on square
	
	public GameSquare(String name, Controller controller, int floor, int pos) {
		this.name = name;
		this.controller = controller;
		this.floor = floor;
		this.pos = pos;
		color = new Color(213, 255, 255);
		updateLabelText();
	}
	
	/**
	 * @MODIFIES: game view
	 * @EFFECTS: Updates the square's label
	 */
	public void updateLabelText() {
		String nameText = name;
		String playersOnSquareText = "";
		if (players.size() > 0) {
			playersOnSquareText = "<font color='blue'>";
			for (Player player : players)
				playersOnSquareText += player.getId() + " ";
			playersOnSquareText += "</font>";
		}
		
		this.labelText = "<html><div style='text-align: center; font-size: 12; color: #000000'>" + nameText + "<br>" + playersOnSquareText + "</div></html>";
	}
	
	/**
	 * @MODIFIES: turn
	 * @EFFECTS: Do appropriate action regarding the square type then give the turn to next player.
	 */
	public void onPlayerLands(Player player) {
		players.add(player);
		updateLabelText();
		controller.updateTextOnSquare(floor, pos);
		
		if (controller.getBoard().isBoardReady() && name.equals("GO"))
			controller.giveTurnToNextPlayer();
	}
	
	/**
	 * @MODIFIES: game view
	 * @EFFECTS: Updates the square's label by removing player's id from label
	 */
	public void onPlayerLeaves(Player player) {
		for (Iterator<Player> i = players.iterator(); i.hasNext();) {
			Player pl = i.next();
			
			if (pl.equals(player)) {
				i.remove();
			}
		}
		updateLabelText();
		controller.updateTextOnSquare(floor, pos);
	}
	
	/**
	 * @MODIFIES: game view
	 * @EFFECTS: Updates the square's label by removing player's id from label
	 */
	public void addPlayer(Player player) {
		players.add(player);
		updateLabelText();
		controller.updateTextOnSquare(floor, pos);
	}
	
	public int getFloor() {
		return floor;
	}
	
	public int getPos() {
		return pos;
	}
	
	public String getName() {
		return name;
	}
	
	public String getLabelText() {
		return labelText;
	}
	
	public Color getColor() {
		return color;
	}
	
	@Override
	public String toString() {
		return "GameSquare [controller=" + controller + ", name=" + name + ", labelText=" + labelText + ", color="
				+ color + ", floor=" + floor + ", pos=" + pos + ", players=" + players + "]";
	}

	public boolean repOk() {
		if (name.isEmpty())
			return false;
		
		if (color.getR() != 213 || color.getG() != 255 || color.getB() != 255)
			return false;
		
		return true;
	}
	
	
}
