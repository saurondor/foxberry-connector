/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.tiempometa.speedway.commands;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

/**
 * @author Gerardo Tasistro gtasistro@tiempometa.com
 * Copyright 2015 Gerardo Tasistro
 * Licensed un the Mozilla Public License, v. 2.0
 * 
 */
public class SetTime extends RShellCommand {

	public SetTime(String hostname, String username, String password) {
		super(hostname, username, password);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute() throws IOException {
		Date time = new Date();
		GregorianCalendar calendar = new GregorianCalendar(TimeZone.getTimeZone("GMT-06:00"));
		time = calendar.getTime();
		System.out.println(calendar.toString());
		SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd-hh:mm:ss");
		List<String> response = executeCommand("config system time "+format.format(time));
		if (response.size() == 1) {
			System.out.println(response.get(0));
			parseStatus(response.get(0));
		}
	}


}
