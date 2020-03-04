package com.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.service.BoardCommand;
import com.service.BoardDeleteCommand;
import com.service.BoardListCommand;
import com.service.BoardpwdCheckCommand;
import com.service.BoardpwdCheckFormCommand;

/**
 * Servlet implementation class BoardFrontController
 */
@WebServlet("*.do")
public class BoardFrontController extends HttpServlet {
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");
		String requestURI = request.getRequestURI();
		String contextPath = request.getContextPath();
		String com = requestURI.substring(contextPath.length());
		BoardCommand command = null;
		String nextPage = null;
		//목록보기
		if(com.equals("/list.do")) {
			command = new BoardListCommand();
			command.execute(request, response);
			nextPage = "list.jsp";
		}

		
		//글 삭제하기
		if(com.equals("delelte.do")) {
			command = new BoardDeleteCommand();
			command.execute(request, response);
			nextPage="list.do";
    }
		// 비밀번호 입력 화면
		if(com.equals("/pwdCheckui.do")) {
			command = new BoardpwdCheckFormCommand();
			command.execute(request, response);
			nextPage = "passwdChk.jsp";
		}
		// 비밀번호 체크
		if(com.equals("/pwdCheck.do")) {
			command = new BoardpwdCheckCommand();
			command.execute(request, response);
			nextPage = (String)request.getAttribute("resultUrl");
		}
		// 글 수정 화면 보기
		if(com.equals("/updateui.do")) {
			command = new BoardRetrieveCommand();
			command.execute(request, response);
			nextPage = "update.jsp";
		}
		// 글 수정 하기
		if(com.equals("/update.do")) {
			command = new BoardUpdateCommand();
			command.execute(request, response);
			nextPage = "list.do";
		}
		RequestDispatcher dis = request.getRequestDispatcher(nextPage);
		dis.forward(request, response);
		
	}

}
