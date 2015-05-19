/**
 * 
 */
package com.tiempometa.speedway;

import java.io.IOException;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;

/**
 * @author Gerardo Tasistro
 *
 */
public class TcpReaderTest {
	private static final Logger logger = Logger.getLogger(TcpReaderTest.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TcpReader reader = new TcpReader();
		try {
			logger.info("Connecting...");
			reader.connect("speedwayr-11-49-c4.attlocal.net");
			logger.info("Connected!");
			Thread thread = new Thread(reader);
			thread.start();
			try {
				Thread.sleep(60000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			reader.disconnect();
			logger.info("Disconnected!");
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

}
