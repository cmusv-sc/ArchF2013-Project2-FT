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

import models.OldSensorReading;


public interface OldSensorReadingDao {
	
	public OldSensorReading searchReading(String deviceId, Long timeStamp, String sensorType);
	
	public boolean addReading(String deviceId, Long timeStamp, String sensorType, Double value);

	public List<OldSensorReading> searchReading(String deviceId, Long startTime, Long endTime, String sensorType);
	
	public List<OldSensorReading> lastReadingFromAllDevices(Long timeStamp, String sensorType);

	public List<OldSensorReading> latestReadingFromAllDevices(String sensorType);

}
