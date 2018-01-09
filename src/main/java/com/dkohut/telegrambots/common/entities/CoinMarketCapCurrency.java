package com.dkohut.telegrambots.common.entities;

public class CoinMarketCapCurrency {
	
	private String name;
	private String value;
	private String percentChange;
	
	public CoinMarketCapCurrency() {
		// default constructor
	}
	
	public CoinMarketCapCurrency(String name, String value, String percentChange) {
		this.name = name;
		this.value = value;
		this.percentChange = percentChange;
	}
	
	public String getPercentChange() {
		return percentChange;
	}

	public void setPercentChange(String percentChange) {
		this.percentChange = percentChange;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}	
	
}
