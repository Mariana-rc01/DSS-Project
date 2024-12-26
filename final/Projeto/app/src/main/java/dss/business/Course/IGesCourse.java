package dss.business.Course;

public interface IGesCourse {
    
    public boolean registerPolicyOption(String idCourse, String idUC, String policyPreference);

    public boolean importStudents(String path, String idCourse);

    public boolean importUCs(String path, String idCourse);

    public boolean addStudent(int idStudent, String idCourse);
}
