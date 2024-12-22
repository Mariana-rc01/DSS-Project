package dss.business.User;

import java.util.List;
import java.util.Map;
import dss.data.StudentDAO;
import dss.data.CourseDirectorDAO;

public class GesUserFacade implements IGesUser {
    private final StudentDAO students;
    private final CourseDirectorDAO directors;

    public GesUserFacade() {
        this.students = StudentDAO.getInstance();
        this.directors = CourseDirectorDAO.getInstance();
    }

    public boolean verifyIdentity(int idUser){
        return false;
    }

    
    public boolean verifyPassword(int idUser, String password){
        return false;
    }
}