/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.tiempometa.muestradatos;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Stores information regarding the downloaded data from TiempoMeta
 * 
 * @author Gerardo Tasistro
 * 
 *         Copyright 2015 Gerardo Tasistro Licensed un the Mozilla Public
 *         License, v. 2.0
 * 
 */
public class TagReadingLog {
	private String checkpoint;
	private Integer count;
	private Date lastDownload;
	private SimpleDateFormat dateFormat = new SimpleDateFormat(
			"yyyy-MM-dd'T'HH:mm:ss'Z'");

	/**
	 * @return the checkpoint
	 */
	public String getCheckpoint() {
		return checkpoint;
	}

	/**
	 * @param checkpoint
	 *            the checkpoint to set
	 */
	public void setCheckpoint(String checkpoint) {
		this.checkpoint = checkpoint;
	}

	/**
	 * @return the count
	 */
	public Integer getCount() {
		return count;
	}

	/**
	 * @param count
	 *            the count to set
	 */
	public void setCount(Integer count) {
		this.count = count;
	}

	/**
	 * @return the lastDownload
	 */
	public Date getLastDownload() {
		return lastDownload;
	}

	/**
	 * @param lastDownload
	 *            the lastDownload to set
	 */
	public void setLastDownload(Date lastDownload) {
		this.lastDownload = lastDownload;
	}

	public void setLastDownload(String created_at) {
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		try {
			this.lastDownload = dateFormat.parse(created_at);
		} catch (ParseException e) {
			this.lastDownload = null;
		}
	}

}
