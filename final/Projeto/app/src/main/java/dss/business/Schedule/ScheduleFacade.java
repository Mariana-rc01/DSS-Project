package dss.business.Schedule;

import java.util.List;
import java.util.Map;
import dss.data.StudentDAO;

public class ScheduleFacade {
    //private Map<String, Course> courses;
    //private Map<String, Student> students;

    public ScheduleFacade() {
        //this.courses = CourseDAO.getInstance();
        //this.students = StudentDAO.getInstance();
    }

    public List<Integer> getStudentsWithScheduleConflicts(String idCourse) {
        return null;
    }

    public boolean exportSchedule (int idStudent, String filename) {
        return false;
    }

    public void generateSchedule (int idCourse) {
        
    }

    /*public List<Student> getStudentsWithoutSchedule (String idCourse) {
        return null;
    }*/

    public boolean importTimeTable (String idCourse, String year, String path) {
        return false;
    }

    public boolean postSchedule (String idCourse) {
        return false;
    }

    public boolean sendEmails (String idCourse) {
        return false;
    }

    /*public Map<UC, Map<Shift,List<TimeSlots>>> getStudentSchedule (int idStudent, String idCourse) {
        return null;
    }*/

    public boolean registerSchedule (String idCourse, int idStudent, Map<Integer, List<Integer>> schedule) {
        return false;
    }
}
