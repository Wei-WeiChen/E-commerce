<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Language" content="zh-tw">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>販賣機-後臺</title>
	<script type="text/javascript">

	</script>
</head>
<body>
	<jsp:include page="/WEB-INF/Back/VM_Backend_Title.jsp"></jsp:include>
		
	<h2>商品列表</h2><br/>
	<div style="margin-left:25px;">
	<p style="color:blue;">${sessionScope.deleteMsg}</p>
	<% session.removeAttribute("deleteMsg"); %>	
	<table border="1">
		<tbody>
			<tr height="50">
				<td width="150"><b>商品名稱</b></td> 
				<td width="100"><b>商品價格</b></td>
				<td width="100"><b>現有庫存</b></td>
				<td width="100"><b>商品狀態</b></td>
			</tr>
			<c:forEach items="${goods}" var="good">
				<tr height="30" align="center">
					
					<td>${good.goodsName}</td>
					<td>${good.goodsPrice}</td>
					<td>${good.goodsQuantity}</td>
					<td>${good.status}</td>
					
			</c:forEach>
			
		</tbody>
	</table>
	</div>
</body>
</html>