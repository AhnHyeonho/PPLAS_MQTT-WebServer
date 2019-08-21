package hospital;

import location.Location;

public class Hospital {
	private String hospitalName;	// 이름
	private String hospitalPhone;	// 전화번호
	private Location hospitalLocationInfo;	// 위치정보
	
	public String getHospitalName() {
		return hospitalName;
	}
	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}
	public String getHospitalPhone() {
		return hospitalPhone;
	}
	public void setHospitalPhone(String hospitalPhone) {
		this.hospitalPhone = hospitalPhone;
	}
	public Location getHospitalLocationInfo() {
		return hospitalLocationInfo;
	}
	public void setHospitalLocationInfo(Location hospitalLocationInfo) {
		this.hospitalLocationInfo = hospitalLocationInfo;
	}	
	
};


// 환자의 위치를 저장하는 임시 변수

// 그거랑 구급대 ArrayList에 저장된 구급대들 중 가장 가까운 구급대 정보를 알아내고 메시지 내용에 추가

// 모든 구급대에게 메시지 발생