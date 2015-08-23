/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.tiempometa.muestradatos;

import java.sql.Savepoint;

import org.apache.log4j.Logger;

/**
 * @author Gerardo Tasistro
 *
 * Copyright 2015 Gerardo Tasistro
 * Licensed un the Mozilla Public License, v. 2.0
 *
 */
public class NoReadFilter extends ReadFilter {
	
	private static final Logger logger = Logger.getLogger(NoReadFilter.class);

	/* (non-Javadoc)
	 * @see com.tiempometa.muestradatos.ReadFilter#addReading(com.tiempometa.muestradatos.TagReading)
	 */
	@Override
	public void addReading(TagReading reading) {
		// don't filter, just save directly
		logger.debug("Passtrough reading, saving data without filtering.");
		saveReading(reading);

	}
	

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Ninguno";
	}

	/* (non-Javadoc)
	 * @see com.tiempometa.muestradatos.ReadFilter#initialize(java.lang.Long)
	 */
	@Override
	public void initialize(Long timeWindow, String checkPoint, String loadName) {
		logger.info("Initialize No Read Filter");
		super.initialize(timeWindow, checkPoint, loadName);
		run = true;
	}

}
