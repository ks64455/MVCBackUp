package com.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import com.entity.BoardDTO;
import com.entity.PageTO;

public class BoardDAO {
	DataSource dataFactory;
	public BoardDAO() {	//생성자
		//DataSource 얻기, 커넥션 풀 사용
		try {
			Context ctx = new InitialContext();
			dataFactory =(DataSource)ctx.lookup("java:comp/env/jdbc/Oracle11g");
		}catch(Exception e) { e.printStackTrace();}
	}//end 생성자
	
  
  //목록보기
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
				}//end while
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
			
 
	
	// 답변글의 기존 repStep 1 증가
		public void makeReply(int _root, int _step) {
			Connection con = null;
			PreparedStatement pstmt = null;
			
			try {
				con = dataFactory.getConnection();
				StringBuffer query = new StringBuffer();
				query.append("UPDATE board SET repStep = repStep + 1 ");
				query.append("WHERE repRoot = ? AND repStep > ? ");
				pstmt = con.prepareStatement(query.toString());
				pstmt.setInt(1, _root);
				pstmt.setInt(2, _step);
				pstmt.executeUpdate();
			}
			catch (Exception e) { e.printStackTrace(); }
			finally {
				try {
					if(pstmt != null) { pstmt.close(); }
					if(con != null) { con.close(); }
				}
				catch (SQLException e) { e.printStackTrace(); }
			}
		}
		
		// 답변 달기
		public void reply(BoardDTO dto) {
			makeReply(dto.getRepRoot(), dto.getRepStep());
			Connection con = null;
			PreparedStatement pstmt = null;
			
			try {
				con = dataFactory.getConnection();
				StringBuffer query = new StringBuffer();
				query.append("INSERT INTO board(num, title, author, ");
				query.append("content, repRoot, repStep, repIndent, passwd)");
				query.append("values( board_seq.nextVal, ?, ?, ?, ?, ?, ?, ?)");
				pstmt = con.prepareStatement(query.toString());
				pstmt.setString(1, dto.getTitle());
				pstmt.setString(2, dto.getAuthor());
				pstmt.setString(3, dto.getContent());
				pstmt.setInt(4, dto.getRepRoot());
				pstmt.setInt(5, dto.getRepStep() + 1);
				pstmt.setInt(6, dto.getRepIndent() + 1);
				pstmt.setString(7, dto.getPasswd());
				pstmt.executeUpdate();
			}
			catch (Exception e) { e.printStackTrace(); }
			finally {
				try {
					if(pstmt != null) { pstmt.close(); }
					if(con != null) { con.close(); }
				}
				catch (SQLException e) { e.printStackTrace(); }
			}
		} //end reply
		
		// 페이징 처리 : 전체 레코드 갯수 구하기
		public int totalCount() {
			int count = 0;
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			
			try {
				con = dataFactory.getConnection();
				String query = "SELECT count(*) FROM board";
				pstmt = con.prepareStatement(query);
				rs = pstmt.executeQuery();
				if(rs.next()) {
					count = rs.getInt(1);
				}
			}catch (Exception e) {
				e.printStackTrace();
			}finally {
				try {
					if( rs != null) { rs.close(); }
					if( pstmt != null) { pstmt.close(); }
					if( con != null) { con.close(); }
				}catch (SQLException e) {
					e.printStackTrace();
				}
			}
			return count;
		} //end totalCount
		
		// 페이지 구현
		public PageTO page(int curPage) {
			PageTO to = new PageTO();
			int totalCount = totalCount();
			ArrayList<BoardDTO> list = new ArrayList<BoardDTO>();
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			
			try {
				con = dataFactory.getConnection();
				StringBuffer query = new StringBuffer();
				query.append("SELECT num, author, title, content, ");
				query.append("to_char(writeday, 'YYYY/MM/DD') writeday, ");
				query.append("readcnt, repRoot, repStep, repIndent ");
				query.append("FROM board order by repRoot desc, repStep asc");
				pstmt = con.prepareStatement(query.toString(),
						ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY);
				rs = pstmt.executeQuery();
				
				int perPage = to.getPerPage(); //5
				int skip = (curPage - 1) * perPage;
				if(skip > 0) {
					rs.absolute(skip);
				}
				for(int i = 0; i < perPage && rs.next(); i++) {
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
				} //end for
				
				to.setList(list); //ArrayList 저장
				to.setTotalCount(totalCount); // 전체 레코드 갯수
				to.setCurPage(curPage); // 현재 페이지
				
			}catch (Exception e) {
				e.printStackTrace();
			}finally {
				try {
					if( rs != null) { rs.close(); }
					if( pstmt != null) { pstmt.close(); }
					if( con != null) { con.close(); }
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			return to;
		} //end page
		
		

    
	public void delete(String _num) {
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = dataFactory.getConnection();
			String query = "DELETE FROM board WHERE num =?";
			pstmt = con.prepareStatement(query);
			pstmt.setInt(1, Integer.parseInt(_num));
			int n = pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}// end delete()

	
	

//비밀번호 체크
	public Map<String,String> pwdCheck(String _num, String _mode, String _passwd){
			Map<String,String> map = new HashMap<String, String>();
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String pwdChk = null;
			try {
				con = dataFactory.getConnection();
				String query = "SELECT passwd FROM board WHERE num = ?";
				pstmt = con.prepareStatement(query);
				pstmt.setInt(1, Integer.parseInt(_num));
				rs = pstmt.executeQuery();
				
				if(rs.next()) {
					pwdChk = rs.getString("passwd");
				}
				
				if(pwdChk.equals(_passwd)) {
					if(_mode.equals("update")) {
						map.put("resultUrl", "updateui.do");
					}else if(_mode.equals("delete")) {
						map.put("resultUrl", "delete.do");
					}
				} else {
					map.put("resultUrl", "pwdCheckui.do");
					map.put("resultMsg", "비밀번호가 일치하지 않습니다.");
				}
			}catch(Exception e) {
				e.printStackTrace();
			}finally {
				try {
					if(rs != null) rs.close();
					if(pstmt != null) pstmt.close();
					if(con != null) con.close();
				} catch(SQLException e) {
					e.printStackTrace();
				}
			}
			return map;
	}
	

	//글 수정하기
	public void update(BoardDTO dto) {
		Connection con = null;
		PreparedStatement pstmt = null;
		try {
			con = dataFactory.getConnection();
			StringBuffer query = new StringBuffer();
			query.append("UPDATE board SET title = ?, author = ?, ");
			query.append("content = ?, passwd = ? WHERE num = ?");
			pstmt = con.prepareStatement(query.toString());
			pstmt.setString(1, dto.getTitle());
			pstmt.setString(2, dto.getAuthor());
			pstmt.setString(3, dto.getContent());
			pstmt.setString(4, dto.getPasswd());
			pstmt.setInt(5, dto.getNum());
			pstmt.executeUpdate();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(pstmt != null)pstmt.close();
				if(con != null)con.close();
			} catch(SQLException e) {
				e.printStackTrace();
			}
		}
	}//end update
	}//end class
