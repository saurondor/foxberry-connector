/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.tiempometa.muestradatos;

import java.sql.Savepoint;

/**
 * @author Gerardo Tasistro
 *
 * Copyright 2015 Gerardo Tasistro
 * Licensed un the Mozilla Public License, v. 2.0
 *
 */
public class NoReadFilter extends ReadFilter {

	/* (non-Javadoc)
	 * @see com.tiempometa.muestradatos.ReadFilter#addReading(com.tiempometa.muestradatos.TagReading)
	 */
	@Override
	public void addReading(TagReading reading) {
		// don't filter, just save directly
		saveReading(reading);

	}

}
