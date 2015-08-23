/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.tiempometa.muestradatos;

import java.util.Date;

import org.apache.log4j.Logger;

/**
 * @author Gerardo Tasistro
 *
 * Copyright 2015 Gerardo Tasistro
 * Licensed un the Mozilla Public License, v. 2.0
 *
 */
public class BestReadFilter extends ReadFilter {

	private static final Logger logger = Logger.getLogger(BestReadFilter.class);
	
	@Override
	public void addReading(TagReading reading) {
		logger.debug("Adding reading to filter");
		synchronized (this) {
			lastTagRead = reading.getTimeMillis();
		}
		reading.setProcessedMillis((new Date()).getTime());
		TagReading mapReading = tagReads.get(reading.getEpc());
		if (mapReading == null) {
			logger.debug(">>> Setting reading as valid");
			tagReads.put(reading.getEpc(), reading);
		} else {
			if (reading.getPeakRssi() > mapReading.getPeakRssi()) {
				logger.debug(">>> Updating reading as valid");
				tagReads.put(reading.getEpc(), reading);
			}
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Mejor Lectura";
	}

	/* (non-Javadoc)
	 * @see com.tiempometa.muestradatos.ReadFilter#initialize(java.lang.Long)
	 */
	@Override
	public void initialize(Long timeWindow, String checkPoint, String loadName) {
		logger.info("Initialize Best Read Filter");
		super.initialize(timeWindow, checkPoint, loadName);
		run = true;
	}

}
