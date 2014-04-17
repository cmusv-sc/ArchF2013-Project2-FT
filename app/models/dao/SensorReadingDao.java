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

import models.Device;
import models.SensorReading;


public interface SensorReadingDao {
	
	public SensorReading searchReading(String sensorName, Long timeStamp);
	
	public SensorReading searchReading(String deviceUri, String sensorTypeName, Long timeStamp);
	
	public boolean addReading( String sensorName, Boolean isIndoor, long timeStamp, String value, Double longitude, Double latitude, Double altitude, String locationInterpreter);

	public List<SensorReading> searchReading(String sensorName, Long startTime, Long endTime);
	
	public List<SensorReading> searchReading(String deviceUri, String sensorTypeName, Long startTime, Long endTime);
	
	public List<SensorReading> lastReadingFromAllDevices(Long timeStamp);

	public List<SensorReading> latestReadingFromAllDevicesBySensorType(String sensorType);
	
	public List<SensorReading> latestReadingFromAllDevices(List<Device> devices);

}
