/**
 * 
 */
package com.tiempometa.muestradatos;

import java.io.File;

import com.thingmagic.ReaderException;
import com.tiempometa.thingmagic.UsbReader;

/**
 * @author Gerardo Tasistro
 * 
 */
public class ReaderContext {

	private static UsbReader reader = new UsbReader();
	private static Thread workerThread;
	private static File databaseFile;
	
	public static boolean isUsbConnected() {
		return reader.isConnected();
	}

	public static void connectUsbReader(String commPort) throws ReaderException {
		reader.connectToComm(commPort);
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

	public static void disconnectUsbReader() {
		reader.disconnect();
		
	}

	/**
	 * @return the databaseFile
	 */
	public static File getDatabaseFile() {
		return databaseFile;
	}

	/**
	 * @param databaseFile the databaseFile to set
	 */
	public static void setDatabaseFile(File databaseFile) {
		ReaderContext.databaseFile = databaseFile;
	}

}
