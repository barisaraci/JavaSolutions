package monopoly.domains.squares;

import monopoly.domains.Controller;
import monopoly.domains.entities.Player;

public class TaxSquare extends GameSquare {
	
	private int taxValue = 100;
	
	public TaxSquare(String name, Controller controller, int floor, int pos, int taxValue) {
		super(name, controller, floor, pos);
		if (taxValue == -1) {
			this.taxValue = 100;
		} else {
			this.taxValue = taxValue;
		}
		
	}
	
	public void onPlayerLands(Player player) {
		super.onPlayerLands(player);
		
		controller.getCurPlayer().payRent(taxValue);
		controller.giveTurnToNextPlayer();
	}
	
	public void onPlayerLeaves(Player player) {
		super.onPlayerLeaves(player);
	}

}
