package com.collabera.Day19Maven;

import java.io.FileInputStream;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.collabera.jdbcdemo.dao.CityDao;
import com.collabera.jdbcdemo.dao.CountryDao;
import com.collabera.jdbcdemo.dao.EmployeeDao;
import com.collabera.jdbcdemo.model.City;
import com.collabera.jdbcdemo.model.Country;
import com.collabera.jdbcdemo.model.Department;
import com.collabera.jdbcdemo.model.Employee;

import java.util.List;

public class App {
	static Logger log = Logger.getLogger(App.class.getName());
    public static void main( String[] args ) throws IOException
    {
    	log.debug("Hello Wordl");
    	log.info("JDBC test app started.");
    	
    	
    	Properties props = new Properties();
    	props.load(new FileInputStream("jdbc.properties"));
    	
    	String user = props.getProperty("user");
    	String password = props.getProperty("password");
    	String dburl = props.getProperty("dburl");
    	
    	Connection conn = null;
    	
        try {
        	conn = (Connection) DriverManager.getConnection(dburl,user,password);
        	if(conn!=null) {
        		System.out.println("mysql connection successfully expired!");
        	}
        	
        	String sql = "select * from city"; 
        	PreparedStatement stmt = conn.prepareStatement(sql);
        	ResultSet rs = stmt.executeQuery();
        	while(rs.next()) {
        		log.debug(rs.getInt(1)+" "+rs.getString(2) +" "+rs.getString(3)+" "+rs.getString(4));
        				
        	}
        	
        	rs.close();
        	conn.close();
        		
        }catch(Exception ex) {
        	log.error(ex.getMessage());
        }
        
        
    
    //test CityDao
    log.info("Testing CityDao..."); 
    CityDao cityDao = new CityDao();
    try {
    	List<City> cities = cityDao.findByName("Miami");
    	if(cities.size() > 0) {
    		log.info(cities.get(0));
    	}
    	
    } catch (Exception e) {
    	log.error(e.getMessage());
    }
    
    //test CountryDao
    log.info("Testing CountryDao..."); 
    CountryDao countryDao = new CountryDao();
    
    
    try {
    	List<Country> countries = countryDao.findByName("Congo");
    	if(countries.size() > 0) {
    		log.info(countries.get(0));
    	}
    	
    } catch (Exception e) {
    	log.error(e.getMessage());
    }
    
    
//    try {
//    	String code ="XYZ";
//    	boolean count = countryDao.delete("XYZ");
//    	log.info("SUCCESS:  delete country: " +code);
//    } catch (Exception  x) {
//    	
//    }
    
    
    try {
    Country country = new Country("AAA","DDD","Asia","WWW");
    boolean success = countryDao.insert(country);
    if(success)
    	log.info("SUCCESS:  INSERT country: " +country);
    } catch (Exception  x) {
    	log.error(x.getMessage());
    }
    
 
    
    
    }
}
