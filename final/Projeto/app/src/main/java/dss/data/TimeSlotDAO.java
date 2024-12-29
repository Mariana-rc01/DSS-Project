package dss.data;

import java.sql.*;
import java.time.DayOfWeek;
import java.util.*;

import dss.business.Course.TimeSlot;

public class TimeSlotDAO {

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

    public TimeSlot getTimeSlot(int id) throws Exception {
        try (PreparedStatement stm = DAOConfig.connection.prepareStatement("SELECT * FROM timeslots WHERE id = ?")) {
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                int shiftId = rs.getInt("shift");
                DayOfWeek weekDay = DayOfWeek.of(rs.getInt("weekDay"));
                return new TimeSlot(
                        rs.getInt("id"),
                        rs.getTime("time_start"),
                        rs.getTime("time_end"),
                        weekDay,
                        shiftId
                );
            }
            return null;
        } catch (SQLException e) {
            throw new Exception("Erro ao obter timeslot: " + e.getMessage());
        }
    }

    public boolean updateTimeSlot(int id, Time timeStart, Time timeEnd, DayOfWeek weekDay, int shiftId) throws Exception {
        try (PreparedStatement stm = DAOConfig.connection.prepareStatement(
                "UPDATE timeslots SET time_start = ?, time_end = ?, weekDay = ?, shift = ? WHERE id = ?")) {
            stm.setTime(1, timeStart);
            stm.setTime(2, timeEnd);
            stm.setInt(3, weekDay.getValue());
            stm.setInt(4, shiftId);
            stm.setInt(5, id);
            int rowsUpdated = stm.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            throw new Exception("Erro ao atualizar timeslot: " + e.getMessage());
        }
    }

    public boolean exists(int id) throws Exception {
        try (PreparedStatement stm = DAOConfig.connection.prepareStatement("SELECT 1 FROM timeslots WHERE id = ?")) {
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            throw new Exception("Erro ao verificar a existência do timeslot: " + e.getMessage());
        }
    }

    public boolean removeTimeSlot(int id) throws Exception {
        try (PreparedStatement stm = DAOConfig.connection.prepareStatement("DELETE FROM timeslots WHERE id = ?")) {
            stm.setInt(1, id);
            int rowsDeleted = stm.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            throw new Exception("Erro ao remover timeslot: " + e.getMessage());
        }
    }

    public List<TimeSlot> getTimeSlotsByShift(int shiftId) throws Exception {
        List<TimeSlot> timeslots = new ArrayList<>();
        try (PreparedStatement stm = DAOConfig.connection.prepareStatement("SELECT * FROM timeslots WHERE shift = ?")) {
            stm.setInt(1, shiftId);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                DayOfWeek weekDay = DayOfWeek.of(rs.getInt("weekDay")); // Convertendo int para DayOfWeek
                timeslots.add(new TimeSlot(
                        rs.getInt("id"),
                        rs.getTime("time_start"),
                        rs.getTime("time_end"),
                        weekDay,
                        rs.getInt("shift")
                ));
            }

            if (timeslots.isEmpty()) {
                throw new Exception("Não existem timeslots para o shift com id " + shiftId);
            }

            return timeslots;
        } catch (SQLException e) {
            throw new Exception("Erro ao obter timeslots do shift: " + e.getMessage());
        }
    }
}
