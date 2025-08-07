<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ tag description="利用者確認表示項目" pageEncoding="UTF-8"%>
<table>
	<tr>
		<th>ログインID</th>
		<td>
			<c:out value="${loginId}" />
		</td>
	</tr>
	<tr>
		<th>利用者氏名</th>
		<td>
			<c:out value="${username}" />
		</td>
	</tr>
	<tr>
		<th>パスワード</th>
		<td>*****</td>
	</tr>
	<tr>
		<th>アカウント有効状態</th>
		<td>
			<c:out value="${enabled ? '有効' : '無効'}" />
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
		<th>アカウントロック状態</th>
		<td>
			<c:out value="${accountNonLocked ? 'ロックなし' : 'ロックあり' }" />
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
</table>
