// ====================
// src/main/java/hrs/entity/SingleRoom.java
// ====================
package hrs.entity;

public class SingleRoom extends RoomType {
    public SingleRoom(int id, int price, int capacity) {
        super(id, "シングルルーム", price, capacity);
    }
}