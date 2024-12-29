package dss.data;

import java.sql.*;

import dss.business.User.CourseDirector;

public class CourseDirectorDAO {

    public boolean addCourseDirector(int id, String password, int courseId) throws Exception {
        try (PreparedStatement stm = DAOConfig.connection.prepareStatement(
                "INSERT INTO course_director (id, password, course_id) VALUES (?, ?, ?)")) {
            stm.setInt(1, id);
            stm.setString(2, password);
            stm.setInt(3, courseId);
            stm.executeUpdate();
            return true;
        } catch (SQLException e) {
            throw new Exception("Erro ao adicionar diretor de curso: " + e.getMessage());
        }
    }

    public CourseDirector getCourseDirector(int id) throws Exception {
        try (PreparedStatement stm = DAOConfig.connection.prepareStatement("SELECT * FROM course_director WHERE id = ?")) {
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                return new CourseDirector(
                        rs.getInt("id"),
                        rs.getString("password"),
                        rs.getInt("course_id")
                );
            }
            return null;
        } catch (SQLException e) {
            throw new Exception("Erro ao obter diretor de curso: " + e.getMessage());
        }
    }

    public boolean updatePassword(int id, String newPassword) throws Exception {
        try (PreparedStatement stm = DAOConfig.connection.prepareStatement(
                "UPDATE course_director SET password = ? WHERE id = ?")) {
            stm.setString(1, newPassword);
            stm.setInt(2, id);
            int rowsUpdated = stm.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            throw new Exception("Erro ao atualizar a password do diretor de curso: " + e.getMessage());
        }
    }

    public boolean exists(int id) throws Exception {
        try (PreparedStatement stm = DAOConfig.connection.prepareStatement("SELECT 1 FROM course_director WHERE id = ?")) {
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            throw new Exception("Erro ao verificar a existência do diretor de curso: " + e.getMessage());
        }
    }

    public boolean removeCourseDirector(int id) throws Exception {
        try (PreparedStatement stm = DAOConfig.connection.prepareStatement("DELETE FROM course_director WHERE id = ?")) {
            stm.setInt(1, id);
            int rowsDeleted = stm.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            throw new Exception("Erro ao remover diretor de curso: " + e.getMessage());
        }
    }
}