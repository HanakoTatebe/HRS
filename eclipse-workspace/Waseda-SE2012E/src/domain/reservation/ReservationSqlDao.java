package domain.reservation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import util.DateUtil;

/**
 * DB SQL implementation of ReservationDao interface
 */
public class ReservationSqlDao implements ReservationDao {

    private static final String ID         = "sa";
    private static final String PASSWORD   = "";
    private static final String DRIVER_NAME= "org.hsqldb.jdbcDriver";
    private static final String URL        = "jdbc:hsqldb:hsql://localhost;shutdown=true";
    private static final String TABLE_NAME = "RESERVATION";

    @Override
    public Reservation getReservation(String reservationNumber) throws ReservationException {
        StringBuffer sql = new StringBuffer();
        Statement statement = null;
        ResultSet resultSet = null;
        Connection connection = null;
        Reservation reservation = null;
        try {
            connection = getConnection();
            statement = connection.createStatement();
            sql.append("SELECT reservationnumber, stayingdate, status, room_type, room_count FROM ")
               .append(TABLE_NAME)
               .append(" WHERE RESERVATIONNUMBER='")
               .append(reservationNumber)
               .append("';");

            resultSet = statement.executeQuery(sql.toString());
            if (resultSet.next()) {
                reservation = new Reservation();
                reservation.setReservationNumber(reservationNumber);
                reservation.setStatus(resultSet.getString("status"));
                reservation.setStayingDate(DateUtil.convertToDate(resultSet.getString("stayingDate")));
                reservation.setRoomType(resultSet.getString("room_type"));
                reservation.setRoomCount(resultSet.getInt("room_count"));
            }
        } catch (SQLException e) {
            ReservationException ex = new ReservationException(
                ReservationException.CODE_DB_EXEC_QUERY_ERROR, e);
            ex.getDetailMessages().add("getReservation()");
            throw ex;
        } finally {
            close(resultSet, statement, connection);
        }
        return reservation;
    }

    @Override
    public void createReservation(Reservation reservation) throws ReservationException {
        StringBuffer sql = new StringBuffer();
        Statement statement = null;
        Connection connection = null;
        try {
            connection = getConnection();
            statement = connection.createStatement();
            sql.append("INSERT INTO ")
               .append(TABLE_NAME)
               .append(" (reservationNumber, stayingDate, status, room_type, room_count) VALUES ('")
               .append(reservation.getReservationNumber()).append("','")
               .append(DateUtil.convertToString(reservation.getStayingDate())).append("','")
               .append(reservation.getStatus()).append("','")
               .append(reservation.getRoomType()).append("',")
               .append(reservation.getRoomCount()).append(");");

            statement.executeUpdate(sql.toString());
        } catch (SQLException e) {
            ReservationException ex = new ReservationException(
                ReservationException.CODE_DB_EXEC_QUERY_ERROR, e);
            ex.getDetailMessages().add("createReservation()");
            throw ex;
        } finally {
            close(null, statement, connection);
        }
    }

    @Override
    public void updateReservation(Reservation reservation) throws ReservationException {
        StringBuffer sql = new StringBuffer();
        Statement statement = null;
        Connection connection = null;
        try {
            connection = getConnection();
            statement = connection.createStatement();
            sql.append("UPDATE ")
               .append(TABLE_NAME)
               .append(" SET status='")
               .append(reservation.getStatus())
               .append("' WHERE reservationNumber='")
               .append(reservation.getReservationNumber())
               .append("';");

            statement.executeUpdate(sql.toString());
        } catch (SQLException e) {
            ReservationException ex = new ReservationException(
                ReservationException.CODE_DB_EXEC_QUERY_ERROR, e);
            ex.getDetailMessages().add("updateReservation()");
            throw ex;
        } finally {
            close(null, statement, connection);
        }
    }

    private Connection getConnection() throws ReservationException {
        try {
            Class.forName(DRIVER_NAME);
            return DriverManager.getConnection(URL, ID, PASSWORD);
        } catch (Exception e) {
            throw new ReservationException(ReservationException.CODE_DB_CONNECT_ERROR, e);
        }
    }

    private void close(ResultSet rs, Statement stmt, Connection conn) throws ReservationException {
        try {
            if (rs   != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            throw new ReservationException(ReservationException.CODE_DB_CLOSE_ERROR, e);
        }
    }
}