package dss.business.Course;

import java.util.*;

import dss.business.User.Student;
import dss.data.CourseDAO;
import dss.data.UCDAO;

public class GesCourseFacade implements IGesCourse {

    private CourseDAO courses;
    private UCDAO ucs;

    public GesCourseFacade() {
        this.courses = new CourseDAO();
        this.ucs = new UCDAO();
    }

    public boolean registerPolicyOption(int idCourse, int idUC, String policyPreference){
        return false;
    }

    public boolean importStudents(String path, int idCourse) throws Exception{
        try{
            List<Student> studentS;
            Course course = courses.getCourse(idCourse);
            if (course == null) {
                return false;
            }

            studentS = course.importStudents(path);

            if (studentS != null) {
                for (Student student : studentS) {
                    boolean flag = courses.addStudent(student);
                    if (!flag) {
                        return false;
                    }
                }
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    public boolean importUCs(String path, int idCourse){
        return false;
    }

    public boolean addStudent(int idStudent, int idCourse){
        return false;
    }
}
