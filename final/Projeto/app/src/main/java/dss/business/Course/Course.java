package dss.business.Course;

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

    /*
    // Mariana
    public boolean sendEmail(String to, String subject, String body){
        
    }
    */

    /*
    // Mariana
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
    // Mariana
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