package dss.data;

import java.sql.*;

import dss.business.Course.UC;

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
                return new UC(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("year"),
                        rs.getInt("semester"),
                        rs.getString("policyPreference")
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
        try (PreparedStatement stm = DAOConfig.connection.prepareStatement("DELETE FROM ucs WHERE id = ?")) {
            stm.setInt(1, id);
            int rowsDeleted = stm.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            throw new Exception("Erro ao remover Unidade Curricular: " + e.getMessage());
        }
    }
}