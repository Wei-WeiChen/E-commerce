<%@ page language="java" contentType="text/html; charset=BIG5"
    pageEncoding="BIG5"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=BIG5">
<title>Insert title here</title>
</head>
<head>
<meta http-equiv="Content-Language" content="zh-tw">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>�c���-��O</title>
	<script type="text/javascript">

	</script>
</head>
<body>
	<h1 style="color:blue;">
		${sessionScope.account.name} �z�n�I
	</h1>
	<h1>Vending Machine Backend Service
		<a href="LoginAction.do?action=logout" align="left">(�n�X)</a>
	</h1><br/>		
	<table border="1" style="border-collapse:collapse;margin-left:25px;">
		<tr>
			<td width="200" height="50" align="center">
				<a href="BackendAction.do?action=queryGoods">�ӫ~�C��</a>
			</td>
			<td width="200" height="50" align="center">
				<a href="BackendAction.do?action=updateGoodsView">�ӫ~���@�@�~</a>
				
			</td>
			<td width="200" height="50" align="center">
				<a href="BackendAction.do?action=createGoodsView">�ӫ~�s�W�W�[</a>
			</td>
			<td width="200" height="50" align="center">
				<a href="BackendAction.do?action=goodsSaleReportView">�P�����</a>
			</td>
			<td width="200" height="50" align="center">
				<a href="FrontendAction.do?action=initial">�e�x����</a>
			</td>
		</tr>
	</table>
	<br/><br/><HR>
</body>
</html>