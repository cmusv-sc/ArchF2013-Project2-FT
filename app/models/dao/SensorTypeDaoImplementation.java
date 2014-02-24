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

import models.SensorType;

import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

public class SensorTypeDaoImplementation implements SensorTypeDao{
	private SimpleJdbcTemplate simpleJdbcTemplate;

	@Override
	public boolean addSensorType(String sensorTypeName,
			String manufacturer, String version, double maxValue,
			double minValue, String unit, String interpreter,
			String userDefinedFields, String sensorCategoryName) {
//		Check if SensorTypeName is duplicate
		final String SQL_CHECK = "SELECT * FROM CMU.COURSE_SENSOR_TYPE WHERE SENSOR_TYPE_NAME=?";
		try {
		List<SensorType> types = simpleJdbcTemplate.query(SQL_CHECK, 
				ParameterizedBeanPropertyRowMapper.newInstance(SensorType.class), 
				sensorTypeName);
		if(types.size() > 0){
			return false;
		}} catch(Exception e) {
			return false;
		}
		
//		Find SensorCategoryId by SensorCategoryName, return false if SensorCategoryId is not found
		int sensorCategoryId = -1;
		final String SQL_FIND_CATEGORY_ID =
				"SELECT SENSOR_CATEGORY_ID "
				+ "FROM CMU.COURSE_SENSOR_CATEGORY "
				+ "WHERE SENSOR_CATEGORY_NAME = ?";
		try{
			sensorCategoryId = simpleJdbcTemplate.queryForInt(SQL_FIND_CATEGORY_ID, sensorCategoryName);
		}catch(Exception e){
			return false;
		}
		
//		TODO: Need to use this in production for SAP HANA
		final String SQL_SEQUENCE = "SELECT CMU.COURSE_SENSOR_TYPE_ID_SEQ.NEXTVAL FROM DUMMY";
		try{
			int sensorTypeId = simpleJdbcTemplate.queryForInt(SQL_SEQUENCE);
		
		
//		TODO: Need to use this in production for SAP HANA
		final String SQL = "INSERT INTO CMU.COURSE_SENSOR_TYPE (SENSOR_TYPE_ID, SENSOR_TYPE_NAME, MANUFACTURER, VERSION, MAX_VALUE, MIN_VALUE, UNIT, INTERPRETER, SENSOR_TYPE_USER_DEFINED_FIELDS, SENSOR_CATEGORY_ID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";		
//		Test Only
//		final String SQL = "INSERT INTO CMU.COURSE_SENSOR_TYPE (SENSOR_TYPE_ID, SENSOR_TYPE_NAME, MANUFACTURER, VERSION, MAX_VALUE, MIN_VALUE, UNIT, INTERPRETER, SENSOR_TYPE_USER_DEFINED_FIELDS, SENSOR_CATEGORY_ID) VALUES (NEXT VALUE FOR CMU.COURSE_SENSOR_TYPE_ID_SEQ, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
//			TODO: Need to use this in production for SAP HANA
			simpleJdbcTemplate.update(SQL, sensorTypeId, sensorTypeName, 
					manufacturer, version, maxValue, minValue, unit, 
					interpreter, userDefinedFields, sensorCategoryId);
//			Test Only
//			simpleJdbcTemplate.update(SQL, sensorTypeName, 
//					manufacturer, version, maxValue, minValue, unit, 
//					interpreter, userDefinedFields, sensorCategoryId);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
//		Set SensorCategoryId
		final String SQL_UPDATE_CATEGORY_ID = "UPDATE CMU.COURSE_SENSOR_TYPE "
				+ "SET SENSOR_CATEGORY_ID = ? "
				+ "WHERE SENSOR_TYPE_NAME = ?";
		try{
			simpleJdbcTemplate.update(SQL_UPDATE_CATEGORY_ID, sensorCategoryId, sensorTypeName);
		}catch(Exception e){
			return false;
		}
		return true;
	}
	

	@Override
	public boolean updateSensorType(String sensorTypeName, String userDefinedFields) {
		final String SQL = "UPDATE CMU.COURSE_SENSOR_TYPE " 
				+ "SET SENSOR_TYPE_USER_DEFINED_FIELDS = ? " 
				+ "WHERE SENSOR_TYPE_NAME = ?";		
		try{
			simpleJdbcTemplate.update(SQL, userDefinedFields, sensorTypeName);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	@Override
	public SensorType getSensorType(String sensorTypeName) {
		final String SQL = "SELECT ST.*, SC.SENSOR_CATEGORY_NAME FROM CMU.COURSE_SENSOR_TYPE ST, CMU.COURSE_SENSOR_CATEGORY SC " 
				+ "WHERE ST.SENSOR_TYPE_NAME = ? AND ST.SENSOR_CATEGORY_ID = SC.SENSOR_CATEGORY_ID";
		try{
			SensorType type = simpleJdbcTemplate.queryForObject(SQL, 
					ParameterizedBeanPropertyRowMapper.newInstance(SensorType.class), sensorTypeName);
			return type;
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public List<SensorType> getAllSensorTypes() {
		final String SQL = "SELECT * FROM CMU.COURSE_SENSOR_TYPE";
		List<SensorType> sensorType = null;
		try {
			sensorType = simpleJdbcTemplate.query(SQL, ParameterizedBeanPropertyRowMapper.newInstance(SensorType.class));
		}catch(Exception e) {
			return null;
		}

		for(SensorType type : sensorType){
			String sensorCategoryName = new String();
			final String SQL_GET_CATEGORY_ID =
					"SELECT SENSOR_CATEGORY_ID "
							+ "FROM CMU.COURSE_SENSOR_TYPE "
							+ "WHERE SENSOR_TYPE_NAME = ?";
			final String SQL_FIND_CATEGORY_NAME =
					"SELECT SENSOR_CATEGORY_NAME "
							+ "FROM CMU.COURSE_SENSOR_CATEGORY "
							+ "WHERE SENSOR_CATEGORY_ID = ?";
			int sensorCategoryId = -1;
			try{
				sensorCategoryId = simpleJdbcTemplate.queryForInt(SQL_GET_CATEGORY_ID, type.getSensorTypeName());
				sensorCategoryName = simpleJdbcTemplate.queryForObject(SQL_FIND_CATEGORY_NAME, String.class, sensorCategoryId);
				type.setSensorCategoryName(sensorCategoryName);
			}catch(Exception e){
				type.setSensorCategoryName("");
			}
		}
		
		return sensorType;
	}

	@Override
	public boolean deleteSensorType(String sensorTypeName) {
		final String SQL_DELETE_SENSOR_TYPE = "DELETE FROM CMU.COURSE_SENSOR_TYPE "
						+ "WHERE SENSOR_TYPE_NAME = ?";
		try{
			simpleJdbcTemplate.update(SQL_DELETE_SENSOR_TYPE, sensorTypeName);
		}catch(Exception e){
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
