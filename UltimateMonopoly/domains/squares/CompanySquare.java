package monopoly.domains.squares;

import monopoly.domains.Color;
import monopoly.domains.Controller;
import monopoly.domains.entities.Player;

public class CompanySquare extends GameSquare {
	
	private String companyName;
	private int ownerId = -1;
	private int prices[] = new int[2]; // other prices in order of; buy, mortgage
	private int rents[] = new int[4]; // rent prices in order of; 1 owned, 2 owned, 3, owned, 4 owned
	
	public CompanySquare(String companyName, String name, int floor, int pos, int prices[], int rents[], Controller controller) {
		super(name, controller, floor, pos);
		this.companyName = companyName;
		this.prices = prices;
		this.rents = rents;
		color = new Color(113, 155, 255);
		updateLabelText();
	}
	
	public void updateLabelText() {
		String nameText = name;
		String ownerText = (ownerId == -1) ? "<font color='red'>(unclaimed)</font>" : "<font color='red'>(player" + ownerId + ")</font>";
		String playersOnSquareText = "";
		if (players.size() > 0) {
			playersOnSquareText = "<font color='blue'>";
			for (Player player : players)
				playersOnSquareText += player.getId() + " ";
			playersOnSquareText += "</font>";
		}
		
		this.labelText = "<html><div style='text-align: center; font-size: 12; color: #000000'>" + nameText + "<br>" + ownerText + "<br>" + playersOnSquareText + "</div></html>";
	}
	
	public void onPlayerLands(Player player) {
		super.onPlayerLands(player);
		
		if (ownerId == -1) {
			controller.setBuyMenu(name, prices[0]);
		} else if (ownerId != player.getId()) {
			player.payRent(ownerId, (companyName.equals("Cab")) ? (rents[controller.getBoard().getPlayer(ownerId).getNumCabCompanies()])
																: (rents[controller.getBoard().getPlayer(ownerId).getNumRailroads()]));
			controller.giveTurnToNextPlayer();
		}
	}
	
	public void onPlayerLeaves(Player player) {
		super.onPlayerLeaves(player);
	}
	
	public int getPrice() {
		return prices[0];
	}
	
	public void setOwner(int id) {
		ownerId = id;
		updateLabelText();
		controller.updateTextOnSquare(floor, pos);
	}

	public int getOwner() {
		return ownerId;
	}

}
