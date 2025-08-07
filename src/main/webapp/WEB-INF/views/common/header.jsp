<h5>
    <c:out value="${greeting}" />
    <sec:authentication var="user" property="principal" />
    <c:out value="${user.username}" /> さん
</h5>

<form action="${pageContext.request.contextPath}/logout" method="post">
    <sec:csrfInput />
    <input type="submit" value="ログアウト" />
</form>
<hr />