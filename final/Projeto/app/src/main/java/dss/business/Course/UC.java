package dss.business.Course;

import java.util.List;

public class UC {

    private int id;
    private String name;
    private int year;
    private int semester;
    private String policyPreference;
    private List<Shift> shifts;
    private int idCourse;

    // Construtor
    public UC(int id, String name, int year, int semester, String policyPreference, int idCourse) {
        this.id = id;
        this.name = name;
        this.year = year;
        this.semester = semester;
        this.policyPreference = policyPreference;
        this.idCourse = idCourse;
    }

    public UC(int id, String name, int year, int semester, String policyPreference, List<Shift> shifts, int idCourse) {
        this.id = id;
        this.name = name;
        this.year = year;
        this.semester = semester;
        this.policyPreference = policyPreference;
        this.shifts = shifts;
        this.idCourse = idCourse;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdCourse() {
        return idCourse;
    }

    public void setIdCourse(int idCourse) {
        this.idCourse = idCourse;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public String getPolicyPreference() {
        return policyPreference;
    }

    public void setPolicyPreference(String policyPreference) {
        this.policyPreference = policyPreference;
    }

    /*
    public void addShift(Shift shift){
        
    }
    */

    /*
    public boolean addStudentToShift(List<Integer> mapShifts){
        
    }
    */
    
    /*
    public boolean removeStudentFromShift(List<Integer> mapShifts){
        
    }
    */
}