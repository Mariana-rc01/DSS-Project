package dss.business.Course;

import java.util.List;

public class Theoretical extends Shift{

    public Theoretical(int id, int capacityRoom, int enrolledCount, int ucId) {
        super(id, capacityRoom, enrolledCount, ucId);
    }

    public Theoretical(int id, int capacityRoom, int enrolledCount, int ucId, List<TimeSlot> timeSlots) {
        super(id, capacityRoom, enrolledCount, ucId, timeSlots);
    }
    
    @Override
    public boolean hasCapacity() {
        return enrolledCount < capacityRoom;
    }
}
