package com.dkohut.telegrambots.logging;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CoinDeskLogger {
	
	public static void log(String firstName, String lastName, long userId, String text) {
		System.out.println("\n------------------------------");
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		System.out.println(dateFormat.format(date));
		System.out.println("From: " + firstName + " " + lastName + "(" + userId + ")");
		System.out.println("Text: " + text + "\n");
	}
	
}
