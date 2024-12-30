package dss.business.Course;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import dss.business.User.*;

public class Course {

    private int id;
    private String name;
    private boolean visibilitySchedules;

    public Course(int id, String name, boolean visibilitySchedules) {
        this.id = id;
        this.name = name;
        this.visibilitySchedules = visibilitySchedules;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isVisibilitySchedules() {
        return visibilitySchedules;
    }

    public void setVisibilitySchedules(boolean visibilitySchedules) {
        this.visibilitySchedules = visibilitySchedules;
    }

    /*
    // Mariana
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


    public List<Student> importStudents(String path) throws IOException {
        List<Student> students = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                int studentId = Integer.parseInt(parts[0]);
                int type = Integer.parseInt(parts[1]);
                List<Integer> ucs = new ArrayList<>();
                for (int i = 2; i < parts.length; i++) {
                    ucs.add(Integer.valueOf(parts[i]));
                }

                // Gerar senha
                String password = Student.generateRandomPassword();

                // Criar estudante do tipo apropriado
                Student student = createStudentByType(studentId, password, this.getId(), ucs, type);

                students.add(student);
            }
        }
        return students;
    }

    private static Student createStudentByType(int id, String password, int course, List<Integer> ucs, int type) {
        Map<Integer, List<Integer>> emptySchedule = new HashMap<>();
        switch (type) {
            case 1:
                return new AthleteStudent(id, password, course, ucs, emptySchedule);
            case 2:
                return new EmployedStudent(id, password, course, ucs, emptySchedule);
            default:
                return new Student(id, password, course, ucs, emptySchedule);
        }
    }

    /*
    public String generateRandomPassword(int length){
        
    }
    */

    /*
    public void addUC(UC uc){
        
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