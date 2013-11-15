insert into cmu.cmu_sensor values('device1', 1383927321, 'temp', 15);
insert into cmu.cmu_sensor values('device1', 1383927322, 'temp', 16);
insert into cmu.cmu_sensor values('device1', 1383927321, 'light', 200);
insert into cmu.cmu_sensor values('device1', 1383927322, 'light', 200);
insert into cmu.cmu_sensor values('device1', 1383927321, 'co2', 50);
insert into cmu.cmu_sensor values('device1', 1383927322, 'co2', 50);

insert into cmu.cmu_sensor values('device2', 1383927321, 'temp', 15);
insert into cmu.cmu_sensor values('device2', 1383927322, 'temp', 16);
insert into cmu.cmu_sensor values('device2', 1383927321, 'light', 200);
insert into cmu.cmu_sensor values('device2', 1383927322, 'light', 200);
insert into cmu.cmu_sensor values('device2', 1383927321, 'co2', 50);
insert into cmu.cmu_sensor values('device12', 1383927322, 'co2', 50);


insert into cmu.course_sensor_category values(next value for cmu.COURSE_SENSOR_CATEGORY_ID_SEQ, 'temp', 'indoor temprature');

insert into cmu.course_sensor_type values(next value for cmu.COURSE_SENSOR_TYPE_ID_SEQ, 'motorola temp', 'motorola', '0.1', 100, 1, 'Fahrenheit', null, 'anything', 1);

insert into cmu.course_sensor_type values(next value for cmu.COURSE_SENSOR_TYPE_ID_SEQ, 'texas instrument temp', 'motorola', '0.1', 100, 1, 'Fahrenheit', null, 'anything', 1);

insert into cmu.course_device_type values(next value for cmu.COURSE_DEVICE_TYPE_ID_SEQ, 'device_type1', 'motorola', '0.1', 'anything');

insert into cmu.course_device values(next value for cmu.COURSE_DEVICE_ID_SEQ, 1, 'devece1.sv.cmu.edu', current_timestamp, 'anything');

insert into cmu.course_sensor values(next value for cmu.COURSE_SENSOR_ID_SEQ, 1, 1, 'sensor1', 'anything');

insert into cmu.COURSE_DEVICE_TYPE_SENSOR_TYPE values(1, 1);

insert into cmu.COURSE_DEVICE_TYPE_SENSOR_TYPE values(1, 2);