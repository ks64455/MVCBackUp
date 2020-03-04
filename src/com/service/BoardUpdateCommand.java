package com.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.dao.BoardDAO;
import com.entity.BoardDTO;

public class BoardUpdateCommand implements BoardCommand {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) {
			BoardDTO dto = new BoardDTO();
			dto.setNum(Integer.parseInt(request.getParameter("num")));
			dto.setTitle(request.getParameter("title"));
			dto.setAuthor(request.getParameter("author"));
			dto.setContent(request.getParameter("content"));
			dto.setPasswd(request.getParameter("passwd"));
			
			BoardDAO dao = new BoardDAO();
			dao.update(dto);
	}

}
