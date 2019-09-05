package account;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import log.Log;
import setting.Setting;

public class AccountDAO {

	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;

	public AccountDAO() {
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

	public int login(String accountID, String accountPassword) {
		String SQL = "SELECT accountPassword FROM ACCOUNT WHERE accountID = ?";

		try {
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, accountID);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				if (rs.getString(1).equals(accountPassword)) {
					return 1; // 로그인 성공
				} else
					return 0; // 비밀번호 불일치
			}
			return -1; // 아이디가 없음
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -2; // 데이터베이스 오류
	}

	public int join(Account account) {
		String SQL = "INSERT INTO ACCOUNT VALUES (?, ?, ?, ?, ?, ?)";

		try {
			pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, account.getAccountID()); // 계정 ID
			pstmt.setString(2, account.getAccountPassword()); // 계정 비밀번호
			pstmt.setString(3, account.getAccountName()); // 이름
			pstmt.setString(4, account.getAccountResidentID()); // 주민등록번호
			pstmt.setString(5, account.getAccountAuthority()); // 의료진, 환자, 관리자
			pstmt.setString(6, account.getAccountPhone()); // 전화번호
			return pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1; // 데이터베이스 오류
	}

	public Account getInfo(String id) {
		String SQL = "SELECT * FROM ACCOUNT WHERE accountID = ?";
		Account account = new Account();
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				account.setAccountID(rs.getString(1));
				account.setAccountPassword(rs.getString(2));
				account.setAccountName(rs.getString(3));
				account.setAccountResidentID(rs.getString(4));
				account.setAccountAuthority(rs.getString(5));
				account.setAccountPhone(rs.getString(6));
				return account;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return account;
	}

	public ArrayList<Account> getList(int pageNumber) {
		String SQL = "SELECT * FROM ACCOUNT WHERE ACCOUNTAUTHORITY = 'PATIENT' ORDER BY accountID ASC LIMIT ?, 10;";
		/* SQL문에서 LIMIT의 10은 게시글 목록을 10개씩 출력한다는 의미 */
		ArrayList<Account> list = new ArrayList<Account>();

		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			int startNum = (pageNumber - 1) * 10;
			pstmt.setInt(1, startNum);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				Account account = new Account();
				account.setAccountID(rs.getString(1));
				account.setAccountPassword(rs.getString(2));
				account.setAccountName(rs.getString(3));
				account.setAccountResidentID(rs.getString(4));
				account.setAccountAuthority(rs.getString(5));
				account.setAccountPhone(rs.getString(6));
				list.add(account);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public int getNext() {
		String SQL = "SELECT accountID FROM ACCOUNT ORDER BY accountID ASC";
		
		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getInt(1) + 1;
			}
			return 1; 	// 첫 번째 게시물인 경우
		} catch(Exception e) {
			e.printStackTrace();
		}
		return -1; // 데이터베이스 오류
	}
	/* getNext()날려도되겠는데..? */
	

	public boolean nextPage(int pageNumber) {
		String SQL = "SELECT * FROM ACCOUNT ORDER BY accountID DESC LIMIT ?, 10";
		ArrayList<Account> list = new ArrayList<Account>();

		try {
			PreparedStatement pstmt = conn.prepareStatement(SQL);
			pstmt.setInt(1, (pageNumber - 1) * 10);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

}
