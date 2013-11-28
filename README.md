<b>Released under a Dual Licensing / GPL 3.</b>
Sensor Service Platform APIs Version 1.0
========================================
Advisors: Jia Zhang, Bob Iannucci, Martin Griss, Steven Rosenberg, Anthony Rowe<br/>
Current contributors: Bo Liu, Lyman Cao, Chris Lee<br/>
Past Contributors: Yuan Ren, Mark Hennessy, Kaushik Gopal, Sean Xiao, Sumeet Kumar, David Pfeffer, Basmah Aljedia

EXECUTIVE SUMMARY
=================
On top of CMU SensorAndrew, the largest nation-wide campus sensor network, our Sensor Data and Service Platform (SDSP) 
aims to build a software service layer serving sensor data service discovery, reuse, composition, mashup, provisioning, and 
analysis. Another major objective of our project is to support SDSP-empowered innovative application design and 
development. Supported by a cloud computing enrironment with high-performance database, SDSP provides a platform 
to enable and facilitate a variety of research projects at CMUSV in the areas of mobile services, internet of things, 
cloud computing, big data analytics, software as a service, and social services.

Service URL:
------------
[http://einstein.sv.cmu.edu][1]


Overview:
---------
Currently we are providing APIs in 3 categores:

**Category 1: Post sensor readings**<br/>
   - [Post sensor reading data through a file](#3)<br/>
    
**Category 2: Query database for sensor readings**<br/>
   - [Get sensor reading at a time point by timestamp, for a sensor (specify by sensor type) in a device](#4)<br/>
   - [Get sensor reading at a time point by readable time, for a sensor (specify by sensor type) in a device](#20)<br/>
   - [Get sensor readings in a time frame by timestamp, for a sensor (specify by sensor type) in a device](#5)<br/>
   - [Get sensor readings in a time frame by readable time, for a sensor (specify by sensor type) in a device](#21)<br/>
   - [Get current sensor readings for a sensor type in all registered devices](#6)<br/>
   - [Get latest sensor readings for a sensor type in all registered devices](#7)

**Category 3: Query database for metadata**<br/>
   - [Get all devices registered](#1)<br/>
   - [Get all sensor types of a specific device](#2)<br/>
    
**Category 4: Manage metadata---under construction**<br/>
   - [Add a sensor type](#8)
   - [Add a sensor](#9)
   - [Add a device type](#10)
   - [Add a device](#11)
   - [Edit a sensor type](#12)
   - [Edit a sensor](#13)
   - [Edit a device type](#14)
   - [Edit a device](#15)
   - 
   - The following APIs will, at the current time, reserved for internal usage only.
   - [Delete a sensor type](#16)
   - [Delete a sensor](#17)
   - [Delete a device type](#18)
   - [Delete a device](#19)


Detailed Usages:
----------------
Note: all TimeStamps are in Unix epoch time format to millisecond. Conversion from readable timestamp format to Unix epoch timestamp can be found in http://www.epochconverter.com

1. <a name="1"></a>**GET ALL DEVICES**
    - **Purpose**: Query all registered devices' metadata.
    - **Method**: GET
    - **URL**: http://einstein.sv.cmu.edu/get_devices/<"result_format">
    - **Semantics**:
        - **uri**: User-defined identifier for a device. Each uri is an identifier unique to the corresponding device
        - **device_type**: Model of the device. A device is a container (i.e., physical device) object that comprises one or more sensors and is capable of transmitting their readings over a network to a Device Agent.
        - **device_agent**: A local server or proxy that manages a set of devices registered to it. Device agents can receive data from devices, convert data to another format (eg. JSON), and can transmit it to central server over a LAN or WAN. 
        - **device_location**: The location of the device that is transmitting sensor data. 
        - **result_format**: Either json or csv (2 formats are supported).
    - **Sample Usages**:
      - **Sample request in csv format**: http://einstein.sv.cmu.edu/get_devices/csv
      - **Sample result in csv format**: <br/>
          uri,device_type,device_agent,device_location <br/>
          10170202,Firefly_v3,SensorAndrew2,B23.216
      - **Sample request in json format**: http://einstein.sv.cmu.edu/get_devices/json
      - **Sample result in json format**: {"device_type":"Firefly_v3","device_location":"B23.216","device_agent":"SensorAndrew2","uri":"10170202"}


2. <a name="2"></a>**GET SENSOR TYPES OF A DEVICE**
    - **Purpose**: Query all sensor types contained in a specific device model (type).
    - **Method**: GET
    - **URL**: http://einstein.sv.cmu.edu/get_sensor_type/<"device_type">/<"result_format">
    - **Semantics**:        
        - **device_type**: Model of the device.
        - **sensor_type**: Type of a contained sensor, e.g., temperature, CO2 level etc. A device type could correspond to multiple sensor types if the device has multiple sensors.
        - **result_format**: Either json or csv.
    - **Sample Usages**:
      - **Sample csv request**: http://einstein.sv.cmu.edu/get_sensor_types/firefly_v3/csv
      - **Sample csv result**: <br/>
          sensor_types<br/>
          temp<br/>
          digital_temp
      - **Sample json request**: http://einstein.sv.cmu.edu/get_sensor_types/firefly_v3/json        
      - **Sample json result**: {"device_type":"Firefly_v3", "sensor_type":"temp,digital_temp,light,pressure,humidity,motion,audio_p2p,acc_x,acc_y,acc_z"}


3. <a name="3"></a>**PUBLISH SENSOR READINGS**
    - **Purpose**: Publish sensor readings to sensor data service platform.
    - **Method**: POST
    - **URL**: http://einstein.sv.cmu.edu/sensors
    - **Semantics**: As a POST method, the API cannot be directly executed through a web browser.  Instead, it may be executed through Rails, JQuery, Python, BASH, etc.
        - **device_id** (string): Unique device uri/id.
        - **timestamp** (int): Recording timestamp of the sensor reading in Unix epoch timestamp format. 
        - **sensor_type** (string): Type of the sensor, e.g., temperature, CO2 Levels, etc. It is user's responsibility to tag the correct sensor type to the sensor reading.
        - **sensor_value** (double): The value of the sensor reading. It is user's responsibility to calibrate the sensor readings before publishing.
    - **Sensor data format**: {"id": <"device_id">, "timestamp": <"timestamp">, <"sensor_type">: <"sensor_value">} 
        <br/> Note: more than one (sensor_type:sensor_value) pairs can be included in a json file.   
    - **Sample Usages**:
      - **Command Line Example**: 
          1. Prepare input sensor reading data in a json file (**please modify the timestamp to a different value**):
              - "sample_reading.json" file contains: {"id":"test", "timestamp": 1373566899000, "temp": 123}
          2. curl -H "Content-Type: application/json" -d @sample_reading.json "http://einstein.sv.cmu.edu/sensors"
      - **Result**: "saved" if the sensor readings have been successfully added to the database.

4. <a name="4"></a>**GET SENSOR READINGS OF A TYPE OF SENSOR IN A DEVICE AT A TIME**
    - **Purpose**: Query sensor readings for a specific type of sensor, in a particular device, at a specific time point.
    - **Method**: GET
    - **URL**: http://einstein.sv.cmu.edu/sensors/<"device_id">/<"timestamp">/<"sensor_type">/<"result_format">
    - **Semantics**: 
        - **device_id**: Unique uri/identifier of a device.
        - **timestamp**: Time of the readings to query.
        - **sensor_type**: Type of the sensor (e.g., temperature, CO2, etc.) to query.
        - **result_format**: Either json or csv.
    - **Sample Usages**: 
      - **Sample csv request**: http://einstein.sv.cmu.edu/sensors/10170102/1368568896000/temp/csv<br/> (note: "temp" represents the temperature sensor type)
      - **Sample csv result**: (device_id,timestamp,sensor_type,value) </br>10170102,1368568896000,temp,518.0
      - **Sample json request**: http://einstein.sv.cmu.edu/sensors/10170102/1368568896000/temp/json
      - **Sample json result**: {"timestamp":1368568896000,"sensor_type":"temp","value":518,"device_id":"10170102"}

5. <a name="5"></a>**GET SENSOR READINGS IN A TIME RANGE FOR A DEVICE**
    - **Purpose**: Query sensor readings for a specific type of sensor, in a particular device, for a specific time range. 
    - **Method**: GET
    - **URL**: http://einstein.sv.cmu.edu/sensors/<"device_id">/<"start_time">/<"end_time">/<"sensor_type">/<"result_format">
    - **Semantics**:
        - **device_id**: Unique uri/identifier of a device.
        - **start_time**: Start time to retrieve the sensor readings.
        - **end_time**: End time to retreive the sensor readings.
        - **sensor_type**: Type of the sensor (e.g., temperature, CO2, etc.) to retrieve its readings.
        - **result_format**: Either json or csv.
    - **Sample Usages**: 
      - **Sample csv request**: http://einstein.sv.cmu.edu/sensors/10170102/1368568896000/1368568996000/temp/csv
      - **Sample csv result**: (device_id,timestamp,sensor_type,value)<br/>
          10170102,1368568993000,temp,517.0 <br/>
          ... <br/>
          10170102,1368568896000,temp,518.0
      - **Sample json request**: http://einstein.sv.cmu.edu/sensors/10170102/1368568896000/1368568996000/temp/json
      - **Sample json result**: <br/>
          [{"timestamp":1368568993000,"sensor_type":"temp","value":517,"device_id":"10170102"},
          ... <br/>
          {"timestamp":1368568896000,"sensor_type":"temp","value":518,"device_id":"10170102"}]

6. <a name="6"></a>**GET CURRENT SENSOR READINGS AT A TIME POINT FOR A TYPE OF SENSOR IN ALL REGISTERED DEVICES**
    - **Purpose**: Query all sensor readings at a time point (within 60 seconds), of a specific sensor type contained in all registered devices.
    - **Method**: GET
    - **URL**: http://einstein.sv.cmu.edu/last_readings_from_all_devices/<"timestamp">/<"sensor_type">/<"result_format">
    - **Semantics**:
        - **timestamp**: Time to query the last readings of all sensors for all devices registered at the sensor data service platform.
        - **sensor_type**: Type of the sensor (e.g., temperature, CO2, etc.).
        - **result_format**: Either json or csv.
    - **Sample Usages**: 
      - **Sample csv request**: http://einstein.sv.cmu.edu/last_readings_from_all_devices/1368568896000/temp/csv
      - **Sample csv result**: (device_id,timestamp,sensor_type,value) </br>
          10170203,1368568896000,temp,513.0 <br/>
          ... <br/>
          10170204,1368568889000,temp,516.0
      - **Sample json request**: http://einstein.sv.cmu.edu/last_readings_from_all_devices/1368568896000/temp/json
      - **Sample json result**: <br/>
          [{"timestamp":1368568896000,"sensor_type":"temp","value":513,"device_id":"10170203"},
          ... <br/>
          {"timestamp":1368568889000,"sensor_type":"temp","value":516,"device_id":"10170204"}]


7. <a name="7"></a>**GET LATEST SENSOR READINGS AT A TIME POINT FOR A TYPE OF SENSOR IN ALL REGISTERED DEVICES**
    - **Purpose**: Query all latest sensor readings, of a specific sensor type contained in all devices.  If no reading for a sensor in the last 60 seconds, the latest stored reading of the corresponding sensor will be returned. 
    - **Method**: GET
    - **URL**: http://einstein.sv.cmu.edu/lastest_readings_from_all_devices/<"sensor_type">/<"result_format">
    - **Semantics**:
        - **sensor_type**: Type of the sensor (e.g., temperature, CO2, etc.).
        - **result_format**: Either json or csv.
        - Note: The difference between API#7 and API#6 (last_readings_from_all_devices) given the current timestamp is that, API#7 returns the last readings stored for each device even if it is more than 60 seconds old.
    - **Sample Usages**: 
      - **Sample csv request**: http://einstein.sv.cmu.edu/lastest_readings_from_all_devices/temp/csv     
      - **Sample csv result**: (device_id,timestamp,sensor_type,value) </br>
          10170203,1368568896000,temp,513.0 <br/>
          ... <br/>
          10170204,1368568889000,temp,515.0
      - **Sample json request**: http://einstein.sv.cmu.edu/lastest_readings_from_all_devices/temp/json
      - **Sample json result**: <br/>
        [{"timestamp":1368568896000,"sensor_type":"temp","value":513,"device_id":"10170203"},
        ... <br/>
        {"timestamp":1368568889000,"sensor_type":"temp","value":515,"device_id":"10170204"}]

8. <a name="8"></a>**ADD SENSOR TYPE**
    - **Purpose**: Add a new sensor type to sensor data service platform.
    - **Method**: POST
    - **URL**: http://einstein.sv.cmu.edu/add_sensor_type
    - **Semantics**: As a POST method, the API cannot be directly executed through a web browser.  Instead, it may be executed through Rails, JQuery, Python, BASH, etc.
        - **sensor_type** (string): Name of the sensor type.
        - **user_defined_fields** (string): User defined fields. 
    - **Sensor type metadata format**: {"sensor_type": <"sensor_type">, "user_defined_fields": <"user_defined_fields">}    
    - **Sample Usages**:
      - **Command Line Example**: 
          1. Prepare input sensor type metadata in a json file:
              - "sensor_type.json" file contains: {"sensor_type": "Humidity", "user_defined_fields": "For test"}
          2. curl -H "Content-Type: application/json" -d @sensor_type.json "http://einstein.sv.cmu.edu/add_sensor_type"
      - **Result**: "sensor type saved" if the sensor type metadata has been successfully added to the database.

9. <a name="9"></a>**ADD SENSOR**
    - **Purpose**: Add a new sensor to sensor data service platform.
    - **Method**: POST
    - **URL**: http://einstein.sv.cmu.edu/add_sensor
    - **Semantics**: As a POST method, the API cannot be directly executed through a web browser.  Instead, it may be executed through Rails, JQuery, Python, BASH, etc.
        - **print_name** (string): Name of the sensor.
        - **sensor_type** (string): Its sensor type.
        - **device_id** (string): The device ID it belongs to.
        - **user_defined_fields** (string): User defined fields. 
    - **Sensor metadata format**: {"print_name": <"print_name">, "sensor_type": <"sensor_type">, "device_id": <"device_id">, "user_defined_fields": <"user_defined_fields">}    
    - **Sample Usages**:
      - **Command Line Example**: 
          1. Prepare input sensor metadata in a json file:
              - "sensor.json" file contains: {"print_name": "test_sensor", "sensor_type": "Humidity", "device_id": "test_id", "user_defined_fields": "For test"}
          2. curl -H "Content-Type: application/json" -d @sensor.json "http://einstein.sv.cmu.edu/add_sensor"
      - **Result**: "sensor saved" if the sensor metadata have been successfully added to the database.

10. <a name="10"></a>**ADD DEVICE TYPE**
    - **Purpose**: Add a new device type to sensor data service platform.
    - **Method**: POST
    - **URL**: http://einstein.sv.cmu.edu/add_device_type
    - **Semantics**: As a POST method, the API cannot be directly executed through a web browser.  Instead, it may be executed through Rails, JQuery, Python, BASH, etc.
        - **device_type_name** (string): Name of the device type.
        - **manufacturer** (string): Name of the manufacturer.
        - **version** (string): Version of the device type.
        - **user_defined_fields** (string): User defined fields. 
    - **Sensor type metadata format**: {"device_type_name": <"device_type_name">, "manufacturer": <"manufacturer">, "version": <"version">, "user_defined_fields": <"user_defined_fields">}    
    - **Sample Usages**:
      - **Command Line Example**: 
          1. Prepare input device type metadata in a json file:
              - "device_type.json" file contains: {"device_type_name": "test_device_type", "manufacturer": "TI", "version": "1.0", "user_defined_fields": "For test"}
          2. curl -H "Content-Type: application/json" -d @sensor_type.json "http://einstein.sv.cmu.edu/add_device_type"
      - **Result**: "device type saved" if the device type metadata has been successfully added to the database.

11. <a name="11"></a>**ADD DEVICE**
    - **Purpose**: Add a new device to sensor data service platform.
    - **Method**: POST
    - **URL**: http://einstein.sv.cmu.edu/add_device
    - **Semantics**: As a POST method, the API cannot be directly executed through a web browser.  Instead, it may be executed through Rails, JQuery, Python, BASH, etc.
        - **device_type** (string): Name of the device type.
        - **device_agent** (string): Name of the device agent.
        - **device_id** (string): The device id (i.e., network address, uri, macaddress to date). This device_id will be needed as a reference in all consequent senarios.
        - **location_description** (string): Location.
        - **latitude** (string): Latitude.
        - **longitude** (string): Longitude.
        - **altitude** (string): Altitude.
        - **position_format_system** (string): Format of the position.
        - **user_defined_fields** (string): User defined fields. 
    - **Sensor metadata format**: {"device_type": <"device_type">, "device_agent": <"device_agent">, "network_address": <"network_address">, "location_description": <"location_description">, "latitude": <"latitude">, "longitude": <"longitude">, "altitude": <"altitude">, "position_format_system": <"position_format_system">, "user_defined_fields": <"user_defined_fields">}    
    - **Sample Usages**:
      - **Command Line Example**: 
          1. Prepare input device metadata in a json file:
              - "device.json" file contains: {"device_type": "test_device_type", "device_agent": "test_device_agent", "device_id": "test_network_address", "location_description": "test_location_description", "latitude": "test_latitude", "longitude": "test_longitude", "altitude": "test_altitude", "position_format_system": "test_position_format_system", "user_defined_fields": "For test"}
          2. curl -H "Content-Type: application/json" -d @device.json "http://einstein.sv.cmu.edu/add_device"
      - **Result**: "device saved" if the device metadata have been successfully added to the database.

12. <a name="20"></a>**GET SENSOR READINGS OF A TYPE OF SENSOR IN A DEVICE AT A TIME BY READABLE TIME**
    - **Purpose**: Query sensor readings for a specific type of sensor, in a particular device, at a specific time point.
    - **Method**: GET
    - **URL**: http://einstein.sv.cmu.edu/sensors/<"device_id">/<"time">/<"sensor_type">/<"result_format">?dateformat=ISO8601
    - **Semantics**: 
        - **device_id**: Unique uri/identifier of a device.
        - **time**: Time of the readings to query.
        - **sensor_type**: Type of the sensor (e.g., temperature, CO2, etc.) to query.
        - **result_format**: Either json or csv.
    - **Sample Usages**: 
      - **Sample csv request**: http://einstein.sv.cmu.edu/sensors/10170102/05-14-2013T15:01:36/temp/csv?dateformat=ISO8601<br/> (note: "temp" represents the temperature sensor type)
      - **Sample csv result**: (device_id,timestamp,sensor_type,value) </br>10170102,05-04-2013T12:00:00,temp,518.0
      - **Sample json request**: http://einstein.sv.cmu.edu/sensors/10170102/05-14-2013T15:01:36/temp/json?dateformat=ISO8601
      - **Sample json result**: {"time":05-04-2013T12:00:00,"sensor_type":"temp","value":518,"device_id":"10170102"}

13. <a name="21"></a>**GET SENSOR READINGS IN A TIME RANGE FOR A DEVICE**
    - **Purpose**: Query sensor readings for a specific type of sensor, in a particular device, for a specific readable time range. 
    - **Method**: GET
    - **URL**: http://einstein.sv.cmu.edu/sensors/<"device_id">/<"start_time">/<"end_time">/<"sensor_type">/<"result_format">?dateformat=ISO8601
    - **Semantics**:
        - **device_id**: Unique uri/identifier of a device.
        - **start_time**: Start readable time to retrieve the sensor readings.
        - **end_time**: End readable time to retreive the sensor readings.
        - **sensor_type**: Type of the sensor (e.g., temperature, CO2, etc.) to retrieve its readings.
        - **result_format**: Either json or csv.
    - **Sample Usages**: 
      - **Sample csv request**: http://einstein.sv.cmu.edu/sensors/10170102/05-04-2013T12:00:00/05-05-2013T12:00:00/temp/csv?dateformat=ISO8601
      - **Sample csv result**: (device_id,timestamp,sensor_type,value)<br/>
          10170102,05-04-2013T12:00:00,temp,517.0 <br/>
          ... <br/>
          10170102,05-05-2013T12:00:00,temp,518.0
      - **Sample json request**: http://einstein.sv.cmu.edu/sensors/10170102/05-04-2013T12:00:00/05-05-2013T12:00:00/temp/json?dateformat=ISO8601
      - **Sample json result**: <br/>
          [{"time":05-04-2013T12:00:00,"sensor_type":"temp","value":517,"device_id":"10170102"},
          ... <br/>
          {"time":05-05-2013T12:00:00,"sensor_type":"temp","value":518,"device_id":"10170102"}]

[1]: http://einstein.sv.cmu.edu/ "The Application Server running in the Smart Spaces Lab, CMUSV"

Examples:
----------------
1. Consume Rest API in Python
    - GET
    <pre>
      <code>
         import json, requests
         response = requests.get("http://einstein.sv.cmu.edu/get_devices/json")
         print(response.json())
      </code>
    </pre>
    - POST
    <pre>
      <code>
         import requests
         requests.post("http://einstein.sv.cmu.edu/sensors", data={}, headers={}, files={}, cookies=None, auth=None)
      </code>
    </pre>
    
2. Consume Rest API in Java
    - GET
   <pre>
      <code>
      import java.net.HttpURLConnection;
      import java.net.URL;
      public static String httpGet(String urlStr) throws IOException {
      		URL url = new URL(urlStr);
      		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      
      		if (conn.getResponseCode() != 200) {
      			throw new IOException(conn.getResponseMessage());
      		}
      
      		// Buffer the result into a string
      		BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
      		StringBuilder sb = new StringBuilder();
      		String line;
      		while ((line = rd.readLine()) != null) {
      			sb.append(line);
      		}
      		rd.close();
      
      		conn.disconnect();
      		return sb.toString();
   	}
      </code>
   </pre>
    - POST (Please download Gson Jar first http://code.google.com/p/google-gson/downloads/list)
   <pre>
      <code>
      import java.io.BufferedReader;
      import java.io.IOException;
      import java.io.InputStreamReader;
      import java.io.OutputStream;
      import java.io.OutputStreamWriter;
      import java.io.Writer;
      import java.net.HttpURLConnection;
      import java.net.URL;
      import com.google.gson.JsonObject;
      
      public class SensorReadingPostExample {
         	public static void main(String args[]) throws Exception {
         		String URLStr = "http://einstein.sv.cmu.edu/sensors";
         		java.util.Date date = new java.util.Date();
         		
         		JsonObject jo = new JsonObject();
         		//Sample data
         		jo.addProperty("timestamp", date.getTime()); //Long type
         		jo.addProperty("id", "my_test_device_id");   //String type
         		jo.addProperty("temp", 888);                 //Double type
         		   
         		httpPostSensorReading(URLStr, jo.toString());
         	}
         
         	public static String httpPostSensorReading(String urlStr, String jsonString) throws Exception {
         		URL url = new URL(urlStr);
         		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
         		conn.setRequestMethod("POST");
         		conn.setRequestProperty("Content-Type", "application/json");
         		conn.setRequestProperty("Accept", "application/json");
         		conn.setDoOutput(true);
         
         		// Create the form content
         		OutputStream out = conn.getOutputStream();
         		Writer writer = new OutputStreamWriter(out, "UTF-8");
         
         		writer.write(jsonString);
         
         		writer.close();
         		out.close();
         
         		if (conn.getResponseCode() != 200) {
         			throw new IOException(conn.getResponseMessage());
         		}
         
         		// Buffer the result into a string
         		BufferedReader rd = new BufferedReader(new InputStreamReader(
         				conn.getInputStream()));
         		StringBuilder sb = new StringBuilder();
         		String line;
         		while ((line = rd.readLine()) != null) {
         			sb.append(line);
         		}
         		rd.close();
         
         		conn.disconnect();
         		return sb.toString();
         	}
      
      }
      </code>
   </pre>
    
To do items:
============
   - Provide APIs that allow users to specify human time specification (time zone?).
      - key name of "timestamp"? in json 
      - which apis need readable time
   - How about when some fields are added or changed for some sensors?
   - Rethink the design of HANA tables, to leverage HANA indexing previlege while keeping records info.
   - Mobile sensor into the picture (location: room, GPS, configurable)?


