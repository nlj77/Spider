//JDK 17
package com.smt.nick.training.spiderproject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
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
 * <b>Description:</b>This parser class is used with my Spider app to strip HTML elements from a given String of an HTML page,
 *  and parse them into a queue <br>
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
	// Set up a new HashMap of cookies
	private Set<String> cookies;
	// Declares a logger object. For scaling purposes, being able to log successes
	// and errors will be useful if I add functionality to my lang pack
	final Logger logger = Logger.getLogger(StringBuilder.class.getName());
	// Sets a final constant for a HTTPS port
	static final int PORT = 443;
	// Gets the working directory from the user, if this is cloned and pulled to a
	// different user's local repo
	static final String DIR = System.getProperty("user.dir") + "/src/main/resources/";
	
	    //Constructor with a new urlMap object for funneling in urls
		public Parser() {
			urlMap = new HashMap<>();
		}
		
	/**
	 * 
	 * @param htmlPath this is the string for the saved file path of the designated starting url
	 * @param socket is a fed in created SocketManager object 
	 * @return urlMap. This can be printed or logged, and will be used to loop through. 
	 */
	public Map<String, Boolean> parseHTML(String htmlPath, SocketManager socket) {
		try {
			//Create a new file from a given file path for input
			File input = new File(htmlPath);
			//Create a Document object from JSOUP, specify encoding as UTF-8, use file input, and get the root url for parsing purposes.
			Document doc = Jsoup.parse(input, "UTF-8", socket.getRootURL());
			//Create a set of links from doc by pulling out every element with a a[href] tag
			Elements links = doc.select("a[href]");
			//Instantiate a loop for every link in links
			for (Element link : links) {
				 //Assign link to a String
				 String linkHref = link.attr("href");
				 //if the link contains telephone, or a mailer, or if it's shorter than 1, skip it, and continue the loop
				 if(linkHref.contains("tel") || linkHref.contains("mailto") || linkHref.length() <= 1) {
					 continue;
				 }
				//put the link into the urlMap, and give it the value of false, as it hasn't been saved yet
				urlMap.put(linkHref, false);
			}
		//throw error if file is not found
		} catch (FileNotFoundException e) {
			logger.log(Level.INFO, "Could not find file", e);
		//log error if links can't be returned
		} catch (IOException e) {
			logger.log(Level.INFO, "Could not successfully parse links", e);
		}
		return urlMap;
	}
	
	/**
	 * 
	 * @param writer fed in writer object
	 * @param sock fed in sock object for use of get page methods
	 * @throws IOException if sock get page isn't found
	 */
	public void parseQueue(Writer writer, SocketManager sock) throws IOException {
		//loop through every key in urlMap.entryset
		 for (Entry<String, Boolean> entry : getUrlMap().entrySet()) {
			 	//assign route "/contact" for example, to a String, this prevents concurrent modification exceptions
				String route = entry.getKey();
				//Call parse HTML function on the StringBuilder object that writeHTMLToFile returns, and the given SocketManager Sock object
				parseHTML(
				writer.writeHTMLToFile(
						sock.getPage(sock.getRootURL(), route, PORT)
						),
			        sock);
			  //As that file has been written, we now want to update the entry's value to true
	          getUrlMap().put(entry.getKey(), true);
	      //Log the path as saved
 		  logger.log(Level.INFO, "Key = " + route +
                   ", Value = " + entry.getValue() + " " + sock.getRootURL()+ route);
 	 }
	}
	


}
