
package com.smt.nick.training.spiderproject;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/****************************************************************************
 * <b>Title:</b> Writer.java<br>
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

public class Writer {
	// Declares a logger object. For scaling purposes, being able to log successes
	// and errors will be useful if I add functionality to my lang pack
	final Logger logger = Logger.getLogger(StringBuilder.class.getName());

	public Writer() {
	}
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

}
