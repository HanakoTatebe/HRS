/*
 * Copyright(C) 2007-2013 National Institute of Informatics, All rights reserved.
 */
package domain.room;

import java.util.List;

/**
 * Interface for accessing Room Data Object
 */
public interface RoomDao {

    /**
     * 全ての部屋番号一覧を返す
     * @return List of roomNumber strings
     */
    List<String> getRooms() throws RoomException;

    /**
     * 空室(Room オブジェクト) の一覧を返す
     * @return List of Room
     */
    List<Room> getEmptyRooms() throws RoomException;

    /**
     * 指定の部屋番号で Room を取得
     */
    Room getRoom(String roomNumber) throws RoomException;

    /**
     * 部屋情報を更新（チェックイン／チェックアウト時の stayingDate 更新用）
     */
    void updateRoom(Room room) throws RoomException;
}