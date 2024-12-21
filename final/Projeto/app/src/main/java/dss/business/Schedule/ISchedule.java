package dss.business.Schedule;

import java.util.List;
import java.util.Map;

public interface ISchedule {
    
    public List<Integer> getStudentsWithScheduleConflicts(String idCourse);

    public boolean exportSchedule (int idStudent, String filename);

    public void generateSchedule (int idCourse);

    /*public List<Student> getStudentsWithoutSchedule (String idCourse); */

    public boolean importTimeTable (String idCourse, String year, String path);

    public boolean postSchedule (String idCourse);

    public boolean sendEmails (String idCourse);

    /*public Map<UC, Map<Shift,List<TimeSlots>>> getStudentSchedule (int idStudent, String idCourse);   return null;
    }*/

    public boolean registerSchedule (String idCourse, int idStudent, Map<Integer, List<Integer>> schedule);

}
