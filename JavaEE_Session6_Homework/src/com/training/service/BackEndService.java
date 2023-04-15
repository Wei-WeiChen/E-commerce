package com.training.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

import com.training.dao.BackEndDao;
import com.training.model.Goods;
import com.training.vo.BuyGoodsRtn;
import com.training.vo.SalesReport;

public class BackEndService {
	
	private static BackEndService backEndService = new BackEndService();
	
	private BackEndService (){}
	
	private BackEndDao backendDao= BackEndDao.getInstance();

	public final static SimpleDateFormat sf = new SimpleDateFormat("yyyy/MM/dd");
	
	public static BackEndService getInstance() {
		return backEndService;
	}
	
	public Set<Goods> queryGoods() {
		return backendDao.queryGoods();
	}
	
	public boolean updateGoods(Goods goods){
		return backendDao.updateGoods(goods);
	}
	
	public boolean createGoods(Goods goods) {
		
		
		
		return backendDao.createGoods(goods);
	}
	
	public Set<SalesReport> queryOrderBetweenDate(String startDate,String endDate) {		
		return backendDao.queryOrderBetweenDate(startDate, endDate);
	}

	public boolean compareTime(String startDate, String endDate) {
		String timeStart = startDate.replaceAll("-", "");
		String timeEnd = endDate.replaceAll("-", "");
		if(Integer.parseInt(timeStart) <= Integer.parseInt(timeEnd)){
			return true;
		}else{
			return false;
		}
	}
	//頁面資料List對應資料庫List
	public List<Goods> queryGoodById(List<Goods> goods) {
		String ID = BuyGoodsRtn.totalIDs(goods);
		List<Goods> idGoods = backendDao.queryGoodById(ID);
		return idGoods;
	}
	

	
	
	
//	public static void main(String[] args) {
//		// 請先執行 BEVERAGE.sql 至 Local DB
//		BackEndDao backendDao = BackEndDao.getInstance();
//		
//		// 1.後臺管理商品列表
//		Set<Goods> goodsList = backendDao.queryGoods();
//		goodsList.stream().forEach(g -> System.out.println(g));
//		System.out.println("----------------------------------------");
//		
//		
//		// 2.後臺管理新增商品
//		Goods goods = new Goods();
//		goods.setGoodsName("黑糖珍珠鮮奶茶");
//		goods.setGoodsPrice(65);
//		goods.setGoodsQuantity(10);
//		goods.setGoodsImageName("BrownSugarPearlMilkTea.jpg");
//		goods.setStatus("1");
//		int goodsID = backendDao.createGoods(goods);
//		if(goodsID > 0){ System.out.println("商品新增上架成功！ 商品編號：" + goodsID); }
//		System.out.println("----------------------------------------");
//		
//		// 3.後臺管理更新商品
//		goods.setGoodsID(new BigDecimal(goodsID));
//		goods.setGoodsPrice(55); // 更改價格
//		boolean updateSuccess = backendDao.updateGoods(goods);
//		if(updateSuccess){ System.out.println("商品更新成功！ 商品編號：" + goodsID); }
//		System.out.println("----------------------------------------");
//		
//		// 4.後臺管理刪除商品
//		boolean deleteSuccess = backendDao.deleteGoods(goods.getGoodsID());
//		if(deleteSuccess){ System.out.println("商品刪除成功！ 商品編號：" + goodsID); }
//		System.out.println("----------------------------------------");
//		
//		// 5.後臺管理顧客訂單查詢
//		String orderDate = sf.format(Calendar.getInstance().getTime());
//		Set<SalesReport> reports = backendDao.queryOrderBetweenDate(orderDate, orderDate);
//		reports.stream().forEach(r -> System.out.println(r));
//		
//	}

	

	

	

	

}
