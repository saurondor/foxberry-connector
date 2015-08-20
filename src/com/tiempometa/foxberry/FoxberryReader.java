/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.tiempometa.foxberry;

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
 * @author Gerardo Tasistro gtasistro@tiempometa.com Copyright 2015 Gerardo
 *         Tasistro Licensed under the Mozilla Public License, v. 2.0
 * 
 */
public class FoxberryReader implements Runnable {

	private static final Logger logger = Logger.getLogger(FoxberryReader.class);

	private int port = 10201; // use default port
	private String hostname = "";
	private Socket readerSocket = null;
	private InputStream dataInputStream = null;
	private OutputStream dataOutputStream = null;
	private List<TagReadListener> listeners = new ArrayList<TagReadListener>();
	private List<ReaderStatusListener> statusListeners = new ArrayList<ReaderStatusListener>();
	private String preferredAntenna = null;
	private String preferredReader = null;
	private boolean doReadings = false;
	
	public boolean isConnected() {
		if ((dataInputStream == null)||(dataOutputStream == null)) {
			return false;
		} else {
			return true;
		}
 	}

	public void connect(String hostName, Integer port, String preferredReader,
			String preferredAntenna) throws UnknownHostException, IOException {
		this.hostname = hostName;
		if (port != null) {
			this.port = port;
		}
		this.preferredAntenna = preferredAntenna;
		this.preferredReader = preferredReader;
		logger.info("Opening socket");
		openSocket();
		logger.info("Socket opened");
		notifyConnected();
		logger.info("Notify successful connect");
	}

	private void connect() throws UnknownHostException, IOException {
		logger.info("Opening socket");
		openSocket();
		logger.info("Socket opened");
		notifyConnected();
		logger.info("Notify successful connect");

	}

	private void openSocket() throws UnknownHostException, IOException {
		readerSocket = new Socket(hostname, port);
	}

	public void disconnect() throws IOException {
		doReadings = false;
		readerSocket.close();
		dataInputStream = null;
		dataOutputStream = null;
		notifyDisconnected();

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
			listener.startedReading();
		}
	}

	private void notifyStoppedReading() {
		for (ReaderStatusListener listener : statusListeners) {
			listener.stoppedReading();
		}
	}

	public void addReaderStatusListener(ReaderStatusListener listener) {
		statusListeners.add(listener);
	}

	public void removeReaderStatusListener(ReaderStatusListener listener) {
		statusListeners.remove(listener);
	}

	@Override
	public void run() {
		try {
			dataInputStream = readerSocket.getInputStream();
			dataOutputStream = readerSocket.getOutputStream();
			boolean read = true;
			while ((read) & (this.isConnected())) {
				synchronized (this) {
					read = doReadings;
				}
				logger.info("Socket is open");
				while (this.isConnected()) {
					if (readerSocket.isClosed()) {
						logger.warn("Socket is closed!");
					}

					int dataInStream;
					try {
						dataInStream = dataInputStream.available();
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
								if (reading.isValid()) {
									logger.debug(reading);
								} else {
									logger.debug(reading);
								}
								List<TagReading> readings = new ArrayList<TagReading>();
								readings.add(reading);
								notifyListeners(readings);
							}
						} else {
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

	public void stop() {
		synchronized (this) {
			doReadings = false;
		}
	}

	public void rewind() throws IOException {
		dataOutputStream.write("rewind\n".getBytes());
		dataOutputStream.flush();
		
	}

	public void clear() throws IOException {
		dataOutputStream.write("clear\n".getBytes());
		dataOutputStream.flush();
		
	}

}
