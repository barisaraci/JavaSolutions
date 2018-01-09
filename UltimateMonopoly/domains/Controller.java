package monopoly.domains;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

import javax.swing.JOptionPane;
import javax.swing.Timer;

import monopoly.MonopolyGame;
import monopoly.domains.entities.Board;
import monopoly.domains.entities.Player;
import monopoly.domains.squares.CardSquare;
import monopoly.domains.squares.CompanySquare;
import monopoly.domains.squares.GameSquare;
import monopoly.domains.squares.PropertySquare;
import monopoly.ui.GameView;

public class Controller {
	
	private Board board;
	private GameView gameView;
	private int numPlayers, numPlayersBankrupt, numPlayersFold, numPropertiesOwned, turn, bidTurn, saleTurn, salePrice, propertyPrice, bidPrice, selectType;
	private boolean isGameOver, isAuctionActive, isSaleActive;
	private Player curPlayer, curAuctionPlayer, curSalePlayer;
	private GameSquare selectedSquare;
	private PropertySquare auctionProperty, saleProperty;
	private ControllerObserver observer;
	private Timer afkTimer;
	
	public Controller(int numPlayers) {
		this.numPlayers = numPlayers;
		board = new Board(this);
	}
	
	/** 
	 * @REQUIRES: game is not over
	 * @MODIFIES: turn
	 * @EFFECTS: Gives turn to next player.
	 */
	public void giveTurnToNextPlayer() {
		if (isGameOver)
			return;
		
		if (curPlayer.isMrMonopoly()) {
			curPlayer.setMrMonopoly(false);
			if (numPropertiesOwned >= MonopolyGame.TOTAL_NUMBER_OF_PROPERTIES[curPlayer.getFloor()])
				curPlayer.moveTo(curPlayer.getFloor(), findNextPropertyPos(curPlayer.getFloor(), true));
			else
				curPlayer.moveTo(curPlayer.getFloor(), findNextPropertyPos(curPlayer.getFloor(), false));
		} else if (curPlayer.isBusIcon()) {
			curPlayer.setBusIcon(false);
			curPlayer.moveTo(curPlayer.getFloor(), findNextChanceOrCommunityPos(curPlayer.getFloor()));
		} else if (curPlayer.isDoubled()) {
			curPlayer.setDoubled(false);
			gameView.updatePlayerTurn();
		} else {
			do {
				turn++;
				if (turn >= numPlayers)
					turn -= numPlayers;
				
				curPlayer = board.getPlayer(turn);
				if (curPlayer.isInJail()) {
					if (curPlayer.getNumJailMoves() >= 3) {
						curPlayer.setInJail(false);
					} else {
						curPlayer.setNumJailMoves(curPlayer.getNumJailMoves() + 1);
						print("player" + turn + " cannot move since the player is in jail for " + (3 - curPlayer.getNumJailMoves()) + " more turns.");
					}
				}
			} while (curPlayer.isBankrupt() || curPlayer.isInJail());
			gameView.updatePlayerTurn();
		}
	}
	
	/** 
	 * @MODIFIES: bidTurn
	 * @EFFECTS: Gives bid turn to next player.
	 */
	public void giveBidTurnToNextPlayer() {
		do {
			bidTurn++;
			if (bidTurn >= numPlayers)
				bidTurn -= numPlayers;
			
			curAuctionPlayer = board.getPlayer(bidTurn);
		} while (curAuctionPlayer.isFold());
		gameView.updateAuctionInfo();
	}
	
	public void giveSaleTurnToNextPlayer() {
		if (saleTurn >= numPlayers) {
			isSaleActive = false;
		} else {
			saleTurn++;
			if (saleTurn == curPlayer.getId())
				saleTurn++;
			curSalePlayer = board.getPlayer(saleTurn);
			gameView.updateSaleInfo();
		}
	}
	
	/** 
	 * @MODIFIES: game view
	 * @EFFECTS: Updates player's assets.
	 */
	public void updatePlayerAssets(int playerId) {
		gameView.updatePlayerAssets(playerId);
	}
	
	/** 
	 * @MODIFIES: game view
	 * @EFFECTS: Makes the visibility of buy menu visible.
	 */
	public void setBuyMenu(String name, int price) {
		gameView.activateBuyMenu(name, price);
	}
	
	/** 
	 * @MODIFIES: game view
	 * @EFFECTS: Makes the visibility of upgrade menu visible.
	 */
	public void setUpgradeMenu() {
		selectedSquare = null;
		//gameView.activateUpgradeMenu();
	}
	
	/** 
	 * @MODIFIES: player assets and afk timer
	 * @EFFECTS: Initiates player assets and starts the afk timer.
	 */
	private void startGame() {
		board.initPlayers();
		afkTimer = new Timer((int) 1000 * 30, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				notifyObserver(MonopolyGame.EMOJI_AFK, curPlayer.getId());
			}
		});
		afkTimer.setRepeats(true);
		afkTimer.start();
		
	}
	
	/** 
	 * @MODIFIES: board.totalValue
	 * @EFFECTS: Rolls the dice.
	 */
	public void onButtonDiceClick() {
		afkTimer.restart();
		notifyObserver(MonopolyGame.EMOJI_DEFAULT, curPlayer.getId());
		board.rollTheDice();
	}
	
	/** 
	 * @EFFECTS: Upgrades the selected property.
	 */
	public void onButtonUpgradeClick() {
		curPlayer.tryUpgrade(selectedSquare);
	}
	
	/** 
	 * @EFFECTS: Sells the selected property.
	 */
	public void onButtonMortgageClick() {
		if (selectedSquare == null || !(selectedSquare instanceof PropertySquare)) {
			showError("Selected square is not a property.");
			return;
		}
		
		PropertySquare propertySquare = (PropertySquare) selectedSquare;
		
		if (curPlayer.getId() != propertySquare.getOwner()) {
			showError("Selected property is not yours.");
			return;
		}
		
		int price = Integer.valueOf(JOptionPane.showInputDialog("Enter a price for sale"));
		putPropertyUpForSale(((PropertySquare) selectedSquare), price);
	}
	
	/** 
	 * @EFFECTS: Raises the price if the auction is active, otherwise buys property.
	 */
	public void onButtonPositiveClick() {
		if (isAuctionActive) {
			if (curAuctionPlayer.getMoney() > propertyPrice + bidPrice) {
				propertyPrice += bidPrice;
				gameView.updateAuctionInfo();
				
				if (numPlayersFold >= numPlayers - 1) {
					for (Player pl : board.getPlayers()) {
						if (!pl.isFold()) {
							isAuctionActive = false;
							pl.tryToBuyProperty(auctionProperty, propertyPrice);
							giveTurnToNextPlayer();
						}
					}
					isAuctionActive = false;
					giveTurnToNextPlayer();
				} else {
					giveBidTurnToNextPlayer();
				}
			} else {
				print("player" + bidTurn + " could not afford " + curPlayer.getSquare().getName());
			}
		} else if (isSaleActive) {
			isSaleActive = false;
			if (curSalePlayer.getMoney() > salePrice) {
				curSalePlayer.tryConsume(salePrice);
				curPlayer.gain(salePrice);
				saleProperty.setOwner(curSalePlayer.getId());
				saleProperty.updateLabelText();
				updateTextOnSquare(saleProperty.getFloor(), saleProperty.getPos());
				updatePlayerAssets(curPlayer.getId());
				updatePlayerAssets(curSalePlayer.getId());
				print("player" + saleTurn + " could not afford " + saleProperty.getName());
				giveTurnToNextPlayer();
			} else {
				print("player" + saleTurn + " could not afford " + saleProperty.getName());
			}
		} else {
			if (curPlayer.getSquare() instanceof PropertySquare) {
				curPlayer.tryToBuyProperty();
			}/* else if (curPlayer.getSquare() instanceof SpecialSquare && curPlayer.getSquare().getName().equals("Roll Once")) {
				rollOnce();
			} else if (curPlayer.getSquare() instanceof SpecialSquare && curPlayer.getSquare().getName().equals("Squeeze Play")) {
				squeezePlay();
			}*/
			
			giveTurnToNextPlayer();
		}
	}
	
	/** 
	 * @EFFECTS: Player folds if the auction is active, otherwise puts current property up for auction.
	 */
	public void onButtonNegativeClick() {
		if (isAuctionActive) {
			curAuctionPlayer.setFold(true);
			numPlayersFold++;
			if (numPlayersFold >= numPlayers - 1) {
				for (Player pl : board.getPlayers()) {
					if (!pl.isFold()) {
						isAuctionActive = false;
						pl.tryToBuyProperty(auctionProperty, propertyPrice);
						giveTurnToNextPlayer();
					}
				}
				isAuctionActive = false;
				giveTurnToNextPlayer();
			} else {
				giveBidTurnToNextPlayer();
			}
		} else if (isSaleActive) {
			giveSaleTurnToNextPlayer();
		} else {
			if (curPlayer.getSquare() instanceof PropertySquare) {
				putPropertyUpForAuction((PropertySquare) curPlayer.getSquare(), false);
			} else {
				giveTurnToNextPlayer();
			}
		}
	}
	
	/** 
	 * @EFFECTS: Puts the current property up for auction.
	 */
	public void putPropertyUpForAuction(PropertySquare property, boolean isAuctionSquare) {
		isAuctionActive = true;
		numPlayersFold = 0;
		auctionProperty = property;
		if (!isAuctionSquare) {
			for (Player p: board.getPlayers())
				p.setFold(false);
			curPlayer.setFold(true);
			numPlayersFold++;
		}
		propertyPrice = property.getPrice();
		bidPrice = property.getPrice() / 10;
		gameView.activateAuction();
		bidTurn = turn;
		giveBidTurnToNextPlayer();
	}
	
	public void putPropertyUpForSale(PropertySquare property, int price) {
		isSaleActive = true;
		saleProperty = property;
		salePrice = price;
		gameView.activateSale();
		saleTurn = -1;
		giveSaleTurnToNextPlayer();
	}
	
	/** 
	 * @MODIFIES: game view
	 * @EFFECTS: Ends game if all the players are gone bankrupt except one, otherwise increases number of players gone bankrupt by 1.
	 */
	public void playerBankrupt() { // checks whether there is a winner or not
		numPlayersBankrupt++;
		if (numPlayersBankrupt >= numPlayers - 1) {
			isGameOver = true;
			for (Player pl : board.getPlayers()) {
				if (!pl.isBankrupt()) {
					gameView.gameOver(pl.getId());
					notifyObserver(MonopolyGame.EMOJI_BANKRUPT, pl.getId());
				}
			}
		}
	}
	
	/** 
	 * @EFFECTS: Returns the difference between next property and player's position.
	 */
	public int findNextPropertyPos(int floor, boolean isEverythingOwned) {
		int nextPos = curPlayer.getPos() + 1;
		if (nextPos > MonopolyGame.TOTAL_NUMBER_OF_SQUARES[floor] - 1)
			nextPos -= MonopolyGame.TOTAL_NUMBER_OF_SQUARES[floor];
		GameSquare nextProperty = board.getSquare(floor, nextPos);
		
		while (true) {
			if (isEverythingOwned) {
				if (nextProperty instanceof PropertySquare) {
					break;
				}
			} else {
				if (nextProperty instanceof PropertySquare && ((PropertySquare) nextProperty).getOwner() == -1) {
					break;
				}
			}
			
			nextPos++;
			if (nextPos > MonopolyGame.TOTAL_NUMBER_OF_SQUARES[floor] - 1)
				nextPos -= MonopolyGame.TOTAL_NUMBER_OF_SQUARES[floor];
			nextProperty = board.getSquare(floor, nextPos);
		}
		
		return nextProperty.getPos();
	}
	
	public int findNextChanceOrCommunityPos(int floor) {
		int nextPos = curPlayer.getPos() + 1;
		if (nextPos > MonopolyGame.TOTAL_NUMBER_OF_SQUARES[floor] - 1)
			nextPos -= MonopolyGame.TOTAL_NUMBER_OF_SQUARES[floor];
		GameSquare nextProperty = board.getSquare(floor, nextPos);
		
		while (true) {
			if (nextProperty instanceof CardSquare) {
				break;
			}
			
			nextPos++;
			if (nextPos > MonopolyGame.TOTAL_NUMBER_OF_SQUARES[floor] - 1)
				nextPos -= MonopolyGame.TOTAL_NUMBER_OF_SQUARES[floor];
			nextProperty = board.getSquare(floor, nextPos);
		}
		
		return nextProperty.getPos();
	}
	
	/** 
	 * @EFFECTS: Saves the game into a file.
	 */
	public void saveGame() {
		try {
			File file = new File("save.mnply");
			FileOutputStream fileOutputStream = new FileOutputStream(file);
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, Charset.forName("UTF-8"));
			BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);

			if (!file.exists()) {
				file.createNewFile();
			}

			String saveString = getSaveString().trim();

			bufferedWriter.write(saveString);
			bufferedWriter.flush();
			bufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/** 
	 * @EFFECTS: Transforms game info to a string.
	 */
	private String getSaveString() {
		String save = Integer.toString(numPlayers) + " " + turn + System.lineSeparator();
		
		for (int i = 0; i < numPlayers; i++) {
			Player pl = board.getPlayer(i);
			String playerLine = pl.getFloor() + " " + pl.getPos() + " " + pl.getMoney() + " " + (pl.isBankrupt() ? 1 : 0) + " " + pl.getNumCabCompanies() + " " + pl.getNumRailroads() + System.lineSeparator();
			save += playerLine;
		}
		
		for (int i = 0; i < 3; i++) {
			for (GameSquare sq : board.getSquares(i)) {
				String squareLine;
				if (sq instanceof PropertySquare) {
					squareLine = sq.getFloor() + " " + sq.getPos() + " " + ((PropertySquare) sq).getOwner() + " " + ((PropertySquare) sq).getUpgradeLevel() + System.lineSeparator();
				} else if (sq instanceof CompanySquare) {
					squareLine = sq.getFloor() + " " + sq.getPos() + " " + ((CompanySquare) sq).getOwner() + System.lineSeparator();
				} else {
					squareLine = -2 + System.lineSeparator();
				}
				save += squareLine;
			}
		}
		
		return save;
	}
	
	/** 
	 * @EFFECTS: Loads the game from a file.
	 */
	public void loadGame() {
		try {
			FileInputStream fileInputStream = new FileInputStream(new File("save.mnply"));
			InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, Charset.forName("UTF-8"));
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

			String line;
			int count = 0;

			while ((line = bufferedReader.readLine()) != null) {
				if (count == 0) {
					turn = Integer.valueOf(line.split(" ")[1]);
					gameView.updatePlayerTurn();
				} else if (count - 1 < numPlayers) {
					String playerInfo[] = line.split(" ");
					board.getPlayer(count - 1).load(Integer.valueOf(playerInfo[0]), Integer.valueOf(playerInfo[1]), Integer.valueOf(playerInfo[2]), Integer.valueOf(playerInfo[3]), Integer.valueOf(playerInfo[4]), Integer.valueOf(playerInfo[5]));
				} else {
					String squareInfo[] = line.split(" ");
					if (squareInfo.length == 4) {
						PropertySquare pr = ((PropertySquare) board.getSquare(Integer.valueOf(squareInfo[0]), Integer.valueOf(squareInfo[1])));
						pr.setOwner(Integer.valueOf(squareInfo[2]));
						pr.setUpgradeLevel(Integer.valueOf(squareInfo[3]));
						pr.updateLabelText();
					} else if (squareInfo.length == 3) {
						CompanySquare pr = ((CompanySquare) board.getSquare(Integer.valueOf(squareInfo[0]), Integer.valueOf(squareInfo[1])));
						pr.setOwner(Integer.valueOf(squareInfo[2]));
						pr.updateLabelText();
					}
				}
				count++;
			}

			bufferedReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void notifyObserver(String state, int id) {
		observer.update(state, id);
	}
	
	public PropertySquare getAuctionProperty() {
		return auctionProperty;
	}
	
	public PropertySquare getSaleProperty() {
		return saleProperty;
	}
	
	public void attach(ControllerObserver observer) {
		this.observer = observer;
	}
	
	public void updateTextOnSquare(int floor, int pos) {
		gameView.updateTextOnSquare(floor, pos);
	}
	
	public void print(String text) {
		gameView.print(text);
	}
	
	public void showError(String text) {
		gameView.showError(text);
	}
	
	public void setView(GameView view) {
		gameView = view;
		startGame();
	}
	
	public void selectSquare(GameSquare square) {
		selectedSquare = square;
		
		if (selectType == 1 && selectedSquare instanceof PropertySquare) {
				putPropertyUpForAuction((PropertySquare) selectedSquare, true);
				giveTurnToNextPlayer();
		} else if (selectType == 2 && selectedSquare instanceof PropertySquare) {
			for (PropertySquare property: ((PropertySquare) selectedSquare).getPropertyPairs()) {
				property.downgrade();
			}
			giveTurnToNextPlayer();
		} else {
			print("player" + turn + " did not select a property.");
		}
		
		selectType = 0;
	}
	
	public void propertyBuyed() {
		numPropertiesOwned++;
	}
	
	public Board getBoard() {
		return board;
	}
	
	public int getTurn() {
		return turn;
	}
	
	public int getNumPlayers() {
		return numPlayers;
	}

	public Player getCurPlayer() {
		return curPlayer;
	}
	
	public GameSquare getSelectedSquare() {
		return selectedSquare;
	}

	public void setCurPlayer(Player curPlayer) {
		this.curPlayer = curPlayer;
	}

	public int getBidTurn() {
		return bidTurn;
	}
	
	public int getSaleTurn() {
		return saleTurn;
	}

	public int getBidPrice() {
		return bidPrice;
	}

	public int getPropertyPrice() {
		return propertyPrice;
	}
	
	public int getSalePrice() {
		return salePrice;
	}
	
	public int getNumPlayersBankrupt() {
		return numPlayersBankrupt;
	}

	public int getSelectType() {
		return selectType;
	}

	public void setSelectType(int selectType) {
		this.selectType = selectType;
		gameView.showSelectMessage(selectType);
	}

	@Override
	public String toString() {
		return "Controller [board=" + board + ", gameView=" + gameView + ", numPlayers=" + numPlayers
				+ ", numPlayersBankrupt=" + numPlayersBankrupt + ", numPlayersFold=" + numPlayersFold
				+ ", numPropertiesOwned=" + numPropertiesOwned + ", turn=" + turn + ", bidTurn=" + bidTurn
				+ ", propertyPrice=" + propertyPrice + ", bidPrice=" + bidPrice + ", isGameOver=" + isGameOver
				+ ", isAuctionActive=" + isAuctionActive + ", curPlayer=" + curPlayer + ", curAuctionPlayer="
				+ curAuctionPlayer + ", selectedSquare=" + selectedSquare + ", observer=" + observer + ", afkTimer="
				+ afkTimer + "]";
	}

	public boolean repOk() {
		if (numPlayers > 8 || numPlayers < 2)
			return false;
		
		if (board == null)
			return false;
		
		if (turn > 7 || turn < 0)
			return false;
		
		if (curPlayer == null)
			return false;
		
		return true;
	}

}
