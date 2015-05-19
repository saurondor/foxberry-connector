/**
 * 
 */
package com.tiempometa.muestradatos;

import com.thingmagic.ReaderException;
import com.tiempometa.thingmagic.UsbReader;

/**
 * @author Gerardo Tasistro
 * 
 */
public class ReaderContext {

	private static UsbReader reader = new UsbReader();
	private static Thread workerThread;

	public static void connectUsbReader(String commPort) throws ReaderException {
		reader.connectToComm(commPort);
		startReading();
	}
	
	public static void addReadingListener(TagReadListener listener) {
		reader.addListener(listener);
	}
	
	public static void removeReadingListener(TagReadListener listener) {
		reader.removeListener(listener);
	}

	public static void startReading() throws ReaderException {
		workerThread = new Thread(reader);
		workerThread.start();
	}

	public static void stopReading() {

	}

}
