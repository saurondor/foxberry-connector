/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.tiempometa.muestradatos;

import javax.swing.JOptionPane;

import com.tiempometa.utils.TiempometaUtils;

/**
 * @author Gerardo Tasistro
 * 
 *         Copyright 2015 Gerardo Tasistro Licensed un the Mozilla Public
 *         License, v. 2.0
 * 
 */
public class ReaderSettings {

	public static final String PROPERTY_DATABASENAME = "DATABASENAME";
	public static final String PROPERTY_FOXBERRY_ADDRESS = "FOXBERRY_ADDRESS";
	public static final String PROPERTY_TCPIP_READER_TYPE = "TCPIIP_READER_TYPE";
	public static final String PROPERTY_PREFERRED_READER = "PREFERRED_READER";
	public static final String PROPERTY_PREFERRED_ANTENNA = "PREFERRED_ANTENNA";
	public static final String PROPERTY_USB_PORT = "USB_PORT";
	public static final String PROPERTY_USB_REGION = "USB_REGION";
	public static final String PROPERTY_FILTER_WINDOW = "FILTER_WINDOW";

	private String databaseName;
	private String foxberryReaderAddress;
	private String tcpIpReaderType;
	private String preferredReader;
	private String preferredAntenna;
	private String usbPort;
	private String usbRegion;
	private Integer filterWindow = 5000;

	public void loadSettings(String settingsFile) throws Exception {
		if (!TiempometaUtils.verifyPropertyFile(settingsFile)) {
			if (!TiempometaUtils.verifySettingsDirectory()) {
				TiempometaUtils.createSettingsDirectory();
			}
		}
		databaseName = TiempometaUtils.getProperty(PROPERTY_DATABASENAME,
				settingsFile);
		foxberryReaderAddress = TiempometaUtils.getProperty(
				PROPERTY_FOXBERRY_ADDRESS, settingsFile);
		tcpIpReaderType = TiempometaUtils.getProperty(
				PROPERTY_TCPIP_READER_TYPE, settingsFile);
		preferredReader = TiempometaUtils.getProperty(
				PROPERTY_PREFERRED_READER, settingsFile);
		preferredAntenna = TiempometaUtils.getProperty(
				PROPERTY_PREFERRED_ANTENNA, settingsFile);
		usbPort = TiempometaUtils.getProperty(PROPERTY_USB_PORT, settingsFile);
		usbRegion = TiempometaUtils.getProperty(PROPERTY_USB_REGION,
				settingsFile);
		try {
			filterWindow = Integer.valueOf(TiempometaUtils.getProperty(
					PROPERTY_FILTER_WINDOW, settingsFile));
		} catch (NumberFormatException e) {

		}
	}

	public void saveSettings(String settingsFile) {
		TiempometaUtils.setProperty(PROPERTY_DATABASENAME, databaseName,
				settingsFile);
		TiempometaUtils.setProperty(PROPERTY_FOXBERRY_ADDRESS,
				foxberryReaderAddress, settingsFile);
		TiempometaUtils.setProperty(PROPERTY_TCPIP_READER_TYPE,
				tcpIpReaderType, settingsFile);
		TiempometaUtils.setProperty(PROPERTY_PREFERRED_READER, preferredReader,
				settingsFile);
		TiempometaUtils.setProperty(PROPERTY_PREFERRED_ANTENNA,
				preferredAntenna, settingsFile);
		TiempometaUtils.setProperty(PROPERTY_USB_PORT, usbPort, settingsFile);
		TiempometaUtils.setProperty(PROPERTY_USB_REGION, usbRegion,
				settingsFile);
		TiempometaUtils.setProperty(PROPERTY_FILTER_WINDOW,
				filterWindow.toString(), settingsFile);
	}

	/**
	 * @return the databaseName
	 */
	public String getDatabaseName() {
		return databaseName;
	}

	/**
	 * @param databaseName
	 *            the databaseName to set
	 */
	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}

	/**
	 * @return the foxberryReaderAddress
	 */
	public String getFoxberryReaderAddress() {
		return foxberryReaderAddress;
	}

	/**
	 * @param foxberryReaderAddress
	 *            the foxberryReaderAddress to set
	 */
	public void setFoxberryReaderAddress(String foxberryReaderAddress) {
		this.foxberryReaderAddress = foxberryReaderAddress;
	}

	/**
	 * @return the tcpIpReaderType
	 */
	public String getTcpIpReaderType() {
		return tcpIpReaderType;
	}

	/**
	 * @param tcpIpReaderType
	 *            the tcpIpReaderType to set
	 */
	public void setTcpIpReaderType(String tcpIpReaderType) {
		this.tcpIpReaderType = tcpIpReaderType;
	}

	/**
	 * @return the preferredReader
	 */
	public String getPreferredReader() {
		return preferredReader;
	}

	/**
	 * @param preferredReader
	 *            the preferredReader to set
	 */
	public void setPreferredReader(String preferredReader) {
		this.preferredReader = preferredReader;
	}

	/**
	 * @return the preferredAntenna
	 */
	public String getPreferredAntenna() {
		return preferredAntenna;
	}

	/**
	 * @param preferredAntenna
	 *            the preferredAntenna to set
	 */
	public void setPreferredAntenna(String preferredAntenna) {
		this.preferredAntenna = preferredAntenna;
	}

	/**
	 * @return the usbPort
	 */
	public String getUsbPort() {
		return usbPort;
	}

	/**
	 * @param usbPort
	 *            the usbPort to set
	 */
	public void setUsbPort(String usbPort) {
		this.usbPort = usbPort;
	}

	/**
	 * @return the usbRegion
	 */
	public String getUsbRegion() {
		return usbRegion;
	}

	/**
	 * @param usbRegion
	 *            the usbRegion to set
	 */
	public void setUsbRegion(String usbRegion) {
		this.usbRegion = usbRegion;
	}

	/**
	 * @return the filterWindow
	 */
	public synchronized Integer getFilterWindow() {
		return filterWindow;
	}

	/**
	 * @param filterWindow
	 *            the filterWindow to set
	 */
	public synchronized void setFilterWindow(Integer filterWindow) {
		this.filterWindow = filterWindow;
	}

}
