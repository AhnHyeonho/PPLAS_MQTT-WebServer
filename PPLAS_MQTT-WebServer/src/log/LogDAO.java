package log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class LogDAO {
	private Connection conn;
	private ResultSet rs;
	
	public LogDAO() {
		try {
			String dbURL = "jdbc:mysql://localhost:3306/pplas";
			String dbID = "root";
			String dbPassword = "1234";
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(dbURL, dbID, dbPassword);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getDate() {
		String SQL = "SELECT NOW()";
		
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getString(1);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public int getNext() {
		String SQL ="SELECT logID FROM LOG ORDER BY logID DESC";
		
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getInt(1) + 1;
			}
			return 1; // 첫 번째 게시물인 경우
		} catch(Exception e) {
			e.printStackTrace();
		}
		return -1; // 데이터베이스 오류
	}
	
	public int write(String logTitle, String patientID, String logContent) {
		String SQL = "INSERT INTO LOG VALUES (?, ?, ?, ?, ?, ?)";
		
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, getNext());
			pstmt.setString(2, logTitle);
			pstmt.setString(3,  patientID);
			pstmt.setString(4, getDate());
			pstmt.setString(5, logContent);
			pstmt.setInt(6, 1);
			return pstmt.executeUpdate();
		} catch(Exception e) {
			e.printStackTrace();
		}
		return -1; // 데이터베이스 오류
	}
	
	public ArrayList<Log> getList(int pageNumber) {
		String SQL = "SELECT * FROM LOG WHERE logID < ? AND logAvailable = 1 ORDER BY logID DESC LIMIT 10";
		ArrayList<Log> list = new ArrayList<Log>();
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, getNext() - (pageNumber - 1) * 10);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				Log log = new Log();
				log.setLogID(rs.getInt(1));
				log.setLogTitle(rs.getString(2));
				log.setPatientID(rs.getString(3));
				log.setLogDate(rs.getString(4));
				log.setLogContent(rs.getString(5));
				log.setLogAvailable(rs.getInt(6));
				list.add(log);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public boolean nextPage(int pageNumber) {
		String SQL = "SELECT * FROM LOG WHERE logID < ? AND logAvailable = 1";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, getNext() - (pageNumber - 1) * 10);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				return true;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public Log getLog(int logID) {
		String SQL = "SELECT * FROM LOG WHERE logID = ?";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, logID);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				Log log = new Log();
				log.setLogID(rs.getInt(1));
				log.setLogTitle(rs.getString(2));
				log.setPatientID(rs.getString(3));
				log.setLogDate(rs.getString(4));
				log.setLogContent(rs.getString(5));
				log.setLogAvailable(rs.getInt(6));
				return log;
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public int update(int logID, String logTitle, String logContent) {
		String SQL = "UPDATE LOG SET logTitle = ?, logContent = ? WHERE logID = ?";
		
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, logTitle);
			pstmt.setString(2, logContent);
			pstmt.setInt(3, logID);
			return pstmt.executeUpdate();
		} catch(Exception e) {
			e.printStackTrace();
		}
		return -1; // 데이터베이스 오류
	}
	
	public int delete(int logID) {
		String SQL = "UPDATE LOG SET logAvailable = 0 WHERE logID = ?";
		
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, logID);
			return pstmt.executeUpdate();
		} catch(Exception e) {
			e.printStackTrace();
		}
		return -1; // 데이터베이스 오류
	}
}
