package monopoly.domains.squares;

import monopoly.domains.Controller;
import monopoly.domains.entities.Player;

public class TransportSquare extends GameSquare {
	
	public TransportSquare(String name, Controller controller, int floor, int pos) {
		super(name, controller, floor, pos);
	}
	
	public void onPlayerLands(Player player) {
		super.onPlayerLands(player);
		
		if (name.equals("Holland Tunnel") && floor == 2 && pos == 0) {
			player.moveTo(0, 0);
		} else if (name.equals("Holland Tunnel") && floor == 0 && pos == 0) {
			player.moveTo(2, 0);
		}
		
		controller.giveTurnToNextPlayer();
	}
	
	public void onPlayerLeaves(Player player) {
		super.onPlayerLeaves(player);
	}

}
