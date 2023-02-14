
package com.smt.nick.training.spiderproject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import lombok.Data;

/****************************************************************************
 * <b>Title:</b> SocketManagerRevised.java<br>
 * <b>Project:</b> Spider-lib<br>
 * <b>Description:</b> <br>
 * <b>Copyright:</b> Copyright (c) 2023<br>
 * <b>Company:</b> Silicon Mountain Technologies<br>
 * 
 * @author Nick Jones
 * @version 1.0
 * @since Feb 08 2023
 * @updates:
 ****************************************************************************/
@Data
public class SocketManager {
	// Declares a logger object. For scaling purposes, being able to log successes
	// and errors will be useful if I add functionality to my lang pack
	final Logger logger = Logger.getLogger(StringBuilder.class.getName());
	// Gets the working directory from the user, if this is cloned and pulled to a
	// different user's local repo
	static final String DIR = System.getProperty("user.dir") + "/src/main/resources/";
	//Holds a designated starting url point and trim down a given url, as SSLSocket factory does not take the https part of a url
	private String rootUrl = "";
	//This is for adding on a given path provided a query to a domain
	private String path = "";
	//Sets a final constant for a HTTPS port
	static final int PORT = 443;
	private HashMap<String, Boolean> urlMap;
	

    
    /**
     * @param url is fed in as a full url including https, and then trimmed down.
     */
    public void setRootUrl(String url) {
    	this.rootUrl = url.substring(8);
    }
 
	
	public StringBuilder getWebPage(String host, int portNumber) {

		// instantiates a new Stringbuilder object called html
		StringBuilder html = new StringBuilder();
		// try with resources creates a new socket object from the fed in portNumber and
		// host

		try {
			SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
			SSLSocket socket = (SSLSocket) factory.createSocket(host, portNumber);
			// logs to console that a socket has been successfully created
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

}
