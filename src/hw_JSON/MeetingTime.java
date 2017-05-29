package hw_JSON;

import java.util.ArrayList;

public class MeetingTime {
	
	private String day, startHour, finishHour;
	
	private int startTimeAsMinute, finishTimeAsMinute;
	
	private ArrayList<String> roomCodes;
	
	public MeetingTime() {
		roomCodes = new ArrayList<String>();
	}
	
	public boolean isMorningCourse(String time) {
		String hourAndMinute[] = time.split(":");
		
		int hour = getHour(hourAndMinute);
		int minute = getMinute(hourAndMinute);
		
		if(hour > 8 && hour < 11) {
			return true;
		} else if(hour == 11 && minute <= 40) {
			return true;
		} else if(hour == 8 && minute >= 40) {
			return true;
		}
		
		return false;
	}
	
	public int getHour(String hourAndMinute[]) {
		int hour;
		
		try {
			hour = Integer.parseInt(hourAndMinute[0]);
		} catch(NumberFormatException e) {
			hour = Integer.parseInt(hourAndMinute[0].substring(1, 2)); // first zero does not appear in time
		}
		
		return hour;
	}
	
	public int getMinute(String hourAndMinute[]) {
		int minute;
		
		try {
			minute = Integer.parseInt(hourAndMinute[1]);
		} catch(NumberFormatException e) {
			minute = Integer.parseInt(hourAndMinute[1].substring(1, 2)); // first zero does not appear in time
		}
		
		return minute;
	}
	
	public void addRoom(String room) {
		roomCodes.add(room);
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getStartHour() {
		return startHour;
	}

	public void setStartHour(String startHour) {
		this.startHour = startHour;
		String hourAndMinute[] = startHour.split(":");
		startTimeAsMinute = getHour(hourAndMinute) * 60 + getMinute(hourAndMinute);
	}

	public String getFinishHour() {
		return finishHour;
	}

	public void setFinishHour(String finishHour) {
		this.finishHour = finishHour;
		String hourAndMinute[] = finishHour.split(":");
		finishTimeAsMinute = getHour(hourAndMinute) * 60 + getMinute(hourAndMinute);
	}

	public ArrayList<String> getRoomCodes() {
		return roomCodes;
	}

	public void setRoomCodes(ArrayList<String> roomCodes) {
		this.roomCodes = roomCodes;
	}

	public int getStartTimeAsMinute() {
		return startTimeAsMinute;
	}

	public void setStartTimeAsMinute(int startTimeAsMinute) {
		this.startTimeAsMinute = startTimeAsMinute;
	}

	public int getFinishTimeAsMinute() {
		return finishTimeAsMinute;
	}

	public void setFinishTimeAsMinute(int finishTimeAsMinute) {
		this.finishTimeAsMinute = finishTimeAsMinute;
	}

}
