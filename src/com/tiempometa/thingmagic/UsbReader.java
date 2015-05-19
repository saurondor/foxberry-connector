/**
 * 
 */
package com.tiempometa.thingmagic;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.binary.Hex;

import com.thingmagic.Gen2;
import com.thingmagic.Reader;
import com.thingmagic.ReaderCodeException;
import com.thingmagic.ReaderException;
import com.thingmagic.TMConstants;
import com.thingmagic.TagData;
import com.thingmagic.TagReadData;
import com.tiempometa.muestradatos.TagReadListener;
import com.tiempometa.muestradatos.TagReading;

/**
 * @author Gerardo Tasistro
 * 
 */
public class UsbReader implements Runnable {
	private Reader reader;
	private List<TagReadListener> listeners = new ArrayList<TagReadListener>();
	
	public void addListener(TagReadListener listener) {
		listeners.add(listener);
	}

	public void removeListener(TagReadListener listener) {
		listeners.remove(listener);
	}
	
	public void notifyListeners(List<TagReading> readings) {
		for (TagReadListener listener : listeners) {
			listener.handleReadings(readings);
		}
	}

	public void getParamters() {
		String[] parameters = reader.paramList();
		for (int i = 0; i < parameters.length; i++) {
			System.out.println("Parameter " + parameters[i]);
			try {
				System.out.println("Value " + reader.paramGet(parameters[i]));
				System.out.println("Value "
						+ reader.paramGet(parameters[i]).getClass()
								.getCanonicalName());
			} catch (ReaderCodeException e) {
				// TODO: handle exception
			} catch (ReaderException e) {
				// TODO: handle exception
			}
		}
	}

	public void getSupportedRegions() throws ReaderException {
		Reader.Region[] supportedRegions = (Reader.Region[]) reader.paramGet(TMConstants.TMR_PARAM_REGION_SUPPORTEDREGIONS);
		for (int i = 0; i < supportedRegions.length; i++) {
			Reader.Region region = supportedRegions[i];
			System.out.println(region.name());
		}
	}
	
	public void connectToComm(String commPort) throws ReaderException {
		connect("tmr:///"+commPort);
	}

	public void connect(String uri) throws ReaderException {
		System.out.println("Creating reader with " + uri);
		reader = Reader.create(uri);
		reader.connect();
		System.out.println("Created reader!");
//		getSupportedRegions();
		reader.paramSet("/reader/radio/writePower", 1000);
		reader.paramSet("/reader/radio/readPower", 1000);
	}

	public void read() throws ReaderException {
		for (int j = 0; j < 100; j++) {
			System.out.println("Reading tags");
			TagReadData[] tagReads;
			tagReads = reader.read(500);
			// Print tag reads
			List<TagReading> readings = new ArrayList<TagReading>();
			for (TagReadData tr : tagReads) {
				readings.add(new TagReading(tr));
				System.out.println(tr.toString());
				System.out.println(new String(tr.getTIDMemData()));
				System.out.println(new String(tr.getUserMemData()));
				
//				TagData target = new TagData(tr.getEPCMemData());
//				try {
//					System.out.println("User data");
//					System.out.println(Hex.encodeHex(reader.readTagMemBytes(
//							target, Gen2.Bank.USER.ordinal(), 0, 32)));
//					System.out.println("TID");
//					System.out.println(Hex.encodeHex(reader.readTagMemBytes(
//							target, Gen2.Bank.TID.ordinal(), 0, 12)));
//				} catch (ReaderCodeException e) {
//
//				}
				// write(tr);
			}
			notifyListeners(readings);
		}
	}

	public void write(TagReadData data) throws ReaderException {
		Gen2.TagData epc = new Gen2.TagData(new byte[] { (byte) 0x01,
				(byte) 0x23, (byte) 0x45, (byte) 0x67, (byte) 0x89,
				(byte) 0xAB, (byte) 0xCD, (byte) 0xEF, (byte) 0x01,
				(byte) 0x23, (byte) 0x45, (byte) 0x67, });
		Gen2.WriteTag tagop = new Gen2.WriteTag(epc);
		System.out.println("Requesting write EPC...");
		TagData target = new TagData(data.getEPCMemData());
		reader.executeTagOp(tagop, target);
		System.out.println("Wrote tag!");
	}

	public void disconnect() {
		reader.destroy();
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
}
