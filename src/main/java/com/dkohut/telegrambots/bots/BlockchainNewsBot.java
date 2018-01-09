package com.dkohut.telegrambots.bots;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import com.dkohut.telegrambots.common.dao.MongoDBService;
import com.dkohut.telegrambots.common.entities.CoinDeskPost;
import com.dkohut.telegrambots.common.entities.CoinMarketCapCurrency;
import com.dkohut.telegrambots.common.interfaces.ICoinDeskParser;
import com.dkohut.telegrambots.common.interfaces.IMongoDBService;
import com.dkohut.telegrambots.logging.CoinDeskLogger;
import com.dkohut.telegrambots.parser.CoinDeskParser;

public class BlockchainNewsBot extends TelegramLongPollingBot {
	
	private static final Logger logger = Logger.getLogger(BlockchainNewsBot.class.getName());
	
	private IMongoDBService mongoDBService;
	private ICoinDeskParser coinDeskParser;
	
	
	public BlockchainNewsBot(CoinDeskParser coinDeskParser, MongoDBService mongoDBService) {		
		this.coinDeskParser = coinDeskParser;
		this.mongoDBService = mongoDBService;
		mongoDBService.createCollection();
	}
	

	@Override
	public void onUpdateReceived(Update update) {		
		String command = update.getMessage().getText();
		
		CoinDeskLogger.log(
				update.getMessage().getChat().getFirstName(), 
				update.getMessage().getChat().getLastName(), 
				update.getMessage().getChat().getId(), 
				command
			);
		
		if (update.hasMessage() && update.getMessage().isCommand()) {			
			long chatId = update.getMessage().getChatId();			
			
			if(command.equals("/recent")) {
				List<CoinDeskPost> posts = coinDeskParser.getPosts();
				
				for (CoinDeskPost post : posts) {					
					mongoDBService.save(update.getMessage().getChat().getId(),	post);					
					sendMessage(chatId, post);
				}
				
			} else if(command.equals("/new")) {
				long userId = update.getMessage().getChat().getId();
				
				List<CoinDeskPost> receivedPosts = mongoDBService.getNewsByUserId(userId);
				
				if(receivedPosts.isEmpty()) {
					sendTextMessage(chatId, "You are send message first time, use /recent command first.");
					return;
				}
				
				List<CoinDeskPost> freshPosts = getFreshPosts(receivedPosts, coinDeskParser.getPosts());
				
				if(freshPosts.isEmpty()) {
					sendTextMessage(chatId, "You have not news.");
					return;
				}
				
				for (CoinDeskPost post : freshPosts) {
					mongoDBService.save(update.getMessage().getChat().getId(),	post);
					sendMessage(chatId, post);
				}
				
			} else if(command.equals("/prices")) {
				List<CoinMarketCapCurrency> listOfCurrencies = coinDeskParser.getPrices();
				
				if(listOfCurrencies.isEmpty()) {
					sendTextMessage(chatId, "Could not get the data.");
				}
				
				String response = "";
				for(CoinMarketCapCurrency currency : listOfCurrencies) {
					response += currency.getName() + " - " + currency.getValue() + " (" + currency.getPercentChange() + ")\n";
				}
				
				sendTextMessage(chatId, response);
				
			} else if(command.equals("/help")) {
				String helpMessage = 
						"/recent - Displays recent 13 news\n" +
						"/news - Displays fresh news\n" +
						"/prices - Displays current currency prices";
				
				sendTextMessage(chatId, helpMessage);
				
			} else {
				logger.info("Command is not recognized");
				sendTextMessage(update.getMessage().getChatId(), "Command is not recognized");
			}
			
		} else {
			logger.info("Command is not recognized");
			sendTextMessage(update.getMessage().getChatId(), "Command is not recognized");
		}
		
	}
	
	@Override
	public String getBotUsername() {
		return "BlockchainNews";
	}

	@Override
	public String getBotToken() {
		return "538860876:AAE8vRQYhqIn0scdQt9dFq3gCIxPlO06nME";
	}
	
	private void sendMessage(Long chatId, CoinDeskPost post) {
		SendMessage message = new SendMessage();
		
		message.setChatId(chatId);
		message.setText(
				"Title: " + post.getArticleTitle() + "\n" + 
				"Author:" + post.getAuthor() + "\n" + 
				"Date: " + post.getDate() + "\n" + 
				"Link: " + post.getArticleLink()
			);
		
		try {						
			sendMessage(message);
		} catch (TelegramApiException e) {
			logger.log(Level.SEVERE, "Exception during message sending");
			throw new RuntimeException(e);
		}
	}
	
	private void sendTextMessage(Long chatId, String text) {
		SendMessage message = new SendMessage();
		
		message.setChatId(chatId);
		message.setText(text);
		
		try {						
			sendMessage(message);
		} catch (TelegramApiException e) {
			logger.log(Level.SEVERE, "Exception during message sending");
			throw new RuntimeException(e);
		}
	}
	
	private List<CoinDeskPost> getFreshPosts(List<CoinDeskPost> receivedPosts, List<CoinDeskPost> recentPosts) {
		List<CoinDeskPost> freshPosts = new ArrayList<>();
		
		for (CoinDeskPost post : recentPosts) {
			boolean fresh = true;
			
			for (CoinDeskPost receivedPost : receivedPosts) {
				if (post.getArticleTitle().equals(receivedPost.getArticleTitle()) 
						&& post.getDate().equals(receivedPost.getDate())) {
					fresh = false;
					break;
				}
			}
			
			if (!fresh)	{
				continue;
			}
			
			freshPosts.add(post);
		}
		
		return freshPosts;
	}

}
