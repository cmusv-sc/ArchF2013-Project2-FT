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
package controllers;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;

import models.ClimateService;
import models.ServiceExecutionLog;
import models.ServiceParamter;
import models.dao.ClimateServiceDao;
import models.dao.ServiceExecutionLogDao;

import org.codehaus.jackson.JsonNode;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import play.mvc.Controller;
import play.mvc.Result;

import com.google.gson.Gson;

public class ClimateServiceController extends Controller {
	private static ApplicationContext context;
	private static ClimateServiceDao climateServiceDao;
	private static ServiceExecutionLogDao serviceExecutionLogDao;
	
	private static boolean checkDao() {
		try{
			if (context == null) {
				context = new ClassPathXmlApplicationContext("application-context.xml");
			}
			if (climateServiceDao == null) {
				climateServiceDao = (ClimateServiceDao) context.getBean("climateServiceDaoImplementation");
				System.out.println("Step1");
			}
			if (serviceExecutionLogDao == null) {
				serviceExecutionLogDao = (ServiceExecutionLogDao) context.getBean("serviceExecutionLogDaoImplementation");
				System.out.println("Step2" + serviceExecutionLogDao);
				
			}
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public static Result addClimateService() {	
		response().setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, "*");
		
		JsonNode json = request().body().asJson();
		if(json == null) {
			System.out.println("Climate service not saved, expecting Json data");
			return badRequest("Climate service not saved, expecting Json data");
		}
		
		if(!checkDao()) {
			System.out.println("Climate service not saved, database conf file not found");
			return internalServerError("Climate service not saved, database conf file not found");
		}	
		
		// Parse JSON File 
		String climateServiceName = json.findPath("climateServiceName").getTextValue();
		String purpose = json.findPath("purpose").getTextValue();
		String url = json.findPath("url").getTextValue();
		String scenario = json.findPath("scenario").getTextValue();
		String creatorId = json.findPath("creatorId").getTextValue();
		String createTime = json.findPath("createTime").getTextValue();
		String versionNo = json.findPath("versionNo").getTextValue();
		String rootServiceId = json.findPath("rootServiceId").getTextValue();
		
		if(climateServiceName == null || climateServiceName.length() == 0) {
			System.out.println("Climate service not saved, null name");
			return ok("Climate service not saved, null name");
		}
		
		boolean result = climateServiceDao.addClimateService(climateServiceName, purpose, url, scenario, 
				creatorId, createTime, versionNo, rootServiceId);

//		TODO API document says it should return a HTTP 201 here. However, Play does not have a class for it
//		Is HTTP 200 (implemented by Result.Ok) fine?
		if(result) {
			System.out.println("Climate service saved: " + climateServiceName);
			return created("Climate service saved: " + climateServiceName);
		} else {
			System.out.println("Climate service not saved: " + climateServiceName);
			return badRequest("Climate service not saved: " + climateServiceName);
		}
	}
	
	public static Result updateClimateService() {
		response().setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, "*");
		
		JsonNode json = request().body().asJson();
		if(json == null) {
			System.out.println("Climate service not saved, expecting Json data");
			return badRequest("Climate service not saved, expecting Json data");
		}
		
		if(!checkDao()) {
			System.out.println("Climate service not updated, database conf file not found");
			return internalServerError("Climate service not updated, database conf file not found");
		}

		//Parse JSON File 
		String climateServiceName = json.findPath("climateServiceName").getTextValue();
		String purpose = json.findPath("purpose").getTextValue();
		String url = json.findPath("url").getTextValue();
		String scenario = json.findPath("scenario").getTextValue();
		String creatorId = json.findPath("creatorId").getTextValue();
		String createTime = json.findPath("createTime").getTextValue();
		String versionNo = json.findPath("versionNo").getTextValue();
		String rootServiceId = json.findPath("rootServiceId").getTextValue();
		
		if(climateServiceName == null || climateServiceName.length() == 0) {
			System.out.println("Climate service not updated, null climateServiceName");
			return ok("Climate service not updated: null climateServiceName");
		} else if(purpose == null || purpose.length() == 0) {
			System.out.println("Climate service not updated, null purpose: " + climateServiceName);
			return ok("Climate service not updated: null purpose: " + climateServiceName);
		} else if(url == null || url.length() == 0) {
			System.out.println("Climate service not updated, null url: " + climateServiceName);
			return ok("Climate service not updated: null url: " + climateServiceName);
		} else if(scenario == null || scenario.length() == 0) {
			System.out.println("Climate service not updated, null scenario: " + climateServiceName);
			return ok("Climate service not updated: null scenario: " + climateServiceName);
		} else if(creatorId == null || creatorId.length() == 0) {
			System.out.println("Climate service not updated, null creatorId: " + climateServiceName);
			return ok("Climate service not updated: null creatorId: " + climateServiceName);
		} else if(versionNo == null || versionNo.length() == 0) {
			System.out.println("Climate service not updated, null versionNo: " + climateServiceName);
			return ok("Climate service not updated: null versionNo: " + climateServiceName);
		} else if(rootServiceId == null || rootServiceId.length() == 0) {
			System.out.println("Climate service not updated, null rootServiceId: " + climateServiceName);
			return ok("Climate service not updated: null rootServiceId: " + climateServiceName);
		}
		
		//Return error message if the ClimateService is not found 
		if(climateServiceDao.getClimateService(climateServiceName) == null){
			System.out.println("Climate service not updated, climate service not found: " + climateServiceName);
			return ok("Climate service not updated, Climate service not found: " + climateServiceName); 
		}
		
		boolean result = climateServiceDao.updateClimateService(climateServiceName, purpose, url, scenario, 
				creatorId, createTime, versionNo, rootServiceId);

		if(result) {
			System.out.println("Climate service updated: " + climateServiceName);
			return ok("Climate service updated: " + climateServiceName);
		} else {
			System.out.println("Climate service not updated: " + climateServiceName);
			return ok("Climate service not updated: " + climateServiceName);
		}
	}
	
	public static Result getClimateService(String climateServiceName, String format) {
		response().setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, "*");
		
		if(climateServiceName == null || climateServiceName.length() == 0) {
			System.out.println("Climate service not found, null climateServiceName");
			return ok("Climate service not found, null climateServiceName");
		}
		
		if(!checkDao()) {
			System.out.println("Climate service not found, database conf file not found");
			return internalServerError("Climate service not found, database conf file not found");
		}
		
		
		ClimateService climateService = climateServiceDao.getClimateService(climateServiceName);
		if(climateService == null){
			System.out.println("Climate service not found: " + climateServiceName);
			return notFound("Climate service not found: " + climateServiceName);
		}
		String ret = new String();
		if (format.equals("json")){
			ret = new Gson().toJson(climateService);
		} else {
			ret = toCsv(Arrays.asList(climateService));
		}
		return ok(ret);
	}
	
	public static Result addServiceExecutionLog(String serviceId, String userId, String purpose, String serviceConfigurationId, String datasetLogId, String executionStartTime, String executionEndTime) {
		if(!checkDao()) {
			System.out.println("Climate service not saved, database conf file not found");
			return internalServerError("Climate service not saved, database conf file not found");
		}	
		
		if(serviceId == null || serviceId.length() == 0) {
			System.out.println("Service Execution Log not saved, null serviceId");
			return ok("Service Execution Log not saved, null serviceId");
		}
		
		boolean result = serviceExecutionLogDao.addServiceExecutionLog(serviceId, userId, purpose, 
			serviceConfigurationId, datasetLogId, executionStartTime, executionEndTime);

//		TODO API document says it should return a HTTP 201 here. However, Play does not have a class for it
//		Is HTTP 200 (implemented by Result.Ok) fine?
		if(result) {
			System.out.println("Climate service saved: " + serviceId);
			return created("Climate service saved: " + serviceId);
		} else {
			System.out.println("Climate service not saved: " + serviceId);
			return badRequest("Climate service not saved: " + serviceId);
		}
	}

    public static Result addServiceParameter(){
        response().setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, "*");

        JsonNode json = request().body().asJson();
        if(json == null) {
            System.out.println("Service execution log not saved, expecting Json data");
            return badRequest("Service execution log not saved, expecting Json data");
        }

        if(!checkDao()) {
            System.out.println("Service execution log not saved, database conf file not found");
            return internalServerError("Service execution log not saved, database conf file not found");
        }

        // Parse JSON File
//        String serviceExecutionLogId = json.findPath("serviceExecutionLogId").getTextValue();
        String serviceId = json.findPath("serviceId").getTextValue();
        String parameterDateType = json.findPath("parameterdatatype").getTextValue();
        String parameterRange = json.findPath("parameterrange").getTextValue();
        String parameterNumeration = json.findPath("parameterenumeration").getTextValue();
        String parameterRule = json.findPath("parameterrule").getTextValue();
        String parameterPurpose = json.findPath("parameterpurpose").getTextValue();
        //String executionEndTime = json.findPath("executionEndTime").getTextValue();

        if(serviceId == null || serviceId.length() == 0) {
            System.out.println("Service ID not saved, null name");
            return ok("Service ID not saved, null name");
        }

        boolean result = serviceExecutionLogDao.addServiceParameter(serviceId, parameterDateType, parameterRange, parameterNumeration,
                parameterRule, parameterPurpose);

//		TODO API document says it should return a HTTP 201 here. However, Play does not have a class for it
//		Is HTTP 200 (implemented by Result.Ok) fine?
        if(result) {
            System.out.println("Execution of service saved: " + serviceId);
            return created("Execution of service saved: " + serviceId);
        } else {
            System.out.println("Execution of service not saved: " + serviceId);
            return badRequest("Execution of service not saved: " + serviceId);
        }
    }

    public static Result addServiceExecutionLogUsingPost(){
        response().setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, "*");

        JsonNode json = request().body().asJson();
        if(json == null) {
            System.out.println("Service execution log not saved, expecting Json data");
            return badRequest("Service execution log not saved, expecting Json data");
        }

        if(!checkDao()) {
            System.out.println("Service execution log not saved, database conf file not found");
            return internalServerError("Service execution log not saved, database conf file not found");
        }

        // Parse JSON File
//        String serviceExecutionLogId = json.findPath("serviceExecutionLogId").getTextValue();
        String serviceId = json.findPath("serviceId").getTextValue();
        String userId = json.findPath("userId").getTextValue();
        String purpose = json.findPath("purpose").getTextValue();
        String serviceConfigurationId = json.findPath("serviceConfigurationId").getTextValue();
        String datasetLogId = json.findPath("datasetLogId").getTextValue();
        String executionStartTime = json.findPath("executionStartTime").getTextValue();
        String executionEndTime = json.findPath("executionEndTime").getTextValue();

        if(serviceId == null || serviceId.length() == 0) {
            System.out.println("Service ID not saved, null name");
            return ok("Service ID not saved, null name");
        }

        boolean result = serviceExecutionLogDao.addServiceExecutionLog(serviceId, userId, purpose, serviceConfigurationId,
                datasetLogId, executionStartTime, executionEndTime);

//		TODO API document says it should return a HTTP 201 here. However, Play does not have a class for it
//		Is HTTP 200 (implemented by Result.Ok) fine?
        if(result) {
            System.out.println("Execution of service saved: " + serviceId);
            return created("Execution of service saved: " + serviceId);
        } else {
            System.out.println("Execution of service not saved: " + serviceId);
            return badRequest("Execution of service not saved: " + serviceId);
        }
    }
	
public static Result getServiceExecutionLogs(String userId, String startTime, String endTime, String format) {
    response().setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, "*");

    if(!checkDao()) {
        System.out.println("Search failed, database conf file not found");
        return internalServerError("Search failed, database conf file not found");
    }

    if(userId == null || userId.length() == 0) {
        System.out.println("User ID not saved, null name");
        return ok("User ID not saved, null name");
    }

    if(startTime == null || endTime == null) {
        System.out.println("Time not specified");
        return ok("Time not specified");
    }
    // TODO May handle time format from the frontend here

    // Currently the startTime/endTime is a number String
	List<ServiceExecutionLog> serviceExecutionLogs =serviceExecutionLogDao.getServiceExecutionLogs(userId, startTime, endTime);
		
	if(serviceExecutionLogs == null || serviceExecutionLogs.isEmpty()){
		System.out.println("No Service Execution Log found");
		return notFound("No Service Execution Log found");
	}
		
		String ret = new String();
		if (format.equals("json")){
			ret = new Gson().toJson(serviceExecutionLogs);
		} else {
			ret = executionLogToCsv(serviceExecutionLogs);
		}
		return ok(ret);
	}


    public static Result getAllServiceExecutionLogs(String format) {
        response().setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, "*");

        if(!checkDao()) {
            System.out.println("Climate service not found, database conf file not found");
            return internalServerError("Climate service not found, database conf file not found");
        }

        List<ServiceExecutionLog> executionLogs = serviceExecutionLogDao.getAllServiceExecutionLogs();

        if(executionLogs == null || executionLogs.isEmpty()){
            System.out.println("No Climate service found");
            return notFound("No Climate service found");
        }

        String ret = new String();
        if (format.equals("json")){
            ret = new Gson().toJson(executionLogs);
        } else {
            ret = executionLogToCsv(executionLogs);
        }
        return ok(ret);
    }

    public static Result getAllServiceParameters(String format) {
        response().setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, "*");

        if(!checkDao()) {
            System.out.println("Climate service not found, database conf file not found");
            return internalServerError("Climate service not found, database conf file not found");
        }

        List<ServiceParamter> parameters = serviceExecutionLogDao.getAllServiceParameters();

        if(parameters == null || parameters.isEmpty()){
            System.out.println("No Climate service parameter found");
            return notFound("No Climate service parameter found");
        }

        String ret = new String();
        if (format.equals("json")){
            ret = new Gson().toJson(parameters);
        } else {
            ret = parameterToCsv(parameters);
        }
        return ok(ret);
    }
	
	public static Result getAllClimateServices(String format) {
		response().setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, "*");

		if(!checkDao()) {
			System.out.println("Climate service not found, database conf file not found");
			return internalServerError("Climate service not found, database conf file not found");
		}	
		
		List<ClimateService> climateServices = climateServiceDao.getAllClimateServices();
		
		if(climateServices == null || climateServices.isEmpty()){
			System.out.println("No Climate service found");
			return notFound("No Climate service found");
		}
		
		String ret = new String();
		if (format.equals("json")){
			ret = new Gson().toJson(climateServices);
		} else {
			ret = toCsv(climateServices);
		}
		return ok(ret);
	}
	
	public static Result deleteClimateService(String climateServiceName){
		response().setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, "*");
		
		if(climateServiceName == null || climateServiceName.length() == 0){
			System.out.println("Climate service not deleted, null climateServiceName");
			return ok("Climate service not deleted, null climateServiceName");
		}
		
		if(!checkDao()){
			System.out.println("Climate service not deleted, database conf file not found");
			return internalServerError("Climate service not deleted, database conf file not found");
		}
		
		//Return error message if the ClimateService is not found 
		if(climateServiceDao.getClimateService(climateServiceName) == null) {
			System.out.println("Climate service not deleted, Climate service not found: " + climateServiceName);
			return ok("Climate service not deleted, Climate service not found: " + climateServiceName); 
		}
		
		boolean result = climateServiceDao.deleteClimateService(climateServiceName);
		
		if(result) {
			System.out.println("Climate service is deleted: " + climateServiceName);
			return ok("Climate service is deleted: " + climateServiceName);
		}else {
			System.out.println("Climate service is not deleted: " + climateServiceName);
			return ok("Climate service is not deleted: " + climateServiceName);
		}
	}
	
	private static String toCsv(List<ClimateService> climateServices) {
		StringWriter sw = new StringWriter();
		CellProcessor[] processors = new CellProcessor[] {
				new Optional(),	new Optional(),	new Optional(), new Optional(),	new Optional(),	new Optional(),
					new Optional(),	new Optional()};
		ICsvBeanWriter writer = new CsvBeanWriter(sw, CsvPreference.STANDARD_PREFERENCE);
		
		try {
			final String[] header = new String[] {"climateServiceName", "purpose", "url", 
					"scenario", "creatorId", "createTime", "versionNo", "rootServiceId"};
			writer.writeHeader(header);
			for(ClimateService climateService : climateServices){
				writer.write(climateService, header, processors);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sw.getBuffer().toString();
	}

    private static String executionLogToCsv(List<ServiceExecutionLog> executionLogs) {
        StringWriter sw = new StringWriter();
        CellProcessor[] processors = new CellProcessor[] {
                new Optional(),	new Optional(),	new Optional(), new Optional(),	new Optional(),	new Optional(),
                new Optional(),	new Optional()};
        ICsvBeanWriter writer = new CsvBeanWriter(sw, CsvPreference.STANDARD_PREFERENCE);

        try {
            final String[] header = new String[] {"serviceExecutionLogId", "serviceId", "userId",
                    "purpose", "serviceConfigurationId", "datasetLogId", "executionStartTime", "executionEndTime"};
            writer.writeHeader(header);
            for(ServiceExecutionLog log : executionLogs){
                writer.write(log, header, processors);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sw.getBuffer().toString();
    }

    private static String parameterToCsv(List<ServiceParamter> parameters){
        StringWriter sw = new StringWriter();
        CellProcessor[] processors = new CellProcessor[] {
                new Optional(),	new Optional(),	new Optional(), new Optional(),	new Optional(),	new Optional(),
                new Optional()};
        ICsvBeanWriter writer = new CsvBeanWriter(sw, CsvPreference.STANDARD_PREFERENCE);

        try {
            final String[] header = new String[] {"parameterId", "serviceId", "parameterDataType",
                    "parameterRange", "parameterEnumeration", "parameterRule", "parameterPurpose"};
            writer.writeHeader(header);
            for(ServiceParamter log : parameters){
                writer.write(log, header, processors);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sw.getBuffer().toString();
    }
}
