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
	public static final String TYPE_TAG = "tag";
	public static final String TYPE_COMMAND_RESPONSE = "response";
	public static final String TYPE_KEEP_ALIVE = "keepalive";

	private TagReadData tagReadData;
	private String reader;
	private String antenna;
	private String epc;
	private String tid;
	private String userData;
	private String bib;
	private Long timeMillis;
	private Date time;
	private Long processedMillis;
	private Integer peakRssi;
	private Boolean valid = false;
	private String stringData = null;
	private String readingType = null;
	private String firstName = null;
	private String lastName = null;
	private String middleName = null;
	private String eventTitle = null;
	private String categoryTitle = null;

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
		this.readingType = TagReading.TYPE_TAG;
	}

	public TagReading(String data) {
		super();
		if (data.length() == 0) {
			valid = false;
			return;
		}
		stringData = data;
		if (data.startsWith("#")) {
			readingType = TagReading.TYPE_COMMAND_RESPONSE;
			// command response
			String[] fields = data.replaceAll("\\r", "").replaceAll("\\n", "")
					.split(",");
			switch (fields.length) {
			case 0:
				break;
			case 1:
				break;
			case 2:
				break;
			case 3:
				System.out.println("Time - " + fields[2]);
				System.out.println("Time + " + (new Date()).getTime());
				Double millis = Double.valueOf(fields[2]) * 1000;
				time = new Date(millis.longValue());
				Long diff = (new Date()).getTime() - time.getTime();
				epc = diff.toString();
				readingType = TagReading.TYPE_COMMAND_RESPONSE;
				timeMillis = time.getTime();

				break;
			}
		} else {
			readingType = TagReading.TYPE_TAG;
			String[] fields = data.replaceAll("\\r", "").split(",");
			switch (fields.length) {
			case 0:
				break;
			case 1:
				break;
			case 2:
				valid = true;
				// single field data packet is keep alive
				readingType = TagReading.TYPE_KEEP_ALIVE;
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

	/**
	 * @return the readingType
	 */
	public String getReadingType() {
		return readingType;
	}

	/**
	 * @return the bib
	 */
	public String getBib() {
		return bib;
	}

	/**
	 * @param bib
	 *            the bib to set
	 */
	public void setBib(String bib) {
		this.bib = bib;
	}

	/**
	 * @return the processedMillis
	 */
	public Long getProcessedMillis() {
		return processedMillis;
	}

	/**
	 * @param processedMillis
	 *            the processedMillis to set
	 */
	public void setProcessedMillis(Long processedMillis) {
		this.processedMillis = processedMillis;
	}

	private String prettyName(String nameValue) {
		if (nameValue == null) {
			return "";
		}
		return nameValue.trim();
	}

	public String getParticipantFullName() {
		return (prettyName(firstName) + " " + prettyName(lastName) + " " + prettyName(middleName))
				.trim();
	}

	/**
	 * @return the firstName
	 */
	public synchronized String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName
	 *            the firstName to set
	 */
	public synchronized void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the lastName
	 */
	public synchronized String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName
	 *            the lastName to set
	 */
	public synchronized void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return the middleName
	 */
	public synchronized String getMiddleName() {
		return middleName;
	}

	/**
	 * @param middleName
	 *            the middleName to set
	 */
	public synchronized void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	/**
	 * @return the eventTitle
	 */
	public synchronized String getEventTitle() {
		return eventTitle;
	}

	/**
	 * @param eventTitle
	 *            the eventTitle to set
	 */
	public synchronized void setEventTitle(String eventTitle) {
		this.eventTitle = eventTitle;
	}

	/**
	 * @return the categoryTitle
	 */
	public synchronized String getCategoryTitle() {
		return categoryTitle;
	}

	/**
	 * @param categoryTitle
	 *            the categoryTitle to set
	 */
	public synchronized void setCategoryTitle(String categoryTitle) {
		this.categoryTitle = categoryTitle;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TagReading [tagReadData=" + tagReadData + ", reader=" + reader
				+ ", antenna=" + antenna + ", epc=" + epc + ", tid=" + tid
				+ ", userData=" + userData + ", bib=" + bib + ", timeMillis="
				+ timeMillis + ", time=" + time + ", processedMillis="
				+ processedMillis + ", peakRssi=" + peakRssi + ", valid="
				+ valid + ", stringData=" + stringData + ", readingType="
				+ readingType + ", firstName=" + firstName + ", lastName="
				+ lastName + ", middleName=" + middleName + ", eventTitle="
				+ eventTitle + ", categoryTitle=" + categoryTitle + "]";
	}

}
