/*
 * Copyright(C) 2007-2013 National Institute of Informatics, All rights reserved.
 */
package app.checkout;

import java.util.Date;
import java.util.List;

import app.AppException;
import app.ManagerFactory;
import domain.payment.PaymentManager;
import domain.payment.PaymentException;
import domain.room.RoomManager;
import domain.room.RoomException;

/**
 * Control class for Check-out Customer
 * 
 */
public class CheckOutRoomControl {

 /**
     * 指定された部屋番号のチェックアウト処理を行う
     *
     * @param roomNumber フロントが受け取った部屋番号
     * @throws AppException ドメイン層の例外をラップしてスロー
     */
 
 public void checkOut(String roomNumber) throws AppException {
  try {
   // --- Clear room ---
            // RoomManager にチェックアウト（日付戻し・状態更新）を依頼
            RoomManager roomMgr = getRoomManager();
            roomMgr.checkOut(roomNumber, new Date());

            // --- Calculate charge ---
            // チェックアウト日時を元に宿泊料を取得
            int amount = roomMgr.calculateCharge(roomNumber);

            // --- Consume payment ---
            // PaymentManager で支払い処理を実行
            PaymentManager payMgr = getPaymentManager();
            payMgr.processPayment(roomNumber, amount, new Date());
  }
  catch (RoomException e) {
   AppException exception = new AppException("Failed to check-out", e);
   exception.getDetailMessages().add(e.getMessage());
   exception.getDetailMessages().addAll(e.getDetailMessages());
   throw exception;
  }
  catch (PaymentException e) {
     AppException exception = new AppException("Failed to process payment", e);
     exception.getDetailMessages().add(e.getMessage());
	 List<String> details = e.getDetailMessages();
     exception.getDetailMessages().addAll(details);
     throw exception;
 }

 }

 private RoomManager getRoomManager() {
  return ManagerFactory.getInstance().getRoomManager();
 }

 private PaymentManager getPaymentManager() {
  return ManagerFactory.getInstance().getPaymentManager();
 }
}