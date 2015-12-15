package com.hadoopMongo;

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.bson.BSONObject;

import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;
import com.mongodb.hadoop.io.BSONWritable;

public class WordSplitByDocReducer extends Reducer<Text, IntWritable, NullWritable, BSONWritable> {

	protected void reduce(Text key, Iterable<IntWritable> value,
			Context context)
			throws IOException, InterruptedException {
		int sum = 0;
		System.out.println(key);
			for(IntWritable word_count:value){
				sum+=word_count.get();
			}
			 BSONObject doc = BasicDBObjectBuilder.start()
				//	.add("_id", key.toString())
					 .add("word",key.toString())
			          .add("count", sum)
			          .get();
			   BSONWritable outDoc = new BSONWritable(doc);
			 
			context.write(NullWritable.get(), outDoc);
	}
}
