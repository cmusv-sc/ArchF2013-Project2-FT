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

public interface SensorTypeDao {
	public boolean addSensorType(String sensorTypeName, String manufacturer, String version, double maxValue, double minValue, String unit, String interpreter, String userDefinedFields, String sensorCategoryName);
	public boolean updateSensorType(String sensorTypeName, String userDefinedFields);
	public SensorType getSensorType(String sensorTypeName);
	public List<SensorType> getAllSensorTypes();
	public boolean deleteSensorType(String sensorTypeName);
}
