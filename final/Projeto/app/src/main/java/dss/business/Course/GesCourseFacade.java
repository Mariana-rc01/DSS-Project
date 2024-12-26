package dss.business.Course;

import java.util.List;
import java.util.Map;
import dss.data.CourseDAO;
import dss.data.UCDAO;

public class GesCourseFacade {
    private final CourseDAO courses;
    private final UCDAO ucs;

    public GesCourseFacade() {
        this.courses = CourseDAO.getInstance();
        this.ucs = UCDAO.getInstance();
    }

    public boolean registerPolicyOption(String idCourse, String idUC, String policyPreference){
        return false;
    }

    public boolean importStudents(String path, String idCourse){
        return false;
    }

    public boolean importUCs(String path, String idCourse){
        return false;
    }

    public boolean addStudent(int idStudent, String idCourse){
        return false;
    }
}
