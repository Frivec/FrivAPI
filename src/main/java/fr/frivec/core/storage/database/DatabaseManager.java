package fr.frivec.core.storage.database;

import java.sql.Connection;
import java.sql.SQLException;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import fr.frivec.core.storage.Credentials;

public class DatabaseManager {
	
	private DatabaseAccess access;
	
	public DatabaseManager(final Credentials credentials) {
		
		this.access = new DatabaseAccess(credentials);
		
	}
	
	public void initAllDatabaseConnections() {
		
		this.access.initPool();
		
	}
	
	public void closeAllDatabaseConnections() {
		
		this.access.closePool();
		
	}
	
	public Connection getConnection() {
		
		return this.access.getConnection();
		
	}
	
	public DatabaseAccess getAccess() {
		return access;
	}
	
	public void setAccess(DatabaseAccess access) {
		this.access = access;
	}
	
	private class DatabaseAccess {
		
		//Classe de configuration de HikariCP
		
		private Credentials credentials;
		private HikariDataSource dataSource;
		
		public DatabaseAccess(Credentials credentials) {
			
			this.credentials = credentials;
			
		}
		
		private void setupHikariCP() {
			
			final HikariConfig config = new HikariConfig();
			
			config.setMaximumPoolSize(credentials.getPoolSize());
			config.setJdbcUrl(credentials.toMySqlURL());
			config.setUsername(credentials.getClient());  
			config.setPassword(credentials.getPassword());
			config.setMaxLifetime(600000L);
			config.setIdleTimeout(300000L);
			config.setLeakDetectionThreshold(300000L);
			config.setConnectionTimeout(10000L);
			
			this.dataSource = new HikariDataSource(config);
			
		}
		
		public void initPool() {
			setupHikariCP();
		}
		
		public void closePool() {
			this.dataSource.close();
		}
		
		public Connection getConnection() {
			
			if(this.dataSource == null) {
				
				System.out.println("Not connected. Reconnection in proress..");
				setupHikariCP();
				
			}
			
			Connection connection = null;
			
			try {
				connection = this.dataSource.getConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			return connection;
			
		}
		
	}

}
