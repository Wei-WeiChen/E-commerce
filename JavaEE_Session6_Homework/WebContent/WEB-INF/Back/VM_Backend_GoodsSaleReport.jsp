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

	</script>
</head>
<body>
	<jsp:include page="/WEB-INF/Back/VM_Backend_Title.jsp"></jsp:include>
		
	<h2>銷售報表</h2><br/>
	<div style="margin-left:25px;">
	
	<p style="color:blue;">${sessionScope.deleteMsg}</p>
	<% session.removeAttribute("deleteMsg"); %>	
	
	<form action="BackendAction.do" method="get">
		<input type="hidden" name="action" value="querySalesReport"/>
		<%String startDate = request.getParameter("queryStartDate"); %>
		<%String endDate = request.getParameter("queryEndDate"); %>
		起 &nbsp; <input type="date" name="queryStartDate" style="height:25px;width:180px;font-size:16px;text-align:center;"value=<%=startDate %>/>
		&nbsp;
		迄 &nbsp; <input type="date" name="queryEndDate" style="height:25px;width:180px;font-size:16px;text-align:center;"value=<%=endDate %>/>	
		<input type="submit" value="查詢" style="margin-left:25px; width:50px;height:32px"/>
	</form>
	<br/>
	
	<table border="1">
		<c:if test="${not empty requestScope.salesReports}">
			<tbody>
				<tr height="50">
					<td width="100"><b>訂單編號</b></td>
					<td width="100"><b>顧客姓名</b></td>
					<td width="100"><b>購買日期</b></td>
					<td width="125"><b>飲料名稱</b></td> 
					<td width="100"><b>購買單價</b></td>
					<td width="100"><b>購買數量</b></td>
					<td width="100"><b>購買金額</b></td>
				</tr>
				<c:forEach items="${requestScope.salesReports}" var="salesReport">
					<tr height="30">
						<td>${salesReport.orderID}</td>
						<td>${salesReport.customerName}</td>
						<td><fmt:formatDate value="${salesReport.orderDate}" pattern="yyyy/MM/dd" /></td>
						<td>${salesReport.goodsName}</td>
						<td>${salesReport.goodsBuyPrice}</td> 
						<td>${salesReport.buyQuantity}</td>
						<td>${salesReport.buyAmount}</td>	
					</tr>
				</c:forEach>
			</tbody>
		</c:if>
	</table>
	</div>
</body>
</html>