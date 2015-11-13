package dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import org.bson.Document;
import org.bson.conversions.Bson;
import com.mongodb.BasicDBObject;
import com.mongodb.Block;
import com.mongodb.DBObject;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoDatabase;
import conf.Config;

public class FetchData {
	MongoDatabase db;
	public FetchData(){
		new MongoConnection();
		db=MongoConnection.db();
	}
	
	public ArrayList<String> fetchAllReview(){
		ArrayList<String> fetchAllReviewData = null;
		db.getCollection(Config.config().getProperty("mongo_collection")).find(new BasicDBObject("{}","{review:1,_id:0}"));
		return fetchAllReviewData;
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
