package dss.business.User;

import java.util.*;

import dss.business.Course.Shift;
import dss.business.Course.TimeSlot;
import dss.business.Course.UC;

public class Student {

    private int id;
    private String password;
    private int course;
    private Map<Integer, List<Integer>> schedule;
    private List<Integer> ucs;

    public Student(int id, String password, int idCourse){
        this.id = id;
        this.password = password;
        this.course = idCourse;
    }

    public int getId() {
        return id;
    }

    public int getCourse() {
        return course;
    }

    public void addUC(UC uc) {
        ucs.add(uc.getId());
    }

    public void removeUC(UC uc) {
        ucs.remove(uc.getId());
    }

    public List<Integer> getUCs() {
        return ucs;
    }

    public void setUCs(List<Integer> ucs) {
        this.ucs = ucs;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setCourse(int course) {
        this.course = course;
    }

    public boolean verifyPassword(String password) {
        return this.password.equals(password);
    }

    public int getType() {
        return 0;
    }

    public Map<Integer, List<Integer>> getSchedule() {
        return schedule;
    }

    public void setSchedule(Map<Integer, List<Integer>> schedule) {
        this.schedule = schedule;
    }


    public Map<UC, Map<Shift, List<TimeSlot>>> getSchedulePretty() {
        return null;
    }


   public void addSchedule(Map<Integer, List<Integer>> schedule) {
   
   }

    public String getEmail(){
        return "a" + id + "@alunos.uminho.pt";
    }

    public boolean hasSchedule() {
        return false;
    }

    public void sendEmail() {

    }

    public void sendEmailAux(String title, String body){

    }

    @Override
    public Student clone() {
        try {
            Student cloned = (Student) super.clone();
            cloned.schedule = new HashMap<>(this.schedule);
            for (Map.Entry<Integer, List<Integer>> entry : this.schedule.entrySet()) {
                cloned.schedule.put(entry.getKey(), new ArrayList<>(entry.getValue()));
            }
            cloned.ucs = new ArrayList<>(this.ucs);
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

}