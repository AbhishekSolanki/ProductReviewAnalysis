package dao;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

import conf.Config;

public class MongoConnection {
	static MongoClient mongoClient;
	static DB _db;
	static MongoDatabase db;
	
	 MongoConnection() {
		mongoClient = mongoClient();
	}
	
	public static MongoClient mongoClient(){
		  mongoClient = new MongoClient();
		  return mongoClient;
	}
	
	public static MongoDatabase db(){
		 db = mongoClient.getDatabase(Config.config().getProperty("mongo_database"));
		 return db;
	}
	
	@SuppressWarnings("deprecation")
	public static DB _db(){
		_db = mongoClient.getDB(Config.config().getProperty("mongo_database"));
		return _db;
		
	}
	
	
	
}
