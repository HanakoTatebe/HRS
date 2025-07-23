// ====================
// src/main/java/hrs/control/ReservationControl.java
// ====================
package hrs.control;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import hrs.entity.Reservation;
import hrs.entity.RoomType;

public class ReservationControl {
    private final List<RoomType> types;
    private final List<Reservation> reservations = new ArrayList<>();
    private int nextResNo = 1;
    private final Map<Integer,Integer> nextRoomSeq = new HashMap<>();

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
        RoomType target = findRooms(ci,co).stream()
            .filter(t->t.getId()==typeId)
            .findFirst().orElse(null);
        if (target==null) return null;
        Reservation r = new Reservation(nextResNo++, typeId, count, ci, co);
        reservations.add(r);
        return r;
    }

    // チェックイン
    public List<Integer> checkIn(int reservationNumber) {
        for (Reservation r : reservations) {
            if (r.getReservationNumber()==reservationNumber) {
                int typeId = r.getTypeId();
                List<Integer> rooms = new ArrayList<>();
                for (int i=0; i<r.getRoomCount(); i++) {
                    int seq = nextRoomSeq.getOrDefault(typeId, 0) + 1;
                    nextRoomSeq.put(typeId, seq);
                    int roomNo = typeId * 100 + seq;
                    r.addRoomNumber(roomNo);
                    rooms.add(roomNo);
                }
                return rooms;
            }
        }
        return List.of();
    }

    //チェックアウト
    public int checkOut(int roomNo) {
        for (var it = reservations.iterator(); it.hasNext(); ) {
            Reservation r = it.next();
            // ここを reservationNumber から roomNumber へ切り替え
            if (r.getRoomNumbers().contains(roomNo)) {
                // 単価取得
                int price = types.stream()
                    .filter(t -> t.getId() == r.getTypeId())
                    .findFirst()
                    .map(RoomType::getPrice)
                    .orElse(0);
                // 宿泊日数を計算 (チェックイン〜チェックアウト間の日数)
                long nights = ChronoUnit.DAYS.between(r.getCheckinDate(), r.getCheckoutDate());
                if (nights <= 0) nights = 1;  // 同日チェックインは1泊扱い
                // 宿泊料計算
                int fee = price * r.getRoomCount() * (int) nights;
                // 予約解除
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