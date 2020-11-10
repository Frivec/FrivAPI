package fr.frivec.core.storage.redis.pubsub;

import org.redisson.api.listener.MessageListener;

public abstract class PubSubListener implements MessageListener<String> {
	
	@Override
	public abstract void onMessage(CharSequence channel, String msg);
	
}
