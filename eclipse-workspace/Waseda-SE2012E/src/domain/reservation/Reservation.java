/*
 * Copyright(C) 2007-2013 National Institute of Informatics, All rights reserved.
 */
package domain.reservation;

import java.util.Date;

/**
 * Reservation entity<br>
 */
public class Reservation {

    public static final String RESERVATION_STATUS_CREATE  = "create";
    public static final String RESERVATION_STATUS_CONSUME = "consume";

    private String reservationNumber;
    private Date   stayingDate;
    private String status;

    // 追加フィールド: 部屋種別と予約部屋数
    private String roomType;
    private int    roomCount;

    public String getReservationNumber() {
        return reservationNumber;
    }

    public void setReservationNumber(String reservationNumber) {
        this.reservationNumber = reservationNumber;
    }

    public Date getStayingDate() {
        return stayingDate;
    }

    public void setStayingDate(Date stayingDate) {
        this.stayingDate = stayingDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // 以下、追加したゲッター／セッター
    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public int getRoomCount() {
        return roomCount;
    }

    public void setRoomCount(int roomCount) {
        this.roomCount = roomCount;
    }
}