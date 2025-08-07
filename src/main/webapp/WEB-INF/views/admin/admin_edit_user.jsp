<%@ taglib prefix="t" tagdir="/WEB-INF/tags/users" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>利用者情報編集画面</title>
</head>
<body>
	<jsp:include page="/WEB-INF/views/common/header.jsp" />
	<h3>利用者情報編集</h3>
	<hr />
	<form:form modelAttribute="usersForm" method="post" action="${pageContext.request.contextPath}/admin/users/edit/${usersForm.loginId}">
		<sec:csrfInput />
		<%-- 利用者入力フォームタグ参照 --%>
		<t:user_form />
		<br>
		<input type="submit" value="更新確認" />
	</form:form>
	<br>
	<form method="get" action="${pageContext.request.contextPath}/admin/users/${usersForm.loginId}">
		<input type="submit" value="戻る">
	</form>
	<hr />
	<a href="<c:url value='/'/>">TOP</a>
</body>
</html>