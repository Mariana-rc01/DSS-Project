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
    public boolean registerPolicyOption(int idCourse, int idUC, String policyPreference){
        return gesCourseFacade.registerPolicyOption(idCourse, idUC, policyPreference);
    }

    public boolean importStudents(String path, int idCourse) throws Exception{
        return gesCourseFacade.importStudents(path, idCourse);
    }

    public boolean importUCs(String path, int idCourse){
        return gesCourseFacade.importUCs(path, idCourse);
    }

    public boolean addStudent(int idStudent, int idCourse, List<Integer> ucs){
        return gesCourseFacade.addStudent(idStudent, idCourse, ucs);
    }

    // User
    public boolean verifyIdentity(int idUser){
        return gesUserFacade.verifyIdentity(idUser);
    }

    public boolean verifyPassword(int idUser, String password){
        return gesUserFacade.verifyPassword(idUser, password);
    }

    public Student getStudent(int idStudent) throws Exception{
        return gesUserFacade.getStudent(idStudent);
    }

    public int getCourseId(int idUser){
        return gesUserFacade.getCourseId(idUser);
    }

    public int getUserType(int idUser) {
        return gesUserFacade.getUserType(idUser);
    }

    // Schedule
    public List<Integer> getStudentsWithScheduleConflicts(int idCourse) {
        return gesScheduleFacade.getStudentsWithScheduleConflicts(idCourse);
    }

    public boolean exportSchedule (int idStudent, String filename) {
        return gesScheduleFacade.exportSchedule(idStudent, filename);
    }

    public void generateSchedule (int idCourse) {
        gesScheduleFacade.generateSchedule(idCourse);
    }

    public List<Student> getStudentsWithoutSchedule (int idCourse) {
        return gesScheduleFacade.getStudentsWithoutSchedule(idCourse);
    }

    public boolean importTimeTable (int idCourse, int year, String path) {
        return gesScheduleFacade.importTimeTable(idCourse, year, path);
    }

    public boolean postSchedule (int idCourse) {
        return gesScheduleFacade.postSchedule(idCourse);
    }

    public boolean sendEmails (int idCourse) {
        return gesScheduleFacade.sendEmails(idCourse);
    }

    public Map<UC, Map<Shift,List<TimeSlot>>> getStudentSchedule (int idStudent, int idCourse) {
        return gesScheduleFacade.getStudentSchedule(idStudent, idCourse);
    }

    public boolean registerSchedule (int idStudent, Map<Integer, List<Integer>> schedule) {
        return gesScheduleFacade.registerSchedule(idStudent, schedule);
    }

    public boolean importSchedulesPreDefined (int idCourse, String path) {
        return gesScheduleFacade.importSchedulesPreDefined(idCourse, path);
    }
}
