package dss.business.Course;

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

    // Mariana
    public boolean importStudents(String path, int idCourse){
        return false;
    }

    public boolean importUCs(String path, int idCourse){
        return false;
    }

    public boolean addStudent(int idStudent, int idCourse){
        return false;
    }
}
