package dss.business.Schedule;

import java.util.List;
import java.util.Map;

import dss.business.Course.*;
import dss.business.User.Student;

public interface ISchedule {
    
    public List<Integer> getStudentsWithScheduleConflicts(String idCourse);

    public boolean exportSchedule (int idStudent, String filename);

    public void generateSchedule (int idCourse);

    public List<Student> getStudentsWithoutSchedule (String idCourse);

    public boolean importTimeTable (String idCourse, String year, String path);

    public boolean postSchedule (String idCourse);

    public boolean sendEmails (String idCourse);

    public Map<UC, Map<Shift,List<TimeSlot>>> getStudentSchedule (int idStudent, String idCourse);

    public boolean registerSchedule (String idCourse, int idStudent, Map<Integer, List<Integer>> schedule);

}
