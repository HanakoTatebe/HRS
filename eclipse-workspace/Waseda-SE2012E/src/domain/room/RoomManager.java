package domain.room;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

import util.DateUtil;
import domain.DaoFactory;

/**
 * Manager for rooms
 */
public class RoomManager {

    /**
     * 指定日の全体在庫数を変更（旧来の一括操作）
     */
    public void updateRoomAvailableQty(Date stayingDate, int qtyOfChange) throws RoomException {
        if (stayingDate == null) {
            throw new NullPointerException("stayingDate");
        }
        if (qtyOfChange == 0) {
            return;
        }

        AvailableQtyDao availableQtyDao = getAvailableQtyDao();
        AvailableQty availableQty = availableQtyDao.getAvailableQty(stayingDate);

        if (availableQty == null) {
            availableQty = new AvailableQty();
            availableQty.setRoomType("ALL");
            availableQty.setQty(AvailableQty.AVAILABLE_ALL);
            availableQty.setDate(stayingDate);
        }

        int max = getMaxAvailableQty();
        if (availableQty.getQty() == AvailableQty.AVAILABLE_ALL) {
            availableQty.setQty(max);
            availableQtyDao.createAbailableQty(availableQty);
        }

        int changed = availableQty.getQty() + qtyOfChange;
        if (changed >= 0 && changed <= max) {
            availableQty.setQty(changed);
            availableQty.setDate(stayingDate);
            availableQtyDao.updateAvailableQty(availableQty);
        } else {
            RoomException ex =
                new RoomException(RoomException.CODE_AVAILABLE_QTY_OUT_OF_BOUNDS);
            ex.getDetailMessages()
              .add("staying_date[" + DateUtil.convertToString(stayingDate) + "]");
            throw ex;
        }
    }

    /**
     * 指定日の指定種別ごとの在庫数を変更（部屋種別予約／キャンセル用）
     */
    public void updateRoomAvailableQtyByType(Date stayingDate,
                                             String roomType,
                                             int qtyOfChange)
            throws RoomException {
        if (stayingDate == null) throw new NullPointerException("stayingDate");
        if (roomType    == null) throw new NullPointerException("roomType");
        if (qtyOfChange == 0)    return;

        RoomDao roomDao = getRoomDao();
        List<Room> emptyByType = roomDao.getEmptyRooms().stream()
            .filter(r -> getRoomTypeFromNumber(r.getRoomNumber())
                        .equalsIgnoreCase(roomType.trim()))
            .collect(Collectors.toList());

        if (qtyOfChange < 0) {
            int needed = -qtyOfChange;
            if (emptyByType.size() < needed) {
                throw new RoomException(RoomException.CODE_EMPTYROOM_NOT_FOUND);
            }
            for (int i = 0; i < needed; i++) {
                Room r = emptyByType.get(i);
                r.setStayingDate(stayingDate);
                roomDao.updateRoom(r);
            }
        } else {
            for (int i = 0; i < qtyOfChange && i < emptyByType.size(); i++) {
                Room r = emptyByType.get(i);
                r.setStayingDate(null);
                roomDao.updateRoom(r);
            }
        }
    }

    private int getMaxAvailableQty() throws RoomException {
        return getRoomDao().getRooms().size();
    }

    /**
     * チェックイン時：部屋を割り当てる
     */
    public String assignCustomer(Date stayingDate) throws RoomException {
        if (stayingDate == null) throw new NullPointerException("stayingDate");
        List<Room> empty = getRoomDao().getEmptyRooms();
        if (empty.isEmpty()) throw new RoomException(RoomException.CODE_EMPTYROOM_NOT_FOUND);
        Room r = empty.get(0);
        r.setStayingDate(stayingDate);
        getRoomDao().updateRoom(r);
        return r.getRoomNumber();
    }

    /**
     * チェックアウト時：利用日を取得して空室に戻す
     */
    public Date removeCustomer(String roomNumber) throws RoomException {
        if (roomNumber == null) throw new NullPointerException("roomNumber");
        Room r = getRoomDao().getRoom(roomNumber);
        if (r == null) {
            RoomException ex = new RoomException(RoomException.CODE_ROOM_NOT_FOUND);
            ex.getDetailMessages().add("room_number[" + roomNumber + "]");
            throw ex;
        }
        Date d = r.getStayingDate();
        if (d == null) {
            RoomException ex = new RoomException(RoomException.CODE_ROOM_NOT_FULL);
            ex.getDetailMessages().add("room_number[" + roomNumber + "]");
            throw ex;
        }
        r.setStayingDate(null);
        getRoomDao().updateRoom(r);
        return d;
    }

    /**
     * チェックアウト処理：空室数を＋1
     */
    public void checkOut(String roomNumber, Date checkOutDate) throws RoomException {
        Date d = removeCustomer(roomNumber);
        updateRoomAvailableQty(d, +1);
    }

    /**
     * 宿泊料金を計算して返す
     */
    public int calculateCharge(String roomNumber) throws RoomException {
        String type = getRoomTypeFromNumber(roomNumber);
        return getPriceByType(type);
    }

    /**
     * 日付＋種別ごとの空室数を取得し、Presentation用の Availability に詰める
     */
    public List<Availability> getRoomAvailabilityByType(Date stayingDate)
            throws RoomException {
        AvailableQtyDao qtyDao = getAvailableQtyDao();
        List<AvailableQty> records = qtyDao.findByDate(stayingDate);

        List<Availability> availList = new ArrayList<>();
        for (AvailableQty q : records) {
            String type  = q.getRoomType();
            int    price = getPriceByType(type);
            availList.add(new Availability(type, price, q.getQty()));
        }
        return availList;
    }

    private String getRoomTypeFromNumber(String roomNumber) {
        if (roomNumber.startsWith("1")) return "Single";
        if (roomNumber.startsWith("2")) return "Double";
        return "Unknown";
    }

    private int getPriceByType(String type) {
        if ("Single".equalsIgnoreCase(type)) return 8000;
        if ("Double".equalsIgnoreCase(type)) return 10000;
        return 0;
    }

    // ────────── DAO取得 ──────────

    private AvailableQtyDao getAvailableQtyDao() {
        return DaoFactory.getInstance().getAvailableQtyDao();
    }

    private RoomDao getRoomDao() {
        return DaoFactory.getInstance().getRoomDao();
    }
}