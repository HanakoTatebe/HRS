// ====================
// src/main/java/hrs/control/ManagementControl.java
// ====================
package hrs.control;

import java.util.ArrayList;
import java.util.List;
import hrs.entity.RoomType;
import hrs.entity.SingleRoom;
import hrs.entity.DoubleRoom;

public class ManagementControl {
    private final List<RoomType> types = new ArrayList<>();
    private int nextId = 1;

    /**
     * 種別(1=Single,2=Double)かつ料金一致であれば容量加算、
     * それ以外は新規登録
     */
    public RoomType registerRoom(int typeChoice, int price, int count) {
        RoomType found = types.stream()
            .filter(t -> ((typeChoice == 1 && t instanceof SingleRoom)
                        || (typeChoice == 2 && t instanceof DoubleRoom))
                        && t.getPrice() == price)
            .findFirst()
            .orElse(null);

        if (found != null) {
            found.incrementCapacity(count);
            return found;
        }

        RoomType t = (typeChoice == 1)
            ? new SingleRoom(nextId++, price, count)
            : new DoubleRoom(nextId++, price, count);
        types.add(t);
        return t;
    }

    public List<RoomType> getAllRooms() {
        return types;
    }
}