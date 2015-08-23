/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.tiempometa.muestradatos;

/**
 * @author Gerardo Tasistro
 *
 * Copyright 2015 Gerardo Tasistro
 * Licensed un the Mozilla Public License, v. 2.0
 *
 */
public class BestReadFilter extends ReadFilter {

	@Override
	public void addReading(TagReading reading) {
		logger.debug("Adding reading to filter");
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

}
