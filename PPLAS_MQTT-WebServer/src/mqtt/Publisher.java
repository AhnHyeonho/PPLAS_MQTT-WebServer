package mqtt;


import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;


/**
 * The Class Publisher.
 * @author Yasith Lokuge
 */
public class Publisher {

	
	private String brokerUrl;	// broker URL
	private String clientId; // Client ID
	private String topic; // MQTT Topic
	private String content; // MQTT Message
	private int qos = 0; // MQTT QoS
	
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getQos() {
		return qos;
	}

	public void setQos(int qos) {
		this.qos = qos;
	}
	
	public Publisher() {
		
	}	// default constructor
	
	public Publisher(String brokerUrl, String clientId, String topic) {
		this.brokerUrl = brokerUrl;
		this.clientId = clientId;
		this.topic = topic;
	}	// constructor
	
	public Publisher(String brokerUrl, String clientId, String topic, int qos) {
		this.brokerUrl = brokerUrl;
		this.clientId = clientId;
		this.topic = topic;
		this.qos = qos;
	}	// constructor

	public void myPublish() {
		
        MemoryPersistence persistence = new MemoryPersistence();

        try {
            
            MqttClient sampleClient = new MqttClient(brokerUrl, clientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            
            System.out.println("Connecting to broker: " + brokerUrl);
            sampleClient.connect(connOpts);
            System.out.println("Connected");
            System.out.println("Publishing message: " + content);
            
            MqttMessage message = new MqttMessage(content.getBytes());
            message.setQos(qos);
            sampleClient.publish(topic, message);
            System.out.println("Message published");
            sampleClient.disconnect();
            System.out.println("Disconnected");
                        
        } catch (MqttException me) {
            System.out.println("reason " + me.getReasonCode());
            System.out.println("msg " + me.getMessage());
            System.out.println("loc " + me.getLocalizedMessage());
            System.out.println("cause " + me.getCause());
            System.out.println("excep " + me);
            me.printStackTrace();   
        }
	}
}