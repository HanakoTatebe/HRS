// ====================
// src/main/java/hrs/boundary/ReservationUI.java
// ====================
package hrs.boundary;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;
import hrs.entity.Reservation;
import hrs.entity.RoomType;

public class ReservationUI {
    private final Scanner scanner = new Scanner(System.in);
    private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public LocalDate[] inputDates() {
        System.out.print("チェックイン (yyyy-MM-dd): ");
        LocalDate ci = LocalDate.parse(scanner.nextLine(), fmt);
        System.out.print("チェックアウト (yyyy-MM-dd): ");
        LocalDate co = LocalDate.parse(scanner.nextLine(), fmt);

        // 入力検証：チェックアウトはチェックインより後
        if (!co.isAfter(ci)) {
            System.out.println("エラー: チェックアウト日はチェックイン日より後の日付を指定してください。\n");
            return null;  // メニューに戻す
        }
        return new LocalDate[]{ci, co};
    }

    public void showRooms(List<RoomType> types) {
        if (types.isEmpty()) { System.out.println("空室無し。\n"); return; }
            System.out.println("空室一覧:");
            for (RoomType t : types) {
                System.out.printf("ID:%d 名称:%s 料金:%d 空室:%d%n",
                    t.getId(), t.getName(), t.getPrice(), t.getVacancy());
        }
        System.out.println();
    }

    public int selectRoomType() {
        System.out.print("部屋種別ID: ");
        return Integer.parseInt(scanner.nextLine());
    }

    public int selectCount() {
        System.out.print("部屋数: ");
        return Integer.parseInt(scanner.nextLine());
    }

    public void showReservationNumber(Reservation r) {
        System.out.println("予約完了: 番号=" + r.getReservationNumber() + "\n");
    }

    public void showRoomNumbers(List<Integer> roomNos) {
        System.out.println("チェックイン完了: 部屋番号=" + roomNos + "\n");
    }
}