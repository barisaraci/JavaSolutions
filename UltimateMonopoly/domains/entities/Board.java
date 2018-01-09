package monopoly.domains.entities;

import java.util.ArrayList;

import monopoly.MonopolyGame;
import monopoly.domains.Color;
import monopoly.domains.Controller;
import monopoly.domains.Die;
import monopoly.domains.PlayerObserver;
import monopoly.domains.factories.SquareFactory;
import monopoly.domains.squares.GameSquare;

public class Board {
	
	private Controller controller;
	private ArrayList<Player> players = new ArrayList<>();
	private ArrayList<GameSquare> squares0 = new ArrayList<>(); // floor 0
	private ArrayList<GameSquare> squares1 = new ArrayList<>(); // floor 1
	private ArrayList<GameSquare> squares2 = new ArrayList<>(); // floor 2
	private boolean isBoardReady;
	private int totalValue;
	
	private Die die1, die2, speedDie;
	
	public Board(Controller controller) {
		this.controller = controller;
		initSquares();
		initDice();
	}
	
	/** 
	 * @MODIFIES: player's money
	 * @EFFECTS: Initiates the players' assets.
	 */
	public void initPlayers() {
		for (int i = 0; i < controller.getNumPlayers(); i++) {
			Player p = new Player(i, controller);
			new PlayerObserver(p, MonopolyGame.getBotUI()).run();
			players.add(p);
		}
		controller.setCurPlayer(players.get(0));
		isBoardReady = true;
	}
	
	/** 
	 * @MODIFIES: totalValue
	 * @EFFECTS: Rolls the dice.
	 */
	public void rollTheDice() {
		die1.roll();
		die2.roll();
		int die1Value = die1.getFaceValue();
		int die2Value = die2.getFaceValue();
		if (die1Value == die2Value)
			controller.getCurPlayer().setDoubled(true);
		else
			controller.getCurPlayer().resetDoubles();
		speedDie.roll();
		int speedDieValue = speedDie.getFaceValue();
		totalValue = die1Value + die2Value;
		if (speedDieValue == 4 || speedDieValue == 5) { // mr. monopoly
			controller.getCurPlayer().setMrMonopoly(true);
		} else if (speedDieValue == 6) { // bus icon
			controller.getCurPlayer().setBusIcon(true);
		} else {
			totalValue += speedDieValue;
		}
		controller.print("player" + controller.getTurn() + " has rolled " + totalValue + "(" + die1Value + ", " + die2Value + ((speedDieValue >= 1 && speedDieValue <= 3) ? ", " + speedDieValue + ")" : (speedDieValue == 4 || speedDieValue == 5) ? ", mr. monopoly)" : ", bus icon)"));
		controller.getCurPlayer().move(totalValue);
	}
	
	public GameSquare getSquare(int floor, int pos) {
		if (floor == 0 && pos < squares0.size())
			return squares0.get(pos);
		else if (floor == 1 && pos < squares1.size())
			return squares1.get(pos);
		else if (floor == 2 && pos < squares2.size())
			return squares2.get(pos);
		
		return null;
	}
	
	public ArrayList<GameSquare> getSquares(int floor) {
		if (floor == 0) 
			return squares0;
		else if (floor == 1) 
			return squares1;
		else if (floor == 2) 
			return squares2;
		
		return null;
	}
	
	private void initDice() {
		die1 = new RegularDie();
		die2 = new RegularDie();
		speedDie = new SpeedDie();
	}
	
	public Player getPlayer(int i) {
		return players.get(i);
	}
	
	public ArrayList<Player> getPlayers() {
		return players;
	}
	
	public boolean isBoardReady() {
		return isBoardReady;
	}

	public void setBoardReady(boolean isBoardReady) {
		this.isBoardReady = isBoardReady;
	}
	
	public int getTotalValue() {
		return totalValue;
	}

	private void initSquares() {
		SquareFactory factory = SquareFactory.getInstance();
		
		// floor 0
		GameSquare square0 = factory.getSquare(MonopolyGame.Type.Transport, "Holland Tunnel", 0, 0, controller);
		GameSquare square1 = factory.getSquare(MonopolyGame.Type.Special, "Auction", 0, 1, controller);
		GameSquare square2 = factory.getSquare("Katy Freeway", new Color(253, 228, 127), 0, 2, new int[] {150, 70, 100}, new int[] {11, 55, 160, 475, 650, 800, 1300}, controller);
		GameSquare square3 = factory.getSquare("Westheimer Rd.", new Color(253, 228, 127), 0, 3, new int[] {150, 70, 100}, new int[] {11, 55, 160, 475, 650, 800, 1300}, controller);
		GameSquare square4 = factory.getSquare(MonopolyGame.Type.Tax, "Internet Provider", 0, 4, 150, controller);
		GameSquare square5 = factory.getSquare("Kirby Drive", new Color(253, 228, 127), 0, 5, new int[] {180, 80, 100}, new int[] {14, 70, 200, 550, 750, 950, 1450}, controller);
		GameSquare square6 = factory.getSquare("Cullen Blvd.", new Color(253, 228, 127), 0, 6, new int[] {180, 80, 100}, new int[] {14, 70, 200, 550, 750, 950, 1450}, controller);
		GameSquare square7 = factory.getSquare(MonopolyGame.Type.Card, "Chance", 0, 7, controller);
		GameSquare square8 = factory.getSquare("Cab", "Black&White Cab", 0, 8, new int[] {300, 150}, new int[] {30, 60, 120, 240}, controller);
		GameSquare square9 = factory.getSquare("Dekalb Ave.", new Color(4, 126, 104), 0, 9, new int[] {210, 90, 100}, new int[] {17, 85, 240, 670, 840, 1025, 1525}, controller);
		GameSquare square10 = factory.getSquare(MonopolyGame.Type.Card, "Community Chest", 0, 10, controller);
		GameSquare square11 = factory.getSquare("Young Blvd.", new Color(4, 126, 104), 0, 11, new int[] {210, 90, 100}, new int[] {17, 85, 240, 670, 840, 1025, 1525}, controller);
		GameSquare square12 = factory.getSquare("Decatur St.", new Color(4, 126, 104), 0, 12, new int[] {240, 100, 100}, new int[] {20, 100, 300, 750, 925, 1100, 1600}, controller);
		GameSquare square13 = factory.getSquare("Peachtree St.", new Color(4, 126, 104), 0, 13, new int[] {240, 100, 100}, new int[] {20, 100, 300, 750, 925, 1100, 1600}, controller);
		GameSquare square14 = factory.getSquare(MonopolyGame.Type.Special, "Payday", 0, 14, controller);
		GameSquare square15 = factory.getSquare("Randolph St.", new Color(139, 0, 49), 0, 15, new int[] {270, 110, 150}, new int[] {23, 115, 345, 825, 1010, 1180, 2180}, controller);
		GameSquare square16 = factory.getSquare(MonopolyGame.Type.Card, "Chance", 0, 16, controller);
		GameSquare square17 = factory.getSquare("Lake Shore Dr.", new Color(139, 0, 49), 0, 17, new int[] {270, 110, 150}, new int[] {23, 115, 345, 825, 1010, 1180, 2180}, controller);
		GameSquare square18 = factory.getSquare("Wacker Dr.", new Color(139, 0, 49), 0, 18, new int[] {300, 120, 150}, new int[] {26, 130, 390, 900, 1100, 1275, 2275}, controller);
		GameSquare square19 = factory.getSquare("Michigan Ave.", new Color(139, 0, 49), 0, 19, new int[] {300, 120, 150}, new int[] {26, 130, 390, 900, 1100, 1275, 2275}, controller);
		GameSquare square20 = factory.getSquare("Cab", "Yellow Cab", 0, 20, new int[] {300, 150}, new int[] {30, 60, 120, 240}, controller);
		GameSquare square21 = factory.getSquare("Raildroad", "B&O Railroad", 0, 21, new int[] {200, 100}, new int[] {25, 50, 100, 200}, controller);
		GameSquare square22 = factory.getSquare(MonopolyGame.Type.Card, "Community Chest", 0, 22, controller);
		GameSquare square23 = factory.getSquare("South Temple", new Color(165, 141, 22), 0, 23, new int[] {330, 130, 200}, new int[] {32, 160, 470, 1050, 1250, 1500, 2500}, controller);
		GameSquare square24 = factory.getSquare("West Temple", new Color(165, 141, 22), 0, 24, new int[] {330, 130, 200}, new int[] {32, 160, 470, 1050, 1250, 1500, 2500}, controller);
		GameSquare square25 = factory.getSquare(MonopolyGame.Type.Tax, "Trash Collector", 0, 25, 150, controller);
		GameSquare square26 = factory.getSquare("North Temple", new Color(165, 141, 22), 0, 26, new int[] {360, 140, 200}, new int[] {38, 170, 520, 1125, 1425, 1275, 1600}, controller);
		GameSquare square27 = factory.getSquare("Temple Square", new Color(165, 141, 22), 0, 27, new int[] {360, 140, 200}, new int[] {38, 170, 520, 1125, 1425, 1275, 1600}, controller);
		GameSquare square28 = factory.getSquare(MonopolyGame.Type.Special, "Go to Jail", 0, 28, controller);
		GameSquare square29 = factory.getSquare("South Street", new Color(245, 186, 128), 0, 29, new int[] {390, 150, 250}, new int[] {45, 210, 575, 1300, 1600, 1800, 3300}, controller);
		GameSquare square30 = factory.getSquare("Broad Street", new Color(245, 186, 128), 0, 30, new int[] {390, 150, 250}, new int[] {45, 210, 575, 1300, 1600, 1800, 3300}, controller);
		GameSquare square31 = factory.getSquare("Walnut Street", new Color(245, 186, 128), 0, 31, new int[] {420, 160, 250}, new int[] {55, 225, 630, 1450, 1750, 2050, 3550}, controller);
		GameSquare square32 = factory.getSquare(MonopolyGame.Type.Card, "Community Chest", 0, 32, controller);
		GameSquare square33 = factory.getSquare("Market Street", new Color(245, 186, 128), 0, 33, new int[] {420, 160, 250}, new int[] {55, 225, 630, 1450, 1750, 2050, 3550}, controller);
		GameSquare square34 = factory.getSquare(MonopolyGame.Type.Card, "Bus Ticket", 0, 34, controller);
		GameSquare square35 = factory.getSquare(MonopolyGame.Type.Tax, "Sewage System", 0, 35, 150, controller);
		GameSquare square36 = factory.getSquare("Cab", "Ute Cab", 0, 36, new int[] {300, 150}, new int[] {30, 60, 120, 240}, controller);	
		GameSquare square37 = factory.getSquare(MonopolyGame.Type.Special, "Birthday Gift", 0, 37, controller);
		GameSquare square38 = factory.getSquare("Mulholland Blvd.", new Color(122, 5, 6), 0, 38, new int[] {450, 175, 300}, new int[] {70, 350, 750, 1600, 1850, 2100, 3600}, controller);
		GameSquare square39 = factory.getSquare("Ventura Blvd.", new Color(122, 5, 6), 0, 39, new int[] {480, 200, 300}, new int[] {80, 400, 825, 1800, 2175, 2550, 4050}, controller);
		GameSquare square40 = factory.getSquare(MonopolyGame.Type.Card, "Chance", 0, 40, controller);
		GameSquare square41 = factory.getSquare("Rodeo Dr.", new Color(122, 5, 6), 0, 41, new int[] {510, 250, 300}, new int[] {90, 450, 900, 2000, 2500, 3000, 4500}, controller);
		GameSquare square42 = factory.getSquare(MonopolyGame.Type.Transport, "Subway", 0, 42, controller);
		GameSquare square43 = factory.getSquare("Lake St.", new Color(250, 175, 169), 0, 43, new int[] {30, 15, 50}, new int[] {1, 5, 15, 45, 80, 125, 625}, controller);
		GameSquare square44 = factory.getSquare(MonopolyGame.Type.Card, "Community Chest", 0, 44, controller);
		GameSquare square45 = factory.getSquare("Nicollat Ave.", new Color(250, 175, 169), 0, 45, new int[] {30, 15, 50}, new int[] {1, 5, 15, 45, 80, 125, 625}, controller);
		GameSquare square46 = factory.getSquare("Hennepin Ave.", new Color(250, 175, 169), 0, 46, new int[] {60, 30, 50}, new int[] {3, 15, 45, 120, 240, 350, 850}, controller);
		GameSquare square47 = factory.getSquare(MonopolyGame.Type.Card, "Bus Ticket", 0, 47, controller);
		GameSquare square48 = factory.getSquare("Cab", "Checker Cab", 0, 48, new int[] {300, 150}, new int[] {30, 60, 120, 240}, controller);
		GameSquare square49 = factory.getSquare("Raildroad", "Read Railroad", 0, 49, new int[] {200, 100}, new int[] {25, 50, 100, 200}, controller);
		GameSquare square50 = factory.getSquare("The Esplanade", new Color(133, 254, 136), 0, 50, new int[] {90, 50, 50}, new int[] {5, 25, 80, 225, 360, 600, 1000}, controller);
		GameSquare square51 = factory.getSquare("Canal St.", new Color(133, 254, 136), 0, 51, new int[] {90, 50, 50}, new int[] {5, 25, 80, 225, 360, 600, 1000}, controller);
		GameSquare square52 = factory.getSquare(MonopolyGame.Type.Card, "Chance", 0, 52, controller);
		GameSquare square53 = factory.getSquare(MonopolyGame.Type.Tax, "Cable Company", 0, 53, 150, controller);
		GameSquare square54 = factory.getSquare("Magazine St.", new Color(133, 254, 136), 0, 54, new int[] {120, 60, 50}, new int[] {8, 40, 100, 300, 450, 600, 1100}, controller);
		GameSquare square55 = factory.getSquare("Bourbon St.", new Color(133, 254, 136), 0, 55, new int[] {120, 60, 50}, new int[] {8, 40, 100, 300, 450, 600, 1100}, controller);

		// floor 1
		GameSquare square56 = factory.getSquare(MonopolyGame.Type.Go, "GO", 1, 0, controller);
		GameSquare square57 = factory.getSquare("Mediter. Ave.", new Color(88, 12, 57), 1, 1, new int[] {60, 10, 30}, new int[]{2, 10, 30, 90, 160, 250, 750}, controller);
		GameSquare square58 = factory.getSquare(MonopolyGame.Type.Card, "Community Chest", 1, 2, controller);
		GameSquare square59 = factory.getSquare("Baltic Avenue", new Color(88,12,57),1,3,new int[] {60,10,30},new int[]{4,20,60,180,320,450,90}, controller);
		GameSquare square60 = factory.getSquare(MonopolyGame.Type.Tax,"Income Tax", 1,4,-1, controller); // income tax pay 10% or 200
		GameSquare square61 = factory.getSquare(MonopolyGame.Type.Transport,"Transit Station",1,5, controller);
		GameSquare square62 = factory.getSquare("Oriental Avenue", new Color(135,165,215),1,6,new int[] {100,50,50},new int[]{6,30,90,270,400,550,1050}, controller);
		GameSquare square63 = factory.getSquare(MonopolyGame.Type.Card,"Chance",1,7, controller);
		GameSquare square64 = factory.getSquare("Vermont Avenue", new Color(135,165,215),1,8,new int[] {100,50,50},new int[]{6,30,90,270,400,550,1050}, controller);
		GameSquare square65 = factory.getSquare("Connect. Ave.", new Color(135,165,215),1,9,new int[] {120,60,50},new int[]{8,40,100,300,450,600,1100}, controller);
		GameSquare square66 = factory.getSquare(MonopolyGame.Type.Special, "Jail",1,10, controller);
		GameSquare square67 = factory.getSquare("Charles Place", new Color(239,56,120),1,11,new int[] {140,70,100},new int[]{10,50,150,450,625,750,1250}, controller);
		GameSquare square68 = factory.getSquare(MonopolyGame.Type.Tax, "Electric Company",1,12,150, controller);
		GameSquare square69 = factory.getSquare("State Avenue", new Color(239,56,120),1,13,new int[] {140,70,100},new int[]{10,50,150,450,625,750,1250}, controller);
		GameSquare square70 = factory.getSquare("Virginia Avenue", new Color(239,56,120),1,14,new int[] {160,80,100},new int[]{12,60,180,500,700,900,1400}, controller);
		GameSquare square71 = factory.getSquare("Raildroad", "Sylvan Railroad", 1, 15, new int[] {200, 100}, new int[] {20, 40, 80, 160}, controller);
		GameSquare square72 = factory.getSquare("James Place", new Color(245,128,35),1,16,new int[] {180,90,100},new int[]{14,70,200,550,750,950,1450}, controller);
		GameSquare square73 = factory.getSquare(MonopolyGame.Type.Card, "Community Chest",1,17, controller);
		GameSquare square74 = factory.getSquare("Tennessee Ave.", new Color(245,128,35),1,18,new int[] {180,90,100},new int[]{14,70,200,550,750,950,1450}, controller);
		GameSquare square75 = factory.getSquare("New York Ave.", new Color(245,128,35),1,19,new int[] {200,100,100},new int[]{16,80,220,600,800,1000,1500}, controller);
		GameSquare square76 = factory.getSquare(MonopolyGame.Type.Special, "Free Parking",1,20, controller);
		GameSquare square77 = factory.getSquare("Kentucky Ave.", new Color(212,0,0),1,21,new int[] {220,100,150},new int[]{18,90,250,700,875,1050,2050}, controller);
		GameSquare square78 = factory.getSquare(MonopolyGame.Type.Card,"Chance",0,22, controller);
		GameSquare square79 = factory.getSquare("Indiana Avenue", new Color(212,0,0),1,23,new int[] {220,100,150},new int[]{18,90,250,700,875,1050,2050}, controller);
		GameSquare square80 = factory.getSquare("Illinois Avenue", new Color(212,0,0),1,24,new int[] {240,120,150},new int[]{20,100,300,750,925,1100,2100}, controller);
		GameSquare square81 = factory.getSquare(MonopolyGame.Type.Transport, "Transit Station",1,25, controller);
		GameSquare square82 = factory.getSquare("Atlantic Avenue", new Color(255,204,0),1,26,new int[] {260,130,150},new int[]{22,110,330,800,975,1150,2150}, controller);
		GameSquare square83 = factory.getSquare("Ventnor Avenue", new Color(255,204,0),1,27,new int[] {260,130,150},new int[]{22,110,330,800,975,1150,2150}, controller);
		GameSquare square84 = factory.getSquare(MonopolyGame.Type.Tax,"Water Works",1,28, 150, controller);
		GameSquare square85 = factory.getSquare("Marvin Gardens", new Color(255,204,0),1,29,new int[] {280,140,150},new int[]{24,120,350,850,1025,1200,2200}, controller);
		GameSquare square86 = factory.getSquare(MonopolyGame.Type.Special, "Roll Three",1,30, controller);
		GameSquare square87 = factory.getSquare("Pacific Avenue", new Color(8,118,45),1,31,new int[] {300,150,200},new int[]{26,130,390,900,1100,1275,2275}, controller);
		GameSquare square88 = factory.getSquare("North Car. Ave.", new Color(8,118,45),1,32,new int[] {300,150,200},new int[]{26,130,390,900,1100,1275,2275}, controller);
		GameSquare square89 = factory.getSquare(MonopolyGame.Type.Card, "Community Chest",1,33, controller);
		GameSquare square90 = factory.getSquare("Pennsyl. Ave.", new Color(8,118,45),1,34,new int[] {320,150,200},new int[]{28,150,450,1000,1200,1400,2400}, controller);
		GameSquare square91 = factory.getSquare("Raildroad", "Short Line", 1, 35, new int[] {200, 100}, new int[] {20, 40, 80, 160}, controller);
		GameSquare square92 = factory.getSquare(MonopolyGame.Type.Card,"Chance",1,36, controller);
		GameSquare square93 = factory.getSquare("Park Place", new Color(40,78,161),1,37,new int[] {350,200,200},new int[]{35,175,500,1100,1300,1500,2500}, controller);
		GameSquare square94 = factory.getSquare(MonopolyGame.Type.Tax,"Luxury Tax", 1, 38, 150, controller);
		GameSquare square95 = factory.getSquare("Boardwalk", new Color(40,78,161),1,39,new int[] {400,200,200},new int[]{50,200,600,1400,1700,2000,3000}, controller);

		// floor 2
		GameSquare square96 = factory.getSquare(MonopolyGame.Type.Transport, "Holland Tunnel", 2, 0, controller); // bonus, 300 on land, 250 on pass
		GameSquare square97 = factory.getSquare("Miami Avenue", new Color(170,68,0),2,1,new int[] {130,65,50},new int[]{9,45,120,350,500,700,1200}, controller);
		GameSquare square98 = factory.getSquare("Biscayne Avenue", new Color(170,68,0),2,2,new int[] {150,75,50},new int[]{11,55,160,475,650,800,1300}, controller);
		GameSquare square99 = factory.getSquare(MonopolyGame.Type.Transport,"Transit Station",2,3, controller);
		GameSquare square100 = factory.getSquare(MonopolyGame.Type.Special, "Reverse Direction",2,4, controller);
		GameSquare square101 = factory.getSquare("Lombard Street", new Color(255,255,255),2,5,new int[] {210,105,100},new int[]{17,85,240,475,670,1025,1525}, controller);
		GameSquare square102 = factory.getSquare(MonopolyGame.Type.Special, "Squeeze Play",2,6, controller);
		GameSquare square103 = factory.getSquare("Embarcadero", new Color(255,255,255),2,7,new int[] {210,105,100},new int[]{17,85,240,475,670,1025,1525}, controller);
		GameSquare square104 = factory.getSquare("Fisher's Wharf", new Color(255,255,255),2,8,new int[] {250,125,100},new int[]{21,105,320,780,950,1125,1625}, controller);
		GameSquare square105 = factory.getSquare(MonopolyGame.Type.Special,"Telephone Company",2,9,150, controller);
		GameSquare square106 = factory.getSquare(MonopolyGame.Type.Card, "Community Chest",2,10, controller);
		GameSquare square107 = factory.getSquare("Beacon Street", new Color(100,100,100),2,11,new int[] {330,165,200},new int[]{30,160,470,1050,1250,1500,2500}, controller);
		GameSquare square108 = factory.getSquare(MonopolyGame.Type.Special,"Bonus",2,12,-2, controller);
		GameSquare square109 = factory.getSquare("Boylston Street", new Color(100,100,100),2,13,new int[] {330,165,200},new int[]{30,160,470,1050,1250,1500,2500}, controller);
		GameSquare square110 = factory.getSquare("Newbury Street", new Color(100,100,100),2,14,new int[] {380,190,200},new int[]{40,185,550,1200,1500,1700,2700}, controller);
		GameSquare square111 = factory.getSquare(MonopolyGame.Type.Transport,"Transit Station",2,15, controller);
		GameSquare square112 = factory.getSquare("Fifth Avenue", new Color(188,188,188),2,16,new int[] {430,215,300},new int[]{60,220,650,1500,1800,2100,3600}, controller);
		GameSquare square113 = factory.getSquare("Madison Avenue", new Color(188,188,188),2,17,new int[] {430,215,300},new int[]{60,220,650,1500,1800,2100,3600}, controller);
		GameSquare square114 = factory.getSquare(MonopolyGame.Type.Special,"Stock Exchange",2,18, controller);
		GameSquare square115 = factory.getSquare("Wall Street", new Color(188,188,188),2,19,new int[] {500,250,300},new int[]{80,300,800,1800,2200,2700,4200}, controller);
		GameSquare square116 = factory.getSquare(MonopolyGame.Type.Tax,"Tax Refund", 2,20,-3, controller); // collect 50% from pool
		GameSquare square117 = factory.getSquare(MonopolyGame.Type.Special,"Bonus",2,21,150, controller);
		GameSquare square118 = factory.getSquare(MonopolyGame.Type.Card,"Chance",2,22, controller);
		GameSquare square119 = factory.getSquare("Florida Avenue", new Color(170,68,0),2,23,new int[] {130,65,50},new int[]{9,45,120,350,500,700,1200}, controller);

		squares0.add(square0);
		squares0.add(square1);
		squares0.add(square2);
		squares0.add(square3);
		squares0.add(square4);
		squares0.add(square5);
		squares0.add(square6);
		squares0.add(square7);
		squares0.add(square8);
		squares0.add(square9);
		squares0.add(square10);
		squares0.add(square11);
		squares0.add(square12);
		squares0.add(square13);
		squares0.add(square14);
		squares0.add(square15);
		squares0.add(square16);
		squares0.add(square17);
		squares0.add(square18);
		squares0.add(square19);
		squares0.add(square20);
		squares0.add(square21);
		squares0.add(square22);
		squares0.add(square23);
		squares0.add(square24);
		squares0.add(square25);
		squares0.add(square26);
		squares0.add(square27);
		squares0.add(square28);
		squares0.add(square29);
		squares0.add(square30);
		squares0.add(square31);
		squares0.add(square32);
		squares0.add(square33);
		squares0.add(square34);
		squares0.add(square35);
		squares0.add(square36);
		squares0.add(square37);
		squares0.add(square38);
		squares0.add(square39);
		squares0.add(square40);
		squares0.add(square41);
		squares0.add(square42);
		squares0.add(square43);
		squares0.add(square44);
		squares0.add(square45);
		squares0.add(square46);
		squares0.add(square47);
		squares0.add(square48);
		squares0.add(square49);
		squares0.add(square50);
		squares0.add(square51);
		squares0.add(square52);
		squares0.add(square53);
		squares0.add(square54);
		squares0.add(square55);
		
		squares1.add(square56);
		squares1.add(square57);
		squares1.add(square58);
		squares1.add(square59);
		squares1.add(square60);
		squares1.add(square61);
		squares1.add(square62);
		squares1.add(square63);
		squares1.add(square64);
		squares1.add(square65);
		squares1.add(square66);
		squares1.add(square67);
		squares1.add(square68);
		squares1.add(square69);
		squares1.add(square70);
		squares1.add(square71);
		squares1.add(square72);
		squares1.add(square73);
		squares1.add(square74);
		squares1.add(square75);
		squares1.add(square76);
		squares1.add(square77);
		squares1.add(square78);
		squares1.add(square79);
		squares1.add(square80);
		squares1.add(square81);
		squares1.add(square82);
		squares1.add(square83);
		squares1.add(square84);
		squares1.add(square85);
		squares1.add(square86);
		squares1.add(square87);
		squares1.add(square88);
		squares1.add(square89);
		squares1.add(square90);
		squares1.add(square91);
		squares1.add(square92);
		squares1.add(square93);
		squares1.add(square94);
		squares1.add(square95);
		
		squares2.add(square96);
		squares2.add(square97);
		squares2.add(square98);
		squares2.add(square99);
		squares2.add(square100);
		squares2.add(square101);
		squares2.add(square102);
		squares2.add(square103);
		squares2.add(square104);
		squares2.add(square105);
		squares2.add(square106);
		squares2.add(square107);
		squares2.add(square108);
		squares2.add(square109);
		squares2.add(square110);
		squares2.add(square111);
		squares2.add(square112);
		squares2.add(square113);
		squares2.add(square114);
		squares2.add(square115);
		squares2.add(square116);
		squares2.add(square117);
		squares2.add(square118);
		squares2.add(square119);
	}
	
	@Override
	public String toString() {
		return "Board [controller=" + controller + ", players=" + players + ", squares0=" + squares0 + ", squares1="
				+ squares1 + ", squares2=" + squares2 + ", isBoardReady=" + isBoardReady + ", totalValue=" + totalValue
				+ ", die1=" + die1 + ", die2=" + die2 + ", speedDie=" + speedDie + "]";
	}

	public boolean repOk() {
		if (players.size() != controller.getNumPlayers())
			return false;
		
		if (squares0.isEmpty() || squares1.isEmpty() || squares2.isEmpty())
			return false;
		
		return true;
	}

}
