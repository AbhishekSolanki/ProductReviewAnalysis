package com.hadoopMongo;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.bson.BSONObject;

import com.mongodb.hadoop.MongoInputFormat;
import com.mongodb.hadoop.MongoOutputFormat;
import com.mongodb.hadoop.io.BSONWritable;
import com.mongodb.hadoop.io.MongoUpdateWritable;

public class TermFrequencyDriver {

	public TermFrequencyDriver(){
		Configuration conf = new Configuration();
		
		conf.setClass("mongo.job.mapper", TermFrequencyMapper.class, Mapper.class);
		conf.setClass("mongo.job.reducer",TermFrequencyReducer.class,Reducer.class);

		conf.setClass("mongo.job.mapper.output.key",Text.class,Object.class);
		conf.setClass("mongo.job.mapper.output.value",IntWritable.class,Object.class);

		conf.setClass("mongo.job.output.key", NullWritable.class, Object.class);
		conf.setClass("mongo.job.output.value", BSONObject.class, Object.class);

		conf.set("mongo.input.uri",  "mongodb://192.168.8.100/productReview.hadoop");
		conf.set("mongo.output.uri", "mongodb://192.168.8.100/productReview.tfidf_job2");

		Job job;
		try {
			job = Job.getInstance(conf);
			job.setInputFormatClass(MongoInputFormat.class);
			job.setOutputFormatClass(MongoOutputFormat.class);

			job.setMapOutputKeyClass(Text.class);
			job.setMapOutputValueClass(Text.class);

			job.setOutputKeyClass(NullWritable.class);
			job.setOutputValueClass(BSONWritable.class);

			job.setMapperClass(TermFrequencyMapper.class);
			job.setReducerClass(TermFrequencyReducer.class);

			job.setJarByClass(TermFrequencyDriver.class);

			job.submit();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}




	}
	
	public static void main(String args[]) {
		TermFrequencyDriver termFrequencyDriver = new TermFrequencyDriver();
	}
	
}
