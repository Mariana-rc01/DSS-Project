package dss.business.User;

public class CourseDirector {
    int id;
    String password;

    public CourseDirector(int id, String password){
        this.id = id;
        this.password = password;
    }

    public boolean verifyPassword(String password) {
        return this.password == password;
    }
}