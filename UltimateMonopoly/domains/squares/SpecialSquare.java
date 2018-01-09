package monopoly.domains.squares;

import monopoly.domains.Controller;
import monopoly.domains.entities.Player;

public class SpecialSquare extends GameSquare {
	
	public SpecialSquare(String name, Controller controller, int floor, int pos) {
		super(name, controller, floor, pos);
	}
	
	public void onPlayerLands(Player player) {
		super.onPlayerLands(player);
		
		if (name.equals("Auction")) {
			controller.setSelectType(1);
		} else {
			controller.giveTurnToNextPlayer();
		}
	}
	
	public void onPlayerLeaves(Player player) {
		super.onPlayerLeaves(player);
	}

}
