package com.hadoopMongo;

import java.io.IOException;
import java.util.HashMap;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.bson.BSONObject;

import com.mongodb.BasicDBObject;

public class TermFrequencyMapper extends Mapper<Object, BSONObject, Text, Text>{

	
	protected void map(Object key, BSONObject value,
		Mapper<Object, BSONObject, Text, Text> .Context context)
			throws IOException, InterruptedException {
	//	MapWritable result= new MapWritable();
	String word_key = (String)value.get("word");
	int count = (int)value.get("count");
	String reviewNo = word_key.split("@")[1];
	//result.put(new Text(word_key.split("@")[0]), new IntWritable(count));
	Text op = new Text((String)word_key.split("@")[0]+";"+count);
	context.write(new Text(reviewNo), op);
	
	}
}
