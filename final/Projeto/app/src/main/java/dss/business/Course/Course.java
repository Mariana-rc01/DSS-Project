package dss.business.Course;

import java.util.ArrayList;
import java.util.Collection;
import java.util.TreeSet;

public class Course {
    int id;
    String name;
    boolean visibilitySchedules;

    private Collection<Integer> ucs;
    private Collection<Integer> students;

    public Course(int id, String name){
        this.id = id;
        this.name = name;
        this.visibilitySchedules = false;
        this.ucs = new TreeSet<>();
        this.students = new TreeSet<>();
    }

    public Course(int id, String name, boolean visibilitySchedules){
        this.id = id;
        this.name = name;
        this.visibilitySchedules = visibilitySchedules;
    }

    public Course(int id, String name, boolean visibilitySchedules, Collection<Integer> ucs, Collection<Integer> students){
        this.id = id;
        this.name = name;
        this.visibilitySchedules = visibilitySchedules;
        this.ucs.addAll(ucs);
        this.students.addAll(students);
    }

    public Course(){
        this.id = 0;
        this.name = "";
        this.visibilitySchedules = false;
        this.ucs = new TreeSet<>();
        this.students = new TreeSet<>();
    }

    public int getId(){
        return this.id;
    }

    public String getName(){
        return this.name;
    }

    public boolean getVisibilitySchedules(){
        return this.visibilitySchedules;
    }

    public void setId(int id){
        this.id = id;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setVisibilitySchedules(boolean visibilitySchedules){
        this.visibilitySchedules = visibilitySchedules;
    }

    public Collection<Integer> getUCs(){
        return new ArrayList<>(this.ucs);
    }

    public Collection<Integer> getStudents(){
        return new ArrayList<>(this.students);
    }

    public void addUC(int idUC){
        this.ucs.add(idUC);
    }

    public void addStudent(int idStudent){
        this.students.add(idStudent);
    }

    public void removeUC(int idUC){
        this.ucs.remove(idUC);
    }

    public void removeStudent(int idStudent){
        this.students.remove(idStudent);
    }

    public boolean hasUC(int idUC){
        return this.ucs.contains(idUC);
    }

    public boolean hasStudent(int idStudent){
        return this.students.contains(idStudent);
    }

    public boolean equals(Object o){
        if(this == o){
            return true;
        }
        if(o == null || this.getClass() != o.getClass()){
            return false;
        }
        Course course = (Course) o;
        return this.id == course.getId() && this.name.equals(course.getName());
    }

    public Course clone(){
        return new Course(this.id, this.name, this.visibilitySchedules, this.ucs, this.students);
    }


    /*
    public void generateSchedule(){
        
    }
    */

    /*
    public List<Student> showStudentsWithoutSchedule(){
        
    }
    */
    
    /*
    public boolean registerSchedule(int idStudent, Map<Integer, List<Integer>> schedule, Map<Integer, List<Integer>> oldSchedule){
        
    }
    */

    /*
    public boolean sendEmail(String to, String subject, String body){
        
    }
    */

    /*
    public Map<Integer, Student> importStudents(String path){
        
    }
    */

    /*
    public String generateRandomPassword(int length){
        
    }
    */

    /*
    public void addUC(UC uc){
        
    }
    */

    /*
    public List<Integer> getStudentsWithScheduleConflicts(List<Student> students){
        
    }
    */

    /*
    public Map<String, List<Map<String, String>>> getStudentSchedule(Map<Integer, List<Integer>> idsSchedule){
        
    }
    */

    /*
    public void postSchedule(){
        
    }
    */

    /*
    public void importUCs(String path){
        
    }
    */
}