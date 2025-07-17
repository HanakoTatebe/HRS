// ====================
// src/main/java/hrs/boundary/ManagementUI.java
// ====================
package hrs.boundary;

import java.util.List;
import java.util.Scanner;
import hrs.control.ManagementControl;
import hrs.entity.RoomType;

public class ManagementUI {
    private final Scanner scanner = new Scanner(System.in);
    private final ManagementControl control;

    public ManagementUI(ManagementControl control) {
        this.control = control;
    }

    /**
     * 部屋種別登録：
     * 登録後に登録済みの全部屋種別と在庫数を表示します。
     */
    public void inputRoom() {
        System.out.println("部屋登録: 1) シングル 2) ダブル");
        int ch = Integer.parseInt(scanner.nextLine());
        System.out.print("料金: ");
        int price = Integer.parseInt(scanner.nextLine());
        System.out.print("部屋数: ");
        int cnt = Integer.parseInt(scanner.nextLine());
        RoomType t = control.registerRoom(ch, price, cnt);
        System.out.println(t.getName() + " (ID=" + t.getId() + ") 登録完了\n");
        // 登録後に全登録部屋を一覧表示
        showAllRooms(control.getAllRooms());
    }

    /**
     * 全部屋種別一覧表示：
     * 登録済みの部屋種別ごとに料金と現在の在庫数を表示します。
     */
    public void showAllRooms(List<RoomType> list) {
        if (list.isEmpty()) {
            System.out.println("登録済み部屋無し。");
            return;
        }
        System.out.println("登録済み部屋種別一覧:");
        for (RoomType t : list) {
            System.out.printf("名称:%s 料金:%d 部屋数:%d%n",
                t.getName(), t.getPrice(), t.getCapacity());
        }
        System.out.println();
    }
}
