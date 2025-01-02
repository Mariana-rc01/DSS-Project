package dss.ui.student;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import dss.business.LNFacade;
import dss.business.Course.Shift;
import dss.business.Course.TimeSlot;
import dss.business.Course.UC;
import dss.business.User.Student;
import dss.ui.Menu;

public class StudentView {
    private final LNFacade lnFacade;
    private Menu menuPrincipal;
    private Student student;
    private int courseId;

    public StudentView(LNFacade facade) {
        this.lnFacade = facade;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public Menu initMenu() {
        menuPrincipal = new Menu("Menu Principal", Arrays.asList(
                "Consultar Horário",
                "Exportar Horário"
        ));

        menuPrincipal.setHandler(1, this::consultarHorario);
        menuPrincipal.setHandler(2, this::exportarHorario);

        return menuPrincipal;
    }

    private void consultarHorario() {
        Map<UC, Map<Shift,List<TimeSlot>>> horario = lnFacade.getStudentSchedule(student.getId(), courseId);
        if(horario == null) {
            System.out.println("Ainda não tem horário definido.");
            return;
        }
        for (UC uc : horario.keySet()) {
            System.out.println(uc.getName());
            for (Shift shift : horario.get(uc).keySet()) {
                for (TimeSlot timeSlot : horario.get(uc).get(shift)) {
                    System.out.println("\t\t" + timeSlot.getWeekDay() + " - " + timeSlot.getTimeStart() + " - " + timeSlot.getTimeEnd());
                }
            }
        }
    }

    private void exportarHorario() {
        System.out.print("Nome do ficheiro: ");
        Scanner sc = new Scanner(System.in);
        String filename = sc.nextLine();
        if (lnFacade.exportSchedule(student.getId(), filename)) {
            System.out.println("Horário exportado com sucesso!");
        } else {
            System.out.println("Erro ao exportar horário.");
        }
    }
}
