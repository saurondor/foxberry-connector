/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.tiempometa.thingmagic;

import com.thingmagic.Gen2;
import com.thingmagic.Gen2.Bank;
import com.thingmagic.Reader;
import com.thingmagic.ReaderException;
import com.thingmagic.TMConstants;
import com.thingmagic.TagFilter;
import com.thingmagic.TransportListener;

/**
 * @author Gerardo Tasistro gtasistro@tiempometa.com
 * Copyright 2015 Gerardo Tasistro
 * Licensed un the Mozilla Public License, v. 2.0
 * 
 */
public class WriteTest {
	static SerialPrinter serialPrinter;
	static StringPrinter stringPrinter;
	static TransportListener currentListener;

	static void usage() {
		System.out.printf("Usage: demo reader-uri <command> [args]\n"
				+ "  (URI: 'tmr:///COM1' or 'tmr://astra-2100d3/' "
				+ "or 'tmr:///dev/ttyS0')\n\n" + "Available commands:\n");
		System.exit(1);
	}

	public static void setTrace(Reader r, String args[]) {
		if (args[0].toLowerCase().equals("on")) {
			r.addTransportListener(Reader.simpleTransportListener);
			currentListener = Reader.simpleTransportListener;
		} else if (currentListener != null) {
			r.removeTransportListener(Reader.simpleTransportListener);
		}
	}

	static class SerialPrinter implements TransportListener {
		public void message(boolean tx, byte[] data, int timeout) {
			System.out.print(tx ? "Sending: " : "Received:");
			for (int i = 0; i < data.length; i++) {
				if (i > 0 && (i & 15) == 0)
					System.out.printf("\n         ");
				System.out.printf(" %02x", data[i]);
			}
			System.out.printf("\n");
		}
	}

	static class StringPrinter implements TransportListener {
		public void message(boolean tx, byte[] data, int timeout) {
			System.out.println((tx ? "Sending:\n" : "Receiving:\n")
					+ new String(data));
		}
	}

	public static void main(String argv[]) {
		// Program setup
		TagFilter target = null;
		Reader r = null;
		int nextarg = 0;
		boolean trace = false;

		if (argv.length < 1)
			usage();

		if (argv[nextarg].equals("-v")) {
			trace = true;
			nextarg++;
		}

		// Create Reader object, connecting to physical device
		try {

			System.out.println("Creating reader...");
			r = Reader.create(argv[nextarg]);
			if (trace) {
				setTrace(r, new String[] { "on" });
			}
			r.connect();

			System.out.println("Connected!");
			if (Reader.Region.UNSPEC == (Reader.Region) r
					.paramGet("/reader/region/id")) {
				Reader.Region[] supportedRegions = (Reader.Region[]) r
						.paramGet(TMConstants.TMR_PARAM_REGION_SUPPORTEDREGIONS);
				if (supportedRegions.length < 1) {
					throw new Exception("Reader doesn't support any regions");
				} else {
					r.paramSet("/reader/region/id", supportedRegions[0]);
				}
			}

			Gen2.TagData epc = new Gen2.TagData(new byte[] { (byte) 0xE1,
					(byte) 0x23, (byte) 0x45, (byte) 0x67, (byte) 0x89,
					(byte) 0xAB, (byte) 0xCD, (byte) 0xEF, (byte) 0x01,
					(byte) 0x23, (byte) 0x45, (byte) 0x67, });
			Gen2.WriteTag tagop = new Gen2.WriteTag(epc);
			
			short[] userData = new short[] {(short) 0xE4f1, (short) 0xE1f1, (short) 0xE2f1, (short) 0xE3f1, (short) 0xE4f1 
					
			};
			Gen2.WriteData userOp = new Gen2.WriteData(Bank.USER, 0, userData);

			System.out.println("Requesting write EPC...");
			r.executeTagOp(tagop, target);
			System.out.println("Wrote tag!");
			System.out.println("Requesting write user data...");
			r.executeTagOp(userOp, target);
			System.out.println("Wrote tag!");


			// Shut down reader
			r.destroy();
		} catch (ReaderException re) {
			System.out.println("ReaderException: " + re.getMessage());
		} catch (Exception re) {
			System.out.println("Exception: " + re.getMessage());
		}
	}
}
