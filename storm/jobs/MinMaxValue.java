package storm.jobs;
import java.util.HashMap;
import java.util.Map;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class MinMaxValue {

	public static class SensorDataSpout extends BaseRichSpout {
		SpoutOutputCollector _collector;
		SensorDataReader streamSensorDataReader;
		

		@Override
		public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
			_collector = collector;
		}

		public SensorDataReader getSensorDataReader() {
			return streamSensorDataReader;
		}

		public void setSensorDataReader(SensorDataReader sensorDataReader) {
			this.streamSensorDataReader = sensorDataReader;
		}

		@Override
		public void nextTuple() {
			if (streamSensorDataReader.hasNextSensorData()) {
				String sensorId = streamSensorDataReader.getNextSensorId();
				String sensorData = streamSensorDataReader.getNextSensorData();
				_collector.emit(new Values(sensorId, Double.valueOf(sensorData)));
			}
		}

		@Override
		public void declareOutputFields(OutputFieldsDeclarer declarer) {
			declarer.declare(new Fields("sensorId", "sensorData"));
		}

	}
	
	public static class MaxMinBolt extends BaseBasicBolt {
		Map<String, Double> maxMap = new HashMap<String, Double>();
		Map<String, Double> minMap = new HashMap<String, Double>();

		
		public void setBatchMap(Map<String, Double> batchMaxMap, Map<String, Double> batchMinMap) {
			this.maxMap = batchMaxMap;
			this.minMap = batchMinMap;
		}

		@Override
		public void execute(Tuple tuple, BasicOutputCollector collector) {
			String sensorId = tuple.getString(0);
			Double newVal = Double.valueOf(tuple.getString(1));
			Double curMaxVal = maxMap.get(sensorId);
			Double curMinVal = minMap.get(sensorId);
			
			if (newVal > curMaxVal) {
				maxMap.put(sensorId, newVal);
				curMaxVal = newVal;
			}
			
			if (newVal < curMinVal) {
				minMap.put(sensorId, newVal);
				curMinVal = newVal;
			}
			
			collector.emit(new Values(sensorId, curMaxVal, curMinVal));
		}

		@Override
		public void declareOutputFields(OutputFieldsDeclarer declarer) {
			declarer.declare(new Fields("sensorId", "maxValue", "minValue"));
		}
	}

	public static void main(String[] args) throws Exception {

		TopologyBuilder builder = new TopologyBuilder();

		builder.setSpout("sensorDataSpout", new SensorDataSpout(), 5);
		builder.setBolt("getMaxMin", new MaxMinBolt(), 12).fieldsGrouping("spout", new Fields("sensorId"));

		Config conf = new Config();
		conf.setDebug(true);

		if (args != null && args.length > 0) {
			conf.setNumWorkers(2);

			StormSubmitter.submitTopology(args[0], conf, builder.createTopology());
		} else {
			conf.setMaxTaskParallelism(2);

			LocalCluster cluster = new LocalCluster();
			cluster.submitTopology("getMaxMin", conf, builder.createTopology());

			Thread.sleep(10000);

			cluster.shutdown();
		}
	}
}