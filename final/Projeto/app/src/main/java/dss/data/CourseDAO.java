package dss.data;

import java.sql.*;

import dss.business.Course.Course;

public class CourseDAO {

    public boolean addCourse(int id, String name, boolean visibilitySchedules) throws Exception {
        try (PreparedStatement stm = DAOConfig.connection.prepareStatement(
                "INSERT INTO courses (id, name, visibilitySchedules) VALUES (?, ?, ?)")) {
            stm.setInt(1, id);
            stm.setString(2, name);
            stm.setBoolean(3, visibilitySchedules);
            stm.executeUpdate();
            return true;
        } catch (SQLException e) {
            throw new Exception("Erro ao adicionar curso: " + e.getMessage());
        }
    }

    public Course getCourse(int id) throws Exception {
        try (PreparedStatement stm = DAOConfig.connection.prepareStatement("SELECT * FROM courses WHERE id = ?")) {
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                return new Course(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getBoolean("visibilitySchedules")
                );
            }
            return null;
        } catch (SQLException e) {
            throw new Exception("Erro ao obter curso: " + e.getMessage());
        }
    }

    public boolean updateCourse(int id, String name, boolean visibilitySchedules) throws Exception {
        try (PreparedStatement stm = DAOConfig.connection.prepareStatement(
                "UPDATE courses SET name = ?, visibilitySchedules = ? WHERE id = ?")) {
            stm.setString(1, name);
            stm.setBoolean(2, visibilitySchedules);
            stm.setInt(3, id);
            int rowsUpdated = stm.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            throw new Exception("Erro ao atualizar curso: " + e.getMessage());
        }
    }

    public boolean exists(int id) throws Exception {
        try (PreparedStatement stm = DAOConfig.connection.prepareStatement("SELECT 1 FROM courses WHERE id = ?")) {
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            throw new Exception("Erro ao verificar a existência do curso: " + e.getMessage());
        }
    }

    public boolean removeCourse(int id) throws Exception {
        try (PreparedStatement stm = DAOConfig.connection.prepareStatement("DELETE FROM courses WHERE id = ?")) {
            stm.setInt(1, id);
            int rowsDeleted = stm.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            throw new Exception("Erro ao remover curso: " + e.getMessage());
        }
    }
}
