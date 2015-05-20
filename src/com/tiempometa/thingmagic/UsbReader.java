/**
 * 
 */
package com.tiempometa.thingmagic;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.binary.Hex;

import com.thingmagic.Gen2;
import com.thingmagic.Gen2.WriteTag;
import com.thingmagic.Reader;
import com.thingmagic.Reader.GpioPin;
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
	private Reader reader = null;
	private List<TagReadListener> listeners = new ArrayList<TagReadListener>();
	private boolean connected = false;

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

	public boolean isConnected() {
		return connected;
	}

	public void getSupportedRegions() throws ReaderException {
		Reader.Region[] supportedRegions = (Reader.Region[]) reader
				.paramGet(TMConstants.TMR_PARAM_REGION_SUPPORTEDREGIONS);
		for (int i = 0; i < supportedRegions.length; i++) {
			Reader.Region region = supportedRegions[i];
			System.out.println(region.name());
		}
	}

	public void connectToComm(String commPort) throws ReaderException {
		connect("tmr:///" + commPort);
	}

	public void connect(String uri) throws ReaderException {
		System.out.println("Creating reader with " + uri);
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
		System.out.println(inputList.getClass().getCanonicalName());
		for (int i = 0; i < inputList.length; i++) {
			int gpioPin = inputList[i];
			System.out.println("GPIO input " + gpioPin);
		}
		int[] outputList = (int[]) reader.paramGet("/reader/gpio/outputList");
		Reader.GpioPin[] pins = new Reader.GpioPin[2];
		for (int i = 0; i < outputList.length; i++) {
			int gpioPin = outputList[i];
			System.out.println("GPIO input " + gpioPin);
			pins[0] = new GpioPin(1,true);
			pins[1] = new GpioPin(2,true);
			reader.gpoSet(pins);
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			pins[0] = new GpioPin(1,false);
			pins[1] = new GpioPin(2,false);
			reader.gpoSet(pins);
		}
		System.out.println("Created reader!");
		// getSupportedRegions();
		reader.paramSet("/reader/radio/writePower", 1000);
		reader.paramSet("/reader/radio/readPower", 1000);

	}

	public void read() throws ReaderException {
		for (int j = 0; j < 1000; j++) {
			TagReadData[] tagReads;
			List<TagReading> readings = new ArrayList<TagReading>();
			Reader.GpioPin[] pins = new Reader.GpioPin[1];
			if (isConnected()) {
				pins[0] = new GpioPin(1,true);
				reader.gpoSet(pins);
				tagReads = reader.read(50);
				pins[0] = new GpioPin(1,false);
				reader.gpoSet(pins);
				// Print tag reads
				for (TagReadData tr : tagReads) {
					readings.add(new TagReading(tr));
					System.out.println(tr.toString());
					System.out.println(new String(tr.getTIDMemData()));
					System.out.println(new String(tr.getUserMemData()));

					// TagData target = new TagData(tr.getEPCMemData());
					// try {
					// System.out.println("User data");
					// System.out.println(Hex.encodeHex(reader.readTagMemBytes(
					// target, Gen2.Bank.USER.ordinal(), 0, 32)));
					// System.out.println("TID");
					// System.out.println(Hex.encodeHex(reader.readTagMemBytes(
					// target, Gen2.Bank.TID.ordinal(), 0, 12)));
					// } catch (ReaderCodeException e) {
					//
					// }
					// write(tr);
				}
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

	public void disconnect() throws ReaderException {
		Reader.GpioPin[] pins = new Reader.GpioPin[2];
		pins[0] = new GpioPin(1,false);
		pins[1] = new GpioPin(2,false);
		reader.gpoSet(pins);
		connected = false;
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

	public byte[] readTagMemBytes(TagData target, int bank, int start, int size)
			throws ReaderException {
		return reader.readTagMemBytes(target, bank, start, size);
	}

	public void gpoSet(GpioPin[] pins) throws ReaderException {
		reader.gpoSet(pins);
	}

	public void executeTagOp(WriteTag tagop, TagData target) throws ReaderException {
		reader.executeTagOp(tagop, target);
		
	}

}
