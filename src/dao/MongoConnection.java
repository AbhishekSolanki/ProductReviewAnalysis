package dao;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

import conf.Config;

public class MongoConnection {
	static MongoClient mongoClient;
	
	public static MongoDatabase db(){
		 mongoClient = new MongoClient();
		MongoDatabase db = mongoClient.getDatabase(Config.config().getProperty("mongo_database"));
		return db;
	}
	
}
