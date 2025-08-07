<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ attribute name="showLoginId" required="false" type="java.lang.Boolean" %>
<%@ tag description="利用者入力フォーム" pageEncoding="UTF-8"%>
<table>
	<c:if test="${showLoginId}">
		<tr>
			<th>ログインID</th>
			<td>
				<form:input path="loginId" />
			</td>
			<td>
				<form:errors path="loginId" cssStyle="color: red;" />
			</td>
		</tr>
	</c:if>
	<tr>
		<th>利用者氏名</th>
		<td>
			<form:input path="username" />
		</td>
		<td>
			<form:errors path="username" cssStyle="color: red;" />
		</td>
	</tr>
	<tr>
		<th>パスワード</th>
		<td>
			<form:password path="password" />
		</td>
		<td>
			<form:errors path="password" cssStyle="color: red;" />
		</td>
	</tr>
	<tr>
		<th>確認用パスワード</th>
		<td>
			<form:password path="confirmPassword" />
		</td>
		<td>
			<form:errors path="confirmPassword" cssStyle="color: red;" />
		</td>
	</tr>
	<tr>
		<th>アカウント有効状態</th>
		<td>
			<label>
				<form:radiobutton path="enabled" value="true" />
				有効
			</label>
			<label>
				<form:radiobutton path="enabled" value="false" />
				無効
			</label>
		</td>
		<td>
			<form:errors path="enabled" cssStyle="color: red;" />
		</td>
	</tr>
	<tr>
		<th>アカウント有効期限</th>
		<td>
			<form:input path="accountExpiryAt" type="datetime-local" />
		</td>
		<td>
			<form:errors path="accountExpiryAt" cssStyle="color: red;" />
		</td>
	</tr>
	<tr>
		<th>パスワード有効期限</th>
		<td>
			<form:input path="passwordExpiryAt" type="datetime-local" />
		</td>
		<td>
			<form:errors path="passwordExpiryAt" cssStyle="color: red;" />
		</td>
	</tr>
	<tr>
		<th>アカウントロック状態</th>
		<td>
			<label>
				<form:radiobutton path="accountNonLocked" value="true" />
				ロックなし
			</label>
			<label>
				<form:radiobutton path="accountNonLocked" value="false" />
				ロックあり
			</label>
		</td>
		<td>
			<form:errors path="accountNonLocked" cssStyle="color: red;" />
		</td>
	</tr>
	<tr>
		<th>利用者権限</th>
		<td>
			<c:forEach var="authority" items="${authorityList}">
				<label>
					<form:checkbox path="authorityIds" value="${authority.authorityId}" />
					${authority.authorityName}
				</label>
				<br />
			</c:forEach>
		</td>
		<td>
			<form:errors path="authorityIds" cssStyle="color: red;" />
		</td>
	</tr>
</table>