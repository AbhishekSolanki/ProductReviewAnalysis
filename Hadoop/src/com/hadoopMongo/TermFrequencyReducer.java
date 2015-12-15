package com.hadoopMongo;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.bson.BSONObject;
import org.bson.BasicBSONObject;

import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.hadoop.io.BSONWritable;
import com.mongodb.hadoop.io.MongoUpdateWritable;

public class TermFrequencyReducer extends Reducer<Text,Text, NullWritable, BSONWritable>{
	@Override
	protected void reduce(Text arg0, Iterable<Text> arg1,
			Context arg2)
					throws IOException, InterruptedException {
		String word = null;
		int wordCount = 0;
		int sum=0;
		HashMap<String, Integer> wordsInComment = new HashMap<String, Integer>();
		for(Text value:arg1){
			sum++;
			String data = value.toString();
			String[] parts = data.split(";");
			word=parts[0];
			wordCount=Integer.parseInt(parts[1]);
			wordsInComment.put(word, wordCount);
			}
			
			//System.err.println(sum+" "+wordCount+" "+termFrequency);
			
			 Iterator it = wordsInComment.entrySet().iterator();
			 while (it.hasNext()) {
			        Map.Entry pair = (Map.Entry)it.next();
			        System.out.println(pair.getKey() + " = " + pair.getValue());
			        double termFrequency = Double.valueOf(pair.getValue().toString())/(double)sum;
			        BSONObject doc = BasicDBObjectBuilder.start()
							//	.add("_id", key.toString())
								 .add("word",pair.getKey()+"@"+arg0)
								 .add("wordsPerDoc",sum)
								 .add("wordcount",pair.getValue())
						          .add("TF", termFrequency)
						          .get();
						   BSONWritable outDoc = new BSONWritable(doc);
						 
						arg2.write(NullWritable.get(), outDoc);
			        it.remove(); // avoids a ConcurrentModificationException
			    }
			
			 
			/*BasicBSONObject query=new BasicBSONObject();
			Pattern regex = Pattern.compile(arg0.toString());
			query.append("word", regex);
			BasicBSONObject update=new BasicBSONObject();

			update.append("$set", new BasicBSONObject().append("tf",termFrequency).append("totalwords", sum));
			MongoUpdateWritable muw=new MongoUpdateWritable(query,update,false,true);
			arg2.write(NullWritable.get(), muw);*/

		}
	}

