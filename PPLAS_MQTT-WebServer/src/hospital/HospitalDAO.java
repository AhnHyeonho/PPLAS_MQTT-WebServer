package hospital;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import location.Location;
import setting.Setting;

public class HospitalDAO {
	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;
	
	public HospitalDAO() {
		try {
			String dbURL = Setting.dbURL;
			String dbID = Setting.dbID;
			String dbPassword = Setting.dbPassword;
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(dbURL, dbID, dbPassword);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public int getCount() {
		String SQL = "SELECT hospitalName FROM HOSPITAL ORDER BY hospitalName DESC";
		
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getInt(1) + 1;
			}
			return 1; 	// 첫 번째 병원인 경우
		}	catch(Exception e) {
			e.printStackTrace();
		}
		return -1;	// 데이터베이스 오류
	}
	
	public ArrayList<Hospital> getList() {
		String SQL = "SELECT * FROM HOSPITAL ORDER BY hospitalName";
		ArrayList<Hospital> list = new ArrayList<Hospital>();
		
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				Hospital hos = new Hospital();
				hos.setHospitalName(rs.getString(1));
				hos.setHospitalPhone(rs.getString(3));
				String locaShift [] = rs.getString(2).split("%");	// 
				
				Location hl = new Location();
				hl.setLatitude(locaShift[0]);
				hl.setLongitude(locaShift[1]);
				hos.setHospitalLocationInfo(hl);
				list.add(hos);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}
}
