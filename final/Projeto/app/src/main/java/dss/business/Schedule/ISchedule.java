package dss.business.Schedule;

import java.util.List;
import java.util.Map;

import dss.business.Course.*;
import dss.business.User.Student;

public interface ISchedule {
    
    public List<Integer> getStudentsWithScheduleConflicts(int idCourse);

    public boolean exportSchedule (int idStudent, String filename);

    public void generateSchedule (int idCourse);

    public List<Student> getStudentsWithoutSchedule (int idCourse);

    public boolean importTimeTable (int idCourse, int year, String path);

    public boolean postSchedule (int idCourse);

    public boolean sendEmails (int idCourse);

    public Map<UC, Map<Shift,List<TimeSlot>>> getStudentSchedule (int idStudent, int idCourse);

    public boolean registerSchedule (int idStudent, Map<Integer, List<Integer>> schedule);

}
