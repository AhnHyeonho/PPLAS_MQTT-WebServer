package monitoring;

import account.Account;

public class Monitoring {
	
	private int monitoringID;
	private Account accountInfo;	// SQL로부터 받아온 환자 정보
	private String latitude;	// 위도
	private String longtitude; // 경도
	private String pulse;	// 맥박
	private String temp;	// 체온
	private String date;
	public int getMonitoringID() {
		return monitoringID;
	}
	public void setMonitoringID(int monitoringID) {
		this.monitoringID = monitoringID;
	}
	public Account getAccountInfo() {
		return accountInfo;
	}
	public void setAccountInfo(Account accountInfo) {
		this.accountInfo = accountInfo;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLongtitude() {
		return longtitude;
	}
	public void setLongtitude(String longtitude) {
		this.longtitude = longtitude;
	}
	public String getPulse() {
		return pulse;
	}
	public void setPulse(String pulse) {
		this.pulse = pulse;
	}
	public String getTemp() {
		return temp;
	}
	public void setTemp(String temp) {
		this.temp = temp;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	
}

