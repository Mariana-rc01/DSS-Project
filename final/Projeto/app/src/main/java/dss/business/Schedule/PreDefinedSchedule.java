package dss.business.Schedule;

import java.util.*;

public class PreDefinedSchedule {
    private int Id;
    private int year;
    private int no_conflicts;
    private Map<Integer, Map<Integer, List<Integer>>> schedule;
            // <UC, <Shift, List<TimeSlot>>>

    public PreDefinedSchedule(int scheduleId, int year, int no_conflicts, Map<Integer, Map<Integer, List<Integer>>> schedule) {
        this.Id = scheduleId;
        this.year = year;
        this.no_conflicts = no_conflicts;
        this.schedule = schedule;
    }

    public int getId() {
        return Id;
    }

    public void setId(int scheduleId) {
        this.Id = scheduleId;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getNoConflicts() {
        return no_conflicts;
    }

    public void setNoConflicts(int noConflicts) {
        this.no_conflicts = noConflicts;
    }

    public Map<Integer, Map<Integer, List<Integer>>> getSchedule() {
        return schedule;
    }

    public void setSchedule(Map<Integer, Map<Integer, List<Integer>>> schedule) {
        this.schedule = schedule;
    }
}
