package dss.business.User;

import java.util.List;
import java.util.Map;

public class AthleteStudent extends Student {

    public AthleteStudent(int id, String pass, int idCourse) {
        super(id, pass, idCourse);
    }

    public AthleteStudent(int id, String pass, int idCourse, List<Integer> ucs, Map<Integer, List<Integer>> schedule) {
        super(id, pass, idCourse, ucs, schedule);
    }

    @Override
    public int getType() {
        return 1;
    }
}
