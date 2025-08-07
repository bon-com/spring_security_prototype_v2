<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>TOP画面</title>
</head>
<body>
	<jsp:include page="/WEB-INF/views/common/header.jsp" />
	<h3>利用者メニュー</h3>
	<p style="color: red;">
		<c:out value="${warning}" />
	</p>
	<ul>
		<li>パスワードの更新： <a href="${pageContext.request.contextPath}/user/password-update">こちら</a></li>
		<li>商品の購入： <a href="${pageContext.request.contextPath}/items/">こちら</a></li>
		<li>購入履歴： <a href="${pageContext.request.contextPath}/history/">こちら</a></li>
	</ul>
	<sec:authorize var="isAdmin" access="hasAnyRole('ROLE_ADMIN', 'ROLE_DEVELOPER')"/>
	<c:if test="${isAdmin}">
		<hr />
		<h3>管理者メニュー</h3>
		<ul>
			<li>商品の登録・更新： <a href="${pageContext.request.contextPath}/admin/items">こちら</a></li>
			<li>利用者情報の一覧： <a href="${pageContext.request.contextPath}/admin/users">こちら</a></li>
		</ul>
	</c:if>
</body>
</html>