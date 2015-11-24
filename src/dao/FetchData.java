package dao;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.apache.bcel.generic.CPInstruction;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.json.JsonWriter;

import com.mongodb.BasicDBObject;
import com.mongodb.Block;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.util.JSON;

import conf.Config;

public class FetchData {
	MongoDatabase db;
	DB _db;
	public FetchData(){
		new MongoConnection();
		db=MongoConnection.db();
		_db=MongoConnection._db();
	}


	public ArrayList<String> ReviewDocToJSON() throws IOException{
		ArrayList<String> fetchAllReviewData = null;

		DBObject query = new BasicDBObject();
		BasicDBObject projection = new BasicDBObject("review", 1).append("_id",0);
		DBCollection col =  _db.getCollection(Config.config().getProperty("mongo_collection"));
		DBCursor cursor =  col.find(query,projection);
		FileWriter file = new FileWriter("F:\\test1.json");
		int count=0;
		while(cursor.hasNext()){
			DBObject document = cursor.next();
			@SuppressWarnings("unchecked")
			List<BasicDBObject> list = (List<BasicDBObject>) document.get("review");
			for(BasicDBObject list_temp:list){
				file.write(list_temp.toString());
			}

			count++;
		}
		file.close();
		System.out.println(count);
		return fetchAllReviewData;
	}

	//Method used for frequency calculation
	//	@SuppressWarnings("null")
	public boolean getOnlyReviewComment() throws IOException{
		boolean status = false;
		try{
			//ArrayList<String> fetchAllReviewData =null;
			DBObject query = new BasicDBObject();
			BasicDBObject projection = new BasicDBObject("review", 1).append("_id",0);
			DBCollection col =  _db.getCollection(Config.config().getProperty("mongo_collection"));
			DBCursor cursor =  col.find(query,projection);
			FileWriter file = new FileWriter("getOnlyReviewComment.tmp");
			int count=0;
			//fetchAllReviewData=new ArrayList<>();
			while(cursor.hasNext()){
				DBObject document = cursor.next();
				@SuppressWarnings("unchecked")
				List<BasicDBObject> list = (List<BasicDBObject>) document.get("review");
				for(BasicDBObject list_temp:list){
					if(!list_temp.getString("comment").equals(null) && !list_temp.getString("reviewNo").equals(null)){
					String comment = list_temp.getString("comment").toLowerCase().replaceAll("[^\\w\\s]"," ").replaceAll("\\d+","").replaceAll("\\s+", " ").trim()+"\n";
					String reviewNo = list_temp.getString("reviewNo").toLowerCase();
					file.write(reviewNo+"%%COMMENT%%"+comment);
					count++;
					}
				}
			}
			file.close();
			status=true;
			System.out.println("Filtered comment in getOnlyReviewComment method: "+count);

		}catch(Exception ex){
			return status;
		}

		return status;
	}

	@SuppressWarnings("unchecked")
	public ArrayList<String> usernames(){
		ArrayList<String> usernames = null;
		DBObject obj = new BasicDBObject();
		obj.put("distinct", "productReviewCol");
		obj.put("key", "review.user");
		Document commandResult = db.runCommand((Bson) obj);
		usernames=(ArrayList<String>) commandResult.get("values");
		return usernames;
	}

	public HashMap<String,String> filteredUsersComment(ArrayList<String> filteredUserList){
		final HashMap<String,String> fetchAllReviewComment;

		List<String> username_array = Arrays.asList(filteredUserList.toArray(new String[filteredUserList.size()]));

		AggregateIterable<Document> iterable = db.getCollection(Config.config().getProperty("mongo_collection")).aggregate(Arrays.asList(
				new Document("$unwind", "$review"),
				new Document("$match",new Document("review.user",new Document("$in",username_array))),
				new Document("$project",new Document("review.comment",1).append("review.reviewNo", 1))
				));
		if(iterable!=null){
			fetchAllReviewComment = new HashMap<String,String>();
		}else{
			fetchAllReviewComment= null;
		}

		iterable.forEach(new Block<Document>() {
			@Override
			public void apply(final Document document) {
				Document d = (Document) document.get("review");
				fetchAllReviewComment.put((String)d.get("reviewNo"), (String)d.get("comment"));
			}
		});

		return fetchAllReviewComment;	
	}

}
