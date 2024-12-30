package dss.business.Course;

import java.sql.Time;
import java.time.DayOfWeek;

public class TimeSlot {

    private int id;
    private Time timeStart;
    private Time timeEnd;
    private DayOfWeek weekDay;
    private int shiftId;

    public TimeSlot(int id, Time start, Time end, DayOfWeek weekDay, int shiftId) {
        this.id = id;
        this.timeStart = start;
        this.timeEnd = end;
        this.weekDay = weekDay;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Time getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(Time timeStart) {
        this.timeStart = timeStart;
    }

    public Time getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(Time timeEnd) {
        this.timeEnd = timeEnd;
    }

    public DayOfWeek getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(DayOfWeek weekDay) {
        this.weekDay = weekDay;
    }

    public int getShiftId() {
        return shiftId;
    }

    public void setShiftId(int shiftId) {
        this.shiftId = shiftId;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        TimeSlot timeSlot = (TimeSlot) obj;
        return timeStart.equals(timeSlot.timeStart) && timeEnd.equals(timeSlot.timeEnd) && weekDay == timeSlot.weekDay;
    }

    public boolean hasConflict(TimeSlot timeSlot) {
        return this.timeStart.before(timeSlot.timeEnd) &&
        this.timeEnd.after(timeSlot.timeStart) &&
        this.weekDay == timeSlot.weekDay;
    }
}
