CMU Sensor Service Platform
============

- built on Java play framework

- models package
    - defines the underlying framework of the classes so the user can see how the data is represented
      within the Java code
    
- controller package
    - defines logic as to how requests are handled via the web service and what results will be
      returned
    - calls the corresponding view that renders the data for the user to visualize
    - user uses controllers package to interact with the backend

**Note: all TimeStamps must be converted via unix epoch time to millisecond**

- Add Sensor Reading:
    - SensorReading.java is a class that provides users of this service with an efficient way of
      storing detailed data sets in the backend of this service. When a user wants to post a 
      SensorReading, he/she just has to use the url listed a few lines above, and the code
      will handle the rest
