<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>商品一覧画面</title>
</head>
<body>
	<jsp:include page="/WEB-INF/views/common/header.jsp" />
	<h3>商品一覧</h3>
	<%-- DIコンテナで管理しているセッションスコープBeanはSpELを利用して参照する --%>
	<spring:eval var="cart" expression="@cart" />
	<%-- オブジェクトだけ取得したり --%>
	<h5>カートの数量： ${cart.total}</h5>
	<p style="color: red;">
		<c:out value="${warning}" />
	</p>
	<hr />
	<table border="1">
		<tr>
			<th>商品名</th>
			<th>値段</th>
			<th>数量</th>
			<th>操作</th>
		</tr>
		<c:forEach var="item" items="${items}">
			<form:form method="post" action="${pageContext.request.contextPath}/cart/add">
				<sec:csrfInput />
				<tr>
					<td>${item.name}</td>
					<td>${item.price}円</td>
					<td><select name="quantity">
							<c:forEach var="i" begin="1" end="10">
								<c:set var="selected" value="" />
								<c:if test="${not empty cart.items}">
									<c:forEach var="entry" items="${cart.items}">
										<c:if test="${entry.value.item.id eq item.id and entry.value.quantity == i}">
											<c:set var="selected" value="selected" />
										</c:if>
									</c:forEach>
								</c:if>
								<option value="${i}" ${selected}>${i}</option>
							</c:forEach>
						</select> <form:errors path="quantity" cssStyle="color: red;" /></td>
					<td><input type="submit" value="カートに追加" /></td>
				</tr>
				<input type="hidden" name="itemId" value="${item.id}" />
			</form:form>
		</c:forEach>
	</table>
	<br>
	<c:if test="${cart.total ne '0'}">
		<form method="get" action="${pageContext.request.contextPath}/order/">
			<input type="submit" value="購入確認" />
		</form>
		<br>
	</c:if>
	<hr />
	<a href="<c:url value='/'/>">TOP</a>
</body>
</html>