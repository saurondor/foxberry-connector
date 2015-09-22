/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.tiempometa.muestradatos;

import java.sql.SQLException;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.tiempometa.timing.dao.ChipReadRawDao;
import com.tiempometa.timing.dao.access.ChipReadRawDaoImpl;
import com.tiempometa.timing.models.ChipReadRaw;

/**
 * @author Gerardo Tasistro
 * 
 *         Copyright 2015 Gerardo Tasistro Licensed un the Mozilla Public
 *         License, v. 2.0
 * 
 */
public abstract class ReadFilter implements Runnable {

	protected static final Logger logger = Logger.getLogger(ReadFilter.class);
	protected Map<String, TagReading> tagReads = Collections
			.synchronizedMap(new HashMap<String, TagReading>());
	Long timeWindowMillis = 2000l;
	Long lastTagRead = null;
	ChipReadRawDao chipReadRawDao = new ChipReadRawDaoImpl();
	boolean run = true;
	String checkPoint = null;
	String loadName = null;

	public void initialize(Long timeWindow, String checkPoint, String loadName) {
		logger.info("Initialize filter with time window: " + timeWindow
				+ " check point: " + checkPoint + " load name: " + loadName);
		timeWindowMillis = timeWindow;
		this.checkPoint = checkPoint;
		this.loadName = loadName;
	}

	public void stop() {
		synchronized (this) {
			run = false;
		}
	}

	@Override
	public void run() {
		boolean runMe = true;
		while (runMe) {
			synchronized (this) {
				runMe = run;
			}
			try {
				Thread.sleep(1000);
				flushStaleReads();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void flushStaleReads() {
		// logger.debug("Flush stale readings");
		Set<String> keys = tagReads.keySet();
		// logger.debug("Flush key length " + keys.size());
		for (String key : keys) {
			TagReading reading = tagReads.get(key);
			Date now = new Date();
			// logger.debug(">>> tag read time " +
			// reading.getTime().toGMTString());
			// logger.debug(">>> now time " + now.toGMTString());
			// logger.debug(">>> now millis " + now.getTime());
			Long timeLimit = null;
			synchronized (this) {
				timeLimit = (lastTagRead - timeWindowMillis * 1000);
			}
			Long systemLimit = (now.getTime() - timeWindowMillis * 2);
			// logger.debug(">>> millis " + timeLimit + " vs reading "
			// + reading.getTimeMillis() + " delta "
			// + (timeLimit - reading.getTimeMillis()));
			if ((reading.getTimeMillis() < timeLimit)||(reading.getProcessedMillis() < systemLimit)) {
				logger.debug("*** Reading is stale, flushing " + key);
				saveReading(reading);
				tagReads.remove(key);
			}
		}
	}

	protected void saveReading(TagReading tagReading) {
		ChipReadRaw chipReading = null;
		synchronized (this) {
			chipReading = new ChipReadRaw(null, tagReading.getEpc()
					.toLowerCase(), tagReading.getTime(),
					tagReading.getTimeMillis() / 1000, checkPoint, checkPoint,
					null, ChipReadRaw.STATUS_RAW, ChipReadRaw.FILTERED_READER,
					tagReading.getPeakRssi().toString(), null);
			if (logger.isDebugEnabled()) {
				logger.debug("Saving reading " + chipReading);
			}
		}
		try {
			chipReadRawDao.save(chipReading);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public abstract void addReading(TagReading reading);

	/**
	 * @return the checkPoint
	 */
	public synchronized String getCheckPoint() {
		return checkPoint;
	}

	/**
	 * @param checkPoint
	 *            the checkPoint to set
	 */
	public synchronized void setCheckPoint(String checkPoint) {
		this.checkPoint = checkPoint;
	}

}
