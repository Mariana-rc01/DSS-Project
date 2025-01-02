package dss.ui;

import java.util.*;

import dss.business.LNFacade;
import dss.ui.coursedirector.CourseDirectorView;
import dss.ui.student.StudentView;

public class TextUI {
    private CourseDirectorView courseDirectorView;
    private StudentView studentView;
    private final LNFacade lnFacade;

    public TextUI(LNFacade facade) {
        this.lnFacade = facade;
        this.courseDirectorView = new CourseDirectorView(facade);
        this.studentView = new StudentView(facade);
    }

    public void run() throws Exception {
        int userId = login();
        if (userId != -1) {
            System.out.println("Bem-vindo ao Sistema de Gestão de Cursos e Horários!");
            // Get user type
            switch (lnFacade.getUserType(userId)) {
                case 1:
                    System.out.println("Bem-vindo, Aluno!");
                    studentView.setStudent(lnFacade.getStudent(userId));
                    studentView.setCourseId(lnFacade.getCourseId(userId));
                    Menu menuStudent = studentView.initMenu();
                    menuStudent.run();
                    break;
                case 2:
                    System.out.println("Bem-vindo, Diretor de Curso!");
                    courseDirectorView.setCourseId(lnFacade.getCourseId(userId));
                    Menu menuDirector = courseDirectorView.initMenu();
                    menuDirector.run();
                    break;
            }

            System.out.println("Até breve...");
        } else {
            System.out.println("Login falhou. Até breve...");
        }
    }

    private int login() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Id de Utilizador: ");
        int userId = sc.nextInt();
        System.out.print("Senha: ");
        String password = sc.next();
        if (lnFacade.verifyIdentity(userId) && lnFacade.verifyPassword(userId, password)) {
            return userId;
        } else {
            return -1;
        }
    }
}
