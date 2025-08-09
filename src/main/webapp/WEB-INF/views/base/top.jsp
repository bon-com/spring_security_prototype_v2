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
	<%-- パスワード有効期限など表示など --%>
	<p style="color: red;">
		<c:out value="${warning}" />
	</p>
	<%-- Google OAuth紐づけ失敗 --%>
	<p style="color: red;">
		<c:out value="${googleLinkWarning}" />
	</p>
	<%-- Google OAuth紐づけ成功など --%>
	<p>
		<c:out value="${message}" />
	</p>
	<ul>
		<c:choose>
			<c:when test="${googleLinked eq true }">
				<li>Googleアカウント紐づけ状況： <a href="${pageContext.request.contextPath}/oauth2/google/sub/cansel">紐づけ解除</a></li>
			</c:when>
			<c:otherwise>
				<li>Googleアカウント紐づけ状況： <a href="${pageContext.request.contextPath}/oauth2/authorization/google-link">紐づけ開始</a></li>
			</c:otherwise>
		</c:choose>
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