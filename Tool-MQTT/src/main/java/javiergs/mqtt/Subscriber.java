package javiergs.mqtt;

import org.eclipse.paho.client.mqttv3.*;

public class Subscriber implements MqttCallback {

	private MqttClient client;

	public Subscriber() {
		try {
			client = new MqttClient(
					Blackboard.BROKER,
					MqttClient.generateClientId()
			);

			client.setCallback(this);
			client.connect();
			client.subscribe(Blackboard.TOPIC);

			System.out.println("Subscriber connected and subscribed.");

		} catch (MqttException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void connectionLost(Throwable cause) {
		System.out.println("Connection lost.");
		cause.printStackTrace();
	}

	@Override
	public void messageArrived(String topic, MqttMessage message) {

		String payload = new String(message.getPayload());
		System.out.println("Received: " + payload);

		Blackboard.getInstance().updateRemotePlayer(payload);
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
		// Not needed
	}
}