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
<<<<<<< HEAD

	public void DataStreamReceiver(String username,String user_profile_url,int rating,String review_text,int revirew_no,String refId) {
		
=======
	
	public void DataStreamReceiver(String username,String user_profile_url,int rating,String review_text,int revirew_no) {
		/*Socket	clientSocket = null;
		InputStream is = null;
		DataInputStream dis = null;
		ServerSocket listener = null;*/
>>>>>>> origin/master
		try {
			
			DBObject document = new BasicDBObject();
			document.put("reviewNo", revirew_no);
			document.put("user", username);
			document.put("userUrl", user_profile_url);
			document.put("commentRate", rating);
			document.put("comment", review_text);
<<<<<<< HEAD
			mongoDao.MongoReviewStorage(document,refId);
=======
			mongoDao.MongoReviewStorage(document,doc_id);
>>>>>>> origin/master


		} 
		catch (Exception e) {
<<<<<<< HEAD
			//mongoDao.CloseConnection();
=======
			mongoDao.CloseConnection();
>>>>>>> origin/master
			e.printStackTrace();

		}

	}
	
	public void closeConnection(){
		mongoDao.CloseConnection();
	}

	public void closeConnection(){
		mongoDao.CloseConnection();
	}

}
