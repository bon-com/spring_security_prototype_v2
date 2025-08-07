<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>パスワード更新画面</title>
</head>
<body>
	<jsp:include page="/WEB-INF/views/common/header.jsp" />
	<h3>パスワード更新</h3>
	<hr />
	<p>
		<c:out value="${message}" />
	</p>
	<form:form modelAttribute="userPasswordForm" method="post" action="${pageContext.request.contextPath}/user/password-update">
		<sec:csrfInput />
		<table>
			<tr>
				<td>新しいパスワード:</td>
				<td><form:password path="newPassword" />　<form:errors path="newPassword" cssStyle="color: red;"/></td>
			</tr>
			<tr>
				<td>確認用パスワード:</td>
				<td><form:password path="confirmPassword" />　<form:errors path="confirmPassword" cssStyle="color: red;"/></td>
			</tr>
			<tr>
				<td colspan="2"><input type="submit" value="更新" /></td>
			</tr>
		</table>
	</form:form>
	<hr />
	<a href="<c:url value='/'/>">TOP</a>
</body>
</html>