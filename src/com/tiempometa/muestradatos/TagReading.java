/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.tiempometa.muestradatos;

import java.util.Date;

import org.apache.commons.codec.binary.Hex;

import com.thingmagic.TagData;
import com.thingmagic.TagReadData;

/**
 * @author Gerardo Tasistro gtasistro@tiempometa.com Copyright 2015 Gerardo
 *         Tasistro Licensed under the Mozilla Public License, v. 2.0
 * 
 */
public class TagReading {
	public static final String KEEP_ALIVE = "*";

	private TagReadData tagReadData;
	private String reader;
	private String antenna;
	private String epc;
	private String tid;
	private String userData;
	private Long timeMillis;
	private Date time;
	private Integer peakRssi;
	private Boolean valid = false;

	public TagReading() {
		super();
	}

	public TagReading(TagReadData tagReadData) {
		this.tagReadData = tagReadData;
		this.epc = tagReadData.epcString();
		this.peakRssi = tagReadData.getRssi();
		this.antenna = String.valueOf(tagReadData.getAntenna());
		this.timeMillis = tagReadData.getTime();
		this.time = new Date(this.timeMillis);
	}

	public TagReading(String data) {
		super();
		String[] fields = data.replaceAll("\\r", "").split(",");
		switch (fields.length) {
		case 0:
			break;
		case 1:
			break;
		case 2:
			valid = true;
			// single field data packet is keep alive
			antenna = fields[1];
			break;
		case 7:
			// six field data packet includes user data
			userData = fields[6];
		case 6:
			tid = fields[5];
		case 5:
			// five field data packet lacks user data
			valid = true;
			reader = fields[0];
			antenna = fields[1];
			epc = fields[2];
			try {
				timeMillis = Long.valueOf(fields[3]);
				time = new Date(timeMillis / 1000);
				peakRssi = Integer.parseInt(fields[4]);
			} catch (NumberFormatException e) {
				valid = false;
			}
			break;
		default:

		}
	}

	/**
	 * Returns true if reading is a keepalive package
	 * 
	 * @return
	 */
	public boolean isKeepAlive() {
		if (antenna == null) {
			return false;
		}
		return antenna.equals(KEEP_ALIVE);
	}

	/**
	 * @return the reader
	 */
	public String getReader() {
		return reader;
	}

	/**
	 * @param reader
	 *            the reader to set
	 */
	public void setReader(String reader) {
		this.reader = reader;
	}

	/**
	 * @return the antenna
	 */
	public String getAntenna() {
		return antenna;
	}

	/**
	 * @param antenna
	 *            the antenna to set
	 */
	public void setAntenna(String antenna) {
		this.antenna = antenna;
	}

	/**
	 * @return the epc
	 */
	public String getEpc() {
		return epc;
	}

	/**
	 * @param epc
	 *            the epc to set
	 */
	public void setEpc(String epc) {
		this.epc = epc;
	}

	/**
	 * @return the tid
	 */
	public String getTid() {
		return tid;
	}

	/**
	 * @param tid
	 *            the tid to set
	 */
	public void setTid(String tid) {
		this.tid = tid;
	}

	/**
	 * @return the userData
	 */
	public String getUserData() {
		return userData;
	}

	/**
	 * @param userData
	 *            the userData to set
	 */
	public void setUserData(String userData) {
		this.userData = userData;
	}

	/**
	 * @return the timeMillis
	 */
	public Long getTimeMillis() {
		return timeMillis;
	}

	/**
	 * @param timeMillis
	 *            the timeMillis to set
	 */
	public void setTimeMillis(Long timeMillis) {
		this.timeMillis = timeMillis;
	}

	/**
	 * @return the time
	 */
	public Date getTime() {
		return time;
	}

	/**
	 * @param time
	 *            the time to set
	 */
	public void setTime(Date time) {
		this.time = time;
	}

	/**
	 * @return the peakRssi
	 */
	public Integer getPeakRssi() {
		return peakRssi;
	}

	/**
	 * @param peakRssi
	 *            the peakRssi to set
	 */
	public void setPeakRssi(Integer peakRssi) {
		this.peakRssi = peakRssi;
	}

	/**
	 * @return the valid
	 */
	public Boolean isValid() {
		return valid;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TagReading [reader=" + reader + ", antenna=" + antenna
				+ ", epc=" + epc + ", tid=" + tid + ", userData=" + userData
				+ ", timeMillis=" + timeMillis + ", time=" + time
				+ ", peakRssi=" + peakRssi + ", isKeepAlive()=" + isKeepAlive()
				+ ", isValid()=" + isValid() + "]";
	}

	/**
	 * @return the tagReadData
	 */
	public TagReadData getTagReadData() {
		return tagReadData;
	}

	/**
	 * @param tagReadData
	 *            the tagReadData to set
	 */
	public void setTagReadData(TagReadData tagReadData) {
		this.tagReadData = tagReadData;
	}

}
