
package com.smt.nick.training.spiderproject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import lombok.Data;





/****************************************************************************
 * <b>Title:</b> SocketManager.java<br>
 * <b>Project:</b> Spider-lib<br>
 * <b>Description:</b> This is the Socket Manager for my spider class <br>
 * <b>Copyright:</b> Copyright (c) 2023<br>
 * <b>Company:</b> Silicon Mountain Technologies<br>
 * 
 * @author Nick Jones
 * @version 1.0
 * @since Feb 03 2023
 * @updates:
 ****************************************************************************/
@Data
public class SocketManagerOld {
	
	// Declares a logger object. For scaling purposes, being able to log successes
	// and errors will be useful if I add functionality to my lang pack
	final Logger logger = Logger.getLogger(StringBuilder.class.getName());
	// Gets the working directory from the user, if this is cloned and pulled to a
	// different user's local repo
	static final String DIR = System.getProperty("user.dir") + "/src/main/resources/";

	static final int PORT = 443;
	private String fullAddress = "";
	private String urlPath = "";
	private Queue<String> urlQueue;
	private List<String> visitedURLs;

	
    public static void main(String[] args) throws IOException {
    	SocketManager socketManager = new SocketManager();
    	socketManager.parseHTML(socketManager.writeHTMLToFile(socketManager.getWebPage(socketManager.setUrlPath("smt-stage.qa.siliconmtn.com"), PORT)));
    	socketManager.saveFilesInQueue();
    }
	public SocketManagerOld() {
		urlQueue = new LinkedList<>();
		visitedURLs = new ArrayList<>();
	}
    private String setUrlPath(String string) {
		return this.urlPath = string;

	}

	private String setAddress(String string) {
		return this.fullAddress = string;
	}
	/**
     * 
     * @param host this will be the IP address after a DNS lookup, potential feature to include DNS lookup capability
     * @param portNumber for this homework assignment it will typically be 443 for HTTPS
     * @return a StringBuilder html object to save
     */
	public StringBuilder getWebPage(String host, int portNumber) {

		//instantiates a new Stringbuilder object called html
		StringBuilder html = new StringBuilder();
		//try with resources creates a new socket object from the fed in portNumber and host
		
		try {
			SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
			SSLSocket socket = (SSLSocket) factory.createSocket(host, portNumber);
			//logs to console that a socket has been successfully created
			logger.log(Level.INFO, "Socket Created");
			
			DataOutputStream out = new DataOutputStream(socket.getOutputStream());
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			logger.log(Level.INFO, "Streams Created");
			out.writeBytes("GET / HTTP/1.0 \r\n");
			out.writeBytes("Host: " + host + "\r\n");
			out.writeBytes("\r\n");
			out.flush();
			String inData = null;
			while ((inData = in.readLine()) != null) {
				html.append(inData + "\n");
			}
		} catch (Exception e) {
			logger.log(Level.INFO, "Unable to connect to Server", e);
		}
		return html;
	}
	
	public String writeHTMLToFile(StringBuilder htmlDoc) throws IOException {
		String htmlDocStr = htmlDoc.toString().toLowerCase();
		String title = htmlDocStr.substring(htmlDocStr.indexOf("<title>") + 7, htmlDocStr.indexOf("</title>"));
		String savedFilePath = (DIR + title + ".html");
		FileWriter input = new FileWriter(savedFilePath); 

	    try (BufferedWriter buffer = new BufferedWriter(input)) {
			try {
				buffer.write(htmlDocStr);
			} catch (IOException e) {
				logger.log(Level.INFO, "Could not successfully write document", e);
			}
		}
		logger.log(Level.INFO, "Created " + savedFilePath + " file in " + DIR);
	    return savedFilePath;
	}
	
	public Queue parseHTML(String htmlPath) {
		try {
			File input = new File(htmlPath);

			Document doc = Jsoup.parse(input, "UTF-8", "https://smt-stage.qa.siliconmtn.com/");
			Elements links = doc.select("a[href]");
			for (Element link : links) {
				 String linkHref = link.attr("href");
				 if(linkHref.contains("tel") || linkHref.contains("mailto") || linkHref.length() <= 1) {
					 continue;
				 }
				logger.log(Level.INFO, "Created " + linkHref + " file in " + DIR);
//				writeHTMLToFile(getWebPage(linkHref, PORT));
				urlQueue.add(linkHref);
			}

		} catch (FileNotFoundException e) {
			logger.log(Level.INFO, "Could not find file", e);
		} catch (IOException e) {
			logger.log(Level.INFO, "Could not successfully parse links", e);
		}
		System.out.println(urlQueue);
		return urlQueue;
	}
	
	public void saveFilesInQueue() {
		Queue urls = getUrlQueue();
		for(Object link : urls) {
			urlQueue.remove();
			System.out.println(link);
			visitedURLs.add((String) link);
		}
	}
	

//	public Map<Object, Boolean> addURLsToQueue(String fileLocation) throws IOException {
//		File input = new File(fileLocation);
//		Document doc = Jsoup.parse(input);
////		System.out.println(doc);
//
//		Elements links = doc.getElementsContainingText("a href='/");
//		System.out.println(links);
//		for (Element link : links) {
//			urlQueue.put(link, false);
//			System.out.println(link + "\r\n");
//		}
//		return urlQueue;
//	}
	
	public void saveQueueURLsAsHTMLs() {
		 for(String link: urlQueue) {
			 String outputLocation = DIR + link + ".html";
			 System.out.println(outputLocation + " was created");
			 String absHref = link.attr("abs:href");
			 try {
				writeHTMLToFile(getWebPage(absHref, PORT));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				logger.log(Level.INFO, "Could not successfully save HTMLs in queue", e);
			}
		 }
	}

}