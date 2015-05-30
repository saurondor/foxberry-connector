/**
 * 
 */
package com.tiempometa.muestradatos;

import java.io.File;

import com.thingmagic.Gen2.WriteTag;
import com.thingmagic.Reader.GpioPin;
import com.thingmagic.ReaderException;
import com.thingmagic.TagData;
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
	
	public static boolean setRegion(String regionName) throws ReaderException {
		return reader.setRegion(regionName);
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

	public static void disconnectUsbReader() throws ReaderException {
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

	public static byte[] readTagMemBytes(TagData target, int bank, int start,
			int size) throws ReaderException {
		return reader.readTagMemBytes(target, bank, start, size);
	}

	public static void gpoSet(GpioPin[] pins) throws ReaderException {
		reader.gpoSet(pins);
		
	}
	
	public static GpioPin[] getGpo() throws ReaderException {
		return reader.gpoGet();
	}
	
	public static boolean getGpo(int gpi) throws ReaderException {
		GpioPin[] pins = getGpo();
		if (gpi < pins.length) {
			return pins[gpi].high;
		} else {
			throw new IndexOutOfBoundsException("Pin id beyond available inputs.");
		}
	}

	public static void executeTagOp(WriteTag tagop, TagData target) throws ReaderException {
		reader.executeTagOp(tagop, target);
	}

}
