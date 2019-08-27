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
import location.Location;
import log.Log;
import log.LogDAO;
import distance.LocationDistance;
import hospital.Hospital;
import hospital.HospitalDAO;

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
	private Hospital nearestHospital;
	private Account account;
	private int count;//////////////////////////////////////////////////////////
	private Publisher publisher;
	private HashMap<String, Integer> emergencyJudgment;

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
		emergencyJudgment = new HashMap<String, Integer>(); // 해쉬맵 생성
	} // default constructor

	public Subscriber(String brokerUrl, String clientId, String topic) {
		super();
		this.brokerUrl = "tcp://" + brokerUrl + ":1883";
		this.clientId = clientId;
		this.topic = topic;
		emergencyJudgment = new HashMap<String, Integer>(); // 해쉬맵 생성
		count = 0; //////////////////////////////////////////////////////////
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
		
		AccountDAO acDAO = new AccountDAO();
		HospitalDAO hospitalDAO = new HospitalDAO();
		ArrayList<Hospital> hospitalList = hospitalDAO.getList();
		int NearestHospitalIndex = 0;
		
		String topicSplit[] = topic.split("/"); // user/patient/id 에서 id를 짜름
		String id = topicSplit[2];	//account id저장
		account = acDAO.getInfo(id); // account테이블로부터 해당 id로 account객체 리딩
		String arr[] = message.toString().split("%"); // 메시지 분리
		String pulse = arr[0];	// 맥박 저장
		String temp = arr[1];	// 체온 저장
		String locationArr[] = arr[2].split(":");	// 위도, 경도 분리
		String latitude = locationArr[0];	// 위도(latitude) 저장
		String longitude = locationArr[1];	// 경도(longitude) 저장

		NearestHospitalIndex = findNearestHospital(hospitalDAO, hospitalList, latitude, longitude);	// 가장 가까운 병원 결정

		///////////////////////////////////// 확인을 위한 출력 문구
		///////////////////////////////////// /////////////////////////////////////////////
		System.out.println("Mqtt topic : " + topic);
		System.out.println("Mqtt msg : " + message.toString());
		System.out.println(topicSplit[2]);
		System.out.println(arr[0]);
		System.out.println(arr[1]);
		System.out.println(arr[2]);
		System.out.println(locationArr[0]);
		System.out.println(locationArr[1]);
		System.out.println(NearestHospitalIndex);
		System.out.println("가장 가까운 병원 : " + hospitalList.get(NearestHospitalIndex).getHospitalName());
		// 환자의 정보와 가장 가까운 병원의 위치 정보까지 알아냄
		//////////////////////////////////////////////////////////////////////////////////////////////////

		if ((Float.parseFloat(temp) > 38 || Float.parseFloat(temp) < 36)
				|| (Float.parseFloat(pulse) < 60 || Float.parseFloat(pulse) > 100)) {
			// 이상 증상으로 예상되는 mqtt 메시지 도착
			if (emergencyJudgment.containsKey(topic)) {
				// 해당 토픽의 데이터가 해쉬맵에 있다면
				emergencyJudgment.replace(topic, emergencyJudgment.get(topic) + 1);
				System.out.println(topic + " 환자(기존) :" + emergencyJudgment.get(topic));
			} else {
				// 해당 토픽의 데이터가 해쉬맵에 없다면
				emergencyJudgment.put(topic, 1); // 해당 토픽으로 데이터 생성
				System.out.println(topic + " 환자(신규) :" + emergencyJudgment.get(topic));
			}

			if (emergencyJudgment.get(topic) >= 5) {
				// 응급상황 알고리즘 : 응급상황이 지속되면 count가 쌓이다가 5번정도 지속이 되면 log 정보를 기록하고 구조대에게 publish 한다
				// 응급상황일 경우 데이터베이스에 로그정보를 저장한다

				/*
				 * Log log = new Log();
				 * 
				 * log.setAccountInfo(account); log.setPulse(pulse); log.setTemp(temp);
				 * log.setLatitude(latitude); log.setLongtitude(longtitude);
				 * 
				 * LogDAO logDAO = new LogDAO(); logDAO.store(id, latitude, longtitude, pulse,
				 * temp);
				 */

				System.out.println("로그 생성");

			}

		} else {
			// 그냥 정상 수치일 경우
		}

	}
	
	public int findNearestHospital(HospitalDAO hospitalDAO, ArrayList<Hospital> hospitalList, String latitude, String longitude) {
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