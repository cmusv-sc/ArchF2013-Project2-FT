/*******************************************************************************
 * Copyright (c) 2014 Carnegie Mellon University Silicon Valley. 
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

import models.ClimateService;

import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

public class ClimateServiceDaoImplementation implements ClimateServiceDao {
	private SimpleJdbcTemplate simpleJdbcTemplate;

	@Override
	public boolean addClimateService(String climateServiceName, String purpose, String url) {
		// TODO need to use this in production for SAP HANA
		final String SQL_SEQUENCE = "SELECT CMU.COURSE_CLIMATE_SERVICE_ID_SEQ.NEXTVAL FROM DUMMY";
		int climateServiceId = simpleJdbcTemplate.queryForInt(SQL_SEQUENCE);

		// TODO need to use this in production for SAP HANA
		final String SQL = "INSERT INTO CMU.COURSE_CLIMATE_SERVICE "
				+ "(CLIMATE_SERVICE_ID, CLIMATE_SERVICE_NAME, PURPOSE, URL) VALUES (?, ?, ?, ?)";

		// for test only
		// final String SQL =
		// "INSERT INTO CMU.COURSE_CLIMATE_SERVICE (CLIMATE_SERVICE_ID, CLIMATE_SERVICE_NAME, PURPOSE, URL) VALUES (next value for CMU.COURSE_CLIMATE_SERVICE_ID_SEQ, ?, ?, ?)";
		try {
			// TODO need to use this in production for SAP HANA
			simpleJdbcTemplate.update(SQL, climateServiceId,
					climateServiceName, purpose, url);
			// for test only
			// simpleJdbcTemplate.update(SQL, climateServiceName, purpose, url);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	@Override
	public boolean deleteClimateService(String climateServiceName) {
		// change to default climate service		
		final String SQL_DELETE_CLIMATE_SERVICE = "DELETE FROM CMU.COURSE_CLIMATE_SERVICE "
				+ "WHERE CLIMATE_SERVICE_NAME = ?";
		try {
			simpleJdbcTemplate.update(SQL_DELETE_CLIMATE_SERVICE,
					climateServiceName);
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	@Override
	public boolean updateClimateService(String climateServiceName,
			String purpose, String url) {
		final String SQL = "UPDATE CMU.COURSE_CLIMATE_SERVICE "
				+ "SET PURPOSE = ?, URL = ? " + "WHERE CLIMATE_SERVICE_NAME = ?";
		try {
			simpleJdbcTemplate.update(SQL, purpose, url, climateServiceName);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	@Override
	public List<ClimateService> getAllClimateServices() {
		final String SQL = "SELECT * FROM CMU.COURSE_CLIMATE_SERVICE";
		try {
			List<ClimateService> climateServices = simpleJdbcTemplate.query(
					SQL, ParameterizedBeanPropertyRowMapper
							.newInstance(ClimateService.class));
			return climateServices;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public ClimateService getClimateService(String ClimateServiceName) {
		final String SQL = "SELECT * FROM CMU.COURSE_CLIMATE_SERVICE WHERE CLIMATE_SERVICE_NAME = ?";
		try {
			ClimateService climateService = simpleJdbcTemplate.queryForObject(
					SQL, ParameterizedBeanPropertyRowMapper
							.newInstance(ClimateService.class),
					ClimateServiceName);
			return climateService;
		} catch (Exception e) {
			return null;
		}
	}

	public SimpleJdbcTemplate getSimpleJdbcTemplate() {
		return simpleJdbcTemplate;
	}

	public void setSimpleJdbcTemplate(SimpleJdbcTemplate simpleJdbcTemplate) {
		this.simpleJdbcTemplate = simpleJdbcTemplate;
	}

/*******************************************************************************
 * Copyright (c) 2014 Carnegie Mellon University Silicon Valley. 
 * All rights reserved. 
 * 
 * Create Table (add new field: URL)
 ******************************************************************************/
}
