<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Language" content="zh-tw">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>販賣機</title>
	<style type="text/css">
		.page {
			display:inline-block;
			padding-left: 10px;
		}
	</style>
	

</head>

<body align="center">
<br/><br/>

<table width="1000" height="400" align="center">
	<tr>
		<td colspan="2" align="right">
			<form action="FrontendAction.do" method="get">
				<input type="hidden" name="action" value="buyGoodsView"/>
				<input type="hidden" name="page" value="1"/>
				<input type="text" name="searchKeyword"/>
				<input type="submit" value="商品查詢"/>
			</form>
		</td>
	</tr>
	<form name="FrontendForm" id="FrontendForm" action="FrontendAction.do" method="post">
			<input type="hidden" name="action" value="buyGoods"/>
			<input type="hidden" name="hiddenpage" value="${requestScope.page}"/>
	<tr>
	
			
<!-- 			<table width="1000" height="400" align="center"> -->
				<tr>
					<td width="400" height="200">
						<img border="0" src="DrinksImage/coffee.jpg" width="200" height="200" >
						<h1>歡迎光臨，${sessionScope.account.name}！</h1>
						<a href="BackendAction.do?action=queryGoods" align="left" >後臺頁面</a>&nbsp; &nbsp;
						<a href="LoginAction.do?action=logout" align="left">登出</a>
						<br/>
						<br/>	
						<font face="微軟正黑體" size="4" >
							<b>投入:</b>
							<input type="number" name="inputMoney" max="100000" min="0"  size="5" value="0">
							<b>元</b>		
							<b><input type="submit" value="送出" >					
							<br/><br/>
						</font>
				<!--訊息框 -->
				<div style="border-width:3px;border-style:dashed;border-color:#FFAC55;
						padding:5px;width:300px;">
						<font>
							${sessionScope.message}
						</font>						
						<% session.removeAttribute("message"); %>	
						<br/>
					</div>	
					</td>
					
					<td width="600" height="400">
						<table border="1" style="border-collapse: collapse">
							<c:forEach var="i" begin="0" end="1" varStatus="loop">
							<tr>
								<c:forEach var="item" begin="${i*3}" end="${i*3+2}" varStatus="loop">
								<c:if test="${not empty requestScope.showGoods.get(item)}">
									<td width="300">
									<font face="微軟正黑體" size="5" >
										${requestScope.showGoods.get(item).goodsName}
									</font>
									<br/>
									<font face="微軟正黑體" size="4" style="color: gray;" >
										<!-- EX:柳橙汁 10元/罐 -->
										${requestScope.showGoods.get(item).goodsPrice} 元/份
									</font>
									
									<!-- 各商品圖片 -->
									<img border="0" src="${requestScope.showGoods.get(item).goodsImage}" width="150" height="150" >						
									<br/>	
									<font face="微軟正黑體" size="3">
										<input type="hidden" name="goodsID" value="${requestScope.showGoods.get(item).goodsID}">
										
										<!-- 設定最多不能買大於庫存數量 -->
										<c:choose>
											<c:when test="${sessionScope.carGoods.containsKey(requestScope.showGoods.get(item).goodsID) eq true}">
												購買<input type="number" name="buyQuantity" min="0" 
												max="${requestScope.showGoods.get(item).goodsQuantity}" size="5" 
												value="${sessionScope.carGoods.get(requestScope.showGoods.get(item).goodsID)}">份
<!-- 												onchange="buyQuantityfun();" -->
											</c:when>
											<c:otherwise>
												購買<input type="number" name="buyQuantity" min="0" 
												max="${requestScope.showGoods.get(item).goodsQuantity}" size="5" 
												value="0" >份
<!-- 												onchange="buyQuantityfun();" -->
											</c:otherwise>
										</c:choose>
										
										<!-- 顯示庫存數量 -->
										<br>
										<p style="color: red;">
										(庫存 ${requestScope.showGoods.get(item).goodsQuantity} 份)
										</p>
									</font>	
								</c:if>
								</c:forEach>
							</tr>	
							</c:forEach>						
						</table>
						<br/>
						
		<c:url value='FrontendAction.do?action=buyGoodsView' var="link">
			<c:param name="page" value="${requestScope.page-1}"/>
		</c:url>		
		<c:choose>
			<c:when test="${requestScope.page eq 1}">
				上一頁
			</c:when>
			<c:otherwise>
				<a href="${link}&searchKeyword=${searchKeyword}" style="text-decoration:none">"上一頁"</a>
			</c:otherwise>
		</c:choose>			
		
		<c:forEach items="${requestScope.rowList}" var="row">
			<c:url value='/FrontendAction.do?action=buyGoodsView' var="link">
				<c:param name="page" value="${row}"/>
			</c:url>
			<c:choose>
			<c:when test="${requestScope.page eq row}">
				<a href="${link}&searchKeyword=${searchKeyword}"><b>${row}</b></a>
			</c:when>
				<c:otherwise>
					<a href="${link}&searchKeyword=${searchKeyword}" style="text-decoration:none">${row}</a>
				</c:otherwise>
			</c:choose>	
		</c:forEach>
		
		<c:url value='FrontendAction.do?action=buyGoodsView' var="link">
			<c:param name="page" value="${requestScope.page+1}"/>			
		</c:url>		
		<c:choose>
			<c:when test="${requestScope.next eq 1}">
				<a href="${link}&searchKeyword=${searchKeyword}" style="text-decoration:none">"下一頁"</a>
			</c:when>
			<c:otherwise>
				下一頁
			</c:otherwise>
		</c:choose>	
							
		
				</td>
			</tr>
		</table>
	</form>	
</body>
</html>