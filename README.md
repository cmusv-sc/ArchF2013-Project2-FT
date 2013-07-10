CMUSensorNetwork
============

Service:
--------

[http://cmu-sensor-network.herokuapp.com][1]

Usage:
------

- Add Sensor Reading:
    - **Method**: POST
    - **URL**: sensors/
    - **Data**: {deviceID: Int, timeStamp: Int, sensorType: String,  value: Double}

- Get Device:
    - **Method**: GET
    - **URL (return csv format)**: get_devices/
    - **URL (return json format)**: get_devices/json

- Get Sensor Reading at specific timestamp 
    - **Method**: GET
    - **URL (return csv format)**: sensors/:DeviceID/:TimeStamp/:SensorType
    - **URL (return json format)**: sensors/:DeviceID/:TimeStamp/:SensorType/json

- Get Sensor Reading at a time range
    - **Method**: GET
    - **URL (return csv format)**: sensors/:DeviceID/:startTime/:endTime/:SensorType
    - **URL (return json format)**: sensors/:DeviceID/:startTime/:endTime/:SensorType/json

- Get the last reading before timestamp from all devices
    - **Method**: GET
    - **URL(return csv format)**: /last_readings_from_all_devices/:timeStamp/:sensorType
    - **URL(return json format)**: /last_readings_from_all_devices/:timeStamp/:sensorType/json

[1]: http://cmu-sensor-network.herokuapp.com/ "heroku"
