package com.hadoopMongo;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.bson.BSONObject;

public class TFIDFMapper extends Mapper<Object, BSONObject, Text, Text>{

	
	protected void map(Object key, BSONObject value,
		Mapper<Object, BSONObject, Text, Text> .Context context)
			throws IOException, InterruptedException {
	String word_key = (String)value.get("word");
	double tf = (double)value.get("TF");
	String word = word_key.split("@")[0];
	Text op = new Text((String)word_key.split("@")[1]+";"+tf); //reviewNo,TF
	context.write(new Text(word), op);
	
	}
}
