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
        try {
            UC uc = ucs.getUC(idUC);
            if (uc == null) {
                return false;
            }
            uc.setPolicyPreference(policyPreference);
            return ucs.updateUC(uc.getId(), uc.getName(), uc.getYear(), uc.getSemester(), uc.getPolicyPreference(), uc.getIdCourse());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
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
        try {
            Course course = courses.getCourse(idCourse);
            if (course == null) {
                return false;
            }

            List<UC> ucS = course.importUCs(path);

            if (ucS != null) {
                for (UC uc : ucS) {
                    ucs.addUC(uc.getId(), uc.getName(), uc.getYear(), uc.getSemester(), uc.getPolicyPreference(), uc.getIdCourse());
                }
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean addStudent(int idStudent, int idCourse, List<Integer> ucs){
        try {
            Course course = courses.getCourse(idCourse);
            if (course == null) {
                return false;
            }
            return courses.addStudent(course.addStudent(idStudent, idCourse, ucs));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
