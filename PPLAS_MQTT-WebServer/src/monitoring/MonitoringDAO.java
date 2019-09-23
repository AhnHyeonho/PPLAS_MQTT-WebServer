package monitoring;

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

public class MonitoringDAO {

	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;

	public MonitoringDAO() {
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

	public int store(Monitoring monitoring) {
		String SQL = "INSERT INTO MONITORING VALUES (?, ?, ?, ?, ?, ?, ?)";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, getNext());
			pstmt.setString(2, monitoring.getAccountInfo().getAccountID());
			pstmt.setString(3, monitoring.getLatitude());
			pstmt.setString(4, monitoring.getLongtitude());
			pstmt.setString(5, monitoring.getPulse());
			pstmt.setString(6, monitoring.getTemp());
			pstmt.setString(7, getDate().trim());
			System.out.println(getDate());
			return pstmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return -1;
	}

	public Monitoring getMonitoring(int monitoringID) {
		String SQL = "SELECT * FROM MONITORING WHERE monitoringID = ?";
		AccountDAO accountDAO = new AccountDAO();

		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, monitoringID);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				Monitoring monitoring = new Monitoring();
				monitoring.setMonitoringID(rs.getInt(1));
				monitoring.setAccountInfo((accountDAO.getInfo(rs.getString(2))));
				monitoring.setLatitude(rs.getString(3));
				monitoring.setLongtitude(rs.getString(4));
				monitoring.setPulse(rs.getString(5));
				monitoring.setTemp(rs.getString(6));
				monitoring.setDate(rs.getString(7));
				return monitoring;
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
			if (rs.next()) {
				return rs.getString(1);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return "";
	}

	public int getNext() {
		String SQL = "SELECT monitoringID FROM MONITORING ORDER BY monitoringID DESC";
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				return rs.getInt(1) + 1;
			}
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	public ArrayList<Monitoring> getList(int pageNumber) {
		String SQL = "SELECT * FROM MONITORING WHERE monitoringID < ? ORDER BY monitoringID DESC LIMIT 10";
		ArrayList<Monitoring> list = new ArrayList<Monitoring>();
		AccountDAO accountDAO = new AccountDAO();

		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, getNext() - (pageNumber - 1) * 10);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				Monitoring monitoring = new Monitoring();
				monitoring.setMonitoringID(rs.getInt(1));
				monitoring.setAccountInfo((accountDAO.getInfo(rs.getString(2))));
				monitoring.setLatitude(rs.getString(3));
				monitoring.setLongtitude(rs.getString(4));
				monitoring.setPulse(rs.getString(5));
				monitoring.setTemp(rs.getString(6));
				monitoring.setDate(rs.getString(7));
				list.add(monitoring);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public boolean nextPage(int pageNumber) {
		String SQL = "SELECT monitoringID FROM MONITORING WHERE monitoringID < ?";

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

	public Monitoring getMaxMonitoring(String accountID) {
		String SQL = "SELECT * FROM MONITORING WHERE ACCOUNTINFO = ? ORDER BY monitoringID DESC LIMIT 1";
		AccountDAO accountDAO = new AccountDAO();
		Monitoring monitoring = new Monitoring();
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, accountID);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				monitoring.setMonitoringID(rs.getInt(1));
				monitoring.setAccountInfo((accountDAO.getInfo(rs.getString(2))));
				monitoring.setLatitude(rs.getString(3));
				monitoring.setLongtitude(rs.getString(4));
				monitoring.setPulse(rs.getString(5));
				monitoring.setTemp(rs.getString(6));
				monitoring.setDate(rs.getString(7));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return monitoring;
	}
}