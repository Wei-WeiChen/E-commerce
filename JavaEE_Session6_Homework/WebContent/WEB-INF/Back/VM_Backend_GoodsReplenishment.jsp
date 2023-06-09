<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:url value="/" var="WEB_PATH"/>
<c:url value="/js" var="JS_PATH"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Language" content="zh-tw">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>販賣機-後臺</title>

<script src="${JS_PATH}/jquery-3.2.1.min.js"></script>
	<script type="text/javascript">
		$(document).ready(function(){
			$("#goodsID").bind("change",function(){
				
				var goodsID = $("#goodsID option:selected").val();
				
				var goodsParam = {id : goodsID};
				
				if(goodsID != ""){
					$.ajax({
					  url: '${WEB_PATH}BackendAction.do?action=getUpdateGoods', // 指定要進行呼叫的位址
					  type: "GET", // 請求方式 POST/GET
					  data: goodsParam, // 傳送至 Server的請求資料(物件型式則為 Key/Value pairs)
					  dataType : 'JSON', // Server回傳的資料類型
					  success: function(goodsInfo) { // 請求成功時執行函式
					  	$("#goodsName").val(goodsInfo.goodsName);
					  	$("#goodsPrice").val(goodsInfo.goodsPrice);
					  	$("#goodsQuantity").val(goodsInfo.goodsQuantity);
					  	$("#status").val(goodsInfo.status);
					  },
					  error: function(error) { // 請求發生錯誤時執行函式
					  	alert("Ajax Error!");
					  }
					});
				}else{
				  	$("#goodsName").val('');
				  	$("#goodsPrice").val('');
				  	$("#goodsQuantity").val('');
				  	$("#status").val('');
				}
			});
			
		    $("form[name='ReplenishmentForm']").submit(function(e) {
		        e.preventDefault(); // 防止表單預設提交行為
		        
		        var replenishQuantity = parseInt($("#replenishQuantity").val()); // 獲取補貨數量的值
		        var currentQuantity = parseInt($("#goodsQuantity").val()); // 獲取當前庫存數量的值
		        var updatedQuantity = currentQuantity + replenishQuantity; // 計算補貨後的庫存數量
		        
		        // 更新庫存數量的輸入欄位
		        $("#goodsQuantity").val(updatedQuantity);
		        
		        // 在此處執行後續的 AJAX 請求或其他操作，例如提交表單
		        var formData = $("form[name='ReplenishmentForm']").serialize(); // 序列化表單資料
		        
		        $.ajax({
		            url: '${WEB_PATH}BackendAction.do', // 提交表單的位址
		            type: "POST", // 請求方式 POST
		            data: formData, // 傳送至 Server 的請求資料
		            success: function(response) { // 請求成功時執行函式
		                if (response.updateSuccess) {
		                    // 資料修改成功的處理邏輯
		                    console.log('資料修改成功！');
		                    console.log('訊息：', response.message);
		                    console.log('商品ID：', response.goodsID);
		                    $("#message").text(response.message + '更新商品ID：' + response.goodsID);
		                } else {
		                    // 資料修改失敗的處理邏輯
		                    console.log('資料修改失敗！');
		                    console.log('訊息：', response.message);
		                    $("#message").text(response.message);
		                }
		            },
		            error: function(error) { // 請求發生錯誤時執行函式
		                alert("Ajax Error!");
		            }
		        });
		    });
		    
		});	

	</script>
</head>
<body>
	<jsp:include page="/WEB-INF/Back/VM_Backend_Title.jsp"></jsp:include>
		
	<h2>商品維護作業</h2><br/>
	<div style="margin-left:25px;">
	
	<p id="message" style="color:blue;"></p>
	
	<form name="ReplenishmentForm" action="BackendAction.do" method="post">
		<input type="hidden" name="action" value="updateGoods"/>
		<p>
			商品名稱：
			 <select size="1" id="goodsID" name="goodsID">
				<option value="">-----請選擇------</option>
				<c:forEach items="${goods}" var="good">
					<option  value = "${good.goodsID}">
						${good.goodsName}
					</option>
				</c:forEach>
			
			</select>
		</p>		
		<p>
			更改價格： 
			<input type="number" id="goodsPrice" name="goodsPrice" size="5" value="${updateGoods.goodsPrice}" min="0" max="100000">
		</p>
		<p>
			庫存數量：
			<input type="number"  id="goodsQuantity" name="goodsQuantity" size="5" value="0" min="0" max="1000" readonly>
		</p>
		<p>
			補貨數量：
			<input type="number" id="replenishQuantity" name="replenishQuantity" size="5" value="0" min="0" max="100000">
		</p>
		<p>
			商品狀態：
			<select id="status" name="status">
				<option value="${updateGoods.status}">---請選擇---</option>
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