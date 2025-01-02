package dss.business.Course;
import java.util.List;

public interface IGesCourse {
    
    public boolean registerPolicyOption(int idCourse, int idUC, String policyPreference);

    public boolean importStudents(String path, int idCourse) throws Exception;

    public boolean importUCs(String path, int idCourse);

    public boolean addStudent(int idStudent, int idCourse, List<Integer> ucs);
}
