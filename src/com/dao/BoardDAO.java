package com.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import com.entity.*;
import com.sun.corba.se.impl.encoding.CodeSetComponentInfo.CodeSetContext;

public class BoardDAO {
	DataSource dataFactory;
	public BoardDAO() {
		//DataSource 얻기, 커넥션 풀 ㅅ ㅏ용
		try {
			Context ctx = new InitialContext();
			dataFactory =(DataSource)ctx.lookup("java:comp/env/jdbc/Oracle11g");
		}catch(Exception e) {
			
		}
		//목록보기
		
	}
	public ArrayList<BoardDTO> list() {
			ArrayList<BoardDTO> list = new ArrayList<BoardDTO>();
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			
			try {
				con = dataFactory.getConnection();
				String query = "select num, author, title, content, to_char(writeday, 'YYYY/MM/DD') writeday,"
						+ " readcnt, repRoot, repStep, repIndent from board order by repRoot desc, repStep asc";
				pstmt = con.prepareStatement(query);
				rs = pstmt.executeQuery();
				while(rs.next()) {
					BoardDTO data = new BoardDTO();
					data.setNum(rs.getInt("num"));
					data.setAuthor(rs.getString("author"));
					data.setTitle(rs.getString("title"));
					data.setContent(rs.getString("content"));
					data.setWriteday(rs.getString("writeday"));
					data.setReadcnt(rs.getInt("readcnt"));
					data.setRepRoot(rs.getInt("repRoot"));
					data.setRepStep(rs.getInt("repStep"));
					data.setRepIndent(rs.getInt("repIndent"));
					
					list.add(data);
				}
			}catch(Exception e) {
				e.printStackTrace();
			}finally {
				try {
					if(rs != null) rs.close();
					if(pstmt != null) pstmt.close();
					if(con != null) con.close();
				}catch(SQLException e) {
					e.printStackTrace();
				}
			}
			return list;
		}
	}
