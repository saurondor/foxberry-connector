/**
 * 
 */
/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.tiempometa.muestradatos;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.log4j.Logger;
import org.llrp.ltk.generated.parameters.ReaderExceptionEvent;

import com.thingmagic.Gen2.Bank;
import com.thingmagic.Gen2.LockAction;
import com.thingmagic.Gen2.WriteTag;
import com.thingmagic.Reader.GpioPin;
import com.thingmagic.Gen2;
import com.thingmagic.Reader;
import com.thingmagic.ReaderCodeException;
import com.thingmagic.ReaderException;
import com.thingmagic.TagData;
import com.thingmagic.TagFilter;
import com.thingmagic.TagReadData;
import com.tiempometa.foxberry.FoxberryReader;
import com.tiempometa.speedway.TcpReader;
import com.tiempometa.thingmagic.UsbReader;

/**
 * @author Gerardo Tasistro gtasistro@tiempometa.com Copyright 2015 Gerardo
 *         Tasistro Licensed under the Mozilla Public License, v. 2.0
 * 
 */
public class ReaderContext {

	public static final int USB_READER = 1;
	public static final int SPEEDWAY_READER = 2;
	public static final int FOXBERRY_READER = 3;

	private static final Logger logger = Logger.getLogger(ReaderContext.class);
	private static UsbReader reader = new UsbReader();
	private static TcpReader speedwayReader = new TcpReader();
	private static FoxberryReader foxberryReader = new FoxberryReader();
	private static Thread workerThread;
	private static File databaseFile;
	private static Integer readerType = null;

	public static boolean isSpeedwayConnected() {
		return false;
	}

	public static boolean isFoxberryConnected() {
		return false;
	}

	public static void connectSpeedway(String hostName, Integer port,
			String preferredAntenna) throws UnknownHostException, IOException {
		speedwayReader.connect(hostName, port, preferredAntenna);
		readerType = ReaderContext.SPEEDWAY_READER;
	}

	public static void connectFoxberry(String hostName, Integer port,
			String preferredReader, String preferredAntenna)
			throws UnknownHostException, IOException {
		logger.info("Connecting to reader " + hostName + ":" + port
				+ " reader: " + preferredReader + " antenna: "
				+ preferredAntenna);
		foxberryReader.connect(hostName, port, preferredReader,
				preferredAntenna);
		readerType = ReaderContext.FOXBERRY_READER;
		workerThread = new Thread(foxberryReader);
		workerThread.start();
	}

	public static void disconnectSpeedway() throws IOException {
		speedwayReader.disconnect();
		readerType = null;
	}

	public static void disconnectFoxberry() throws IOException {
		foxberryReader.disconnect();
		readerType = null;
	}

	public static boolean isUsbReading() {
		return reader.isReading();
	}

	public static boolean isUsbConnected() {
		return reader.isConnected();
	}

	public static boolean setRegion(String regionName) throws ReaderException {
		return reader.setRegion(regionName);
	}

	public static void connectUsbReader(String commPort) throws ReaderException {
		reader.connectToComm(commPort);
		readerType = ReaderContext.USB_READER;
	}

	public static void addReaderStatusListener(ReaderStatusListener listener) {
		reader.addReaderStatusListener(listener);
		speedwayReader.addReaderStatusListener(listener);
		foxberryReader.addReaderStatusListener(listener);
	}

	public static void addReadingListener(TagReadListener listener) {
		reader.addListener(listener);
		speedwayReader.addListener(listener);
		foxberryReader.addListener(listener);
	}

	public static void removeReadingListener(TagReadListener listener) {
		reader.removeListener(listener);
		speedwayReader.removeListener(listener);
		foxberryReader.removeListener(listener);
	}

	public static void startReading() throws ReaderException {
		switch (readerType) {
		case ReaderContext.USB_READER:
			workerThread = new Thread(reader);
			workerThread.start();
			break;
		case ReaderContext.SPEEDWAY_READER:
			workerThread = new Thread(speedwayReader);
			workerThread.start();
			break;
		case ReaderContext.FOXBERRY_READER:
			workerThread = new Thread(foxberryReader);
			workerThread.start();
			break;
		default:
			break;
		}
	}

	public static void stopReading() {
		if (readerType != null) {
			switch (readerType) {
			case ReaderContext.USB_READER:
				reader.stop();
				break;
			case ReaderContext.SPEEDWAY_READER:
				speedwayReader.stop();
				break;
			case ReaderContext.FOXBERRY_READER:
				foxberryReader.stop();
				break;
			default:
				break;
			}
		}
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
		if (readerType == ReaderContext.USB_READER) {
			GpioPin[] pins = new GpioPin[1];
			pins[0] = new GpioPin(2, true);
			reader.gpoSet(pins);
		}
	}

	public static void setRedOff() throws ReaderException {
		if (readerType == ReaderContext.USB_READER) {
			GpioPin[] pins = new GpioPin[1];
			pins[0] = new GpioPin(2, false);
			reader.gpoSet(pins);
		}
	}

	public static void setOrangeOn() throws ReaderException {
		if (readerType == ReaderContext.USB_READER) {
			GpioPin[] pins = new GpioPin[1];
			pins[0] = new GpioPin(1, true);
			reader.gpoSet(pins);
		}
	}

	public static void setOrangeOff() throws ReaderException {
		if (readerType == ReaderContext.USB_READER) {
			GpioPin[] pins = new GpioPin[1];
			pins[0] = new GpioPin(1, false);
			reader.gpoSet(pins);
		}
	}

	public static GpioPin[] getGpo() throws ReaderException {
		if (readerType == ReaderContext.USB_READER) {
			return reader.gpoGet();
		} else {
			return null;
		}
	}

	public static boolean getGpo(int gpi) throws ReaderException {
		if (readerType == ReaderContext.USB_READER) {
			GpioPin[] pins = getGpo();
			if (gpi < pins.length) {
				return pins[gpi].high;
			} else {
				throw new IndexOutOfBoundsException(
						"Pin id beyond available inputs.");
			}
		} else {
			return false;
		}
	}

	public static void executeTagOp(WriteTag tagop, TagData target)
			throws ReaderException {
		reader.executeTagOp(tagop, target);
	}

	public static void writeEpc(TagReadData data, String hexString)
			throws ReaderException, DecoderException {
		reader.write(data, hexString);
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
			logger.error("Error reading " + tid_length
					+ " byte TID. Falling back to 12 bit TID.");
			e2.printStackTrace();
			tid = String.valueOf(Hex.encodeHex(ReaderContext.readTagMemBytes(
					target, Gen2.Bank.TID.ordinal(), 0, 12)));
			logger.debug("Got 12 byte TID");
		}
		return tid;
	}

	public static void killTag(int killword, TagData td) throws ReaderException {
		Gen2.Kill killOp = new Gen2.Kill(killword);
		reader.executeTagOp(killOp, td);

	}

	public static void writeKill(short[] killword, TagData td)
			throws ReaderException {
		Gen2.WriteData writeOp = new Gen2.WriteData(Bank.RESERVED, 0, killword);
		logger.debug("Writing kill code on " + td);
		reader.executeTagOp(writeOp, td);
		logger.debug("Kill code written");
	}

	public static String readKill(TagData td) throws ReaderException {
		Gen2.ReadData readOp = new Gen2.ReadData(Bank.RESERVED, 0, (byte) 2);
		short[] data = (short[]) reader.executeTagOp(readOp, td);
		String access = String.format("%04X", data[0])
				+ String.format("%04X", data[1]);
		return access;
	}

	public static void writeAccess(short[] password, TagData td)
			throws ReaderException {
		Gen2.WriteData writeOp = new Gen2.WriteData(Bank.RESERVED, 2, password);
		logger.debug("Writing access code on " + td);
		reader.executeTagOp(writeOp, td);
		logger.debug("Access code written");
	}

	public static void lockTag(Integer password, LockAction lockAction,
			TagData td) throws ReaderException {
		logger.debug("Performing lock operation on " + td);
		Gen2.Lock lockOp = new Gen2.Lock(password, lockAction);
		reader.executeTagOp(lockOp, td);
	}

	public static String readAccess(TagData td) throws ReaderException {
		Gen2.ReadData readOp = new Gen2.ReadData(Bank.RESERVED, 2, (byte) 2);
		short[] data = (short[]) reader.executeTagOp(readOp, td);
		String access = String.format("%04X", data[0])
				+ String.format("%04X", data[1]);
		return access;
	}
}
