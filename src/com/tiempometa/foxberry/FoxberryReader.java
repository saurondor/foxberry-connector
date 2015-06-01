/**
 * 
 */
package com.tiempometa.foxberry;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.tiempometa.muestradatos.ReaderStatusListener;
import com.tiempometa.muestradatos.TagReadListener;
import com.tiempometa.muestradatos.TagReading;

/**
 * @author Gerardo Tasistro
 * 
 */
public class FoxberryReader implements Runnable {

	private static final Logger logger = Logger.getLogger(FoxberryReader.class);

	private int port = 10200; // use default port
	private String hostname = "";
	private Socket readerSocket = null;
	private InputStream dataInputStream = null;
	private OutputStream dataOutputStream = null;
	private List<TagReadListener> listeners = new ArrayList<TagReadListener>();
	private List<ReaderStatusListener> statusListeners = new ArrayList<ReaderStatusListener>();
	private String preferredAntenna = null;
	private String preferredReader = null;
	private boolean doReadings = false;

	public void connect(String hostName, Integer port, String preferredReader,
			String preferredAntenna) throws UnknownHostException, IOException {
		this.hostname = hostName;
		if (port != null) {
			this.port = port;
		}
		this.preferredAntenna = preferredAntenna;
		this.preferredReader = preferredReader;
		openSocket();
		notifyConnected();
	}

	private void openSocket() throws UnknownHostException, IOException {
		readerSocket = new Socket(hostname, port);
	}

	public void disconnect() throws IOException {
		readerSocket.close();
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
		boolean read = true;
		while (read) {
			synchronized (this) {
				read = doReadings;
			}

		}

	}

	public void stop() {
		synchronized (this) {
			doReadings = false;
		}
	}

}
