// ====================
// src/main/java/hrs/entity/Room.java
// ====================
package hrs.entity;

public class Room {
    private int roomNumber;
    private RoomType roomType;

    public int getRoomNumber() {
        return roomNumber;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public int getAllValue(int nights, int count) {
    return roomType.getPrice() * nights * count;
}


    // constructor, setters omitted
}