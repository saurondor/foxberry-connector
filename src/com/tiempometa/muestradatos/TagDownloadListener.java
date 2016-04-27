/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.tiempometa.muestradatos;

import java.util.Date;
import java.util.List;

/**
 * Interface for classes that want to listen to download notifications
 * 
 * @author Gerardo Tasistro
 *
 * Copyright 2015 Gerardo Tasistro
 * Licensed un the Mozilla Public License, v. 2.0
 *
 */
public interface TagDownloadListener {
	
	void notifyDownload(Integer downloadCount, Date latestDownload, List<TagReadingLog> readingLog);

	void notifyDataRequest(Date date);

}
