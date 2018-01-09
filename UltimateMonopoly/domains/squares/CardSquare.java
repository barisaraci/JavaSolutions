package monopoly.domains.squares;

import java.util.Random;

import monopoly.domains.Controller;
import monopoly.domains.entities.Player;

public class CardSquare extends GameSquare {
	
	private Random random = new Random();;
	
	public CardSquare(String name, Controller controller, int floor, int pos) {
		super(name, controller, floor, pos);
	}
	
	public void onPlayerLands(Player player) {
		super.onPlayerLands(player);
		
		int rnd = random.nextInt(3 + ((name.equals("Chance")) ? 1 : 0));
		
		if (name.equals("Chance")) {
			if (rnd == 0) {
				controller.getCurPlayer().goToJail();
				controller.print("Chance! Go To Jail! (player" + controller.getCurPlayer().getId() + " went to jail.)");
			} else if (rnd == 1) {
				controller.getCurPlayer().moveTo(1, 24);
				controller.print("Chance! Advance to Illinois Ave! (player" + controller.getCurPlayer().getId() + " moved to Illinois Ave.)");
			} else if (rnd == 2) {
				for (Player p: controller.getBoard().getPlayers()) {
					p.moveTo(0, 51);
				}
				controller.print("Chance! MARDI GRAS! (All players moved to Canal Street.)");
			} else if (rnd == 3) {
				controller.setSelectType(2);
				controller.print("Chance! Hurricane makes landfall! (Remove 1 house from each property in any player's 1 color group.)");
			}
		} else if (name.equals("Community Chest")) {
			if (rnd == 0) {
				controller.getCurPlayer().gain(200);
				controller.print("Community Chest! Bank Error in Your Favor! (player" + controller.getCurPlayer().getId() + " won $200.)");
			} else if (rnd == 1) {
				controller.getCurPlayer().gain(10);
				controller.print("Community Chest! You Win 2nd Place in an Board Game Remix Design Contest!! (player" + controller.getCurPlayer().getId() + " won $10.)");
			} else if (rnd == 2) {
				Player curPlayer = controller.getCurPlayer();
				int totalValue = 0;
				for (Player p: controller.getBoard().getPlayers()) {
					if (curPlayer.getId() != p.getId() && !p.isBankrupt()) {
						totalValue += p.tryConsume(50);
					}
				}
				curPlayer.gain(totalValue);
				controller.print("Community Chest! Entrepreneur of the Year! (player" + curPlayer.getId() + " collected $" + totalValue + " from other players.)");
			}
		}
		
		if (controller.getSelectType() != 2)
			controller.giveTurnToNextPlayer();
	}
	
	public void onPlayerLeaves(Player player) {
		super.onPlayerLeaves(player);
	}

}
