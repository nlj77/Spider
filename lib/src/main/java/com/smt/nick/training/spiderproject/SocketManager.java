
package com.smt.nick.training.spiderproject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
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
public class SocketManager {
	
	// Declares a logger object. For scaling purposes, being able to log successes
	// and errors will be useful if I add functionality to my lang pack
	final Logger logger = Logger.getLogger(StringBuilder.class.getName());
	// Gets the working directory from the user, if this is cloned and pulled to a
	// different user's local repo
	static final String DIR = System.getProperty("user.dir");
	// This sets the output variable to the user's directory plus
	static final String STARTLOCATION = DIR + "/src/main/resources/homepage.html";
	private  URL url = null;
	//This hashmap will be used to store URLs and determine if they've been visited and saved yet.
	Map<Object, Boolean> urlQueue = new HashMap<Object, Boolean>();
	static final int PORT = 443;
//	static final String ADDRESS = "https://smt-stage.qa.siliconmtn.com";
	static final String BASEURI = "https://smt-stage.qa.siliconmtn.com/";
	private String address = null;

	
    public static void main(String[] args) throws Exception {
    	SocketManager socketManager = new SocketManager();
    	URL hostURL = new URL("https://smt-stage.qa.siliconmtn.com/");
    	socketManager.writeHTMLToFile(STARTLOCATION, socketManager.getWebPage("smt-stage.qa.siliconmtn.com", PORT));
    	socketManager.addURLsToQueue(STARTLOCATION);
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
	
	public void setUrl(String urlString) throws MalformedURLException {
		this.url = new URL(urlString);
	}
	public void writeHTMLToFile(String outputPath, StringBuilder htmlDoc) throws IOException {
		String htmlDocStr = htmlDoc.toString();
		FileWriter writer = new FileWriter(outputPath);  
	    try (BufferedWriter buffer = new BufferedWriter(writer)) {
			try {
				buffer.write(htmlDocStr);
			} catch (IOException e) {
				logger.log(Level.INFO, "Could not successfully write document", e);
			}
		}
	}
	
	public void parseHTML() {
		
	}

	public void addURLsToQueue(String fileLocation) throws IOException {
		File input = new File(fileLocation);
		Document doc = Jsoup.parse(input);
//		System.out.println(doc);

		Element content = doc.getElementById("content");


		Elements links = doc.select("a[href]"); // a with href
		for (Element link : links) {
			urlQueue.put(link, false);
			System.out.println(link + "\r\n");

		}
	}

}