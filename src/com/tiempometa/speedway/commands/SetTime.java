/**
 * 
 */
package com.tiempometa.speedway.commands;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
		List<String> response = executeCommand("config system time 2015.05.19-10:07:00");
		if (response.size() == 1) {
			System.out.println(response.get(0));
			parseStatus(response.get(0));
		}
	}


}
