package models;

public class ServiceExecutionLog {
	private String serviceExecutionLogId;
	private String serviceId;
	private String userId;
	private String purpose;
	private String serviceConfigurationId;
	private String datasetLogId;
	private String executionStartTime;
	private String executionEndTime;
	
	public String getServiceExecutionLogId() {
		return serviceExecutionLogId;
	}
	
	public void setServiceExecutionLogId(String serviceExecutionLogId) {
		this.serviceExecutionLogId = serviceExecutionLogId;
	}
	
	public String getServiceId() {
		return serviceId;
	}
	
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	
	public String getUserId() {
		return userId;
	}
	
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public String getPurpose() {
		return purpose;
	}
	
	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}
	
	public String getServiceConfigurationId() {
		return serviceConfigurationId;
	}
	
	public void setServiceConfigurationId(String serviceConfigurationId) {
		this.serviceConfigurationId = serviceConfigurationId;
	}
	
	public String getDatasetLogId() {
		return datasetLogId;
	}
	
	public void setDatasetLogId(String datasetLogId) {
		this.datasetLogId = datasetLogId;
	}
	
	public String getExecutionStartTime() {
		return executionStartTime;
	}
	
	public void setExecutionStartTime(String executionStartTime) {
		this.executionStartTime = executionStartTime;
	}
	
	public String getExecutionEndTime() {
		return executionEndTime;
	}
	
	public void setExecutionEndTime(String executionEndTime) {
		this.executionEndTime = executionEndTime;
	}
	
}
