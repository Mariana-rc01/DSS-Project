package dss.business;

import java.util.List;
import java.util.Map;

import dss.business.Course.GesCourseFacade;
import dss.business.Course.Shift;
import dss.business.Course.TimeSlot;
import dss.business.Course.UC;
import dss.business.User.GesUserFacade;
import dss.business.User.Student;
import dss.business.Schedule.GesScheduleFacade;

public class LNFacade {
    private final GesCourseFacade gesCourseFacade = new GesCourseFacade();
    private final GesUserFacade gesUserFacade = new GesUserFacade();
    private final GesScheduleFacade gesScheduleFacade = new GesScheduleFacade();

    // Course
    public boolean registerPolicyOption(String idCourse, String idUC, String policyPreference){
        return gesCourseFacade.registerPolicyOption(idCourse, idUC, policyPreference);
    }

    public boolean importStudents(String path, String idCourse){
        return gesCourseFacade.importStudents(path, idCourse);
    }

    public boolean importUCs(String path, String idCourse){
        return gesCourseFacade.importUCs(path, idCourse);
    }

    public boolean addStudent(int idStudent, String idCourse){
        return gesCourseFacade.addStudent(idStudent, idCourse);
    }

    // User
    public boolean verifyIdentity(int idUser){
        return gesUserFacade.verifyIdentity(idUser);
    }

    public boolean verifyPassword(int idUser, String password){
        return gesUserFacade.verifyPassword(idUser, password);
    }

    // Schedule
    public List<Integer> getStudentsWithScheduleConflicts(String idCourse) {
        return gesScheduleFacade.getStudentsWithScheduleConflicts(idCourse);
    }

    public boolean exportSchedule (int idStudent, String filename) {
        return gesScheduleFacade.exportSchedule(idStudent, filename);
    }

    public void generateSchedule (int idCourse) {
        gesScheduleFacade.generateSchedule(idCourse);
    }

    public List<Student> getStudentsWithoutSchedule (String idCourse) {
        return gesScheduleFacade.getStudentsWithoutSchedule(idCourse);
    }

    public boolean importTimeTable (String idCourse, String year, String path) {
        return gesScheduleFacade.importTimeTable(idCourse, year, path);
    }

    public boolean postSchedule (String idCourse) {
        return gesScheduleFacade.postSchedule(idCourse);
    }

    public boolean sendEmails (String idCourse) {
        return gesScheduleFacade.sendEmails(idCourse);
    }

    public Map<UC, Map<Shift,List<TimeSlot>>> getStudentSchedule (int idStudent, String idCourse) {
        return gesScheduleFacade.getStudentSchedule(idStudent, idCourse);
    }

    public boolean registerSchedule (String idCourse, int idStudent, Map<Integer, List<Integer>> schedule) {
        return gesScheduleFacade.registerSchedule(idCourse, idStudent, schedule);
    }
}
