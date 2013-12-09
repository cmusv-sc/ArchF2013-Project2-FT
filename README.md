<b>Released under a Dual Licensing / GPL 3.</b>
Sensor Service Platform APIs Version 1.2
========================================
Advisors: Jia Zhang, Bob Iannucci, Martin Griss, Steven Rosenberg, Anthony Rowe<br/>
Current contributors: Bo Liu, Ching Lun Lin, Chris Lee, David Lee, Lyman Cao, <br/>
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
[http://einstein.sv.cmu.edu:9000][1]


Overview:
---------
Currently we are providing APIs in 3 categores:

**Category 1: Post sensor readings---under construction**<br/>
   - [Post sensor reading data through a file](#1)<br/>
    
**Category 2: Query database for sensor readings---under construction**<br/>
   - [Get sensor reading from a sensor(specified by sensorName) at a timestamp](#4)<br/>
   - [Get sensor reading from a sensor(specified by sensorName) among a timestamp range](#5)<br/>
   - [Get current sensor readings for a sensor type in all registered devices](#6)<br/>
   - [Get latest sensor readings for a sensor type in all registered devices](#7)
    
**Category 3: Manage metadata---under construction**<br/>
   - [Add a sensor category](#22)
   - [Add a sensor type](#8)
   - [Add a sensor](#9)
   - [Add a device type](#10)
   - [Add a device](#11)
   - [Edit a sensor category](#23)
   - [Edit a sensor type](#12)
   - [Edit a sensor](#13)
   - [Edit a device type](#14)
   - [Edit a device](#15)
   - [Delete a sensor category](#24)
   - [Delete a sensor type](#16)
   - [Delete a sensor](#17)
   - [Delete a device type](#18)
   - [Delete a device](#19)
   - [Get all sensor categories](#31)
   - [Get a specific sensor category](#32)
   - [Get all sensor types](#33)
   - [Get a specific sensor type](#34)
   - [Get all sensors](#35)
   - [Get a specific sensor](#36)
   - [Get all device types](#37)
   - [Get a specific device type](#38)
   - [Get all devices](#39)
   - [Get a specific device](#40)


Detailed Usages:
----------------
Note: all TimeStamps are in Unix epoch time format to millisecond. Conversion from readable timestamp format to Unix epoch timestamp can be found in http://www.epochconverter.com

1. <a name="1"></a>**PUBLISH SENSOR READINGS**
    - **Purpose**: Publish sensor readings to sensor data service platform.
    - **Method**: POST
    - **URL**: http://einstein.sv.cmu.edu:9000/sensorReading
    - **Semantics**: As a POST method, the API cannot be directly executed through a web browser.  Instead, it may be executed through Rails, JQuery, Python, BASH, etc.
        - **sensorName** (string, not null): Unique sensor name.
        - **timestamp** (int, not null): Recording timestamp of the sensor reading in Unix epoch timestamp format. 
        - **value** (string, not null): The value of the sensor reading. It is user's responsibility to calibrate the sensor readings before publishing.
        - **isIndoor** (boolean, optional): Sent from indoor or not.
        - **longitude** (double, optional): Longitude of the sensor reading.
        - **latitude** (double, optional): Longitude of the sensor reading.
        - **altitude** (double, optional): Longitude of the sensor reading.
        - **locationInterpreter** (string, optional): Interpreter information of location.
    - **Sample Usages**:
      - **Command Line Example**: 
          1. Prepare input sensor reading data in a json file (**please modify the timestamp to a different value**):
              - "sampleReading.json" file contains: [{"sensorName1": "sensorName", "timestamp": 1368568896000, "value": "16", "isIndoor":true, "longitude":10, "latitude":10, "altitude", 10, "locationInterpreter":"GPS"}, {"sensorName": "sensorName2", "timestamp": 1368568896000, "value": "17", "isIndoor":true, "longitude":10, "latitude":10, "altitude", 10, "locationInterpreter":"GPS"}]
          2. curl -H "Content-Type: application/json" -d @sampleReading.json "http://einstein.sv.cmu.edu:9000/sensorReading"
      - **Result**: HTTP 201 if the sensor readings have been successfully posted.
      
2. <a name="2"></a>**GET ALL DEVICES**
    - **Purpose**: Query all registered devices' metadata.
    - **Method**: GET
    - **URL**: http://einstein.sv.cmu.edu:9000/get_devices/<"result_format">
    - **Semantics**:
        - **uri**: User-defined identifier for a device. Each uri is an identifier unique to the corresponding device
        - **device_type**: Model of the device. A device is a container (i.e., physical device) object that comprises one or more sensors and is capable of transmitting their readings over a network to a Device Agent.
        - **device_agent**: A local server or proxy that manages a set of devices registered to it. Device agents can receive data from devices, convert data to another format (eg. JSON), and can transmit it to central server over a LAN or WAN. 
        - **device_location**: The location of the device that is transmitting sensor data. 
        - **result_format**: Either json or csv (2 formats are supported).
    - **Sample Usages**:
      - **Sample request in csv format**: http://einstein.sv.cmu.edu:9000/get_devices/csv
      - **Sample result in csv format**: <br/>
          uri,device_type,device_agent,device_location <br/>
          10170202,Firefly_v3,SensorAndrew2,B23.216
      - **Sample request in json format**: http://einstein.sv.cmu.edu:9000/get_devices/json
      - **Sample result in json format**: {"device_type":"Firefly_v3","device_location":"B23.216","device_agent":"SensorAndrew2","uri":"10170202"}


3. <a name="3"></a>**GET SENSOR TYPES OF A DEVICE**
    - **Purpose**: Query all sensor types contained in a specific device model (type).
    - **Method**: GET
    - **URL**: http://einstein.sv.cmu.edu:9000/get_sensor_type/<"device_type">/<"result_format">
    - **Semantics**:        
        - **device_type**: Model of the device.
        - **sensor_type**: Type of a contained sensor, e.g., temperature, CO2 level etc. A device type could correspond to multiple sensor types if the device has multiple sensors.
        - **result_format**: Either json or csv.
    - **Sample Usages**:
      - **Sample csv request**: http://einstein.sv.cmu.edu:9000/get_sensor_types/firefly_v3/csv
      - **Sample csv result**: <br/>
          sensor_types<br/>
          temp<br/>
          digital_temp
      - **Sample json request**: http://einstein.sv.cmu.edu:9000/get_sensor_types/firefly_v3/json        
      - **Sample json result**: {"device_type":"Firefly_v3", "sensor_type":"temp,digital_temp,light,pressure,humidity,motion,audio_p2p,acc_x,acc_y,acc_z"}


4. <a name="4"></a>**GET SENSOR READING FROM A SENSOR(SPECIFIED BY SENSORNAME) AT A TIMESTAMP**
    - **Purpose**: Query sensor reading for a specific sensor at a specific time point.
    - **Method**: GET
    - **URL**: http://einstein.sv.cmu.edu:9000/sensorReading/<"sensorName">/<"timestamp">/<"resultFormat">
    - **Semantics**: 
        - **sensorName**: Unique sensor name.
        - **timestamp**: Time of the readings to query.
        - **resultFormat**: Either JSON or CSV.
    - **Sample Usages**: 
      - **Sample csv request**: http://einstein.sv.cmu.edu:9000/sensorReading/sensor1/1368568896000/csv<br/>
      - **Sample csv result**: (sensorName,timestamp,value) </br>sensor1,1368568896000,518.0
      - **Sample json request**: http://einstein.sv.cmu.edu:9000/sensorReading/sensor1/1368568896000/json
      - **Sample json result**: {"timestamp":1368568896000,"sensorName":"sensor1","value":518}

5. <a name="5"></a>**GET SENSOR READING FROM A SENSOR(SPECIFIED BY SENSORNAME) AMONG A TIMESTAMP RANGE**
    - **Purpose**: Query sensor readings for a specific sensor among a specific time range. 
    - **Method**: GET
    - **URL**: http://einstein.sv.cmu.edu:9000/sensorReading/<"sensorName">/<"startTime">/<"endTime">/<"resultFormat">
    - **Semantics**:
        - **sensorName**: Unique sensor name.
        - **startTime**: Start time of the readings to query.
        - **endTime**: End time of the readings to query.
        - **resultFormat**: Either JSON or CSV.
    - **Sample Usages**: 
      - **Sample csv request**: http://einstein.sv.cmu.edu:9000/sensorReading/sensor1/1368568896000/1368568996000/csv
      - **Sample csv result**: (sensorName,timestamp,value)<br/>
          sensor1,1368568993000,517.0 <br/>
          ... <br/>
          sensor1,1368568896000,518.0
      - **Sample json request**: http://einstein.sv.cmu.edu:9000/sensorReading/sensor1/1368568896000/1368568996000/json
      - **Sample json result**: <br/>
          [{"timestamp":1368568993000,"value":517,"sensorName":"sensor1"},
          ... <br/>
          {"timestamp":1368568896000,"value": 520,"sensorName":"sensor1"}]

6. <a name="6"></a>**GET CURRENT SENSOR READINGS AT A TIME POINT FOR A TYPE OF SENSOR IN ALL REGISTERED DEVICES**
    - **Purpose**: Query all sensor readings at a time point (within 60 seconds), of a specific sensor type contained in all registered devices.
    - **Method**: GET
    - **URL**: http://einstein.sv.cmu.edu/lastReadingsFromAllDevices/<"timestamp">/<"sensorType">/<"resultFormat">
    - **Semantics**:
        - **timestamp**: Time to query the last readings of all sensors for all devices registered at the sensor data service platform.
        - **sensorType**: Type of the sensor (e.g., temperature, CO2, etc.).
        - **resultFormat**: Either JSON or CSV.
    - **Sample Usages**: 
      - **Sample csv request**: http://einstein.sv.cmu.edu/lastReadingsFromAllDevices/1368568896000/temp/csv
      - **Sample csv result**: (deviceUri,timestamp,sensorType,value) </br>
          device1,1368568896000,temp,513.0 <br/>
          ... <br/>
          device2,1368568889000,temp,516.0
      - **Sample json request**: http://einstein.sv.cmu.edu/lastReadingsFromAllDevices/1368568896000/temp/json
      - **Sample json result**: <br/>
          [{"timestamp":1368568896000,"sensorType":"temp","value":513,"deviceName":"device1"},
          ... <br/>
          {"timestamp":1368568889000,"sensorType":"temp","value":516,"deviceName":"device2"}]


7. <a name="7"></a>**GET LATEST SENSOR READINGS AT A TIME POINT FOR A TYPE OF SENSOR IN ALL REGISTERED DEVICES**
    - **Purpose**: Query all latest sensor readings, of a specific sensor type contained in all devices.  If no reading for a sensor in the last 60 seconds, the latest stored reading of the corresponding sensor will be returned. 
    - **Method**: GET
    - **URL**: http://einstein.sv.cmu.edu/lastestReadingsFromAllDevices/<"sensorType">/<"resultFormat">
    - **Semantics**:
        - **sensorType**: Type of the sensor (e.g., temperature, CO2, etc.).
        - **resultFormat**: Either json or csv.
        - Note: The difference between API#7 and API#6 (lastReadingsFromAllDevices) given the current timestamp is that, API#7 returns the last readings stored for each device even if it is more than 60 seconds old.
    - **Sample Usages**: 
      - **Sample csv request**: http://einstein.sv.cmu.edu/lastestReadingsFromAllDevices/temp/csv     
      - **Sample csv result**: (device_id,timestamp,sensorType,value) </br>
          10170203,1368568896000,temp,513.0 <br/>
          ... <br/>
          10170204,1368568889000,temp,515.0
      - **Sample json request**: http://einstein.sv.cmu.edu/lastestReadingsFromAllDevices/temp/json
      - **Sample json result**: <br/>
        [{"timestamp":1368568896000,"sensorType":"temp","value":513,"deviceId":"10170203"},
        ... <br/>
        {"timestamp":1368568889000,"sensorType":"temp","value":515,"deviceId":"10170204"}]

8. <a name="8"></a>**ADD SENSOR TYPE**
    - **Purpose**: Add a new sensor type to sensor data service platform.
    - **Method**: POST
    - **URL**: http://einstein.sv.cmu.edu/addSensorType
    - **Semantics**: As a POST method, the API cannot be directly executed through a web browser.  Instead, it may be executed through Rails, JQuery, Python, BASH, etc.
        - **sensorTypeName** (string, not null): Name of the sensor type
        - **manufacturer** (string, optional): Name of the manufacturerof this sensor type
        - **version** (string, optional): Version of the sensor type
        - **maximumValue** (double, optional): Maximum value of the sensor reading under this sensor type
        - **minimumValue** (double, optional): Minimum value of the sensor reading under this sensor type
        - **unit** (string, optional): Unit of the sensor reading under this sensor type
        - **interpreter** (string, optional): The interpreter used to parse the sensor reading under this sensor type
        - **sensorTypeUserDefinedFields** (string): User defined fields
        - **sensorCategoryName** (string, not null): The category this sensor type belongs to
    - **Sample Usages**:
      - **Command Line Example**: 
          1. Prepare input sensor type metadata in a json file:
              - "sensorType.json" file contains: {"sensorTypeName": "Humidity", "manufacturer": "Motorola", "version": "1.0", "maximumValue": 100, "minimumValue": 0, "unit": "Percentage", "interpreter": "MyInterpreter", "sensorTypeUserDefinedFields": "Testing only", "sensorCategoryName": "Environment"}
          2. curl -H "Content-Type: application/json" -d @sensorType.json "http://einstein.sv.cmu.edu/addSensorType"
      - **Result**: HTTP 201 if the sensor type metadata has been successfully added to the database

9. <a name="9"></a>**ADD SENSOR**
    - **Purpose**: Add a new sensor to sensor data service platform.
    - **Method**: POST
    - **URL**: http://einstein.sv.cmu.edu/addSensor
    - **Semantics**: As a POST method, the API cannot be directly executed through a web browser.  Instead, it may be executed through Rails, JQuery, Python, BASH, etc.
        - **sensorName** (string, not null): Name of the sensor
        - **sensorTypeName** (string, not null): Name of its sensor type
        - **deviceUri** (string, not null): The device URI it belongs to
        - **sensorUserDefinedFields** (string, optional): User defined fields.
    - **Sample Usages**:
      - **Command Line Example**: 
          1. Prepare input sensor metadata in a json file:
              - "sensor.json" file contains: {"sensorName": "TestSensor", "sensorTypeName": "Humidity", "deviceUri": "www.testsensor.com", "sensorUserDefinedFields": "Test only"}
          2. curl -H "Content-Type: application/json" -d @sensor.json "http://einstein.sv.cmu.edu/addSensor"
      - **Result**: HTTP 201 if the sensor metadata have been successfully added to the database

10. <a name="10"></a>**ADD DEVICE TYPE**
    - **Purpose**: Add a new device type to sensor data service platform.
    - **Method**: POST
    - **URL**: http://einstein.sv.cmu.edu/addDeviceType
    - **Semantics**: As a POST method, the API cannot be directly executed through a web browser.  Instead, it may be executed through Rails, JQuery, Python, BASH, etc.
        - **deviceTypeName** (string, not null): Name of the device type.
        - **manufacturer** (string, optional): Name of the manufacturer.
        - **version** (string, optional): Version of the device type.
        - **deviceTypeUserDefinedFields** (string): User defined fields. 
        - **sensorTypeNames** (list of string, not null): All sensor type names contained in the device.
    - **Sample Usages**:
      - **Command Line Example**: 
          1. Prepare input device type metadata in a json file:
              - "deviceType.json" file contains: {"deviceTypeName": "device 1", "manufacturer": "TI", "version": "1.0", "deviceTypeUserDefinedFields": "For test", "sensorTypeNames":["temp", "light"]}
          2. curl -H "Content-Type: application/json" -d @deviceType.json "http://einstein.sv.cmu.edu/addDeviceType"
      - **Result**: HTTP 201 if the device type metadata has been successfully added to the database.


11. <a name="11"></a>**ADD DEVICE**
    - **Purpose**: Add a new device to sensor data service platform.
    - **Method**: POST
    - **URL**: http://einstein.sv.cmu.edu/addDevice
    - **Semantics**: As a POST method, the API cannot be directly executed through a web browser.  Instead, it may be executed through Rails, JQuery, Python, BASH, etc.
        - **deviceTypeName** (string, not null): Name of the device type.
        - **deviceUri** (string): Unique uri of a device.
        - **locationInterpreter** (string): Location interpreter.
        - **latitude** (double): Latitude.
        - **longitude** (double): Longitude.
        - **altitude** (double): Altitude.
        - **deviceUserDefinedFields** (string): User defined fields. 
    - **Sample Usages**:
      - **Command Line Example**: 
          1. Prepare input device metadata in a json file:
              - "device.json" file contains: {"deviceTypeName": "fireimp", "deviceUri": "www.device.com", "locationInterpreter": "test location description", "latitude": 10, "longitude": 10, "altitude": 10, "deviceUserDefinedFields": "For test"}
          2. curl -H "Content-Type: application/json" -d @device.json "http://einstein.sv.cmu.edu/addDevice"
      - **Result**: HTTP 201 if the device metadata have been successfully added to the database.

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

14. <a name="12"></a>**EDIT SENSOR TYPE**
    - **Purpose**: Edit a sensor type to sensor data service platform.
    - **Method**: POST
    - **URL**: http://einstein.sv.cmu.edu/updateSensorType
    - **Semantics**: As a POST method, the API cannot be directly executed through a web browser.  Instead, it may be executed through Rails, JQuery, Python, BASH, etc.
        - **sensorTypeName** (string, not null): Name of the sensor type, which cannot be changed
        - **sensorTypeUserDefinedFields** (string, not null): User defined fields
    - **Sample Usages**:
      - **Command Line Example**: 
          1. Prepare input sensor type metadata in a json file:
              - "sensorType.json" file contains: {"sensorTypeName": "Humidity", "sensorTypeUserDefinedFields": "Testing only"}
          2. curl -H "Content-Type: application/json" -d @sensorType.json "http://einstein.sv.cmu.edu/updateSensorType"
      - **Result**: HTTP 200 if the sensor type metadata has been successfully updated to the database

15. <a name="22"></a>**ADD SENSOR CATEGORY**
    - **Purpose**: Add a new sensor category to sensor data service platform.
    - **Method**: POST
    - **URL**: http://einstein.sv.cmu.edu/addSensorCategory
    - **Semantics**: As a POST method, the API cannot be directly executed through a web browser.  Instead, it may be executed through Rails, JQuery, Python, BASH, etc.
        - **sensorCategoryName** (string, not null): Unique name of the sensor category
        - **purpose** (string, optional): Purpose of the sensor category
    - **Sensor type metadata format**: {"sensorCategoryName": <"sensorCategoryName">, "purpose": <"purpose">}    
    - **Sample Usages**:
      - **Command Line Example**: 
          1. Prepare input sensor type metadata in a json file:
              - "sensorCategory.json" file contains: {"sensorCategoryName": "Category 1", "purpose": "Test only"}
          2. curl -H "Content-Type: application/json" -d @sensorCategory.json "http://einstein.sv.cmu.edu/addSensorCategory"
      - **Result**: HTTP 201 if the sensor category metadata has been successfully added to the database, HTTP 400 if the sensorCategoryName is already been used

16. <a name="23"></a>**EDIT SENSOR CATEGORY**
    - **Purpose**: Edit a sensor category to sensor data service platform.
    - **Method**: POST
    - **URL**: http://einstein.sv.cmu.edu/updateSensorCategory
    - **Semantics**: As a POST method, the API cannot be directly executed through a web browser.  Instead, it may be executed through Rails, JQuery, Python, BASH, etc.
        - **sensorCategoryName** (string, not null): Name of the sensor category, which cannot be changed
        - **purpose** (string, not null): Purpose of the sensor category
    - **Sample Usages**:
      - **Command Line Example**: 
          1. Prepare input sensor type metadata in a json file:
              - "sensorCategory.json" file contains: {"sensorCategoryName": "Category 1", "purpose": "Production only"}
          2. curl -H "Content-Type: application/json" -d @sensorCategory.json "http://einstein.sv.cmu.edu/updateSensorCategory"
      - **Result**: HTTP 200 if the sensor category metadata has been successfully updated to the database

17. <a name="13"></a>**EDIT SENSOR**
    - **Purpose**: Edit a sensor to sensor data service platform.
    - **Method**: POST
    - **URL**: http://einstein.sv.cmu.edu/updateSensor
    - **Semantics**: As a POST method, the API cannot be directly executed through a web browser.  Instead, it may be executed through Rails, JQuery, Python, BASH, etc.
        - **sensorName** (string, not null): Name of the sensor, which cannot be changed
        - **sensorUserDefinedFields** (string, not null): User defined fields.
    - **Sample Usages**:
      - **Command Line Example**: 
          1. Prepare input sensor metadata in a json file:
              - "sensor.json" file contains: {"sensorName": "TestSensor", "sensorUserDefinedFields": "Production only"}
          2. curl -H "Content-Type: application/json" -d @sensor.json "http://einstein.sv.cmu.edu/updateSensor"
      - **Result**: HTTP 200 if the sensor metadata have been successfully updated to the database

18. <a name="14"></a>**EDIT DEVICE TYPE**
    - **Purpose**: Edit an existing device type in sensor data service platform.
    - **Method**: POST
    - **URL**: http://einstein.sv.cmu.edu/updateDeviceType
    - **Semantics**: As a POST method, the API cannot be directly executed through a web browser.  Instead, it may be executed through Rails, JQuery, Python, BASH, etc.
        - **deviceTypeName** (string, not null): Name of the device type.
        - **deviceTypeUserDefinedFields** (string): User defined fields. 
    - **Sample Usages**:
      - **Command Line Example**: 
          1. Prepare input device type metadata in a json file:
              - "deviceType.json" file contains: {"deviceTypeName": "device 1", "deviceTypeUserDefinedFields": "For production"}
          2. curl -H "Content-Type: application/json" -d @deviceType.json "http://einstein.sv.cmu.edu/updateDeviceType"
      - **Result**: HTTP 200 if the device type metadata has been successfully added to the database.


19. <a name="15"></a>**EDIT DEVICE**
    - **Purpose**: Edit an existing device in sensor data service platform.
    - **Method**: POST
    - **URL**: http://einstein.sv.cmu.edu/updateDevice
    - **Semantics**: As a POST method, the API cannot be directly executed through a web browser.  Instead, it may be executed through Rails, JQuery, Python, BASH, etc.
        - **deviceTypeName** (string, not null): Name of the device type.
        - **deviceUserDefinedFields** (string): User defined fields. 
    - **Sample Usages**:
      - **Command Line Example**: 
          1. Prepare input device metadata in a json file:
              - "device.json" file contains: {"deviceTypeName": "fireimp", "deviceUserDefinedFields": "For production"}
          2. curl -H "Content-Type: application/json" -d @device.json "http://einstein.sv.cmu.edu/updateDevice"
      - **Result**: HTTP 200 if the device metadata have been successfully added to the database.
      

31. <a name="31"></a>**GET ALL SENSOR CATEGORIES**
    - **Purpose**: Query all sensor categories.
    - **Method**: GET
    - **URL**: http://einstein.sv.cmu.edu/getAllSensorCategories/<"resultFormat">
    - **Semantics**: 
        - **resultFormat**: Either JSON or CSV.
    - **Sample Usages**:  
      - **Sample csv request**: http://einstein.sv.cmu.edu/getAllSensorCategories/csv<br/>
      - **Sample csv result**: (sensorCategoryName,purpose) </br>sensorCategory1, temp
      - **Sample json request**: http://einstein.sv.cmu.edu/getAllSensorCategories/json
      - **Sample json result**: [{"sensorCategoryName":sensorCategory1,"purpose":"temp"}]
      
32. <a name="32"></a>**GET A SPECIFIC SENSOR CATEGORY**
    - **Purpose**: Query a specific sensor category.
    - **Method**: GET
    - **URL**: http://einstein.sv.cmu.edu/getSensorCategory/<"sensorCategoryName">/<"resultFormat">
    - **Semantics**: 
        - **sensorCategoryName**: Sensor category name.
        - **resultFormat**: Either JSON or CSV.
    - **Sample Usages**: 
      - **Result**: HTTP 200 if the sensorCategoryName exists, HTTP 404 if not found
      - **Sample csv request**: http://einstein.sv.cmu.edu/getSensorCategoryName/sensorCategory1/csv<br/>
      - **Sample csv result**: (sensorCategoryName,purpose) </br>sensorCategory1, temp
      - **Sample json request**: http://einstein.sv.cmu.edu/getSensorCategoryName/sensorCategory1/json
      - **Sample json result**: {"sensorCategoryName":sensorCategory1,"purpose":"temp"}
      
33. <a name="33"></a>**GET ALL SENSOR TYPES**
    - **Purpose**: Query all sensor types.
    - **Method**: GET
    - **URL**: http://einstein.sv.cmu.edu/getAllSensorTypes/<"resultFormat">
    - **Semantics**: 
        - **resultFormat**: Either JSON or CSV.
    - **Sample Usages**: 
      - **Sample csv request**: http://einstein.sv.cmu.edu/getAllSensorTypes/csv<br/>
      - **Sample csv result**: (sensorTypeName, manufacturer,version,maximumValue,minimumValue,unit,interpreter,sensorTypeUserDefinedFields, sensorCategoryName) </br>Humidity, Motorola, 1.0, 100, 0, Percentage, MyInterpreter, Testing only, Environment
      - **Sample json request**: http://einstein.sv.cmu.edu/getAllSensorTypes/json
      - **Sample json result**: [{"sensorTypeName": "Humidity", "manufacturer": "Motorola", "version": "1.0", "maximumValue": 100, "minimumValue": 0, "unit": "Percentage", "interpreter": "MyInterpreter", "sensorTypeUserDefinedFields": "Testing only", "sensorCategoryName": "Environment"}]
      
34. <a name="34"></a>**GET A SPECIFIC SENSOR TYPE**
    - **Purpose**: Query a specific sensor type.
    - **Method**: GET
    - **URL**: http://einstein.sv.cmu.edu/getSensorType/<"sensorTypeName">/<"resultFormat">
    - **Semantics**: 
        - **sensorTypeName**" Sensor type name
        - **resultFormat**: Either JSON or CSV.
    - **Sample Usages**:  
      - **Result**: HTTP 200 if the sensorTypeName exists, HTTP 404 if not found
      - **Sample csv request**: http://einstein.sv.cmu.edu/getSensorType/Humidity/csv<br/>
      - **Sample csv result**: (sensorTypeName, manufacturer,version,maximumValue,minimumValue,unit,interpreter,sensorTypeUserDefinedFields, sensorCategoryName) </br>Humidity, Motorola, 1.0, 100, 0, Percentage, MyInterpreter, Testing only, Environment
      - **Sample json request**: http://einstein.sv.cmu.edu/getSensorType/Humidity/json
      - **Sample json result**: {"sensorTypeName": "Humidity", "manufacturer": "Motorola", "version": "1.0", "maximumValue": 100, "minimumValue": 0, "unit": "Percentage", "interpreter": "MyInterpreter", "sensorTypeUserDefinedFields": "Testing only", "sensorCategoryName": "Environment"}
      

35. <a name="35"></a>**GET ALL SENSORS**
    - **Purpose**: Query all sensors.
    - **Method**: GET
    - **URL**: http://einstein.sv.cmu.edu/getAllSensors/<"resultFormat">
    - **Semantics**: 
        - **resultFormat**: Either JSON or CSV.
    - **Sample Usages**: 
      - **Sample csv request**: http://einstein.sv.cmu.edu/getAllSensors/csv<br/>
      - **Sample csv result**: (sensorName, sensorUserDefinedFields, deviceUri, sensorTypeName, manufacturer,version,maximumValue,minimumValue,unit,interpreter,sensorTypeUserDefinedFields, sensorCategoryName) </br>sensor01, for test, www.device.com/001, Motorola, 1.0, 100, 0, Percentage, MyInterpreter, Testing only, Environment, test only
      - **Sample json request**: http://einstein.sv.cmu.edu/getAllSensorTypes/json
      - **Sample json result**: [{"sensorName": "sensor01", "sensorUserDefinedFields": "for test", "deviceUri": "www.device.com/001","sensorTypeName": "Humidity", "manufacturer": "Motorola", "version": "1.0", "maximumValue": 100, "minimumValue": 0, "unit": "Percentage", "interpreter": "MyInterpreter", "sensorTypeUserDefinedFields": "Testing only", "sensorCategoryName": "Environment", "purpose": "test only"}]
      

36. <a name="36"></a>**GET A SPECIFIC SENSOR**
    - **Purpose**: Query a specific sensor.
    - **Method**: GET
    - **URL**: http://einstein.sv.cmu.edu/getSensor/<"sensorName">/<"resultFormat">
    - **Semantics**: 
        - **sensorName**: Sensor name
        - **resultFormat**: Either JSON or CSV.
    - **Sample Usages**:  
      - **Result**: HTTP 200 if the sensorName exists, HTTP 404 if not found
      - **Sample csv request**: http://einstein.sv.cmu.edu/sensor1/getSensor/csv<br/>
      - **Sample csv result**: (sensorName, sensorUserDefinedFields, deviceUri, sensorTypeName, manufacturer,version,maxValue,minValue,unit,interpreter,sensorTypeUserDefinedFields, sensorCategoryName) </br>sensor1, for test, www.device.com, Humidity, Motorola, 1.0, 100, 0, Percentage, MyInterpreter, Testing only, Environment
      - **Sample json request**: http://einstein.sv.cmu.edu/getSensor/<"sensorName">/json
      - **Sample json result**: {"sensorName": "sensor1", "sensorUserDefinedFields": "for test", "deviceUri":"www.device.com", "sensorTypeName": "Humidity", "manufacturer": "Motorola", "version": "1.0", "maximumValue": 100, "minimumValue": 0, "unit": "Percentage", "interpreter": "MyInterpreter", "sensorTypeUserDefinedFields": "Testing only", "sensorCategoryName": "Environment"}
      

      
37. <a name="37"></a>**GET ALL DEVICE TYPES**
    - **Purpose**: Query all device types.
    - **Method**: GET
    - **URL**: http://einstein.sv.cmu.edu/getAllDeviceTypes/<"resultFormat">
    - **Semantics**: 
        - **resultFormat**: Either JSON or CSV.
    - **Sample Usages**: 
      - **Sample csv request**: http://einstein.sv.cmu.edu/getAllDeviceTypes/csv<br/>
      - **Sample csv result**: (deviceTypeName,manufacturer,version,deviceTypeUserDefinedFields,sensorTypeNames) </br>device type 1, TI, 1.0, For Test, [temp, light]
      - **Sample json request**: http://einstein.sv.cmu.edu/getAllDeviceTypes/json
      - **Sample json result**: [{"deviceTypeName": "device type  1", "manufacturer": "TI", "version": "1.0", "deviceTypeUserDefinedFields": "For test", "sensorTypeNames":["temp", "light"]}]
     

      
38. <a name="38"></a>**GET A SPECIFIC DEVICE TYPE**
    - **Purpose**: Query a specific device type.
    - **Method**: GET
    - **URL**: http://einstein.sv.cmu.edu/getDeviceType/<"deviceTypeName">/<"resultFormat">
    - **Semantics**: 
        - **deviceTypeName**: Device type name.
        - **resultFormat**: Either JSON or CSV.
    - **Sample Usages**:  
      - **Result**: HTTP 200 if the deviceTypeName exists, HTTP 404 if not found
      - **Sample csv request**: http://einstein.sv.cmu.edu/getDeviceType/device type 1/csv<br/>
      - **Sample csv result**: (deviceTypeName,manufacturer,version,deviceTypeUserDefinedFields,sensorTypeNames) </br>device 1, TI, 1.0, For Test, [temp, light]
      - **Sample json request**: http://einstein.sv.cmu.edu/getDeviceType/<"deviceTypeName">/json
      - **Sample json result**: {"deviceTypeName": "device type  1", "manufacturer": "TI", "version": "1.0", "deviceTypeUserDefinedFields": "For test", "sensorTypeNames":["temp", "light"]}
      


39. <a name="39"></a>**GET ALL DEVICES**
    - **Purpose**: Query all device types.
    - **Method**: GET
    - **URL**: http://einstein.sv.cmu.edu/getAllDevices/<"resultFormat">
    - **Semantics**: 
        - **resultFormat**: Either JSON or CSV.
    - **Sample Usages**: 
      - **Sample csv request**: http://einstein.sv.cmu.edu/getAllDevices/csv<br/>
      - **Sample csv result**: (deviceUri, deviceUserDefinedFields, longitude, latitude, altitude, locationInterpreter, deviceTypeName,manufacturer,version,deviceTypeUserDefinedFields,sensorTypeNames, sensorNames) </br>www.device.com, For test, 10, 10, 10, myInterpreter device type 1, TI, 1.0, For Test, [temp, light], [sensor1, sensor2]
      - **Sample json request**: http://einstein.sv.cmu.edu/getAllDevices/json
      - **Sample json result**: [{"deviceUri":"www.device.com", "deviceUserDefinedFields": "For test", "longitude":10, "latitude": 10, "altitude":10, "locationInterpreter": "myInterpreter", "deviceTypeName": "device type  1", "manufacturer": "TI", "version": "1.0", "deviceTypeUserDefinedFields": "For test", "sensorTypeNames":["temp", "light"], "sensorNames":["sensor1", "sensor2"]}]
      


40. <a name="40"></a>**GET A SPECIFIC DEVICE**
    - **Purpose**: Query a specific device.
    - **Method**: GET
    - **URL**: http://einstein.sv.cmu.edu/getDevice/<"deviceUri">/<"resultFormat">
    - **Semantics**: 
        - **resultFormat**: Either JSON or CSV.
    - **Sample Usages**:  
      - **Result**: HTTP 200 if the device exists, HTTP 404 if not found
      - **Sample csv request**: http://einstein.sv.cmu.edu/getDevice/www.device.com/csv<br/>
      - **Sample csv result**: (deviceUri, deviceUserDefinedFields, longitude, latitude, altitude, locationInterpreter, deviceTypeName,manufacturer,version,deviceTypeUserDefinedFields,sensorTypeNames, sensorNames) </br>www.device.com, For test, 10, 10, 10, myInterpreter device type 1, TI, 1.0, For Test, [temp, light], [sensor1, sensor2]
      - **Sample json request**: http://einstein.sv.cmu.edu/getDevice/www.device.com/json
      - **Sample json result**: {"deviceUri":"www.device.com", "deviceUserDefinedFields": "For test", "longitude":10, "latitude": 10, "altitude":10, "locationInterpreter": "myInterpreter", "deviceTypeName": "device type  1", "manufacturer": "TI", "version": "1.0", "deviceTypeUserDefinedFields": "For test", "sensorTypeNames":["temp", "light"], "sensorNames":["sensor1", "sensor2"]}

41. <a name="24"></a>**DELETE SENSOR CATEGORY**
    - **Purpose**: Delete a sensor category from sensor data service platform.
    - **Method**: DELETE
    - **URL**: http://einstein.sv.cmu.edu/deleteSensorCategory/<"sensorCategoryName">
    - **Semantics**
        - **sensorCategoryName** (string, not null): Name of the sensor category
    - **Sample Usages**:
      - **Command Line Example**: 
          1. curl -X DELETE http://localhost:9000/deleteSensorCategory/testSensorCategoryName
      - **Result**: HTTP 201 if the sensor category metadata has been successfully deleted from the database

42. <a name="16"></a>**DELETE SENSOR TYPE**
    - **Purpose**: Delete a sensor type from sensor data service platform.
    - **Method**: DELETE
    - **URL**: http://einstein.sv.cmu.edu/deleteSensorType/<"sensorTypeName">
    - **Semantics**
        - **sensorTypeName** (string, not null): Name of the sensor type
    - **Sample Usages**:
      - **Command Line Example**: 
          1. curl -X DELETE http://localhost:9000/deleteSensorType/testSensorTypeName
      - **Result**: HTTP 201 if the sensor type metadata has been successfully deleted from the database

43. <a name="17"></a>**DELETE SENSOR**
    - **Purpose**: Delete a sensor from sensor data service platform.
    - **Method**: DELETE
    - **URL**: http://einstein.sv.cmu.edu/deleteSensor/<"sensorName">
    - **Semantics**
        - **sensorName** (string, not null): Name of the sensor
    - **Sample Usages**:
      - **Command Line Example**: 
          1. curl -X DELETE http://localhost:9000/deleteSensor/testSensorName
      - **Result**: HTTP 201 if the sensor metadata has been successfully deleted from the database

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


