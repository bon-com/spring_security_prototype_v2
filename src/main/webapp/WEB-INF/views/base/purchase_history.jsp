<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>購入履歴一覧画面</title>
</head>
<body>
	<jsp:include page="/WEB-INF/views/common/header.jsp" />
	<h3>購入履歴一覧</h3>
	<hr />
	<form:form method="get" modelAttribute="historyForm" action="${pageContext.request.contextPath}/history/purchase-history">
		<label for="date">購入日：</label>
		<form:select path="purchaseDate" id="date">
			<form:option value="" label="選択してください" />
			<form:options items="${availableDates}" />
		</form:select>
		<input type="submit" value="検索" />　<form:errors path="purchaseDate" cssStyle="color: red;"/>
	</form:form>
	<c:if test="${not empty purchaseHistory}">
		<hr />
		<h4>購入日： ${purchaseHistory.purchaseDate}</h4>
		<h4>合計金額： ${totalPrice} 円</h4>
		<table border="1">
			<thead>
				<tr>
					<th>商品名</th>
					<th>数量</th>
					<th>購入価格</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="item" items="${purchaseHistory.itemList}">
					<tr>
						<td>${item.itemName}</td>
						<td>${item.quantity}個</td>
						<td>${item.price}円</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</c:if>
	<hr />
	<a href="<c:url value='/'/>">TOP</a>
</body>
</html>