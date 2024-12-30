package dss;

import dss.business.LNFacade;
import dss.ui.TextUI;
import dss.data.StudentDAO; // Propósito de teste

public class Main {

    static LNFacade facade = new LNFacade();
    public static void main(String[] args) {
        try {
            StudentDAO studentDAO = new StudentDAO(); // Propósito de teste
            int size = studentDAO.size(); // Propósito de teste
            System.out.println("Number of students: " + size); // Propósito de teste
            boolean b = facade.sendEmails(1);
        TextUI ui = new TextUI();
        ui.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
