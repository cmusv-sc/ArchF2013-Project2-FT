<b>Released under a Dual Licensing / GPL 3.</b>
Sensor Service Platform APIs Version 1.3
========================================
Advisors: Jia Zhang, Bob Iannucci, Martin Griss, Steven Rosenberg, Anthony Rowe<br/>
Current contributors: Bo Liu, Ching Lun Lin, Chris Lee, David Lee, Jeff Hsu, Lyman Cao <br/>
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

**Category 1: Post sensor reading**<br/>
   - [Post sensor reading data](#1)<br/>
    
**Category 2: Query database for sensor readings**<br/>
   - [Get sensor reading from a sensor(specified by sensorName) at a timestamp](#4)<br/>
   - [Get sensor reading from a sensor(specified by deviceUri and sensorTypeName) at a timestamp](#21)<br/>
   - [Get sensor reading from a sensor(specified by sensorName) among a timestamp range](#5)<br/>
   - [Get sensor reading from a sensor(specified by deviceUri and sensorTypeName) between a timestamp range](#25)<br/>
   - [Get last minute's sensor readings for a specific sensor](#26)
   - [Get last minute's sensor readings for a sensor type in all registered devices](#6)
   - [Get latest sensor reading for a specific sensor](#27)
   - [Get latest sensor readings for a sensor type in all registered devices](#7)
   - [Get latest sensor readings from devices inside a specific geo-fence](#24)<br/>

    
**Category 3: Manage metadata**<br/>
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
   - [Get all sensors (reduced)](#28)
   - [Get a specific sensor](#36)
   - [Get all device types](#37)
   - [Get a specific device type](#38)
   - [Get all devices](#39)
   - [Get devices inside a specific geo-fence](#30)
   - [Get a specific device](#40)

**Category 4: Access Control**<br/>
   - [Add a user](#41)
   - [Get a user](#42)
   - [Add a sensor as a user](#43)
   - [Get all sensors which has been added by a user](#44)
   - [Get a specific sensor which has been added by a user](#45)

**Category 5: User management for contest**<br/>
   - [Add a contest user](#51)
   - [Update a contest user](#52)
   - [Delete a contest user](#53)
   - [Get all contest users](#54)
   - [Get specific contest user](#55)

Detailed Usages:
----------------
Note: all TimeStamps are in Unix epoch time format to millisecond. Conversion from readable timestamp format to Unix epoch timestamp can be found in http://www.epochconverter.com

1. <a name="1"></a>**PUBLISH SENSOR READINGS**
    - **Purpose**: Publish sensor readings to sensor data service platform.
    - **Method**: POST
    - **URL**: http://einstein.sv.cmu.edu:9000/addSensorReading
    - **Semantics**: As a POST method, the API cannot be directly executed through a web browser.  Instead, it may be executed through Rails, JQuery, Python, BASH, etc.
        - **sensorName** (string, **not null**): Existing sensor name.
        - **timestamp** (int, **not null**): Recording timestamp of the sensor reading in Unix epoch timestamp format. 
        - **value** (string, **not null**): The value of the sensor reading. It is user's responsibility to calibrate the sensor readings before publishing.
        - **isIndoor** (boolean, optional): Sent from indoor or not.
        - **longitude** (double, optional): Longitude of the sensor reading.
        - **latitude** (double, optional): Longitude of the sensor reading.
        - **altitude** (double, optional): Longitude of the sensor reading.
        - **locationInterpreter** (string, optional): Interpreter information of location.
    - **Sample Usages**:
      - **Command Line Example**: 
          1. Prepare input sensor reading data in a JSON array (**please modify the timestamp to a different value**):
              - "sampleReading.json" file contains: [{"sensorName": "testSensor1", "timestamp": 1368568896000, "value": "16", "isIndoor":true, "longitude":10, "latitude":10, "altitude": 10, "locationInterpreter":"GPS"}, {"sensorName": "testSensor2", "timestamp": 1368568896000, "value": "17", "isIndoor":true, "longitude":10, "latitude":10, "altitude": 10, "locationInterpreter":"GPS"}]
          2. curl -H "Content-Type: application/json" -d @sampleReading.json "http://einstein.sv.cmu.edu:9000/addSensorReading"
      - **Result**: HTTP 201 if the sensor readings have been successfully posted, HTTP 400 if failed.
    

4. <a name="4"></a>**GET SENSOR READING FROM A SENSOR(SPECIFIED BY SENSOR NAME) AT A TIMESTAMP**
    - **Purpose**: Query sensor reading for a specific sensor at a specific time point.
    - **Method**: GET
    - **URL**: http://einstein.sv.cmu.edu:9000/getSensorReading/<"sensorName">/<"timestamp">/<"resultFormat">
    - **Semantics**: 
        - **sensorName**: Existing sensor name.
        - **timestamp**: Time of the readings to query.
        - **resultFormat**: Either JSON or CSV.
    - **Sample Usages**: 
      - **Sample csv request**: http://einstein.sv.cmu.edu:9000/getSensorReading/androidAccelerometer/1395247329000/csv<br/>
      - **Sample csv result**: (sensorName,timestamp,value) </br>sensor1,1368568896000,518.0
      - **Sample json request**: http://einstein.sv.cmu.edu:9000/getSensorReading/androidAccelerometer/1395247329000/json
      - **Sample json result**: {"timestamp":1368568896000,"sensorName":"sensor1","value":518}
      - **Result**: HTTP 200 if returned successfully, HTTP 404 if not found.

21. <a name="21"></a>**GET SENSOR READING FROM A SENSOR(SPECIFIED BY DEVICE URI AND SENSOR TYPE NAME) AT A TIMESTAMP**
    - **Purpose**: Query sensor reading for a specific sensor at a specific time point.
    - **Method**: GET
    - **URL**: http://einstein.sv.cmu.edu:9000/getSensorReading/<"deviceUri">/<"sensorTypeName">/<"timestamp">/<"resultFormat">
    - **Semantics**: 
        - **deviceUri**: Existing device uri.
        - **sensorTypeName**: Existing sensor type name.
        - **timestamp**: Time of the readings to query.
        - **resultFormat**: Either JSON or CSV.
    - **Sample Usages**: 
      - **Sample csv request**: http://einstein.sv.cmu.edu:9000/getSensorReading/23420ca4e4830bee/fireImpXAccelerometer/1395247329000/csv<br/>
      - **Sample csv result**: (sensorName,timestamp,value) </br>sensor1,1368568896000,518.0
      - **Sample json request**: http://einstein.sv.cmu.edu:9000/getSensorReading/23420ca4e4830bee/fireImpXAccelerometer/1395247329000/json<br/>
      - **Sample json result**: {"timestamp":1368568896000,"sensorName":"sensor1","value":518}
      - **Result**: HTTP 200 if returned successfully, HTTP 404 if not found.
      
5. <a name="5"></a>**GET SENSOR READING FROM A SENSOR(SPECIFIED BY SENSOR NAME) AMONG A TIMESTAMP RANGE**
    - **Purpose**: Query sensor readings for a specific sensor among a specific time range. 
    - **Method**: GET
    - **URL**: http://einstein.sv.cmu.edu:9000/getSensorReadingInRange/<"sensorName">/<"startTime">/<"endTime">/<"resultFormat">
    - **Semantics**:
        - **sensorName**: Existing sensor name.
        - **startTime**: Start time of the readings to query.
        - **endTime**: End time of the readings to query.
        - **resultFormat**: Either JSON or CSV.
    - **Sample Usages**: 
      - **Sample csv request**: http://einstein.sv.cmu.edu:9000/getSensorReadingInRange/androidAccelerometer/1395266600000/1395279800000/csv
      - **Sample csv result**: (sensorName,timestamp,value)<br/>
          sensor1,1368568993000,517.0 <br/>
          ... <br/>
          sensor1,1368568896000,518.0
      - **Sample json request**: http://einstein.sv.cmu.edu:9000/getSensorReadingInRange/androidAccelerometer/1395266600000/1395279800000/json
      - **Sample json result**: <br/>
          [{"timestamp":1368568993000,"value":517,"sensorName":"sensor1"},
          ... <br/>
          {"timestamp":1368568896000,"value": 520,"sensorName":"sensor1"}]
      - **Result**: HTTP 200 if returned successfully, HTTP 404 if not found.

25. <a name="25"></a>**GET SENSOR READING FROM A SENSOR(SPECIFIED BY DEVICE URI AND SENSOR TYPE NAME) BETWEEN A TIMESTAMP RANGE**
    - **Purpose**: Query sensor readings for a specific sensor among a specific time range. 
    - **Method**: GET
    - **URL**: http://einstein.sv.cmu.edu:9000/getSensorReadingInRange/<"deviceUri">/<"sensorTypeName">/<"startTime">/<"endTime">/<"resultFormat">
    - **Semantics**:
        - **deviceUri**: Existing device uri.
        - **sensorTypeName**: Existing sensor type name.
        - **startTime**: Start time of the readings to query.
        - **endTime**: End time of the readings to query.
        - **resultFormat**: Either JSON or CSV.
    - **Sample Usages**: 
      - **Sample csv request**: http://einstein.sv.cmu.edu:9000/getSensorReadingInRange/23420ca4e4830bee/fireImpXAccelerometer/1394557419000/1394643819000/csv
      - **Sample csv result**: (sensorName,timestamp,value)<br/>
          sensor1,1368568993000,517.0 <br/>
          ... <br/>
          sensor1,1368568896000,518.0
      - **Sample json request**: http://einstein.sv.cmu.edu:9000/getSensorReadingInRange/23420ca4e4830bee/fireImpXAccelerometer/1394557419000/1394643819000/json
      - **Sample json result**: <br/>
          [{"timestamp":1368568993000,"value":517,"sensorName":"sensor1"},
          ... <br/>
          {"timestamp":1368568896000,"value": 520,"sensorName":"sensor1"}]
      - **Result**: HTTP 200 if returned successfully, HTTP 404 if not found.
26. <a name="26"></a>**GET LAST MINUTE'S SENSOR READINGS FOR A SPECIFIC SENSOR**
    - **Purpose**: Query the last minute of sensor readings based on sensor name.
    - **Method**: GET
    - **URL**: http://einstein.sv.cmu.edu:9000/getLastMinuteSensorReadings/<"sensorName">/<"resultFormat">
    - **Semantics**:
        - **sensorName**: Existing sensor name
        - **resultFormat**: Either JSON or CSV
    - **Sample Usages**: 
      - TODO
6. <a name="6"></a>**GET LAST MINUTE OF SENSOR READINGS AT A TIME POINT FOR ALL REGISTERED DEVICES**
    - **Purpose**: Query all sensor readings at a time point (within 60 seconds), of a specific sensor type contained in all registered devices.
    - **Method**: GET
    - **URL**: http://einstein.sv.cmu.edu:9000/getLastMinuteReadingsFromAllDevices/<"timestamp">/<"resultFormat">
    - **Semantics**:
        - **timestamp**: Time to query the last readings of all sensors for all devices registered at the sensor data service platform.
        - **resultFormat**: Either JSON or CSV.
    - **Sample Usages**: 
      - **Sample csv request**: http://einstein.sv.cmu.edu:9000/getLastMinuteReadingsFromAllDevices/1395684000000/csv
      - **Sample csv result**: 
          (sensorName,timestamp,value)  
          androidAccelerometer,1395683988039,0.7101593   
          ...  
          androidAccelerometer,1395683991877,-0.9180145  
      - **Sample json request**: http://einstein.sv.cmu.edu:9000/getLastMinuteReadingsFromAllDevices/1395684000000/json
      - **Sample json result**: <br/>
          [{"timestamp":"2014-03-24 10:59:48.039","value":"0.7101593","sensorName":"androidAccelerometer"},
          ... <br/>
          {"timestamp":"2014-03-24 10:59:48.174","value":"188.17767","sensorName":"androidOrientationVector"},{"timestamp":"2014-03-24 10:59:49.182","value":"182.88602","sensorName":"androidOrientationVector"}]
      - **Result**: HTTP 200 if returned successfully, HTTP 404 if not found.

27. <a name="27"></a>**GET LATEST SENSOR READING FOR A SPECIFIC SENSOR**
    - **Purpose**: Query the lastest sensor reading for a specific sensor.
    - **Method**: GET
    - **URL**: http://einstein.sv.cmu.edu:9000/getLatestSensorReading/<"sensorName">/<"resultFormat">
    - **Semantics**:
        - **sensorName**: Existing sensor name
        - **resultFormat**: Either JSON or CSV
    - **Sample Usages**: 
      - TODO

7. <a name="7"></a>**GET LATEST SENSOR READINGS AT A TIME POINT FOR A TYPE OF SENSOR IN ALL REGISTERED DEVICES**
    - **Purpose**: Query all latest sensor readings, of a specific sensor type contained in all devices.  If no reading for a sensor in the last 60 seconds, the latest stored reading of the corresponding sensor will be returned. 
    - **Method**: GET
    - **URL**: http://einstein.sv.cmu.edu:9000/getLatestReadingsFromAllDevices/<"sensorType">/<"resultFormat">
    - **Semantics**:
        - **sensorType**: Type of the sensor (e.g., temperature, CO2, etc.).
        - **resultFormat**: Either json or csv.
        - Note: The difference between API#7 and API#6 (lastReadingsFromAllDevices) given the current timestamp is that, API#7 returns the last readings stored for each device even if it is more than 60 seconds old.
    - **Sample Usages**: 
      - **Sample csv request**: http://einstein.sv.cmu.edu:9000/getLatestReadingsFromAllDevices/androidMobile/csv     
      - **Sample csv result**: (device_id,timestamp,sensorType,value) </br>
          10170203,1368568896000,temp,513.0 <br/>
          ... <br/>
          10170204,1368568889000,temp,515.0
      - **Sample json request**: http://einstein.sv.cmu.edu:9000/getLatestReadingsFromAllDevices/androidMobile/json
      - **Sample json result**: <br/>
        [{"timestamp":1368568896000,"sensorType":"temp","value":513,"deviceId":"10170203"},
        ... <br/>
        {"timestamp":1368568889000,"sensorType":"temp","value":515,"deviceId":"10170204"}]
      - **Result**: HTTP 200 if returned successfully, HTTP 404 if not found.

24. <a name="24"></a>**GET LATEST SENSOR READINGS FROM DEVICES INSIDE A SPECIFIED GEO-FENCE**
    - **Purpose**: Query all latest sensor readings, of all sensors contained in all devices inside a specified geo-fence.  If no reading for a sensor in the last 60 seconds, the latest stored reading of the corresponding sensor will be returned. 
    - **Method**: GET
    - **URL**: http://einstein.sv.cmu.edu:9000/latestReadingFromDevicesByGeofence/<"geo-fence">/<"resultFormat">
    - **Semantics**:
        - **geo-fence**: The location representation of the device.
        - **resultFormat**: Either json or csv.
    - **Sample Usages**: 
      - **Sample csv request**: http://einstein.sv.cmu.edu:9000/latestReadingFromDevicesByGeofence/room129A/csv     
      - **Sample csv result**: (sensorName,isIndoor,timeStamp,value,longitude,latitude,altitude,locationInterpreter) </br>
          fireImpXAccelerometer23420ca4e4830bee,true,2014-03-13 12:18:59.0,37904,91.0,91.0,91.0,GPS <br/>
          ... <br/>
          fireImpZAccelerometer23420ca4e4830bee,true,2014-03-13 12:18:59.0,37904,91.0,91.0,91.0,GPS
      - **Sample json request**: http://einstein.sv.cmu.edu:9000/latestReadingFromDevicesByGeofence/room129A/json
      - **Sample json result**: <br/>
        [{"sensorName":"fireImpXAccelerometer23420ca4e4830bee","isIndoor":true,"timeStamp":"Mar 13, 2014 12:18:59 PM","locationInterpreter":"GPS","value":"37904","longitude":91.0,"latitude":91.0,"altitude":91.0},{"sensorName":"fireImpZAccelerometer23420ca4e4830bee","isIndoor":true,"timeStamp":"Mar 13, 2014 12:18:59 PM","locationInterpreter":"GPS","value":"37904","longitude":91.0,"latitude":91.0,"altitude":91.0}]
      - **Result**: HTTP 200 if returned successfully, HTTP 404 if not found.

15. <a name="22"></a>**ADD SENSOR CATEGORY**
    - **Purpose**: Add a new sensor category to sensor data service platform.
    - **Method**: POST
    - **URL**: http://einstein.sv.cmu.edu:9000/addSensorCategory
    - **Semantics**: As a POST method, the API cannot be directly executed through a web browser.  Instead, it may be executed through Rails, JQuery, Python, BASH, etc.
        - **sensorCategoryName** (string, not null): Non existing unique name of the sensor category
        - **purpose** (string, optional): Purpose of the sensor category
    - **Sensor type metadata format**: {"sensorCategoryName": <"sensorCategoryName">, "purpose": <"purpose">}    
    - **Sample Usages**:
      - **Command Line Example**: 
          1. Prepare input sensor type metadata in a json file:
              - "sensorCategory.json" file contains: {"sensorCategoryName": "Category 1", "purpose": "Test only"}
          2. curl -H "Content-Type: application/json" -d @sensorCategory.json "http://einstein.sv.cmu.edu:9000/addSensorCategory"
      - **Result**: HTTP 201 if the sensor category metadata has been successfully added to the database, HTTP 400 if the sensorCategoryName is already been used


8. <a name="8"></a>**ADD SENSOR TYPE**
    - **Purpose**: Add a new sensor type to sensor data service platform.
    - **Method**: POST
    - **URL**: http://einstein.sv.cmu.edu:9000/addSensorType
    - **Semantics**: As a POST method, the API cannot be directly executed through a web browser.  Instead, it may be executed through Rails, JQuery, Python, BASH, etc.
        - **sensorTypeName** (string, not null): Non existing unique name of the sensor type
        - **manufacturer** (string, optional): Name of the manufacturerof this sensor type
        - **version** (string, optional): Version of the sensor type
        - **maximumValue** (double, optional): Maximum value of the sensor reading under this sensor type
        - **minimumValue** (double, optional): Minimum value of the sensor reading under this sensor type
        - **unit** (string, optional): Unit of the sensor reading under this sensor type
        - **interpreter** (string, optional): The interpreter used to parse the sensor reading under this sensor type
        - **sensorTypeUserDefinedFields** (string): User defined fields
        - **sensorCategoryName** (string, not null): Existing sensor category name which the sensor type belongs to
    - **Sample Usages**:
      - **Command Line Example**: 
          1. Prepare input sensor type metadata in a json file:
              - "sensorType.json" file contains: {"sensorTypeName": "Humidity", "manufacturer": "Motorola", "version": "1.0", "maximumValue": 100, "minimumValue": 0, "unit": "Percentage", "interpreter": "MyInterpreter", "sensorTypeUserDefinedFields": "Testing only", "sensorCategoryName": "Environment"}
          2. curl -H "Content-Type: application/json" -d @sensorType.json "http://einstein.sv.cmu.edu:9000/addSensorType"
      - **Result**: HTTP 201 if the sensor type metadata has been successfully added to the database, HTTP 400 if failed.

9. <a name="9"></a>**ADD SENSOR**
    - **Purpose**: Add a new sensor to sensor data service platform.
    - **Method**: POST
    - **URL**: http://einstein.sv.cmu.edu:9000/addSensor
    - **Semantics**: As a POST method, the API cannot be directly executed through a web browser.  Instead, it may be executed through Rails, JQuery, Python, BASH, etc.
        - **sensorName** (string, not null): Non existing unique name of the sensor
        - **sensorTypeName** (string, not null): Existing name of its sensor type
        - **deviceUri** (string, not null): Existing device URI it belongs to
        - **sensorUserDefinedFields** (string, optional): User defined fields.
    - **Sample Usages**:
      - **Command Line Example**: 
          1. Prepare input sensor metadata in a json file:
              - "sensor.json" file contains: {"sensorName": "TestSensor", "sensorTypeName": "Humidity", "deviceUri": "www.testsensor.com", "sensorUserDefinedFields": "Test only"}
          2. curl -H "Content-Type: application/json" -d @sensor.json "http://einstein.sv.cmu.edu:9000/addSensor"
      - **Result**: HTTP 201 if the sensor metadata have been successfully added to the database, HTTP 400 if failed.

10. <a name="10"></a>**ADD DEVICE TYPE**
    - **Purpose**: Add a new device type to sensor data service platform.
    - **Method**: POST
    - **URL**: http://einstein.sv.cmu.edu:9000/addDeviceType
    - **Semantics**: As a POST method, the API cannot be directly executed through a web browser.  Instead, it may be executed through Rails, JQuery, Python, BASH, etc.
        - **deviceTypeName** (string, not null): Non existing unique name of the device type.
        - **manufacturer** (string, optional): Name of the manufacturer.
        - **version** (string, optional): Version of the device type.
        - **deviceTypeUserDefinedFields** (string): User defined fields. 
        - **sensorTypeNames** (list of string, not null): Existing sensor type names contained in the device.
    - **Sample Usages**:
      - **Command Line Example**: 
          1. Prepare input device type metadata in a json file:
              - "deviceType.json" file contains: {"deviceTypeName": "device 1", "manufacturer": "TI", "version": "1.0", "deviceTypeUserDefinedFields": "For test", "sensorTypeNames":["temp", "light"]}
          2. curl -H "Content-Type: application/json" -d @deviceType.json "http://einstein.sv.cmu.edu:9000/addDeviceType"
      - **Result**: HTTP 201 if the device type metadata has been successfully added to the database, HTTP 400 if failed.


11. <a name="11"></a>**ADD DEVICE**
    - **Purpose**: Add a new device to sensor data service platform.
    - **Method**: POST
    - **URL**: http://einstein.sv.cmu.edu:9000/addDevice
    - **Semantics**: As a POST method, the API cannot be directly executed through a web browser.  Instead, it may be executed through Rails, JQuery, Python, BASH, etc.
        - **deviceTypeName** (string, not null): Existing name of its device type.
        - **uri** (string): Unique uri of a device.
        - **location**
            - **representation** (string): Location interpreter.
            - **latitude** (double): Latitude.
            - **longitude** (double): Longitude.
            - **altitude** (double): Altitude.
        - **deviceUserDefinedFields** (string): User defined fields. 
    - **Sample Usages**:
      - **Command Line Example**: 
          1. Prepare input device metadata in a json file:
              - "device.json" file contains: {"deviceTypeName": "fireimp", "uri": "www.device.com", "location" : {"representation": "test location description", "latitude": 10, "longitude": 10, "altitude": 10}, "deviceUserDefinedFields": "For test"}
          2. curl -H "Content-Type: application/json" -d @device.json "http://einstein.sv.cmu.edu:9000/addDevice"
      - **Result**: HTTP 201 if the device metadata have been successfully added to the database, HTTP 400 if failed.

14. <a name="12"></a>**EDIT SENSOR TYPE**
    - **Purpose**: Edit a sensor type to sensor data service platform.
    - **Method**: POST
    - **URL**: http://einstein.sv.cmu.edu:9000/updateSensorType
    - **Semantics**: As a POST method, the API cannot be directly executed through a web browser.  Instead, it may be executed through Rails, JQuery, Python, BASH, etc.
        - **sensorTypeName** (string, not null): Name of the sensor type, which cannot be changed
        - **sensorTypeUserDefinedFields** (string, not null): User defined fields
    - **Sample Usages**:
      - **Command Line Example**: 
          1. Prepare input sensor type metadata in a json file:
              - "sensorType.json" file contains: {"sensorTypeName": "Humidity", "sensorTypeUserDefinedFields": "Testing only"}
          2. curl -H "Content-Type: application/json" -d @sensorType.json "http://einstein.sv.cmu.edu:9000/updateSensorType"
      - **Result**: HTTP 200 if the sensor type metadata has been successfully updated to the database


16. <a name="23"></a>**EDIT SENSOR CATEGORY**
    - **Purpose**: Edit a sensor category to sensor data service platform.
    - **Method**: POST
    - **URL**: http://einstein.sv.cmu.edu:9000/updateSensorCategory
    - **Semantics**: As a POST method, the API cannot be directly executed through a web browser.  Instead, it may be executed through Rails, JQuery, Python, BASH, etc.
        - **sensorCategoryName** (string, not null): Name of the sensor category, which cannot be changed
        - **purpose** (string, not null): Purpose of the sensor category
    - **Sample Usages**:
      - **Command Line Example**: 
          1. Prepare input sensor type metadata in a json file:
              - "sensorCategory.json" file contains: {"sensorCategoryName": "Category 1", "purpose": "Production only"}
          2. curl -H "Content-Type: application/json" -d @sensorCategory.json "http://einstein.sv.cmu.edu:9000/updateSensorCategory"
      - **Result**: HTTP 200 if the sensor category metadata has been successfully updated to the database

17. <a name="13"></a>**EDIT SENSOR**
    - **Purpose**: Edit a sensor to sensor data service platform.
    - **Method**: POST
    - **URL**: http://einstein.sv.cmu.edu:9000/updateSensor
    - **Semantics**: As a POST method, the API cannot be directly executed through a web browser.  Instead, it may be executed through Rails, JQuery, Python, BASH, etc.
        - **sensorName** (string, not null): Name of the sensor, which cannot be changed
        - **sensorUserDefinedFields** (string, not null): User defined fields.
    - **Sample Usages**:
      - **Command Line Example**: 
          1. Prepare input sensor metadata in a json file:
              - "sensor.json" file contains: {"sensorName": "TestSensor", "sensorUserDefinedFields": "Production only"}
          2. curl -H "Content-Type: application/json" -d @sensor.json "http://einstein.sv.cmu.edu:9000/updateSensor"
      - **Result**: HTTP 200 if the sensor metadata have been successfully updated to the database

18. <a name="14"></a>**EDIT DEVICE TYPE**
    - **Purpose**: Edit an existing device type in sensor data service platform.
    - **Method**: **PUT**
    - **URL**: http://einstein.sv.cmu.edu:9000/updateDeviceType
    - **Semantics**: As a PUT method, the API cannot be directly executed through a web browser.  Instead, it may be executed through Rails, JQuery, Python, BASH, etc.
        - **deviceTypeName** (string, not null): Name of the device type.
        - **deviceTypeUserDefinedFields** (string): User defined fields. 
    - **Sample Usages**:
      - **Command Line Example**: 
          1. Prepare input device type metadata in a json file:
              - "deviceType.json" file contains: {"deviceTypeName": "device 1", "deviceTypeUserDefinedFields": "For production"}
          2. curl -X PUT -H "Content-Type: application/json" -d @deviceType.json "http://einstein.sv.cmu.edu:9000/updateDeviceType"
      - **Result**: HTTP 200 if the device type metadata has been successfully added to the database.


19. <a name="15"></a>**EDIT DEVICE**
    - **Purpose**: Edit an existing device location in sensor data service platform.
    - **Method**: **PUT**
    - **URL**: http://einstein.sv.cmu.edu:9000/updateDevice
    - **Semantics**: As a PUT method, the API cannot be directly executed through a web browser.  Instead, it may be executed through Rails, JQuery, Python, BASH, etc.
        - **deviceUri** (string, not null): Existing device URI.
        - **location**
            - **representation** (string): Location interpreter.
            - **latitude** (double): Latitude.
            - **longitude** (double): Longitude.
            - **altitude** (double): Altitude.
        - **deviceUserDefinedFields** (string): User defined fields. 
   - **Sample Usages**:
      - **Command Line Example**: 
          1. Prepare input device metadata in a json file:
              - "device.json" file contains: {"deviceUri": "fireimp", "location" : {"representation": "test location description", "latitude": 10, "longitude": 10, "altitude": 10}, "deviceUserDefinedFields" : "For production"}
          2. curl -X PUT -H "Content-Type: application/json" -d @device.json "http://einstein.sv.cmu.edu:9000/updateDevice"
      - **Result**: HTTP 200 if the device metadata have been successfully added to the database
      

31. <a name="31"></a>**GET ALL SENSOR CATEGORIES**
    - **Purpose**: Query all sensor categories.
    - **Method**: GET
    - **URL**: http://einstein.sv.cmu.edu:9000/getAllSensorCategories/<"resultFormat">
    - **Semantics**: 
        - **resultFormat**: Either JSON or CSV.
    - **Sample Usages**:  
      - **Sample csv request**: http://einstein.sv.cmu.edu:9000/getAllSensorCategories/csv<br/>
      - **Sample csv result**: (sensorCategoryName,purpose) </br>sensorCategory1, temp
      - **Sample json request**: http://einstein.sv.cmu.edu:9000/getAllSensorCategories/json
      - **Sample json result**: [{"sensorCategoryName":sensorCategory1,"purpose":"temp"}]
      - **Result**: HTTP 200 if successful, HTTP 404 if failed.
      
32. <a name="32"></a>**GET A SPECIFIC SENSOR CATEGORY**
    - **Purpose**: Query a specific sensor category.
    - **Method**: GET
    - **URL**: http://einstein.sv.cmu.edu:9000/getSensorCategory/<"sensorCategoryName">/<"resultFormat">
    - **Semantics**: 
        - **sensorCategoryName**: Sensor category name.
        - **resultFormat**: Either JSON or CSV.
    - **Sample Usages**: 
      - **Result**: HTTP 200 if the sensorCategoryName exists, HTTP 404 if not found
      - **Sample csv request**: http://einstein.sv.cmu.edu:9000/getSensorCategory/sensorCategory1/csv<br/>
      - **Sample csv result**: (sensorCategoryName,purpose) </br>sensorCategory1, temp
      - **Sample json request**: http://einstein.sv.cmu.edu:9000/getSensorCategory/sensorCategory1/json
      - **Sample json result**: {"sensorCategoryName":sensorCategory1,"purpose":"temp"}
      - **Result**: HTTP 200 if successful, HTTP 404 if failed.
      
33. <a name="33"></a>**GET ALL SENSOR TYPES**
    - **Purpose**: Query all sensor types.
    - **Method**: GET
    - **URL**: http://einstein.sv.cmu.edu:9000/getAllSensorTypes/<"resultFormat">
    - **Semantics**: 
        - **resultFormat**: Either JSON or CSV.
    - **Sample Usages**: 
      - **Sample csv request**: http://einstein.sv.cmu.edu:9000/getAllSensorTypes/csv<br/>
      - **Sample csv result**: (sensorTypeName, manufacturer,version,maximumValue,minimumValue,unit,interpreter,sensorTypeUserDefinedFields, sensorCategoryName) </br>Humidity, Motorola, 1.0, 100, 0, Percentage, MyInterpreter, Testing only, Environment
      - **Sample json request**: http://einstein.sv.cmu.edu:9000/getAllSensorTypes/json
      - **Sample json result**: [{"sensorTypeName": "Humidity", "manufacturer": "Motorola", "version": "1.0", "maximumValue": 100, "minimumValue": 0, "unit": "Percentage", "interpreter": "MyInterpreter", "sensorTypeUserDefinedFields": "Testing only", "sensorCategoryName": "Environment"}]
      - **Result**: HTTP 200 if successful, HTTP 404 if failed.

34. <a name="34"></a>**GET A SPECIFIC SENSOR TYPE**
    - **Purpose**: Query a specific sensor type.
    - **Method**: GET
    - **URL**: http://einstein.sv.cmu.edu:9000/getSensorType/<"sensorTypeName">/<"resultFormat">
    - **Semantics**: 
        - **sensorTypeName**" Sensor type name
        - **resultFormat**: Either JSON or CSV.
    - **Sample Usages**:  
      - **Sample csv request**: http://einstein.sv.cmu.edu:9000/getSensorType/Humidity/csv<br/>
      - **Sample csv result**: (sensorTypeName, manufacturer,version,maximumValue,minimumValue,unit,interpreter,sensorTypeUserDefinedFields, sensorCategoryName) </br>Humidity, Motorola, 1.0, 100, 0, Percentage, MyInterpreter, Testing only, Environment
      - **Sample json request**: http://einstein.sv.cmu.edu:9000/getSensorType/Humidity/json
      - **Sample json result**: {"sensorTypeName": "Humidity", "manufacturer": "Motorola", "version": "1.0", "maximumValue": 100, "minimumValue": 0, "unit": "Percentage", "interpreter": "MyInterpreter", "sensorTypeUserDefinedFields": "Testing only", "sensorCategoryName": "Environment"}
      - **Result**: HTTP 200 if successful, HTTP 404 if failed.
    

35. <a name="35"></a>**GET ALL SENSORS**
    - **Purpose**: Query all sensors.
    - **Method**: GET
    - **URL**: http://einstein.sv.cmu.edu:9000/getAllSensors/<"resultFormat">
    - **Semantics**: 
        - **resultFormat**: Either JSON or CSV.
    - **Sample Usages**: 
      - **Sample csv request**: http://einstein.sv.cmu.edu:9000/getAllSensors/csv<br/>
      - **Sample csv result**: (sensorName, sensorUserDefinedFields, deviceUri, sensorTypeName, manufacturer,version,maximumValue,minimumValue,unit,interpreter,sensorTypeUserDefinedFields, sensorCategoryName) </br>sensor01, for test, www.device.com/001, Humidity, Motorola, 1.0, 100, 0, Percentage, MyInterpreter, Testing only, Environment, test only
      - **Sample json request**: http://einstein.sv.cmu.edu:9000/getAllSensors/json
      - **Sample json result**: [{"sensorName": "sensor01", "sensorUserDefinedFields": "for test", "deviceUri": "www.device.com/001","sensorTypeName": "Humidity", "manufacturer": "Motorola", "version": "1.0", "maximumValue": 100, "minimumValue": 0, "unit": "Percentage", "interpreter": "MyInterpreter", "sensorTypeUserDefinedFields": "Testing only", "sensorCategoryName": "Environment", "purpose": "test only"}]
      - **Result**: HTTP 200 if successful, HTTP 404 if failed.

28. <a name="28"></a>**GET ALL SENSORS (REDUCED)**
    - **Purpose**: Query all sensors and returns a subset of the information (for faster performance).
    - **Method**: GET
    - **URL**: http://einstein.sv.cmu.edu:9000/getAllSensorsReduced/<"resultFormat">
    - **Semantics**: 
        - **resultFormat**: Either JSON or CSV.
    - **Sample Usages**: 
      TODO

36. <a name="36"></a>**GET A SPECIFIC SENSOR**
    - **Purpose**: Query a specific sensor.
    - **Method**: GET
    - **URL**: http://einstein.sv.cmu.edu:9000/getSensor/<"sensorName">/<"resultFormat">
    - **Semantics**: 
        - **sensorName**: Sensor name
        - **resultFormat**: Either JSON or CSV.
    - **Sample Usages**:  
      - **Result**: HTTP 200 if the sensorName exists, HTTP 404 if not found
      - **Sample csv request**: http://einstein.sv.cmu.edu:9000/getSensor/<"sensorName">/csv<br/>
      - **Sample csv result**: (sensorName, sensorUserDefinedFields, deviceUri, sensorTypeName, manufacturer,version,maxValue,minValue,unit,interpreter,sensorTypeUserDefinedFields, sensorCategoryName) </br>sensor1, for test, www.device.com, Humidity, Motorola, 1.0, 100, 0, Percentage, MyInterpreter, Testing only, Environment
      - **Sample json request**: http://einstein.sv.cmu.edu:9000/getSensor/sensor1/json
      - **Sample json result**: {"sensorName": "sensor1", "sensorUserDefinedFields": "for test", "deviceUri":"www.device.com", "sensorTypeName": "Humidity", "manufacturer": "Motorola", "version": "1.0", "maximumValue": 100, "minimumValue": 0, "unit": "Percentage", "interpreter": "MyInterpreter", "sensorTypeUserDefinedFields": "Testing only", "sensorCategoryName": "Environment"}
      - **Result**: HTTP 200 if successful, HTTP 404 if failed.


      
37. <a name="37"></a>**GET ALL DEVICE TYPES**
    - **Purpose**: Query all device types.
    - **Method**: GET
    - **URL**: http://einstein.sv.cmu.edu:9000/getAllDeviceTypes/<"resultFormat">
    - **Semantics**: 
        - **resultFormat**: Either JSON or CSV.
    - **Sample Usages**: 
      - **Sample csv request**: http://einstein.sv.cmu.edu:9000/getAllDeviceTypes/csv<br/>
      - **Sample csv result**: (deviceTypeName,manufacturer,version,deviceTypeUserDefinedFields,sensorTypeNames) </br>device type 1, TI, 1.0, For Test, "[temp, light]"
      - **Sample json request**: http://einstein.sv.cmu.edu:9000/getAllDeviceTypes/json
      - **Sample json result**: [{"deviceTypeName": "device type  1", "manufacturer": "TI", "version": "1.0", "deviceTypeUserDefinedFields": "For test", "sensorTypeNames":["temp", "light"]}]
      - **Result**: HTTP 200 if successful, HTTP 404 if failed.


      
38. <a name="38"></a>**GET A SPECIFIC DEVICE TYPE**
    - **Purpose**: Query a specific device type.
    - **Method**: GET
    - **URL**: http://einstein.sv.cmu.edu:9000/getDeviceType/<"deviceTypeName">/<"resultFormat">
    - **Semantics**: 
        - **deviceTypeName**: Device type name.
        - **resultFormat**: Either JSON or CSV.
    - **Sample Usages**:  
      - **Result**: HTTP 200 if the deviceTypeName exists, HTTP 404 if not found
      - **Sample csv request**: http://einstein.sv.cmu.edu:9000/getDeviceType/device type 1/csv<br/>
      - **Sample csv result**: (deviceTypeName,manufacturer,version,deviceTypeUserDefinedFields,sensorTypeNames) </br>device 1, TI, 1.0, For Test, "[temp, light]"
      - **Sample json request**: http://einstein.sv.cmu.edu:9000/getDeviceType/<"deviceTypeName">/json
      - **Sample json result**: {"deviceTypeName": "device type  1", "manufacturer": "TI", "version": "1.0", "deviceTypeUserDefinedFields": "For test", "sensorTypeNames":["temp", "light"]}
      


39. <a name="39"></a>**GET ALL DEVICES**
    - **Purpose**: Query all devices.
    - **Method**: GET
    - **URL**: http://einstein.sv.cmu.edu:9000/getAllDevices/<"resultFormat">
    - **Semantics**: 
        - **resultFormat**: Either JSON or CSV.
    - **Sample Usages**: 
      - **Sample csv request**: http://einstein.sv.cmu.edu:9000/getAllDevices/csv<br/>
      - **Sample csv result**: (deviceUri, deviceUserDefinedFields, longitude, latitude, altitude, representation, deviceTypeName,manufacturer,version,deviceTypeUserDefinedFields,sensorTypeNames, sensorNames) </br>www.device.com, For test, 10, 10, 10, myInterpreter, device type 1, TI, 1.0, For Test, "[temp, light]", "[sensor1, sensor2]"
      - **Sample json request**: http://einstein.sv.cmu.edu:9000/getAllDevices/json
      - **Sample json result**: [{"deviceUri":"www.device.com", "deviceUserDefinedFields": "For test", "longitude":10, "latitude": 10, "altitude":10, "representation": "myInterpreter", "deviceTypeName": "device type  1", "manufacturer": "TI", "version": "1.0", "deviceTypeUserDefinedFields": "For test", "sensorTypeNames":["temp", "light"], "sensorNames":["sensor1", "sensor2"]}]
      - **Result**: HTTP 200 if successful, HTTP 404 if failed.

30. <a name="30"></a>**GET DEVICES INSIDE A SPECIFIC GEO-FENCE**
    - **Purpose**: Query devices by specifying the geo-fence.
    - **Method**: GET
    - **URL**: http://einstein.sv.cmu.edu:9000/getDevicesByGeofence/<"geo-fence">/<"resultFormat">
    - **Semantics**: 
        - **geo-fence**: The location representation of the device.
        - **resultFormat**: Either JSON or CSV.
    - **Sample Usages**: 
      - **Sample csv request**: http://einstein.sv.cmu.edu:9000/getDevicesByGeofence/room129A/csv<br/>
      - **Sample csv result**: (deviceUri, deviceUserDefinedFields, longitude, latitude, altitude, representation, deviceTypeName,manufacturer,version,deviceTypeUserDefinedFields,sensorTypeNames, sensorNames) </br>www.device.com, For test, 10, 10, 10, myInterpreter, device type 1, TI, 1.0, For Test, "[temp, light]", "[sensor1, sensor2]"
      - **Sample json request**: http://einstein.sv.cmu.edu:9000/getDevicesByGeofence/room129A/json
      - **Sample json result**: [{"deviceUri":"www.device.com", "deviceUserDefinedFields": "For test", "longitude":10, "latitude": 10, "altitude":10, "representation": "myInterpreter", "deviceTypeName": "device type  1", "manufacturer": "TI", "version": "1.0", "deviceTypeUserDefinedFields": "For test", "sensorTypeNames":["temp", "light"], "sensorNames":["sensor1", "sensor2"]}]
      - **Result**: HTTP 200 if successful, HTTP 404 if failed.


40. <a name="40"></a>**GET A SPECIFIC DEVICE**
    - **Purpose**: Query a specific device.
    - **Method**: GET
    - **URL**: http://einstein.sv.cmu.edu:9000/getDevice/<"deviceUri">/<"resultFormat">
    - **Semantics**: 
        - **resultFormat**: Either JSON or CSV.
    - **Sample Usages**:  
      - **Sample csv request**: http://einstein.sv.cmu.edu:9000/getDevice/www.device.com/csv<br/>
      - **Sample csv result**: (deviceUri, deviceUserDefinedFields, longitude, latitude, altitude, locationInterpreter, deviceTypeName,manufacturer,version,deviceTypeUserDefinedFields,sensorTypeNames, sensorNames) </br>www.device.com, For test, 10, 10, 10, myInterpreter, device type 1, TI, 1.0, For Test, "[temp, light]", "[sensor1, sensor2]"
      - **Sample json request**: http://einstein.sv.cmu.edu:9000/getDevice/www.device.com/json
      - **Sample json result**: {"deviceUri":"www.device.com", "deviceUserDefinedFields": "For test", "longitude":10, "latitude": 10, "altitude":10, "locationInterpreter": "myInterpreter", "deviceTypeName": "device type  1", "manufacturer": "TI", "version": "1.0", "deviceTypeUserDefinedFields": "For test", "sensorTypeNames":["temp", "light"], "sensorNames":["sensor1", "sensor2"]}
      - **Result**: HTTP 200 if successful, HTTP 404 if failed.

41. <a name="24"></a>**DELETE SENSOR CATEGORY**
    - **Purpose**: Delete a sensor category from sensor data service platform.
    - **Method**: DELETE
    - **URL**: http://einstein.sv.cmu.edu:9000/deleteSensorCategory/<"sensorCategoryName">
    - **Semantics**
        - **sensorCategoryName** (string, not null): Name of the sensor category
    - **Sample Usages**:
      - **Command Line Example**: 
          1. curl -X DELETE http://localhost:9000/deleteSensorCategory/testSensorCategoryName
      - **Result**: HTTP 201 if the sensor category metadata has been successfully deleted from the database

42. <a name="16"></a>**DELETE SENSOR TYPE**
    - **Purpose**: Delete a sensor type from sensor data service platform.
    - **Method**: DELETE
    - **URL**: http://einstein.sv.cmu.edu:9000/deleteSensorType/<"sensorTypeName">
    - **Semantics**
        - **sensorTypeName** (string, not null): Name of the sensor type
    - **Sample Usages**:
      - **Command Line Example**: 
          1. curl -X DELETE http://localhost:9000/deleteSensorType/testSensorTypeName
      - **Result**: HTTP 201 if the sensor type metadata has been successfully deleted from the database

43. <a name="17"></a>**DELETE SENSOR**
    - **Purpose**: Delete a sensor from sensor data service platform.
    - **Method**: DELETE
    - **URL**: http://einstein.sv.cmu.edu:9000/deleteSensor/<"sensorName">
    - **Semantics**
        - **sensorName** (string, not null): Name of the sensor
    - **Sample Usages**:
      - **Command Line Example**: 
          1. curl -X DELETE http://localhost:9000/deleteSensor/testSensorName
      - **Result**: HTTP 201 if the sensor metadata has been successfully deleted from the database
      

41. <a name="41"></a>**ADD USER**
    - **Purpose**: Add a new user to sensor data service platform.
    - **Method**: POST
    - **URL**: http://einstein.sv.cmu.edu:9000/addUser
    - **Semantics**: As a POST method, the API cannot be directly executed through a web browser.  Instead, it may be executed through Rails, JQuery, Python, BASH, etc.
        - **userName** (string, not null): Non existing unique user name
        - **userProfile** (string, optional): User profile
    - **Sample Usages**:
      - **Command Line Example**: 
          1. Prepare input sensor type metadata in a json file:
              - "user.json" file contains: {"userName": "John", "userProfile": "CMU student"}
          2. curl -H "Content-Type: application/json" -d @user.json "http://einstein.sv.cmu.edu:9000/addUser"
      - **Result**: HTTP 201 if the user has been successfully added to the database, HTTP 400 if the userName is already been used

42. <a name="42"></a>**GET A USER INFORMATION**
    - **Purpose**: Query a specific user.
    - **Method**: GET
    - **URL**: http://einstein.sv.cmu.edu:9000/getUser/<"userName">/<"resultFormat">
    - **Semantics**: 
        - **userName**: Existing user name.
        - **resultFormat**: Either JSON or CSV.
    - **Sample Usages**: 
      - **Sample csv request**: http://einstein.sv.cmu.edu:9000/getUser/John/csv<br/>
      - **Sample csv result**: (userName,userProfile) </br>John, CMU student
      - **Sample json request**: http://einstein.sv.cmu.edu:9000/getUser/John/json
      - **Sample json result**: {"userName":John,"userProfile":"CMU student"}
      - **Result**: HTTP 200 if successful, HTTP 404 if failed.

43. <a name="43"></a>**ADD SENSOR AS A REGISTERD USER**
    - **Purpose**: Add a new sensor as a registered user to sensor data service platform.
    - **Method**: POST
    - **URL**: http://einstein.sv.cmu.edu:9000/addSensor
    - **Semantics**: As a POST method, the API cannot be directly executed through a web browser.  Instead, it may be executed through Rails, JQuery, Python, BASH, etc.
        - **sensorName** (string, not null): Non existing unique name of the sensor
        - **sensorTypeName** (string, not null): Existing name of its sensor type
        - **deviceUri** (string, not null): Existing device URI it belongs to
        - **sensorUserDefinedFields** (string, optional): User defined fields.
        - **userName**(string, not null): Existing user name
    - **Sample Usages**:
      - **Command Line Example**: 
          1. Prepare input sensor metadata in a json file:
              - "sensor.json" file contains: {"sensorName": "TestSensor", "sensorTypeName": "Humidity", "deviceUri": "www.testsensor.com", "sensorUserDefinedFields": "Test only", "userName":"John"}
          2. curl -H "Content-Type: application/json" -d @sensor.json "http://einstein.sv.cmu.edu:9000/addSensor"
      - **Result**: HTTP 201 if the sensor metadata have been successfully added to the database, HTTP 400 if failed.

44. <a name="44"></a>**GET ALL SENSORS AS A REGISTERED USER**
    - **Purpose**: Query all sensors which has been added by a registered user.
    - **Method**: GET(Specify user name in request header)
    - **URL**: http://einstein.sv.cmu.edu:9000/getAllSensors/<"resultFormat">
    - **Semantics**: 
        - **resultFormat**: Either JSON or CSV.
    - **Sample Usages**: 
      - **Sample csv request**: curl -H "Authorization:John" http://einstein.sv.cmu.edu:9000/getAllSensors/csv<br/>
      - **Sample csv result**: (sensorName, sensorUserDefinedFields, deviceUri, sensorTypeName, manufacturer,version,maximumValue,minimumValue,unit,interpreter,sensorTypeUserDefinedFields, sensorCategoryName) </br>sensor01, for test, www.device.com/001, Humidity, Motorola, 1.0, 100, 0, Percentage, MyInterpreter, Testing only, Environment, test only
      - **Sample json request**: curl -H "Authorization:John" http://einstein.sv.cmu.edu:9000/getAllSensorTypes/json
      - **Sample json result**: [{"sensorName": "sensor01", "sensorUserDefinedFields": "for test", "deviceUri": "www.device.com/001","sensorTypeName": "Humidity", "manufacturer": "Motorola", "version": "1.0", "maximumValue": 100, "minimumValue": 0, "unit": "Percentage", "interpreter": "MyInterpreter", "sensorTypeUserDefinedFields": "Testing only", "sensorCategoryName": "Environment", "purpose": "test only"}]
      - **Result**: HTTP 200 if successful, HTTP 404 if failed.


45. <a name="45"></a>**GET A SPECIFIC SENSOR AS A REGISTERED USER**
    - **Purpose**: Query a specific sensor which has been added by a registered user.
    - **Method**: GET (Specify user name in request header)
    - **URL**: http://einstein.sv.cmu.edu:9000/getSensor/<"sensorName">/<"resultFormat">
    - **Semantics**: 
        - **sensorName**: Sensor name
        - **resultFormat**: Either JSON or CSV.
    - **Sample Usages**:  
      - **Sample csv request**: curl -H "Authorization:John" http://einstein.sv.cmu.edu:9000/getSensor/<"sensorName">/csv<br/>
      - **Sample csv result**: (sensorName, sensorUserDefinedFields, deviceUri, sensorTypeName, manufacturer,version,maxValue,minValue,unit,interpreter,sensorTypeUserDefinedFields, sensorCategoryName) </br>sensor1, for test, www.device.com, Humidity, Motorola, 1.0, 100, 0, Percentage, MyInterpreter, Testing only, Environment
      - **Sample json request**: curl -H "Authorization:John" http://einstein.sv.cmu.edu:9000/getSensor/sensor1/json
      - **Sample json result**: {"sensorName": "sensor1", "sensorUserDefinedFields": "for test", "deviceUri":"www.device.com", "sensorTypeName": "Humidity", "manufacturer": "Motorola", "version": "1.0", "maximumValue": 100, "minimumValue": 0, "unit": "Percentage", "interpreter": "MyInterpreter", "sensorTypeUserDefinedFields": "Testing only", "sensorCategoryName": "Environment"}
      - **Result**: HTTP 200 if successful, HTTP 404 if failed.


51. <a name="51"></a>**ADD A CONTEST USER**
    - **Purpose**: Add a new contest user.
    - **Method**: POST
    - **URL**: http://einstein.sv.cmu.edu:9000/addContestUser
    - **Semantics**: As a POST method, the API cannot be directly executed through a web browser.  Instead, it may be executed through Rails, JQuery, Python, BASH, etc.
        - **userName** (string, not null): Non existing unique user name
        - **password** (string, not null): password
        - **firstName** (string, optional): first name
        - **lastName** (string, optional): last name
        - **middleName** (string, optional): middle name
        - **affiliation** (string, optional): affiliation
        - **email** (string, optional): email
        - **researchArea** (string, optional): research area
        - **goal** (string, optional): goal
    - **Sample Usages**:
      - **Command Line Example**: 
          1. Prepare input contest user data in a json file:
              - "contestUser.json" file contains: {"userName": "John", "password": "123", "firstName": "John"}
          2. curl -H "Content-Type: application/json" -d @user.json "http://einstein.sv.cmu.edu:9000/addContestUser"
      - **Result**: HTTP 201 if the user has been successfully added to the database, HTTP 400 if the userName is already been used or register limit has been reached.
      
52. <a name="52"></a>**UPDATE A CONTEST USER**
    - **Purpose**: Update a new contest user.
    - **Method**: POST
    - **URL**: http://einstein.sv.cmu.edu:9000/updateontestUser
    - **Semantics**: As a POST method, the API cannot be directly executed through a web browser.  Instead, it may be executed through Rails, JQuery, Python, BASH, etc.
        - **userName** (string, not null): Non existing unique user name
        - **password** (string, not null): password
        - **firstName** (string, optional): first name
        - **lastName** (string, optional): last name
        - **middleName** (string, optional): middle name
        - **affiliation** (string, optional): affiliation
        - **email** (string, optional): email
        - **researchArea** (string, optional): research area
        - **goal** (string, optional): goal
    - **Sample Usages**:
      - **Command Line Example**: 
          1. Prepare input contest user data in a json file:
              - "contestUser.json" file contains: {"userName": "John", "password": "123", "firstName": "John", "lastName": "White"}
          2. curl -H "Content-Type: application/json" -d @user.json "http://einstein.sv.cmu.edu:9000/updateContestUser"
      - **Result**: HTTP 201 if the user has been successfully updated, HTTP 400 if the userName/password is wrong
      
53. <a name="53"></a>**DELETE A CONTEST USER**
    - **Purpose**: Delete a contest user.
    - **Method**: DELETE
    - **URL**: http://einstein.sv.cmu.edu:9000/deleteContestUser/<"userName">/<"password">
    - **Semantics**
        - **userName** (string, not null): Contest user name
        - **password** (string, not null): Password
    - **Sample Usages**:
      - **Command Line Example**: 
          1. curl -X DELETE http://localhost:9000/deleteContestUser/John/123
      - **Result**: HTTP 201 if the contest user has been successfully deleted

54. <a name="54"></a>**GET ALL REGISTERED CONTEST USERS**
    - **Purpose**: Query all registered contest users.
    - **Method**: GET
    - **URL**: http://einstein.sv.cmu.edu:9000/getAllContestUsers/<"resultFormat">
    - **Semantics**: 
        - **resultFormat**: Either JSON or CSV.
    - **Sample Usages**: 
      - **Sample csv request**: http://einstein.sv.cmu.edu:9000/getAllContestUsers/csv<br/>
      - **Sample json request**: http://einstein.sv.cmu.edu:9000/getAllContestUsers/json
      - **Result**: HTTP 200 if successful, HTTP 404 if failed.

55. <a name="55"></a>**GET SPECIFIC REGISTERED CONTEST USER**
    - **Purpose**: Query specific registered contest user.
    - **Method**: GET
    - **URL**: http://einstein.sv.cmu.edu:9000/getContestUser/<"userName">/<"password">/<"resultFormat">
    - **Semantics**: 
        - **resultFormat**: Either JSON or CSV.
    - **Sample Usages**: 
      - **Sample csv request**: http://einstein.sv.cmu.edu:9000/getContestUser/John/123/csv<br/>
      - **Sample json request**: http://einstein.sv.cmu.edu:9000/getContestUser/John/123/json
      - **Result**: HTTP 200 if successful, HTTP 404 if failed.

[1]: http://einstein.sv.cmu.edu:9000/ "The Application Server running in the Smart Spaces Lab, CMUSV"

Examples:
----------------
1. Consume Rest API in Python
    - GET
    <pre>
      <code>
         import json, requests
         response = requests.get("http://einstein.sv.cmu.edu:9000/get_devices/json")
         print(response.json())
      </code>
    </pre>
    - POST
    <pre>
      <code>
         import requests
         requests.post("http://einstein.sv.cmu.edu:9000/sensors", data={}, headers={}, files={}, cookies=None, auth=None)
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
         		String URLStr = "http://einstein.sv.cmu.edu:9000/sensors";
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

