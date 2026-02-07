package edu.aitu.oop3.DataComponent;

import edu.aitu.oop3.db.IDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public abstract class BaseRepository<T, ID> implements Repository<T, ID> {
    protected final IDB db;

    protected BaseRepository(IDB db) {
        this.db = db;
    }

    protected List<T> query(String sql, RowMapper<T> mapper, Object... params) {
        List<T> result = new ArrayList<>();
        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                result.add(mapper.map(rs));
            }
            return result;
        } catch (SQLException e) {
            throw new RuntimeException("Query execution error" + sql, e);
        }
    }


    protected <R> List<R> queryOtherType(String sql, RowMapper<R> mapper, Object... params) {
        List<R> result = new ArrayList<>();
        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                result.add(mapper.map(rs));
            }
            return result;
        } catch (SQLException e) {
            throw new RuntimeException("Query execution error" + sql, e);
        }
    }


    protected void executeUpdate(String sql, Object... params) {
        try (Connection con = db.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error while updating " + sql, e);
        }
    }
}
