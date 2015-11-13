package dao;

import java.util.List;
import org.bson.Document;
import com.mongodb.DBObject;
import com.mongodb.client.MongoDatabase;
import conf.Config;

public class MongoDao{
	
	public static MongoDatabase db=null;
	
public MongoDao() {
		db = MongoConnection.db();
		
	}
	
	public void MongoDocumentCreate(Document document){
		
		db.getCollection(Config.config().getProperty("mongo_collection")).insertOne(document);
		
	}

	//FUNCTION NOT IN USE - FOR BULK STORAGE
	public void MongoReviewStore(List<DBObject> reviewList,String documentId){
		System.out.println("insider");
		try{
			if(db!=null){
			
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
			//MongoConnection.mongoClient.close();
		}
	}
	
	public void MongoReviewStorage(DBObject document,String doc_id){
		try{
			if(db!=null){
				db.getCollection(Config.config().getProperty("mongo_collection")).updateOne(new Document().append("refId", doc_id),
						new Document().append("$push", new Document().append("review", document)));
		}
		}catch(Exception ex){
<<<<<<< HEAD
			//MongoConnection.mongoClient.close();
=======
<<<<<<< HEAD
			//MongoConnection.mongoClient.close();
=======
			MongoConnection.mongoClient.close();
>>>>>>> origin/master
>>>>>>> origin/master
			ex.printStackTrace();
		}finally{
			//MongoConnection.mongoClient.close();
		}
	}
	
	
	public void CloseConnection(){
		MongoConnection.mongoClient.close();
	}
}
