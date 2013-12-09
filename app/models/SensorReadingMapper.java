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
package models;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class SensorReadingMapper implements RowMapper{

	@Override
	public OldSensorReading mapRow(ResultSet rs, int rowNum) throws SQLException {
		OldSensorReading sensorReading = new OldSensorReading();
		sensorReading.setDeviceId(rs.getString("deviceid"));
		sensorReading.setTimeStamp(rs.getLong("timestamp"));
		sensorReading.setSensorType(rs.getString("sensortype"));
		sensorReading.setValue(rs.getDouble("value"));
		return sensorReading;
	}
}
