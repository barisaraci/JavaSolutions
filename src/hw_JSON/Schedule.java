package hw_JSON;

import java.util.ArrayList;
import java.util.Date;

public class Schedule {
	
	private Date startDate, finishDate;
	
	private ArrayList<MeetingTime> meetingTimes;
	
	public Schedule() {
		meetingTimes = new ArrayList<MeetingTime>();
	}
	
	public void addMeetingTime(MeetingTime meetingTime) {
		meetingTimes.add(meetingTime);
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getFinishDate() {
		return finishDate;
	}

	public void setFinishDate(Date finishDate) {
		this.finishDate = finishDate;
	}

	public ArrayList<MeetingTime> getMeetingTimes() {
		return meetingTimes;
	}

	public void setMeetingTimes(ArrayList<MeetingTime> meetingTimes) {
		this.meetingTimes = meetingTimes;
	}

}
