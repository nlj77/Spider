
package com.smt.nick.training.spiderproject;

import java.io.IOException;



/****************************************************************************
 * <b>Title:</b> Spider.java<br>
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

public class Spider {
	  private static final int PORT = 443;
	  private static final String HOST1 = "smt-stage.qa.siliconmtn.com";

	public static void main(String[] args) throws IOException {
		crawl(HOST1);

}
	  
	  public static void crawl(String host) throws IOException {
			SocketManager sock = new SocketManager();
	    	Writer writer = new Writer();
	    	Parser parser = new Parser();
	    	sock.setRootURL(host);
	    	System.out.println(sock.getRootURL());
	    	String startPath = writer.writeHTMLToFile((sock.getPage(sock.getRootURL(),"/", PORT)));
	    	parser.parseHTML(startPath, sock);
	    	parser.parseQueue(writer, sock);
	     
	  }
}
