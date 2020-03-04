<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>글수정</title>
</head>
<body>
	<h1>게시판 글수정 화면</h1>
	<form method="post" action="update.do">
		<input type="hidden" name="num" value="${retrieve.num}">
			<table border="1" width="600" align="center">
				<colgroup>
					<col width="150"></col>
					<col width="150"></col>
					<col width="150"></col>
					<col width="150"></col>
				</colgroup>
				<tr>
					<td>글번호</td>
					<td>${retrieve.num} <span>(조회소: ${retrieve.readcnt})</span></td>
					<td>작성일</td>
					<td>${retrieve.writeday}</td>
				</tr>
				<tr>
					<td>제목</td>
					<td colspan="3">
					<input type="text" name="title" value="${retrieve.title}"></td>
				</tr>
				<tr>
					<td>작성자</td>
					<td colspan="3">
					<input type="text" name="author" value="${retrieve.author}"></td>
				</tr>
				<tr>
					<td>내용</td>
					<td colspan="3">
					<textarea rows="10" cols="80" name="content">${retrieve.content}</textarea></td>
				</tr>
				<tr>
					<td>비밀번호</td>
					<td colspan="3">
					<input type="password" name="passwd"></td>
				</tr>
			</table>
			<div><input type="submit" value="수정완료">
				<input type="button" value="목록보기" onclick="location.href='list.do'"/>
			</div>
	</form>
</body>
</html>