<%@ taglib prefix="t" tagdir="/WEB-INF/tags/users" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>利用者新規登録画面</title>
</head>
<body>
	<jsp:include page="/WEB-INF/views/common/header.jsp" />
	<h3>利用者新規登録</h3>
	<p style="color: red;">
		<c:out value="${warning}" />
	</p>
	<hr />
	<form:form modelAttribute="usersForm" method="post" action="${pageContext.request.contextPath}/admin/users/register">
		<sec:csrfInput />
		<%-- 利用者入力フォームタグ参照 --%>
		<t:user_form showLoginId="true" />
		<br>
		<input type="submit" value="登録確認" />
	</form:form>
	<br>
	<form method="get" action="${pageContext.request.contextPath}/admin/users">
		<input type="submit" value="戻る">
	</form>
	<hr />
	<a href="<c:url value='/'/>">TOP</a>
</body>
</html>