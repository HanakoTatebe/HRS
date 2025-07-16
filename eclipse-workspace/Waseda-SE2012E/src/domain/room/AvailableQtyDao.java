package domain.room;

import java.util.Date;
import java.util.List;

/**
 * DAO for AVAILABLEQTY テーブル
 */
public interface AvailableQtyDao {

    /** (従来) 単一キー（日付のみ）で在庫レコードを取ってくる */
    AvailableQty getAvailableQty(Date date) throws RoomException;

    /** (従来) 初回レコード作成用 */
    void createAbailableQty(AvailableQty availableQty) throws RoomException;

    /** (従来) 更新用 */
    void updateAvailableQty(AvailableQty availableQty) throws RoomException;

    /** ★ 新規：指定日に紐づく全種別の在庫を取得 */
    List<AvailableQty> findByDate(Date date) throws RoomException;
}