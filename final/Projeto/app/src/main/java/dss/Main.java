package dss;

import dss.ui.TextUI;
import dss.data.StudentDAO; // Propósito de teste

public class Main {
    public static void main(String[] args) {
        try {
            StudentDAO studentDAO = new StudentDAO(); // Propósito de teste
            int size = studentDAO.size(); // Propósito de teste
            System.out.println("Number of students: " + size); // Propósito de teste
            TextUI ui = new TextUI();
            ui.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
