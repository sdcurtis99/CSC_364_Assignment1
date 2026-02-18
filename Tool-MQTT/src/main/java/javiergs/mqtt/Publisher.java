package javiergs.mqtt;

import org.eclipse.paho.client.mqttv3.*;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class Publisher implements PropertyChangeListener {

	private MqttClient client;

	public Publisher() {
		try {
			client = new MqttClient(
					Blackboard.BROKER,
					MqttClient.generateClientId()
			);
			client.connect();
			System.out.println("Publisher connected to broker.");

			publishCurrentPosition();

		} catch (MqttException e) {
			e.printStackTrace();
		}

		Blackboard.getInstance().addPropertyChangeListener(this);
	}

	private void publishCurrentPosition() {
		Player me = Blackboard.getInstance().getMe();
		String payload = me.getId() + "," + me.getX() + "," + me.getY();

		try {
			MqttMessage message = new MqttMessage(payload.getBytes());
			message.setQos(0);
			client.publish(Blackboard.TOPIC, message);
			System.out.println("Initial position published: " + payload);
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {

		if (!"move".equals(evt.getPropertyName())) return;

		Player me = (Player) evt.getNewValue();
		String payload = me.getId() + "," + me.getX() + "," + me.getY();

		try {
			MqttMessage message = new MqttMessage(payload.getBytes());
			message.setQos(0);
			client.publish(Blackboard.TOPIC, message);
			System.out.println("Published: " + payload);
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}
}