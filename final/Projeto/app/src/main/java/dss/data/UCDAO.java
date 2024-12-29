package dss.data;

import java.sql.*;
import java.time.DayOfWeek;
import java.util.*;

import dss.business.Course.*;

public class UCDAO {

    public boolean addUC(int id, String name, int year, int semester, String policyPreference) throws Exception {
        try (PreparedStatement stm = DAOConfig.connection.prepareStatement(
                "INSERT INTO ucs (id, name, year, semester, policyPreference) VALUES (?, ?, ?, ?, ?)")) {
            stm.setInt(1, id);
            stm.setString(2, name);
            stm.setInt(3, year);
            stm.setInt(4, semester);
            stm.setString(5, policyPreference);
            stm.executeUpdate();
            return true;
        } catch (SQLException e) {
            throw new Exception("Erro ao adicionar Unidade Curricular: " + e.getMessage());
        }
    }

    public UC getUC(int id) throws Exception {
        try (PreparedStatement stm = DAOConfig.connection.prepareStatement("SELECT * FROM ucs WHERE id = ?")) {
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                List<Shift> shifts = getShiftsByUC(id);
                return new UC(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("year"),
                        rs.getInt("semester"),
                        rs.getString("policyPreference"),
                        shifts
                );
            }
            return null;
        } catch (SQLException e) {
            throw new Exception("Erro ao obter Unidade Curricular: " + e.getMessage());
        }
    }

    public boolean updateUC(int id, String name, int year, int semester, String policyPreference) throws Exception {
        try (PreparedStatement stm = DAOConfig.connection.prepareStatement(
                "UPDATE ucs SET name = ?, year = ?, semester = ?, policyPreference = ? WHERE id = ?")) {
            stm.setString(1, name);
            stm.setInt(2, year);
            stm.setInt(3, semester);
            stm.setString(4, policyPreference);
            stm.setInt(5, id);
            int rowsUpdated = stm.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            throw new Exception("Erro ao atualizar Unidade Curricular: " + e.getMessage());
        }
    }

    public boolean exists(int id) throws Exception {
        try (PreparedStatement stm = DAOConfig.connection.prepareStatement("SELECT 1 FROM ucs WHERE id = ?")) {
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            throw new Exception("Erro ao verificar a existência da Unidade Curricular: " + e.getMessage());
        }
    }

    public boolean removeUC(int id) throws Exception {
        try {
            removeShiftsByUC(id);
            try (PreparedStatement stm = DAOConfig.connection.prepareStatement("DELETE FROM ucs WHERE id = ?")) {
                stm.setInt(1, id);
                int rowsDeleted = stm.executeUpdate();
                return rowsDeleted > 0;
            }
        } catch (SQLException e) {
            throw new Exception("Erro ao remover Unidade Curricular: " + e.getMessage());
        }
    }

    public boolean addShift(int id, int capacityRoom, int enrolledCount, int type, int capacity, int ucId) throws Exception {
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

    public List<Shift> getShiftsByUC(int ucId) throws Exception {
        List<Shift> shifts = new ArrayList<>();
        try (PreparedStatement stm = DAOConfig.connection.prepareStatement("SELECT * FROM shifts WHERE uc = ?")) {
            stm.setInt(1, ucId);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                int shiftType = rs.getInt("type");
                List<TimeSlot> timeSlots = getTimeSlotsByShift(rs.getInt("id"));
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
            throw new Exception("Erro ao obter shifts da UC: " + e.getMessage());
        }
    }

    public boolean removeShiftsByUC(int ucId) throws Exception {
        try (PreparedStatement stm = DAOConfig.connection.prepareStatement("DELETE FROM shifts WHERE uc = ?")) {
            stm.setInt(1, ucId);
            int rowsDeleted = stm.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            throw new Exception("Erro ao remover shifts da UC: " + e.getMessage());
        }
    }

    public boolean addTimeSlot(int id, Time timeStart, Time timeEnd, DayOfWeek weekDay, int shiftId) throws Exception {
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

    public List<TimeSlot> getTimeSlotsByShift(int shiftId) throws Exception {
        List<TimeSlot> timeslots = new ArrayList<>();
        try (PreparedStatement stm = DAOConfig.connection.prepareStatement("SELECT * FROM timeslots WHERE shift = ?")) {
            stm.setInt(1, shiftId);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                DayOfWeek weekDay = DayOfWeek.of(rs.getInt("weekDay"));
                timeslots.add(new TimeSlot(
                        rs.getInt("id"),
                        rs.getTime("time_start"),
                        rs.getTime("time_end"),
                        weekDay,
                        rs.getInt("shift")
                ));
            }
            return timeslots;
        } catch (SQLException e) {
            throw new Exception("Erro ao obter timeslots do shift: " + e.getMessage());
        }
    }
}
