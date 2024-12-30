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
    public List<Integer> getStudentsWithScheduleConflicts(int idCourse) {
        return null;
    }

    public boolean exportSchedule (int idStudent, String filename) {
        return false;
    }

    // Mariana
    public void generateSchedule (int idCourse) {
        
    }

    public List<Student> getStudentsWithoutSchedule (int idCourse) {
        return null;
    }

    public boolean importTimeTable (int idCourse, int year, String path) {
        return false;
    }

    public boolean postSchedule (int idCourse) {
        return false;
    }

    public boolean sendEmails (int idCourse) {
        try {
            Course course = courses.getCourse(idCourse);
            if (course == null) {
                return false;
            }

            List<Student> studentss = students.getStudentsByCourse(idCourse);
            if (studentss == null) {
                return false;
            }

            for (Student student : studentss) {
                student.sendEmail();
            }

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Map<UC, Map<Shift,List<TimeSlot>>> getStudentSchedule (int idStudent, int idCourse) {
        return null;
    }

    // Mariana
    public boolean registerSchedule (int idCourse, int idStudent, Map<Integer, List<Integer>> schedule) {
        return false;
    }
}
