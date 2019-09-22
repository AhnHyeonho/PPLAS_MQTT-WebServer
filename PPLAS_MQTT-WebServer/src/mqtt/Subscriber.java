package mqtt;

import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import account.Account;
import account.AccountDAO;
import log.Log;
import log.LogDAO;
import sms.SendMessageLMS;
import distance.LocationDistance;
import hospital.Hospital;
import hospital.HospitalDAO;
import monitoring.Monitoring;
import monitoring.MonitoringDAO;

/**
 * The Class Listner.
 * 
 * @author Yasith Lokuge
 */

public class Subscriber implements MqttCallback {

	private String brokerUrl;
	/** The broker url. */
	private String clientId;
	/** The client id. */
	private String topic;
	/** The topic. */
	private Account account;
	private HashMap<String, Integer> emergencyJudgment;
	private HashMap<String, Integer> reportStatus;

	public String getBrokerUrl() {
		return brokerUrl;
	}

	public void setBrokerUrl(String brokerUrl) {
		this.brokerUrl = "tcp://" + brokerUrl + ":1883";
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public Subscriber() {
		super();
		emergencyJudgment = new HashMap<String, Integer>(); // 응급상황판단 해쉬맵 생성
		reportStatus = new HashMap<String, Integer>(); // 신고현황 해쉬맵 생성
	} // default constructor

	public Subscriber(String brokerUrl, String clientId, String topic) {
		super();
		this.brokerUrl = "tcp://" + brokerUrl + ":1883";
		this.clientId = clientId;
		this.topic = topic;
		emergencyJudgment = new HashMap<String, Integer>(); // 응급상황판단 해쉬맵 생성
		reportStatus = new HashMap<String, Integer>(); // 신고현황 해쉬맵 생성
	}

	public void subscribe() {
		MemoryPersistence persistence = new MemoryPersistence();

		try {

			MqttClient sampleClient = new MqttClient(brokerUrl, clientId, persistence);
			MqttConnectOptions connOpts = new MqttConnectOptions();
			connOpts.setCleanSession(true);

			System.out.println("checking");

			System.out.println("Mqtt Connecting to broker: " + brokerUrl);
			sampleClient.connect(connOpts);
			System.out.println("Mqtt Connected");

			sampleClient.setCallback(this);
			sampleClient.subscribe(this.topic);

			System.out.println("Subscribed");
			System.out.println("Listening");

		} catch (MqttException me) {

			System.out.println("Mqtt reason " + me.getReasonCode());
			System.out.println("Mqtt msg " + me.getMessage());
			System.out.println("Mqtt loc " + me.getLocalizedMessage());
			System.out.println("Mqtt cause " + me.getCause());
			System.out.println("Mqtt excep " + me);
		}
	}

	public void subscribe(String topic) {

		MemoryPersistence persistence = new MemoryPersistence();

		try {

			MqttClient sampleClient = new MqttClient(brokerUrl, clientId, persistence);
			MqttConnectOptions connOpts = new MqttConnectOptions();
			connOpts.setCleanSession(true);

			System.out.println("checking");

			System.out.println("Mqtt Connecting to broker: " + brokerUrl);
			sampleClient.connect(connOpts);
			System.out.println("Mqtt Connected");

			sampleClient.setCallback(this);
			sampleClient.subscribe(topic);

			System.out.println("Subscribed");
			System.out.println("Listening");

		} catch (MqttException me) {

			System.out.println("Mqtt reason " + me.getReasonCode());
			System.out.println("Mqtt msg " + me.getMessage());
			System.out.println("Mqtt loc " + me.getLocalizedMessage());
			System.out.println("Mqtt cause " + me.getCause());
			System.out.println("Mqtt excep " + me);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.paho.client.mqttv3.MqttCallback#connectionLost(java.lang.
	 * Throwable)
	 */
	public void connectionLost(Throwable arg0) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.paho.client.mqttv3.MqttCallback#deliveryComplete(org.eclipse.
	 * paho.client.mqttv3.IMqttDeliveryToken)
	 */
	public void deliveryComplete(IMqttDeliveryToken arg0) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.paho.client.mqttv3.MqttCallback#messageArrived(java.lang.
	 * String, org.eclipse.paho.client.mqttv3.MqttMessage)
	 */
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		// Message 형태 : "맥박%체온%경도:위도"
		// 메시지는 초당 1회씩 받는다고 가정
		AccountDAO acDAO = new AccountDAO();
		HospitalDAO hospitalDAO = new HospitalDAO();
		ArrayList<Hospital> hospitalList = hospitalDAO.getList();
		int NearestHospitalIndex = 0;

		String topicSplit[] = topic.split("/"); // user/patient/id 에서 id를 짜름
		final String id = topicSplit[2]; // account id저장
		account = acDAO.getInfo(id); // account테이블로부터 해당 id로 account객체 리딩
		String arr[] = message.toString().split("%"); // 메시지 분리
		String pulse = arr[0]; // 맥박 저장
		String temp = arr[1]; // 체온 저장
		String locationArr[] = arr[2].split(":"); // 위도, 경도 분리
		String latitude = locationArr[0]; // 위도(latitude) 저장
		String longitude = locationArr[1]; // 경도(longitude) 저장

		NearestHospitalIndex = findNearestHospital(hospitalDAO, hospitalList, latitude, longitude); // 가장 가까운 병원 결정

		///////////////////////////////////// 확인을 위한 출력 문구
		///////////////////////////////////// /////////////////////////////////////////////
		System.out.println("Mqtt topic : " + topic);
		System.out.println("Mqtt msg : " + message.toString());
		/*
		 * System.out.println(topicSplit[2]); System.out.println(arr[0]);
		 * System.out.println(arr[1]); System.out.println(arr[2]);
		 * System.out.println(locationArr[0]); System.out.println(locationArr[1]);
		 */
		System.out.println("가장 가까운 병원 : " + hospitalList.get(NearestHospitalIndex).getHospitalName());
		// 환자의 정보와 가장 가까운 병원의 위치 정보까지 알아냄
		//////////////////////////////////////////////////////////////////////////////////////////////////

		if (Float.parseFloat(temp) > 38 || Float.parseFloat(pulse) > 90) {
			// 이상 증상으로 예상되는 mqtt 메시지 도착

			/* 해당 id로 신고가 되어있지 않으면 */

			// 정상 수치이든 응급상황이든지 간에 모니터링은 데이터베이스에 계속 넣어주어야 함
			Monitoring monitoring = new Monitoring(); // 모니터링을 하기위한 객체 생성

			monitoring.setAccountInfo(account);
			monitoring.setPulse(pulse);
			monitoring.setTemp(temp);
			monitoring.setLatitude(latitude);
			monitoring.setLongtitude(longitude);

			MonitoringDAO monitoringDAO = new MonitoringDAO(); /* 해당 정보로 monitoring데이터 생성 */
			monitoringDAO.store(monitoring); // 모니터링 데이터베이스에 저장

			if (emergencyJudgment.containsKey(id)) {
				// 해당 토픽의 데이터가 해쉬맵에 있다면
				emergencyJudgment.replace(id, emergencyJudgment.get(id) + 1);
				System.out.println(id + " 환자(기존) :" + emergencyJudgment.get(id));
			} else {
				// 해당 토픽의 데이터가 해쉬맵에 없다면
				emergencyJudgment.put(id, 1); // 해당 토픽으로 데이터 생성
				System.out.println(topic + " 환자(신규) :" + emergencyJudgment.get(id));
				
					new java.util.Timer().schedule(new java.util.TimerTask() {
						@Override
						public void run() {
							if (emergencyJudgment.containsKey(id) && !(reportStatus.containsKey(id))) { /* 만약 로그가 생성되어 해당 해쉬가 삭제되었을 수도 있기 때문에 */
								emergencyJudgment.remove(id);
							}
						}
					}, 10000 /* 5분(300,000)이 경과하면 해당 해쉬데이터 삭제, 1,000당 1초 */);
				}
			

			/* 20190906-02:34 현재 문자 발송까지는 구현. 응급상황 판단 과정과 신고중복 판단 과정을 분리해야할 것 같음. */

			if (emergencyJudgment.get(id) >= 5) {
				// 응급상황 알고리즘 : 응급상황이 지속되면 count가 쌓이다가 5번정도 지속이 되면 log 정보를 기록하고 구조대에게 publish 한다
				// --> 추후 count>=120 정도(2분)로 수정
				// 응급상황일 경우 데이터베이스에 로그정보를 저장한다

				if (reportStatus.containsKey(id)) {
					/* 만약 해당 id로 신고가 되어있으면 */
					/* 아무것도 하지 않는다. */
				} else {
					/* 해당 id로 신고가 되어있지 않으면 */
					Log log = new Log();

					log.setAccountInfo(account);
					log.setPulse(pulse);
					log.setTemp(temp);
					log.setLatitude(latitude);
					log.setLongtitude(longitude);

					LogDAO logDAO = new LogDAO(); /* 해당 정보로 log데이터 생성 */
					logDAO.store(log); // 신고 로그 생성
					System.out.println("로그 생성"); // 신고 로그 생성
					SendMessageLMS.sendLMS(log,
							hospitalList.get(NearestHospitalIndex).getHospitalName());/* 신고 메시지 발송 - 로그와 인근병원 정보 포함 */

					// emergencyJudgment.remove(id); // 신고가 되었으므로 응급판단 해쉬에서는 삭제 ---여기 삭제해도될듯..?
					reportStatus.put(id, 1); // 신고가 되었으므로 신고현황 해쉬에 추가


						/* 해당 id의 신고현황 해쉬데이터를 1시간이 지나면 삭제. 1시간이 지나도 생체신호의 변화가 없다면 재신고를 위한 삭제임 */
						new java.util.Timer().schedule(new java.util.TimerTask() {
							@Override
							public void run() {
								if (reportStatus.containsKey(id)) { // containsKey 함수를 run() 함수 안에 넣으면 TimerTask가 계속 생기기 때문에 if문을
									// 밖으로 빼서 TimerTask를 생성하지 못하도록 함
									reportStatus.remove(id); // 신고현황 해쉬에서 삭제
									emergencyJudgment.remove(id); // 응급판단 해쉬에서 삭제
								}
							}
						}, 10000 /* 1시간(3,600,000)이 경과하면 해당 해쉬데이터 삭제, 1,000당 1초 */);
					
					/*
					 * 타이머로 1시간 뒤에 신고현황에서 해당 id 지우는 이유는 1시간이 지나도 생체데이터의 변화가 없으면 구조가 되지 않은 것으로 판단하고
					 * 재신고를 하기 위해서임
					 */
				}
			}

		} else {
			// 그냥 정상 수치일 경우

			// 정상 수치이든 응급상황이든지 간에 모니터링은 데이터베이스에 계속 넣어주어야 함
			Monitoring monitoring = new Monitoring(); // 모니터링을 하기위한 객체 생성

			monitoring.setAccountInfo(account);
			monitoring.setPulse(pulse);
			monitoring.setTemp(temp);
			monitoring.setLatitude(latitude);
			monitoring.setLongtitude(longitude);

			MonitoringDAO monitoringDAO = new MonitoringDAO(); /* 해당 정보로 monitoring데이터 생성 */
			monitoringDAO.store(monitoring); // 모니터링 데이터베이스에 저장

		}

	}

	public int findNearestHospital(HospitalDAO hospitalDAO, ArrayList<Hospital> hospitalList, String latitude,
			String longitude) {
		// 가장 가까운 병원 계산 메소드
		LocationDistance calculator = new LocationDistance();
		int index = 0;
		double min = 999999;
		for (int i = 0; i < hospitalList.size(); i++) {
			if (i == 0) {
				double dis = calculator.distance(
						Double.parseDouble(hospitalList.get(i).getHospitalLocationInfo().getLatitude()),
						Double.parseDouble(hospitalList.get(i).getHospitalLocationInfo().getLongitude()),
						Double.parseDouble(latitude), Double.parseDouble(longitude), "meter");
				min = dis;
			} else {
				double dis = calculator.distance(
						Double.parseDouble(hospitalList.get(i).getHospitalLocationInfo().getLatitude()),
						Double.parseDouble(hospitalList.get(i).getHospitalLocationInfo().getLongitude()),
						Double.parseDouble(latitude), Double.parseDouble(longitude), "meter");
				// lat1,lon1,lat2,lon2,unit
				if (dis < min) {
					min = dis;
					index = i;
				}
			}
		}
		return index;
	}

}