package medicalstaff;

import account.Account;
import location.Location;

public class MedicalStaff {
	// ���޴� ����
	// ����ȯ�� �߻� >> ��� ���޴뿡 �޼��� ���� >> � ���޴밡 ���� >> ���� ����� ����(ȯ�� ���� ������ ����)���� �����°��� -> �� ������ü�� �޼��� �ȿ� ����.
	
	
	
	private Account accountInfo;	// SQL�κ��� �޾ƿ� �Ƿ��� ����
	private Location locationInfo;	// �Ƿ��� ��ġ ����
	
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
	
};


// ȯ���� ��ġ�� �����ϴ� �ӽ� ����

// �װŶ� ���޴� ArrayList�� ����� ���޴�� �� ���� ����� ���޴� ������ �˾Ƴ��� �޽��� ���뿡 �߰�

// ��� ���޴뿡�� �޽��� �߻�