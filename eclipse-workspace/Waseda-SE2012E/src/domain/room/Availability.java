package domain.room;

public class Availability {
    private String typeName;
    private int price;
    private int qty;

    public Availability(String typeName, int price, int qty) {
        this.typeName = typeName;
        this.price = price;
        this.qty = qty;
    }

    public String getTypeName() {
        return typeName;
    }

    public int getPrice() {
        return price;
    }

    public int getQty() {
        return qty;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }
}