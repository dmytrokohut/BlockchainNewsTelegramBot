package com.dkohut.telegrambots.common.interfaces;

import java.util.List;

import com.dkohut.telegrambots.common.entities.CoinDeskPost;
import com.dkohut.telegrambots.common.entities.CoinMarketCapCurrency;

public interface ICoinDeskParser {

	/**
	 * Returns the list of posts from CoinDesk site.
	 * Post contains the title of the article, the author, date of publishing and
	 * link to the full article.
	 * 
	 * @return list of posts
	 */
	List<CoinDeskPost> getPosts();
	
	/**
	 * Returns the list of currencies from CoinMarketCap site.
	 * Currency is an object that contains name of currency, current price and
	 * the percent of changing of the currency.
	 * 
	 * @return list of currencies
	 */
	List<CoinMarketCapCurrency> getPrices();
}
