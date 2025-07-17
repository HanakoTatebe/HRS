// ====================
// src/main/java/hrs/control/ReservationControl.java
// ====================
package hrs.control;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import hrs.entity.Reservation;
import hrs.entity.RoomType;

public class ReservationControl {
    private final List<RoomType> types;
    private final List<Reservation> reservations = new ArrayList<>();
    private int nextResNo = 1;
    private int nextRoomNo = 1;

    public ReservationControl(List<RoomType> roomTypes) {
        this.types = roomTypes;
    }

    /**
     * 空室検索：capacity - 重複予約数 を vacancy に設定し返却
     */
    public List<RoomType> findRooms(LocalDate ci, LocalDate co) {
        return types.stream().map(t -> {
            int reserved = reservations.stream()
                .filter(r -> r.getTypeId() == t.getId() && r.overlaps(ci, co))
                .mapToInt(Reservation::getRoomCount)
                .sum();
            t.setVacancy(t.getCapacity() - reserved);
            return t;
        })
        .filter(t -> t.getVacancy() > 0)
        .collect(Collectors.toList());
    }

    // 予約登録
    public Reservation reserveRooms(LocalDate ci, LocalDate co,
                                    int typeId, int count) {
        RoomType target = findRooms(ci, co).stream()
            .filter(t -> t.getId() == typeId)
            .findFirst()
            .orElse(null);
        if (target == null) return null;
        Reservation r = new Reservation(nextResNo++, typeId, count, ci, co);
        reservations.add(r);
        return r;
    }

    // チェックイン
    public int checkIn(int no) {
        for (Reservation r : reservations) {
            if (r.getReservationNumber() == no) {
                int rn = nextRoomNo++;
                r.setRoomNumber(rn);
                return rn;
            }
        }
        return -1;
    }

    // チェックアウト
    public int checkOut(int no) {
        for (var it = reservations.iterator(); it.hasNext(); ) {
            Reservation r = it.next();
            if (r.getReservationNumber() == no) {
                int price = types.stream()
                    .filter(t -> t.getId() == r.getTypeId())
                    .findFirst()
                    .map(RoomType::getPrice)
                    .orElse(0);
                int fee = price * r.getRoomCount();
                it.remove();
                return fee;
            }
        }
        return -1;
    }

    // キャンセル
    public boolean cancelReservation(int no) {
        return reservations.removeIf(r -> r.getReservationNumber() == no);
    }
}