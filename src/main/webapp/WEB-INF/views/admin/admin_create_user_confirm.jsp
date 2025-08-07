<%@ taglib prefix="t" tagdir="/WEB-INF/tags/users"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>利用者新規登録確認画面</title>
<style>
table {
	border-collapse: collapse;
	width: 80%;
	margin: 20px auto;
}

th, td {
	border: 1px solid #ccc;
	padding: 8px;
	text-align: left;
}

th {
	background-color: #f5f5f5;
}
</style>
</head>
<body>
	<jsp:include page="/WEB-INF/views/common/header.jsp" />
	<h3>利用者新規登録確認</h3>
	<hr />
	<%-- 利用者確認表示項目タグ参照 --%>
	<t:user_view />
	<form method="get" action="${pageContext.request.contextPath}/admin/users/register/complete">
		<input type="submit" value="登録">
	</form>
	<br>
	<form method="get" action="${pageContext.request.contextPath}/admin/users/register">
		<input type="submit" value="戻る">
	</form>
</body>
<hr />
<a href="<c:url value='/'/>">TOP</a>
</body>
</html>