package dss.business.Course;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Time;
import java.time.DayOfWeek;
import java.util.*;
import javax.json.*;

import dss.business.Schedule.PreDefinedSchedule;
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

    /**
     * Generates schedules for students based on their year of enrollment and predefined schedules.
     *
     * @param students List of students to assign schedules.
     * @param shifts Map of shifts with their details.
     * @param ucsByYear Map of UCs grouped by year.
     * @param schedules Map of predefined schedules.
     */
    public void generateSchedule(List<Student> students, Map<Integer, Shift> shifts, Map<Integer, List<Integer>> ucsByYear, Map<Integer, PreDefinedSchedule> schedules) {
        // A counter to alternate the schedules assigned to students of the same year
        Map<Integer, Integer> scheduleRoundRobin = new HashMap<>();
    
        for (Student student : students) {
            // Find all years the student is enrolled in
            List<Integer> studentYears = student.getYearsWithUCs(ucsByYear);
    
            // Map to store the final schedule of the student
            Map<Integer, List<Integer>> finalSchedule = new HashMap<>();
    
            if (studentYears.size() == 1) {
                // Student has UCs from only one year
                int year = studentYears.get(0);
    
                // Find the schedules of the corresponding year with no_conflicts = 0
                List<PreDefinedSchedule> yearSchedules = schedules.values().stream()
                        .filter(schedule -> schedule.getYear() == year && schedule.getNoConflicts() == 0)
                        .toList();
    
                // Find the index of the schedule to assign
                int selectedIndex = scheduleRoundRobin.getOrDefault(year, 0);
                PreDefinedSchedule selectedSchedule = yearSchedules.get(selectedIndex % yearSchedules.size());
                scheduleRoundRobin.put(year, selectedIndex + 1);
    
                // Assign the schedule to the student
                assignScheduleToStudent(student, finalSchedule, selectedSchedule, shifts);
            } else {
                // Student has UCs from multiple years
                for (int year : studentYears) {
                    // Find the schedule corresponding to the year with the appropriate no_conflicts
                    int noConflicts;
                    if (year == 1 || year == 3) {                        
                        noConflicts = 2;
                    } else if (year == 2) {
                        // For year 2, the conflict is with the other year in the list (1 or 3)
                        noConflicts = studentYears.stream().filter(y -> y != 2).findFirst().orElse(0);
                    } else {
                        throw new IllegalArgumentException("Ano inesperado: " + year);
                    }

                    PreDefinedSchedule selectedSchedule = schedules.values().stream()
                            .filter(schedule -> schedule.getYear() == year && schedule.getNoConflicts() == noConflicts)
                            .findFirst()
                            .orElse(null);

                    if (selectedSchedule != null) {
                        assignScheduleToStudent(student, finalSchedule, selectedSchedule, shifts);
                    }
                }
            }
    
            // Set the final schedule to the student
            student.setSchedule(finalSchedule);
        }
    }
    
    /**
     * Auxiliary method to assign a schedule to a student.
     *
     * @param student The student to assign the schedule to.
     * @param finalSchedule Map to store the final schedule for the student.
     * @param schedule The predefined schedule being assigned.
     * @param shifts Map of shifts with their details.
     */
    private void assignScheduleToStudent(Student student, Map<Integer, List<Integer>> finalSchedule, PreDefinedSchedule schedule, Map<Integer, Shift> shifts) {
        Map<Integer, Map<Integer, List<Integer>>> scheduleMap = schedule.getSchedule();
    
        for (Map.Entry<Integer, Map<Integer, List<Integer>>> ucEntry : scheduleMap.entrySet()) {
            int ucId = ucEntry.getKey();
            // Only assign the UCs the student is enrolled in
            if (!student.getUCs().contains(ucId)) continue;
    
            Map<Integer, List<Integer>> shiftsMap = ucEntry.getValue();
            for (Map.Entry<Integer, List<Integer>> shiftEntry : shiftsMap.entrySet()) {
                int shiftId = shiftEntry.getKey();    
                Shift shift = shifts.get(shiftId);
    
                // Check if the shift has capacity
                if (shift.hasCapacity()) {
                    // Increase the shift capacity
                    shift.addStudent();
    
                    // Assign the shifts to the student
                    finalSchedule.computeIfAbsent(ucId, k -> new ArrayList<>()).add(shiftId);
                }
            }
        }
    }

    /**
     * Imports students from a CSV file.
     *
     * @param path Path to the CSV file containing student data.
     * @return List of imported students.
     * @throws IOException if an error occurs while reading the file.
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

                String password = Student.generateRandomPassword();

                Student student = createStudentByType(studentId, password, this.getId(), ucs, type);

                students.add(student);
            }
        }
        return students;
    }

    /**
     * Creates a student instance based on the specified type.
     *
     * @param id Student's ID.
     * @param password Generated password for the student.
     * @param course Course ID the student is enrolled in.
     * @param ucs List of UCs the student is enrolled in.
     * @param type Type of the student (e.g., regular, athlete, employed).
     * @return Instance of the created student.
     */
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

    /**
     * Sets the visibility of schedules to true, making them public.
     *
     * @return True if the visibility is updated successfully.
     */
    public boolean postSchedule() {
        setVisibilitySchedules(true);
        return true;
    }

    /**
     * Imports UCs from a JSON file.
     *
     * @param path Path to the JSON file containing UC data.
     * @return List of imported UCs.
     * @throws IllegalArgumentException if the file does not exist or an error occurs while parsing the JSON.
     */
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

    /**
     * Imports shifts and time tables from a JSON file.
     *
     * @param year Year for which the time table is being imported.
     * @param path Path to the JSON file containing time table data.
     * @return List of imported shifts.
     * @throws IllegalArgumentException if the file does not exist or an error occurs while parsing the JSON.
     */
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
                for (JsonObject slot : slots.getValuesAs(JsonObject.class)) {
                    int idSlot = slot.getInt("id");
                    String day = slot.getString("day");
                    DayOfWeek weekday = DayOfWeek.valueOf(day.toUpperCase());
                    String start = slot.getString("start");
                    String end = slot.getString("end");
                    Time startTime = Time.valueOf(start + ":00");
                    Time endTime = Time.valueOf(end + ":00");
                    TimeSlot timeSlot = new TimeSlot(idSlot, startTime, endTime, weekday, id);
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

    /**
     * Adds a student to the course.
     *
     * @param idStudent ID of the student.
     * @param idCourse ID of the course.
     * @param ucs List of UCs the student is enrolled in.
     * @param type Type of the student (e.g., regular, athlete, employed).
     * @return Instance of the added student.
     */
    public Student addStudent(int idStudent, int idCourse, List<Integer> ucs, int type) {
        return createStudentByType(idStudent, Student.generateRandomPassword(), idCourse, ucs, type);
    }

    /**
     * Imports predefined schedules from a JSON file.
     *
     * @param path Path to the JSON file containing predefined schedule data.
     * @return List of imported predefined schedules.
     * @throws IllegalArgumentException if the file does not exist or an error occurs while parsing the JSON.
     */
    public List<PreDefinedSchedule> importSchedulesPreDefined(String path) {
        try {
            // Carregar o arquivo JSON
            File file = new File(path);
            if (!file.exists() || file.isDirectory()) {
                throw new IllegalArgumentException("O arquivo não existe ou é um diretório");
            }

            List<PreDefinedSchedule> preDefinedSchedules = new ArrayList<>();

            // Ler e processar o JSON
            try (BufferedReader br = new BufferedReader(new FileReader(path))) {
                JsonReader jsonReader = Json.createReader(br);
                JsonArray jsonArray = jsonReader.readArray();

                for (JsonObject scheduleObject : jsonArray.getValuesAs(JsonObject.class)) {
                    int scheduleId = scheduleObject.getInt("id");
                    int scheduleYear = scheduleObject.getInt("year");
                    int no_conflicts = scheduleObject.getInt("no_conflicts");

                    Map<Integer, Map<Integer, List<Integer>>> scheduleMap = new HashMap<>();

                    JsonArray scheduleArray = scheduleObject.getJsonArray("schedule");
                    for (JsonObject entry : scheduleArray.getValuesAs(JsonObject.class)) {
                        int ucId = entry.getInt("uc");
                        int shiftId = entry.getInt("shift");
                        int timeSlotId = entry.getInt("timeslot");

                        scheduleMap
                            .computeIfAbsent(ucId, k -> new HashMap<>())
                            .computeIfAbsent(shiftId, k -> new ArrayList<>())
                            .add(timeSlotId);
                    }

                    PreDefinedSchedule preDefinedSchedule = new PreDefinedSchedule(scheduleId, scheduleYear, no_conflicts, scheduleMap);
                    preDefinedSchedules.add(preDefinedSchedule);
                }

                return preDefinedSchedules;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}