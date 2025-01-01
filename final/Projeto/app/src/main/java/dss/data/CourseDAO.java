package dss.data;

import java.sql.*;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dss.business.Course.Course;
import dss.business.Course.Shift;
import dss.business.Course.Theoretical;
import dss.business.Course.TheoreticalPractical;
import dss.business.Course.TimeSlot;
import dss.business.Schedule.PreDefinedSchedule;
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

    public Map<Integer,Shift> getShiftsByCourse(int idCourse) throws Exception {
        Map<Integer, Shift> shifts = new HashMap<>();
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
                    shifts.put(rs.getInt("id"), new Theoretical(
                            rs.getInt("id"),
                            rs.getInt("capacityRoom"),
                            rs.getInt("enrolledCount"),
                            ucId,
                            timeSlots
                    ));
                } else if (shiftType == 1) {
                    shifts.put(rs.getInt("id"), new TheoreticalPractical(
                            rs.getInt("id"),
                            rs.getInt("capacityRoom"),
                            rs.getInt("enrolledCount"),
                            rs.getInt("capacity"),
                            ucId,
                            timeSlots
                    ));
                } else {
                    shifts.put(rs.getInt("id"), new Shift(
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

    public boolean addPreDefinedScheduleToCourse(int courseId, int scheduleId, int year, int no_conflicts, Map<Integer, Map<Integer, List<Integer>>> schedule) throws Exception {
        // Inserir o schedule na tabela `schedules`
        String scheduleInsertQuery = "INSERT INTO schedules (id, course, year, no_conflicts) VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement stm = DAOConfig.connection.prepareStatement(scheduleInsertQuery)) {
            stm.setInt(1, scheduleId);
            stm.setInt(2, courseId);
            stm.setInt(3, year);
            stm.setInt(4, no_conflicts);
            stm.executeUpdate();
    
            String predefinedScheduleInsertQuery = "INSERT INTO predefined_schedule (idSchedule, uc_id, shift_id, timeslot_id) VALUES (?, ?, ?, ?)";
            
            try (PreparedStatement stmPredefined = DAOConfig.connection.prepareStatement(predefinedScheduleInsertQuery)) {
                for (Map.Entry<Integer, Map<Integer, List<Integer>>> ucEntry : schedule.entrySet()) {
                    int ucId = ucEntry.getKey();
                    Map<Integer, List<Integer>> shifts = ucEntry.getValue();
                    
                    for (Map.Entry<Integer, List<Integer>> shiftEntry : shifts.entrySet()) {
                        int shiftId = shiftEntry.getKey();
                        List<Integer> timeSlots = shiftEntry.getValue();
    
                        for (int timeSlotId : timeSlots) {
                            stmPredefined.setInt(1, scheduleId);
                            stmPredefined.setInt(2, ucId);
                            stmPredefined.setInt(3, shiftId);
                            stmPredefined.setInt(4, timeSlotId);
                            stmPredefined.addBatch();
                        }
                    }
                }
    
                stmPredefined.executeBatch();
                return true;
    
            } catch (SQLException e) {
                throw new Exception("Erro ao adicionar o horário pré-definido na tabela `predefined_schedule`: " + e.getMessage(), e);
            }
    
        } catch (SQLException e) {
            throw new Exception("Erro ao adicionar o horário na tabela `schedules`: " + e.getMessage(), e);
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

    public Map<Integer, List<Integer>> getUcsByYearForCourse(int idCourse) throws SQLException {
        String query = "SELECT year, id AS uc_id FROM ucs WHERE course = ?";
        Map<Integer, List<Integer>> ucsByYear = new HashMap<>();

        try (PreparedStatement stm = DAOConfig.connection.prepareStatement(query)) {
            stm.setInt(1, idCourse);

            try (ResultSet rs = stm.executeQuery()) {
                while (rs.next()) {
                    int year = rs.getInt("year");
                    int ucId = rs.getInt("uc_id");

                    ucsByYear.computeIfAbsent(year, k -> new ArrayList<>()).add(ucId);
                }
            }
        }

        return ucsByYear;
    }

    public Map<Integer, PreDefinedSchedule> getPreDefinedScheduleByCourse(int idCourse) throws Exception {
        Map<Integer, PreDefinedSchedule> schedulesMap = new HashMap<>();

        String schedulesQuery = "SELECT id, year, no_conflicts FROM schedules WHERE course = ?";
        String predefinedScheduleQuery = "SELECT idSchedule, uc_id, shift_id, timeslot_id FROM predefined_schedule WHERE idSchedule = ?";

        try (PreparedStatement schedulesStm = DAOConfig.connection.prepareStatement(schedulesQuery)) {
            schedulesStm.setInt(1, idCourse);

            try (ResultSet schedulesRs = schedulesStm.executeQuery()) {
                while (schedulesRs.next()) {
                    int scheduleId = schedulesRs.getInt("id");
                    int year = schedulesRs.getInt("year");
                    int noConflicts = schedulesRs.getInt("no_conflicts");

                    Map<Integer, Map<Integer, List<Integer>>> scheduleMap = new HashMap<>();

                    try (PreparedStatement predefinedStm = DAOConfig.connection.prepareStatement(predefinedScheduleQuery)) {
                        predefinedStm.setInt(1, scheduleId);

                        try (ResultSet predefinedRs = predefinedStm.executeQuery()) {
                            while (predefinedRs.next()) {
                                int ucId = predefinedRs.getInt("uc_id");
                                int shiftId = predefinedRs.getInt("shift_id");
                                int timeSlotId = predefinedRs.getInt("timeslot_id");

                                scheduleMap
                                    .computeIfAbsent(ucId, k -> new HashMap<>())
                                    .computeIfAbsent(shiftId, k -> new ArrayList<>())
                                    .add(timeSlotId);
                            }
                        }
                    }

                    PreDefinedSchedule preDefinedSchedule = new PreDefinedSchedule(scheduleId, year, noConflicts, scheduleMap);
                    schedulesMap.put(scheduleId, preDefinedSchedule);
                }
            }
        } catch (SQLException e) {
            throw new Exception("Erro ao buscar os horários pré-definidos para o curso: " + e.getMessage(), e);
        }
        return schedulesMap;
    }


    public void updateStudentSchedule(int studentId, Map<Integer, List<Integer>> newSchedule) throws Exception {
        try {
            // Search for the existing shifts of the student before deleting
            Map<Integer, List<Integer>> existingSchedule = new HashMap<>();
            try (PreparedStatement fetchStm = DAOConfig.connection.prepareStatement(
                    "SELECT uc_id, shift_id FROM student_schedule WHERE student_id = ?")) {
                fetchStm.setInt(1, studentId);
                try (ResultSet rs = fetchStm.executeQuery()) {
                    while (rs.next()) {
                        int ucId = rs.getInt("uc_id");
                        int shiftId = rs.getInt("shift_id");

                        existingSchedule.computeIfAbsent(ucId, k -> new ArrayList<>()).add(shiftId);
                    }
                }
            }

            // Decrement the enrolledCount for the old shifts
            for (Map.Entry<Integer, List<Integer>> entry : existingSchedule.entrySet()) {
                for (int shiftId : entry.getValue()) {
                    try (PreparedStatement decrementShiftStm = DAOConfig.connection.prepareStatement(
                            "UPDATE shifts SET enrolledCount = enrolledCount - 1 WHERE id = ?")) {
                        decrementShiftStm.setInt(1, shiftId);
                        decrementShiftStm.executeUpdate();
                    }
                }
            }

            // Remove the existing schedule of the student
            try (PreparedStatement deleteStm = DAOConfig.connection.prepareStatement(
                    "DELETE FROM student_schedule WHERE student_id = ?")) {
                deleteStm.setInt(1, studentId);
                deleteStm.executeUpdate();
            }

            // Insert the new schedule
            for (Map.Entry<Integer, List<Integer>> entry : newSchedule.entrySet()) {
                int ucId = entry.getKey();
                for (int shiftId : entry.getValue()) {
                    try (PreparedStatement stm = DAOConfig.connection.prepareStatement(
                            "INSERT INTO student_schedule (student_id, uc_id, shift_id) VALUES (?, ?, ?)")) {
                        stm.setInt(1, studentId);
                        stm.setInt(2, ucId);
                        stm.setInt(3, shiftId);
                        stm.executeUpdate();
                    }

                    // Increment the count of students in the shift
                    try (PreparedStatement incrementShiftStm = DAOConfig.connection.prepareStatement(
                            "UPDATE shifts SET enrolledCount = enrolledCount + 1 WHERE id = ?")) {
                        incrementShiftStm.setInt(1, shiftId);
                        incrementShiftStm.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            throw new Exception("Erro ao atualizar o horário do estudante: " + e.getMessage());
        }
    }

    public void updateStudentsSchedules(List<Student> students) throws Exception {
        for (Student student : students) {
            Map<Integer, List<Integer>> studentSchedule = student.getSchedule();
            if (studentSchedule != null && !studentSchedule.isEmpty()) {
                updateStudentSchedule(student.getId(), studentSchedule);
            }
        }
    }
 
}
