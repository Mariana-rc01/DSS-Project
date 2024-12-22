package dss.business.User;

public class Student {
    int id;
    String password;
    String idCourse;

    public Student(int id, String password, String idCourse){
        this.id = id;
        this.password = password;
        this.idCourse = idCourse;
    }

    public boolean verifyPassword(String password) {
        return this.password == password;
    }

    /*
    public Map<UC, Map<Shift, List<TimeSlot>>> getSchedule() {

    }
    */

   /*
   public void addSchedule(Map<Integer, List<Integer>>) {
   
   }
   */

    public String getEmail(){
        return "a" + id + "@alunos.uminho.pt";
    }

    public boolean hasSchedule() {
        return false;
    }

    public void sendEmail() {

    }

    public void senEmailAux(String title, String body){

    }
}