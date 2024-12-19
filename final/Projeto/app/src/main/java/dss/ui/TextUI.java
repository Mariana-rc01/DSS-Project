package dss.ui;

import java.util.Arrays;

public class TextUI {
    private Menu menuPrincipal;
    private Menu menuGestaoAlunos;
    private Menu menuGestaoCursos;
    private Menu menuGestaoUcs;

    public TextUI() {
        initMenus();
    }

    public void run() {
        System.out.println("Bem-vindo ao Sistema de Gestão de Cursos e Horários!");
        menuPrincipal.run();
        System.out.println("Até breve...");
    }

    private void initMenus() {
        menuPrincipal = new Menu("Menu Principal", Arrays.asList(
                "Gestão de Alunos",
                "Gestão de Cursos",
                "Gestão de UC",
                "Atribuir Aluno a UC",
                "Remover Aluno de UC",
                "Listar Alunos de UC"
        ));

        menuPrincipal.setHandler(1, this::gestaoDeAlunos);
        menuPrincipal.setHandler(2, this::gestaoDeCursos);
        menuPrincipal.setHandler(3, this::gestaoDeUcs);
        menuPrincipal.setHandler(4, this::atribuirAlunoAUC);
        menuPrincipal.setHandler(5, this::removerAlunoDeUC);
        menuPrincipal.setHandler(6, this::listarAlunosDeUC);

        menuGestaoAlunos = new Menu("Gestão de Alunos", Arrays.asList(
                "Adicionar Aluno",
                "Consultar Aluno",
                "Listar Alunos"
        ));

        menuGestaoAlunos.setHandler(1, this::adicionarAluno);
        menuGestaoAlunos.setHandler(2, this::consultarAluno);
        menuGestaoAlunos.setHandler(3, this::listarAlunos);

        menuGestaoCursos = new Menu("Gestão de Cursos", Arrays.asList(
                "Adicionar Curso",
                "Listar Cursos"
        ));

        menuGestaoCursos.setHandler(1, this::adicionarCurso);
        menuGestaoCursos.setHandler(2, this::listarCursos);

        menuGestaoUcs = new Menu("Gestão de UC", Arrays.asList(
                "Adicionar UC",
                "Listar UCs"
        ));

        menuGestaoUcs.setHandler(1, this::adicionarUC);
        menuGestaoUcs.setHandler(2, this::listarUCs);
    }

    private void gestaoDeAlunos() {
        menuGestaoAlunos.run();
    }

    private void gestaoDeCursos() {
        menuGestaoCursos.run();
    }

    private void gestaoDeUcs() {
        menuGestaoUcs.run();
    }

    private void adicionarAluno() {
        System.out.println("Adicionar Aluno");
    }

    private void consultarAluno() {
        System.out.println("Consultar Aluno");
    }

    private void listarAlunos() {
        System.out.println("Listar Alunos");
    }

    private void adicionarCurso() {
        System.out.println("Adicionar Curso");
    }

    private void listarCursos() {
        System.out.println("Listar Cursos");
    }

    private void adicionarUC() {
        System.out.println("Adicionar UC");
    }

    private void listarUCs() {
        System.out.println("Listar UCs");
    }

    private void atribuirAlunoAUC() {
        System.out.println("Atribuir Aluno a UC");
    }

    private void removerAlunoDeUC() {
        System.out.println("Remover Aluno de UC");
    }

    private void listarAlunosDeUC() {
        System.out.println("Listar Alunos de UC");
    }
}
