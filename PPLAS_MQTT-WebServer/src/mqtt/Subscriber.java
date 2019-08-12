package mqtt;

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
import patient.Patient;

/**
 * The Class Listner.
 * 
 * @author Yasith Lokuge
 */

public class Subscriber implements MqttCallback {

	private String brokerUrl; 	/** The broker url. */
	private String clientId;	/** The client id. */
	private String topic;	/** The topic. */

	public String getBrokerUrl() {
		return brokerUrl;
	}

	public void setBrokerUrl(String brokerUrl) {
		this.brokerUrl = brokerUrl;
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
	}	// default constructor
	
	public Subscriber(String brokerUrl, String clientId, String topic) {
		super();
		this.brokerUrl = brokerUrl;
		this.clientId = clientId;
		this.topic = topic;
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

		/*
		 * Thread th = new Thread() { public void run(){ AccountDAO acDAO = new
		 * AccountDAO(); Account patient = acDAO.getInfo(); // 아직 안만들었는데 정보 읽어오는 메소드
		 * 
		 * 
		 * System.out.println("Mqtt topic : " + topic); System.out.println("Mqtt msg : "
		 * + message.toString());
		 * 
		 * String arr[] = message.toString().split("%");
		 * 
		 * Patient patient = new Patient(); Location pl = new Location();
		 * 
		 * patient.setPulse(arr[0]); patient.setTemp(arr[1]); pl.setLatitude(arr[2]); //
		 * 위도 지정 pl.setLongitude(arr[3]); // 경도 지정 patient.setLocationInfo(pl); } };
		 */
		

		/*
		 * String pLa = patient.getLocationInfo().getLatitude(); String pLo =
		 * patient.getLocationInfo().getLongitude();
		 * 
		 * Gson gson = new Gson(); String json = gson.toJson(patient);
		 */

		
		
	}

}