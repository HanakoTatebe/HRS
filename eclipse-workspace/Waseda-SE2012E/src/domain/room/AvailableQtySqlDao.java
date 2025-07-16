package domain.room;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import util.DateUtil;

/**
 * DB SQL 実装 of AvailableQtyDao
 */
public class AvailableQtySqlDao implements AvailableQtyDao {

    private static final String ID          = "sa";
    private static final String PASSWORD    = "";
    private static final String DRIVER_NAME = "org.hsqldb.jdbcDriver";
    private static final String URL         = "jdbc:hsqldb:hsql://localhost;shutdown=true";
    private static final String TABLE_NAME  = "AVAILABLEQTY";

    @Override
    public AvailableQty getAvailableQty(Date date) throws RoomException {
        String sql = ""
            + "SELECT stayingdate, room_type, qty "
            + "FROM " + TABLE_NAME + " "
            + "WHERE stayingdate = '" + DateUtil.convertToString(date) + "';";
        try (Connection conn = getConnection();
             Statement stmt   = conn.createStatement();
             ResultSet rs     = stmt.executeQuery(sql)) {

            if (rs.next()) {
                AvailableQty q = new AvailableQty();
                q.setDate(DateUtil.convertToDate(rs.getString("stayingdate")));
                q.setRoomType(rs.getString("room_type"));
                q.setQty(rs.getInt("qty"));
                return q;
            }
            return null;
        } catch (SQLException e) {
            RoomException ex = new RoomException(RoomException.CODE_DB_EXEC_QUERY_ERROR, e);
            ex.getDetailMessages().add("getAvailableQty()");
            throw ex;
        }
    }

    @Override
    public void createAbailableQty(AvailableQty availableQty) throws RoomException {
        String sql = ""
            + "INSERT INTO " + TABLE_NAME
            + " (stayingdate, room_type, qty) VALUES ('"
            + DateUtil.convertToString(availableQty.getDate()) + "','"
            + availableQty.getRoomType() + "',"
            + availableQty.getQty()
            + ");";
        try (Connection conn = getConnection();
             Statement stmt   = conn.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            RoomException ex = new RoomException(RoomException.CODE_DB_EXEC_QUERY_ERROR, e);
            ex.getDetailMessages().add("createAbailableQty()");
            throw ex;
        }
    }

    @Override
    public void updateAvailableQty(AvailableQty availableQty) throws RoomException {
        String sql = ""
            + "UPDATE " + TABLE_NAME
            + " SET qty = " + availableQty.getQty()
            + " WHERE stayingdate = '" + DateUtil.convertToString(availableQty.getDate()) + "'"
            + "   AND room_type   = '" + availableQty.getRoomType() + "';";
        try (Connection conn = getConnection();
             Statement stmt   = conn.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            RoomException ex = new RoomException(RoomException.CODE_DB_EXEC_QUERY_ERROR, e);
            ex.getDetailMessages().add("updateAvailableQty()");
            throw ex;
        }
    }

    @Override
    public List<AvailableQty> findByDate(Date date) throws RoomException {
        List<AvailableQty> list = new ArrayList<>();
        String sql = ""
            + "SELECT stayingdate, room_type, qty "
            + "FROM " + TABLE_NAME + " "
            + "WHERE stayingdate = '" + DateUtil.convertToString(date) + "';";
        try (Connection conn = getConnection();
             Statement stmt   = conn.createStatement();
             ResultSet rs     = stmt.executeQuery(sql)) {
            while (rs.next()) {
                AvailableQty q = new AvailableQty();
                q.setDate(DateUtil.convertToDate(rs.getString("stayingdate")));
                q.setRoomType(rs.getString("room_type"));
                q.setQty(rs.getInt("qty"));
                list.add(q);
            }
        } catch (SQLException e) {
            RoomException ex = new RoomException(RoomException.CODE_DB_EXEC_QUERY_ERROR, e);
            ex.getDetailMessages().add("findByDate()");
            throw ex;
        }
        return list;
    }

    private Connection getConnection() throws RoomException {
        try {
            Class.forName(DRIVER_NAME);
            return DriverManager.getConnection(URL, ID, PASSWORD);
        } catch (Exception e) {
            throw new RoomException(RoomException.CODE_DB_CONNECT_ERROR, e);
        }
    }
}