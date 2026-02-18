![Static Badge](https://img.shields.io/badge/author-javiergs-orange)
![GitHub repo size](https://img.shields.io/github/repo-size/CSC3100/Tool-MQTT)
![Java](https://img.shields.io/badge/Java-17+-blue)
![Platform](https://img.shields.io/badge/platform-Java_Swing-orange)

This project provides three Java examples for learning MQTT using the Eclipse Paho MQTT client and the public test broker at **test.mosquitto.org**.

## 🚀 Features
- Simple **Publisher**, **Subscriber**, and **Publish–Subscriber** (Handler)
- Uses the Eclipse **Paho MQTT Client**
- Demonstrates core MQTT concepts (topics, QoS, callbacks)
- Ready for students to explore and extend

## 📦Maven Dependency
```xml
<dependency>
    <groupId>org.eclipse.paho</groupId>
    <artifactId>org.eclipse.paho.client.mqttv3</artifactId>
    <version>1.2.5</version>
</dependency>
```

## 📨 Broker and Topics

All communication goes through the public MQTT broker:

- **Broker URL:** `tcp://test.mosquitto.org:1883`

The handler uses two topics:

- `example/toIgnore`
- `example/toRead`

The subscriber listens to both, but you can imagine `example/toIgnore` as background or noisy data and `example/toRead` as the “interesting” stream.
You are encouraged to change these topic strings and observe how behavior changes.


## 📘 Class Overview

### `Handler.java` 

This is the **only** class you need to run to see the full publish/subscribe demo. 
- Defines the broker URL and the topics (`example/toIgnore`, `example/toRead`)
- Creates a `Subscriber` and a `Publisher`, each with the same broker and topics
- Starts both as independent threads so they run concurrently

- **How to run:**

```bash
javac *.java
java Handler
```

### `Publisher.java`

A **Runnable** that connects to the broker and periodically publishes messages to the two topics given by `Handler`.

**Conceptual behavior:**

- Connects to `tcp://test.mosquitto.org:1883`
- For a fixed number of iterations:
    - Builds one message for `example/toIgnore`
    - Builds one message for `example/toRead`
    - Publishes both with a chosen QoS (e.g., 2)
- Logs each published message to the console
- Disconnects cleanly at the end

### `Subscriber.java`

A **Runnable** and **MqttCallback** that connects to the broker and subscribes to the given topics.

**Conceptual behavior:**

- Connects to `tcp://test.mosquitto.org:1883`
- Subscribes to both:
    - `example/toIgnore`
    - `example/toRead`
- Implements `messageArrived(...)` to print incoming messages:


## 🧪 Example Console Output

### Subscriber Output Example

```text
📥 Connected to broker: tcp://test.mosquitto.org:1883
📥 Subscribed to: example/toIgnore
📥 Subscribed to: example/toRead
📥 Delivery :: [example/toRead : 2] :: this is message to read 0
📥 Delivery :: [example/toIgnore : 2] :: this is message to ignore 0
```

### Publisher Output Example

```text
↗️ Connected to broker: tcp://test.mosquitto.org:1883
↗️ Published to example/toIgnore: this is message to ignore 0
↗️ Published to example/toRead: this is message to read 0
```

## 🏞️ Class Diagram
A drafted class diagram of the current release is as follows:

<img width="989" height="610" alt="Class Diagram0" src="https://github.com/user-attachments/assets/eaeb2ff2-f452-4c37-af8d-aab16f076a6a" />

## 🎯 Notes & Tips

- `test.mosquitto.org` is a **public** broker:
    - Do **not** send sensitive or private data.
    - Anyone can publish/subscribe on the same topics.
- If you don’t see messages:
    - Make sure `Handler` is running (both publisher and subscriber threads are started).
    - Check your network and any firewalls that might block port **1883**.
    - Confirm that the broker is online (the public test broker can occasionally restart).

