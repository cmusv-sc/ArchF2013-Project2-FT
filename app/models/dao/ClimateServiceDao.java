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

public interface ClimateServiceDao {
	public boolean addClimateService(String climateServiceName, String purpose, String url, String scenario,
			String creatorId, String createTime, String versionNo, String rootServiceId);
	public boolean deleteClimateService(String climateServiceName);
	public boolean updateClimateService(String climateServiceName, String purpose, String url, String scenario,
			String creatorId, String createTime, String versionNo, String rootServiceId);
	public List<ClimateService> getAllClimateServices();
	public ClimateService getClimateService(String climateServiceName);
}
