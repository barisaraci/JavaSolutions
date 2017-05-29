package hw_JSON;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.Collator;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class Main {

	private ArrayList<Course> courses = new ArrayList<Course>();
	private ArrayList<Student> students = new ArrayList<Student>();

	private ArrayList<String> instructors = new ArrayList<String>();
	private ArrayList<String> rooms = new ArrayList<String>();
	private ArrayList<String> subjectNames = new ArrayList<String>();
	private ArrayList<String> coursesNos = new ArrayList<String>();

	private final String COMMAND_LIST = "list";
	private final String COMMAND_SEARCH = "search";
	private final String COMMAND_EXIT = "exit";
	private final String COMMAND_ADD_NEW_COURSE = "add new course";
	private final String COMMAND_ADD_NEW_STUDENT = "add new student";
	private final String COMMAND_SHOW = "show";
	
	private final String ERROR_OVERLAP = "There is another course in your program, overlapping with this one";
	private final String INVALID_COURSE = "Could not found course";
	private final String INVALID_STUDENT = "Could not found student";
	private final String INVALID_COMMAND = "Invalid command";
	private final String FILE_NAME = "CoursesOffered.json";

	private final String ARGUMENT_INSTRUCTORS = "instructors";
	private final String ARGUMENT_ROOMS = "rooms";
	private final String ARGUMENT_SUBJECT_NAMES = "subject names";
	private final String ARGUMENT_COURSES_NOS = "courses nos";

	private final String JSON_ARGUMENT_COURSE = "Course";
	private final String JSON_ARGUMENT_SUBJECT_NAME = "SubjectName";
	private final String JSON_ARGUMENT_COURSE_NO = "CourseNo";
	private final String JSON_ARGUMENT_SECTION_NO = "SectionNo";
	private final String JSON_ARGUMENT_INSTRUCTORS = "Instructors";
	private final String JSON_ARGUMENT_NAME = "Name";
	private final String JSON_ARGUMENT_SURNAME = "Surname";
	private final String JSON_ARGUMENT_SCHEDULE = "Schedule";
	private final String JSON_ARGUMENT_MEETING_TIME = "MeetingTime";
	private final String JSON_ARGUMENT_START_DATE = "StartDate";
	private final String JSON_ARGUMENT_FINISH_DATE = "FinishDate";
	private final String JSON_ARGUMENT_ROOM = "Room";
	private final String JSON_ARGUMENT_DAY = "Day";
	private final String JSON_ARGUMENT_START_HOUR = "StartHour";
	private final String JSON_ARGUMENT_FINISH_HOUR = "FinishHour";
	private final String JSON_ARGUMENT_ROOM_CODE = "RoomCode";

	private final String COMMAND_LINE_INTERFACE = "Course Query System" + System.lineSeparator() + "Commands: "
			+ System.lineSeparator()
			+ "list <item name> (instructors, rooms, subject names, courses nos) (ex: \"list subject names\")"
			+ System.lineSeparator()
			+ "search <1/2/3/4/5/6> [value] (room, day, instructor, course no, subject name, courses that start in the morning)"
			+ " (ex: \"search 1 CK:EF: 237\", \"search 6\")" + System.lineSeparator() + "add new course"
			+ System.lineSeparator() + "add new student" + System.lineSeparator()
			+ "show <student id> (ex : \"show S001234\")" + System.lineSeparator() + "exit";
	
	public static void main(String[] args) {
		Main courseQuerySystem = new Main();
		courseQuerySystem.run();
	}

	private void run() {
		prepareLists();
		sortLists();
		checkCommands();
	}

	private void prepareLists() {
		try {
			FileReader reader = new FileReader(FILE_NAME);
			JSONArray allData = (JSONArray) JSONValue.parse(reader);

			for (int i = 0; i < allData.size(); i++) {
				JSONObject index = (JSONObject) allData.get(i);
				JSONObject courseJSON = (JSONObject) index.get(JSON_ARGUMENT_COURSE);
				Course course = new Course();

				addMainInfo(courseJSON, course);
				addInstructors(courseJSON, course);
				addSchedule(courseJSON, course);

				courses.add(course);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void addMainInfo(JSONObject courseJSON, Course course) {
		String subjectName = courseJSON.get(JSON_ARGUMENT_SUBJECT_NAME).toString();
		String courseNo = courseJSON.get(JSON_ARGUMENT_COURSE_NO).toString();
		String sectionNo = courseJSON.get(JSON_ARGUMENT_SECTION_NO).toString();

		if (!isListContains(subjectNames, subjectName))
			subjectNames.add(subjectName);

		if (!isListContains(coursesNos, courseNo))
			coursesNos.add(courseNo);

		course.setSubjectName(subjectName);
		course.setCourseNo(courseNo);
		course.setSectionNo(sectionNo);
	}

	private void addInstructors(JSONObject courseJSON, Course course) {
		JSONArray instructorArray = (JSONArray) courseJSON.get(JSON_ARGUMENT_INSTRUCTORS);

		for (int i = 0; i < instructorArray.size(); i++) {
			JSONObject instructor = (JSONObject) instructorArray.get(i);
			String instructorName = instructor.get(JSON_ARGUMENT_NAME).toString() + " "
					+ instructor.get(JSON_ARGUMENT_SURNAME).toString();

			course.addInstructor(instructorName);

			if (!isListContains(instructors, instructorName))
				instructors.add(instructorName);
		}
	}

	private void addSchedule(JSONObject courseJSON, Course course) {
		JSONArray scheduleArray = (JSONArray) courseJSON.get(JSON_ARGUMENT_SCHEDULE);

		for (int i = 0; i < scheduleArray.size(); i++) {
			JSONObject scheduleJSON = (JSONObject) scheduleArray.get(i);
			JSONArray meetingTimes = (JSONArray) scheduleJSON.get(JSON_ARGUMENT_MEETING_TIME);

			Schedule schedule = new Schedule();
			Date startDate = new Date(Long.parseLong(scheduleJSON.get(JSON_ARGUMENT_START_DATE).toString().substring(6,
					scheduleJSON.get(JSON_ARGUMENT_START_DATE).toString().length() - 2))); // substring removes the string part of the date
			Date finishDate = new Date(Long.parseLong(scheduleJSON.get(JSON_ARGUMENT_FINISH_DATE).toString().substring(6,
					scheduleJSON.get(JSON_ARGUMENT_FINISH_DATE).toString().length() - 2))); // substring removes the string part of the date
			schedule.setStartDate(startDate);
			schedule.setFinishDate(finishDate);

			addMeetingTimes(schedule, meetingTimes);

			course.addSchedule(schedule);
		}
	}

	private void addMeetingTimes(Schedule schedule, JSONArray meetingTimes) {
		for (int j = 0; j < meetingTimes.size(); j++) {
			JSONObject meetingTimeJSON = (JSONObject) meetingTimes.get(j);
			JSONArray roomArray = (JSONArray) meetingTimeJSON.get(JSON_ARGUMENT_ROOM);

			MeetingTime meetingTime = new MeetingTime();
			meetingTime.setDay(meetingTimeJSON.get(JSON_ARGUMENT_DAY).toString());
			meetingTime.setStartHour(meetingTimeJSON.get(JSON_ARGUMENT_START_HOUR).toString());
			meetingTime.setFinishHour(meetingTimeJSON.get(JSON_ARGUMENT_FINISH_HOUR).toString());

			addRooms(roomArray, meetingTime);

			schedule.addMeetingTime(meetingTime);
		}
	}

	private void addRooms(JSONArray roomArray, MeetingTime meetingTime) {
		for (int k = 0; k < roomArray.size(); k++) {
			JSONObject room = (JSONObject) roomArray.get(k);
			String roomName = room.get(JSON_ARGUMENT_ROOM_CODE).toString();

			meetingTime.addRoom(roomName);

			if (!isListContains(rooms, roomName))
				rooms.add(roomName);
		}
	}

	private boolean isListContains(ArrayList<String> arrayList, String data) {
		for (String listItem : arrayList) {
			if (listItem.equals(data))
				return true;
		}

		return false;
	}

	private void sortLists() {
		Collator collator = Collator.getInstance(Locale.US);
		collator.setStrength(Collator.PRIMARY);
		Collections.sort(instructors, collator);
		Collections.sort(rooms, collator);
		Collections.sort(subjectNames, collator);
		Collections.sort(coursesNos, collator);
	}

	private void checkCommands() {
		Scanner scanner = new Scanner(System.in);

		while (true) {
			System.out.println(COMMAND_LINE_INTERFACE);

			String command = scanner.nextLine();
			String commandArguments[] = command.split(" ");

			if (commandArguments[0].equals(COMMAND_LIST)) {
				String commandArgument = command.substring("list ".length(), command.length());
				list(commandArgument);
			} else if (commandArguments[0].equals(COMMAND_SEARCH)) {
				String commandArgument = command.substring("search ".length(), command.length());
				int condition = Integer.parseInt(commandArgument.substring(0, 1));
				String value = condition != 6 ? commandArgument.substring(2, commandArgument.length()) : null;
				search(condition, value);
			} else if (command.equals(COMMAND_ADD_NEW_COURSE)) {
				addNewCourse(scanner);
			} else if (command.equals(COMMAND_ADD_NEW_STUDENT)) {
				addNewStudent(scanner);
			} else if (commandArguments[0].equals(COMMAND_SHOW)) {
				String studentId = command.substring("show ".length(), command.length());
				show(studentId);
			} else if (command.equals(COMMAND_EXIT)) {
				break;
			} else {
				System.out.println(INVALID_COMMAND);
			}
		}

		scanner.close();
	}

	private void list(String commandArgument) {
		if (commandArgument.equals(ARGUMENT_INSTRUCTORS)) {
			list(instructors);
		} else if (commandArgument.equals(ARGUMENT_ROOMS)) {
			list(rooms);
		} else if (commandArgument.equals(ARGUMENT_SUBJECT_NAMES)) {
			list(subjectNames);
		} else if (commandArgument.equals(ARGUMENT_COURSES_NOS)) {
			list(coursesNos);
		} else {
			System.out.println(INVALID_COMMAND);
		}
	}

	private void list(ArrayList<String> list) {
		for (String listItem : list) {
			System.out.println(listItem);
		}
	}

	private void search(int condition, String value) {
		switch (condition) {
		case 1:
			queryRoom(value);
			break;
		case 2:
			queryDay(value);
			break;
		case 3:
			queryInstructor(value);
			break;
		case 4:
			queryCourseNo(value);
			break;
		case 5:
			querySubjectName(value);
			break;
		case 6:
			queryCoursesThatStartInTheMorning();
			break;
		default:
			System.out.println(INVALID_COMMAND);
			break;
		}
	}

	private void queryRoom(String value) {
		for (Course course : courses) {
			for (Schedule schedule : course.getSchedule()) {
				for (MeetingTime meetingTime : schedule.getMeetingTimes()) {
					for (String room : meetingTime.getRoomCodes()) {
						if (room.equals(value)) {
							course.print();
						}
					}
				}
			}
		}
	}

	private void queryDay(String value) {
		for (Course course : courses) {
			for (Schedule schedule : course.getSchedule()) {
				for (MeetingTime meetingTime : schedule.getMeetingTimes()) {
					if (meetingTime.getDay().equals(value)) {
						course.print();
					}
				}
			}
		}
	}

	private void queryInstructor(String value) {
		for (Course course : courses) {
			for (String instructor : course.getInstructors()) {
				if (instructor.equals(value)) {
					course.print();
				}
			}
		}
	}

	private void queryCourseNo(String value) {
		for (Course course : courses) {
			if (course.getCourseNo().equals(value)) {
				course.print();
			}
		}
	}

	private void querySubjectName(String value) {
		for (Course course : courses) {
			if (course.getSubjectName().equals(value)) {
				course.print();
			}
		}
	}

	private void queryCoursesThatStartInTheMorning() {
		for (Course course : courses) {
			for (Schedule schedule : course.getSchedule()) {
				for (MeetingTime meetingTime : schedule.getMeetingTimes()) {
					if (meetingTime.isMorningCourse(meetingTime.getStartHour())) {
						course.print();
					}
				}
			}
		}
	}
	
	private void addNewCourse(Scanner scanner) {
		Course course = new Course();
		System.out.println("Enter subject name: ");
		course.setSubjectName(scanner.nextLine());
		
		System.out.println("Enter course no: ");
		course.setCourseNo(scanner.nextLine());
		
		System.out.println("Enter section no: ");
		course.setSectionNo(scanner.nextLine());
		
		addInstructorToCourse(scanner, course);
		addScheduleToCourse(scanner, course);
		
		courses.add(course);
	}
	
	private void addInstructorToCourse(Scanner scanner, Course course) {
		String instructorName;
		
		while (true) {
			System.out.println("Enter instructor: (-1 to exit)");
			instructorName = scanner.nextLine();
			if (instructorName.equals("-1")) {
				break;
			} else {
				course.addInstructor(instructorName);
			}
		}
	}
	
	private void addScheduleToCourse(Scanner scanner, Course course) {
		Schedule schedule = new Schedule();
		
		addDatesToSchedule(scanner, schedule);
		addMeetingTimesToSchedule(scanner, schedule);
		
		course.addSchedule(schedule);
	}
	
	private void addDatesToSchedule(Scanner scanner, Schedule schedule) {
		System.out.println("Enter start date: (ex: \"September 17 2012\"");
		DateFormat startDateFormat = new SimpleDateFormat("MMMM d yyyy", Locale.ENGLISH);
		Date startDate;
		try {
			startDate = startDateFormat.parse(scanner.nextLine());
			schedule.setStartDate(startDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		System.out.println("Enter finish date: (ex: \"December 28 2012\"");
		DateFormat finishDateFormat = new SimpleDateFormat("MMMM d yyyy", Locale.ENGLISH);
		Date finishDate;
		try {
			finishDate = finishDateFormat.parse(scanner.nextLine());
			schedule.setFinishDate(finishDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	private void addMeetingTimesToSchedule(Scanner scanner, Schedule schedule) {
		boolean isThereNewMeetingTime;
		while (true) {
			System.out.println("Enter \"1\" to add new meeting time, \"-1\" to exit: ");
			isThereNewMeetingTime = Integer.parseInt(scanner.nextLine()) == 1;
			if(isThereNewMeetingTime) {
				MeetingTime meetingTime = new MeetingTime();
				System.out.println("Enter day: ");
				meetingTime.setDay(scanner.nextLine());
				System.out.println("Enter start hour: ");
				meetingTime.setStartHour(scanner.nextLine());
				System.out.println("Enter finish hour: ");
				meetingTime.setFinishHour(scanner.nextLine());
				addRoomsToMeetingTime(scanner, meetingTime);
				
				schedule.addMeetingTime(meetingTime);
			} else {
				break;
			}
		}
	}
	
	private void addRoomsToMeetingTime(Scanner scanner, MeetingTime meetingTime) {
		String room;
		
		while (true) {
			System.out.println("Enter room: (-1 to exit)");
			room = scanner.nextLine();
			if (room.equals("-1")) {
				break;
			} else {
				meetingTime.addRoom(room);
			}
		}
	}
	
	private void addNewStudent(Scanner scanner) {
		Student student = new Student();
		System.out.println("Enter student full name: ");
		student.setFullName(scanner.nextLine());
		
		System.out.println("Enter student id: ");
		student.setId(scanner.nextLine());
		
		addCourseToStudent(scanner, student);
		students.add(student);
	}
	
	private void addCourseToStudent(Scanner scanner, Student student) {
		boolean isThereNewCourse;
		while (true) {
			System.out.println("Enter \"1\" to add new course, \"-1\" to exit: ");
			isThereNewCourse = Integer.parseInt(scanner.nextLine()) == 1;
			if (isThereNewCourse) {
				System.out.println("Enter subject name: ");
				String subjectName = scanner.nextLine();
				System.out.println("Enter course no: ");
				String courseNo = scanner.nextLine();
				System.out.println("Enter section no: ");
				String sectionNo = scanner.nextLine();
				tryToAddCourse(student, subjectName, courseNo, sectionNo);
 			} else {
				break;
			}
		}
	}
	
	private void tryToAddCourse(Student student, String subjectName, String courseNo, String sectionNo) {
		boolean isCourseFound = false;
		for (Course course : courses) {
			if (course.getSubjectName().equals(subjectName) && course.getCourseNo().equals(courseNo) && course.getSectionNo().equals(sectionNo)) {
				isCourseFound = true;
				if (student.isCourseAppropriate(course)) {
					student.addCourse(course);
					course.addStudent(student);
					break;
				} else {
					System.out.println(ERROR_OVERLAP);
				}
			}
		}
		if (!isCourseFound) {
			System.out.println(INVALID_COURSE);
		}
	}
	
	private void show(String studentId) {
		boolean isStudentFound = false;
		for (Student student : students) {
			if (student.getId().equalsIgnoreCase(studentId)) {
				isStudentFound = true;
				student.print();
				break;
			}
		}
		if (!isStudentFound) {
			System.out.println(INVALID_STUDENT);
		}
	}

}
