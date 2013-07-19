HANAPlatform
============

SAP HANA-based Sensor Data and Service Platform


Service URL:
--------

[http://cmu-sensor-network.herokuapp.com][1]

Usage:
------

Note: all TimeStamps are in Unix epoch time format to millisecond

- **Get all devices**
    - **Method**: GET
    - **Semantics**: entering the following requests into your browser returns a list of all the
        - **uri**: user-defined identifier for a device. Each uri is an identifier unique to the
          corresponding device
        - **device_type**: the official "brand" of the device. A device is a container (i.e., physical device) object 
          that comprises one or more sensors and is capable of transmitting their readings over a network to a Device Agent.
        - **device_agent**: a local server or proxy that manages a set of devices registered to it. Device agents 
          can receive data from devices, convert data to another format (eg. JSON), and can transmit it to central 
          server over a LAN or WAN. 
        - **device_location**: the location of the device that is transmitting sensor data

    - **URL (return csv format)**: http://cmu-sensor-network.herokuapp.com/get_devices
    - **URL (return json format)**: http://cmu-sensor-network.herokuapp.com/get_devices/json
    - **Sample result (in csv)**: <br/>
        uri,device_type,device_agent,device_location <br/>
        10170202,Firefly_v3,SensorAndrew2,B23.216 <br/>
    - **Sample result (in json)**: {"device_type":"Firefly_v3","device_location":"B23.216","device_agent":"SensorAndrew2","uri":"10170202"}

- **Add sensor readings**
    - **Method**: POST
    - **Semantics**: this is a POST method, so the command cannot be directly execute through the
      browser.  It may be executed through JQuery, Python, BASH, etc.
        - **"id"**: this input is equivalent to the uri/device id
        - **"timestamp"**: this input corresponds to the time pertaining to the data you'd
          like to add. It is in epoch time - a conversion can be executed from GMT to epoch time
          via numerous web services, kue.g. http://www.epochconverter.com
        - **sensor type**: this input corresponds to the type of data the sensor is transmitting,
          such as temperature, CO2Levels, etc.  It is up to the user to choose the sensor type to
          post to.
        - **sensor value**: this input corresponds to the value the user would like to post. It is
          up to the user to post the correct, pertinent value that correctly corresponds to the sensor type.
    - **URL**: http://cmu-sensor-network.herokuapp.com/sensors
    - **Data**: {"id": <"device id in string">, "timestamp": <"timestamp in int">, <"sensor type in string">: <"sensor value in double">} 
        <br/> Note: more than one sensor type:sensor value pairs can be included in the json.
    - **Command Line Example**: 
        1. input data into .json file
            - sample_reading.json contains {"id":"test", "timestamp": 1373566899100, "temp": 123}
        2. curl -H "Content-Type: application/json" -d @sample_reading.json "http://cmu-sensor-network.herokuapp.com/sensors/"
    - **Command Line Result**: the contents of sample_reading.json have been pushed to "http://cmu-sensor-network.herokuapp.com/sensors/"

- **Get sensor readings at a specific time**
    - **Method**: GET
    - **Semantics**: this is a GET method, so the following may be executed through a browser
        - **Device ID**: this input is equivalent to the device uri/device's unique identifier and is to be formatted as a String of   
          numbers
        - **Timestamp**: this input is equivalent to the time at which the readings were recorded at. The timestamp is in UNIX's 
          epoch time. http://www.epochconverter.com can convert GMT time to epoch time.
        - **Sensor Type**: this input is equivalent to the type of sensor (temperature, CO2, etc.) that the data is coming from.
    - **URL (return csv format)**: http://cmu-sensor-network.herokuapp.com/sensors/<"DeviceID">/<"TimeStamp">/<"SensorType">
    - **URL (return json format)**: http://cmu-sensor-network.herokuapp.com/sensors/<"DeviceID">/<"TimeStamp">/<"SensorType">/json
    - **Sample csv request**: http://cmu-sensor-network.herokuapp.com/sensors/10170102/1368568896000/temp
    <br/>("temp" stands for temperature sensor)
    - **Sample csv result**: 10170102,1368568896000,temp,518.0
    - **Sample json request**: http://cmu-sensor-network.herokuapp.com/sensors/10170102/1368568896000/temp/json
    - **Sample json result**: {"timestamp":1368568896000,"sensor_type":"temp","value":518,"device_id":"10170102"}

- **Get sensor readings in a time range**
    - **Method**: GET
    - **Semantics**: this is a GET method, so the following may be executed through a browser
        - **Device ID**: this input is equivalent to the uri/device's unique identifier and is to be formatted as a String of numbers
        - **Start time**: this input is equivalent to the start time of the retrieved readings. The start time is in UNIX's epoch time.
          http://www.epochconverter.com/ can convert GMT time to epoch time
        - **End time**: this input is equivalent to the end time of the retrieved readings. The end time is in UNIX's epoch time.
        - **Sensor Type**: this input is equivalent to the type of sensor ("temperature, CO2, etc.") that the data is coming from.
    - **URL (return csv format)**: http://cmu-sensor-network.herokuapp.com/sensors/<"DeviceID">/<"StartTime">/<"EndTime">/<"SensorType">
    - **URL (return json format)**: http://cmu-sensor-network.herokuapp.com/sensors/<"DeviceID">/<"StartTime">/<"EndTime">/<"SensorType">/json
    - **Sample csv request**: http://cmu-sensor-network.herokuapp.com/sensors/10170102/1368568896000/1368568996000/temp
    - **Sample result**: <br/>
        10170102,1368568993000,temp,517.0 <br/>
        ... <br/>
        10170102,1368568896000,temp,518.0
    - **Sample json request**: http://cmu-sensor-network.herokuapp.com/sensors/10170102/1368568896000/1368568996000/temp/json
    - **Sample result**: <br/>
        [{"timestamp":1368568993000,"sensor_type":"temp","value":517,"device_id":"10170102"},
        ... <br/>
        {"timestamp":1368568896000,"sensor_type":"temp","value":518,"device_id":"10170102"}]

- **Get the latest readings at specific time from all devices**
    - **Method**: GET
    - **Semantics**: this is a GET method, so the following may be executed through a browser
        - **Time Stamp**: this input is equivalent to the time at which the retrieved data was recorded. It is to be entered in 
          UNIX's epoch time. http://www.epochconverter.com/ can convert GMT time to epoch time.
        - **Sensor Type**: this input is equivalent to the type of sensor (Temperature, CO2, etc.) that recorded the data retrieved.
    - **URL(return csv format)**: http://cmu-sensor-network.herokuapp.com/last_readings_from_all_devices/<"TimeStamp">/<"sensorType">
    - **URL(return json format)**: http://cmu-sensor-network.herokuapp.com/last_readings_from_all_devices/<"TimeStamp">/<"sensorType">/json
    - **Sample csv request**: http://cmu-sensor-network.herokuapp.com/last_readings_from_all_devices/1368568896000/temp      
    - **Sample csv result**: <br/>
        10170203,1368568896000,temp,513.0 <br/>
        ... <br/>
        10170204,1368568889000,temp,513.0
    - **Sample json request**: http://cmu-sensor-network.herokuapp.com/last_readings_from_all_devices/1368568896000/temp/json
    - **Sample json result**: <br/>
        [{"timestamp":1368568896000,"sensor_type":"temp","value":513,"device_id":"10170203"},
        ... <br/>
        {"timestamp":1368568889000,"sensor_type":"temp","value":513,"device_id":"10170204"}]

[1]: http://cmu-sensor-network.herokuapp.com/ "heroku"

