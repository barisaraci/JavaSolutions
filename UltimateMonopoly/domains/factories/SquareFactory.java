package monopoly.domains.factories;

import monopoly.MonopolyGame;
import monopoly.MonopolyGame.Type;
import monopoly.domains.Color;
import monopoly.domains.Controller;
import monopoly.domains.squares.CardSquare;
import monopoly.domains.squares.CompanySquare;
import monopoly.domains.squares.GameSquare;
import monopoly.domains.squares.PropertySquare;
import monopoly.domains.squares.SpecialSquare;
import monopoly.domains.squares.TaxSquare;
import monopoly.domains.squares.TransportSquare;

public class SquareFactory {
	
	private static SquareFactory instance;
	
	private SquareFactory() {
		
	}
	
	public static SquareFactory getInstance() {
		if (instance == null)
			instance = new SquareFactory();
		
		return instance;
	}
	
	public GameSquare getSquare(Type type, String name, int floor, int pos, Controller controller) {
		if (type == MonopolyGame.Type.Transport) {
			return new TransportSquare(name, controller, floor, pos);
		} else if (type == MonopolyGame.Type.Special) {
			return new SpecialSquare(name, controller, floor, pos);
		} else if (type == MonopolyGame.Type.Card) {
			return new CardSquare(name, controller, floor, pos);
		} else if (type == MonopolyGame.Type.Go) {
			return new GameSquare(name, controller, floor, pos);
		}
		
		return null;
	}
	
	public GameSquare getSquare(Type type, String name, int floor, int pos, int taxValue, Controller controller) {
		return new TaxSquare(name, controller, floor, pos, taxValue);
	}
	
	public GameSquare getSquare(String name, Color color, int floor, int pos, int prices[], int rents[], Controller controller) {
		return new PropertySquare(name, color, floor, pos, prices, rents, controller);
	}
	
	public GameSquare getSquare(String companyName, String name, int floor, int pos, int prices[], int rents[], Controller controller) {
		return new CompanySquare(companyName, name, floor, pos, prices, rents, controller);
	}

}
