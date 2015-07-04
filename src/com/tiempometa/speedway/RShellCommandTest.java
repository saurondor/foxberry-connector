/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.tiempometa.speedway;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.tiempometa.speedway.commands.GetTime;
import com.tiempometa.speedway.commands.RShellCommand;
import com.tiempometa.speedway.commands.SetTime;

/**
 * @author Gerardo Tasistro gtasistro@tiempometa.com
 * Copyright 2015 Gerardo Tasistro
 * Licensed under the Mozilla Public License, v. 2.0
 * 
 */
public class RShellCommandTest {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SetTime setTime = new SetTime("speedwayr-11-49-c4.attlocal.net",
				"root", "impinj");
		GetTime getTime = new GetTime("speedwayr-11-49-c4.attlocal.net",
				"root", "impinj");
		try {
			getTime.execute();
			if (getTime.isSuccessful()) {
				System.out.println("System time " + getTime.getSysTimeString());
				System.out.println("System time " + getTime.getSysTime());
			} else {
				System.out.println("Could not get time");
			}
			setTime.execute();
			if (setTime.isSuccessful()) {
				System.out.println("Time set");
			} else {
				System.out.println("Time NOT set");
			}
			getTime = new GetTime("speedwayr-11-49-c4.attlocal.net", "root",
					"impinj");
			getTime.execute();
			if (getTime.isSuccessful()) {
				System.out.println("System time " + getTime.getSysTimeString());
				System.out.println("System time " + getTime.getSysTime());
			} else {
				System.out.println("Could not get time");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
