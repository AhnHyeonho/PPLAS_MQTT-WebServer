package log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import hospital.Hospital;
import location.Location;
import account.Account;
import account.AccountDAO;

public class LogDAO {

	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;
	
	public LogDAO() {
		try {
			String dbURL = "jdbc:mysql://localhost:3306/pplas?useSSL=false";
			String dbID = "root";
			String dbPassword = "1234";
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(dbURL, dbID, dbPassword);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public int store(String accountID, String latitude, String longtitude, String pulse, String temp) {
		String SQL = "INSERT INTO LOG VALUES (?, ?, ?, ?, ?, ?, ?)";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, getNext());
			pstmt.setString(2, accountID);
			pstmt.setString(3, latitude);
			pstmt.setString(4, longtitude);
			pstmt.setString(5, pulse);
			pstmt.setString(6, temp);
			pstmt.setString(7, getDate());
			return pstmt.executeUpdate();
			
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return -1; 
	}
	
	public Log getLog(int logID) {
		String SQL = "SELECT * FROM LOG WHERE logID = ?";
		AccountDAO accountDAO = new AccountDAO();
		
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, logID);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				Log log = new Log();
				log.setLogID(rs.getInt(1));
				log.setAccountInfo((accountDAO.getInfo(rs.getString(2))));
				log.setLatitude(rs.getString(3));
				log.setLongtitude(rs.getString(4));
				log.setPulse(rs.getString(5));
				log.setTemp(rs.getString(6));
				log.setDate(rs.getString(7));
				return log;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String getDate() {
		String SQL = "SELECT NOW()";  
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getString(1);
			}
			
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return ""; 
	}
	
	public int getNext() {
		String SQL = "SELECT logID FROM LOG ORDER BY logID DESC"; 
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getInt(1) +1;
			}
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1; 
	}
	
	public ArrayList<Log> getList(int pageNumber)
	{
		String SQL = "SELECT * FROM LOG WHERE logID < ? ORDER BY logID DESC LIMIT 10";
		ArrayList<Log> list = new ArrayList<Log>();
		AccountDAO accountDAO = new AccountDAO();
		
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, getNext() - (pageNumber -1) * 10);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				Log log = new Log();
				log.setLogID(rs.getInt(1));
				log.setAccountInfo((accountDAO.getInfo(rs.getString(2))));
				log.setLatitude(rs.getString(3));
				log.setLongtitude(rs.getString(4));
				log.setPulse(rs.getString(5));
				log.setTemp(rs.getString(6));
				log.setDate(rs.getString(7));
				list.add(log);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list; 
	}
	
	public boolean nextPage(int pageNumber) {
		String SQL = "SELECT logID FROM Log WHERE logID < ?";

		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, getNext() - (pageNumber -1) * 10);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	

	

	
}
