<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>

<meta charset="UTF-8">
<title>답변글 쓰기</title>
</head>
<body>
	<h1>답변글 쓰기 화면</h1>
	<form action="reply.do" method="post">
		<!-- 답변글 필요 -->
		<input type="hidden" name="num" value="${replyui.num }"> <input
			type="hidden" name="repRoot" value="${replyui.repRoot }"> <input
			type="hidden" name="repStep" value="${replyui.repStep }"> <input
			type="hidden" name="repIndent" value="${replyui.repIndent }">


		<table border="1" width="600" align="center">

			<tr>
				<td colspan="2">원래글 번호 ${replyui.num }%nbsp;(조회수 :
					${replyui.readcnt})</td>
			</tr>
			<tr>
				<td>제목</td>
				<td><input type="text" name="title"
					value="[답변]${replyui.title }"></td>
			</tr>
			<tr>
			<td>작성자</td>
			<td><input type="text" name="author" size="20"></td>
			</tr>
			<tr>
			<td>내용</td>
			<td><textarea name="content" rows="10" cols="60">
	  		기존글내용 ${replyui.content }>>>>>>>>>>>>>>>>>
			</textarea></td>
			</tr>
			<tr>
			<td>비밀번호</td>
			<td><input type="password" name="passwd"></td>
			</tr>
		</table>
		<div>
			<input type="submit" value="저장"> <input type="button"
				value="목록보기" onclick="location.href='list.do'">
		</div>
		</form>
</body>
</html>