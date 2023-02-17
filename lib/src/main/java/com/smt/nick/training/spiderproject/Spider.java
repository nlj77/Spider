//JDK 17
package com.smt.nick.training.spiderproject;

import java.io.IOException;



/****************************************************************************
 * <b>Title:</b> Spider.java<br>
 * <b>Project:</b> Spider-lib<br>
 * <b>Description:</b> This is the main runner for my Spider project,
 * submitted in learning design. <br>
 * <b>Copyright:</b> Copyright (c) 2023<br>
 * <b>Company:</b> Silicon Mountain Technologies<br>
 * 
 * @author Nick Jones
 * @version 1.0
 * @since Feb 14 2023
 * @updates:
 ****************************************************************************/

public class Spider {
	  //Establishes a constant for port
	  private static final int PORT = 443;
	  //establishes an constant for the target host
	  private static final String HOST = "smt-stage.qa.siliconmtn.com";
	  //establishes a String constant for the second part of the assignment
	  private static final String EXTENSION = "/sb/admintool?cPage=index&actionId=WEB_SOCKET&organizationId=SMT";

	public static void main(String[] args) throws IOException {
		//Demonstrate a regular "crawl" action
		crawl(HOST);
		//Demonstrate a crawl with login
		crawlWithLogin(HOST, EXTENSION);

}
	  /**
	   * 
	   * @param host takes a starting string host
	   * @throws IOException throws an exception if a file cannot be written. 
	   */
	  public static void crawl(String host) throws IOException {
		  	//create a new socket manager object
			SocketManager sock = new SocketManager();
			//new writer object
	    	Writer writer = new Writer();
	    	//new parser object
	    	Parser parser = new Parser();
	    	//set the given host as our start root url
	    	sock.setRootURL(host);
	    	//establish starting path name for root url's created and saved file, with a given PORT on the get page call
	    	String startPath = writer.writeHTMLToFile((sock.getPage(sock.getRootURL(),"/", PORT)));
	    	//parse the HTML on the given file path, which adds to queue
	    	parser.parseHTML(startPath, sock);
	    	//run through the queue and call the write function
	    	parser.parseQueue(writer, sock);
	     
	  }
	  /**
	   * 
	   * @param host given String host starting point for a page requiring login
	   * @param extension add on for the second part of assignment
	   * @throws IOException file not found error handling
	   */
	  public static void crawlWithLogin(String host, String extension) throws IOException {
		  	//create a new socket manager object
			SocketManager sock = new SocketManager();
			//new writer object
	    	Writer writer = new Writer();
	    	//new parser object
	    	Parser parser = new Parser();
	    	//set the given host as our start root url
	    	sock.setRootURL(host);
	    	//Call Socket object post function
	    	sock.postToPage(host, extension, PORT);
	    	//call socket retrieve cookies and store function
	    	String cookiez = sock.pullCookies();
	    	//establish starting path name for root url's created and saved file, with a given PORT on the get page call
	    	String startPath = writer.writeHTMLToFile((sock.getPage(sock.getRootURL(),"/", PORT)));
	    	//parse the HTML on the given file path, which adds to queue
	    	parser.parseHTML(startPath, sock);
	    	//run through the queue and call the write function
	    	parser.parseQueue(writer, sock);
	     
	  }
}
