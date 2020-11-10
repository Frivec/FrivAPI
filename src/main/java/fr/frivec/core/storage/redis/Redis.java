package fr.frivec.core.storage.redis;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;

import fr.frivec.core.storage.Credentials;

public class Redis {
	
	private RedissonClient client;
	
	private static Redis instance;
	
	public Redis(Credentials credentials) {
		
		instance = this;
		this.client = initRedisson(credentials);
		
	}
	
	public void close() {
		
		this.client.shutdown();
		
	}
	
	private RedissonClient initRedisson(final Credentials credentials) {
		
		final Config config = new Config();
		
		config.setCodec(new JsonJacksonCodec());
		config.setThreads(8);
		config.setNettyThreads(8);
		config.useSingleServer()
			.setAddress(credentials.toRedisURL())
			.setPassword(credentials.getPassword())
			.setDatabase(3)
			.setClientName(credentials.getClient());
		
		return Redisson.create(config);
		
	}
	
	public RedissonClient getClient() {
		return client;
	}
	
	public static Redis getInstance() {
		return instance;
	}

}
