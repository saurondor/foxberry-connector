/**
 * 
 */
package com.tiempometa.speedway;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;

/**
 * @author Gerardo Tasistro
 * 
 */

class InputListener implements Runnable {

	private static final Logger logger = Logger.getLogger(InputListener.class);
	private InputStream inputStream = null;
	private boolean listening = true;
	private CommandListener commandListener = null;

	public InputListener(InputStream inputStream, CommandListener commandListener) {
		super();
		this.inputStream = inputStream;
		this.commandListener = commandListener;
	}

	@Override
	public void run() {
		BufferedInputStream bis = new BufferedInputStream(inputStream);
		while (listening) {
			try {
				if (bis.available() > 0) {
					byte[] data = new byte[bis.available()];
					int readBytes = bis.read(data);
					String dataString = new String(data);
					logger.info(dataString);
					commandListener.notifyCommand(dataString);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}