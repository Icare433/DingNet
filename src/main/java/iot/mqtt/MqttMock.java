package iot.mqtt;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class MqttMock implements MqttClientBasicApi {

    private Map<String, List<MqttMessageConsumer>> subscribed = new HashMap<>();
    private final MqttBrokerMock broker = MqttBrokerMock.getInstance();


    public MqttMock() {
        this.connect();
    }

    @Override
    public void connect() {
        broker.connect(this);
    }

    @Override
    public void disconnect() {
        broker.disconnect(this);
        subscribed.clear();
    }

    @Override
    public void publish(String topic, MqttMessageType message) {
        broker.publish(topic, message);
    }

    /**
     * Subscribe to all the topic that start with topicFilter
     * @param topicFilter
     * @param messageListener
     */
    @Override
    public <T extends MqttMessageType> void subscribe(Object subscriber, String topicFilter, Class<T> classMessage, BiConsumer<String, T> messageListener) {
        if (!subscribed.containsKey(topicFilter)) {
            broker.subscribe(this, topicFilter);
            subscribed.put(topicFilter, new LinkedList<>());
        }
        subscribed.get(topicFilter).add(new MqttMessageConsumer<T>(subscriber, messageListener, classMessage));
    }

    @Override
    public void unsubscribe(Object subscriber, String topicFilter) {
        subscribed.get(topicFilter).removeIf(c -> c.getSubscriber().equals(subscriber));
        if (subscribed.get(topicFilter).isEmpty()) {
            subscribed.remove(topicFilter);
            broker.unsubscribe(this, topicFilter);
        }
    }

    public void dispatch(String filter, String topic, MqttMessageType message) {
        if (subscribed.containsKey(filter)) {
            subscribed.get(filter).forEach(c -> c.accept(topic, message));
        }
    }

    private static class MqttMessageConsumer<T extends MqttMessageType> {

        private final Object subscriber;
        private final BiConsumer<String, T> consumer;
        private final Class<T> clazz;

        public MqttMessageConsumer(Object subscriber, BiConsumer<String, T> consumer, Class<T> clazz) {
            this.consumer = consumer;
            this.clazz = clazz;
            this.subscriber = subscriber;
        }

        public void accept(String t, MqttMessageType message) {
            consumer.accept(t, clazz.cast(message));
        }

        public Object getSubscriber() {
            return subscriber;
        }
    }
}
