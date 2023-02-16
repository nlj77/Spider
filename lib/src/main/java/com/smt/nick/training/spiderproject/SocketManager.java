
package com.smt.nick.training.spiderproject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import lombok.Data;

/****************************************************************************
 * <b>Title:</b> SocketManager.java<br>
 * <b>Project:</b> Spider-lib<br>
 * <b>Description:</b> <br>
 * <b>Copyright:</b> Copyright (c) 2023<br>
 * <b>Company:</b> Silicon Mountain Technologies<br>
 * 
 * @author Nick Jones
 * @version 1.0
 * @since Feb 14 2023
 * @updates:
 ****************************************************************************/
@Data
public class SocketManager {
		//logger object for use in logging to console, could add langpack.
		final Logger logger = Logger.getLogger(StringBuilder.class.getName());
		// Gets the working directory from the user
		static final String DIR = System.getProperty("user.dir") + "/src/main/resources/output";
		//HTTPS for SSL Socket factory
		static final int PORT = 443;
		//Sets a starting URL
		private String rootURL = "";
		
		public StringBuilder getPage(String host, String extension, int portNumber) {
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
				out.writeBytes("GET " + extension + " HTTP/1.0 \r\n");
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
