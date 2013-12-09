/*******************************************************************************
 * Copyright (c) 2013 Carnegie Mellon University Silicon Valley. 
 * All rights reserved. 
 * 
 * This program and the accompanying materials are made available
 * under the terms of dual licensing(GPL V2 for Research/Education
 * purposes). GNU Public License v2.0 which accompanies this distribution
 * is available at http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * 
 * Please contact http://www.cmu.edu/silicon-valley/ if you have any 
 * questions.
 ******************************************************************************/
package cmu.sv.sensor.test;


import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

public class AbstractTest {

//	private static EmbeddedDatabase database;
	protected static SimpleJdbcTemplate jdbcTemplate;

	@BeforeClass
	public static void setup() {
//		database = new EmbeddedDatabaseBuilder().addDefaultScripts().build();

//		jdbcTemplate = new SimpleJdbcTemplate(database);
		
	}

	@AfterClass
	public static void tearDown() {
//		database.shutdown();
	}

}
