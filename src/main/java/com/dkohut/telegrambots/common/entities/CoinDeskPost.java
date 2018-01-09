package com.dkohut.telegrambots.common.entities;

public class CoinDeskPost {

	private String articleTitle;
	private String articleLink;
	private String author;
	private String date;
	

	public CoinDeskPost() {
		// default constructor
	}
	
	public CoinDeskPost(String articleTitle, String articleLink, String author, String date) {
		this.articleTitle = articleTitle;
		this.articleLink = articleLink;
		this.author = author;
		this.date = date;
	}
	
	public String getArticleTitle() {
		return articleTitle;
	}
	
	public void setArticleTitle(String articleTitle) {
		this.articleTitle = articleTitle;
	}
	
	public String getArticleLink() {
		return articleLink;
	}
	
	public void setArticleLink(String articleLink) {
		this.articleLink = articleLink;
	}
	
	public String getAuthor() {
		return author;
	}
	
	public void setAuthor(String author) {
		this.author = author;
	}
	
	public String getDate() {
		return date;
	}
	
	public void setDate(String date) {
		this.date = date;
	}	
	
}
