// ====================
// src/main/java/hrs/entity/RoomType.java
// ====================
package hrs.entity;

public abstract class RoomType {
    private final int id;
    private final String name;
    private final int price;
    private int capacity;
    private int vacancy;

    public RoomType(int id, String name, int price, int capacity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.capacity = capacity;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public int getPrice() { return price; }
    public int getCapacity() { return capacity; }

    // 初期登録時の部屋数を増やす
    public void incrementCapacity(int n) {
        this.capacity += n;
    }

    // 動的に計算した空室数を設定／取得
    public void setVacancy(int v) { this.vacancy = v; }
    public int getVacancy() { return vacancy; }
}