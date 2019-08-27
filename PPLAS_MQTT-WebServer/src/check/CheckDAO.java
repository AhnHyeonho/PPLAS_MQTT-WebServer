package check;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import hospital.Hospital;
import location.Location;
import account.Account;
import account.AccountDAO;

public class CheckDAO {

	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;
	
	public CheckDAO() {
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
		String SQL = "INSERT INTO CHECK VALUES (?, ?, ?, ?, ?, ?, ?)";  //�쁽�옱�쓽 �떆媛꾩?�� 媛��졇�삤�뒗 MySQL�쓽 ?��몄옣
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
		
		return -1; //�뜲�씠�꽣踰좎?���뒪 �삤?���?
	}
	
	public Check getCheck(int checkID) {
		String SQL = "SELECT * FROM CHECK WHERE checkID = ?";
		AccountDAO accountDAO = new AccountDAO();
		
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, checkID);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				Check check = new Check();
				check.setCheckID(rs.getInt(1));
				check.setAccountInfo((accountDAO.getInfo(rs.getString(2))));
				check.setLatitude(rs.getString(3));
				check.setLongtitude(rs.getString(4));
				check.setPulse(rs.getString(5));
				check.setTemp(rs.getString(6));
				check.setDate(rs.getString(7));
				return check;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String getDate() {
		String SQL = "SELECT NOW()";  //�쁽�옱�쓽 �떆媛꾩?�� 媛��졇�삤�뒗 MySQL�쓽 ?��몄옣
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
		
		return ""; //�뜲�씠�꽣踰좎?���뒪 �삤?���?
	}
	
	public int getNext() {
		String SQL = "SELECT checkID FROM CHECK ORDER BY checkID DESC"; 
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
		return -1; //�뜲�씠�꽣踰좎?���뒪 �삤?���?
	}
	
	public ArrayList<Check> getList(int pageNumber)
	{
		String SQL = "SELECT * FROM CHECK WHERE checkID < ? ORDER BY checkID DESC LIMIT 10";
		ArrayList<Check> list = new ArrayList<Check>();
		AccountDAO accountDAO = new AccountDAO();
		
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, getNext() - (pageNumber -1) * 10);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				Check check = new Check();
				check.setCheckID(rs.getInt(1));
				check.setAccountInfo((accountDAO.getInfo(rs.getString(2))));
				check.setLatitude(rs.getString(3));
				check.setLongtitude(rs.getString(4));
				check.setPulse(rs.getString(5));
				check.setTemp(rs.getString(6));
				check.setDate(rs.getString(7));
				list.add(check);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list; 
	}
	
	public boolean nextPage(int pageNumber) {
		String SQL = "SELECT checkID FROM Check WHERE checkID < ?";

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
	
	public Check getMaxCheck(String accountID) {
		String SQL = "SELECT * FROM CHECK WHERE ACCOUNTINFO = ? ORDER BY CHECKID DESC LIMIT 1";
		AccountDAO accountDAO = new AccountDAO();
		Check check = new Check();
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, accountID);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				check.setCheckID(rs.getInt(1));
				check.setAccountInfo((accountDAO.getInfo(rs.getString(2))));
				check.setLatitude(rs.getString(3));
				check.setLongtitude(rs.getString(4));
				check.setPulse(rs.getString(5));
				check.setTemp(rs.getString(6));
				check.setDate(rs.getString(7));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return check; 
		
	}
	

	public static void main(String[] args) {
		new CheckDAO();
	}
	

	
}