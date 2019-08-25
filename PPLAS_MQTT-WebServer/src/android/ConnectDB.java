package android;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConnectDB {
	public ConnectDB() {};
	
	private static ConnectDB instance = new ConnectDB();
	
	public static ConnectDB getInstance() {
		return instance;
	}
	
	private String jdbcUrl = "jdbc:mysql://localhost:3306/pplas?characterEncoding=UTF-8&serverTimezone=UTC"; // MySQL 계정 "jdbc:mysql://localhost:3306/DB이름"
	private String dbID = "root"; // MySQL 계정 "로컬일 경우 root"
	private String dbPW = "qkr75886"; // 비밀번호 "mysql 설치 시 설정한 비밀번호"
	
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private PreparedStatement pstmt2 = null;
	private PreparedStatement pstmt3 = null;
	private ResultSet rs = null;
	
	private String sql = "";
	String returns = "";
	

	
	public String joinDB(String id, String pw, String name, String resident_id, String phone, String authority) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(jdbcUrl, dbID, dbPW);
			
			sql = "select * from account where resident_id=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, resident_id);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {		//해당 주민번호로 이미 계정이 존재.
				returns = "imposible";
			}else {
				sql = "select * from account where id=?";
				pstmt2 = conn.prepareStatement(sql);
				pstmt2.setString(1, id);
				rs = pstmt2.executeQuery();
				if(rs.next()) {		//해당 아이디가 이미 존재
					returns="exist";
				}else {
					sql = "insert into account(id,pw,name,resident_id,phone,authority) values(?,?,?,?,?,?)";
					pstmt3 = conn.prepareStatement(sql);
					pstmt3.setString(1, id);
					pstmt3.setString(2, pw);
					pstmt3.setString(3, name);
					pstmt3.setString(4, resident_id);
					pstmt3.setString(5, phone);
					pstmt3.setString(6, authority);
					
					pstmt3.executeUpdate();
					returns = "joinOK";
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}  finally {if (rs != null)try {rs.close();} catch (SQLException ex) {}
		if (pstmt != null)try {pstmt.close();} catch (SQLException ex) {}
		if (pstmt2 != null)try {pstmt.close();} catch (SQLException ex) {}
		if (pstmt3 != null)try {pstmt.close();} catch (SQLException ex) {}
		if (conn != null)try {conn.close();} catch (SQLException ex) {}
	}
		return returns;
	}
	
	public String loginDB(String id, String pw, String authority) {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(jdbcUrl, dbID, dbPW);
			
			sql = "select * from account where id=? and authority=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			pstmt.setString(2, authority);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {//아이디, 권한 일치
				if(rs.getString("pw").equals(pw)) {	//비밀번호도 일치
					returns = "loginOK";
				}
				else {	//비밀번호 불일치
					returns = "wrongPW";
				}
			} else {		//아이디가 존재하지 않음
				returns = "wrongID";
			}
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {if (rs != null)try {rs.close();} catch (SQLException ex) {}
		if (pstmt != null)try {pstmt.close();} catch (SQLException ex) {}
		if (conn != null)try {conn.close();} catch (SQLException ex) {}
		}
		return returns;
	}
	public String searchNameDB(String id) {
		String name = "";
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(jdbcUrl,dbID,dbPW);
			
			sql = "select * from account where id=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				name = rs.getString("name");
				returns = "findName="+name;
			} else {
				returns = "error1";		//스마트 밴드와 연동 오류
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {if (rs != null)try {rs.close();} catch (SQLException ex) {}
		if (pstmt != null)try {pstmt.close();} catch (SQLException ex) {}
		if (conn != null)try {conn.close();} catch (SQLException ex) {}
		}
		return returns;
	}

}
