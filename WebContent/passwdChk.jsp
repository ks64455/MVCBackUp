<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>비밀번호 확인</title>
<script type="text/javascript">
	function chkSave() {
		if (document.frm.passwd.value.replace(/\s/g,"")==""){
			alert("암호를 입력해 주세요.");
			document.frm.passwd.focus();
			return false;
		}
	}
	function loadData() {
		var msg = "${resultMsg}";
		if(msg != ""){
			alert(msg);
		}
	}
</script>
</head>
<body onload="loadData()">
	<h1>글수정 및 삭제 전 확인</h1>
	<form method="post" name="frm" action="pwdCheck.do" onsubmit="return chkSave()">
		<input type="hidden" name="num" value="${num}">
		<input type="hidden" name="mode" value="${mode}">
		<table border="1" align="center" width="360">
			<tr>
				<th colspan="2">글 입력시 작성한 비밀번호를 입력해 주세요.</th>
			</tr>
			<tr>
				<td>비밀번호</td>
				<td><input type="password" name="passwd" size="20" maxlength="12"></td>
			</tr>
			<tr>
				<td colspan="2" align="center">
				<input type="submit" value="확인">
				<input type="button" value="목록" onclick="location.href='/mvc2Board/list.do'">
				</td>
			</tr>
		</table>
	</form>
</body>
</html>