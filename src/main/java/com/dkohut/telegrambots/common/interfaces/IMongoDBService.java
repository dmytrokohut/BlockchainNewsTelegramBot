package com.dkohut.telegrambots.common.interfaces;

import java.util.List;

import com.dkohut.telegrambots.common.entities.CoinDeskPost;

public interface IMongoDBService {

	/**
	 * Returns the list of posts that contains in MongoDB in given user.
	 * 
	 * @param userId - the ID of the user in Telegram
	 * 
	 * @return list of posts
	 */
	List<CoinDeskPost> getNewsByUserId(Long userId);
	
	/**
	 * Saves the post related to the user in MongoDB.
	 * 
	 * @param userId - the ID of the user in Telegram
	 * @param coinDeskPost - the post
	 */
	void save(Long userId, CoinDeskPost coinDeskPost);
	
	/**
	 * Creates the collection with the static name. 
	 * Should be used only when the bot begins working.
	 */
	void createCollection();
}
