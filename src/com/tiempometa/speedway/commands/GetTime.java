/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.tiempometa.speedway.commands;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * @author Gerardo Tasistro gtasistro@tiempometa.com
 * Copyright 2015 Gerardo Tasistro
 * Licensed under the Mozilla Public License, v. 2.0
 * 
 */
public class GetTime extends RShellCommand {

	private String sysDesc = null;
	private String sysContact = null;
	private String sysName = null;
	private String sysLocation = null;
	private String sysTimeString = null;
	private Date sysTime = null;

	public GetTime(String hostname, String username, String password) {
		super(hostname, username, password);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute() throws IOException {
		List<String> response = executeCommand("show system summary");
		if (response.size() > 0) {
			System.out.println(response.get(0));
			parseStatus(response.get(0));
		}
		if (isSuccessful()) {
			parseResponse(response);
			sysDesc = responseMap.get("SysDesc");
			sysContact = responseMap.get("SysContact");
			sysName = responseMap.get("SysName");
			sysLocation = responseMap.get("SysLocation");
			sysTimeString = responseMap.get("SysTime");
			if (sysTimeString == null) {
				sysTime = null;
			} else {
				// Tue May 19 10:07:31 UTC 2015
				SimpleDateFormat format = new SimpleDateFormat("EEE MMM d HH:mm:ss z yyyy", Locale.ENGLISH);
				try {
					sysTime = format.parse(sysTimeString.replaceAll("'", ""));
				} catch (ParseException e) {
					e.printStackTrace();
					sysTime = null;
				}
				
			}
		}
	}

	/**
	 * @return the sysDesc
	 */
	public String getSysDesc() {
		return sysDesc;
	}

	/**
	 * @return the sysContact
	 */
	public String getSysContact() {
		return sysContact;
	}

	/**
	 * @return the sysName
	 */
	public String getSysName() {
		return sysName;
	}

	/**
	 * @return the sysLocation
	 */
	public String getSysLocation() {
		return sysLocation;
	}

	/**
	 * @return the sysTimeString
	 */
	public String getSysTimeString() {
		return sysTimeString;
	}

	/**
	 * @return the sysTime
	 */
	public Date getSysTime() {
		return sysTime;
	}

}
