// ====================
// src/main/java/hrs/control/RoomCancelControl.java
// ====================
package hrs.control;

public class RoomCancelControl {
    private final ReservationControl resCtrl;
    public RoomCancelControl(ReservationControl resCtrl) {
        this.resCtrl = resCtrl;
    }
    public void cancelRoom(int no) {
        boolean ok = resCtrl.cancelReservation(no);
        System.out.println(ok?"キャンセル完了\n":"該当予約なし\n");
    }
}