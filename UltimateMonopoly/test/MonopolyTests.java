package monopoly.test;

import static org.junit.Assert.*;

import org.junit.Test;

import monopoly.domains.Color;
import monopoly.domains.Controller;
import monopoly.domains.entities.Board;
import monopoly.domains.entities.Player;
import monopoly.domains.entities.RegularDie;
import monopoly.domains.squares.CardSquare;
import monopoly.domains.squares.GameSquare;
import monopoly.domains.squares.PropertySquare;
import monopoly.domains.squares.SpecialSquare;
import monopoly.domains.squares.TaxSquare;
import monopoly.domains.squares.TransportSquare;
import monopoly.ui.BotUI;
import monopoly.ui.GameView;

public class MonopolyTests {
	
	@Test
	public void repOkController() {
		Controller controller = new Controller(2);
		BotUI botUI = new BotUI();
		new GameView(controller, botUI);
		
		assertTrue(controller.repOk());
	}
	
	@Test
	public void repOkBoard() {
		Controller controller = new Controller(2);
		BotUI botUI = new BotUI();
		new GameView(controller, botUI);
		
		Board board = controller.getBoard();
		
		assertTrue(board.repOk());
	}
	
	@Test
	public void repOkPlayer() {
		Controller controller = new Controller(2);
		BotUI botUI = new BotUI();
		new GameView(controller, botUI);
		
		Player player = controller.getCurPlayer();
		
		assertTrue(player.repOk());
	}
	
	@Test
	public void repOkSquare() {
		Controller controller = new Controller(2);
		BotUI botUI = new BotUI();
		new GameView(controller, botUI);
		
		GameSquare square = new GameSquare("test", controller, 1, 1);
		
		assertTrue(square.repOk());
	}
	
	@Test
	public void repOkColor() {
		Color color = new Color(5, 10, 15);
		
		assertTrue(color.repOk());
	}
	
	@Test
    public void testPlayerAssets() {
		Controller controller = new Controller(2);
		BotUI botUI = new BotUI();
		new GameView(controller, botUI);
		
		Player player = controller.getBoard().getPlayer(0);
		
        assertEquals(player.getMoney(), 3200);
    }
	
	@Test
    public void testPlayerMove() {
		Controller controller = new Controller(2);
		BotUI botUI = new BotUI();
		new GameView(controller, botUI);
        
		Player player = controller.getBoard().getPlayer(0);
        
        int pos1 = player.getPos();
        player.move(1);
        int pos2 = player.getPos();
        
        assertNotEquals(pos1, pos2);
    }
	
	@Test
    public void testPlayerMoveTo() {
		Controller controller = new Controller(2);
		BotUI botUI = new BotUI();
		new GameView(controller, botUI);
        
		Player player = controller.getBoard().getPlayer(0);
        
        int floor1 = player.getFloor();
        player.moveTo(2, 1);
        int floor2 = player.getFloor();
        
        assertNotEquals(floor1, floor2);
    }
	
	@Test
    public void testPlayerConsume() {
		Controller controller = new Controller(2);
		BotUI botUI = new BotUI();
		new GameView(controller, botUI);
        
		Player player = controller.getBoard().getPlayer(0);
        
        int money1 = player.getMoney();
        player.tryConsume(100);
        int money2 = player.getMoney() + 100;
        
        assertEquals(money1, money2);
    }
	
	@Test
    public void testPlayerBankrupt() {
		Controller controller = new Controller(1);
		BotUI botUI = new BotUI();
		new GameView(controller, botUI);
        
		Player player = controller.getBoard().getPlayer(0);
        
		boolean isBankrupt1 = player.isBankrupt();
        player.tryConsume(999999);
        boolean isBankrupt2 = player.isBankrupt();
        
        assertNotEquals(isBankrupt1, isBankrupt2);
    }
	
	@Test
	public void testPlayerInitPos() {
		Controller controller = new Controller(3);
		BotUI botUI = new BotUI();
		new GameView(controller, botUI);
		
		Player player = controller.getCurPlayer();
		assertTrue(player.getFloor() == 1 && player.getPos() == 0);
	}
	
	@Test
	public void testDie() {
		RegularDie die = new RegularDie();
		die.roll();
		
		int dieValue = die.getFaceValue();
		
		assertTrue(dieValue >= 0);
		assertTrue(dieValue <= 6);
	}
	
	@Test
	public void testBoardPlayerInit() {
		Controller controller = new Controller(3);
		BotUI botUI = new BotUI();
		new GameView(controller, botUI);
		
		assertEquals(controller.getNumPlayers(), 3);
	}
	
	@Test
	public void testBoardDice() {
		Controller controller = new Controller(2);
		Board board = controller.getBoard();
		BotUI botUI = new BotUI();
		new GameView(controller, botUI);
		
		board.rollTheDice();
		int diceValue = board.getTotalValue();
		assertTrue(diceValue >= 0);
		assertTrue(diceValue <= 15);
	}
	
	@Test
	public void tesControllerGiveTurn() {
		Controller controller = new Controller(2);
		BotUI botUI = new BotUI();
		new GameView(controller, botUI);
		
		Player p1 = controller.getCurPlayer();
		controller.giveTurnToNextPlayer();
		Player p2 = controller.getCurPlayer();
		
		assertNotEquals(p1.getId(), p2.getId());
	}
	
	@Test
	public void tesControllerGiveBidTurn() {
		Controller controller = new Controller(2);
		BotUI botUI = new BotUI();
		new GameView(controller, botUI);
		
		int p1 = controller.getBidTurn();
		controller.giveBidTurnToNextPlayer();
		int p2 = controller.getBidTurn();
		
		assertNotEquals(p1, p2);
	}
	
	@Test
	public void tesControllerPlayerBankrupt() {
		Controller controller = new Controller(3);
		BotUI botUI = new BotUI();
		new GameView(controller, botUI);
		
		int p1 = controller.getNumPlayersBankrupt();
		controller.playerBankrupt();
		int p2 = controller.getNumPlayersBankrupt();
		
		assertNotEquals(p1, p2);
	}
	
	@Test
	public void testSquarePos() {
		Controller controller = new Controller(3);
		BotUI botUI = new BotUI();
		new GameView(controller, botUI);
		
		GameSquare square = controller.getBoard().getSquare(2, 3);
		assertTrue(square.getFloor() == 2 && square.getPos() == 3);
	}
	
	@Test
	public void testSquareColor1() {
		Controller controller = new Controller(3);
		BotUI botUI = new BotUI();
		new GameView(controller, botUI);
		
		GameSquare square = controller.getBoard().getSquare(0, 0);
		assertTrue(square.getColor().getR() == 213);
	}
	
	@Test
	public void testSquareColor2() {
		Controller controller = new Controller(3);
		BotUI botUI = new BotUI();
		new GameView(controller, botUI);
		
		GameSquare square = controller.getBoard().getSquare(0, 2);
		assertTrue(square.getColor().getB() == 127);
	}
	
	@Test
	public void testPropertyPrice() {
		Controller controller = new Controller(3);
		BotUI botUI = new BotUI();
		new GameView(controller, botUI);
		
		PropertySquare property = (PropertySquare) controller.getBoard().getSquare(0, 2);
		assertTrue(property.getPrice() == 150);
	}
	
	@Test
	public void testPropertyMortgage() {
		Controller controller = new Controller(3);
		BotUI botUI = new BotUI();
		new GameView(controller, botUI);
		
		PropertySquare property = (PropertySquare) controller.getBoard().getSquare(0, 2);
		assertTrue(property.getMortgagePrice() == 70);
	}
	
	@Test
	public void testPropertyUpgrade() {
		Controller controller = new Controller(3);
		BotUI botUI = new BotUI();
		new GameView(controller, botUI);
		
		PropertySquare property = (PropertySquare) controller.getBoard().getSquare(0, 2);
		assertTrue(property.getUpgradePrice() == 100);
	}
	
	@Test
	public void testPropertyOwner() {
		Controller controller = new Controller(3);
		BotUI botUI = new BotUI();
		new GameView(controller, botUI);
		
		PropertySquare property = (PropertySquare) controller.getBoard().getSquare(0, 2);
		assertTrue(property.getOwner() == -1);
	}
	
	@Test
	public void testPropertyUpgradeLevel() {
		Controller controller = new Controller(3);
		BotUI botUI = new BotUI();
		new GameView(controller, botUI);
		
		PropertySquare property = (PropertySquare) controller.getBoard().getSquare(0, 2);
		assertTrue(property.getUpgradeLevel() == 0);
	}
	
	@Test
	public void testSquareType1() {
		Controller controller = new Controller(3);
		BotUI botUI = new BotUI();
		new GameView(controller, botUI);
		
		GameSquare square = controller.getBoard().getSquare(0, 1);
		assertTrue(square instanceof SpecialSquare);
	}
	
	@Test
	public void testSquareType2() {
		Controller controller = new Controller(3);
		BotUI botUI = new BotUI();
		new GameView(controller, botUI);
		
		GameSquare square = controller.getBoard().getSquare(0, 4);
		assertTrue(square instanceof TaxSquare);
	}
	
	@Test
	public void testSquareType3() {
		Controller controller = new Controller(3);
		BotUI botUI = new BotUI();
		new GameView(controller, botUI);
		
		GameSquare square = controller.getBoard().getSquare(0, 7);
		assertTrue(square instanceof CardSquare);
	}
	
	@Test
	public void testSquareType4() {
		Controller controller = new Controller(3);
		BotUI botUI = new BotUI();
		new GameView(controller, botUI);
		
		GameSquare square = controller.getBoard().getSquare(0, 0);
		assertTrue(square instanceof TransportSquare);
	}
	
	@Test
	public void testSquareType5() {
		Controller controller = new Controller(3);
		BotUI botUI = new BotUI();
		new GameView(controller, botUI);
		
		GameSquare square = controller.getBoard().getSquare(0, 3);
		assertTrue(square instanceof PropertySquare);
	}
	
}
