package dss.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import dss.business.Course.UC;

public class UCDAO implements Map<Integer, UC> {
    private static UCDAO singleton = null;

    public UCDAO() {
        String sql = "CREATE TABLE IF NOT EXISTS ucs (\n"
                + " id integer PRIMARY KEY,\n"
                + " name text NOT NULL,\n"
                + " year integer NOT NULL\n"
                + " semester integer NOT NULL\n"
                + " policyPreference text\n"
                + ");";

        try (Connection conn = DriverManager.getConnection(DAOConfig.URL, DAOConfig.USERNAME, DAOConfig.PASSWORD);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table created successfully");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static UCDAO getInstance(){
        if (singleton == null) {
            singleton = new UCDAO();
        }
        return singleton;
    }


    public int size() {
        int i = 0;
        try (Connection conn = DriverManager.getConnection(DAOConfig.URL, DAOConfig.USERNAME, DAOConfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT count(*) FROM ucs")) {
            if(rs.next()) {
                i = rs.getInt(1);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return i;
    }

    public boolean isEmpty() {
        return this.size() == 0;
    }

    @Override
    public void clear() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'clear'");
    }

    @Override
    public boolean containsKey(Object key) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'containsKey'");
    }

    @Override
    public boolean containsValue(Object value) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'containsValue'");
    }

    @Override
    public Set<Entry<Integer, UC>> entrySet() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'entrySet'");
    }

    @Override
    public UC get(Object key) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'get'");
    }

    @Override
    public Set<Integer> keySet() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'keySet'");
    }

    @Override
    public UC put(Integer arg0, UC arg1) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'put'");
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends UC> m) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'putAll'");
    }

    @Override
    public UC remove(Object key) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'remove'");
    }

    @Override
    public Collection<UC> values() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'values'");
    }
}