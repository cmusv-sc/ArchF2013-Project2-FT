package hadoop.jobs;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;
import org.apache.hadoop.io.WritableUtils;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Partitioner;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.partition.HashPartitioner;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;


public class TopKMaxBySecondarySort extends Configured implements Tool{

	private static class Pair<T extends Comparable<T>,K extends Comparable<K>> implements Comparable<Pair<T, K>>{
		private T first;
		private K second;
		
		public Pair() {
			
		}
		
		public Pair(T first, K second) {
			this.first = first;
			this.second = second;
		}
		public T getFirst() {
			return first;
		}
		public void setFirst(T first) {
			this.first = first;
		}
		public K getSecond() {
			return second;
		}
		public void setSecond(K second) {
			this.second = second;
		}
		
		@Override
		public String toString() {
			return first + "," + second;
		}
		@Override
		public int compareTo(Pair<T, K> o) {
			int result = this.first.compareTo(o.getFirst());
			if (result == 0) {
				result = this.second.compareTo(o.getSecond());
			}
			return result;
		}
	}
	public static class CompositeKey implements WritableComparable<CompositeKey> {

		private Pair<String, String> deviceIdSensorIdPair = new Pair<String, String>();
		private String timestamp;
		
		@Override
		public int compareTo(CompositeKey o) {
			int result = this.deviceIdSensorIdPair.compareTo(o.deviceIdSensorIdPair);
			if (result == 0) {
				result = this.timestamp.compareTo(o.timestamp);
			}
			return result;
		}

		@Override
		public void readFields(DataInput in) throws IOException {
			String[] pair = WritableUtils.readString(in).split(",");
			this.deviceIdSensorIdPair.setFirst(pair[0]);
			this.deviceIdSensorIdPair.setSecond(pair[1]);
			this.timestamp = WritableUtils.readString(in);
		}

		@Override
		public void write(DataOutput out) throws IOException {
			WritableUtils.writeString(out, this.deviceIdSensorIdPair.toString());
			WritableUtils.writeString(out, this.timestamp);
		}

		public Pair<String, String> getDeviceIdSensorIdPair() {
			return deviceIdSensorIdPair;
		}

		public void setDeviceIdSensorIdPair(Pair<String, String> deviceIdSensorIdPair) {
			this.deviceIdSensorIdPair = deviceIdSensorIdPair;
		}

		public String getTimestamp() {
			return timestamp;
		}

		public void setTimestamp(String timeStamp) {
			this.timestamp = timeStamp;
		}
		
		@Override
		public String toString() {
			
			
			return deviceIdSensorIdPair.toString() + "," + this.timestamp;
		}
	}
	
	public static class CompositeKeyPartitioner extends Partitioner<CompositeKey, Text> {
		private HashPartitioner<Text, Text> hashPartitioner = new HashPartitioner<Text, Text>();
		
		@Override
		public int getPartition(CompositeKey compKey, Text value, int numReduceTasks) {
			return hashPartitioner.getPartition(new Text(compKey.getDeviceIdSensorIdPair().toString()), value, numReduceTasks);
		}
	}
	
	public static class CompositeKeyGroupingComparator extends WritableComparator {

		protected CompositeKeyGroupingComparator() {
			super(CompositeKey.class);
		}
		
		@Override
		public int compare(WritableComparable w1, WritableComparable w2) {
			CompositeKey key1 = (CompositeKey) w1;
			CompositeKey key2 = (CompositeKey) w2;
			return key1.getDeviceIdSensorIdPair().compareTo(key2.getDeviceIdSensorIdPair());
		}
		
	}
	
	public static class CompositeKeyComparator extends WritableComparator {
		protected CompositeKeyComparator() {
			super(CompositeKey.class, true);
		}
		
		@SuppressWarnings("rawtypes")
		@Override
		public int compare(WritableComparable w1, WritableComparable w2) {
		 
			CompositeKey key1 = (CompositeKey) w1;
			CompositeKey key2 = (CompositeKey) w2;
			 
			int result = key1.getDeviceIdSensorIdPair().compareTo(key2.getDeviceIdSensorIdPair());
			 
			if (result == 0) {
				return -key1.getTimestamp().compareTo(key2.getTimestamp());
			}
			 
			return result;
		}
		
	}
	
	//input file format:
	//sensorId, timestamp, value, deviceId
	public static class SecondarySortMapper extends Mapper<LongWritable, Text, CompositeKey, Text> {
		private CompositeKey compositeKey = new CompositeKey();
		
		@Override
		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException{
			String line = value.toString();
			String[] tokens = line.split(",");
			compositeKey.setDeviceIdSensorIdPair(new Pair<String, String>(tokens[3], tokens[0]));
			compositeKey.setTimestamp(tokens[1]);
			
			String valueContent = tokens[1] + "," + tokens[2];
			context.write(compositeKey, new Text(valueContent));
		}
	}
	
	public static class SecondarySortReducer extends Reducer<CompositeKey, Text, CompositeKey, Text> {
		private static final int K = 10;
		@Override
		public void reduce(CompositeKey key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
			int i = 0;
			for (Text value : values) {
				if (i >= K) {
					break;
				}
				context.write(key, value);
				i++;
			}
		}
	}

	@Override
	public int run(String[] args) throws Exception {
 
		if (args.length != 2) {
			System.out.printf("Two parameters are required - <input dir> <output dir>\n");
			return -1;
		}
 
		Job job = new Job(getConf());
		job.setJobName("Secondary sort example");
 
		job.setJarByClass(TopKMaxBySecondarySort.class);
		FileInputFormat.setInputPaths(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
 
		job.setMapperClass(SecondarySortMapper.class);
		job.setMapOutputKeyClass(CompositeKey.class);
		job.setMapOutputValueClass(Text.class);
		job.setPartitionerClass(CompositeKeyPartitioner.class);
		job.setSortComparatorClass(CompositeKeyComparator.class);
		job.setGroupingComparatorClass(CompositeKeyGroupingComparator.class);
		job.setReducerClass(SecondarySortReducer.class);
		job.setOutputKeyClass(CompositeKey.class);
		job.setOutputValueClass(NullWritable.class);
 
		job.setNumReduceTasks(8);
 
		boolean success = job.waitForCompletion(true);
		return success ? 0 : 1;
	}
	
	public static void main(String[] args) throws Exception {
		int exitCode = ToolRunner.run(new Configuration(),new TopKMaxBySecondarySort(), args);
		System.exit(exitCode);
	}

}
