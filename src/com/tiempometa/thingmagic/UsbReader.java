/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.tiempometa.thingmagic;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.log4j.Logger;

import com.thingmagic.Gen2;
import com.thingmagic.Gen2.Bank;
import com.thingmagic.Gen2.WriteTag;
import com.thingmagic.Reader;
import com.thingmagic.Reader.GpioPin;
import com.thingmagic.ReaderCodeException;
import com.thingmagic.ReaderException;
import com.thingmagic.SimpleReadPlan;
import com.thingmagic.TMConstants;
import com.thingmagic.TagData;
import com.thingmagic.TagFilter;
import com.thingmagic.TagOp;
import com.thingmagic.TagProtocol;
import com.thingmagic.TagReadData;
import com.tiempometa.muestradatos.ReaderStatusListener;
import com.tiempometa.muestradatos.TagReadListener;
import com.tiempometa.muestradatos.TagReading;

/**
 * @author Gerardo Tasistro gtasistro@tiempometa.com
 * Copyright 2015 Gerardo Tasistro
 * Licensed under the Mozilla Public License, v. 2.0
 * 
 */
public class UsbReader implements Runnable {

	private static final Logger logger = Logger.getLogger(UsbReader.class);
	private Reader reader = null;
	private List<TagReadListener> listeners = new ArrayList<TagReadListener>();
	private List<ReaderStatusListener> statusListeners = new ArrayList<ReaderStatusListener>();
	private Map<String, String> regionMap = new HashMap<String, String>();
	private boolean connected = false;
	private boolean doReadings = false;
	private Integer minPower = null;
	private Integer maxPower = null;
	private int[] portList = null;

	public UsbReader() {
		super();
		regionMap.put("NA", "North America/FCC");
		regionMap.put("EU", "European Union");
		regionMap.put("EU2", "European Union - Rev 2");
		regionMap.put("EU3", "European Union - Rev 3");
		regionMap.put("KR", "Korea");
		regionMap.put("KR2", "Korea Rev");
		regionMap.put("PRC", "China");
		regionMap.put("PRC2", "China 840MHz");
		regionMap.put("IN", "India");
		regionMap.put("JP", "Japan");
		regionMap.put("AU", "Australia");
		regionMap.put("NZ", "New Zealand");
		regionMap.put("OPEN", "No region restrictions enforced");
		regionMap.put("NONE", "No region Specified");
		regionMap.put("MANUFACTURING", "Manufacturing Unrestricted");
	}

	public boolean isReading() {
		return doReadings;
	}
	
	public void stop() {
		synchronized (this) {
			doReadings = false;
		}
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

	private void notifyRegionUpdated(String regionName) {
		for (ReaderStatusListener listener : statusListeners) {
			String region = regionMap.get(regionName);
			if (region == null) {
				region = "No especificada";
			}
			listener.updatedRegion(region);
		}
	}

	private void notifyWritePower(Integer powerLevel) {
		for (ReaderStatusListener listener : statusListeners) {
			listener.updatedWritePower(powerLevel);
		}
	}

	private void notifyReadPower(Integer powerLevel) {
		for (ReaderStatusListener listener : statusListeners) {
			listener.updatedReadPower(powerLevel);
		}
	}

	private void notifyConnected() {
		for (ReaderStatusListener listener : statusListeners) {
			listener.usbConnected();
		}
	}

	private void notifyDisconnected() {
		for (ReaderStatusListener listener : statusListeners) {
			listener.usbDisconnected();
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

	public void getParamters() {
		String[] parameters = reader.paramList();
		for (int i = 0; i < parameters.length; i++) {
			logger.debug("Parameter " + parameters[i]);
			try {
				logger.debug("Value " + reader.paramGet(parameters[i]));
				logger.debug("Value "
						+ reader.paramGet(parameters[i]).getClass()
								.getCanonicalName());
			} catch (ReaderCodeException e) {
				// TODO: handle exception
			} catch (ReaderException e) {
				// TODO: handle exception
			}
		}
	}

	public boolean isConnected() {
		return connected;
	}

	public void getSupportedRegions() throws ReaderException {
		Reader.Region[] supportedRegions = (Reader.Region[]) reader
				.paramGet(TMConstants.TMR_PARAM_REGION_SUPPORTEDREGIONS);
		for (int i = 0; i < supportedRegions.length; i++) {
			Reader.Region region = supportedRegions[i];
			logger.debug(region.name());
		}
	}

	public boolean setRegion(String regionName) throws ReaderException {
		Reader.Region[] supportedRegions = (Reader.Region[]) reader
				.paramGet(TMConstants.TMR_PARAM_REGION_SUPPORTEDREGIONS);
		boolean regionSet = false;
		for (int i = 0; i < supportedRegions.length; i++) {
			Reader.Region region = supportedRegions[i];
			if (region.name().equals(regionName)) {
				reader.paramSet("/reader/region/id", region);
				regionSet = true;
				notifyRegionUpdated(regionName);
			}
		}
		return regionSet;
	}

	public void connectToComm(String commPort) throws ReaderException {
		connect("tmr:///" + commPort);
	}

	public void connect(String uri) throws ReaderException {
		logger.info("Creating reader with " + uri);
		reader = Reader.create(uri);
		reader.connect();
		connected = true;
		if (Reader.Region.UNSPEC == (Reader.Region) reader
				.paramGet("/reader/region/id")) {
			Reader.Region[] supportedRegions = (Reader.Region[]) reader
					.paramGet(TMConstants.TMR_PARAM_REGION_SUPPORTEDREGIONS);
			if (supportedRegions.length < 1) {
				// throw new Exception("Reader doesn't support any regions");
			} else {
				reader.paramSet("/reader/region/id", supportedRegions[0]);
			}
		}
		int[] inputList = (int[]) reader.paramGet("/reader/gpio/inputList");
		for (int i = 0; i < inputList.length; i++) {
			int gpioPin = inputList[i];
			logger.info("Supported input pin " + gpioPin);
		}
		int[] outputList = (int[]) reader.paramGet("/reader/gpio/outputList");
		Reader.GpioPin[] pins = new Reader.GpioPin[2];
		for (int i = 0; i < outputList.length; i++) {
			pins[0] = new GpioPin(1, true);
			pins[1] = new GpioPin(2, true);
			reader.gpoSet(pins);
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			pins[0] = new GpioPin(1, false);
			pins[1] = new GpioPin(2, false);
			reader.gpoSet(pins);
		}
		notifyConnected();
		logger.info("Created reader!");
		logger.info("Max power");
		maxPower = (Integer) reader.paramGet("/reader/radio/powerMax");
		logger.info(maxPower);
		logger.info("Min power");
		minPower = (Integer) reader.paramGet("/reader/radio/powerMin");
		logger.info(reader.paramGet("/reader/radio/powerMin"));
		portList = (int[]) reader.paramGet("/reader/antenna/portList");
		logger.info("Antenna port list:");
		for (int i = 0; i < portList.length; i++) {
			int j = portList[i];
			logger.info(j);
		}
		reader.paramSet("/reader/radio/writePower", 1000);
		notifyWritePower(1000);
		reader.paramSet("/reader/radio/readPower", 1000);
		notifyReadPower(1000);
	}

	public void read() throws ReaderException {
		doReadings = true;
		boolean read = true;
		notifyStartedReading();
		do {
			TagReadData[] tagReads;
			List<TagReading> readings = new ArrayList<TagReading>();
			Reader.GpioPin[] pins = new Reader.GpioPin[1];
			if (isConnected()) {
				pins[0] = new GpioPin(1, true);
				reader.gpoSet(pins);
				tagReads = reader.read(50);
				pins[0] = new GpioPin(1, false);
				reader.gpoSet(pins);
				// Print tag reads
				for (TagReadData tr : tagReads) {
					readings.add(new TagReading(tr));
				}
			}
			notifyListeners(readings);
			synchronized (this) {
				read = doReadings;
			}
		} while (read);
		notifyStoppedReading();
	}

	public void write(TagReadData data,String hexString) throws ReaderException, DecoderException {
		Gen2.TagData epc = new Gen2.TagData(Hex.decodeHex(hexString.toCharArray()));
		Gen2.WriteTag tagop = new Gen2.WriteTag(epc);
		logger.debug("Requesting write EPC...");
		TagData target = new TagData(data.getEPCMemData());
		reader.executeTagOp(tagop, target);
		logger.debug("Wrote tag!");
	}

	public void disconnect() throws ReaderException {
		if (reader == null) {
			// TODO handle disconnect on null reader
		} else {
			Reader.GpioPin[] pins = new Reader.GpioPin[2];
			pins[0] = new GpioPin(1, false);
			pins[1] = new GpioPin(2, false);
			reader.gpoSet(pins);
			connected = false;
			notifyDisconnected();
			reader.destroy();
		}
	}

	public static void main(String argv[]) {
		UsbReader reader = new UsbReader();
		try {
			reader.connect("tmr:///COM4");
			reader.read();
			reader.disconnect();
		} catch (ReaderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			read();
		} catch (ReaderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public byte[] readTagMemBytes(TagData target, int bank, int start, int size)
			throws ReaderException {
		return reader.readTagMemBytes(target, bank, start, size);
	}

	public GpioPin[] gpoGet() throws ReaderException {
		return reader.gpiGet();
	}

	public void gpoSet(GpioPin[] pins) throws ReaderException {
		reader.gpoSet(pins);
	}

	public short[] executeTagOp(TagOp tagop, TagData target)
			throws ReaderException {
		short[] data = (short[]) reader.executeTagOp(tagop, target);
		return data;
	}
}
