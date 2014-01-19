package hadoop.jobs;

import java.io.DataInput;
import java.io.DataOutput;
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
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Job;


public class AverageValJob {
	
	public class SumCountWritable implements Writable {

		private double sum;
		private int count;
		
		
		public double getSum() {
			return sum;
		}

		public void setSum(double sum) {
			this.sum = sum;
		}

		public int getCount() {
			return count;
		}

		public void setCount(int count) {
			this.count = count;
		}

		@Override
		public void readFields(DataInput in) throws IOException {
			sum = in.readDouble();
			count = in.readInt();
		}

		@Override
		public void write(DataOutput out) throws IOException {
			out.writeDouble(sum);
			out.writeInt(count);
		}
		
	}

	public static class AverageMapper extends TableMapper<Text, SumCountWritable> {
		private static final String regex = "sid|ts";
		private static final long start = 1385938651;  //Dec 01, 2013
		private static final long end = 1388617051;    //Jan 01, 2014
		private static final byte[] CF = "value".getBytes();
		private static final byte[] QF = "numerical".getBytes();
		private SumCountWritable sumCount = new AverageValJob().new SumCountWritable();
		
		public void map(ImmutableBytesWritable row, Result value, Context context) throws IOException, InterruptedException {
			String[] sidAndTs = new String(row.get()).split(regex);
			String sid = sidAndTs[1];
			String ts = sidAndTs[2];
			long tsLong = Long.valueOf(ts);
			if (tsLong >= start && tsLong <= end) {
				byte[] valueBytes = value.getValue(CF, QF);
				if (valueBytes != null) {
					String str = new String(valueBytes);
					sumCount.setCount(1);
					sumCount.setSum(Double.parseDouble(str));
					context.write(new Text(sid), sumCount);
				}
			}
		}
	}
	
	public static class AverageCombiner extends TableReducer<Text, SumCountWritable, Text> {
		@Override
		public void reduce(Text key, Iterable<SumCountWritable> values, Context context) throws IOException, InterruptedException{
			double sum = 0;
			int count = 0;
			for (SumCountWritable value : values) {
				sum += value.getSum();
				count += value.getCount();
			}
			
			SumCountWritable sumCount = new AverageValJob().new SumCountWritable();
			sumCount.setCount(count);
			sumCount.setSum(sum);
			
			context.write(key, sumCount);
		}
	} 
	
	public static class AverageReducer extends TableReducer<Text, SumCountWritable, ImmutableBytesWritable> {
		public static final byte[] CF = "month".getBytes();
		public static final byte[] QF = "averageValue".getBytes();
		
		public void reduce(Text key, Iterable<SumCountWritable> values, Context context) throws IOException, InterruptedException {
			double sum = 0;
			double count = 0;
			for (SumCountWritable val : values) {
				sum += val.getSum();
				count += val.getCount();
			}
			
			Put put = new Put(Bytes.toBytes(key.toString()));
			
			put.add(CF, QF, Bytes.toBytes(String.valueOf(sum / count)));
			
			context.write(null, put);
		}
	}
	
	public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
		Configuration config = HBaseConfiguration.create();
		Job job = new Job(config,"ExampleSummary");
		job.setJarByClass(AverageValJob.class);    

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
