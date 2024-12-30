package dss;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dss.business.LNFacade;
import dss.business.User.Student;
import dss.ui.TextUI;
import dss.data.StudentDAO; // Propósito de teste

public class Main {

    static LNFacade facade = new LNFacade();
    public static void main(String[] args) {
        try {
            StudentDAO studentDAO = new StudentDAO(); // Propósito de teste
            int size = studentDAO.size(); // Propósito de teste
            System.out.println("Number of students: " + size); // Propósito de teste

            // Testar sendEmails
            boolean b = facade.sendEmails(1);

            // Testar registerSchedule
            Map<Integer, List<Integer>> schedule = new HashMap<>();

            List<Integer> shiftsForUC101 = Arrays.asList(1, 2);
            List<Integer> shiftsForUC102 = Arrays.asList(3,2);

            schedule.put(1, shiftsForUC101);
            schedule.put(2, shiftsForUC102);

            int studentId = 101;
            boolean result = facade.registerSchedule(studentId, schedule);
            if (result) {
                System.out.println("Horário registrado com sucesso para o estudante " + studentId);
            } else {
                System.out.println("Falha ao registrar horário para o estudante " + studentId);
            }
            try {
                Student student = facade.getStudent(studentId);
                System.out.println("Horários do estudante " + studentId + ": " + student.getSchedule());
            } catch (Exception e) {
                System.out.println("Erro ao recuperar o estudante: " + e.getMessage());
            }

            TextUI ui = new TextUI();
            ui.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
