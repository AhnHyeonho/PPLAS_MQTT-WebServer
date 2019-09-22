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
		emergencyJudgment = new HashMap<String, Integer>(); // ���޻�Ȳ�Ǵ� �ؽ��� ����
		reportStatus = new HashMap<String, Integer>(); // �Ű���Ȳ �ؽ��� ����
	} // default constructor

	public Subscriber(String brokerUrl, String clientId, String topic) {
		super();
		this.brokerUrl = "tcp://" + brokerUrl + ":1883";
		this.clientId = clientId;
		this.topic = topic;
		emergencyJudgment = new HashMap<String, Integer>(); // ���޻�Ȳ�Ǵ� �ؽ��� ����
		reportStatus = new HashMap<String, Integer>(); // �Ű���Ȳ �ؽ��� ����
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
		// �޽����� �ʴ� 1ȸ�� �޴´ٰ� ����
		AccountDAO acDAO = new AccountDAO();
		HospitalDAO hospitalDAO = new HospitalDAO();
		ArrayList<Hospital> hospitalList = hospitalDAO.getList();
		int NearestHospitalIndex = 0;

		String topicSplit[] = topic.split("/"); // user/patient/id ���� id�� ¥��
		final String id = topicSplit[2]; // account id����
		account = acDAO.getInfo(id); // account���̺�κ��� �ش� id�� account��ü ����
		String arr[] = message.toString().split("%"); // �޽��� �и�
		String pulse = arr[0]; // �ƹ� ����
		String temp = arr[1]; // ü�� ����
		String locationArr[] = arr[2].split(":"); // ����, �浵 �и�
		String latitude = locationArr[0]; // ����(latitude) ����
		String longitude = locationArr[1]; // �浵(longitude) ����

		NearestHospitalIndex = findNearestHospital(hospitalDAO, hospitalList, latitude, longitude); // ���� ����� ���� ����

		///////////////////////////////////// Ȯ���� ���� ��� ����
		///////////////////////////////////// /////////////////////////////////////////////
		System.out.println("Mqtt topic : " + topic);
		System.out.println("Mqtt msg : " + message.toString());
		/*
		 * System.out.println(topicSplit[2]); System.out.println(arr[0]);
		 * System.out.println(arr[1]); System.out.println(arr[2]);
		 * System.out.println(locationArr[0]); System.out.println(locationArr[1]);
		 */
		System.out.println("���� ����� ���� : " + hospitalList.get(NearestHospitalIndex).getHospitalName());
		// ȯ���� ������ ���� ����� ������ ��ġ �������� �˾Ƴ�
		//////////////////////////////////////////////////////////////////////////////////////////////////

		if (Float.parseFloat(temp) > 38 || Float.parseFloat(pulse) > 90) {
			// �̻� �������� ����Ǵ� mqtt �޽��� ����

			/* �ش� id�� �Ű� �Ǿ����� ������ */

			// ���� ��ġ�̵� ���޻�Ȳ�̵��� ���� ����͸��� �����ͺ��̽��� ��� �־��־�� ��
			Monitoring monitoring = new Monitoring(); // ����͸��� �ϱ����� ��ü ����

			monitoring.setAccountInfo(account);
			monitoring.setPulse(pulse);
			monitoring.setTemp(temp);
			monitoring.setLatitude(latitude);
			monitoring.setLongtitude(longitude);

			MonitoringDAO monitoringDAO = new MonitoringDAO(); /* �ش� ������ monitoring������ ���� */
			monitoringDAO.store(monitoring); // ����͸� �����ͺ��̽��� ����

			if (emergencyJudgment.containsKey(id)) {
				// �ش� ������ �����Ͱ� �ؽ��ʿ� �ִٸ�
				emergencyJudgment.replace(id, emergencyJudgment.get(id) + 1);
				System.out.println(id + " ȯ��(����) :" + emergencyJudgment.get(id));
			} else {
				// �ش� ������ �����Ͱ� �ؽ��ʿ� ���ٸ�
				emergencyJudgment.put(id, 1); // �ش� �������� ������ ����
				System.out.println(topic + " ȯ��(�ű�) :" + emergencyJudgment.get(id));
				
					new java.util.Timer().schedule(new java.util.TimerTask() {
						@Override
						public void run() {
							if (emergencyJudgment.containsKey(id) && !(reportStatus.containsKey(id))) { /* ���� �αװ� �����Ǿ� �ش� �ؽ��� �����Ǿ��� ���� �ֱ� ������ */
								emergencyJudgment.remove(id);
							}
						}
					}, 10000 /* 5��(300,000)�� ����ϸ� �ش� �ؽ������� ����, 1,000�� 1�� */);
				}
			

			/* 20190906-02:34 ���� ���� �߼۱����� ����. ���޻�Ȳ �Ǵ� ������ �Ű��ߺ� �Ǵ� ������ �и��ؾ��� �� ����. */

			if (emergencyJudgment.get(id) >= 5) {
				// ���޻�Ȳ �˰��� : ���޻�Ȳ�� ���ӵǸ� count�� ���̴ٰ� 5������ ������ �Ǹ� log ������ ����ϰ� �����뿡�� publish �Ѵ�
				// --> ���� count>=120 ����(2��)�� ����
				// ���޻�Ȳ�� ��� �����ͺ��̽��� �α������� �����Ѵ�

				if (reportStatus.containsKey(id)) {
					/* ���� �ش� id�� �Ű� �Ǿ������� */
					/* �ƹ��͵� ���� �ʴ´�. */
				} else {
					/* �ش� id�� �Ű� �Ǿ����� ������ */
					Log log = new Log();

					log.setAccountInfo(account);
					log.setPulse(pulse);
					log.setTemp(temp);
					log.setLatitude(latitude);
					log.setLongtitude(longitude);

					LogDAO logDAO = new LogDAO(); /* �ش� ������ log������ ���� */
					logDAO.store(log); // �Ű� �α� ����
					System.out.println("�α� ����"); // �Ű� �α� ����
					SendMessageLMS.sendLMS(log,
							hospitalList.get(NearestHospitalIndex).getHospitalName());/* �Ű� �޽��� �߼� - �α׿� �αٺ��� ���� ���� */

					// emergencyJudgment.remove(id); // �Ű� �Ǿ����Ƿ� �����Ǵ� �ؽ������� ���� ---���� �����ص��ɵ�..?
					reportStatus.put(id, 1); // �Ű� �Ǿ����Ƿ� �Ű���Ȳ �ؽ��� �߰�


						/* �ش� id�� �Ű���Ȳ �ؽ������͸� 1�ð��� ������ ����. 1�ð��� ������ ��ü��ȣ�� ��ȭ�� ���ٸ� ��Ű� ���� ������ */
						new java.util.Timer().schedule(new java.util.TimerTask() {
							@Override
							public void run() {
								if (reportStatus.containsKey(id)) { // containsKey �Լ��� run() �Լ� �ȿ� ������ TimerTask�� ��� ����� ������ if����
									// ������ ���� TimerTask�� �������� ���ϵ��� ��
									reportStatus.remove(id); // �Ű���Ȳ �ؽ����� ����
									emergencyJudgment.remove(id); // �����Ǵ� �ؽ����� ����
								}
							}
						}, 10000 /* 1�ð�(3,600,000)�� ����ϸ� �ش� �ؽ������� ����, 1,000�� 1�� */);
					
					/*
					 * Ÿ�̸ӷ� 1�ð� �ڿ� �Ű���Ȳ���� �ش� id ����� ������ 1�ð��� ������ ��ü�������� ��ȭ�� ������ ������ ���� ���� ������ �Ǵ��ϰ�
					 * ��Ű� �ϱ� ���ؼ���
					 */
				}
			}

		} else {
			// �׳� ���� ��ġ�� ���

			// ���� ��ġ�̵� ���޻�Ȳ�̵��� ���� ����͸��� �����ͺ��̽��� ��� �־��־�� ��
			Monitoring monitoring = new Monitoring(); // ����͸��� �ϱ����� ��ü ����

			monitoring.setAccountInfo(account);
			monitoring.setPulse(pulse);
			monitoring.setTemp(temp);
			monitoring.setLatitude(latitude);
			monitoring.setLongtitude(longitude);

			MonitoringDAO monitoringDAO = new MonitoringDAO(); /* �ش� ������ monitoring������ ���� */
			monitoringDAO.store(monitoring); // ����͸� �����ͺ��̽��� ����

		}

	}

	public int findNearestHospital(HospitalDAO hospitalDAO, ArrayList<Hospital> hospitalList, String latitude,
			String longitude) {
		// ���� ����� ���� ��� �޼ҵ�
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