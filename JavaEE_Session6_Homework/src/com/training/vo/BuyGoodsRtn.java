package com.training.vo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.training.dao.DBConnectionFactory;
import com.training.model.Goods;
import com.training.service.FrontEndService;

public class BuyGoodsRtn {
	
		
		//組合成SQL 內的查詢文字
		public static String totalIDs (List<Goods> CustomerOrderGoods){
			String useInWordSQL ="(";
			for(int i =0; i< CustomerOrderGoods.size(); i++){
				useInWordSQL += CustomerOrderGoods.get(i).getGoodsID();
				useInWordSQL += ",";
			}
			//把最後一個","拿掉
			useInWordSQL = useInWordSQL.substring(0, useInWordSQL.length()-1);
			useInWordSQL +=")";
			return useInWordSQL;
		}
		//頁面商品與數量陣列組合成List<Goods>
		public static List<Goods> buyGoodsList(Map<Integer, String> tempBuyMap) {
			List<Goods> Goods = new ArrayList<>();
			for(int key:tempBuyMap.keySet()){
				Goods good = new Goods();
				if(Integer.parseInt(tempBuyMap.get(key))!=0){
					good.setGoodsID(key);
					good.getGoodsName();
					good.setGoodsQuantity(Integer.parseInt(tempBuyMap.get(key)));
					Goods.add(good);
				}
			}
			return Goods;
		}
		//顯示購買清單
		public static String showBuyGoods(List<Goods> buyGoodsList) {
			String message = "";
			message += "======================</br>";
			message += "您購買的商品如下</br>";
			for(Goods good : buyGoodsList){
				message += "商品名稱:" + good.getGoodsName() + ",";
				message += "購買數量:" + good.getGoodsQuantity() + "</br>";
			}
			return message;
		}
		public static int pageService(String pageString, String hiddenpage) { //注意static
			
			int page=1;
			if(pageString != null && !"".equals(pageString)){
				page = Integer.parseInt(pageString);
			}else if(hiddenpage != null && "".equals(hiddenpage)){
				page = Integer.parseInt(hiddenpage);
			}
			return page;
		}
		//分頁
		public static List<Integer> rowListService(int page, int countPage) {
			int row = (page-1)/3;
			List<Integer>rowList = new ArrayList<>();
			int endPage = ((row+1)*3<=countPage)?(row+1)*3:countPage;
			for(int i = row*3+1;i<=endPage;i++){
				rowList.add(i);
			}
			return rowList;
		}
		//每頁數量6筆
		
		public static int countPageService(FrontEndService frontEndService) {
			int count = frontEndService.goodsIcon();
			int countPage = (count%6 == 0)?count/6:(count/6)+1;
			
			return countPage;
		}
		
		
		

}


//將兩個讀取的陣列放到Set<Goods>
//public static Set<Goods> buyGoodsList(Map<Integer,Integer> goodsOrders){
//	Set<Goods> GoodsChoose = new HashSet<>();
//	
//	for(Integer key : goodsOrders.keySet()){
//		
//		String querrySQL="SELECT * FROM BEVERAGE_GOODS WHERE GOODS_ID IN ?";			
//		try 
//		(Connection conn=DBConnectionFactory.getOracleDBConnection();
//		PreparedStatement stmt=conn.prepareStatement(querrySQL)	){
//		stmt.setInt(1,key);
//		
//		try(ResultSet rs=stmt.executeQuery()){
//			while(rs.next()){
//				if(goodsOrders.get(key)!=0){
//				Goods good=new Goods();
//				good.setGoodsID(key);
//				good.setGoodsName(rs.getString("GOODS_NAME"));
//				good.setGoodsPrice(rs.getInt("PRICE"));
//				good.setGoodsQuantity(goodsOrders.get(key));
//				good.setGoodsImageName(rs.getString("IMAGE_NAME"));
//				good.setStatus(rs.getString("STATUS"));
//				GoodsChoose.add(good);
//				}
//			}
//		}catch(SQLException e){
//			throw e;
//		}
//	} catch (SQLException e) {
//		e.printStackTrace();
//	}
//		
//	}
//	return GoodsChoose;	
//}

//算出客人選擇商品的總價
//public static int buySum(Set<Goods> buyGoodsList) {
//	int totalCost = 0;
//	for(Goods good : buyGoodsList){
//		totalCost += good.getGoodsPrice()*good.getGoodsQuantity();
//	}
//	return totalCost;
//}