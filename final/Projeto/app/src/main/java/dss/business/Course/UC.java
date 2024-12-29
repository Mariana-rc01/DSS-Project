package dss.business.Course;

import java.util.List;

public class UC {

    private int id;
    private String name;
    private int year;
    private int semester;
    private String policyPreference;
    private List<Shift> shifts;

    // Construtor
    public UC(int id, String name, int year, int semester, String policyPreference) {
        this.id = id;
        this.name = name;
        this.year = year;
        this.semester = semester;
        this.policyPreference = policyPreference;
    }

    public UC(int id, String name, int year, int semester, String policyPreference, List<Shift> shifts) {
        this.id = id;
        this.name = name;
        this.year = year;
        this.semester = semester;
        this.policyPreference = policyPreference;
        this.shifts = shifts;
    }

    // Getters e Setters
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