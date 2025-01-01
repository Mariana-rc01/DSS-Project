package dss.business.Course;

import java.util.List;

public class Shift {

    protected int id;
    protected int capacityRoom;
    protected int enrolledCount;
    protected int ucId;
    protected List<TimeSlot> timeSlots;

    public Shift(int id, int capacityRoom, int enrolledCount, int ucId) {
        this.id = id;
        this.capacityRoom = capacityRoom;
        this.enrolledCount = enrolledCount;
        this.ucId = ucId;
    }

    public Shift(int id, int capacityRoom, int enrolledCount, int ucId, List<TimeSlot> timeSlots) {
        this.id = id;
        this.capacityRoom = capacityRoom;
        this.enrolledCount = enrolledCount;
        this.ucId = ucId;
        this.timeSlots = timeSlots;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCapacityRoom() {
        return capacityRoom;
    }

    public void setCapacityRoom(int capacityRoom) {
        this.capacityRoom = capacityRoom;
    }

    public int getEnrolledCount() {
        return enrolledCount;
    }

    public void setEnrolledCount(int enrolledCount) {
        this.enrolledCount = enrolledCount;
    }

    public int getUcId() {
        return ucId;
    }

    public void setUcId(int ucId) {
        this.ucId = ucId;
    }

    public List<TimeSlot> getTimeSlots() {
        return timeSlots;
    }

    public void setTimeSlots(List<TimeSlot> timeSlots) {
        this.timeSlots = timeSlots;
    }

    public void addStudent() {
        this.enrolledCount++;
    }

    public boolean hasCapacity() {
        return enrolledCount < capacityRoom;
    }
}
