<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>共通エラー画面</title>
</head>
<body>
	<h3>エラー発生</h3>
	<hr />
	<table class="table table-bordered">
		<tr>
			<th class="col-3">ステータスコード</th>
			<td class="col-7"><%=response.getStatus()%></td>
		</tr>
	</table>
	<a href="<c:url value='/'/>">TOP</a>
</body>
</html>