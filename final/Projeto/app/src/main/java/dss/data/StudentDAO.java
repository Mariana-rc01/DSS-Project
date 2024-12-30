package dss.data;

import java.sql.*;
import java.util.*;

import dss.business.User.AthleteStudent;
import dss.business.User.EmployedStudent;
import dss.business.User.Student;

public class StudentDAO {

    public boolean addStudent(Student student) throws Exception {
        int type = student.getType();
        if (type < 0 || type > 2) {
            throw new Exception("Tipo de estudante inválido: " + type);
        }

        try {
            DAOConfig.connection.setAutoCommit(false);

            try (PreparedStatement stm = DAOConfig.connection.prepareStatement(
                    "INSERT INTO students (id, password, type, course) VALUES (?, ?, ?, ?)")) {
                stm.setInt(1, student.getId());
                stm.setString(2, student.getPassword());
                stm.setInt(3, type);
                stm.setInt(4, student.getCourse());
                stm.executeUpdate();
            }

            for (int ucId : student.getUCs()) {
                try (PreparedStatement stm = DAOConfig.connection.prepareStatement(
                        "INSERT INTO student_ucs (student_id, uc_id) VALUES (?, ?)")) {
                    stm.setInt(1, student.getId());
                    stm.setInt(2, ucId);
                    stm.executeUpdate();
                }
            }

            for (Map.Entry<Integer, List<Integer>> entry : student.getSchedule().entrySet()) {
                int ucId = entry.getKey();
                for (int shiftId : entry.getValue()) {
                    try (PreparedStatement stm = DAOConfig.connection.prepareStatement(
                            "INSERT INTO student_schedule (student_id, uc_id, shift_id) VALUES (?, ?, ?)")) {
                        stm.setInt(1, student.getId());
                        stm.setInt(2, ucId);
                        stm.setInt(3, shiftId);
                        stm.executeUpdate();
                    }
                }
            }

            DAOConfig.connection.commit();
            return true;

        } catch (SQLException e) {
            DAOConfig.connection.rollback();
            throw new Exception("Erro ao adicionar estudante: " + e.getMessage());
        } finally {
            DAOConfig.connection.setAutoCommit(true);
        }
    }

    public Student getStudent(int id) throws Exception {
        try {
            Student student = null;
            try (PreparedStatement stm = DAOConfig.connection.prepareStatement("SELECT * FROM students WHERE id = ?")) {
                stm.setInt(1, id);
                ResultSet rs = stm.executeQuery();
                if (rs.next()) {
                    int type = rs.getInt("type");
                    switch (type) {
                        case 0:
                            student = new Student(
                                    rs.getInt("id"),
                                    rs.getString("password"),
                                    rs.getInt("course")
                            );
                            break;
                        case 1:
                            student = new AthleteStudent(
                                    rs.getInt("id"),
                                    rs.getString("password"),
                                    rs.getInt("course")
                            );
                            break;
                        case 2:
                            student = new EmployedStudent(
                                    rs.getInt("id"),
                                    rs.getString("password"),
                                    rs.getInt("course")
                            );
                            break;
                        default:
                            throw new Exception("Tipo de estudante desconhecido: " + type);
                    }
                }
            }

            if (student == null) return null;

            List<Integer> ucs = new ArrayList<>();
            try (PreparedStatement stm = DAOConfig.connection.prepareStatement(
                    "SELECT uc_id FROM student_ucs WHERE student_id = ?")) {
                stm.setInt(1, id);
                ResultSet rs = stm.executeQuery();
                while (rs.next()) {
                    ucs.add(rs.getInt("uc_id"));
                }
            }
            student.setUCs(ucs);

            Map<Integer, List<Integer>> schedule = new HashMap<>();
            try (PreparedStatement stm = DAOConfig.connection.prepareStatement(
                    "SELECT uc_id, shift_id FROM student_schedule WHERE student_id = ?")) {
                stm.setInt(1, id);
                ResultSet rs = stm.executeQuery();
                while (rs.next()) {
                    int ucId = rs.getInt("uc_id");
                    int shiftId = rs.getInt("shift_id");
                    schedule.computeIfAbsent(ucId, k -> new ArrayList<>()).add(shiftId);
                }
            }
            student.setSchedule(schedule);

            return student;

        } catch (SQLException e) {
            throw new Exception("Erro ao obter estudante: " + e.getMessage());
        }
    }

    public void removeStudent(int id) throws Exception {
        try {
            DAOConfig.connection.setAutoCommit(false);

            try (PreparedStatement stm = DAOConfig.connection.prepareStatement(
                    "DELETE FROM student_schedule WHERE student_id = ?")) {
                stm.setInt(1, id);
                stm.executeUpdate();
            }

            try (PreparedStatement stm = DAOConfig.connection.prepareStatement(
                    "DELETE FROM student_ucs WHERE student_id = ?")) {
                stm.setInt(1, id);
                stm.executeUpdate();
            }

            try (PreparedStatement stm = DAOConfig.connection.prepareStatement(
                    "DELETE FROM students WHERE id = ?")) {
                stm.setInt(1, id);
                stm.executeUpdate();
            }

            DAOConfig.connection.commit();

        } catch (SQLException e) {
            DAOConfig.connection.rollback();
            throw new Exception("Erro ao remover estudante: " + e.getMessage());
        } finally {
            DAOConfig.connection.setAutoCommit(true);
        }
    }

    public int size() {
        try (Statement stm = DAOConfig.connection.createStatement()) {
            ResultSet rs = stm.executeQuery("SELECT COUNT(*) FROM students");
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public List<Student> getStudentsByCourse(int course) throws Exception {
        List<Student> students = new ArrayList<>();
        try (PreparedStatement stm = DAOConfig.connection.prepareStatement("SELECT id FROM students WHERE course = ?")) {
            stm.setInt(1, course);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                try {
                    students.add(getStudent(rs.getInt("id")));
                } catch (Exception e) {
                    System.err.println("Erro ao buscar estudante com ID " + rs.getInt("id") + ": " + e.getMessage());
                }
            }
        } catch (SQLException e) {
            throw new Exception("Erro ao buscar estudantes do curso: " + e.getMessage());
        }
        return students;
    }

    public void removeScheduleFromStudent(int id) throws Exception {
        try {
            try (PreparedStatement stm = DAOConfig.connection.prepareStatement(
                    "DELETE FROM student_schedule WHERE student_id = ?")) {
                stm.setInt(1, id);
                stm.executeUpdate();
            }

            try (PreparedStatement stm = DAOConfig.connection.prepareStatement(
                    "SELECT shift_id FROM student_schedule WHERE student_id = ?")) {
                stm.setInt(1, id);
                ResultSet rs = stm.executeQuery();
                while (rs.next()) {
                    int shiftId = rs.getInt("shift_id");
                    // Decrementar a contagem de alunos nos shifts antigos
                    try (PreparedStatement updateShiftStm = DAOConfig.connection.prepareStatement(
                            "UPDATE shifts SET enrolledCount = enrolledCount - 1 WHERE id = ?")) {
                        updateShiftStm.setInt(1, shiftId);
                        updateShiftStm.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            throw new Exception("Erro ao remover horário do estudante: " + e.getMessage());
        }
    }

    public void addScheduleToStudent(int id, Map<Integer, List<Integer>> schedule) throws Exception {
        try {
            for (Map.Entry<Integer, List<Integer>> entry : schedule.entrySet()) {
                int ucId = entry.getKey();
                for (int shiftId : entry.getValue()) {
                    try (PreparedStatement stm = DAOConfig.connection.prepareStatement(
                            "INSERT INTO student_schedule (student_id, uc_id, shift_id) VALUES (?, ?, ?)")) {
                        stm.setInt(1, id);
                        stm.setInt(2, ucId);
                        stm.setInt(3, shiftId);
                        stm.executeUpdate();
                    }

                    // Incrementar a contagem de alunos nos shifts novos
                    try (PreparedStatement updateShiftStm = DAOConfig.connection.prepareStatement(
                            "UPDATE shifts SET enrolledCount = enrolledCount + 1 WHERE id = ?")) {
                        updateShiftStm.setInt(1, shiftId);
                        updateShiftStm.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            throw new Exception("Erro ao adicionar horário ao estudante: " + e.getMessage());
        }
    }
}
