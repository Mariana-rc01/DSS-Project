package dss.business.User;

public class EmployedStudent extends Student{

    public EmployedStudent(int id, String pass, int idCourse) {
        super(id, pass, idCourse);
    }

    @Override
    public int getType() {
        return 2;
    }
}
