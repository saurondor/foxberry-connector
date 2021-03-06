/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.tiempometa.speedway;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.tiempometa.muestradatos.ReaderStatusListener;
import com.tiempometa.muestradatos.TagReadListener;
import com.tiempometa.muestradatos.TagReading;

/**
 * @author Gerardo Tasistro gtasistro@tiempometa.com
 * Copyright 2015 Gerardo Tasistro
 * Licensed under the Mozilla Public License, v. 2.0
 * 
 */
public class TcpReader implements Runnable {

	private static final Logger logger = Logger.getLogger(TcpReader.class);

	private int port = 14150; // use default port
	private String hostname = "";
	private Socket readerSocket = null;
	private InputStream dataInputStream = null;
	private OutputStream dataOutputStream = null;
	private List<TagReadListener> listeners = new ArrayList<TagReadListener>();
	private List<ReaderStatusListener> statusListeners = new ArrayList<ReaderStatusListener>();
	private String preferredAntenna = null;
	private boolean doReadings = false;

	private void openSocket() throws UnknownHostException, IOException {
		logger.info("Connecting to " + hostname + " - " + port);
		readerSocket = new Socket(hostname, port);
		notifyConnected();
	}

	public void connect() throws UnknownHostException, IOException {
		openSocket();
		notifyConnected();
	}

	public void connect(String hostname) throws UnknownHostException,
			IOException {
		this.hostname = hostname;
		openSocket();
		notifyConnected();
	}

	public void connect(String hostname, Integer port)
			throws UnknownHostException, IOException {
		this.hostname = hostname;
		this.port = port;
		openSocket();
		notifyConnected();
	}

	public void disconnect() throws IOException {
		readerSocket.close();
		notifyDisconnected();
	}

	@Override
	public void run() {
		try {
			dataInputStream = readerSocket.getInputStream();
			dataOutputStream = readerSocket.getOutputStream();
			boolean read = true;
			while ((read) & (!readerSocket.isClosed())) {
				synchronized (this) {
					read = doReadings;
				}
				logger.info("Socket is open");
				while (readerSocket.isConnected()) {
					if (readerSocket.isClosed()) {
						logger.warn("Socket is closed!");
					}
					logger.info("Socket is connected");

					int dataInStream;
					try {
						dataInStream = dataInputStream.available();
						if (dataInStream < 0) {
							logger.warn("Less than 0 bytes available, disconnect?");
						}
						logger.info("pinging server");
						dataOutputStream.write("ping".getBytes());
						dataOutputStream.flush();
						logger.info("pinged server!");
						if (dataInStream > 0) {
							byte[] b = new byte[dataInStream];
							dataInputStream.read(b);
							String dataString = new String(b);
							if (logger.isDebugEnabled()) {
								logger.debug("DATA>\n" + dataString + "\nLEN:"
										+ dataString.length());
							}
							StringBuffer buffer = new StringBuffer((new Date())
									+ " " + dataString);
							String[] dataRows = dataString.split("\\n");
							for (String string : dataRows) {
								logger.debug(string);
								TagReading reading = new TagReading(string);
								logger.debug(reading);
								List<TagReading> readings = new ArrayList<TagReading>();
								readings.add(reading);
								notifyListeners(readings);
							}
						} else {
							logger.info("No data");
						}
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						try {
							disconnect();
							connect();
							dataInputStream = readerSocket.getInputStream();
							dataOutputStream = readerSocket.getOutputStream();
						} catch (IOException e2) {
							e2.printStackTrace();
						}
					}
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

	}

	public void connect(String hostName, Integer port, String preferredAntenna)
			throws UnknownHostException, IOException {
		this.hostname = hostName;
		if (port != null) {
			this.port = port;
		}
		this.preferredAntenna = preferredAntenna;
		openSocket();

	}

	public void addListener(TagReadListener listener) {
		listeners.add(listener);

	}

	public void removeListener(TagReadListener listener) {
		listeners.remove(listener);

	}

	private void notifyListeners(List<TagReading> readings) {
		for (TagReadListener listener : listeners) {
			listener.handleReadings(readings);
		}
	}

	public void addReaderStatusListener(ReaderStatusListener listener) {
		statusListeners.add(listener);
	}

	public void removeReaderStatusListener(ReaderStatusListener listener) {
		statusListeners.remove(listener);
	}

	private void notifyConnected() {
		for (ReaderStatusListener listener : statusListeners) {
			listener.tcpConnected();
		}
	}

	private void notifyDisconnected() {
		for (ReaderStatusListener listener : statusListeners) {
			listener.tcpDisconnected();
		}
	}

	private void notifyStartedReading() {
		for (ReaderStatusListener listener : statusListeners) {
			listener.usbStartedReading();
		}
	}

	private void notifyStoppedReading() {
		for (ReaderStatusListener listener : statusListeners) {
			listener.usbStoppedReading();
		}
	}

	public void stop() {
		synchronized (this) {
			doReadings = false;
		}
	}
}
