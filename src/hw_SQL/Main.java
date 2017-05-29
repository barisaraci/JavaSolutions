package hw_SQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Main {
	
	private Connection conn;
	private Scanner scanner;
	
	private static final String DB_URL = "jdbc:mysql://localhost:3306/cs202?autoReconnect=true&useSSL=false";
	private static final String USER = "root";
	private static final String PASS = "";
	
	private static final String MENU = "1. Add instructor\n" +
									   "2. Remove instructor\n" +
									   "3. Add room\n" +
									   "4. Remove room\n" +
									   "5. Make reservation\n" +
									   "6. Cancel reservation\n" +
									   "7. List all reservations\n" +
									   "8. List all reservations of a room\n" +
									   "9. List reservations of an instructor\n" +
									   "10.List instructor of a particular reservation";
	
	public static void main(String[] args) {
		Main main = new Main();
		main.run();
	}
	
	private void run() {
		connect();
		scanner = new Scanner(System.in);
		
		while (true) {
			System.out.println(MENU);
			switch (Integer.parseInt(scanner.nextLine())) {
				case 1: addInstructor();
						break;
				case 2: removeInstructor();
						break;
				case 3: addRoom();
						break;
				case 4: removeRoom();
						break;
				case 5: makeReservation();
						break;
				case 6: cancelReservation();
						break;
				case 7: listReservations();
						break;
				case 8: listReservationsOfRoom();
						break;
				case 9: listReservationsOfInstructor();
						break;
				case 10: listInstructorOfReservation();
						break;
			}
		}
	}
	
	private void connect() {
		try {
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	private void addInstructor() {
		System.out.print("Enter instructor name: ");
		String instructorName = scanner.nextLine();
		
		try {
			Statement stmt = conn.createStatement();
			String sql = "INSERT INTO instructors (instructor_name) " + "VALUES ('" + instructorName + "')";
			stmt.executeUpdate(sql);
			System.out.println("An instructor with name " + instructorName + " has been added");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void removeInstructor() {
		System.out.print("Enter instructor name: ");
		String instructorName = scanner.nextLine();
		
		try {
			Statement stmt = conn.createStatement();
			String sql = "SELECT * FROM instructors WHERE instructor_name = '" + instructorName + "'";
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next()) {
				sql = "DELETE FROM instructors WHERE instructor_name = '" + instructorName + "'";
				stmt.executeUpdate(sql);
				System.out.println("The instructor has been deleted");
			} else {
				System.out.println("The instructor not found");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void addRoom() {
		System.out.print("Enter room name: ");
		String roomName = scanner.nextLine();
		
		try {
			Statement stmt = conn.createStatement();
			String sql = "INSERT INTO rooms (room_name) " + "VALUES ('" + roomName + "')";
			stmt.executeUpdate(sql);
			System.out.println("A room with number " + roomName + " has been added");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void removeRoom() {
		System.out.print("Enter room name: ");
		String roomName = scanner.nextLine();
		
		try {
			Statement stmt = conn.createStatement();
			String sql = "SELECT * FROM rooms WHERE room_name = '" + roomName + "'";
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next()) {
				sql = "DELETE FROM rooms WHERE room_name = '" + roomName + "'";
				stmt.executeUpdate(sql);
				System.out.println("The room has been deleted");
			} else {
				System.out.println("The room not found");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void makeReservation() {
		System.out.print("Enter instructor name: ");
		String instructorName = scanner.nextLine();
		
		System.out.print("Enter room name: ");
		String roomName = scanner.nextLine();
		
		System.out.print("Enter reservation day: ");
		String reservationDay = scanner.nextLine();
		
		int startHour, endHour;
		
		do {
			System.out.print("Enter start hour (8-17): ");
			startHour = Integer.parseInt(scanner.nextLine());
		} while (startHour < 8 || startHour > 17);

		do {
			System.out.print("Enter end hour (8-17): ");
			endHour = Integer.parseInt(scanner.nextLine());
		} while (endHour < 8 || endHour > 17 || endHour <= startHour);

		try {
			Statement stmt = conn.createStatement();
			String sql = "INSERT INTO reservations (reservation_instructor_id, reservation_room_id, reservation_day, reservation_start, reservation_end) " +
					   	 "SELECT instructor_id, room_id, '" + reservationDay + "', " + startHour + ", " + endHour + " FROM instructors i, rooms r " +
					   	 "WHERE i.instructor_name = '" + instructorName + "' AND r.room_name = '" + roomName + "'";
			stmt.executeUpdate(sql);
			System.out.println("A reservation has been added");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void cancelReservation() {
		System.out.print("Enter instructor name: ");
		String instructorName = scanner.nextLine();
		
		System.out.print("Enter room name: ");
		String roomName = scanner.nextLine();
		
		System.out.print("Enter reservation day: ");
		String reservationDay = scanner.nextLine();
		
		int startHour, endHour;
		
		do {
			System.out.print("Enter start hour (8-17): ");
			startHour = Integer.parseInt(scanner.nextLine());
		} while (startHour < 8 || startHour > 17);

		do {
			System.out.print("Enter end hour (8-17): ");
			endHour = Integer.parseInt(scanner.nextLine());
		} while (endHour < 8 || endHour > 17 || endHour <= startHour);
		
		try {
			Statement stmt = conn.createStatement();
			String sql = "SELECT * FROM reservations WHERE reservation_day = '" + reservationDay + "' AND reservation_start = " + startHour +
					 	 " AND reservation_end = " + endHour + " AND reservation_id IN (SELECT reservation_id FROM instructors i, rooms r " +
					 	 "WHERE " + "i.instructor_name = '" + instructorName + "' AND r.room_name = '" + roomName + "')";
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next()) {
				sql = "DELETE FROM reservations WHERE reservation_day = '" + reservationDay + "' AND reservation_start = " + startHour +
					  " AND reservation_end = " + endHour + " AND reservation_id IN (SELECT reservation_id FROM instructors i, rooms r " +
					  "WHERE " + "i.instructor_name = '" + instructorName + "' AND r.room_name = '" + roomName + "')";
				stmt.executeUpdate(sql);
				System.out.println("The reservation has been deleted");
			} else {
				System.out.println("The reservation not found");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void listReservations() {
		try {
			Statement stmt = conn.createStatement();
			String sql = "SELECT instructor_name, room_name, reservation_day, reservation_start, reservation_end " +
						 "FROM instructors i, rooms r, reservations re WHERE re.reservation_instructor_id = i.instructor_id AND " +
						 "re.reservation_room_id = r.room_id";
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next()) {
				printReservations(rs);
			} else {
				System.out.println("Reservation not found");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void listReservationsOfRoom() {
		System.out.print("Enter room name: ");
		String roomName = scanner.nextLine();
		
		try {
			Statement stmt = conn.createStatement();
			String sql = "SELECT instructor_name, room_name, reservation_day, reservation_start, reservation_end " +
						 "FROM instructors i, rooms r, reservations re WHERE re.reservation_instructor_id = i.instructor_id AND " +
						 "re.reservation_room_id = r.room_id AND r.room_name = '" + roomName + "'";
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next()) {
				printReservations(rs);
			} else {
				System.out.println("Reservation not found");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void listReservationsOfInstructor() {
		System.out.print("Enter instructor name: ");
		String instructorName = scanner.nextLine();
		
		try {
			Statement stmt = conn.createStatement();
			String sql = "SELECT instructor_name, room_name, reservation_day, reservation_start, reservation_end " +
						 "FROM instructors i, rooms r, reservations re WHERE re.reservation_instructor_id = i.instructor_id AND " +
						 "re.reservation_room_id = r.room_id AND i.instructor_name = '" + instructorName + "'";
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next()) {
				printReservations(rs);
			} else {
				System.out.println("Reservation not found");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void listInstructorOfReservation() {
		System.out.print("Enter room name: ");
		String roomName = scanner.nextLine();
		
		System.out.print("Enter reservation day: ");
		String reservationDay = scanner.nextLine();
		
		int startHour, endHour;
		
		do {
			System.out.print("Enter start hour (8-17): ");
			startHour = Integer.parseInt(scanner.nextLine());
		} while (startHour < 8 || startHour > 17);

		do {
			System.out.print("Enter end hour (8-17): ");
			endHour = Integer.parseInt(scanner.nextLine());
		} while (endHour < 8 || endHour > 17 || endHour <= startHour);
		
		
		try {
			Statement stmt = conn.createStatement();
			String sql = "SELECT instructor_name " +
						 "FROM instructors i, rooms r, reservations re WHERE re.reservation_instructor_id = i.instructor_id AND " +
						 "re.reservation_room_id = r.room_id AND r.room_name = '" + roomName + "' AND re.reservation_day = '" +
						 reservationDay + "' AND re.reservation_start = " + startHour + " AND re.reservation_end = " + endHour;
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next()) {
				System.out.print("Instructor name: " + rs.getString("instructor_name"));
			} else {
				System.out.println("Instructor not found");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void printReservations(ResultSet rs) throws SQLException {
		while (rs.next()) {
			System.out.print("Instructor name: " + rs.getString("instructor_name"));
			System.out.print(", Room name: " + rs.getString("room_name"));
			System.out.print(", Reservation Day: " + rs.getString("reservation_day"));
			System.out.print(", Start Time: " + rs.getString("reservation_start"));
			System.out.print(", End Time: " + rs.getString("reservation_end") + "\n");
		}
	}
	
}
