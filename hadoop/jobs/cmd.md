rm *.class *.jar

javac -classpath "${HBASE_HOME}/lib/*:${HBASE_HOME}/*" AverageValJob.java

jar -cvf averageVal.jar ./*.class

../../hadoop/bin/hadoop jar averageVal.jar AverageValJob
