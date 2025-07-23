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
        if (ch != 1 && ch != 2) {
            System.out.println("エラー: 不正な部屋種別です。1 または 2 を選択してください。\n");
            return;
        }
        System.out.print("料金: ");
        int price = Integer.parseInt(scanner.nextLine());
        if (price <= 0) {
            System.out.println("エラー: 料金は1以上の数値を入力してください。\n");
            return;
        }
        System.out.print("部屋数: ");
        int cnt = Integer.parseInt(scanner.nextLine());
        // -- 総部屋数マイナスチェック（存在する同種別・同料金の合算後） --
        int currentCap = control.getAllRooms().stream()
            .filter(t -> ((ch == 1 && t.getName().equals("シングルルーム"))
                    || (ch == 2 && t.getName().equals("ダブルルーム")))
                    && t.getPrice() == price)
            .mapToInt(RoomType::getCapacity)
            .findFirst()
            .orElse(0);
        if (currentCap + cnt < 0) {
            System.out.println("エラー: 登録後の総部屋数がマイナスになります。処理を中止します。\n");
            return;
        }

        // -- 登録実行 --
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