package hadoop;
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


public class AvrageValJob {

	public static class AverageMapper extends TableMapper<Text, DoubleWritable> {
		private static final String regex = "sid|ts";
		private static final long start = 666;
		private static final long end = 999;
		private static final byte[] CF = "value".getBytes();
		private static final byte[] QF = "numerical".getBytes();
		
		public void map(ImmutableBytesWritable row, Result value, Context context) throws IOException, InterruptedException {
			String[] sidAndTs = new String(row.get()).split(regex);
			String sid = sidAndTs[1];
			String ts = sidAndTs[2];
			long tsLong = Long.valueOf(ts);
			if (tsLong >= start && tsLong <= end) {
				byte[] valueBytes = value.getValue(CF, QF);
				if (valueBytes != null) {
					String str = new String(valueBytes);
					context.write(new Text(sid), new DoubleWritable(Double.parseDouble(str)));
				}
			}
		}
	}
	
	public static class AverageReducer extends TableReducer<Text, DoubleWritable, ImmutableBytesWritable> {
		public static final byte[] CF = "week".getBytes();
		public static final byte[] QF = "averageValue".getBytes();
		
		public void reduce(Text key, Iterable<DoubleWritable> values, Context context) throws IOException, InterruptedException {
			double sum = 0;
			double num = 0;
			for (DoubleWritable val : values) {
				sum += val.get();
				num += 1;
			}
			System.out.println("new rowkey: " + key.toString());
			Put put = new Put(Bytes.toBytes(key.toString()));
			
			System.out.println("new value: " + sum / num);

			put.add(CF, QF, Bytes.toBytes(String.valueOf(sum / num)));
			
			context.write(null, put);
		}
	}
	
	public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
		Configuration config = HBaseConfiguration.create();
		Job job = new Job(config,"ExampleSummary");
		job.setJarByClass(AvrageValJob.class);    

		Scan scan = new Scan();
		scan.setCaching(500);        
		scan.setCacheBlocks(false);  

		TableMapReduceUtil.initTableMapperJob(
			"SensorReading",        
			scan,               
			AverageMapper.class,     
			Text.class,         
			DoubleWritable.class,  
			job);
		TableMapReduceUtil.initTableReducerJob(
			"AverageSensorReading",        
			AverageReducer.class,    
			job);
		job.setNumReduceTasks(1);   

		boolean b = job.waitForCompletion(true);
		if (!b) {
			throw new IOException("error with job!");
		}
	}
}
