/*
 * Copyright(C) 2007-2013 National Institute of Informatics, All rights reserved.
 */
package domain.room;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import util.DateUtil;

/**
 * DB SQL implementation of RoomDao
 */
public class RoomSqlDao implements RoomDao {

    private static final String ID          = "sa";
    private static final String PASSWORD    = "";
    private static final String DRIVER_NAME = "org.hsqldb.jdbcDriver";
    private static final String URL         = "jdbc:hsqldb:hsql://localhost;shutdown=true";
    private static final String TABLE_NAME  = "ROOM";

    @Override
    public List<String> getRooms() throws RoomException {
        List<String> roomList = new ArrayList<>();
        String sql = "SELECT roomnumber FROM " + TABLE_NAME + ";";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                roomList.add(rs.getString("roomnumber"));
            }
        } catch (SQLException e) {
            RoomException ex = new RoomException(RoomException.CODE_DB_EXEC_QUERY_ERROR, e);
            ex.getDetailMessages().add("getRooms()");
            throw ex;
        }
        return roomList;
    }

    @Override
    public List<Room> getEmptyRooms() throws RoomException {
        List<Room> emptyRoomList = new ArrayList<>();
        String sql = "SELECT roomnumber FROM " + TABLE_NAME + " WHERE stayingdate='';";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Room room = new Room();
                room.setRoomNumber(rs.getString("roomnumber"));
                emptyRoomList.add(room);
            }
        } catch (SQLException e) {
            RoomException ex = new RoomException(RoomException.CODE_DB_EXEC_QUERY_ERROR, e);
            ex.getDetailMessages().add("getEmptyRooms()");
            throw ex;
        }
        return emptyRoomList;
    }

    @Override
    public Room getRoom(String roomNumber) throws RoomException {
        Room room = null;
        String sql = "SELECT roomnumber, stayingdate FROM " + TABLE_NAME
                   + " WHERE ROOMNUMBER='" + roomNumber + "';";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                room = new Room();
                room.setRoomNumber(roomNumber);
                room.setStayingDate(DateUtil.convertToDate(rs.getString("stayingdate")));
            }
        } catch (SQLException e) {
            RoomException ex = new RoomException(RoomException.CODE_DB_EXEC_QUERY_ERROR, e);
            ex.getDetailMessages().add("getRoom()");
            throw ex;
        }
        return room;
    }

    @Override
    public void updateRoom(Room room) throws RoomException {
        String dateStr = (room.getStayingDate() == null)
            ? "''"
            : "'" + DateUtil.convertToString(room.getStayingDate()) + "'";
        String sql = "UPDATE " + TABLE_NAME
                   + " SET stayingdate=" + dateStr
                   + " WHERE roomnumber='" + room.getRoomNumber() + "';";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            RoomException ex = new RoomException(RoomException.CODE_DB_EXEC_QUERY_ERROR, e);
            ex.getDetailMessages().add("updateRoom()");
            throw ex;
        }
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