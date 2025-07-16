package app.reservation;

import java.util.Date;
import java.util.List;

import app.AppException;
import app.ManagerFactory;
import domain.reservation.ReservationException;
import domain.room.Availability;
import domain.room.RoomException;
import domain.room.RoomManager;
import domain.reservation.ReservationManager;

public class ReserveRoomControl {

    /** 宿泊日の空室情報を取得 */
    public List<Availability> getAvailability(Date stayingDate) throws AppException {
        try {
            return getRoomManager().getRoomAvailabilityByType(stayingDate);
        } catch (RoomException e) {
            AppException ex = new AppException("空室取得に失敗しました", e);
            ex.getDetailMessages().addAll(e.getDetailMessages());
            throw ex;
        }
    }

    /** 宿泊予約を行う（部屋種別と部屋数を指定） */
    public String makeReservation(Date stayingDate, String roomType, int roomCount) throws AppException {
        try {
            // 指定種別の在庫数を roomCount 分だけ減らす
            getRoomManager().updateRoomAvailableQtyByType(stayingDate, roomType, -roomCount);
            // 予約を作成（種別・部屋数を渡す）
            return getReservationManager().createReservation(stayingDate, roomType, roomCount);
        } catch (RoomException | ReservationException e) {
            AppException ex = new AppException("予約に失敗しました", e);
            ex.getDetailMessages().add(e.getMessage());
            if (e instanceof RoomException) {
                ex.getDetailMessages().addAll(((RoomException)e).getDetailMessages());
            } else {
                ex.getDetailMessages().addAll(((ReservationException)e).getDetailMessages());
            }
            throw ex;
        }
    }

    private RoomManager getRoomManager() {
        return ManagerFactory.getInstance().getRoomManager();
    }

    private ReservationManager getReservationManager() {
        return ManagerFactory.getInstance().getReservationManager();
    }
}