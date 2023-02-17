
package com.smt.nick.training.spiderproject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Properties;
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
		static final String DIR = System.getProperty("user.dir") + "/src/main/resources/";
		//HTTPS for SSL Socket factory
		static final int PORT = 443;
		//Sets a starting URL
		private String rootURL = "";
		//Set a config file
		static final String CONF = "src/main/resources/signininfo.conf";
		//Set up cookies
		private List<String> cookies;
		
				
		public StringBuilder getPage(String host, String extension, int portNumber) {
			//instantiates a new Stringbuilder object called html
			StringBuilder html = new StringBuilder();
			//try  creates a new socket object from the fed in portNumber and host
			try {
				SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
				//create socket object from SSLSocketFactory, note it gives a suggestion about using a try with resources but SSLSocketFactory is not auto-closeable
				SSLSocket socket = (SSLSocket) factory.createSocket(host, portNumber);
				//logs to console that a socket has been successfully created
				logger.log(Level.INFO, "Socket Created");
				//create a dedicated out stream
				DataOutputStream out = new DataOutputStream(socket.getOutputStream());
				//create a dedicated in stream from a buffered reader object
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				//log info for success
				logger.log(Level.INFO, "Streams Created");
				//write request + the extension (if there is one) plus the protocol and new line
				out.writeBytes("GET " + extension + " HTTP/1.0 \r\n");
				//append host and new line
				out.writeBytes("Host: " + host + "\r\n");
				//new line
				out.writeBytes("\r\n");
				//close data
				out.flush();
				//create a string for the incoming data
				String inData = null;
				//while loop for each line in the incoming data that isn't null
				while ((inData = in.readLine()) != null) {
					//add to the Stringbuilder object the line plus a break
					html.append(inData + "\n");
				}
			//catch if there is a connection to the server error
			} catch (Exception e) {
				logger.log(Level.INFO, "Unable to connect to Server", e);
			}
			//return the StringBuilderObject
			return html;
		}
		/**
		 * 
		 * @return logininfo from a given config file in resources
		 */
		public String getConfig() {
			//Creates a new file object
			File configFile = new File(CONF);
			//sets the String to null before resetting
			String loginInfo = null;
			//Try with resources to create a FileReader object from the configFile
			try(FileReader reader = new FileReader(configFile)){
					//Using the Properties class create a new prop object
				    Properties prop = new Properties();
				    //load that reader file
				    prop.load(reader);
				    //set loginInfo string to key on conf file
				    loginInfo = prop.getProperty("login");
			//catch if there isn't a config file
			} catch (FileNotFoundException e1) {
				logger.log(Level.INFO, "Couldn't find config file");
			//catch if there isn't a key in the file
			} catch (IOException e1) {
				logger.log(Level.INFO, "Couldn't read key from file");
			}
			//return the logininfo string
			return loginInfo;
		}

		public void postToPage(String host, String extension, int portNumber) {
			
			//create a new login info string from getConfig
			String login = getConfig();
			//StringBuilder object to store intended output
			StringBuilder output = new StringBuilder();
			//new writer object
			Writer writer = new Writer();
			//try  creates a new socket object from the fed in portNumber and host

			try {
				
				SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
				//create socket object from SSLSocketFactory, note it gives a suggestion about using a try with resources but SSLSocketFactory is not auto-closeable

				SSLSocket socket = (SSLSocket) factory.createSocket(host, portNumber);
				// logs to console that a socket has been successfully created
				logger.log(Level.INFO, "Socket Created");
				//intended output stream
				DataOutputStream out = new DataOutputStream(socket.getOutputStream());
				logger.log(Level.INFO, "Streams Created");
				// Sending through intended Post request header with extension
				out.writeBytes("POST " + extension + " HTTP/1.0\r\n");
				//given extension
				out.writeBytes("Host: " + host + "\r\n");
				//adding my login credentials 
				out.writeBytes("Content-Length: " + login.length() + "\r\n");
				//Apply html coding
				out.writeBytes("Content-Type: application/x-www-form-urlencoded\r\n");
				//include new line
				out.writeBytes("\r\n");
				//pass in login info, and new line
				out.writeBytes(login + "\r\n");
				//close
				out.flush();
				
				//new reader object
				BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				//String html to set later
				String html = "";
				//append the given string while loop
				while ((html = reader.readLine()) != null) {
					//append html to output stringbuilder object, and new line
					output.append(html + "\r\n");
					//Check if the given html line has a Set-Cookie in front
					if (html.startsWith("Set-Cookie")) {
						//add that piece of html with the cookie header into my cookies list
						cookies.add(html.substring(12, (html.indexOf(";") + 1)));
					}
				}
				//run a write to file with writer object
				writer.writeHTMLToFile(output);
				//close socket
				out.close();
				//close reader
				reader.close();
		    //catch given an inability to connect to server
			} catch (Exception e) {
				logger.log(Level.INFO, "Unable to connect to Server", e);
			}
		}
		/**
		 * 
		 * @return cookies to string object for passing through later.
		 */
		public String pullCookies() {
			StringBuilder cookieSet = new StringBuilder();
			for (String cookie : cookies) {
				cookieSet.append(cookie);
			}
			return cookieSet.toString();
		}
	}
		

