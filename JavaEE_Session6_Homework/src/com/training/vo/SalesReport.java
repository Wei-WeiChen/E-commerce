package com.training.vo;

import java.sql.Timestamp;

public class SalesReport {
	// 訂單編號
	private long orderID;
	// 顧客姓名
	private String customerName;
	// 購買日期
	private Timestamp orderDate;
	// 飲料名稱
	private String goodsName;
	// 商品金額(購買單價)
	private int goodsBuyPrice;
	// 購買數量
	private int buyQuantity;	
	// 購買金額
	private int buyAmount;
	
	public long getOrderID() {
		return orderID;
	}
	public void setOrderID(long orderID) {
		this.orderID = orderID;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public Timestamp getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(Timestamp orderDate) {
		this.orderDate = orderDate;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public int getGoodsBuyPrice() {
		return goodsBuyPrice;
	}
	public void setGoodsBuyPrice(int goodsBuyPrice) {
		this.goodsBuyPrice = goodsBuyPrice;
	}
	public int getBuyQuantity() {
		return buyQuantity;
	}
	public void setBuyQuantity(int buyQuantity) {
		this.buyQuantity = buyQuantity;
	}
	public int getBuyAmount() {
		return buyAmount;
	}
	public void setBuyAmount(int buyAmount) {
		this.buyAmount = buyAmount;
	}
	@Override
	public String toString() {
		return "SalesReport [orderID=" + orderID + ", customerName="
				+ customerName + ", orderDate=" + orderDate + ", goodsName="
				+ goodsName + ", goodsBuyPrice=" + goodsBuyPrice
				+ ", buyQuantity=" + buyQuantity + ", buyAmount=" + buyAmount
				+ "]";
	}	
}
