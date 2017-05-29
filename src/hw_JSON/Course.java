package hw_JSON;

import java.util.ArrayList;

public class Course {
	private String subjectName, sectionNo, courseNo;

	private ArrayList<String> instructors;
	private ArrayList<Schedule> schedule;
	private ArrayList<Student> students;

	public Course() {
		instructors = new ArrayList<String>();
		schedule = new ArrayList<Schedule>();
		students = new ArrayList<Student>();
	}

	public void print() {
		printMainInfo();
		printInstructors();
		printSchedule();
		printStudents();
		System.out.println();
	}
	
	private void printMainInfo() {
		System.out.println("//////////MAIN INFORMATION/////////");
		System.out.println("Subject Name: " + subjectName);
		System.out.println("Course No: " + courseNo);
		System.out.println("Section No: " + sectionNo);
	}
	
	private void printInstructors() {
		System.out.println("//////////INSTRUCTORS/////////");
		for (int i = 0; i < instructors.size(); i++) {
			if (i == 0) {
				System.out.println("Primary Instructor: " + instructors.get(i));
			} else {
				System.out.println("Instructor: " + instructors.get(i));
			}
		}
	}
	
	private void printSchedule() {
		System.out.println("//////////SCHEDULE/////////");
		for (Schedule schedule : this.schedule) {
			System.out.println("Start Date: " + schedule.getStartDate().toString());
			System.out.println("Finish Date: " + schedule.getFinishDate().toString());
			
			System.out.println("--------Meeting Times--------");
			for (MeetingTime meetingTime : schedule.getMeetingTimes()) {
				System.out.println("Date: " + meetingTime.getDay() + " " + meetingTime.getStartHour() + " - " + meetingTime.getFinishHour());
				for(String room : meetingTime.getRoomCodes()) {
					System.out.println("Room: " + room);
				}
				System.out.println("----------");
			}
		}
	}
	
	private void printStudents() {
		System.out.println("//////////STUDENTS/////////");
		for (Student student : students) {
			System.out.println(student.getId());
		}
	}

	public void addInstructor(String instructorName) {
		instructors.add(instructorName);
	}

	public void addSchedule(Schedule schedule) {
		this.schedule.add(schedule);
	}
	
	public void addStudent(Student student) {
		students.add(student);
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public String getCourseNo() {
		return courseNo;
	}

	public void setCourseNo(String courseNo) {
		this.courseNo = courseNo;
	}

	public String getSectionNo() {
		return sectionNo;
	}

	public void setSectionNo(String sectionNo) {
		this.sectionNo = sectionNo;
	}

	public ArrayList<String> getInstructors() {
		return instructors;
	}

	public void setInstructors(ArrayList<String> instructors) {
		this.instructors = instructors;
	}

	public ArrayList<Schedule> getSchedule() {
		return schedule;
	}

	public void setSchedule(ArrayList<Schedule> schedule) {
		this.schedule = schedule;
	}

}
