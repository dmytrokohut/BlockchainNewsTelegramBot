package com.dkohut.telegrambots.common.dao;

import java.util.ArrayList;
import java.util.List;

import com.dkohut.telegrambots.common.entities.CoinDeskPost;
import com.dkohut.telegrambots.common.interfaces.IMongoDBService;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class MongoDBService implements IMongoDBService {

	private static final String collectionName = "posts";
	private MongoClient mongoClient = new MongoClient("localhost", 27017);
	private DB database;
	
	
	private void setConnection() {
		database = mongoClient.getDB("CoinDeskBot");
	}
	
	/**
	 * @see com.dkohut.telegrambots.common.interfaces.IMongoDBService#getNewsByUserId(Long)
	 */
	public List<CoinDeskPost> getNewsByUserId(Long userId) {
		setConnection();
		DBCollection collection = database.getCollection(MongoDBService.collectionName);
		
		BasicDBObject whereQuery = new BasicDBObject();
		whereQuery.put("user_id", userId);
		DBCursor cursor = collection.find(whereQuery);
		
		List<CoinDeskPost> listOfPosts = new ArrayList<>();
		
		while(cursor.hasNext()) {
			DBObject object = cursor.next();
			
			CoinDeskPost post = new CoinDeskPost(
					object.get("post_title").toString(),
					object.get("post_link").toString(),
					object.get("post_author").toString(),
					object.get("post_date").toString()
				);
			listOfPosts.add(post);
		}
		
		return listOfPosts;
	}
	
	/**
	 * @see com.dkohut.telegrambots.common.interfaces.IMongoDBService#save(Long, CoinDeskPost)
	 */
	public void save(Long userId, CoinDeskPost coinDeskPost) {
		setConnection();
		DBCollection collection = database.getCollection(MongoDBService.collectionName);
		
		BasicDBObject document = new BasicDBObject();
		document.put("user_id", userId);
		document.put("post_title", coinDeskPost.getArticleTitle());
		document.put("post_author", coinDeskPost.getAuthor());
		document.put("post_date", coinDeskPost.getDate());
		document.put("post_link", coinDeskPost.getArticleLink());
		
		collection.insert(document);
	}
	
	/**
	 * @see com.dkohut.telegrambots.common.interfaces.IMongoDBService#createCollection()
	 */
	public void createCollection() {
		setConnection();
		database.createCollection(collectionName, null);
	}	
		
	
}
