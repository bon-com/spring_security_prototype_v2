<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>利用者詳細画面</title>
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
	<h3>利用者詳細</h3>
	<hr />
	<table>
		<tr>
			<th>ログインID</th>
			<td>
				<c:out value="${user.loginId}" />
			</td>
		</tr>
		<tr>
			<th>利用者氏名</th>
			<td>
				<c:out value="${user.username}" />
			</td>
		</tr>
		<tr>
			<th>パスワード</th>
			<td>*****</td>
		</tr>
		<tr>
			<th>アカウント有効状態</th>
			<td>
				<c:out value="${user.enabled ? '有効' : '無効'}" />
			</td>
		</tr>
		<tr>
			<th>アカウント有効期限</th>
			<td>
				<fmt:formatDate value="${accountExpiryAt}" pattern="yyyy年MM月dd日 HH時mm分" />
			</td>
		</tr>
		<tr>
			<th>パスワード有効期限</th>
			<td>
				<fmt:formatDate value="${passwordExpiryAt}" pattern="yyyy年MM月dd日 HH時mm分" />
			</td>
		</tr>
		<tr>
			<th>ログイン失敗回数</th>
			<td>
				<c:out value="${user.loginFailureCount}" /> 回
			</td>
		</tr>
		<tr>
			<th>アカウントロック</th>
			<td>
				<c:out value="${user.accountNonLocked ? 'ロックなし' : 'ロックあり' }" />
			</td>
		</tr>
		<tr>
			<th>利用者権限</th>
			<td>
				<c:forEach var="auth" items="${authorities}">
					<span><c:out value="${auth.authorityName}" /></span>
					<br />
				</c:forEach>
			</td>
		</tr>
		<tr>
			<th>最終ログイン日時</th>
			<td>
				<fmt:formatDate value="${lastLoginAt}" pattern="yyyy年MM月dd日 HH時mm分" />
			</td>
		</tr>
	</table>
	<form method="get" action="${pageContext.request.contextPath}/admin/users/edit/${user.loginId}">
		<input type="submit" value="編集">
	</form>
	<br>
	<form method="get" action="${pageContext.request.contextPath}/admin/users">
		<input type="submit" value="戻る">
	</form>
</body>
<hr />
<a href="<c:url value='/'/>">TOP</a>
</body>
</html>