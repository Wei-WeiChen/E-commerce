package com.training.formbean;

import org.apache.struts.action.ActionForm;

public class FrontendformData extends ActionForm{
	
	private String[] goodsID;
	private String[] buyQuantity;
	private String customerID;
	private String searchKeyword;
	private int startPage;
	private int endPage;
	private int inputMoney;
	

	public String[] getGoodsID() {
		return goodsID;
	}
	public void setGoodsID(String[] goodsID) {
		this.goodsID = goodsID;
	}
	public String[] getBuyQuantity() {
		return buyQuantity;
	}
	public void setBuyQuantity(String[] buyQuantity) {
		this.buyQuantity = buyQuantity;
	}
	public String getCustomerID() {
		return customerID;
	}
	public void setCustomerID(String customerID) {
		this.customerID = customerID;
	}
	public String getSearchKeyword() {
		return searchKeyword;
	}
	public void setSearchGoods(String searchKeyword) {
		this.searchKeyword = searchKeyword;
	}
	public int getStartPage() {
		return startPage;
	}
	public void setStartPage(int startPage) {
		this.startPage = startPage;
	}
	public int getEndPage() {
		return endPage;
	}
	public void setEndPage(int endPage) {
		this.endPage = endPage;
	}
	public int getInputMoney() {
		return inputMoney;
	}
	public void setInputMoney(int inputMoney) {
		this.inputMoney = inputMoney;
	}
	
}
