package domain.room;

import java.util.Date;

/**
 * Entity for “日付＋部屋種別ごとの空室数”
 */
public class AvailableQty {
    public static final int AVAILABLE_ALL = -1;

    private Date   date;
    private String roomType;  // 追加
    private int    qty;

    public Date getDate()                { return date; }
    public void setDate(Date date)       { this.date = date; }

    public String getRoomType()          { return roomType; }
    public void setRoomType(String type) { this.roomType = type; }

    public int getQty()                  { return qty; }
    public void setQty(int qty)          { this.qty = qty; }
}