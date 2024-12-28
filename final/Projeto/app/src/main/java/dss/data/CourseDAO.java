package dss.data;

import java.sql.*;
import java.util.*;
import static java.util.stream.Collectors.toList;

import dss.business.Course.Course;

public class CourseDAO implements Map<Integer, Course> {
    private static CourseDAO singleton = null;

    private CourseDAO() {
        try (Connection conn = DriverManager.getConnection(DAOConfig.URL, DAOConfig.USERNAME, DAOConfig.PASSWORD);
             Statement stmt = conn.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS courses (\n"
            + " id integer PRIMARY KEY,\n"
            + " name text NOT NULL,\n"
            + " visibilitySchedules boolean NOT NULL\n"
            + ");";
            stmt.executeUpdate(sql);
            sql = "CREATE TABLE IF NOT EXISTS ucs (\n"
                + " id integer PRIMARY KEY,\n"
                + " name text NOT NULL,\n"
                + " year integer NOT NULL\n"
                + " semester integer NOT NULL\n"
                + " policyPreference text\n"
                + " course integer, foreign key(courses) references courses(id))\n"
                + ");";
            stmt.executeUpdate(sql);
            sql = "CREATE TABLE IF NOT EXISTS students (\n"
                + " id integer PRIMARY KEY,\n"
                + " password text NOT NULL,\n"
                + " course integer, foreign key(courses) references courses(id))\n"
                + ");";
            stmt.executeUpdate(sql);
            System.out.println("Table created successfully");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static CourseDAO getInstance(){
        if (singleton == null) {
            singleton = new CourseDAO();
        }
        return singleton;
    }


    public int size() {
        int i = 0;
        try (Connection conn = DriverManager.getConnection(DAOConfig.URL, DAOConfig.USERNAME, DAOConfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT count(*) FROM courses")) {
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
        try (Connection conn = DriverManager.getConnection(DAOConfig.URL, DAOConfig.USERNAME, DAOConfig.PASSWORD);
             Statement stm = conn.createStatement()) {
            stm.executeUpdate("DELETE FROM courses");
            stm.executeUpdate("TRUNCATE courses");
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
    }

    @Override
    public boolean containsKey(Object key) {
        boolean r;
        try (Connection conn = DriverManager.getConnection(DAOConfig.URL, DAOConfig.USERNAME, DAOConfig.PASSWORD);
             PreparedStatement pstm = conn.prepareStatement("SELECT id FROM courses WHERE id=?")) {
            pstm.setInt(1, (Integer) key);
            try (ResultSet rs = pstm.executeQuery()) {
                r = rs.next();
            }
        } catch (SQLException e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return r;
    }

    @Override
    public boolean containsValue(Object value) {
        Course c = (Course) value;
        return this.containsKey(c.getId());
    }

    @Override
    public Set<Entry<Integer, Course>> entrySet() {
        Set<Entry<Integer, Course>> entries = new HashSet<>();
        for (Integer key : this.keySet()) {
            entries.add(new AbstractMap.SimpleEntry<>(key, this.get(key)));
        }
        return entries;
    }

    @Override
    public Course get(Object key) {
        Course c = null;
        try (Connection conn = DriverManager.getConnection(DAOConfig.URL, DAOConfig.USERNAME, DAOConfig.PASSWORD);
             PreparedStatement pstm = conn.prepareStatement("SELECT * FROM courses WHERE id=?")) {
            pstm.setInt(1, (Integer) key);
            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    boolean visibilitySchedules = rs.getBoolean("visibilitySchedules");
                    Collection<Integer> ucs = getUCsCourse(key.toString(), pstm);
                    Collection<Integer> students = getStudentsCourse(key.toString(), pstm);
                    c = new Course(id, name, visibilitySchedules, ucs, students);
                }
            }
        } catch (SQLException e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return c;
    }

    private Collection<Integer> getUCsCourse(String id, Statement stm) throws SQLException {
        Collection<Integer> ucs = new TreeSet<>();
        try (ResultSet rsa = stm.executeQuery("SELECT id FROM ucs WHERE course='" + id + "'")) {
            while (rsa.next()) {
                ucs.add(rsa.getInt("id"));
            }
        }
        return ucs;
    }

    private Collection<Integer> getStudentsCourse(String id, Statement stm) throws SQLException {
        Collection<Integer> students = new TreeSet<>();
        try (ResultSet rsa = stm.executeQuery("SELECT id FROM students WHERE course='" + id + "'")) {
            while (rsa.next()) {
                students.add(rsa.getInt("id"));
            }
        }
        return students;
    }

    @Override
    public Set<Integer> keySet() {
        Set<Integer> keys = new TreeSet<>();
        try (Connection conn = DriverManager.getConnection(DAOConfig.URL, DAOConfig.USERNAME, DAOConfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT id FROM courses")) {
            while (rs.next()) {
                keys.add(rs.getInt("id"));
            }
        } catch (SQLException e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return keys;
    }

    @Override
    public Course put(Integer key, Course course) {
        Course previous = this.get(key);
        try (Connection conn = DriverManager.getConnection(DAOConfig.URL, DAOConfig.USERNAME, DAOConfig.PASSWORD);
            Statement stm = conn.createStatement()) {
                stm.executeUpdate("INSERT INTO courses VALUES ('" + course.getId() + "', '" + course.getName() + "', '" + course.getVisibilitySchedules() + "')"
                + "ON DUPLICATE KEY UPDATE name=Values(name), visibilitySchedules=Values(visibilitySchedules)");

            Collection<Integer> oldStudents = getStudentsCourse(key.toString(), stm);
            Collection<Integer> newStudents = course.getStudents().stream().collect(toList());
            newStudents.removeAll(oldStudents);
            oldStudents.removeAll(course.getStudents().stream().collect(toList()));
            try (PreparedStatement pstm = conn.prepareStatement("UPDATE students SET course=? WHERE id=?")) {
                pstm.setNull(1, Types.INTEGER);
                for (Integer student : oldStudents) {
                    pstm.setInt(2, student);
                    pstm.executeUpdate();
                }

                pstm.setInt(1, course.getId());
                for (Integer student : newStudents) {
                    pstm.setInt(2, student);
                    pstm.executeUpdate();
                }
            }

            Collection<Integer> oldUCs = getUCsCourse(key.toString(), stm);
            Collection<Integer> newUCs = course.getUCs().stream().collect(toList());
            newUCs.removeAll(oldStudents);
            oldUCs.removeAll(course.getUCs().stream().collect(toList()));
            try (PreparedStatement pstmUC = conn.prepareStatement("UPDATE ucs SET course=? WHERE id=?")) {
                pstmUC.setNull(1, Types.INTEGER);
                for (Integer uc : oldUCs) {
                    pstmUC.setInt(2, uc);
                    pstmUC.executeUpdate();
                }

                pstmUC.setInt(1, course.getId());
                for (Integer uc : newUCs) {
                    pstmUC.setInt(2, uc);
                    pstmUC.executeUpdate();
                }
            }
        } catch (SQLException e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return previous;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends Course> m) {
        for(Course c : m.values()) {
            this.put(c.getId(), c);
        }
    }

    @Override
    public Course remove(Object key) {
        Course c = this.get(key);
        try (Connection conn = DriverManager.getConnection(DAOConfig.URL, DAOConfig.USERNAME, DAOConfig.PASSWORD);
             PreparedStatement course_pstm = conn.prepareStatement("DELETE FROM courses WHERE id=?");
             PreparedStatement students_pstm = conn.prepareStatement("UPDATE students SET course=? WHERE id=?");
             PreparedStatement ucs_pstm = conn.prepareStatement("UPDATE ucs SET course=? WHERE id=?")) {

            students_pstm.setNull(1, Types.INTEGER);
            for (int na: c.getStudents()) {
                students_pstm.setInt(2, na);
                students_pstm.executeUpdate();
            }

            ucs_pstm.setNull(1, Types.INTEGER);
            for (int na: c.getUCs()) {
                ucs_pstm.setInt(2, na);
                ucs_pstm.executeUpdate();
            }

            // apagar a turma
            course_pstm.setInt(1, (Integer) key);
            course_pstm.executeUpdate();
        } catch (Exception e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }   
        return c;
    }

    @Override
    public Collection<Course> values() {
        Collection<Course> courses = new TreeSet<>();
        try (Connection conn = DriverManager.getConnection(DAOConfig.URL, DAOConfig.USERNAME, DAOConfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery("SELECT id FROM courses")) {
            while (rs.next()) {
                courses.add(this.get(rs.getInt("id")));
            }
        } catch (SQLException e) {
            // Database error!
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        }
        return courses;
    }
}