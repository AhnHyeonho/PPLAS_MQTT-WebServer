package medicalstaff;

import account.Account;
import location.Location;

public class MedicalStaff {
	// 구급대 정보
	// 응급환자 발생 >> 모든 구급대에 메세지 전송 >> 어떤 구급대가 응소 >> 가장 가까운 병원(환자 수용 가능한 병원)으로 보내는거지 -> 이 정보자체가 메세지 안에 포함.
	
	
	
	private Account accountInfo;	// SQL로부터 받아온 의료진 정보
	private Location locationInfo;	// 의료진 위치 정보
	
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


// 환자의 위치를 저장하는 임시 변수

// 그거랑 구급대 ArrayList에 저장된 구급대들 중 가장 가까운 구급대 정보를 알아내고 메시지 내용에 추가

// 모든 구급대에게 메시지 발생