package dss.business.Course;

public class TheoreticalPractical extends Shift{
    public int capacity;

    public TheoreticalPractical(int id, int capacityRoom, int enrolledCount, int ucId) {
        super(id, capacityRoom, enrolledCount, ucId);
        this.capacity = 0;
    }

    public TheoreticalPractical(int id, int capacityRoom, int enrolledCount, int capacity, int ucId) {
        super(id, capacityRoom, enrolledCount, ucId);
        this.capacity = capacity;
    }

    @Override
    public boolean hasCapacity() {
        return enrolledCount < capacity && enrolledCount < capacityRoom;
    }
}
