package dao;

import java.util.List;

import org.bson.Document;

import com.mongodb.DBObject;
import com.mongodb.client.MongoDatabase;


import conf.Config;

public class MongoDao {
	MongoDatabase db=null;
	public MongoDao() {
		db = MongoConnection.db();
		
	}
	
	public void MongoDocumentCreate(Document document){
		
		db.getCollection(Config.config().getProperty("mongo_collection")).insertOne(document);
		
	}

	public void MongoReviewStore(List<DBObject> reviewList,String documentId){
		
		try{
			if(db!=null){
				/*db.getCollection(Config.config().getProperty("mongo_collection")).insertOne(
						new Document("prdref",prdref)
								new Document()
								));*/
			//bulk write			
				db.getCollection("").updateOne(new  Document().append("refId", documentId),
						new Document().append("$set", new Document().append("review", reviewList)));
			}
		}catch(Exception ex){
			
		}finally{
			
		}
	}
}
