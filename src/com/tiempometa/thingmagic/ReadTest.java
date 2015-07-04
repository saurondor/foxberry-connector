/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.tiempometa.thingmagic;

import com.thingmagic.ReadListener;
import com.thingmagic.Reader;
import com.thingmagic.ReaderException;
import com.thingmagic.TMConstants;
import com.thingmagic.TagReadData;

/**
 * @author Gerardo Tasistro gtasistro@tiempometa.com
 * Copyright 2015 Gerardo Tasistro
 * Licensed under the Mozilla Public License, v. 2.0
 * 
 */
public class ReadTest {

	static boolean  runMe = true;

	static void usage() {
		System.out.printf("Usage: demo reader-uri <command> [args]\n"
				+ "  (URI: 'tmr:///COM1' or 'tmr://astra-2100d3/' "
				+ "or 'tmr:///dev/ttyS0')\n\n" + "Available commands:\n");
		System.exit(1);
	}

	public static void setTrace(Reader r, String args[]) {
		if (args[0].toLowerCase().equals("on")) {
			r.addTransportListener(r.simpleTransportListener);
		}
	}

	static class TagReadListener implements ReadListener {
		public void tagRead(Reader r, TagReadData t) {
			System.out.println("Tag Read " + t);
		}
	}

	public static void main(String argv[]) {
		// Program setup
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

			TagReadData[] tagReads;
			String uri = argv[nextarg];
			System.out.println("Creating reader with " + uri);
			r = Reader.create(uri);
			System.out.println("Setting trace...");

//			if (trace) {
//				setTrace(r, new String[] { "on" });
//			}
			System.out.println("Connecting...");
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
			r.addReadListener(new TagReadListener());
			while (runMe) {
				// Read tags
				tagReads = r.read(500);
				// Print tag reads
				for (TagReadData tr : tagReads) {
					System.out.println(tr.toString());
					byte[] tid = tr.getTIDMemData();
					for (int i = 0; i < tid.length; i++) {
						System.out.println(tid[i]);
					}
					
				}

			}
			// Shut down reader
			r.destroy();
		} catch (ReaderException re) {
			System.out.println("Reader Exception : " + re.getMessage());
		} catch (Exception re) {
			System.out.println("Exception : " + re.getLocalizedMessage());
			System.out.println("Exception : " + re.getMessage());
		}
	}
}
