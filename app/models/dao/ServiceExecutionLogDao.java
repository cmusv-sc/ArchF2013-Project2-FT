package models.dao;

import java.util.List;

import models.ServiceExecutionLog;
import models.ServiceParamter;

public interface ServiceExecutionLogDao {
	public boolean addServiceExecutionLog(String serviceId, String userId, String purpose, 
			String serviceConfigurationId, String datasetLogId, String executionStartTime, 
				String executionEndTime);
	/*public boolean deleteServiceExecutionLog(String serviceExecutionLogId);
	public boolean updateServiceExecutionLog(String serviceExecutionLogId, String serviceId, 
			String userId, String purpose, String serviceConfigurationId, String datasetLogId, 
				String executionDate, String executionStartTime, String executionEndTime);
	public List<ServiceExecutionLog> getServiceExecutionLogs(String userId, 
						String executionStartTime, String executionEndTime);
	public ServiceExecutionLog getServiceExecutionLog(String serviceExecutionLogId);*/
    public List<ServiceExecutionLog> getAllServiceExecutionLogs();
    public List<ServiceExecutionLog> getServiceExecutionLogs(String userId,
                                                             String startTime, String endTime);
    public List<ServiceParamter> getAllServiceParameters();
    public boolean addServiceParameter(String serviceId, String parameterDateType, String parameterRange, String parameterNumeration,
                                                     String parameterRule, String parameterPurpose);
}
