package dao;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bson.BSONObject;
import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.BulkWriteOperation;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;


import conf.Config;

public class MongoDao{
	MongoDatabase db=null;
	public MongoDao() {
		db = MongoConnection.db();
		
	}
	
	public void MongoDocumentCreate(Document document){
		
		db.getCollection(Config.config().getProperty("mongo_collection")).insertOne(document);
		
	}

	public void MongoReviewStore(List<DBObject> reviewList,String documentId){
		System.out.println("insider");
		try{
			if(db!=null){
				/*db.getCollection(Config.config().getProperty("mongo_collection")).insertOne(
						new Document("prdref",prdref)
								new Document()
								));*/
			//bulk write			
				db.getCollection(Config.config().getProperty("mongo_collection")).updateOne(new Document().append("refId", documentId),
						new Document().append("$push", new Document().append("review", reviewList)));
				
				/*DBCollection col =  (DBCollection) db.getCollection(Config.config().getProperty("mongo_collection"));
				
				BulkWriteOperation bulk = col.initializeUnorderedBulkOperation();
				
				for(DBObject doc:reviewList){
					System.out.println(doc);
									bulk.find(new BasicDBObject("refId", documentId)).updateOne(new BasicDBObject("review",doc));
					}
				
				System.out.println(bulk.execute());
							}*/
		}
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			MongoConnection.mongoClient.close();
		}
	}
	
	public void MongoReviewStorage(DBObject document,int doc_id){
		try{
			if(db!=null){
				db.getCollection(Config.config().getProperty("mongo_collection")).updateOne(new Document().append("refId", doc_id),
						new Document().append("$push", new Document().append("review", document)));
		}
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			MongoConnection.mongoClient.close();
		}
	}
}
