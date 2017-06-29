package com.pratice.LeanTass.dbconnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
@SuppressWarnings("unused")
@Component
@PropertySource("classpath:/application.properties")
public class DBConnection {
	private final static String USER = "lakshmi"; 
	private final static String PASSWORD = "lakshmi";
	private final static String URL="jdbc:oracle:thin:@localhost:1521:xe";
	private final static String DRIVER = "oracle.jdbc.driver.OracleDriver";
	
	
	protected final Logger log = LoggerFactory.getLogger(getClass());	
	/**
	 * Static method that returns the instance for the singleton
	 *
	 * @return {Connection} connection
	 **/
	public  Connection getConnection() {
		Connection connection=null;
		try {
			Class.forName(DRIVER);			
				connection = DriverManager.getConnection(URL, USER, PASSWORD);		
				System.out.println(connection);
		} catch (ClassNotFoundException e) {
			System.out.println("Some class Issue");
			
			log.error("ClassNotFoundException at getConnection",e);
		}catch (SQLException e) {
			System.out.println("Some Issue");
			log.error("SQL Exception at getConnection",e);
		}		
		return connection;
	}	
	
	public void closeConnection(Connection connection){
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				log.error("SQL Exception at closeConnection",e);
				
			}
		}
	}
	
}
