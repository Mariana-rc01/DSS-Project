package dss.business.User;


import dss.data.StudentDAO;
import dss.data.CourseDirectorDAO;

public class GesUserFacade implements IGesUser {
    private final StudentDAO students;
    private final CourseDirectorDAO directors;

    public GesUserFacade() {
        this.students = new StudentDAO();
        this.directors = new CourseDirectorDAO();
    }

    public boolean verifyIdentity(int idUser){
        try {
            return students.getStudent(idUser) != null || directors.getCourseDirector(idUser) != null;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean verifyPassword(int idUser, String password){
        try {
            Student student = students.getStudent(idUser);
            if (student != null) {
                return student.verifyPassword(password);
            }

            CourseDirector director = directors.getCourseDirector(idUser);
            if (director != null) {
                return director.verifyPassword(password);
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    public Student getStudent(int idStudent) throws Exception{
        return students.getStudent(idStudent);
    }
}