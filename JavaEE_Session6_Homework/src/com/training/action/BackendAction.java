package com.training.action;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.upload.FormFile;

import com.training.model.Goods;
import com.training.service.BackEndService;
import com.training.formbean.BackendformData;
import com.training.vo.BuyGoodsRtn;
import com.training.vo.SalesReport;

@MultipartConfig
public class BackendAction extends DispatchAction {
	
	private BackEndService backEndService = BackEndService.getInstance();
       
	//商品列表的方法
	public ActionForward queryGoods(ActionMapping mapping, ActionForm form, 
            HttpServletRequest request, HttpServletResponse response) {
		
		String pageString = request.getParameter("page");
		String hiddenpage = request.getParameter("hiddenpage");
		
		int page = BackEndService.pageService(pageString,hiddenpage);
		request.setAttribute("page", page);
		
		//取出頁面的選項(session)
		HttpSession session = request.getSession();
		int countPage = 0;
		if(session.getAttribute("countPage")!=null){
			int count = backEndService.goodsIcon();
			countPage = (count%10 == 0)? count/10:(count/10)+1;
			session.setAttribute("countPage", countPage);
		}	
		
		//是否可以選擇下一頁
		int next = (countPage > page) ? 1 : 0;
		request.setAttribute("next", next);
		
		//分頁排版
		List<Integer> rowList = BackEndService.rowListService(page , countPage);
		request.setAttribute("rowList", rowList);
		
		//與資料庫連線，找出商品項目，補滿六項
		List<Goods> showGoodsBack = backEndService.showGoodsService(page);
		
		//避免JSTL顯示產生IndexOutOfBoundsException
		while (showGoodsBack.size()!=10){
			showGoodsBack.add(null);
		}
		request.setAttribute("showGoodsBack", showGoodsBack);
					
		
		// 結束後顯示頁面
		return mapping.findForward("goodListView");
	}
	
	//商品維護作業的重導頁面
	public ActionForward updateGoodsView(ActionMapping mapping, ActionForm form, 
            HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		// 商品選單資料
		Set<Goods> goods = backEndService.queryAllGoods();
		request.setAttribute("goods", goods);
		// 被選擇要修改的帳號資料
		String id = request.getParameter("goodsID");
		if(id != null){
			List<Goods> idGood = new ArrayList<>();
			Goods good = new Goods();
			good.setGoodsID(Integer.parseInt(id));
			idGood.add(good);
			Goods goodsUpdate = backEndService.queryGoodById(idGood).get(0);
			request.setAttribute("updateGoods", goodsUpdate);
		}
		
		return mapping.findForward("updateGoodsView");
		
	}
	//商品維護作業的方法
	public ActionForward updateGoods(ActionMapping mapping, ActionForm form, 
            HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		HttpSession session = request.getSession();
		// 將表單資料使用 struts ActionForm 方式自動綁定，省去多次由 request getParameter 取表單資料的工作
		BackendformData formData = (BackendformData) form;
		// 將Struts BackedActionForm 資料複製 Goods
		// 將表單資料轉換儲存資料物件(commons-beanutils-1.8.0.jar)
		Goods goods = new Goods();
		BeanUtils.copyProperties(goods, formData);
				
		boolean updateSuccess = backEndService.updateGoods(goods);
		String message = updateSuccess ? "資料修改成功！" : "資料修改失敗！";
		
		session.setAttribute("message", message);
		session.setAttribute("updateGoodsID", goods.getGoodsID());
		
		//做完顯示回原本GoodsReplenishment頁面
		return mapping.findForward("updateGoods");
	}
	
	//商品新增的重導頁面
	public ActionForward createGoodsView(ActionMapping mapping, ActionForm form, 
            HttpServletRequest request, HttpServletResponse response) throws Exception{
		return mapping.findForward("createGoodsView");
	}
	//商品新增的方法
	public ActionForward createGoods(ActionMapping mapping, ActionForm form, 
            HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		BackendformData formData = (BackendformData) form;		
		Goods goods = new Goods();
		BeanUtils.copyProperties(goods, formData);
					
		//圖片上傳
		boolean updateImg = false;
		FormFile imgFile = (FormFile)PropertyUtils.getSimpleProperty(form, "goodsImage");
		FileOutputStream fos = null; //用io的方法
		try{
			ServletContext application = this.getServlet().getServletContext();
			String realPath = application.getRealPath("/DrinksImage/");
			byte[]data = imgFile.getFileData();
			fos = new FileOutputStream(realPath+"/"+imgFile.getFileName());
			fos.write(data);
			updateImg=true;
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				fos.close();
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
		goods.setGoodsImageName(imgFile.getFileName());
		int createGood = backEndService.createGoods(goods);
		
		String message = (createGood!=0 && updateImg) ?"新增一項商品": "商品新增失敗";
		
		if(createGood!=0 && updateImg){
			message += "<br/>商品ID:"+createGood+"<br/>商品名稱:"+goods.getGoodsName();
		}
		 				
		HttpSession session = request.getSession();
		session.setAttribute("message", message);
		session.removeAttribute("goods");

		return mapping.findForward("createGoods");	
	}
	
	//銷售報表的重導頁面
	public ActionForward goodsSaleReportView(ActionMapping mapping, ActionForm form, 
            HttpServletRequest request, HttpServletResponse response) throws Exception{	
		
		return mapping.findForward("goodsSaleReportView");
	}
	//銷售報表的方法
	public ActionForward querySalesReport(ActionMapping mapping, ActionForm form, 
            HttpServletRequest request, HttpServletResponse response) throws Exception{		
		
		BackendformData formData = (BackendformData) form;
		String startDate = formData.getQueryStartDate();
		String endDate = formData.getQueryEndDate();
		String message = "";
		
		if (!"".equals(startDate) && !"".equals(endDate)){
			if(backEndService.compareTime(startDate , endDate)){
				Set<SalesReport>salesReports = backEndService.queryOrderBetweenDate(startDate, endDate);
				if(salesReports.isEmpty()){
					message = "範圍內報表沒有資料";
				}else{
					request.setAttribute("salesReports", salesReports);
				}
			}else{
				message = "結束時間不能比開始時間早喔";
			}
		}else{
			message = "請好好選一個區間";
		}
		HttpSession session = request.getSession();
		session.setAttribute("message", message);
		request.setAttribute("startDate", startDate);
		request.setAttribute("endDate", endDate);
	
		//做完顯示回原本GoodsSaleReportView頁面
		return mapping.findForward("goodsSaleReportView");
	}
	

}






//boolean updateImg = false;
//FormFile imgFile = (FormFile)PropertyUtils.getSimpleProperty(form, "goodsImage"); 
//FileOutputStream fileOutput = new FileOutputStream("/home/VendingMachine/DrinksImage/" + imgFile.getFileName()); 
//fileOutput.write(imgFile.getFileData()); 
//fileOutput.flush();
//updateImg=true;
//fileOutput.close(); 
//imgFile.destroy() ;  // destroy temperaty file
//
//String message = (createResult && updateImg) ?"新增一項商品" : "商品新增失敗";



//NIO
//boolean updateImg = false;
//BackendformData backForm = (BackendformData) form;
//FormFile imgFile = backForm.getGoodsImage();
//String goodsImgPath = servlet.getInitParameter("GoodsImgPath");
//String serverGoodsImgPath = servlet.getServletContext().getRealPath(goodsImgPath);
//String fileName = imgFile.getFileName();
//
//Part filePart = request.getPart("goodsImage");		
//
//String serverImgPath = imgFile.getFileName();
//System.out.println(serverImgPath);
//
//try (InputStream fileContent = imgFile.getInputStream();){
//	Files.copy(fileContent, serverImgPath, StandardCopyOption.REPLACE_EXISTING);
//}  
//String message = (createResult && updateImg) ?"新增一項商品" : "商品新增失敗";
