package patient;

import account.Account;
import location.Location;

public class Patient {
	
	private Account accountInfo;	// SQL로부터 받아온 환자 정보
	private Location locationInfo;	// 환자 위치 정보
	private String pulse;	// 맥박
	private String temp;	// 체온
	
	
	public Account getAccountInfo() {
		return accountInfo;
	}


	public void setAccountInfo(Account accountInfo) {
		this.accountInfo = accountInfo;
	}


	public Location getLocationInfo() {
		return locationInfo;
	}


	public void setLocationInfo(Location locationInfo) {
		this.locationInfo = locationInfo;
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
}
