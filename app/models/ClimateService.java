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
package models;


public class ClimateService {
	protected String climateServiceName;
	private String purpose;
	private String url;
	private String scenario;
	private String creatorId;
	private String createTime;
	private String versionNo; 
	private String rootServiceId;

	public String getClimateServiceName() {
		return climateServiceName;
	}
	
	public void setClimateServiceName(String climateServiceName) {
		this.climateServiceName = climateServiceName;
	}

	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getScenario() {
		return scenario;
	}

	public void setScenario(String scenario) {
		this.scenario = scenario;
	}
	
	public String getCreatorId() {
		return creatorId;
	}
	
	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}
	
	public String getCreateTime() {
		return createTime;
	}
	
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
	public String getVersionNo() {
		return versionNo;
	}
	
	public void setVersionNo(String versionNo) {
		this.versionNo = versionNo;
	}
	
	public String getRootServiceId() {
		return rootServiceId;
	}
	
	public void setRootServiceId(String rootServiceId) {
		this.rootServiceId = rootServiceId;
	}

}
