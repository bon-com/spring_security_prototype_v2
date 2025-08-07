<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>商品の登録・更新画面</title>
</head>
<body>
	<jsp:include page="/WEB-INF/views/common/header.jsp" />
	<h3>商品の更新</h3>
	<hr />
	<p>
		<c:out value="${message}" />
	</p>
	<table border="1">
		<tr>
			<th>商品ID</th>
			<th>商品名</th>
			<th>価格</th>
			<th>有効可否</th>
			<th>操作</th>
		</tr>
		<c:forEach var="item" items="${items}">
			<form method="get" action="${pageContext.request.contextPath}/admin/items/update-deleted/${item.id}">
				<tr>
					<td>${item.id}</td>
					<td>${item.name}</td>
					<td>${item.price}円</td>
					<td>
						<label>
							<input type="radio" name="deleted" value="false" ${!item.deleted ? 'checked' : ''} /> 有効
						</label>
						<label>
							<input type="radio" name="deleted" value="true" ${item.deleted ? 'checked' : ''} /> 削除済
						</label>
					</td>
					<td>
						<input type="submit" value="更新" />
					</td>
			</form>
			</tr>
		</c:forEach>
	</table>
	<hr />
	<h3>商品の登録</h3>
	<form:form modelAttribute="itemForm" method="post" action="${pageContext.request.contextPath}/admin/items/register">
		<sec:csrfInput />
		<table>
			<tr>
				<th>商品名</th>
				<td>
					<form:input path="name" />
					<form:errors path="name" cssStyle="color: red;" />
				</td>
			</tr>
			<tr>
				<th>値段</th>
				<td>
					<form:input path="price" type="number" />
					<form:errors path="price" cssStyle="color: red;" />
				</td>
			</tr>
		</table>
		<input type="submit" value="登録" />
	</form:form>
	<hr />
	<a href="<c:url value='/'/>">TOP</a>
</body>
</html>