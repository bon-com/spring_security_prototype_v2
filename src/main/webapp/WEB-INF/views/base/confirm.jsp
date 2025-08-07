<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>注文内容確認画面</title>
</head>
<body>
	<jsp:include page="/WEB-INF/views/common/header.jsp" />
	<h3>注文内容確認</h3>
	<hr />
	<table border="1">
		<tr>
			<th>商品名</th>
			<th>数量</th>
			<th>値段</th>
			<th>操作</th>
		</tr>
		<c:forEach var="cartItem" items="${cartitems}">
			<tr>
				<td>${cartItem.item.name}</td>
				<td>${cartItem.quantity}個</td>
				<td>${cartItem.total}円</td>
				<td>
					<form method="get" action="${pageContext.request.contextPath}/cart/delete/${cartItem.item.id}">
						<input type="submit" value="カートから削除" />
					</form>
				</td>
			</tr>
		</c:forEach>
	</table>
	<h5>${totalPriceMsg}</h5>
	<hr />
	<c:if test="${totalPrice != 0}">
		<form method="get" action="${pageContext.request.contextPath}/order/complete">
			<input type="submit" value="購入する" />
		</form>
	</c:if>
	<br>
	<form method="get" action="${pageContext.request.contextPath}/items/">
		<input type="submit" value="商品一覧に戻る" />
	</form>
</body>
</html>