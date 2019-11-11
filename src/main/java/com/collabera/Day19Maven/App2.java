package com.collabera.Day19Maven;

import java.io.FileInputStream;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

import org.apache.log4j.Logger;


import com.collabera.jdbcdemo.dao.EmployeeDao;

import com.collabera.jdbcdemo.model.Department;
import com.collabera.jdbcdemo.model.Employee;

import java.util.List;

public class App2 {
	static Logger log = Logger.getLogger(App2.class.getName());
    public static void main( String[] args ) throws IOException
    {
    	log.debug("Hello Wordl");
    	log.info("JDBC test app started.");
    	
    	
    	Properties props = new Properties();
    	props.load(new FileInputStream("jdbc2.properties"));
    	
    	String user = props.getProperty("user");
    	String password = props.getProperty("password");
    	String dburl = props.getProperty("dburl");
    	
    	Connection conn = null;
    	
        try {
        	conn = (Connection) DriverManager.getConnection(dburl,user,password);
        	if(conn!=null) {
        		System.out.println("mysql connection successfully expired!");
        	}
        	
        	String sql = "select * from employees"; 
        	PreparedStatement stmt = conn.prepareStatement(sql);
        	ResultSet rs = stmt.executeQuery();
        	while(rs.next()) {
        		log.debug(rs.getInt(1)+" "+rs.getString(2) +" "+rs.getString(3)+" "+rs.getInt(4));
        				
        	}
        	
        	rs.close();
        	conn.close();
        		
        }catch(Exception ex) {
        	log.error(ex.getMessage());
        }
        
       
    
    //test EmployeeDao
    log.info("Testing EmployeeDao..."); 
    EmployeeDao employeeDao = new EmployeeDao();
    
    
    try {
    	List<Employee> employees = employeeDao.findByName("Hernandez");
    	if(employees.size() > 0) {
    		log.info(employees.get(0));
    	}
    	
    } catch (Exception e) {
    	log.error(e.getMessage());
    }
    
    
//    try {
//    	String lastName ="Hernandez";
//    	boolean count = employeeDao.delete("Hernandez");
//    	log.info("SUCCESS:  delete employee: " +lastName);
//    } catch (Exception  x) {
//    	
//    }
    
    
    try {
    Employee employees = new Employee(1,"Hernandez","Arianne",Department.values()[0]);
    boolean success = employeeDao.insert(employees);
    if(success)
    	log.info("SUCCESS:  INSERT employees: " +employees);
    } catch (Exception  x) {
    	log.error(x.getMessage());
    }
    
    
    
    }
}

