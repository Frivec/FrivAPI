package fr.frivec.core.storage.redis.pubsub;

import java.util.HashMap;
import java.util.Map;

import org.redisson.api.RTopic;

import fr.frivec.core.storage.redis.Redis;

public class PubSubManager {
	
	public Map<String, RTopic> topics;
	private PubSubListener listener;
	
	public PubSubManager(final PubSubListener listener) {
		
		this.topics = new HashMap<>();
		this.listener = listener;
		
	}
	
	public void subscribe(final String topicName) {
		
		if(topics.containsKey(topicName))
			
			return;
		
		final RTopic newTopic = Redis.getInstance().getClient().getTopic(topicName);
		
		newTopic.addListener(String.class, this.listener);
		
		topics.put(topicName, newTopic);
		
	}
	
	public void unsubscribe(final String topicName) {
		
		if(!topics.containsKey(topicName))
			
			return;
		
		final RTopic topic = topics.get(topicName);
		
		topic.removeListener(0);
		
		topics.remove(topicName);
		
	}
	
	public void publish(final String topicName, final String message) {
		
		if(!this.topics.containsKey(topicName))
			
			return;
		
		this.topics.get(topicName).publish(message);
		
	}

}
