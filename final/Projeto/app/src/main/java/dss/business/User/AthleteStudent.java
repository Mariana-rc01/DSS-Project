package dss.business.User;

public class AthleteStudent extends Student {

    public AthleteStudent(int id, String pass, int idCourse) {
        super(id, pass, idCourse);
    }

    @Override
    public int getType() {
        return 1;
    }
}
