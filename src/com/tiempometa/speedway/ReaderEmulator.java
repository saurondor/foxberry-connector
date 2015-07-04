/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.tiempometa.speedway;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Random;

import org.apache.log4j.Logger;

/**
 * @author Gerardo Tasistro gtasistro@tiempometa.com
 * Copyright 2015 Gerardo Tasistro
 * Licensed under the Mozilla Public License, v. 2.0
 * 
 */
public class ReaderEmulator implements Runnable, CommandListener {

	private static final Logger logger = Logger.getLogger(ReaderEmulator.class);
	private Socket clientSocket = null;
	private boolean transmitting = true;
	private String response = null;

	public ReaderEmulator(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		try {
			OutputStream os = clientSocket.getOutputStream();
			Thread inputThread = new Thread(new InputListener(
					clientSocket.getInputStream(), this));
			inputThread.start();
			Random rand = new Random();
			while (transmitting) {
				synchronized (this) {
					if (response == null) {
						for (int j = 0; j < 100000; j+=10) {
							int random = 1 * (rand.nextInt(10) + 1);
							try {
								Thread.sleep(50);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
//							for (int k = 0; k <1; k++) {
								String dataRow = String.valueOf(j) + ",3008,1432,-"
										+ String.valueOf(j) + ",E280,\n";
//								logger.debug(dataRow);
								os.write(dataRow.getBytes());								
//							}
							os.flush();
						}
					} else {
						os.write(response.getBytes());
						os.write("\\n".getBytes());
						os.flush();
						logger.debug("Sent data!");
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void notifyCommand(String command) {
		synchronized (this) {
			response = command;
		}

	}

}
