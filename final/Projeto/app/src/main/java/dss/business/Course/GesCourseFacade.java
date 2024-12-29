package dss.business.Course;

import dss.data.CourseDAO;
import dss.data.ShiftDAO;
import dss.data.TimeSlotDAO;
import dss.data.UCDAO;

public class GesCourseFacade implements IGesCourse {

    private CourseDAO courses;
    private UCDAO ucs;
    private ShiftDAO shifts;
    private TimeSlotDAO timeSlots;

    public GesCourseFacade() {
        this.courses = new CourseDAO();
        this.ucs = new UCDAO();
        this.shifts = new ShiftDAO();
        this.timeSlots = new TimeSlotDAO();
    }

    public boolean registerPolicyOption(String idCourse, String idUC, String policyPreference){
        return false;
    }

    // Mariana
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
