// ====================
// src/main/java/hrs/Main.java
// ====================
package hrs;

import java.time.LocalDate;
import java.util.Scanner;
import hrs.boundary.ManagementUI;
import hrs.boundary.ReservationUI;
import hrs.control.ManagementControl;
import hrs.control.ReservationControl;
import hrs.control.RoomCancelControl;
import hrs.entity.Reservation;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        var mgmt    = new ManagementControl();
        var resCtrl = new ReservationControl(mgmt.getAllRooms());
        var cancel  = new RoomCancelControl(resCtrl);
        var uiMgmt  = new ManagementUI(mgmt);
        var uiRes   = new ReservationUI();

        while (true) {
            System.out.println("=== HRS メニュー ===");
            System.out.println("1. 部屋登録");
            System.out.println("2. 部屋検索");
            System.out.println("3. 予約");
            System.out.println("4. キャンセル");
            System.out.println("5. チェックイン");
            System.out.println("6. チェックアウト");
            System.out.println("0. 終了");
            System.out.print("選択: ");
            int ch = Integer.parseInt(scanner.nextLine());
            switch (ch) {
                case 1:
                    uiMgmt.inputRoom();
                    break;
                case 2: {
                    LocalDate[] d = uiRes.inputDates();
                    uiRes.showRooms(resCtrl.findRooms(d[0], d[1]));
                    break;
                }
                case 3: {
                    LocalDate[] d = uiRes.inputDates();
                    int tid = uiRes.selectRoomType();
                    int cnt = uiRes.selectCount();
                    Reservation r = resCtrl.reserveRooms(d[0], d[1], tid, cnt);
                    if (r != null) uiRes.showReservationNumber(r);
                    else System.out.println("予約失敗\n");
                    break;
                }
                case 4:
                    System.out.print("予約番号: ");
                    cancel.cancelRoom(Integer.parseInt(scanner.nextLine()));
                    break;
                case 5:
                    System.out.print("予約番号: ");
                    int in = Integer.parseInt(scanner.nextLine());
                    int rn = resCtrl.checkIn(in);
                    if (rn > 0) uiRes.showRoomNumber(rn);
                    else System.out.println("該当なし\n");
                    break;
                case 6:
                    System.out.print("予約番号: ");
                    int out = Integer.parseInt(scanner.nextLine());
                    int fee = resCtrl.checkOut(out);
                    if (fee >= 0) System.out.println("宿泊料=" + fee + "円\n");
                    else System.out.println("該当なし\n");
                    break;
                case 0:
                    System.out.println("終了");
                    System.exit(0);
                default:
                    System.out.println("無効選択\n");
            }
        }
    }
}