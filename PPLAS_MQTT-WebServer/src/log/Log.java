package log;

public class Log {
	private int logID; // log 번호
	private String logTitle; // log 제목
	private String patientID; // 환자 이름
	private String logDate; // log 작성날짜
	private String logContent; // log 내용
	private int logAvailable; // log 삭제여부
	
	public int getLogID() {
		return logID;
	}
	public void setLogID(int logID) {
		this.logID = logID;
	}
	public String getLogTitle() {
		return logTitle;
	}
	public void setLogTitle(String logTitle) {
		this.logTitle = logTitle;
	}
	public String getPatientID() {
		return patientID;
	}
	public void setPatientID(String patientID) {
		this.patientID = patientID;
	}
	public String getLogDate() {
		return logDate;
	}
	public void setLogDate(String logDate) {
		this.logDate = logDate;
	}
	public int getLogAvailable() {
		return logAvailable;
	}
	public void setLogAvailable(int logAvailable) {
		this.logAvailable = logAvailable;
	}
	public String getLogContent() {
		return logContent;
	}
	public void setLogContent(String logContent) {
		this.logContent = logContent;
	}
}
