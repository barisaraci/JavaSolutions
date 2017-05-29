package hw_concurrency;

import java.util.ArrayList;
import java.util.Random;

public class Student extends Thread {
	
	private ArrayList<Stand> stands;
	private int itemsBought;
	
	private final int STANDS_EACH_ITERATION = 3;
	private final int MAX_ITEM = 8;
	
	public Student(ArrayList<Stand> stands) {
		this.stands = stands;
	}
	
	public void run() {
		while (!isInterrupted()) {
			for (int i = 0; i < STANDS_EACH_ITERATION; i++) {
				visitStand(chooseRandomStand());
			}
		
			checkItemsBought();
		}
    }
	
	public Stand chooseRandomStand() {
		int numberOfStands = stands.size();
		Random random = new Random();
		int standNumber = random.nextInt(numberOfStands);
		return stands.get(standNumber);
	}
	
	private void visitStand(Stand stand) {
		Random random = new Random();
		int items = random.nextInt(4) + 2;
		int purchasedItems = stand.sellAndReturnItems(items);
		if (purchasedItems != -1) {
			itemsBought += purchasedItems;
		}
	}
	
	private void checkItemsBought() {
		if (itemsBought == 0) {
			interrupt();
			EntityFactory.removeStudent();
		} else if (itemsBought > MAX_ITEM) {
			EntityFactory.createStudent();
		}
		
		itemsBought = 0;
	}
	
}
