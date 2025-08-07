<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" />
<title>ログイン画面</title>
</head>
<body>
	<h2>ログイン</h2>

	<c:if test="${not empty SPRING_SECURITY_LAST_EXCEPTION}">
		<p style="color: red;">
			<c:out value="${SPRING_SECURITY_LAST_EXCEPTION.message}" />
		</p>
	</c:if>

	<c:if test="${not empty param.logout}">
		<p>ログアウトしました</p>
	</c:if>

	<form action="${pageContext.request.contextPath}/login" method="post">
		<sec:csrfInput />
		<label for="loginId">ログインID:</label> <input type="text" id="loginId" name="loginId" autofocus />
		<br />

		<label for="password">パスワード:</label> <input type="password" id="password" name="password" />
		<br />
		<input type="submit" value="ログイン" />
	</form>
	<br>
	<%-- Googleでログイン --%>
    <a href="<c:url value='/oauth2/authorization/google' />">
        <button>Googleでログイン</button>
    </a>
</body>
</html>