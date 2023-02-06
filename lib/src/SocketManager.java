
package com.smt.nick.training.spiderproject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.apache.commons.io.FileUtils;
import org.w3c.dom.Node;

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
	static final String OUTPUTLOCATION = DIR + "/src/main/resources/output.html";
	private static URL url = null;
	static final int PORT = 443;
//	static final String ADDRESS = "https://smt-stage.qa.siliconmtn.com";
	static final String BASEURI = "https://smt-stage.qa.siliconmtn.com/";
	static final String ADDRESS = URL.getHost();

	
    public static void main(String[] args) throws Exception {
    	SocketManager socketManager = new SocketManager();
    	socketManager.writeHTMLToFile(OUTPUTLOCATION, socketManager.getWebPage(BASEURI, PORT));
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
			out.writeBytes("GET /HTTP/1.1 \r\n");
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
	
	public void writeHTMLToFile(String OUTPUTLOCATION, StringBuilder html) {
		String htmlString = html.toString();
		File input = new File(htmlString);
        final File output = new File("filename.html");
		try {
			Document doc = Jsoup.parse(input, "UTF-8", BASEURI);
	        FileUtils.writeStringToFile(output, doc.outerHtml(), StandardCharsets.UTF_8);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}