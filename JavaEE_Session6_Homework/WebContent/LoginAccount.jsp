<%@ page language="java" contentType="text/html; charset=BIG5"
    pageEncoding="BIG5"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=BIG5">
<title>VendingLogin</title>
	<script src="https://code.jquery.com/jquery-3.2.1.min.js"></script>
	<script type="text/javascript">
	    $(document).ready(function() {
	        <%-- 取得登入錯誤訊息 --%>
	        var message = "<%= session.getAttribute("message") %>";
	        
	        <%-- 若錯誤訊息存在，顯示alert --%>
	        if (message) {
	            alert(message);
	        }
	    });
	</script>
</head>
<body>	
	<form action="LoginAction.do" method="post">
		<input type="hidden" name="action" value="login"/>
	    ID:<input type="text" name="id"/> <br/><br/>
	    PWD:<input type="password" name="pwd"/> <br/><br/>
	    <input type="submit"/>
	</form>
</body>
</html>