/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.tiempometa.muestradatos;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import javax.swing.JOptionPane;

import net.jimmc.jshortcut.JShellLink;

import org.apache.log4j.Logger;

/**
 * @author Gerardo Tasistro
 * 
 *         Copyright 2015 Gerardo Tasistro Licensed un the Mozilla Public
 *         License, v. 2.0
 * 
 */
public class InstallUtils {

	public static final String AMD64 = "amd64";
	public static final String X86 = "x86";
	private static final Logger logger = Logger.getLogger(InstallUtils.class);

	/**
	 * Tests if the java jvm in the command string is 32 bit
	 * 
	 * @param command
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static boolean isJava32Bit(String command) throws IOException,
			InterruptedException {
		String run_command = "'" + command + "' -version";
		System.out.println(run_command);
		Process p;
		String[] args = { command, "-version" };
		p = Runtime.getRuntime().exec(args);
		p.waitFor();
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				p.getErrorStream()));
		String line = reader.readLine();
		int row = 0;
		while (line != null) {
			line = reader.readLine();
			if (line.startsWith("Java HotSpot(TM)")) {
				if (line.contains("64-Bit")) {
					return false;
				} else {
					return true;
				}
			}
		}
		return false;
	}

	public static void unpackDll(String osArch, String userDir)
			throws IOException {
		if (osArch.equals(X86)) {
			logger.info("Unpacking dll for 32 bit architecture");
			saveResouce("/com/tiempometa/installer/dll/jshortcut.dll", userDir
					+ "\\jshortcut.dll");
			return;
		}
		if (osArch.equals(AMD64)) {
			logger.info("Unpacking dll for AMD 64 bit architecture");
			saveResouce(
					"/com/tiempometa/installer/dll/jshortcut_amd64-0.4.dll",
					userDir + "\\jshortcut.dll");
			return;
		}
		logger.info("Unpacking dll for other 64 bit architecture");
		saveResouce("/com/tiempometa/installer/dll/jshortcut_ia64-0.4.dll",
				userDir + "\\jshortcut.dll");

	}

	private static void saveResouce(String resourceName, String outputFile)
			throws IOException {
		InputStream input = resourceName.getClass().getResourceAsStream(
				resourceName);
		OutputStream output;
		output = new FileOutputStream(outputFile);
		writeFile(input, output);
		output.flush();
		output.close();
	}

	private static void writeFile(InputStream input, OutputStream output)
			throws IOException {
		byte[] buffer = new byte[1024];
		int bytesRead = 0;
		while ((bytesRead = input.read(buffer)) != -1) {
			output.write(buffer, 0, bytesRead);
		}
	}

	public static void createShortcut(String programName,
			String argumentString, String workingDirectory, String linkName) {
		String desktopPath = JShellLink.getDirectory("desktop");
		File linkFile = new File(desktopPath + "\\" + linkName + ".lnk");
		System.out.println("Verify " + linkFile);
		if (linkFile.exists()) {
			linkFile.delete();
		}
		JShellLink link = new JShellLink();
		link.setFolder(desktopPath);
		link.setName(linkName);
		link.setWorkingDirectory(workingDirectory);
		link.setPath(programName);
		link.setArguments(argumentString);
		link.save();
	}

	/**
	 * Retrieves the version of the java vm indicated by the command string.
	 * 
	 * @param command
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static String getJavaVersion(String command) throws IOException,
			InterruptedException {
		String run_command = "'" + command + "' -version";
		System.out.println(run_command);
		Process p;
		String[] args = { command, "-version" };
		p = Runtime.getRuntime().exec(args);
		p.waitFor();
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				p.getErrorStream()));
		String line = reader.readLine();
		int row = 0;
		while (line != null) {
			line = reader.readLine();
			if (line.startsWith("Java(TM)")) {
				if (line.contains("build")) {
					int start = line.indexOf("build");
					System.out.println(start);
					return line.substring(start + "build".length())
							.replace(")", "").trim();
				} else {
					return null;
				}
			}
		}
		return null;
	}
}
