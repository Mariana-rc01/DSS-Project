package dss.data;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import dss.business.Course.*;

public class ShiftDAO {

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

    public Shift getShift(int id) throws Exception {
        try (PreparedStatement stm = DAOConfig.connection.prepareStatement("SELECT * FROM shifts WHERE id = ?")) {
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                int shiftType = rs.getInt("type");
                int ucId = rs.getInt("uc");
                if (shiftType == 0) {  // Theoretical
                    return new Theoretical(
                            rs.getInt("id"),
                            rs.getInt("capacityRoom"),
                            rs.getInt("enrolledCount"),
                            ucId
                    );
                } else if (shiftType == 1) {  // TheoreticalPractical
                    return new TheoreticalPractical(
                            rs.getInt("id"),
                            rs.getInt("capacityRoom"),
                            rs.getInt("enrolledCount"),
                            rs.getInt("capacity"),
                            ucId
                    );
                } else {
                    return new Shift(
                            rs.getInt("id"),
                            rs.getInt("capacityRoom"),
                            rs.getInt("enrolledCount"),
                            ucId
                    );
                }
            }
            return null;
        } catch (SQLException e) {
            throw new Exception("Erro ao obter shift: " + e.getMessage());
        }
    }

    public boolean updateShift(int id, int capacityRoom, int enrolledCount, int type, int capacity, int ucId) throws Exception {
        try (PreparedStatement stm = DAOConfig.connection.prepareStatement(
                "UPDATE shifts SET capacityRoom = ?, enrolledCount = ?, type = ?, capacity = ?, uc = ? WHERE id = ?")) {
            stm.setInt(1, capacityRoom);
            stm.setInt(2, enrolledCount);
            stm.setInt(3, type);
            stm.setInt(4, capacity);
            stm.setInt(5, ucId);
            stm.setInt(6, id);
            int rowsUpdated = stm.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            throw new Exception("Erro ao atualizar shift: " + e.getMessage());
        }
    }

    public boolean exists(int id) throws Exception {
        try (PreparedStatement stm = DAOConfig.connection.prepareStatement("SELECT 1 FROM shifts WHERE id = ?")) {
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            throw new Exception("Erro ao verificar a existência do shift: " + e.getMessage());
        }
    }

    public boolean removeShift(int id) throws Exception {
        try (PreparedStatement stm = DAOConfig.connection.prepareStatement("DELETE FROM shifts WHERE id = ?")) {
            stm.setInt(1, id);
            int rowsDeleted = stm.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            throw new Exception("Erro ao remover shift: " + e.getMessage());
        }
    }

    public List<Shift> getShiftsByUC(int ucId) throws Exception {
        List<Shift> shifts = new ArrayList<>();
        try (PreparedStatement stm = DAOConfig.connection.prepareStatement("SELECT * FROM shifts WHERE uc = ?")) {
            stm.setInt(1, ucId);
            ResultSet rs = stm.executeQuery();

            while (rs.next()) {
                int shiftType = rs.getInt("type");
                if (shiftType == 0) {
                    shifts.add(new Theoretical(
                            rs.getInt("id"),
                            rs.getInt("capacityRoom"),
                            rs.getInt("enrolledCount"),
                            rs.getInt("uc")
                    ));
                } else if (shiftType == 1) {
                    shifts.add(new TheoreticalPractical(
                            rs.getInt("id"),
                            rs.getInt("capacityRoom"),
                            rs.getInt("enrolledCount"),
                            rs.getInt("capacity"),
                            rs.getInt("uc") 
                    ));
                } else {
                    shifts.add(new Shift(
                            rs.getInt("id"),
                            rs.getInt("capacityRoom"),
                            rs.getInt("enrolledCount"),
                            rs.getInt("uc")
                    ));
                }
            }

            if (shifts.isEmpty()) {
                throw new Exception("Não existem shifts para a UC com id " + ucId);
            }

            return shifts;
        } catch (SQLException e) {
            throw new Exception("Erro ao obter shifts da UC: " + e.getMessage());
        }
    }
}

