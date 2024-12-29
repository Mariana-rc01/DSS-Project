package dss.business.Course;

public class Theoretical extends Shift{

    public Theoretical(int id, int capacityRoom, int enrolledCount, int ucId) {
        super(id, capacityRoom, enrolledCount, ucId);
    }

    @Override
    public boolean hasCapacity() {
        return enrolledCount < capacityRoom;
    }
}
