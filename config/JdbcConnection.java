package com.se2.alert.config;

import com.se2.alert.properties.AlertProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Component
public class JdbcConnection {

	private final static Logger logger = LoggerFactory.getLogger(JdbcConnection.class);

	private Connection connection;

	@Autowired
	private AlertProperties alertProperties;

	public Connection getConnection() {
		try {
			Class.forName(alertProperties.connectionName);
			connection = DriverManager.getConnection(alertProperties.connectionUrl, alertProperties.connectionUser,
					alertProperties.connectionPassword);
		} catch (Exception e) {
			logger.error("error while jdbc connection with [{}]", e.getMessage());
		}
		return connection;
	}

	public void closeConnection() {
		try {
			if (connection != null)
				connection.close();
		} catch (SQLException e) {
			logger.error("error while close jdbc connection with [{}]", e.getMessage());
		}
	}
}
