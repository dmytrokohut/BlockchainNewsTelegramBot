package com.dkohut.telegrambots;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import com.dkohut.telegrambots.bots.BlockchainNewsBot;
import com.dkohut.telegrambots.common.dao.MongoDBService;
import com.dkohut.telegrambots.parser.CoinDeskParser;

public class Main {
	
	private static final Logger logger = Logger.getLogger(Main.class.getName());

	public static void main(String[] args) {
		// Initialize API context
		ApiContextInitializer.init();
		
		TelegramBotsApi botsApi = new TelegramBotsApi();
		
		// Register bots
		try {
			// binding of classes using Dependency Injection
			botsApi.registerBot(new BlockchainNewsBot(new CoinDeskParser(), new MongoDBService()));
		} catch(TelegramApiException e) {
			logger.log(Level.SEVERE, "Exception during registering of the bots");
		}
	}
	
}
