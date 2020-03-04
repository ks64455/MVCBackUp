package com.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BoardpwdCheckFormCommand implements BoardCommand {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) {
			String num = request.getParameter("num");
			String mode = request.getParameter("mode");
			
			request.setAttribute("num", num);
			request.setAttribute("mode", mode);
	}

}
