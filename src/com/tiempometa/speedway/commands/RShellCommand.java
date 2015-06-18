/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.tiempometa.speedway.commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

/**
 * @author Gerardo Tasistro gtasistro@tiempometa.com
 * Copyright 2015 Gerardo Tasistro
 * Licensed un the Mozilla Public License, v. 2.0
 * 
 */
public abstract class RShellCommand {
	
	private static final Logger logger = Logger.getLogger(RShellCommand.class);
	protected String hostname = null;
	protected String username = null;
	protected String password = null;
	protected boolean successful = false;
	protected Map<String, String> responseMap = null;
	
	public boolean isSuccessful() {
		return successful;
	}
	
	public void parseResponse(List<String> response) {
		responseMap = new HashMap<String, String>();
		for (String string : response) {
			String[] responseRow = string.split("=");
			if (responseRow.length == 2) {
				responseMap.put(responseRow[0], responseRow[1]);
			}
		}
	}
	
	public void parseStatus(String statusResponse) {
		String[] status = statusResponse.split("=");
		successful = false;
		if (status.length == 2) {
			if (status[0].equals("Status")&&status[1].equals("'0,Success'")) {
				successful = true;
			} 
		}
	}
	
	public RShellCommand(String hostname, String username, String password) {
		super();
		this.hostname = hostname;
		this.username = username;
		this.password = password;
	}

	public abstract void execute() throws IOException;
	
	protected ArrayList<String> executeCommand(String command) throws IOException {

		ArrayList<String> response = new ArrayList<>();
		logger.info("SSH connection test");
		Connection conn = new Connection(hostname);
		conn.connect();
		if (conn.authenticateWithPassword(username, password)) {
			logger.info("SSH connection authenticated");
			Session session = conn.openSession();
			logger.info("SSH connection issue command");
			session.execCommand(command);
			logger.info("SSH connection command result");
			InputStream stdOut = new StreamGobbler(session.getStdout());
			BufferedReader br= new BufferedReader(new InputStreamReader(stdOut));
			while (true) {
				String line = br.readLine();
				if (line == null) {
					break;
				}
				response.add(line);
			}
			br.close();
			stdOut.close();
			session.close();
		}
		conn.close();
		return response;
	}

}
