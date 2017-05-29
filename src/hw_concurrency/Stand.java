package hw_concurrency;

public class Stand {
	
	private School school;
	private String standName, service;
	private double price, totalRevenue;
	private int items, itemsSold;
	
	private final int MAX_ITEM = 40;
	private final int ITEMS_EACH_LOAD = 10;

	public Stand(String standName, String service, double price) {
		this.standName = standName;
		this.service = service;
		this.price = price;
	}

	public double getTotalRevenue() {
		return totalRevenue;
	}

	public void setSchool(School school) {
		this.school = school;
	}

	public void loadItemPack() {
		if (items <= MAX_ITEM) {
			items += ITEMS_EACH_LOAD;
		}
	}
	
	public synchronized int sellAndReturnItems(int itemsStudentWants) {
		if (items >= itemsStudentWants) {
			items -= itemsStudentWants;
			sellItem(itemsStudentWants);
			return itemsStudentWants;
		} else {
			items = 0;
			sellItem(items);
			return items;
		}
	}
	
	private void sellItem(int items) {
		EntityFactory.addItemsSold(items);
		EntityFactory.addTotalRevenue(items * price);
		itemsSold += items;
		totalRevenue += items * price;
	}
	
	public void printStatus() {
		if (items >= 40) { 
			System.out.println(standName + " is full now");
		} else if (items == 0) {
			System.out.println(standName + " is empty now");
		}
	}
	
	public void win() {
		System.out.println(standName + " of " + school.getName() + " won");
		System.out.println(standName + " - " + service + " with price " + price);
		System.out.println(standName + " earned " + totalRevenue + " with " + itemsSold + " items");
	}
	
}