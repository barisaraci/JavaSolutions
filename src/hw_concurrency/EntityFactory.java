package hw_concurrency;

import java.util.ArrayList;

public class EntityFactory {
	
	private static ArrayList<Stand> stands = new ArrayList<Stand>();
	
	private static int itemsSold, students;
	private static double totalRevenue;
	
	public static School getSchool(String schoolName) {
		return new School(schoolName);
	}
	
	public static void createStand(String standName, String service, double price, School school) {
		Stand stand = new Stand(standName, service, price);
		stand.setSchool(school);
		stands.add(stand);
	}
	
	public static void createStudent() {
		Student student = new Student(stands);
		students++;
		student.start();
	}

	public static ArrayList<Stand> getStands() {
		return stands;
	}
	
	public static void addItemsSold(int items) {
		itemsSold += items;
	}
	
	public static void addTotalRevenue(double price) {
		totalRevenue += price;
	}
	
	public static void removeStudent() {
		students--;
	}
	
	public static void printStatus() {
		System.out.println(students + " students are in the festival");
		System.out.println(itemsSold + " items sold");
		System.out.println("Total revenue: " + totalRevenue);
	}

}
