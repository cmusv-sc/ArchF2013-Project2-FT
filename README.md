CMUSensorNetwork
============

Service:
--------

[http://cmu-sensor-network.herokuapp.com][1]

Usage:
------

- Add Reading:
    - **Method**: POST
    - **URL**: sensors/
    - **Data**: {deviceID: Int, timeStamp: Int, sensorType: String,  value: Double}

                  
- Get Reading 
    - **Method**: GET
    - **url**: sensors/DeviceID/TimeStamp


[1]: http://cmu-sensor-network.herokuapp.com/ "heroku"
