package com.dkohut.telegrambots.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.dkohut.telegrambots.common.entities.CoinMarketCapCurrency;
import com.dkohut.telegrambots.common.interfaces.ICoinDeskParser;
import com.dkohut.telegrambots.common.entities.CoinDeskPost;

public class CoinDeskParser implements ICoinDeskParser {
	
	private static final Logger logger = Logger.getLogger(CoinDeskParser.class.getName());	
	private static final String COIN_DESK_URL = "https://www.coindesk.com/";
	private static final String COIN_MARKET_CAP_URL = "https://coinmarketcap.com/";
	
	private static final String[] CURRENCIES_ID = {
			"id-bitcoin", 
			"id-ethereum", 
			"id-bitcoin-cash", 
			"id-monero",
			"id-zcash"
		};
	
	/**
	 * @see com.dkohut.telegrambots.common.interfaces.ICoinDeskParser#getPosts()
	 */
	public List<CoinDeskPost> getPosts() {
		try {
			List<CoinDeskPost> listOfPosts = new ArrayList<>();
			
			Document doc = Jsoup.connect(COIN_DESK_URL).get();
			Elements posts = doc.getElementsByAttributeValue("class", "post-info");
			
			for (Element post : posts) {				
				CoinDeskPost coinDeskPost = new CoinDeskPost();
				
				Elements attributes = post.getElementsByAttributeValue("class", "fade");				
				for (Element attr : attributes) {
					coinDeskPost.setArticleLink(attr.attr("href"));
					coinDeskPost.setArticleTitle(attr.text());
				}
				
				attributes = post.getElementsByAttributeValue("class", "timeauthor");
				for (Element attr : attributes) {
					String[] timeAuthor = attr.text().split(Pattern.quote("|"));
					
					coinDeskPost.setDate(timeAuthor[0]);
					coinDeskPost.setAuthor(timeAuthor[1]);
				}
				
				listOfPosts.add(coinDeskPost);
			}
			
			return listOfPosts;
					
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Exception during document loading");
			throw new RuntimeException(e);
		}
		
	}
	
	/**
	 * @see com.dkohut.telegrambots.common.interfaces.ICoinDeskParser#getPrices()
	 */
	public List<CoinMarketCapCurrency> getPrices() {
		try {
			List<CoinMarketCapCurrency> currencyList = new ArrayList<>();
			
			Document doc = Jsoup.connect(COIN_MARKET_CAP_URL).get();
			
			for(String currency : CURRENCIES_ID) {
				Element currencyInfo = doc.getElementById(currency);
				
				Elements name = currencyInfo.getElementsByAttributeValue("class", "currency-name-container");
				
				String prices = currencyInfo.getElementsByAttribute("data-usd").text();
				
				String price = prices.split(Pattern.quote(" "))[1];
				String percentChange = prices.split(Pattern.quote(" "))[3];
				
				currencyList.add(new CoinMarketCapCurrency(name.text(), price, percentChange));
			}			
			
			return currencyList;
			
		} catch(IOException e) {
			logger.log(Level.SEVERE, "Exception during document loading");
			throw new RuntimeException(e);	
		}		
	}
	
}
