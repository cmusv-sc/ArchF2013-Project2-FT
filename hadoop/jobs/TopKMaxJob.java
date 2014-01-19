package hadoop.jobs;
import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.mapreduce.TableReducer;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;

import com.google.common.collect.MinMaxPriorityQueue;

public class TopKMaxJob {
	private static final int K = 10;

	public static class MaxMapper extends TableMapper<Text, DoubleWritable> {
		private static final String regex = "sid|ts";
		private static final byte[] CF = "value".getBytes();
		private static final byte[] QF = "numerical".getBytes();

		@Override
		public void map(ImmutableBytesWritable rowKey, Result value,
				Context context) throws IOException, InterruptedException {
			String[] sidAndTs = new String(rowKey.get()).split(regex);
			String sid = sidAndTs[1];

			byte[] valueBytes = value.getValue(CF, QF);

			if (valueBytes != null) {
				String str = new String(valueBytes);
				context.write(new Text(sid),
						new DoubleWritable(Double.parseDouble(str)));
			}
		}
	}

	public static class MaxCombiner extends
			TableReducer<Text, DoubleWritable, Text> {
		@Override
		public void reduce(Text key, Iterable<DoubleWritable> values,
				Context context) throws IOException, InterruptedException {
			context.write(key, new DoubleWritable(getMaxVal(values)));
		}
	}

	public static class MaxReducer extends
			TableReducer<Text, DoubleWritable, ImmutableBytesWritable> {
		public static final byte[] CF = "maxValue".getBytes();
		public static final byte[] QF = "numerical".getBytes();

		@Override
		public void reduce(Text key, Iterable<DoubleWritable> values,
				Context context) throws IOException, InterruptedException {
			Put put = new Put(Bytes.toBytes(key.toString()));
			put.add(CF, QF, Bytes.toBytes(String.valueOf(getMaxVal(values))));
			context.write(null, put);
		}
	}

	private static class Pair implements Comparable<Pair> {

		private String key;
		private Double value;

		public Pair(String key, double val) {
			this.key = key;
			this.value = val;
		}

		@Override
		public int compareTo(Pair o) {
			return value.compareTo(o.value);
		}

	}

	public static class TopKMapper extends TableMapper<Text, DoubleWritable> {
		private static final byte[] CF = "maxValue".getBytes();
		private static final byte[] QF = "numerical".getBytes();

		private MinMaxPriorityQueue<Pair> queue = MinMaxPriorityQueue.maximumSize(K).create();

		@Override
		public void map(ImmutableBytesWritable rowKey, Result value, Context context) throws IOException, InterruptedException {
			String sid = new String(rowKey.get());
			double maxVal = Double.MIN_VALUE;

			byte[] valueBytes = value.getValue(CF, QF);

			if (valueBytes != null) {
				String str = new String(valueBytes);
				maxVal = Double.parseDouble(str);
			}

			queue.add(new Pair(sid, maxVal));
		}

		@Override
		public void cleanup(Context context) throws IOException,
				InterruptedException {
			for (Pair pair : queue) {
				context.write(new Text(pair.key),
						new DoubleWritable(pair.value));
			}
		}
	}

	public static class TopKReducer extends TableReducer<Text, DoubleWritable, ImmutableBytesWritable> {
		public static final byte[] CF = "topKValue".getBytes();
		public static final byte[] QF = "numerical".getBytes();
		private MinMaxPriorityQueue<Pair> queue = MinMaxPriorityQueue.maximumSize(K).create();

		@Override
		public void reduce(Text key, Iterable<DoubleWritable> values, Context context) throws IOException, InterruptedException {
			for (DoubleWritable value : values) {
				queue.add(new Pair(key.toString(), value.get()));
			}
			
			for (Pair pair : queue) {
				Put put = new Put(Bytes.toBytes(pair.key));
				put.add(CF, QF, Bytes.toBytes(String.valueOf(pair.value)));
				context.write(null, put);
			}
		}
	}

	private static double getMaxVal(Iterable<DoubleWritable> values) {
		double maxVal = Double.MIN_VALUE;

		for (DoubleWritable val : values) {
			double v = val.get();
			if (v > maxVal) {
				maxVal = v;
			}
		}

		return maxVal;
	}

	public static void main(String[] args) throws IOException,
			InterruptedException, ClassNotFoundException {
		Configuration config = HBaseConfiguration.create();
		Job job = new Job(config, "ExampleSummary");
		job.setJarByClass(AverageValJob.class);

		Scan scan = new Scan();
		scan.setCaching(500);
		scan.setCacheBlocks(false);
		
		// get max
		TableMapReduceUtil.initTableMapperJob(
				"SensorReading", 
				scan,
				MaxMapper.class, 
				Text.class, 
				DoubleWritable.class, 
				job);

		job.setCombinerClass(MaxCombiner.class);

		TableMapReduceUtil.initTableReducerJob(
			"StatisticalSensorReading",
			MaxReducer.class, 
			job);
		job.setNumReduceTasks(1);

		boolean b = job.waitForCompletion(true);
		if (!b) {
			throw new IOException("error with job!");
		}

		// get top k
		TableMapReduceUtil.initTableMapperJob(
			"StatisticalSensorReading", 
			scan,
			TopKMapper.class, 
			Text.class, 
			DoubleWritable.class, 
			job);
		
		TableMapReduceUtil.initTableReducerJob(
			"StatisticalSensorReading",
			TopKReducer.class, 
			job);
		job.setNumReduceTasks(1);
		b = job.waitForCompletion(true);
		if (!b) {
			throw new IOException("error with job!");
		}
	}

}
