/*
 * Copyright(C) 2007-2013 National Institute of Informatics, All rights reserved.
 */
package app.reservation;

import java.util.Date;
import java.util.List;

import app.AppException;
import domain.room.Availability;  // ✅ 空室情報クラスを使うためのインポート

/**
 * Form class for Reserve Room
 */
public class ReserveRoomForm {

 private ReserveRoomControl reserveRoomHandler = new ReserveRoomControl();

 private Date stayingDate;

 // ✅ ① 追加：部屋種別と数を保持するフィールド
 private String roomType;
 private int roomCount;

 private ReserveRoomControl getReserveRoomHandler() {
  return reserveRoomHandler;
 }

 // ✅ ② 追加：部屋種別と数を外からセットできるようにする
 public void setRoomType(String roomType) {
  this.roomType = roomType;
 }

 public void setRoomCount(int roomCount) {
  this.roomCount = roomCount;
 }

 public Date getStayingDate() {
  return stayingDate;
 }

 public void setStayingDate(Date stayingDate) {
  this.stayingDate = stayingDate;
 }

 // ✅ ③ 追加：空室状況を表示する
 public void displayAvailability() throws AppException {
  List<Availability> list = reserveRoomHandler.getAvailability(stayingDate);
  System.out.println("Room Type | Price | Available");
  for (Availability a : list) {
   System.out.printf("%s | %,d円 | %d室%n", a.getTypeName(), a.getPrice(), a.getQty());
  }
 }

 // ✅ ④ 修正：予約時に部屋種別と数を引数に追加する
 public String submitReservation() throws AppException {
  return reserveRoomHandler.makeReservation(stayingDate, roomType, roomCount);
 }
}