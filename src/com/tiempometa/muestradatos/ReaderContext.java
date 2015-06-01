/**
 * 
 */
package com.tiempometa.muestradatos;

import java.io.File;

import org.apache.commons.codec.binary.Hex;
import org.apache.log4j.Logger;

import com.thingmagic.Gen2.WriteTag;
import com.thingmagic.Reader.GpioPin;
import com.thingmagic.Gen2;
import com.thingmagic.ReaderCodeException;
import com.thingmagic.ReaderException;
import com.thingmagic.TagData;
import com.tiempometa.thingmagic.UsbReader;

/**
 * @author Gerardo Tasistro
 * 
 */
public class ReaderContext {

	private static final Logger logger = Logger.getLogger(ReaderContext.class);
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
	
	public static void addReaderStatusListener(ReaderStatusListener listener) {
		reader.addReaderStatusListener(listener);
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
		reader.stop();
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
	 * @param databaseFile
	 *            the databaseFile to set
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
	
	public static void setRedOn() throws ReaderException {
		GpioPin[] pins = new GpioPin[1];
		pins[0] = new GpioPin(2, true);
		reader.gpoSet(pins);
	}
	
	public static void setRedOff() throws ReaderException {
		GpioPin[] pins = new GpioPin[1];
		pins[0] = new GpioPin(2, false);
		reader.gpoSet(pins);
	}
	
	public static void setOrangeOn() throws ReaderException {
		GpioPin[] pins = new GpioPin[1];
		pins[0] = new GpioPin(1, true);
		reader.gpoSet(pins);
	}
	
	public static void setOrangeOff() throws ReaderException {
		GpioPin[] pins = new GpioPin[1];
		pins[0] = new GpioPin(1, false);
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
			throw new IndexOutOfBoundsException(
					"Pin id beyond available inputs.");
		}
	}

	public static void executeTagOp(WriteTag tagop, TagData target)
			throws ReaderException {
		reader.executeTagOp(tagop, target);
	}

	public static String readTid(String epc, Integer tid_length)
			throws ReaderException {
		String tid = null;
		TagData target = new TagData(epc);
		if (tid_length == null) {
			tid_length = 16;
		}
		try {
			tid = String.valueOf(Hex.encodeHex(ReaderContext.readTagMemBytes(
					target, Gen2.Bank.TID.ordinal(), 0, tid_length)));
			logger.debug("Got " + tid_length + " byte TID");
		} catch (Exception e2) {
			logger.error("Error reading " + tid_length + " byte TID. Falling back to 12 bit TID.");
			e2.printStackTrace();
			tid = String.valueOf(Hex.encodeHex(ReaderContext.readTagMemBytes(
					target, Gen2.Bank.TID.ordinal(), 0, 12)));
			logger.debug("Got 12 byte TID");
		}
		return tid;
	}

}
