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
	ChipReadRawDao chipReadRawDao = new ChipReadRawDaoImpl();
	boolean run = true;
	String checkPoint = null;
	String loadName = null;

	public void initialize(Long timeWindow) {
		logger.info("Init First Read Filter");
		timeWindowMillis = timeWindow;
	}

	public void close() {
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
			Long timeLimit = (now.getTime() - timeWindowMillis) * 1000;
			// logger.debug(">>> millis " + timeLimit + " vs reading "
			// + reading.getTimeMillis() + " delta "
			// + (timeLimit - reading.getTimeMillis()));
			if (reading.getTimeMillis() < timeLimit) {
				logger.debug("*** Reading is stale, flushing " + key);
				tagReads.remove(key);
				saveReading(reading);
			}
		}
	}

	private void saveReading(TagReading tagReading) {
		ChipReadRaw chipReading = new ChipReadRaw(null, tagReading.getEpc()
				.toLowerCase(), tagReading.getTime(),
				tagReading.getTimeMillis() / 1000, checkPoint, checkPoint,
				null, ChipReadRaw.STATUS_RAW, ChipReadRaw.FILTERED_READER,
				loadName, null);
		try {
			chipReadRawDao.save(chipReading);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public abstract void addReading(TagReading reading);

}
