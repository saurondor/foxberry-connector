/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.tiempometa.speedway;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;

/**
 * @author Gerardo Tasistro gtasistro@tiempometa.com
 * Copyright 2015 Gerardo Tasistro
 * Licensed under the Mozilla Public License, v. 2.0
 * 
 */
public class SpeedwayEmulator {
	private static final Logger logger = Logger
			.getLogger(SpeedwayEmulator.class);

	private int port = 14150; // use default port
	private String hostname = "";
	private ServerSocket serverSocket;
	private InputStream dataInputStream = null;
	private OutputStream dataOutputStream = null;
	private boolean serverOn = true;

	public void run() throws UnknownHostException, IOException {
		Socket clientSocket = null;
		logger.info("Starting socket server");
		serverSocket = new ServerSocket(port);
		logger.info("Successfully started socket server on port :" + port);

		while (serverOn) {
			clientSocket = serverSocket.accept();
			logger.info("Connection accepted, starting thread");
			Thread thread = new Thread(new ReaderEmulator(clientSocket));
			thread.start();
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SpeedwayEmulator emulator = new SpeedwayEmulator();
		try {
			emulator.run();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
