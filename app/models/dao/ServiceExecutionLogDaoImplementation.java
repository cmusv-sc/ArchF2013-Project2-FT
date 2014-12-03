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

import models.ServiceExecutionLog;

import models.ServiceParamter;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;

public class ServiceExecutionLogDaoImplementation implements ServiceExecutionLogDao {
	private SimpleJdbcTemplate simpleJdbcTemplate;

	@Override
	public boolean addServiceExecutionLog(String serviceId, String userId, String purpose, 
			String serviceConfigurationId, String datasetLogId, String executionStartTime, 
				String executionEndTime) {
		// TODO need to use this in production for SAP HANA
		final String SQL_SEQUENCE = "SELECT CMU.COURSE_CLIMATE_SERVICE_ID_SEQ.NEXTVAL FROM DUMMY";
		int serviceExecutionLogId = simpleJdbcTemplate.queryForInt(SQL_SEQUENCE);

		// TODO need to use this in production for SAP HANA
		final String SQL = "INSERT INTO CMU.COURSE_SERVICE_EXECUTION_LOG "
				+ "(SERVICEEXECUTIONLOGID, SERVICEID, USERID, PURPOSE, SERVICECONFIGURATIONID, DATASETLOGID, EXECUTIONSTARTTIME, "
				+ "EXECUTIONENDTIME ) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

		try {
			// TODO need to use this in production for SAP HANA
			simpleJdbcTemplate.update(SQL, serviceExecutionLogId, serviceId, userId, purpose, 
					serviceConfigurationId, datasetLogId, executionStartTime, 
						executionEndTime);

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

    @Override
    public List<ServiceExecutionLog> getAllServiceExecutionLogs() {
        final String SQL = "SELECT * FROM CMU.COURSE_SERVICE_EXECUTION_LOG";
        try {
            List<ServiceExecutionLog> executionLogs = simpleJdbcTemplate.query(
                    SQL, ParameterizedBeanPropertyRowMapper
                            .newInstance(ServiceExecutionLog.class));
            return executionLogs;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<ServiceExecutionLog> getServiceExecutionLogs(String userId, String startTime, String endTime) {
        final String SQL = "SELECT * FROM CMU.COURSE_SERVICE_EXECUTION_LOG WHERE USERID="
                +"? AND EXECUTIONSTARTTIME>=? AND EXECUTIONSTARTTIME<=?";
        try {
            List<ServiceExecutionLog> executionLogs = simpleJdbcTemplate.query(
                    SQL, ParameterizedBeanPropertyRowMapper
                            .newInstance(ServiceExecutionLog.class), userId, startTime, endTime);
            return executionLogs;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<ServiceParamter> getAllServiceParameters() {
        final String SQL = "SELECT * FROM CMU.COURSE_PARAMETER";
        try {
            List<ServiceParamter> parameters = simpleJdbcTemplate.query(
                    SQL, ParameterizedBeanPropertyRowMapper
                            .newInstance(ServiceParamter.class));
            return parameters;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean addServiceParameter(String serviceId, String parameterDateType, String parameterRange, String parameterNumeration, String parameterRule, String parameterPurpose) {

        // TODO need to use this in production for SAP HANA
        final String SQL_SEQUENCE = "SELECT CMU.COURSE_CLIMATE_SERVICE_ID_SEQ.NEXTVAL FROM DUMMY";
        int serviceParameterId = simpleJdbcTemplate.queryForInt(SQL_SEQUENCE);

        // TODO need to use this in production for SAP HANA
        final String SQL = "INSERT INTO CMU.COURSE_PARAMETER "
                + "(PARAMETERID, SERVICEID, PARAMETERDATATYPE, PARAMETERRANGE, PARAMETERENUMERATION, PARAMETERRULE, PARAMETERPURPOSE"
                + " ) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try {
            // TODO need to use this in production for SAP HANA
            simpleJdbcTemplate.update(SQL, serviceParameterId, serviceId, parameterDateType, parameterRange, parameterNumeration,
                    parameterRule, parameterPurpose);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


/*	
// ToDo: the following methods need to be modified
	@Override
	public boolean deleteServiceExecutionLog(String climateServiceName) {
		// change to default climate service		
		final String SQL_DELETE_CLIMATE_SERVICE = "DELETE FROM CMU.COURSE_CLIMATE_SERVICE "
				+ "WHERE CLIMATE_SERVICE_NAME = ?";
		try {
			simpleJdbcTemplate.update(SQL_DELETE_CLIMATE_SERVICE,
					climateServiceName);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	@Override
	public boolean updateServiceExecutionLog(String serviceExecutionLogId, String serviceId, 
			String userId, String purpose, String serviceConfigurationId, String datasetLogId, 
			String executionDate, String executionStartTime, String executionEndTime) {
		final String SQL = "UPDATE CMU.COURSE_CLIMATE_SERVICE "
				+ "SET PURPOSE = ?, URL = ?, SCENARIO = ?, CREATORID = ?, "
				+ "CREATETIME = ?, VERSIONNO = ?, ROOTSERVICEID = ? " + "WHERE CLIMATE_SERVICE_NAME = ?";
		try {
			simpleJdbcTemplate.update(SQL, purpose, url, scenario, creatorId, createTime, 
					versionNo, rootServiceId, climateServiceName);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public List<ServiceExecutionLog> getServiceExecutionLogs(String userId, 
			String executionStartTime, String executionEndTime) {
		final String SQL = "SELECT * FROM CMU.COURSE_CLIMATE_SERVICE";
		try {
			List<ClimateService> climateServices = simpleJdbcTemplate.query(
					SQL, ParameterizedBeanPropertyRowMapper
							.newInstance(ClimateService.class));
			return climateServices;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public ServiceExecutionLog getServiceExecutionLog(String ClimateServiceName) {
		final String SQL = "SELECT * FROM CMU.COURSE_CLIMATE_SERVICE WHERE CLIMATE_SERVICE_NAME = ?";
		try {
			ClimateService climateService = simpleJdbcTemplate.queryForObject(
					SQL, ParameterizedBeanPropertyRowMapper
							.newInstance(ClimateService.class),
					ClimateServiceName);
			return climateService;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	} */

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

