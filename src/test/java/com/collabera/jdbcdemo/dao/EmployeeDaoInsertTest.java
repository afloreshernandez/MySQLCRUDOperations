package com.collabera.jdbcdemo.dao;

import static org.junit.Assert.assertTrue;

import com.collabera.jdbcdemo.model.Department;
import com.collabera.jdbcdemo.model.Employee;

import java.sql.SQLException;

import org.junit.Test;



public class EmployeeDaoInsertTest {
	
	@Test
	public void testInsert() {
		EmployeeDao employeeDao = new EmployeeDao();
		boolean inserted = false;
		
		try {
			inserted = employeeDao.insert(new Employee(46,"Jon6","Snow6", Department.DEVELOPMENT));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		assertTrue( inserted );
	}

}
