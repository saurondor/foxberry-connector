/**
 * 
 */
package com.tiempometa.speedway.commands;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

/**
 * @author Gerardo Tasistro
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
