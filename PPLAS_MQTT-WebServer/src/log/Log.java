package log;

public class Log {
	private int logID; // log ��ȣ
	private String logTitle; // log ����
	private String patientID; // ȯ�� �̸�
	private String logDate; // log �ۼ���¥
	private String logContent; // log ����
	private int logAvailable; // log ��������
	
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
