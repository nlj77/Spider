
package com.smt.nick.training.spiderproject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import lombok.Data;

/****************************************************************************
 * <b>Title:</b> Parser.java<br>
 * <b>Project:</b> Spider-lib<br>
 * <b>Description:</b> <br>
 * <b>Copyright:</b> Copyright (c) 2023<br>
 * <b>Company:</b> Silicon Mountain Technologies<br>
 * 
 * @author Nick Jones
 * @version 1.0
 * @since Feb 07 2023
 * @updates:
 ****************************************************************************/
@Data
public class Parser {
	// this sets up a variable to store the starting link String is the url, and boolean for saved true or false
	private HashMap<String, Boolean> urlMap;
	// Declares a logger object. For scaling purposes, being able to log successes
	// and errors will be useful if I add functionality to my lang pack
	final Logger logger = Logger.getLogger(StringBuilder.class.getName());
	// Sets a final constant for a HTTPS port
	static final int PORT = 443;
	// Gets the working directory from the user, if this is cloned and pulled to a
	// different user's local repo
	static final String DIR = System.getProperty("user.dir") + "/src/main/resources/";
	
	
	  public static void main(String[] args) throws IOException {
	    	SocketManager sock = new SocketManager();
	    	Parser parser = new Parser();
	    	sock.setRootUrl("https://smt-stage.qa.siliconmtn.com");
	    	System.out.println(sock.getRootUrl());
	    	parser.writeHTMLToFile((sock.getWebPage(sock.getRootUrl(), PORT)), sock);
	    	parser.parseHTML(parser.writeHTMLToFile((sock.getWebPage(sock.getRootUrl(), PORT)), sock), sock);
	    }
		public Parser() {
			urlMap = new HashMap<>();
		}
		
		public void writeQueueToFiles(Map<String, Boolean> urls, SocketManager socket) {
			
		}
	/**
	 * 
	 * @param htmlDoc this is the return type of the SocketManager getWebpage function
	 * @param socket this is the socket manager object
	 * @return
	 * @throws IOException if file couldn't be handled
	 */
	public String writeHTMLToFile(StringBuilder htmlDoc, SocketManager socket) throws IOException {
		//creates a string from the StringBuilder htmlDoc, that SocketManager getWebPage returns
		String htmlDocStr = htmlDoc.toString().toLowerCase();
		//pulls the title from the htmlDoc, this is useful for displaying progress or checking for errors
		String title = htmlDocStr.substring(htmlDocStr.indexOf("<title>") + 7, htmlDocStr.indexOf("</title>"));
		//creates a savedFilePath using DIR and the title
		String savedFilePath = (SocketManager.DIR + title + ".html");
		//Creates a new FileWriter Object at the savedFilePath location
		FileWriter input = new FileWriter(savedFilePath); 
		//Try with resources, create a BufferedWriter object and use the input location
	    try (BufferedWriter buffer = new BufferedWriter(input)) {
	    	//Try to write the htmlDocStr
			try {
				buffer.write(htmlDocStr);
			//Write to logger if there's an interface option exception
			} catch (IOException e) {
				logger.log(Level.INFO, "Could not successfully write document", e);
			}
		}
	    //log if successful
		logger.log(Level.INFO, "Created " + savedFilePath + " file in " + SocketManager.DIR);
	    return savedFilePath;
	}
	/**
	 * 
	 * @param htmlPath this is the string for the saved file path of the designated starting url
	 * @param socket is a fed in created SocketManager object 
	 * @return urlMap. This can be printed or logged, and will be used to loop through. 
	 */
	public Map<String, Boolean> parseHTML(String htmlPath, SocketManager socket) {
		try {
			File input = new File(htmlPath);

			Document doc = Jsoup.parse(input, "UTF-8", socket.getRootUrl());
			Elements links = doc.select("a[href]");
			for (Element link : links) {
				 String linkHref = link.attr("href");
				 if(linkHref.contains("tel") || linkHref.contains("mailto") || linkHref.length() <= 1) {
					 continue;
				 }
				logger.log(Level.INFO, "Created " + linkHref + " file in " + DIR);
//				writeHTMLToFile(getWebPage(linkHref, PORT));
				urlMap.put(linkHref, true);
			}

		} catch (FileNotFoundException e) {
			logger.log(Level.INFO, "Could not find file", e);
		} catch (IOException e) {
			logger.log(Level.INFO, "Could not successfully parse links", e);
		}
		System.out.println(urlMap);
		return urlMap;
	}

}
