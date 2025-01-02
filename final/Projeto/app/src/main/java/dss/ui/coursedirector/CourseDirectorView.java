package dss.ui.coursedirector;

import dss.ui.Menu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import dss.business.LNFacade;
import dss.business.User.Student;

public class CourseDirectorView {
    private final LNFacade lnFacade;
    private Menu menuPrincipal;

    public CourseDirectorView(LNFacade facade) {
        this.lnFacade = facade;
    }

    public Menu initMenu() {
        menuPrincipal = new Menu("Menu Principal", Arrays.asList(
                "Adicionar Aluno",
                "Consultar Aluno",
                "Listar Alunos com Conflitos de Horário",
                "Listar Alunos sem Horário",
                "Publicar Horários",
                "Enviar Emails",
                "Consultar Horário de Aluno",
                "Registar Horário de Aluno",
                "Registrar Política de UC",
                "Gerar Horários",
                "Importar Alunos",
                "Importar UCs",
                "Importar Turnos",
                "Importar Horários",
                "Importar Horários Pré-Definidos"
        ));

        menuPrincipal.setHandler(1, this::adicionarAluno);
        menuPrincipal.setHandler(2, this::consultarAluno);
        menuPrincipal.setHandler(3, this::listarAlunosComConflitos);
        menuPrincipal.setHandler(4, this::listarAlunosSemHorario);
        menuPrincipal.setHandler(5, this::publicarHorarios);
        menuPrincipal.setHandler(6, this::enviarEmails);
        menuPrincipal.setHandler(7, this::consultarHorarioAluno);
        menuPrincipal.setHandler(8, this::registarHorarioAluno);
        menuPrincipal.setHandler(9, this::registrarPolitica);
        menuPrincipal.setHandler(10, this::gerarHorarios);
        menuPrincipal.setHandler(11, this::importarAlunos);
        menuPrincipal.setHandler(12, this::importarUCs);
        menuPrincipal.setHandler(13, this::importarTurnos);
        menuPrincipal.setHandler(14, this::importarHorarios);
        menuPrincipal.setHandler(15, this::importarHorariosPreDefinidos);

        return menuPrincipal;
    }

    private void adicionarAluno() {
        Scanner sc = new Scanner(System.in);
        System.out.print("ID do Aluno: ");
        int id = sc.nextInt();
        System.out.print("ID do Curso: ");
        int idCurso = sc.nextInt();
        if (lnFacade.addStudent(id, idCurso)) {
            System.out.println("Aluno adicionado com sucesso!");
        } else {
            System.out.println("Erro ao adicionar aluno.");
        }
    }

    private void consultarAluno() {
        Scanner sc = new Scanner(System.in);
        System.out.print("ID do Aluno: ");
        int id = sc.nextInt();
        try {
            Student student = lnFacade.getStudent(id);
            System.out.println("Detalhes do Aluno: \n");
            System.out.println("ID: " + student.getId());
            System.out.println("Email: " + student.getEmail());
            System.out.println("Password: " + student.getPassword());
            System.out.println("Curso: " + student.getCourse());
        } catch (Exception e) {
            System.out.println("Erro ao consultar aluno: " + e.getMessage());
        }
    }

    private void listarAlunosComConflitos() {
        Scanner sc = new Scanner(System.in);
        System.out.print("ID do Curso: ");
        int idCurso = sc.nextInt();
        List<Integer> alunosComConflito = lnFacade.getStudentsWithScheduleConflicts(idCurso);
        System.out.println("Alunos com conflitos: " + alunosComConflito);
    }

    private void registrarPolitica() {
        Scanner sc = new Scanner(System.in);
        System.out.print("ID do Curso: ");
        int idCurso = sc.nextInt();
        System.out.print("ID da UC: ");
        int idUC = sc.nextInt();
        System.out.print("Política: ");
        String politica = sc.next();
        if (lnFacade.registerPolicyOption(idCurso, idUC, politica)) {
            System.out.println("Política registrada com sucesso!");
        } else {
            System.out.println("Erro ao registrar política.");
        }
    }

    private void importarAlunos() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Caminho do arquivo: ");
        String path = sc.next();
        System.out.print("ID do Curso: ");
        int idCurso = sc.nextInt();
        try {
            if (lnFacade.importStudents(path, idCurso)) {
                System.out.println("Alunos importados com sucesso!");
            } else {
                System.out.println("Erro ao importar alunos.");
            }
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void importarUCs() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Caminho do arquivo: ");
        String path = sc.next();
        System.out.print("ID do Curso: ");
        int idCurso = sc.nextInt();
        if (lnFacade.importUCs(path, idCurso)) {
            System.out.println("UCs importadas com sucesso!");
        } else {
            System.out.println("Erro ao importar UCs.");
        }
    }

    private void importarTurnos() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Caminho do arquivo: ");
        String path = sc.next();
        System.out.print("ID do Curso: ");
        int idCurso = sc.nextInt();
        if (lnFacade.importTimeTable(idCurso, 2025, path)) {
            System.out.println("Turnos importados com sucesso!");
        } else {
            System.out.println("Erro ao importar Turnos.");
        }
    }

    private void importarHorarios() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Caminho do arquivo: ");
        String path = sc.next();
        System.out.print("ID do Curso: ");
        int idCurso = sc.nextInt();
        if (lnFacade.importSchedulesPreDefined(idCurso, path)) {
            System.out.println("Horários importados com sucesso!");
        } else {
            System.out.println("Erro ao importar Horários.");
        }
    }

    private void gerarHorarios() {
        Scanner sc = new Scanner(System.in);
        System.out.print("ID do Curso: ");
        int idCurso = sc.nextInt();
        lnFacade.generateSchedule(idCurso);
        System.out.println("Horários gerados com sucesso!");
    }

    private void listarAlunosSemHorario() {
        Scanner sc = new Scanner(System.in);
        System.out.print("ID do Curso: ");
        int idCurso = sc.nextInt();
        List<Student> alunosSemHorario = lnFacade.getStudentsWithoutSchedule(idCurso);
        System.out.println("Alunos sem horário: \n");
        for (Student s : alunosSemHorario) {
            System.out.println("Aluno: " + s.getEmail());
        }
    }

    private void publicarHorarios() {
        Scanner sc = new Scanner(System.in);
        System.out.print("ID do Curso: ");
        int idCurso = sc.nextInt();
        if (lnFacade.postSchedule(idCurso)) {
            System.out.println("Horários publicados com sucesso!");
        } else {
            System.out.println("Erro ao publicar horários.");
        }
    }

    private void enviarEmails() {
        Scanner sc = new Scanner(System.in);
        System.out.print("ID do Curso: ");
        int idCurso = sc.nextInt();
        if (lnFacade.sendEmails(idCurso)) {
            System.out.println("Emails enviados com sucesso!");
        } else {
            System.out.println("Erro ao enviar emails.");
        }
    }

    private void consultarHorarioAluno() {
        Scanner sc = new Scanner(System.in);
        System.out.print("ID do Aluno: ");
        int idAluno = sc.nextInt();
        System.out.print("ID do Curso: ");
        int idCurso = sc.nextInt();
        System.out.println(lnFacade.getStudentSchedule(idAluno, idCurso));
    }

    private void registarHorarioAluno() {
        Scanner sc = new Scanner(System.in);

        System.out.print("ID do Aluno: ");
        int idAluno = sc.nextInt();

        System.out.print("ID do Curso: ");
        int idCurso = sc.nextInt();

        System.out.println("Informe os horários para o aluno (separados por espaço, por exemplo: 1 2 3 4): ");
        String input = sc.nextLine();  

        List<Integer> horarios = new ArrayList<>();
        String[] horarioStrings = input.split("\\s+");

        try {
            for (String h : horarioStrings) {
                horarios.add(Integer.parseInt(h));
            }
        } catch (NumberFormatException e) {
            System.out.println("Erro: Horário inválido. Por favor, insira apenas números.");
            return;
        }

        Map<Integer, List<Integer>> schedule = new HashMap<Integer, List<Integer>>();
        schedule.put(idCurso, horarios);

        if (lnFacade.registerSchedule(idAluno, schedule)) {
            System.out.println("Horário registrado com sucesso!");
        } else {
            System.out.println("Erro ao registrar horário.");
        }
    }

    private void importarHorariosPreDefinidos() {
        Scanner sc = new Scanner(System.in);
        System.out.print("ID do Curso: ");
        int idCurso = sc.nextInt();
        System.out.print("Caminho do arquivo: ");
        String path = sc.next();
        if (lnFacade.importSchedulesPreDefined(idCurso, path)) {
            System.out.println("Horários importados com sucesso!");
        } else {
            System.out.println("Erro ao importar horários.");
        }
    }
}
