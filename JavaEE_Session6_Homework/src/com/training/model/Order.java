package com.training.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

public class Order implements Serializable{
	private String customerID;
	private int goodsID;
	private Timestamp orderDate;
	private String customerName;
	private String goodsName;
	private int goodsPrice;
	private int buyQuantity;
	
	
	
	public String getCustomerID() {
		return customerID;
	}
	public void setCustomerID(String customerID) {
		this.customerID = customerID;
	}
	public int getGoodsID() {
		return goodsID;
	}
	public void setGoodsID(int goodsID) {
		this.goodsID = goodsID;
	}
	public Date getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(Timestamp orderDate) {
		this.orderDate = orderDate;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public int getGoodsPrice() {
		return goodsPrice;
	}
	public void setGoodsPrice(int goodsPrice) {
		this.goodsPrice = goodsPrice;
	}
	public int getBuyQuantity() {
		return buyQuantity;
	}
	public void setBuyQuantity(int buyQuantity) {
		this.buyQuantity = buyQuantity;
	}
	
	@Override
	public String toString() {
		return "Order [orderDate=" + orderDate + ", customerName="
				+ customerName + ", goodsName=" + goodsName + ", goodsPrice="
				+ goodsPrice + ", buyQuantity=" + buyQuantity + "]";
	}	
}
