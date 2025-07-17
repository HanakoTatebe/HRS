// ====================
// src/main/java/hrs/entity/Reservation.java
// ====================
package hrs.entity;

import java.time.LocalDate;

public class Reservation {
    private final int reservationNumber;
    private final int typeId;
    private final int roomCount;
    private final LocalDate checkinDate;
    private final LocalDate checkoutDate;
    private int roomNumber = -1;

    public Reservation(int reservationNumber,
                       int typeId,
                       int roomCount,
                       LocalDate checkinDate,
                       LocalDate checkoutDate) {
        this.reservationNumber = reservationNumber;
        this.typeId = typeId;
        this.roomCount = roomCount;
        this.checkinDate = checkinDate;
        this.checkoutDate = checkoutDate;
    }

    public int getReservationNumber() { return reservationNumber; }
    public int getTypeId() { return typeId; }
    public int getRoomCount() { return roomCount; }
    public LocalDate getCheckinDate() { return checkinDate; }
    public LocalDate getCheckoutDate() { return checkoutDate; }
    public int getRoomNumber() { return roomNumber; }
    public void setRoomNumber(int rn) { this.roomNumber = rn; }

    /** 指定期間と重複しているか判定 */
    public boolean overlaps(LocalDate ci, LocalDate co) {
        return !(co.isBefore(checkinDate) || ci.isAfter(checkoutDate));
    }
}
