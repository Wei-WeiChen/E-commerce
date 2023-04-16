package com.training.action;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.training.formbean.FrontendformData;
import com.training.model.Account;
import com.training.model.Goods;
import com.training.model.Member;
import com.training.service.BackEndService;
import com.training.service.FrontEndService;
import com.training.vo.BuyGoodsRtn;
import com.training.vo.GoodsResult;


public class FrontendAction extends DispatchAction {

	private FrontEndService frontEndService = FrontEndService.getInstance();
    
	//購買商品的方法
	public ActionForward buyGoods(ActionMapping mapping, ActionForm form, 
            HttpServletRequest request, HttpServletResponse response) {
		
		FrontendformData formData = (FrontendformData)form;
		//投入金額
		int inputMoney = formData.getInputMoney();
		//取session並新增值
		HttpSession session = request.getSession();
		Map<Integer, String> tempBuyMap = (Map<Integer,String>)session.getAttribute("tempBuyMap");
		for(int i =0;i<formData.getGoodsID().length;i++){
			tempBuyMap.put(Integer.parseInt(formData.getGoodsID()[i]), formData.getBuyQuantity()[i]);
		}
		//ID數量組合並移除0的項目
		List<Goods> buyGoodsList = BuyGoodsRtn.buyGoodsList(tempBuyMap);//處理用MAP看看
		//計算總共花費
		List<Goods> totalCost = frontEndService.buySumTotalCost(buyGoodsList);
		
		String message = "";
		
		if(inputMoney >= frontEndService.customerBuy(totalCost)){
			//查庫存數量
			List<Goods> buyGoods = frontEndService.buyTotalCost(buyGoodsList);//查詢問題沒有goods name
			int costMoney = frontEndService.customerBuy(buyGoods);
			
			
			session.setAttribute("giveChange", (inputMoney-costMoney));
			
			message += "<br/>~~~~~消費明細~~~~~<br/>";
			message += "<br/>投入金額:";
			message += inputMoney;
			message += "<br/>購買金額:";
			message += costMoney;
			message += "<br/>找零金額:";
			message += (inputMoney-costMoney);
			message += BuyGoodsRtn.showBuyGoods(buyGoodsList);
			
			//更新資料庫
			boolean updateGoodsDB = frontEndService.batchUpdateGoodsQuantity(buyGoodsList);
			message += updateGoodsDB ? "<br/><br/>庫存更新成功<br/>" : "<br/><br/>庫存更新失敗<br/>";
			
			//新增訂單
			Account account = (Account)session.getAttribute("account");
			String customerID = account.getId();
			
			boolean createGoodsOrder = frontEndService.batchCreateGoodsOrder(frontEndService.listToOrder(buyGoodsList,customerID));
			message += createGoodsOrder ? "訂單新增成功<br/>" : "訂單新增失敗<br/>";
			session.removeAttribute("tempBuyMap");
			session.removeAttribute("countPage");
		}else{
			List<Goods> buyGoods = frontEndService.buyTotalCost(buyGoodsList);//查詢問題沒有goods name
			int costMoney = frontEndService.customerBuy(buyGoods);
			
			message = "投入金額不足！";
			session.setAttribute("giveChange", inputMoney);
			message += "<br/>投入金額:";
			message += inputMoney;
			message += "<br/>購買金額:";
			message += costMoney;
			message += "<br/>找零金額:";
			message += inputMoney;
			tempBuyMap = new HashMap<Integer,String>();
			session.setAttribute("tempBuyMap", tempBuyMap);
		}
		session.setAttribute("message", message);//清空資料
		
		//回到購物頁面
		return mapping.findForward("VendingMachine");		
	}
		

	//商品查詢的方法
	public ActionForward buyGoodsView (ActionMapping mapping, ActionForm form, 
            HttpServletRequest request, HttpServletResponse response) {
		
		//分頁數值,解決js產生的問題
		String pageString = request.getParameter("page");
		String hiddenpage = request.getParameter("hiddenpage");
		String searchKeyword = request.getParameter("searchKeyword"); //查詢關鍵字
		searchKeyword = searchKeyword == null?"":searchKeyword;
		
		int page = BuyGoodsRtn.pageService(pageString,hiddenpage);
		request.setAttribute("page", page);
		
		//取出頁面的選項(session)
		HttpSession session = request.getSession();
		int countPage = 0;
		if(session.getAttribute("countPage")!=null){
			countPage = (int)session.getAttribute("countPage");
		}else{
			int count = frontEndService.goodsIcon();
			countPage = (count%6 == 0)? count/6:(count/6)+1;
			session.setAttribute("countPage", countPage);
		}
		//是否可以選擇下一頁
		int next = (countPage > page) ? 1 : 0;
		request.setAttribute("next", next);
		
		//分頁排版
		List<Integer> rowList = BuyGoodsRtn.rowListService(page , countPage);
		request.setAttribute("rowList", rowList);
		
		//與資料庫連線，找出商品項目，補滿六項
		
		List<Goods> showGoods = frontEndService.showGoodsService(page);	
		//避免JSTL顯示產生IndexOutOfBoundsException
		while (showGoods.size()!=6){
			showGoods.add(null);
		}
		request.setAttribute("showGoods", showGoods);
		
		//擷取暫時購買清單(session)
		FrontendformData formData = (FrontendformData)form;
		Map<Integer,String>tempBuyMap;
		if(formData.getGoodsID()!=null){
			if(session.getAttribute("tempBuyMap")!=null){
				tempBuyMap = (Map<Integer,String>)session.getAttribute("tempBuyMap");
				for(int i = 0;i<formData.getGoodsID().length;i++){
					tempBuyMap.put(Integer.parseInt(formData.getGoodsID()[i]), formData.getBuyQuantity()[i]);
				}
			}else{
				tempBuyMap = new HashMap<Integer,String>();
				System.out.println("建立暫時購物清單");
			}
			System.out.println("tempBuyMap:"+tempBuyMap);
			session.setAttribute("tempBuyMap:",tempBuyMap);
		}
		session.removeAttribute("goods");
//		Set<Goods> goods = FrontEndService.searchGoods(request.getParameter("searchKeyword"), Integer.parseInt(request.getParameter("pageNo")));
//		goods.stream().forEach(g -> System.out.println(g));				
		return mapping.findForward("buyGoodsView");
	}
	

	
	//暫時購物清單
	public ActionForward initial (ActionMapping mapping, ActionForm form, 
            HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		HttpSession session = request.getSession();
		Map<Integer,String>tempBuyMap = new HashMap<>();
		System.out.println("暫時購物清單");
		session.setAttribute("tempBuyMap", tempBuyMap);
		
		int countPage = BuyGoodsRtn.countPageService(frontEndService);
		session.setAttribute("countPage", countPage);
		
		return mapping.findForward("initial");
	}
	


}

//String customerID = request.getParameter("customerID"); //客戶登入資料
//Member member = frontEndService.queryMemberByIdentificationNo(customerID);
//System.out.println(member);
////------------------------------------------------------------------
//
//Set<Integer> goodsIDs = new HashSet<>();
//
//String[] goodsID= request.getParameterValues("goodsID");//選擇商品
//String[] buyQuantity= request.getParameterValues("buyQuantity");// 購買數量
//
//for(int i =0;i<6;i++){
//	goodsIDs.add(Integer.parseInt(goodsID[i]));			
//}
//
//Map<Integer, Goods> buyGoods = FrontEndService.queryBuyGoods(goodsIDs);
////buyGoods.values().stream().forEach(g -> System.out.println(g)); 
////System.out.println("-----------------------------------------");
//
//Map<Goods,Integer> goodsOrders = new HashMap<>();
//	
//for(int i =0;i<6;i++){
//	if(Integer.parseInt(buyQuantity[i])!=0){ //數量0去掉
//	goodsOrders.put(buyGoods.get(Integer.parseInt(goodsID[i])), Integer.parseInt(buyQuantity[i]));	
//	}
//}
//
//for(Goods goods : goodsOrders.keySet()){
//	System.out.println(goods); //查庫存
//}
//
//boolean insertSuccess = frontEndService.batchCreateGoodsOrder(customerID, goodsOrders);//建立訂單
//
//int inputMoney = Integer.parseInt(request.getParameter("inputMoney"));//投入金額
//System.out.println("投入金額:"+inputMoney);
//
//int buySum = frontEndService.buySum(goodsOrders);//購買金額
//System.out.println("購買總金額:"+buySum);
//
//if(inputMoney - buySum < 0){     //選購總金額 大於 投入金額			
//	int giveChange = inputMoney;
//	System.out.println("金額不足，請重新購買");
//	System.out.println("找零金額:"+giveChange);		
//}
//	
//int giveChange = inputMoney - buySum;//找零金額
//System.out.println("找零金額:"+giveChange);
//
//List<Goods> buyTotalQuantity = frontEndService.buyTotalQuantity(goodsOrders);//購買商品與數量
//System.out.println("購買清單:");
//buyTotalQuantity.forEach(b->System.out.println("商品名稱:"+b.getGoodsName() + " 商品金額:"+b.getGoodsPrice() + " 購買數量:"+b.getGoodsQuantity()));
//
//
//if(insertSuccess){System.out.println("建立訂單成功!");}
//
//// 將顧客所購買商品扣除更新商品庫存數量
//buyGoods.values().stream().forEach(g -> g.setGoodsQuantity(g.getGoodsQuantity() ));//問題 goodsOrders 的數量
//boolean updateSuccess = frontEndService.batchUpdateGoodsQuantity(buyGoods.values().stream().collect(Collectors.toSet()));
//if(updateSuccess){System.out.println("商品庫存更新成功!");}
//
//	
//
////String[] goodsID= req.getParameterValues("goodsID");//選擇商品
////String[] buyQuantity= req.getParameterValues("buyQuantity");// 購買數量
////
////Map<Integer,Integer> goodsOrders = new HashMap<>(); //商品數量對應的MAP		
////for(int i =0;i<6;i++){
////	goodsOrders.put(Integer.parseInt(goodsID[i]), Integer.parseInt(buyQuantity[i]));
////}
////
//////列出有購買數量的商品
////Set<Goods> buyGoodsList = BuyGoodsRtn.buyGoodsList(goodsOrders);		
////buyGoodsList.forEach(g -> System.out.println(g));
////
//////計算購買總額
////int buySum = BuyGoodsRtn.buySum(buyGoodsList);
////System.out.println("購買總金額:"+buySum);		
//		
//

// Redirect to view