package com.training.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.training.dao.BackEndDao;
import com.training.dao.FrontEndDao;
import com.training.model.Goods;
import com.training.model.Member;
import com.training.model.Order;
import com.training.vo.GoodsResult;

public class FrontEndService {
	//單一實例化模式
	//建立自己的物件
	private static FrontEndService frontEndService = new FrontEndService();
	//物件不被實例化
	private FrontEndService (){}
	//需要使用的Dao物件
	private static FrontEndDao frontEndDao= FrontEndDao.getInstance();
	//取得實例化方法
	public static FrontEndService getInstance() {
		return frontEndService;
	}
	private static BackEndService backEndService= BackEndService.getInstance();
		
	public GoodsResult searchGoods(String searchKey, int startRowNo, int endRowNo) { //查詢&分頁查詢

		return frontEndDao.searchGoods(searchKey, startRowNo, endRowNo);
	}

	public static Map<Integer, Goods> queryBuyGoods(Set<Integer> goodsIDs) {
		
		return frontEndDao.queryBuyGoods(goodsIDs);
	}

	public Member queryMemberByIdentificationNo(String customerID) {
		
		return frontEndDao.queryMemberByIdentificationNo(customerID);
	}



	public int buySum(Map<Goods, Integer> goodsOrders) { //購買總額
		int totalCost = 0;
		
		for(Map.Entry<Goods,Integer>entry:goodsOrders.entrySet()){
			totalCost += entry.getKey().getGoodsPrice()*entry.getValue();
		}
		return totalCost;
	}

	public List<Goods> buyTotalQuantity(Map<Goods, Integer> goodsOrders) {//購買商品與數量
		List<Goods> buyGoods = new ArrayList<>();
		
		for(Map.Entry<Goods,Integer>entry:goodsOrders.entrySet()){
			Goods good =new Goods();
			good.setGoodsID(entry.getKey().getGoodsID());
			good.setGoodsName(entry.getKey().getGoodsName());
			good.setGoodsPrice(entry.getKey().getGoodsPrice());
			good.setGoodsQuantity(entry.getValue());
			buyGoods.add(good);
		}
		return buyGoods;
	}



	//用購買清單與SQL資料做比對，確定購買數量
	public static List<Goods> buyTotalCost(List<Goods> buyGoodsList) {
		//購買數量求實際數量
		List<Goods> listSQL =  backEndService.queryGoodById(buyGoodsList);
		List<Goods> newListSQL = new ArrayList<Goods>();
		//預計修改SQL回傳List，做為整個方法回傳值
		for(int i=0;i<listSQL.size();i++){
			//商品下架不能買
			//庫存小於0不給買
			if("1".equals(listSQL.get(i).getStatus())){
				if(listSQL.get(i).getGoodsQuantity()>0){
					if(listSQL.get(i).getGoodsQuantity() > buyGoodsList.get(i).getGoodsQuantity()){
						listSQL.get(i).setGoodsQuantity(buyGoodsList.get(i).getGoodsQuantity());
					}
					newListSQL.add(listSQL.get(i));
				}
			}
		}				
		return newListSQL;
	}
	//計算購買總額
	public int customerBuy(List<Goods> totalCost) {
		int buyMoney = 0;
		for(Goods good : totalCost){
			buyMoney += good.getGoodsPrice() * good.getGoodsQuantity();
		}
		return buyMoney;
	}
	
	//用購買數量來計算
	public List<Goods> buySumTotalCost(List<Goods> buyGoodsList) {
		List<Goods> goodsList = backEndService.queryGoodById(buyGoodsList);
		for(int i=0; i<buyGoodsList.size(); i++){
			buyGoodsList.get(i).setGoodsPrice(goodsList.get(i).getGoodsPrice());
			buyGoodsList.get(i).setGoodsName(goodsList.get(i).getGoodsName());
		}
		return buyGoodsList;
	}
	
	// 將顧客所購買商品扣除更新商品庫存數量
	public boolean batchUpdateGoodsQuantity(List<Goods> buyGoodsList) { //buyGoodsList沒抓到Name
		List<Goods> buyGoods = backEndService.queryGoodById(buyGoodsList);
		boolean result = false;
		for(int i= 0;i < buyGoods.size();i++){
			buyGoods.get(i).setGoodsQuantity(buyGoods.get(i).getGoodsQuantity() - buyGoodsList.get(i).getGoodsQuantity());
			if(buyGoods.get(i).getGoodsQuantity()<=0){
				result = false;
			}
			result = frontEndDao.batchUpdateGoodsQuantity(buyGoods);//Quantity負數
		}
		
		return result;
	}
	//新增資料到Order Dao
	public boolean batchCreateGoodsOrder(List<Order> orders) {
		
		return frontEndDao.batchCreateGoodsOrder(orders);
	}

	public List<Order> listToOrder(List<Goods> buyGoodsList, String customerID) {
		List<Order> orders = new ArrayList<Order>();
		for(Goods good : buyGoodsList){
			Order order = new Order();
			order.setCustomerID(customerID);
			order.setGoodsID(good.getGoodsID());
			order.setGoodsPrice(good.getGoodsPrice());
			order.setBuyQuantity(good.getGoodsQuantity());
			orders.add(order);
		}
		return orders;
	}
	
	//回傳頁面商品選項
	public int goodsIcon(String searchKeyword) {
	
		return frontEndDao.goodsIcon(searchKeyword);
	}
	
	//回傳前端顯示的資料
	public List<Goods> showGoodsService(int page ,String searchKeyword) {
		if(searchKeyword==null){
			searchKeyword="";
		}
		return frontEndDao.returnPage(page ,searchKeyword);
	}
	
	


	

	

	
	
	
	
//	public static void main(String[] args) {
//				
//		// 請先執行 BEVERAGE.sql 至 Local DB
//		FrontEndDao frontEndDao = FrontEndDao.getInstance();
//		
//		// 1.前臺顧客登入查詢
//		String customerID = "D201663865";
//		Member member = frontEndDao.queryMemberByIdentificationNo(customerID);
//		System.out.println(member);
//		System.out.println("----------------------------------------");
//		
//		// 2.前臺顧客瀏灠商品
//		Set<Goods> goods = frontEndDao.searchGoods("ca", 6, 13);
//		goods.stream().forEach(g -> System.out.println(g));
//		System.out.println("----------------------------------------");
//		
//		// 3.前臺顧客購買建立訂單
//		// Step1:查詢顧客所購買商品資料(價格、庫存)
//		Set<BigDecimal> goodsIDs = new HashSet<>();
//		goodsIDs.add(new BigDecimal("19"));
//		goodsIDs.add(new BigDecimal("20"));
//		Map<BigDecimal, Goods> buyGoods = frontEndDao.queryBuyGoods(goodsIDs);
//		buyGoods.values().stream().forEach(g -> System.out.println(g));		
//		
//		// Step2:建立訂單資料
//		// 訂單資料(key:購買商品、value:購買數量)
//		int buyQuantity = 2; // 購買數量皆為2
//		Map<Goods,Integer> goodsOrders = new HashMap<>();
//		goodsIDs.stream().forEach(goodsID -> {
//			Goods g = buyGoods.get(goodsID);			
//			goodsOrders.put(g, buyQuantity); 
//		});
//		// 建立訂單
//		boolean insertSuccess = frontEndDao.batchCreateGoodsOrder(customerID, goodsOrders);
//		if(insertSuccess){System.out.println("建立訂單成功!");}
//		
//		// Step3:交易完成更新扣商品庫存數量
//		// 將顧客所購買商品扣除更新商品庫存數量
//		buyGoods.values().stream().forEach(g -> g.setGoodsQuantity(g.getGoodsQuantity() - buyQuantity));
//		boolean updateSuccess = frontEndDao.batchUpdateGoodsQuantity(buyGoods.values().stream().collect(Collectors.toSet()));
//		if(updateSuccess){System.out.println("商品庫存更新成功!");}
//		
//	}

}
