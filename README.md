CMU Sensor Service Platform
============

Service URL:
--------

[http://cmu-sensor-network.herokuapp.com][1]

Usage:
------

Note: all TimeStamps are in Unix epoch time format to millisecond

- **Add sensor reading**
    - **Method**: POST
    - **URL**: http://cmu-sensor-network.herokuapp.com/sensors/
    - **Data**: {"id": <"device id in string">, "timestamp": <"timestamp in int">, <"sensor type in string">: <"sensor value in double">} 
        <br/> Note: more than one sensor type:sensor value pairs can be included in the json.
    - **Example**: Post this json-formatted reading data to the URL - {"id":"test", "timestamp": 1373566898000, "temp": 123}
    - **Result**: saved

- **Get all devices**
    - **Method**: GET
    - **URL (return csv format)**: http://cmu-sensor-network.herokuapp.com/get_devices
    - **URL (return json format)**: http://cmu-sensor-network.herokuapp.com/get_devices/json
    - **Sample result (in csv)**: <br/>
        uri,device_type,device_agent,device_location <br/>
        10170202,Firefly_v3,SensorAndrew2,B23.216 <br/>
        (uri is the device id)
    - **Sample result (in json)**: {"device_type":"Firefly_v3","device_location":"B23.216","device_agent":"SensorAndrew2","uri":"10170202"}

- **Get Sensor Reading at a specific time**
    - **Method**: GET
    - **URL (return csv format)**: http://cmu-sensor-network.herokuapp.com/sensors/<"DeviceID">/<"TimeStampValue">/<"SensorTypeValue">
    - **URL (return json format)**: http://cmu-sensor-network.herokuapp.com/sensors/<"DeviceID">/<"TimeStamp">/<"SensorType">/json
    - **Sample request**: http://cmu-sensor-network.herokuapp.com/sensors/10170102/1368568896000/temp
    <br/>("temp" stands for temperature sensor)
    - **Sample result**: 10170102,1368568896000,temp,518.0

- **Get Sensor Readings in a time range**
    - **Method**: GET
    - **URL (return csv format)**: http://cmu-sensor-network.herokuapp.com/sensors/<"DeviceID">/<"StartTime">/<"EndTime">/<"SensorType">
    - **URL (return json format)**: http://cmu-sensor-network.herokuapp.com/sensors/<"DeviceID">/<"StartTime">/<"EndTime">/<"SensorType">/json
    - **Sample request**: http://cmu-sensor-network.herokuapp.com/sensors/10170102/1368568896000/1368568996000/temp
    - **Sample result**: <br/>
        10170102,1368568993000,temp,517.0 <br/>
        ... <br/>
        10170102,1368568896000,temp,518.0

- **Get the last readings before a specific time from all devices**
    - **Method**: GET
    - **URL(return csv format)**: http://cmu-sensor-network.herokuapp.com/last_readings_from_all_devices/<"TimeStamp">/<"sensorType">
    - **URL(return json format)**: http://cmu-sensor-network.herokuapp.com/last_readings_from_all_devices/<"TimeStamp">/<"sensorType">/json
    - **Sample request**: http://cmu-sensor-network.herokuapp.com/last_readings_from_all_devices/1368568896000/temp      
    - **Sample result**: <br/>
        10170203,1368568896000,temp,513.0 <br/>
        ...<br/>
        10170204,1368568889000,temp,513.0

[1]: http://cmu-sensor-network.herokuapp.com/ "heroku"

