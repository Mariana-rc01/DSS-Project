package dss.business.Schedule;

import java.util.List;
import java.util.Map;

import dss.business.Course.*;
import dss.business.User.Student;
import dss.data.CourseDAO;
import dss.data.StudentDAO;

public class GesScheduleFacade implements ISchedule {

    private CourseDAO courses;
    private StudentDAO students;

    public GesScheduleFacade() {
        this.courses = new CourseDAO();
        this.students = new StudentDAO();
    }

    // Mariana
    public List<Integer> getStudentsWithScheduleConflicts(String idCourse) {
        return null;
    }

    public boolean exportSchedule (int idStudent, String filename) {
        return false;
    }

    // Mariana
    public void generateSchedule (int idCourse) {
        
    }

    public List<Student> getStudentsWithoutSchedule (String idCourse) {
        return null;
    }

    public boolean importTimeTable (String idCourse, String year, String path) {
        return false;
    }

    public boolean postSchedule (String idCourse) {
        return false;
    }

    // Mariana
    public boolean sendEmails (String idCourse) {

        return false;
    }

    public Map<UC, Map<Shift,List<TimeSlot>>> getStudentSchedule (int idStudent, String idCourse) {
        return null;
    }

    // Mariana
    public boolean registerSchedule (String idCourse, int idStudent, Map<Integer, List<Integer>> schedule) {
        return false;
    }
}
