<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Language" content="zh-tw">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>販賣機-後臺</title>
	<script type="text/javascript">
		function goodSelected(){
			document.ReplenishmentForm.action.value = "updateGoodsView";
			document.ReplenishmentForm.submit();
			
		}

	</script>
</head>
<body>
	<jsp:include page="/WEB-INF/Back/VM_Backend_Title.jsp"></jsp:include>
		
	<h2>商品維護作業</h2><br/>
	<div style="margin-left:25px;">
	
	<p style="color:blue;">${sessionScope.message}</p>
	<% session.removeAttribute("message"); %>
	
	<form name="ReplenishmentForm" action="BackendAction.do" method="post">
		<input type="hidden" name="action" value="updateGoods"/>
		<p>
			飲料名稱：
			 <select name="goodsID" onchange="goodSelected();">
				<option value="">-----請選擇------</option>
				<c:forEach items="${goods}" var="good">
					<option value="${good.goodsID}"  <c:if test="${good.goodsID eq updateGoods.goodsID}">selected</c:if>  >
						${good.goodsName}
					</option>
				</c:forEach>
			
			</select>
		</p>		
		<p>
			更改價格： 
			<input type="number" name="goodsPrice" size="5" value="${updateGoods.goodsPrice}" min="0" max="1000">
		</p>
		<p>
			補貨數量：
			<input type="number" name="goodsQuantity" size="5" value="0" min="0" max="1000">
		</p>
		<p>
			商品狀態：
			<select name="status">
				<option value="">---請選擇---</option>
				<option value="1">上架</option>
				<option value="0">下架</option>
			</select>
		</p>		
		<p>
			<input type="submit" value="送出">
		</p>
	</form>
	</div>
</body>
</html>