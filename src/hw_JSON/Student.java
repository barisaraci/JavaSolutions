package hw_JSON;

import java.util.ArrayList;

public class Student {
	
	private String fullName, id;
	private ArrayList<Course> courses;
	
	public Student() {
		courses = new ArrayList<Course>();
	}
	
	public Student(String fullName, String id) {
		this.fullName = fullName;
		this.id = id;
		courses = new ArrayList<Course>();
	}
	
	public void print() {
		System.out.println("Student name: " + fullName);
		System.out.println("~~~~~~~~~~MONDAY~~~~~~~~~~" + System.lineSeparator());
		printCoursesOfDay("monday");
		System.out.println("~~~~~~~~~~TUESDAY~~~~~~~~~~" + System.lineSeparator());
		printCoursesOfDay("tuesday");
		System.out.println("~~~~~~~~~~WEDNESDAY~~~~~~~~~~" + System.lineSeparator());
		printCoursesOfDay("wednesday");
		System.out.println("~~~~~~~~~~THURSDAY~~~~~~~~~~" + System.lineSeparator());
		printCoursesOfDay("thursday");
		System.out.println("~~~~~~~~~~FRIDAY~~~~~~~~~~" + System.lineSeparator());
		printCoursesOfDay("friday");
	}
	
	private void printCoursesOfDay(String day) {
		for (Course course : courses) {
			for (Schedule schedule : course.getSchedule()) {
				for (MeetingTime meetingTime : schedule.getMeetingTimes()) {
					if (meetingTime.getDay().equalsIgnoreCase(day)) {
						course.print();
					}
				}
			}
		}
	}
	
	public boolean isCourseAppropriate(Course course) {
		for (Course anotherCourse : courses) {
			for (Schedule anotherSchedule : anotherCourse.getSchedule()) {
				for (MeetingTime anotherMeetingTime : anotherSchedule.getMeetingTimes()) {
					for (Schedule schedule : course.getSchedule()) {
						for (MeetingTime meetingTime : schedule.getMeetingTimes()) {
							if (meetingTime.getDay().equals(anotherMeetingTime.getDay()) && ((meetingTime.getStartTimeAsMinute() > anotherMeetingTime.getStartTimeAsMinute() && meetingTime.getStartTimeAsMinute() < anotherMeetingTime.getFinishTimeAsMinute())
								|| (anotherMeetingTime.getStartTimeAsMinute() > meetingTime.getStartTimeAsMinute() && anotherMeetingTime.getStartTimeAsMinute() < meetingTime.getFinishTimeAsMinute()))) {
								return false;
							}
						}
					}
				}
			}
		}
		
		return true;
	}

	public void addCourse(Course course) {
		courses.add(course);
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ArrayList<Course> getCourses() {
		return courses;
	}

	public void setCourses(ArrayList<Course> courses) {
		this.courses = courses;
	}
	
}