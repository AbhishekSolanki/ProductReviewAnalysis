package dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import org.bson.Document;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class Store {
	MongoDao mongoDao = null;
	public Store(){
		
	}

	public String MongoDocumentCreate(Document document){
		mongoDao = new MongoDao();
		java.util.Date date= new java.util.Date();
		int random = 0 +(int)(Math.random()*1000);
		String refId = new Timestamp(date.getTime()).toString()+random;
		document.append("refId", refId);
		document.append("review",new ArrayList<>());
		mongoDao.MongoDocumentCreate(document);
		return refId;
	}

	public void DataStreamReceiver(String username,String user_profile_url,int rating,String review_text,String revirew_no,String refId) {
		
		try {
			
			DBObject document = new BasicDBObject();
			document.put("reviewNo", revirew_no);
			document.put("user", username);
			document.put("userUrl", user_profile_url);
			document.put("commentRate", rating);
			document.put("comment", review_text);
			mongoDao.MongoReviewStorage(document,refId);


		} 
		catch (Exception e) {
			//mongoDao.CloseConnection();
			e.printStackTrace();

		}

	}

	public void closeConnection(){
		mongoDao.CloseConnection();
	}

}
