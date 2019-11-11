package com.collabera.jdbcdemo.dao;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.collabera.jdbc.utils.JdbcUtils;
import com.collabera.jdbcdemo.model.City;


public class CityDao {
	
	private static final Logger logger =
			Logger.getLogger(CityDao.class.getName()); //log4j
	// private static final CountryDao countryDao
	private static HashMap<Integer,City> cache = new HashMap<Integer, City>();
	
	/** retrieve a city by its id 
	 * @throws SQLException 
	 * @throws IOException 
	 * @throws FileNotFoundException */
	public City findById( Integer id ) throws FileNotFoundException, IOException, SQLException {
		if(cache.containsKey(id)) return cache.get(id);
		List<City> list = find("WHERE id= "+id);
		return list.get(0);
	}
	
	
	/**retrieve a city by name 
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 * @throws SQLException */
	
	public List<City> findByName( String name) throws FileNotFoundException, IOException, SQLException{
		name = sanitize(name);
		List<City> list = find ("WHERE name = '"+name+"'");
		return list;
	}
	
	
	public List<City> find(String query) throws FileNotFoundException, IOException, SQLException{
		
		List<City> list = new ArrayList<City>( );
		Statement stmt = JdbcUtils.getConnection().createStatement();
		
		String sqlquery = "SELECT *FROM city " + query;
		
		try {
			logger.debug("executing query: " + sqlquery);
			ResultSet rs = stmt.executeQuery(  sqlquery);
			while(rs.next()) {
				City c = resultSetToCity( rs );
				list.add(c);
			}
		}catch ( SQLException sqle ) {
			logger.error( "error executing: "+sqlquery, sqle);
		}finally {
			if(stmt!=null) try { stmt.close(); }
			catch(SQLException e) { /* ignore it */ }
		}
		return list;
	}
	
	
	public String sanitize(String name) {
		return "";
	}
	
	private City resultSetToCity (ResultSet rs) 
			throws SQLException {
		City city = null;
		
		Integer id = rs.getInt("id");
		
		if(cache.containsKey(id) ) city = cache.get(id);
		else city = new City();
		
		city.setId(id);
		city.setName(rs.getString("Name"));
		city.setDistrict(rs.getString("District"));
		city.setPopulation(rs.getInt("Population"));
		//String countrycode = rs.getString("countrycode");
		
		if ( ! cache.containsKey(id)) cache.put(id, city);
		
		logger.info("get country for city "+city.getName());
		return city;
		}
	

}

