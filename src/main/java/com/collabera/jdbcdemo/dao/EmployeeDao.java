package com.collabera.jdbcdemo.dao;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.collabera.jdbc.utils.JdbcUtils;
import com.collabera.jdbc.utils.JdbcUtils2;
import com.collabera.jdbcdemo.model.Department;
import com.collabera.jdbcdemo.model.Employee;

public class EmployeeDao {
	
	private static final Logger logger =
			Logger.getLogger(EmployeeDao.class.getName()); //log4j
	// private static final CountryDao countryDao
	public static void main(String[] args) throws IOException, SQLException {
	
	
	 try {
    	 List<Employee> employees = getAllEmployees();
    	 System.out.println("Printing out all employees!");
    	 for(int i=0; i <employees.size(); i++);
    	 }
     catch (Exception x) {
    	 logger.error(x.getMessage());
    	 
     }
	}

    	 
	private static HashMap<Integer,Employee> cache = new HashMap<Integer, Employee>();
	
	/** retrieve a city by its id 
	 * @throws SQLException 
	 * @throws IOException 
	 * @throws FileNotFoundException */
	public Employee findById( Integer id  ) throws FileNotFoundException, IOException, SQLException {
		if(cache.containsKey(id)) return cache.get(id );
		List<Employee> list = find("WHERE id= "+id );
		return list.get(0);
	}
	
	
	/**retrieve a city by name 
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 * @throws SQLException */
	
	public List<Employee> findByName( String lastName) throws FileNotFoundException, IOException, SQLException{
		lastName = sanitize(lastName);
		List<Employee> list = find ("WHERE lastName = '"+lastName+"'");
		return list;
	}
	
	
	public List<Employee> find(String query) throws FileNotFoundException, IOException, SQLException{
		
		List<Employee> list = new ArrayList<Employee>( );
		Statement stmt = JdbcUtils2.getConnection().createStatement();
		
		String sqlquery = "SELECT *FROM employees " + query;
		
		try {
			logger.debug("executing query: " + sqlquery);
			ResultSet rs = stmt.executeQuery(  sqlquery);
			while(rs.next()) {
				Employee c = resultSetToEmployee( rs );
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
	
	
	public boolean delete( int id)throws SQLException {
		//if( id == null ) return false;
		Connection conn = JdbcUtils2.getConnection();
		PreparedStatement statement = conn.prepareStatement("DELETE FROM `employees` where employee_id = ?" );
		statement.setInt(1,  id);
		
		int count = 0;
		try {
			count = statement.executeUpdate( );
		}catch (SQLException sqle) {
			logger.error("error executing delete for id: "+id);
		}finally {
			statement.close();
		}
		return count > 0;
	}
	
	
	public boolean insert( Employee employee)throws SQLException {
		if( employee == null ) return false;
		
		Connection conn = JdbcUtils2.getConnection();
		PreparedStatement statement = conn.prepareStatement("INSERT INTO `employees`  VALUES (?,?,?,?)");
		
	
		statement.setInt(1,  employee.getId());
		statement.setString(2,  employee.getLastName());
		statement.setString(3,  employee.getFirstName());
		statement.setInt(4,  employee.getDepartment().ordinal()+1);
		
		
		int count = 0;
		try {
			count = statement.executeUpdate( );
		}catch (SQLException sqle) {
			logger.error("error executing insert for employee: "+employee);
		}finally {
			statement.close();
		}
		return count > 0;
	}
	
	
	
	public String sanitize(String id) {
		return "";
	}
	
	private Employee resultSetToEmployee (ResultSet rs) 
			throws SQLException {
		Employee employee = null;
		
		Integer id = rs.getInt("id");
		
		if(cache.containsKey(id) ) employee = cache.get(id);
		else employee = new Employee();
		
		// array for enum
		Department[] depts = Department.values();
		
		employee.setId(rs.getInt("employee_id"));
		employee.setLastName(rs.getString("lastName"));
		employee.setFirstName(rs.getString("firstName"));
		employee.setDepartment(depts[rs.getInt("department_id")-1]);
		
		//String countrycode = rs.getString("countrycode");
		
		if ( ! cache.containsKey(id)) cache.put(id, employee);
		
		logger.info("get employee for employee "+employee.getFirstName()+" "+employee.getLastName());
		return employee;
		}

     public static List<Employee> getAllEmployees() throws FileNotFoundException, IOException, SQLException{
		
	  Connection conn = JdbcUtils2.getConnection();
      List<Employee> employees = new ArrayList<Employee>();
      Department[] department = Department.values();
      
      if(conn!=null) {
          System.out.println("mysql connection successfully acquired!");}
      String sql = "select * from employees";
      PreparedStatement pstmt = conn.prepareStatement(sql);
      ResultSet rs = pstmt.executeQuery();
      
      while(rs.next()) {
          logger.info(rs.getInt("employee_id") + " " +
                  rs.getString("lastName") + " " +
                  rs.getString("firstName") + " " +
                  rs.getInt("department_id"));
          Employee employee = new Employee();
          employee.setId(rs.getInt("employee_id"));
          employee.setLastName(rs.getString("lastName"));
          employee.setFirstName(rs.getString("firstName"));
          for (Department d : department)
          { if ((d.ordinal()+1) == rs.getInt("department_id"))  //integer value of enum starts from 0
          {employee.setDepartment(d);}
          }
          employees.add(employee);

      }
      rs.close();
      conn.close();
      
      return employees;

}
	    
    
}




