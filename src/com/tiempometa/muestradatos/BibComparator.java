 /* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package com.tiempometa.muestradatos;

import java.util.Comparator;

/**
 * @author Gerardo Tasistro gtasistro@tiempometa.com
 * Copyright 2015 Gerardo Tasistro
 * Licensed un the Mozilla Public License, v. 2.0
 * 
 */
public class BibComparator implements Comparator<String> {

	@Override
	public int compare(String arg0, String arg1) {
		try {
			return Integer.valueOf(arg0).compareTo(Integer.valueOf(arg1));
		} catch (Exception e) {
			// TODO: handle exception
		}
		return 0;
	}

}
