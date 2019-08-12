package patient;

import account.Account;
import location.Location;

public class Patient {
	
	private Account accountInfo;	// SQL�κ��� �޾ƿ� ȯ�� ����
	private Location locationInfo;	// ȯ�� ��ġ ����
	private String pulse;	// �ƹ�
	private String temp;	// ü��
	
	
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
