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
		emergencyJudgment = new HashMap<String, Integer>(); // �ؽ��� ����
	} // default constructor

	public Subscriber(String brokerUrl, String clientId, String topic) {
		super();
		this.brokerUrl = "tcp://" + brokerUrl + ":1883";
		this.clientId = clientId;
		this.topic = topic;
		emergencyJudgment = new HashMap<String, Integer>(); // �ؽ��� ����
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
		// Message ���� : "�ƹ�%ü��%�浵:����"
		
		AccountDAO acDAO = new AccountDAO();
		HospitalDAO hospitalDAO = new HospitalDAO();
		ArrayList<Hospital> hospitalList = hospitalDAO.getList();
		int NearestHospitalIndex = 0;
		
		String topicSplit[] = topic.split("/"); // user/patient/id ���� id�� ¥��
		String id = topicSplit[2];	//account id����
		account = acDAO.getInfo(id); // account���̺�κ��� �ش� id�� account��ü ����
		String arr[] = message.toString().split("%"); // �޽��� �и�
		String pulse = arr[0];	// �ƹ� ����
		String temp = arr[1];	// ü�� ����
		String locationArr[] = arr[2].split(":");	// ����, �浵 �и�
		String latitude = locationArr[0];	// ����(latitude) ����
		String longitude = locationArr[1];	// �浵(longitude) ����

		NearestHospitalIndex = findNearestHospital(hospitalDAO, hospitalList, latitude, longitude);	// ���� ����� ���� ����

		///////////////////////////////////// Ȯ���� ���� ��� ����
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
		System.out.println("���� ����� ���� : " + hospitalList.get(NearestHospitalIndex).getHospitalName());
		// ȯ���� ������ ���� ����� ������ ��ġ �������� �˾Ƴ�
		//////////////////////////////////////////////////////////////////////////////////////////////////

		if ((Float.parseFloat(temp) > 38 || Float.parseFloat(temp) < 36)
				|| (Float.parseFloat(pulse) < 60 || Float.parseFloat(pulse) > 100)) {
			// �̻� �������� ����Ǵ� mqtt �޽��� ����
			if (emergencyJudgment.containsKey(topic)) {
				// �ش� ������ �����Ͱ� �ؽ��ʿ� �ִٸ�
				emergencyJudgment.replace(topic, emergencyJudgment.get(topic) + 1);
				System.out.println(topic + " ȯ��(����) :" + emergencyJudgment.get(topic));
			} else {
				// �ش� ������ �����Ͱ� �ؽ��ʿ� ���ٸ�
				emergencyJudgment.put(topic, 1); // �ش� �������� ������ ����
				System.out.println(topic + " ȯ��(�ű�) :" + emergencyJudgment.get(topic));
			}

			if (emergencyJudgment.get(topic) >= 5) {
				// ���޻�Ȳ �˰��� : ���޻�Ȳ�� ���ӵǸ� count�� ���̴ٰ� 5������ ������ �Ǹ� log ������ ����ϰ� �����뿡�� publish �Ѵ�
				// ���޻�Ȳ�� ��� �����ͺ��̽��� �α������� �����Ѵ�

				/*
				 * Log log = new Log();
				 * 
				 * log.setAccountInfo(account); log.setPulse(pulse); log.setTemp(temp);
				 * log.setLatitude(latitude); log.setLongtitude(longtitude);
				 * 
				 * LogDAO logDAO = new LogDAO(); logDAO.store(id, latitude, longtitude, pulse,
				 * temp);
				 */

				System.out.println("�α� ����");

			}

		} else {
			// �׳� ���� ��ġ�� ���
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