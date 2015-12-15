package com.hadoopMongo;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Reducer.Context;
import org.bson.BSONObject;

import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.hadoop.io.BSONWritable;

public class TFIDFRecuer extends Reducer<Text,Text, NullWritable, BSONWritable>{
	@Override
	protected void reduce(Text arg0, Iterable<Text> arg1,
			Context arg2)
					throws IOException, InterruptedException {
		String reviewNo = null;
		double tf = 0;
		int wordContainsCount=0;
		int sum=4525;
		HashMap<String, Double> reviewNos = new HashMap<String, Double>();
		for(Text value:arg1){
			wordContainsCount++;
			String data = value.toString();
			String[] parts = data.split(";");
			reviewNo=parts[0];
			tf=Double.parseDouble(parts[1]);
			reviewNos.put(reviewNo,  tf);
			}
			//System.err.println(sum+" "+wordCount+" "+termFrequency);
			
			 Iterator it = reviewNos.entrySet().iterator();
			 while (it.hasNext()) {
			        Map.Entry pair = (Map.Entry)it.next();
			        System.out.println(pair.getKey() + " = " + pair.getValue());
			        double tfidf = tf *(1 + Math.log(sum/wordContainsCount));
			        		//Double.valueOf(pair.getValue().toString())*(double)sum;
			        BSONObject doc = BasicDBObjectBuilder.start()
							//	.add("_id", key.toString())
								 .add("word",arg0+"@"+pair.getKey())
								 .add("FoundInDoc",wordContainsCount)
								 .add("tf",pair.getValue())
						          .add("TFIDF", tfidf)
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
