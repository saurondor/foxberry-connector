/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.tiempometa.thingmagic;

import org.apache.commons.codec.binary.Hex;

import com.thingmagic.Gen2;
import com.thingmagic.Gen2.Bank;
import com.thingmagic.Gen2.LockAction;
import com.thingmagic.ReadListener;
import com.thingmagic.Reader;
import com.thingmagic.ReaderException;
import com.thingmagic.TMConstants;
import com.thingmagic.TagData;
import com.thingmagic.TagFilter;
import com.thingmagic.TagReadData;

/**
 * @author Gerardo Tasistro gtasistro@tiempometa.com Copyright 2015 Gerardo
 *         Tasistro Licensed under the Mozilla Public License, v. 2.0
 * 
 */
public class ReadTest {

	static boolean runMe = true;

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

	private static void p(String output) {
		System.out.println(output);
	}

	private static void writeEpc(TagReadData tr, Reader r)
			throws ReaderException {
		TagFilter tagFilter = null;
		tagFilter = new TagData(tr.getTag().epcString());

		Gen2.TagData epc = new Gen2.TagData(new byte[] { (byte) 0xE1,
				(byte) 0x23, (byte) 0x45, (byte) 0x67, (byte) 0x89,
				(byte) 0xAB, (byte) 0xCD, (byte) 0xEF, (byte) 0x01,
				(byte) 0x23, (byte) 0x45, (byte) 0xB0 });
		Gen2.WriteTag writeTagOp = new Gen2.WriteTag(epc);
		System.out.println("Writing epc...");
		r.executeTagOp(writeTagOp, tagFilter);
		System.out.println("Wrote epc...");
	}


	private static void killTag(int killword, TagReadData tr, Reader r) throws ReaderException {
		TagFilter tagFilter = null;
		tagFilter = new TagData(tr.getTag().epcString());
		Gen2.Kill killOp = new Gen2.Kill(killword);
		r.executeTagOp(killOp, tagFilter);
		
	}

	private static void writeKill(short[] killword, TagReadData tr, Reader r)
			throws ReaderException {
		TagFilter tagFilter = null;
		tagFilter = new TagData(tr.getTag().epcString());
		Gen2.WriteData writeOp = new Gen2.WriteData(Bank.RESERVED, 0, killword);
		r.executeTagOp(writeOp, tagFilter);

	}

	private static String readKill(TagReadData tr, Reader r)
			throws ReaderException {
		Gen2.ReadData readOp = new Gen2.ReadData(Bank.RESERVED, 0, (byte) 2);
		TagFilter tagFilter = null;
		tagFilter = new TagData(tr.getTag().epcString());
		short[] data = (short[]) r.executeTagOp(readOp, tagFilter);
		String access = String.format("%04X", data[0])
				+ String.format("%04X", data[1]);
		p("Read access " + access);
		return access;

	}

	private static void writeAccess(short[] password, TagReadData tr, Reader r)
			throws ReaderException {
		TagFilter tagFilter = null;
		tagFilter = new TagData(tr.getTag().epcString());
		Gen2.WriteData writeOp = new Gen2.WriteData(Bank.RESERVED, 2, password);
		r.executeTagOp(writeOp, tagFilter);

	}

	private static void lockTag(Integer password, LockAction lockAction,
			TagReadData tr, Reader r) throws ReaderException {
		TagFilter tagFilter = null;
		tagFilter = new TagData(tr.getTag().epcString());
		Gen2.Lock lockOp = new Gen2.Lock(password, lockAction);
		r.executeTagOp(lockOp, tagFilter);
	}

	private static String readAccess(TagReadData tr, Reader r)
			throws ReaderException {
		Gen2.ReadData readOp = new Gen2.ReadData(Bank.RESERVED, 2, (byte) 2);
		TagFilter tagFilter = null;
		tagFilter = new TagData(tr.getTag().epcString());
		short[] data = (short[]) r.executeTagOp(readOp, tagFilter);
		String access = String.format("%04X", data[0])
				+ String.format("%04X", data[1]);
		p("Read access " + access);
		return access;
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

			// if (trace) {
			// setTrace(r, new String[] { "on" });
			// }
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
			int count = 0;
			while (runMe) {
				// Read tags
				tagReads = r.read(500);
				// Print tag reads
				Long password = null;
				Long killword = null;
				for (TagReadData tr : tagReads) {
					p(tr.toString());
					try {
						String accessCode = readAccess(tr, r);
						password = Long.parseLong(accessCode, 16);
						p("Password " + password);
						p("--" + Long.toHexString(password));
						accessCode = readKill(tr, r);
						killword = Long.parseLong(accessCode, 16);
						p("Killword " + killword);
						p("--" + Long.toHexString(killword));
						// if (password == 0) {
						p("Setting password");
						password = 0xAFCC01ACl;
						short[] access = new short[2];
						access[1] = (short) 0x01AC;
						access[0] = (short) 0xAFCC;
						p("Setting password to ");
						for (short word : access) {
							System.out.printf("%04X", word);
						}
						p("\n");
						writeAccess(access, tr, r);
						// }
						// if (killword == 0) {
						p("Setting killword");
						killword = 0xADD001ACl;
						access = new short[2];
						access[1] = (short) 0x01AC;
						access[0] = (short) 0xADD0;
						p("Setting killword to ");
						for (short word : access) {
							System.out.printf("%04X", word);
						}
						p("\n");
						writeKill(access, tr, r);

						// }
						if (count < 10) {
							p("Waiting to lock " + (10 - count));
						} else if (count == 10) {
							p("Locking..." + password.intValue());
							p("--" + Long.toHexString(password));
							lockTag(password.intValue(), new LockAction(
									LockAction.KILL_LOCK), tr, r);
							p("Locked kill");
							lockTag(password.intValue(), new LockAction(
									LockAction.ACCESS_LOCK), tr, r);
							p("Locked access");
						} else {
							p("Done with this tag");
						}
						count++;
					} catch (ReaderException e) {
						System.out.println("Exception : " + e.getMessage());
						killword = 0xADD001ACl;
						password = 0xAFCC01ACl;
						if (count < 20) {
							p("Waiting to unlock " + (20 - count));
						} else if (count == 20) {
							p("Unlocking...");
							p("Password :" + password.intValue());
							p("--" + Integer.toHexString(password.intValue()));
							lockTag(password.intValue(), new LockAction(
									LockAction.ACCESS_UNLOCK), tr, r);
							p("Unlocked access");
							lockTag(password.intValue(), new LockAction(
									LockAction.KILL_UNLOCK), tr, r);
							p("Unlocked kill");
							p("***Unlocked");
							p("Locking...");
							p("Password :" + password.intValue());
							lockTag(password.intValue(), new LockAction(
									LockAction.KILL_LOCK), tr, r);
							p("Locked kill");
							lockTag(password.intValue(), new LockAction(
									LockAction.ACCESS_LOCK), tr, r);
							p("Locked access");
						} else if (count == 30) {
							p("Killing");
							p("Unlocking access");
							lockTag(password.intValue(), new LockAction(LockAction.KILL_UNLOCK), tr, r);
							p("Issuing kill command");
							killTag(killword.intValue(), tr, r);
						} else {
							p("Waiting to kill");
						}
						count++;
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
		} finally {
			r.destroy();
		}
	}
}
