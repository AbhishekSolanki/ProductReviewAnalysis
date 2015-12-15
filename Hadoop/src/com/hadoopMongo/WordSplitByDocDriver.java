package com.hadoopMongo;



import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.bson.BSONObject;

import com.mongodb.hadoop.MongoInputFormat;
import com.mongodb.hadoop.MongoOutputFormat;
import com.mongodb.hadoop.io.BSONWritable;
import com.mongodb.hadoop.util.MongoTool;

public class WordSplitByDocDriver extends MongoTool{

	public WordSplitByDocDriver(){

		Configuration conf = new Configuration();

		conf.setClass("mongo.job.mapper", WordSplitByDocMapper.class, Mapper.class);
		conf.setClass("mongo.job.reducer",WordSplitByDocReducer.class,Reducer.class);

		conf.setClass("mongo.job.mapper.output.key",Text.class,Object.class);
		conf.setClass("mongo.job.mapper.output.value",Text.class,Object.class);

		conf.setClass("mongo.job.output.key", NullWritable.class, Object.class);
		conf.setClass("mongo.job.output.value", BSONObject.class, Object.class);

		conf.set("mongo.input.uri",  "mongodb://192.168.8.100/productReview.productReviewCol");
		conf.set("mongo.output.uri", "mongodb://192.168.8.100/productReview.tfidf_job1");

		Job job;
		try {
			job = Job.getInstance(conf);
			// Set Hadoop-specific job parameters
			job.setInputFormatClass(MongoInputFormat.class);
			job.setOutputFormatClass(MongoOutputFormat.class);

			job.setMapOutputKeyClass(Text.class);
			job.setMapOutputValueClass(IntWritable.class);

			job.setOutputKeyClass(NullWritable.class);
			job.setOutputValueClass(BSONWritable.class);

			job.setMapperClass(WordSplitByDocMapper.class);
			job.setReducerClass(WordSplitByDocReducer.class);

			job.setJarByClass(WordSplitByDocDriver.class);

			job.submit();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}




	}
	
	public static void main(String args[]) {
		WordSplitByDocDriver hadoopMongoDriverTest = new WordSplitByDocDriver();
	}
}
