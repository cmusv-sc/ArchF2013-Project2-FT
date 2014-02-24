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
package models.dao;

import java.util.List;

import models.SensorCategory;

import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

public class SensorCategoryDaoImplementation implements SensorCategoryDao {
	private SimpleJdbcTemplate simpleJdbcTemplate;

	@Override
	public boolean addSensorCategory(String sensorCategoryName, String purpose) {
		// TODO need to use this in production for SAP HANA
		final String SQL_SEQUENCE = "SELECT CMU.COURSE_SENSOR_CATEGORY_ID_SEQ.NEXTVAL FROM DUMMY";
		int sensorCategoryId = simpleJdbcTemplate.queryForInt(SQL_SEQUENCE);

		// TODO need to use this in production for SAP HANA
		final String SQL = "INSERT INTO CMU.COURSE_SENSOR_CATEGORY (SENSOR_CATEGORY_ID, SENSOR_CATEGORY_NAME, PURPOSE) VALUES (?, ?, ?)";

		// for test only
		// final String SQL =
		// "INSERT INTO CMU.COURSE_SENSOR_CATEGORY (SENSOR_CATEGORY_ID, SENSOR_CATEGORY_NAME, PURPOSE) VALUES (next value for CMU.COURSE_SENSOR_CATEGORY_ID_SEQ, ?, ?)";
		try {
			// TODO need to use this in production for SAP HANA
			simpleJdbcTemplate.update(SQL, sensorCategoryId,
					sensorCategoryName, purpose);
			// for test only
			// simpleJdbcTemplate.update(SQL, sensorCategoryName, purpose);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	@Override
	public boolean updateSensorCategory(String sensorCategoryName,
			String purpose) {
		final String SQL = "UPDATE CMU.COURSE_SENSOR_CATEGORY "
				+ "SET PURPOSE = ? " + "WHERE SENSOR_CATEGORY_NAME = ?";
		try {
			simpleJdbcTemplate.update(SQL, purpose, sensorCategoryName);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	@Override
	public List<SensorCategory> getAllSensorCategories() {
		final String SQL = "SELECT * FROM CMU.COURSE_SENSOR_CATEGORY";
		try {
			List<SensorCategory> sensorCategories = simpleJdbcTemplate.query(
					SQL, ParameterizedBeanPropertyRowMapper
							.newInstance(SensorCategory.class));
			return sensorCategories;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public SensorCategory getSensorCategory(String SensorCategoryName) {
		final String SQL = "SELECT * FROM CMU.COURSE_SENSOR_CATEGORY WHERE SENSOR_CATEGORY_NAME = ?";
		try {
			SensorCategory sensorCategory = simpleJdbcTemplate.queryForObject(
					SQL, ParameterizedBeanPropertyRowMapper
							.newInstance(SensorCategory.class),
					SensorCategoryName);
			return sensorCategory;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public boolean deleteSensorCategory(String sensorCategoryName) {
		// change to default sensor category
		final String SQL_RESET_SENSOR_TYPES = "UPDATE CMU.COURSE_SENSOR_TYPE "
				+ "SET SENSOR_CATEGORY_ID = 0 "
				+ "WHERE SENSOR_CATEGORY_ID = "
				+ "(SELECT SENSOR_CATEGORY_ID FROM CMU.COURSE_SENSOR_CATEGORY "
				+ "WHERE SENSOR_CATEGORY_NAME = ?) ";
		
		final String SQL_DELETE_SENSOR_CATEGORY = "DELETE FROM CMU.COURSE_SENSOR_CATEGORY "
				+ "WHERE SENSOR_CATEGORY_NAME = ?";
		try {
			simpleJdbcTemplate.update(SQL_RESET_SENSOR_TYPES,
					sensorCategoryName);
			simpleJdbcTemplate.update(SQL_DELETE_SENSOR_CATEGORY,
					sensorCategoryName);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public SimpleJdbcTemplate getSimpleJdbcTemplate() {
		return simpleJdbcTemplate;
	}

	public void setSimpleJdbcTemplate(SimpleJdbcTemplate simpleJdbcTemplate) {
		this.simpleJdbcTemplate = simpleJdbcTemplate;
	}

}
