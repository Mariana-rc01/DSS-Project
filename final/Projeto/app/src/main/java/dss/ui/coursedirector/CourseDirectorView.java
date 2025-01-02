package dss.ui.coursedirector;

import dss.ui.Menu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import dss.business.LNFacade;
import dss.business.Course.Shift;
import dss.business.Course.TimeSlot;
import dss.business.Course.UC;
import dss.business.User.Student;

public class CourseDirectorView {
    private final LNFacade lnFacade;
    private Menu menuPrincipal;
    private int courseId;

    public CourseDirectorView(LNFacade facade) {
        this.lnFacade = facade;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
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
                "Importar Horários"));

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

        return menuPrincipal;
    }

    private void adicionarAluno() {
        Scanner sc = new Scanner(System.in);
        System.out.print("ID do Aluno: ");
        int id = sc.nextInt();
        sc.nextLine();
        System.out.print("Lista de UCs (Separadas por espaço) (e.g: 1 2 3 4 5 6): ");
        String input = sc.nextLine();
        List<Integer> ucs = new ArrayList<>();
        String[] ucStrings = input.split("\\s+");
        try {
            for (String uc : ucStrings) {
                ucs.add(Integer.parseInt(uc));
            }
        } catch (NumberFormatException e) {
            System.out.println("Erro: UC inválida. Por favor, insira apenas números.");
            return;
        }
        if (lnFacade.addStudent(id, courseId, ucs)) {
            System.out.println("Aluno adicionado com sucesso!");
        } else {
            System.out.println("Erro ao adicionar aluno.");
        }
    }

    private void consultarAluno() {
        Scanner sc = new Scanner(System.in);
        System.out.print("ID do Aluno: ");
        int id = sc.nextInt();
        Student student;
        try {
            student = lnFacade.getStudent(id);
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
            return;
        }
        if (student != null) {
            System.out.println("Detalhes do Aluno: \n");
            System.out.println("ID: " + student.getId());
            System.out.println("Email: " + student.getEmail());
            System.out.println("Password: " + student.getPassword());
            System.out.println("Curso: " + student.getCourse());
        } 
        else {
            System.out.println("Aluno não encontrado.");
        }

    }

    private void listarAlunosComConflitos() {
        List<Integer> alunosComConflito = lnFacade.getStudentsWithScheduleConflicts(courseId);
        System.out.println("Alunos com conflitos: " + alunosComConflito);
    }

    private void registrarPolitica() {
        Scanner sc = new Scanner(System.in);
        System.out.print("ID da UC: ");
        int idUC = sc.nextInt();
        System.out.print("Política: ");
        String politica = sc.next();
        if (lnFacade.registerPolicyOption(courseId, idUC, politica)) {
            System.out.println("Política registrada com sucesso!");
        } else {
            System.out.println("Erro ao registrar política.");
        }

    }

    private void importarAlunos() {
        System.out.println("Aviso: As UCs já devem estar importadas antes de executar esta operação.");
        Scanner sc = new Scanner(System.in);
        System.out.print("Caminho do arquivo: ");
        String path = sc.next();
        try {
            if (lnFacade.importStudents(path, courseId)) {
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
        if (lnFacade.importUCs(path, courseId)) {
            System.out.println("UCs importadas com sucesso!");
        } else {
            System.out.println("Erro ao importar UCs.");
        }

    }

    private void importarTurnos() {
        System.out.println("Aviso: As UCs já devem estar importadas antes de executar esta operação.");
        Scanner sc = new Scanner(System.in);
        System.out.print("Caminho do arquivo: ");
        String path = sc.next();
        if (lnFacade.importTimeTable(courseId, 2025, path)) {
            System.out.println("Turnos importados com sucesso!");
        } else {
            System.out.println("Erro ao importar Turnos.");
        }

    }

    private void importarHorarios() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Caminho do arquivo: ");
        String path = sc.next();
        if (lnFacade.importSchedulesPreDefined(courseId, path)) {
            System.out.println("Horários importados com sucesso!");
        } else {
            System.out.println("Erro ao importar Horários.");
        }
    }

    private void gerarHorarios() {
        lnFacade.generateSchedule(courseId);
        System.out.println("Horários gerados com sucesso!");
    }

    private void listarAlunosSemHorario() {
        List<Student> alunosSemHorario = lnFacade.getStudentsWithoutSchedule(courseId);
        System.out.println("Alunos sem horário: \n");
        for (Student s : alunosSemHorario) {
            System.out.println("Aluno: " + s.getEmail());
        }
    }

    private void publicarHorarios() {
        if (lnFacade.postSchedule(courseId)) {
            System.out.println("Horários publicados com sucesso!");
        } else {
            System.out.println("Erro ao publicar horários.");
        }
    }

    private void enviarEmails() {
        if (lnFacade.sendEmails(courseId)) {
            System.out.println("Emails enviados com sucesso!");
        } else {
            System.out.println("Erro ao enviar emails.");
        }
    }

    private void consultarHorarioAluno() {
        Scanner sc = new Scanner(System.in);
        System.out.print("ID do Aluno: ");
        int idAluno = sc.nextInt();
        Map<UC, Map<Shift, List<TimeSlot>>> horario = lnFacade.getStudentSchedule(idAluno, courseId);
        if (horario == null) {
            System.out.println("Erro: Aluno não encontrado.");
            return;
        }
        for (UC uc : horario.keySet()) {
            System.out.println(uc.getName());
            for (Shift shift : horario.get(uc).keySet()) {
                for (TimeSlot timeSlot : horario.get(uc).get(shift)) {
                    System.out.println("\t\t" + timeSlot.getWeekDay() + " - " + timeSlot.getTimeStart() + " - "
                            + timeSlot.getTimeEnd());
                }
            }
        }

    }

    private void registarHorarioAluno() {
        Scanner sc = new Scanner(System.in);

        System.out.print("ID do Aluno: ");
        int idAluno = sc.nextInt();
        sc.nextLine();
        System.out.print("Quantas UCs deseja adicionar ao horário? ");
        int numUCs = sc.nextInt();
        sc.nextLine();

        Map<Integer, List<Integer>> schedule = new HashMap<>();

        for (int i = 0; i < numUCs; i++) {
            System.out.print("ID da UC: ");
            int idUC = sc.nextInt();
            sc.nextLine();
            System.out.print("Informe os turnos para a UC (separados por espaço, por exemplo: 1 2 3 4): ");
            String input = sc.nextLine();
            List<Integer> shifts = new ArrayList<>();
            String[] shiftStrings = input.split("\\s+");
            try {
                for (String s : shiftStrings) {
                    shifts.add(Integer.parseInt(s));
                }
            } catch (NumberFormatException e) {
                System.out.println("Erro: Turno inválido. Por favor, insira apenas números.");
                return;
            }
            schedule.put(idUC, shifts);
        }

        if (lnFacade.registerSchedule(idAluno, schedule)) {
            System.out.println("Horário registrado com sucesso!");
        } else {
            System.out.println("Erro ao registrar horário.");
        }
    }
}
