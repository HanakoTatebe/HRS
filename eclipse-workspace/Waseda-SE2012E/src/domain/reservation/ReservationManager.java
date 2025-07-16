/*
 * Copyright(C) 2007-2013 National Institute of Informatics, All rights reserved.
 */
package domain.reservation;

import java.util.Calendar;
import java.util.Date;
import domain.DaoFactory;

/**
 * Manager for reservations
 */
public class ReservationManager {

    /**
     * 新規予約を作成（種別と部屋数を含む）
     */
    public String createReservation(Date stayingDate, String roomType, int roomCount)
            throws ReservationException, NullPointerException {
        if (stayingDate == null) {
            throw new NullPointerException("stayingDate");
        }
        if (roomType == null) {
            throw new NullPointerException("roomType");
        }
        if (roomCount <= 0) {
            throw new NullPointerException("roomCount");
        }

        // 予約エンティティを作成
        Reservation reservation = new Reservation();
        String reservationNumber = generateReservationNumber();
        reservation.setReservationNumber(reservationNumber);
        reservation.setStayingDate(stayingDate);
        reservation.setStatus(Reservation.RESERVATION_STATUS_CREATE);
        reservation.setRoomType(roomType);
        reservation.setRoomCount(roomCount);

        // DBに保存
        ReservationDao dao = getReservationDao();
        dao.createReservation(reservation);
        return reservationNumber;
    }

    /**
     * 予約を消費（チェックインなど）し、利用日を返却する
     */
    public Date consumeReservation(String reservationNumber) throws ReservationException, NullPointerException {
        if (reservationNumber == null) {
            throw new NullPointerException("reservationNumber");
        }

        ReservationDao dao = getReservationDao();
        Reservation reservation = dao.getReservation(reservationNumber);
        if (reservation == null) {
            ReservationException exception = new ReservationException(
                    ReservationException.CODE_RESERVATION_NOT_FOUND);
            exception.getDetailMessages().add("reservation_number[" + reservationNumber + "]");
            throw exception;
        }
        if (Reservation.RESERVATION_STATUS_CONSUME.equals(reservation.getStatus())) {
            ReservationException exception = new ReservationException(
                    ReservationException.CODE_RESERVATION_ALREADY_CONSUMED);
            exception.getDetailMessages().add("reservation_number[" + reservationNumber + "]");
            throw exception;
        }

        Date stayingDate = reservation.getStayingDate();
        reservation.setStatus(Reservation.RESERVATION_STATUS_CONSUME);
        dao.updateReservation(reservation);
        return stayingDate;
    }

    /**
     * 予約番号を発番する（ミリ秒単位）
     */
    private synchronized String generateReservationNumber() {
        Calendar calendar = Calendar.getInstance();
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            // ignore
        }
        return String.valueOf(calendar.getTimeInMillis());
    }

    private ReservationDao getReservationDao() {
        return DaoFactory.getInstance().getReservationDao();
    }
}