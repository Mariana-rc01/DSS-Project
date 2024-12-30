package dss.data;

import java.sql.*;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

import dss.business.Course.Course;
import dss.business.Course.Shift;
import dss.business.Course.Theoretical;
import dss.business.Course.TheoreticalPractical;
import dss.business.Course.TimeSlot;
import dss.business.User.AthleteStudent;
import dss.business.User.EmployedStudent;
import dss.business.User.Student;

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

    public List<Shift> getShiftsByCourse(int idCourse) throws Exception {
        List<Shift> shifts = new ArrayList<>();
        UCDAO ucDAO = new UCDAO();
        String query = "SELECT s.* FROM shifts s INNER JOIN ucs u ON s.uc = u.id WHERE u.course = ?";

        try (PreparedStatement stm = DAOConfig.connection.prepareStatement(query)) {
            stm.setInt(1, idCourse);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                int shiftType = rs.getInt("type");
                List<TimeSlot> timeSlots = ucDAO.getTimeSlotsByShift(rs.getInt("id"));
                int ucId = rs.getInt("uc");

                if (shiftType == 0) {
                    shifts.add(new Theoretical(
                            rs.getInt("id"),
                            rs.getInt("capacityRoom"),
                            rs.getInt("enrolledCount"),
                            ucId,
                            timeSlots
                    ));
                } else if (shiftType == 1) {
                    shifts.add(new TheoreticalPractical(
                            rs.getInt("id"),
                            rs.getInt("capacityRoom"),
                            rs.getInt("enrolledCount"),
                            rs.getInt("capacity"),
                            ucId,
                            timeSlots
                    ));
                } else {
                    shifts.add(new Shift(
                            rs.getInt("id"),
                            rs.getInt("capacityRoom"),
                            rs.getInt("enrolledCount"),
                            ucId,
                            timeSlots
                    ));
                }
            }

            return shifts;
        } catch (SQLException e) {
            throw new Exception("Erro ao obter shifts pelo ID do curso: " + e.getMessage());
        }
    }

    public boolean addStudent(Student student) throws Exception {
        String insertStudentQuery = "INSERT INTO students (id, password, type, course) VALUES (?, ?, ?, ?)";
        String insertStudentUCQuery = "INSERT INTO student_ucs (student_id, uc_id) VALUES (?, ?)";
        Connection connection = null;
        PreparedStatement studentStatement = null;
        PreparedStatement ucStatement = null;
    
        try {
            connection = DAOConfig.connection;
            connection.setAutoCommit(false);
    
            studentStatement = connection.prepareStatement(insertStudentQuery);
            studentStatement.setInt(1, student.getId());
            studentStatement.setString(2, student.getPassword());
            studentStatement.setInt(3, getStudentType(student));
            studentStatement.setInt(4, student.getCourse());
            studentStatement.executeUpdate();
    
            ucStatement = connection.prepareStatement(insertStudentUCQuery);
            for (Integer ucId : student.getUCs()) {
                ucStatement.setInt(1, student.getId());
                ucStatement.setInt(2, ucId);
                ucStatement.addBatch();
            }
            ucStatement.executeBatch();
    
            connection.commit();
            return true;
    
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackException) {
                    throw new Exception("Erro ao fazer rollback: " + rollbackException.getMessage(), rollbackException);
                }
            }
            throw new Exception("Erro ao adicionar estudante: " + e.getMessage(), e);
        } finally {
            if (studentStatement != null) studentStatement.close();
            if (ucStatement != null) ucStatement.close();
            if (connection != null) connection.setAutoCommit(true);
        }
    }

    public boolean addShiftToCourse(int id, int capacityRoom, int enrolledCount, int type, int capacity, int ucId) throws Exception {
        try (PreparedStatement stm = DAOConfig.connection.prepareStatement(
                "INSERT INTO shifts (id, capacityRoom, enrolledCount, type, capacity, uc) VALUES (?, ?, ?, ?, ?, ?)")) {
            stm.setInt(1, id);
            stm.setInt(2, capacityRoom);
            stm.setInt(3, enrolledCount);
            stm.setInt(4, type);
            stm.setInt(5, capacity);
            stm.setInt(6, ucId);
            stm.executeUpdate();
            return true;
        } catch (SQLException e) {
            throw new Exception("Erro ao adicionar shift: " + e.getMessage());
        }
    }

    public boolean addTimeSlotToShift(int id, Time timeStart, Time timeEnd, DayOfWeek weekDay, int shiftId) throws Exception {
        try (PreparedStatement stm = DAOConfig.connection.prepareStatement(
                "INSERT INTO timeslots (id, time_start, time_end, weekDay, shift) VALUES (?, ?, ?, ?, ?)")) {
            stm.setInt(1, id);
            stm.setTime(2, timeStart);
            stm.setTime(3, timeEnd);
            stm.setInt(4, weekDay.getValue());
            stm.setInt(5, shiftId);
            stm.executeUpdate();
            return true;
        } catch (SQLException e) {
            throw new Exception("Erro ao adicionar timeslot: " + e.getMessage());
        }
    }

    private int getStudentType(Student student) {
        if (student instanceof AthleteStudent) {
            return 1;
        } else if (student instanceof EmployedStudent) {
            return 2;
        }
        return 0;
    }
    
}
