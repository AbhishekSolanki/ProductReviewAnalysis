package dao;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

import conf.Config;

public class MongoConnection {

	public static MongoDatabase db(){
		MongoClient mongoClient = new MongoClient();
		MongoDatabase db = mongoClient.getDatabase(Config.config().getProperty("mongo_database"));
		
		return db;
	}
	
}
