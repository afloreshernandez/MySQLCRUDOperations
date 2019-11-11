package com.collabera.jdbcdemo.dao;



import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.collabera.jdbc.utils.JdbcUtils;
import com.collabera.jdbcdemo.model.Country;


public class CountryDao {
	
	private static final Logger logger =
			Logger.getLogger(CountryDao.class.getName()); //log4j
	// private static final CountryDao countryDao
	private static HashMap<String,Country> cache = new HashMap<String, Country>();
	
	/** retrieve a city by its id 
	 * @throws SQLException 
	 * @throws IOException 
	 * @throws FileNotFoundException */
	public Country findById( String code ) throws FileNotFoundException, IOException, SQLException {
		if(cache.containsKey(code)) return cache.get(code);
		List<Country> list = find("WHERE code = "+code);
		return list.get(0);
	}
	
	/**retrieve a city by name 
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 * @throws SQLException */
	
	public List<Country> findByName( String name) throws FileNotFoundException, IOException, SQLException{
		List<Country> list = find ("WHERE name = '"+name+"'");
		return list;
	}
	
	public List<Country> find(String query) throws FileNotFoundException, IOException, SQLException{
		
		Properties props = new Properties();
    	props.load(new FileInputStream("jdbc.properties"));
    	
    	String user = props.getProperty("user");
    	String password = props.getProperty("password");
    	String dburl = props.getProperty("dburl");
		
		List<Country> list = new ArrayList<Country>( );
		
		PreparedStatement pstmt = JdbcUtils.getConnection().prepareStatement("SELECT * FROM Country where name =?");
			    pstmt.setString(1, "Thailand");
		
		//String sqlquery = "SELECT *FROM country c" + query;
		
		try {
			ResultSet rs = pstmt.executeQuery();
			while( rs.next()) {
				Country c = resultSetToCountry(rs);
				list.add( c );
			}
		}catch ( SQLException sqle ) {
			logger.error( "error executing: "+sqle);
		}finally {
			if(pstmt!=null) try { pstmt.close(); }
			catch(SQLException e) { /* ignore it */ }	
		}
		return list;	
	}
	
	
	public boolean delete( String code)throws SQLException {
		if( code == null ) return false;
		PreparedStatement statement = JdbcUtils.getConnection()
				.prepareStatement("DELETE FROM Country where name = ?" );
		
		statement.setString(1,  code);
		
		int count = 0;
		try {
			count = statement.executeUpdate( );
		}catch (SQLException sqle) {
			logger.error("error executing delete for code: "+code);
		}finally {
			statement.close();
		}
		return count > 0;
	}
	
	
	public boolean insert( Country country)throws SQLException {
		if( country == null ) return false;
		PreparedStatement statement = JdbcUtils.getConnection()
				.prepareStatement("INSERT INTO Country(code, name, continent, region) VALUES(?,?,?,?)");
		
		statement.setString(1,  country.getCode());
		statement.setString(2,  country.getName());
		statement.setString(3,  country.getContinent());
		statement.setString(4,  country.getRegion());
		
		int count = 0;
		try {
			count = statement.executeUpdate( );
		}catch (SQLException sqle) {
			logger.error("error executing insert for country: "+country);
		}finally {
			statement.close();
		}
		return count > 0;
	}
	
	
	
	
	
	private Country resultSetToCountry (ResultSet rs) 
			throws SQLException {
		Country country = null;
		
		String code = rs.getString("code");
		
		if(cache.containsKey(code) ) country = cache.get(code);
		else country = new Country();
		
		country.setCode(code);
		country.setName(rs.getString("Name"));
		country.setContinent(rs.getString("Continent"));
		country.setRegion(rs.getString("Region"));
		//String country code = rs.getString("country code");
		
		if ( ! cache.containsKey(code)) cache.put(code, country);
		
		logger.info("get country for country "+country.getName());
		return country;	
	}


	
	
}


