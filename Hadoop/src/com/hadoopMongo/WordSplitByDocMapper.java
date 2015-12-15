package com.hadoopMongo;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.bson.BSONObject;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;

public class WordSplitByDocMapper extends Mapper<Object, BSONObject, Text, IntWritable>{

	@Override
	protected void map(Object key, BSONObject value,
			Context context)
					throws IOException, InterruptedException {
		BasicDBList reviews = (BasicDBList)value.get("review");
		for(Object temp:reviews){
			String reviewNo = (String) ((BasicDBObject)temp).get("reviewNo");
			String[] comment_raw_words = ((String) ((BasicDBObject)temp).get("comment")).toLowerCase().replaceAll("[^\\w\\s]"," ").replaceAll("\\d+","").replaceAll("\\s+", " ").split("\\s");
			for(String word:comment_raw_words){
				System.out.println(reviewNo);
				final String write_key = word+"@"+reviewNo;
				context.write(new Text(write_key), new IntWritable(1));
			}
		}
	}
}