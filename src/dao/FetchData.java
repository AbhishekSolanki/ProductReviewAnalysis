package dao;


import java.util.ArrayList;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoDatabase;

import conf.Config;

public class FetchData {
	MongoDatabase db;
	public FetchData(){
		db=new MongoConnection().db();
	}
	
	public ArrayList<String> fetchAllReview(){
		ArrayList<String> fetchAllReviewData = null;
		db.getCollection(Config.config().getProperty("mongo_collection")).find(new BasicDBObject("{}","{review:1,_id:0}"));
		return fetchAllReviewData;
	}

}
