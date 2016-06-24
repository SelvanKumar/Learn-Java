/**
 * 
 */
package com.sample;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
 

public class InsertSQLTest {

	private ApplicationContext appContext;
	
	@Before
	public void setUp(){
		appContext = new ClassPathXmlApplicationContext("spring-jdbc.xml");
	}
	
	@Test
	public void testPrintInsertSqls(){
		GenSelectDao genSelectDao = (GenSelectDao) appContext.getBean("genSelectDao");
		List<String> insertSqls = genSelectDao.getAllSqlQueries("SELECT * FROM asmaptitudecategory ");
		System.out.println("Insert SQLs..."+insertSqls);
	}
}
