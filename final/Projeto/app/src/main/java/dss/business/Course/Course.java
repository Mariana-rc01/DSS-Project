package dss.business.Course;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.sql.Time;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.util.*;
import javax.json.*;
import javax.json.stream.JsonParser;

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

    public boolean postSchedule() {
        setVisibilitySchedules(true);
        return true;
    }

    public List<UC> importUCs(String path) {
        File file = new File(path);
        if (!file.exists() || file.isDirectory()) {
            throw new IllegalArgumentException("File does not exist or is a directory");
        }

        List<UC> ucs = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            JsonReader jsonReader = Json.createReader(br);
            JsonArray jsonArray = jsonReader.readArray();
            for(JsonObject jsonObject : jsonArray.getValuesAs(JsonObject.class)){
                int id = jsonObject.getInt("id");
                String name = jsonObject.getString("name");
                int year = jsonObject.getInt("year");
                int semester = jsonObject.getInt("semester");
                String policyPreference = jsonObject.getString("policy");  
                UC uc = new UC(id, name, year, semester, policyPreference, this.getId());
                ucs.add(uc);
            }
            return ucs;
        } catch (IOException e) {
            throw new IllegalArgumentException("Error reading file", e);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error parsing JSON", e);
        }
    }

    public List<Shift> importTimeTable(Integer year, String path) {
        File file = new File(path);
        if (!file.exists() || file.isDirectory()) {
            throw new IllegalArgumentException("File does not exist or is a directory");
        }

        List<Shift> shifts = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            JsonReader jsonReader = Json.createReader(br);
            JsonArray jsonArray = jsonReader.readArray();
            for(JsonObject jsonObject : jsonArray.getValuesAs(JsonObject.class)){
                int id = jsonObject.getInt("id");
                int idUC = jsonObject.getInt("uc");
                String type = jsonObject.getString("type");
                int roomCapacity = jsonObject.getInt("roomCapacity");
                
                List<TimeSlot> timeSlots = new ArrayList<>();

                JsonArray slots = jsonObject.getJsonArray("slots");
                int index = 0;
                for (JsonObject slot : slots.getValuesAs(JsonObject.class)) {
                    String day = slot.getString("day");
                    DayOfWeek weekday = DayOfWeek.valueOf(day.toUpperCase());
                    String start = slot.getString("start");
                    String end = slot.getString("end");
                    Time startTime = Time.valueOf(start + ":00");
                    Time endTime = Time.valueOf(end + ":00");
                    int uniqueId = id * 1000 + index;
                    TimeSlot timeSlot = new TimeSlot(uniqueId, startTime, endTime, weekday, id);
                    timeSlots.add(timeSlot);
                }
                
                if(type.equals("T")){
                    Theoretical theoretical = new Theoretical(id, roomCapacity, 0, idUC, timeSlots);
                    shifts.add(theoretical);
                } else {
                    int capacity = jsonObject.getInt("capacity");
                    TheoreticalPractical theoreticalPractical = new TheoreticalPractical(id, roomCapacity, 0, capacity, idUC, timeSlots);
                    shifts.add(theoreticalPractical);
                }
            }
            return shifts;
        } catch (IOException e) {
            throw new IllegalArgumentException("Error reading file", e);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error parsing JSON", e);
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

    */
}