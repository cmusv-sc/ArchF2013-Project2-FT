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
    - **Data** (in json format): {deviceID: String, timeStamp: Int, sensorType: String,  value: Double}

- Get Device:
    - **Method**: GET
    - **URL (return csv format)**: get_devices/
    - **URL (return json format)**: get_devices/json

- Get Sensor Reading at a specific time
    - **Method**: GET
    - **URL (return csv format)**: sensors/:DeviceID/:TimeStamp/:SensorType
    - **URL (return json format)**: sensors/:DeviceID/:TimeStamp/:SensorType/json

    Note: TimeStamp is unix epoch time in millisecond

    e.g [http://cmu-sensor-network.herokuapp.com/sensors/10170102/1368568896000/temp]

- Get Sensor Readings in a time range
    - **Method**: GET
    - **URL (return csv format)**: sensors/:DeviceID/:StartTime/:EndTime/:SensorType
    - **URL (return json format)**: sensors/:DeviceID/:StartTime/:EndTime/:SensorType/json

- Get the last readings before a specific time from all devices
    - **Method**: GET
    - **URL(return csv format)**: /last_readings_from_all_devices/:TimeStamp/:sensorType
    - **URL(return json format)**: /last_readings_from_all_devices/:TimeStamp/:sensorType/json

[1]: http://cmu-sensor-network.herokuapp.com/ "heroku"
