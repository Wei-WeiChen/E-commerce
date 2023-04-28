<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html  lang="en">
<head>
<meta http-equiv="Content-Language" content="zh-tw">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<!-- <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no"> -->
<%-- <link rel="stylesheet" href="${pageContext.request.contextPath}/src/bootstrap.min.css"> --%>
<%-- <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-3.2.1.min.js"></script> --%>
<%-- <script type="text/javascript" src="${pageContext.request.contextPath}/js/popper.min.js"></script> --%>
<%-- <script type="text/javascript" src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script> --%>

<title>販賣機-後臺</title>
	<script type="text/javascript">

	</script>
</head>
<body>
	<jsp:include page="/WEB-INF/Back/VM_Backend_Title.jsp"></jsp:include>
		
	<h2>商品列表</h2>
	
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
			<c:forEach var="item" begin="${i}" end="${i+9}" varStatus="loop">
			<c:if test="${not empty requestScope.showGoodsBack.get(item)}">
				<tr height="30" align="center">
					
					<td>${requestScope.showGoodsBack.get(item).goodsName}</td>
					<td>${requestScope.showGoodsBack.get(item).goodsPrice}</td>
					<td>${requestScope.showGoodsBack.get(item).goodsQuantity}</td>
					<c:choose>
  							<c:when test="${requestScope.showGoodsBack.get(item).status == 1}">
    							<td style="color: blue;">上架</td>
  							</c:when>
  							<c:when test="${requestScope.showGoodsBack.get(item).status == 0}">
    							<td style="color: red;">下架</td>
  							</c:when>
  						</c:choose>
			</c:if>		
			</c:forEach>
			
		</tbody>
	</table>
	</div>
	<br/>


		<c:url value='BackendAction.do?action=queryGoods' var="link">
			<c:param name="page" value="${requestScope.page-1}"/>
		</c:url>		
		<c:choose>
			<c:when test="${requestScope.page eq 1}">
				上一頁
			</c:when>
			<c:otherwise>
				<a href="${link}" style="text-decoration:none">"上一頁"</a>
			</c:otherwise>
		</c:choose>			
		
		<c:forEach items="${requestScope.rowList}" var="row">
			<c:url value='/BackendAction.do?action=queryGoods' var="link">
				<c:param name="page" value="${row}"/>
			</c:url>
			<c:choose>
			<c:when test="${requestScope.page eq row}">
				<a href="${link}"><b>${row}</b></a>
			</c:when>
				<c:otherwise>
					<a href="${link}" style="text-decoration:none">${row}</a>
				</c:otherwise>
			</c:choose>	
		</c:forEach>
		
		<c:url value='BackendAction.do?action=queryGoods' var="link">
			<c:param name="page" value="${requestScope.page+1}"/>			
		</c:url>		
		<c:choose>
			<c:when test="${requestScope.next eq 1}">
				<a href="${link}" style="text-decoration:none">"下一頁"</a>
			</c:when>
			<c:otherwise>
				下一頁
			</c:otherwise>
		</c:choose>	

    
</body>
</html>