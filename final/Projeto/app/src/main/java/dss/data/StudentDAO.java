package dss.data;

import java.sql.*;

import dss.business.User.AthleteStudent;
import dss.business.User.EmployedStudent;
import dss.business.User.Student;

public class StudentDAO {

    public boolean addStudent(Student student) throws Exception {
        int type = student.getType();
        if (type < 0 || type > 2) {
            throw new Exception("Tipo de estudante inválido: " + type);
        }
    
        try (PreparedStatement stm = DAOConfig.connection.prepareStatement(
                "INSERT INTO students (id, password, type, course) VALUES (?, ?, ?, ?)")) {
            stm.setInt(1, student.getId());
            stm.setString(2, student.getPassword());
            stm.setInt(3, type);
            stm.setInt(4, student.getCourse());
            stm.executeUpdate();
            return true;
        } catch (SQLException e) {
            throw new Exception("Erro ao adicionar estudante: " + e.getMessage());
        }
    }
    

    public Student getStudent(int id) throws Exception {
        try (PreparedStatement stm = DAOConfig.connection.prepareStatement("SELECT * FROM students WHERE id = ?")) {
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                int type = rs.getInt("type");
            switch (type) {
                case 0: // Estudante normal
                    return new Student(
                            rs.getInt("id"),
                            rs.getString("password"),
                            rs.getInt("course")
                    );
                case 1: // Estudante atleta
                    return new AthleteStudent(
                            rs.getInt("id"),
                            rs.getString("password"),
                            rs.getInt("course")
                    );
                case 2: // Estudante trabalhador
                    return new EmployedStudent(
                            rs.getInt("id"),
                            rs.getString("password"),
                            rs.getInt("course")
                    );
                default:
                    throw new Exception("Tipo de estudante desconhecido: " + type);
            }
            }
            return null;
        } catch (SQLException e) {
            throw new Exception("Erro ao obter estudante: " + e.getMessage());
        }
    }

    public void removeStudent(int id) throws Exception {
        try (PreparedStatement stm = DAOConfig.connection.prepareStatement("DELETE FROM students WHERE id = ?")) {
            stm.setInt(1, id);
            stm.executeUpdate();
        } catch (SQLException e) {
            throw new Exception("Erro ao remover estudante: " + e.getMessage());
        }
    }

    public int size(){
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
}