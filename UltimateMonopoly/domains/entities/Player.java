package monopoly.domains.entities;

import java.util.ArrayList;

import monopoly.MonopolyGame;
import monopoly.domains.Controller;
import monopoly.domains.PlayerObserver;
import monopoly.domains.squares.CompanySquare;
import monopoly.domains.squares.GameSquare;
import monopoly.domains.squares.PropertySquare;

public class Player {
	
	private Controller controller;
	private int id, pos, floor, money, numDoubles, numCabCompanies, numRailroads, numJailMoves;
	private boolean isBankrupt, isDoubled, isMrMonopoly, isFold, isInJail, isBusIcon;
	private GameSquare curSquare;
	private PlayerObserver observer;
	
	public Player(int id, Controller controller) {
		this.id = id;
		this.controller = controller;
		money = 3200;
		floor = 1;
		pos = 0;
		move(0);
	}
	
	/** 
	 * @MODIFIES: floor and position
	 * @EFFECTS: Adds pos to the player.pos.
	 * i.e., player.pos_post = this.pos + {pos} 
	 */
	public void move(int pos) {
		controller.getBoard().getSquare(this.floor, this.pos).onPlayerLeaves(this);
		
		if (floor == 1 && ((this.pos <= 5 && (this.pos + pos) > 5) || (this.pos + pos >= 39 && this.pos + pos - 40 > 5))) {
			if (pos % 2 == 0) {
				floor = 0;
				this.pos = (this.pos < 5 && (this.pos + pos) > 5) ? 49 + (pos - (6 - this.pos)) : 49 + (pos - (6 + (40 - this.pos))); 
			} else {
				this.pos += pos;
			}
		} else if (floor == 1 && this.pos <= 25 && this.pos + pos > 25) {
			if (pos % 2 == 0) {
				floor = 0;
				this.pos = 21 + (pos - (26 - this.pos)); 
			} else {
				this.pos += pos;
			}
		} else if (floor == 1 && this.pos <= 15 && this.pos + pos > 15) {
			if (pos % 2 == 0) {
				floor = 2;
				this.pos = 15 + (pos - (16 - this.pos)); 
			} else {
				this.pos += pos;
			}
		} else if (floor == 1 && this.pos <= 35 && this.pos + pos > 35) {
			if (pos % 2 == 0) {
				floor = 2;
				this.pos = 3 + (pos - (36 - this.pos)); 
			} else {
				this.pos += pos;
			}
		} else if (floor == 0 && this.pos <= 49 && this.pos + pos > 49) {
			if (pos % 2 == 0) {
				floor = 1;
				this.pos = 5 + (pos - (50 - this.pos)); 
			} else {
				this.pos += pos;
			}
		} else if (floor == 0 && this.pos <= 21 && this.pos + pos > 21) {
			if (pos % 2 == 0) {
				floor = 1;
				this.pos = 25 + (pos - (22 - this.pos)); 
			} else {
				this.pos += pos;
			}
		} else if (floor == 2 && this.pos <= 15 && this.pos + pos > 15) {
			if (pos % 2 == 0) {
				floor = 1;
				this.pos = 15 + (pos - (16 - this.pos)); 
			} else {
				this.pos += pos;
			}
		} else if (floor == 2 && this.pos <= 3 && this.pos + pos > 3) {
			if (pos % 2 == 0) {
				floor = 1;
				this.pos = 35 + (pos - (4 - this.pos)); 
			} else {
				this.pos += pos;
			}
		} else {
			this.pos += pos;
		}
		
		if (floor == 1 && this.pos > 39) {
			gain(200);
			controller.print("player" + id + " has passed the starting point and got $200.");
			this.pos -= 40;
		} else if (floor == 0 && this.pos > 55) {
			this.pos -= 56;
		} else if (floor == 2 && this.pos > 23) {
			this.pos -= 24;
		}
		
		curSquare = controller.getBoard().getSquare(floor, this.pos);
		curSquare.onPlayerLands(this);
	}
	
	/** @MODIFIES: player.floor and player.pos
	 *  @EFFECTS: Makes the player move directly to the given pos and floor.
	 *  i.e., player.floor_post = {floor}, player.pos_post = {pos}
	 */
	public void moveTo(int floor, int pos) {
		controller.getBoard().getSquare(this.floor, this.pos).onPlayerLeaves(this);
		this.floor = floor;
		this.pos = pos;
		curSquare = controller.getBoard().getSquare(this.floor, this.pos);
		curSquare.onPlayerLands(this);
	}
	
	/** 
	 * @MODIFIES: this.money
	 * @EFFECTS: If player.money is less than money, player goes bankrupt. Otherwise, player's money decreases by amount of money.
	 * i.e., player.money_post = this.money - {money}
	 */
	public int tryConsume(int money) {
		if (isBankrupt)
			return 0;
		
		if (this.money >= money) {
			this.money -= money;
			controller.updatePlayerAssets(id);
			return money;
		} else {
			isBankrupt = true;
			controller.updatePlayerAssets(id);
			controller.playerBankrupt();
			controller.print("player" + id + " went bankrupt.");
			return this.money;
		}
	}
	
	/**
	 * @MODIFIES: this.money and owner's money
	 * @EFFECTS: Decreases this player's money by amount of rentPrice, and increases the owner's money by amount of rentPrice.
	 * i.e., player.pos_post = this.pos + {pos}
	 */
	public void payRent(int ownerId, int rentPrice) {
		if (!controller.getBoard().getPlayer(ownerId).isBankrupt()) {
			int amount = tryConsume(rentPrice	*100);
			controller.getBoard().getPlayer(ownerId).gain(amount);
			controller.print("player" + id + " has payed $" + amount + " to the player" + ownerId + " as a rent.");
			notifyObserver(MonopolyGame.EMOJI_RENT);
		}
	}
	
	public void payRent(int rentPrice) {
		int amount = tryConsume(rentPrice);
		controller.print("player" + id + " has payed $" + amount + " as a tax.");
		notifyObserver(MonopolyGame.EMOJI_RENT);
	}
	
	/**
	 * @MODIFIES: this.money
	 * @EFFECTS: Adds money to the player.money.
	 * i.e., player.money_post = this.money + {money}
	 */
	public void gain(int money) {
		if (!isBankrupt) {
			this.money += money;
			controller.updatePlayerAssets(id);
			notifyObserver(MonopolyGame.EMOJI_GAIN);
		}
	}
	
	/**
	 * @MODIFIES: curSquare.ownerId
	 * @EFFECTS: Changes the value of the curSquare.ownerId to this.id if this player can afford the price.
	 */
	public void tryToBuyProperty() {
		if (this.money >= ((curSquare instanceof PropertySquare) ? ((PropertySquare) curSquare).getPrice() : ((CompanySquare) curSquare).getPrice())) {
			if (curSquare instanceof PropertySquare)
				((PropertySquare) curSquare).setOwner(id);
			else
				((CompanySquare) curSquare).setOwner(id);;
			tryConsume((curSquare instanceof PropertySquare) ? ((PropertySquare) curSquare).getPrice() : ((CompanySquare) curSquare).getPrice());
			controller.propertyBuyed();
			controller.print("player" + id + " has bought " + curSquare.getName());
		} else {
			controller.print("player" + id + " could not afford " + curSquare.getName());
		}
	}
	
	/**
	 * @MODIFIES: curSquare.ownerId
	 * @EFFECTS: Changes the value of the curSquare.ownerId to this.id if this player can afford the price.
	 */
	public void tryToBuyProperty(PropertySquare property, int auctionPrice) {
			property.setOwner(id);
			tryConsume(auctionPrice);
			controller.propertyBuyed();
			controller.print("player" + id + " has bought " + property.getName());
	}
	
	/**
	 * @MODIFIES: square.upgradeLevel
	 * @EFFECTS: Adds 1 to the square.upgradeLevel if the conditions are appropriate
	 * i.e., square.upgradeLevel_post = square.upgradeLevel + 1
	 */
	public void tryUpgrade(GameSquare square) {
		if (square == null || !(square instanceof PropertySquare)) {
			controller.showError("Selected square is not a property.");
			return;
		}
		
		PropertySquare propertySquare = (PropertySquare) square;
		
		if (id != propertySquare.getOwner()) {
			controller.showError("Selected property is not yours.");
			return;
		}
		
		if (propertySquare.getUpgradeLevel() > 5) {
			controller.showError("You cannot upgrade skyscraper.");
			return;
		}
				
		if (this.money < propertySquare.getUpgradePrice()) {
			controller.showError("You cannot afford it.");
			return;
		}
		
		if (!((PropertySquare) curSquare).getHex().equals(propertySquare.getHex())) {
			controller.showError("You have to be on any pair of that property set to upgrade it.");
			return;
		}
		
		ArrayList<PropertySquare> properties = propertySquare.getPropertyPairs();
		
		if (!isProvidingMajorityOwnership(properties)) {
			controller.showError("You have to own the other pairs of this property.");
			return;
		}
		
		if (!isProvidingUpgradeCheck(propertySquare, properties)) {
			controller.showError("You have to upgrade the other pairs to the same level with this property.");
			return;
		}
		
		tryConsume(propertySquare.getUpgradePrice());
		propertySquare.upgrade();
	}
	
	/**
	 * @EFFECTS: Returns whether the property provide majority ownership or not.
	 */
	private boolean isProvidingMajorityOwnership(ArrayList<PropertySquare> properties) {
		boolean result = true;
		
		for (PropertySquare property: properties) {
			if (property.getOwner() != id)
				result = false;
		}
		
		return result;
	}
	
	/**
	 * @EFFECTS: Returns whether the other pairs of the property's upgradeLevel is equal to or 1 more than propertySquare.
	 */
	private boolean isProvidingUpgradeCheck(PropertySquare propertySquare, ArrayList<PropertySquare> properties) {
		boolean result = true;
		
		for (PropertySquare property: properties) {
			if (property.getUpgradeLevel() < propertySquare.getUpgradeLevel())
				result = false;
		}
		
		return result;
	}
	
	public void checkUpgradeMenu(PropertySquare propertySquare) {
		//ArrayList<PropertySquare> properties = propertySquare.getPropertyPairs();
		
		//if (isProvidingMajorityOwnership(properties))
			controller.setUpgradeMenu();
	}
	
	public void setDoubled(boolean isDoubled) {
		if (isDoubled) {
			if (numDoubles == 2) {
				goToJail();
			} else {
				numDoubles++;
				this.isDoubled = isDoubled;
			}
		} else {
			this.isDoubled = isDoubled;
		}
	}
	
	public void goToJail() {
		isInJail = true;
		numJailMoves = 0;
		controller.print("player" + id + " went to jail.");
		notifyObserver(MonopolyGame.EMOJI_JAIL);
	}
	
	public void increaseCabCompany() {
		numCabCompanies++;
	}
	
	public void increaseRaildroad() {
		numRailroads++;
	}
	
	public void load(int floor, int pos, int money, int isBankrupt, int numCabCompanies, int numRailroads) {
		controller.getBoard().getSquare(this.floor, this.pos).onPlayerLeaves(this);
		this.floor = floor;
		this.pos = pos;
		this.money = money;
		this.numCabCompanies = numCabCompanies;
		this.numRailroads = numRailroads;
		if (isBankrupt == 1) {
			this.isBankrupt = true;
			controller.playerBankrupt();
		}
		controller.updatePlayerAssets(id);
		curSquare = controller.getBoard().getSquare(floor, this.pos);
		curSquare.addPlayer(this);
	}
	
	public void resetDoubles() {
		numDoubles = 0;
	}
	
	public void notifyObserver(String state) {
		observer.update(state);
	}
	
	public void attach(PlayerObserver observer) {
		this.observer = observer;
	}

	public int getNumCabCompanies() {
		return numCabCompanies;
	}

	public int getNumRailroads() {
		return numRailroads;
	}

	public int getId() {
		return id;
	}
	
	public int getMoney() {
		return money;
	}
	
	public boolean isDoubled() {
		return isDoubled;
	}

	public boolean isMrMonopoly() {
		return isMrMonopoly;
	}
	
	public boolean isBusIcon() {
		return isBusIcon;
	}

	public void setMrMonopoly(boolean isMrMonopoly) {
		this.isMrMonopoly = isMrMonopoly;
	}
	
	public void setBusIcon(boolean isBusIcon) {
		this.isBusIcon = isBusIcon;
	}

	public boolean isBankrupt() {
		return isBankrupt;
	}

	public void setBankrupt(boolean isBankrupt) {
		if (isBankrupt)
			notifyObserver(MonopolyGame.EMOJI_BANKRUPT);
		
		this.isBankrupt = isBankrupt;
	}
	
	public GameSquare getSquare() {
		return curSquare;
	}
	
	public int getFloor() {
		return floor;
	}
	
	public int getPos() {
		return pos;
	}

	public boolean isFold() {
		return isFold;
	}
	
	public void setFold(boolean isFold) {
		this.isFold = isFold;
	}

	public boolean isInJail() {
		return isInJail;
	}

	public void setInJail(boolean isInJail) {
		this.isInJail = isInJail;
	}

	public int getNumJailMoves() {
		return numJailMoves;
	}

	public void setNumJailMoves(int numJailMoves) {
		this.numJailMoves = numJailMoves;
	}

	@Override
	public String toString() {
		return "Player [controller=" + controller + ", id=" + id + ", pos=" + pos + ", floor=" + floor + ", money="
				+ money + ", numDoubles=" + numDoubles + ", numCabCompanies=" + numCabCompanies + ", numRailroads="
				+ numRailroads + ", numJailMoves=" + numJailMoves + ", isBankrupt=" + isBankrupt + ", isDoubled="
				+ isDoubled + ", isMrMonopoly=" + isMrMonopoly + ", isFold=" + isFold + ", isInJail=" + isInJail
				+ ", curSquare=" + curSquare + ", observer=" + observer + "]";
	}
	
	public boolean repOk() {
		if (floor != 1 || pos != 0)
			return false;
		
		if (money != 3200)
			return false;
		
		if (id < 0 || id > 7)
			return false;
		
		return true;
	}
	
}
