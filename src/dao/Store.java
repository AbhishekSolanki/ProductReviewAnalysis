package dao;

import java.io.DataInputStream;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import conf.Config;

public class Store {
private static int doc_id =0;
private static int count=0;
private static MongoDao mongoDao = null;
public Store(){
	mongoDao = new MongoDao();

}
	
	public void MongoDocumentCreate(Document document){
		//insert in mysql and get product ref
		document.append("refId", doc_id);
		document.append("review",new ArrayList<>());
		mongoDao.MongoDocumentCreate(document);
	}
	
	public static void DataStreamReceiver(String username,String user_profile_url,int rating,String review_text) {
		/*Socket	clientSocket = null;
		InputStream is = null;
		DataInputStream dis = null;
		ServerSocket listener = null;*/
		try {
			/*System.out.println("dsr");
			listener = new ServerSocket(Integer.parseInt(Config.config().getProperty("socket_port")));
			clientSocket = listener.accept();
			
			clientSocket.setKeepAlive(true);
			is = clientSocket.getInputStream();
			dis = new DataInputStream(is);
			String line;*/
			
			
			
			/*while ((line = dis.readLine()) != null) {
				count++;
				if(!line.equals("EOF")){
					System.out.println(line);
					//mongocode object code here
					DBObject document = new BasicDBObject();
					document.put(String.valueOf(count), line);
					
					mongoDao.MongoReviewStorage(document,doc_id);
				//	mongoBatch.add(document);
					}
				else{
					dis.close();
					is.close();
					clientSocket.close();
					listener.close();
					break;
				}
			}*/
			/*if(mongoBatch.size()>0){
				MongoDao mongoDao = new MongoDao();
				mongoDao.MongoReviewStore(mongoBatch,String.valueOf(doc_id));
			}*/
			count++;
			DBObject document = new BasicDBObject();
			document.put("user", username);
			document.put("userUrl", user_profile_url);
			document.put("commentRate", rating);
			document.put("comment", review_text);
			mongoDao.MongoReviewStorage(document,doc_id);


		} 
		catch (Exception e) {
			mongoDao.CloseConnection();
			e.printStackTrace();

		}

	}
	
	public void closeConnection(){
		mongoDao.CloseConnection();
	}

}
