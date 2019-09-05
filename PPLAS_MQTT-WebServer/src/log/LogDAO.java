package log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import hospital.Hospital;
import location.Location;
import setting.Setting;
import account.Account;
import account.AccountDAO;

public class LogDAO {

	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;

	public LogDAO() {
		try {
			String dbURL = Setting.dbURL;
			String dbID = Setting.dbID;
			String dbPassword = Setting.dbPassword;
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(dbURL, dbID, dbPassword);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int store(Log log) {
		String SQL = "INSERT INTO LOG VALUES (?, ?, ?, ?, ?, ?, ?)"; //
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, getNext());
			pstmt.setString(2, log.getAccountInfo().getAccountID());
			pstmt.setString(3, log.getLatitude());
			pstmt.setString(4, log.getLongtitude());
			pstmt.setString(5, log.getPulse());
			pstmt.setString(6, log.getTemp());
			pstmt.setString(7, getDate());
			return pstmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return -1; //
	}

	public Log getLog(int logID) {
		String SQL = "SELECT * FROM LOG WHERE logID = ?";
		AccountDAO accountDAO = new AccountDAO();

		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, logID);
			rs = pstmt.executeQuery();
			if (rs.next()) {
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
		String SQL = "SELECT NOW()"; //
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				return rs.getString(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ""; //
	}

	public int getNext() {
		String SQL = "SELECT logID FROM LOG ORDER BY logID DESC";

		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				return rs.getInt(1) + 1;
			}
			return 1; // 泥� 踰덉㎏ 寃뚯떆臾쇱씤 寃쎌슦
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1; // �뜲�씠�꽣踰좎씠�뒪 �삤瑜�
	}

	public ArrayList<Log> getList(int pageNumber) {
		String SQL = "SELECT * FROM LOG WHERE logID < ? ORDER BY logID DESC LIMIT 10";
		ArrayList<Log> list = new ArrayList<Log>();
		AccountDAO accountDAO = new AccountDAO();

		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, getNext() - (pageNumber - 1) * 10);
			rs = pstmt.executeQuery();
			while (rs.next()) {
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
			pstmt.setInt(1, getNext() - (pageNumber - 1) * 10);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public Log getMaxLog(String accountID) {
		String SQL = "SELECT * FROM LOG WHERE ACCOUNTINFO = ? ORDER BY LOGID DESC LIMIT 1";
		AccountDAO accountDAO = new AccountDAO();
		Log log = new Log();
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, accountID);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				log.setLogID(rs.getInt(1));
				log.setAccountInfo((accountDAO.getInfo(rs.getString(2))));
				log.setLatitude(rs.getString(3));
				log.setLongtitude(rs.getString(4));
				log.setPulse(rs.getString(5));
				log.setTemp(rs.getString(6));
				log.setDate(rs.getString(7));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return log;
	}
}
