package dss.business.Schedule;

import java.util.*;

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

    public List<Integer> getStudentsWithScheduleConflicts(int idCourse) {
        List<Integer> studentsWithScheduleConflicts = new ArrayList<>();
        try {
            List<Student> studentss = this.students.getStudentsByCourse(idCourse);

            List<Shift> shifts = this.courses.getShiftsByCourse(idCourse);

            for (Student student : studentss) {
                if (student.hasScheduleConflict(shifts)) {
                    studentsWithScheduleConflicts.add(student.getId());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return studentsWithScheduleConflicts;
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

    public boolean registerSchedule (int idStudent, Map<Integer, List<Integer>> schedule) {
        try {

            Student student = students.getStudent(idStudent);
            if (student == null) {
                return false;
            }

            students.removeScheduleFromStudent(idStudent);
            students.addScheduleToStudent(idStudent, schedule);

            return true;
            
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
