package dss.business.User;

public interface IGesUser {
    
    public boolean verifyIdentity(int idUser);

    public boolean verifyPassword(int idUser, String password);

    public Student getStudent(int idStudent) throws Exception;
}
