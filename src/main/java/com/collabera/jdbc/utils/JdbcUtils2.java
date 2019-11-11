package com.collabera.jdbc.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class JdbcUtils2 {
	public static Connection getConnection() {
		Connection con = null;
		
		Properties props = new Properties();
		
		try {
			props.load(new FileInputStream("jdbc2.properties"));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		String user = props.getProperty("user");
		String password = props.getProperty("password");
		String dburl = props.getProperty("dburl");
		
		
		try {
			con = DriverManager.getConnection(dburl,user,password);
		} catch(SQLException e) {
			e.printStackTrace();
			
		}
		return con;
	}

}
