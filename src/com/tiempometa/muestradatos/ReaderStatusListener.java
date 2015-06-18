/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.tiempometa.muestradatos;

/**
 * @author Gerardo Tasistro gtasistro@tiempometa.com
 * Copyright 2015 Gerardo Tasistro
 * Licensed un the Mozilla Public License, v. 2.0
 * 
 */
public interface ReaderStatusListener {
	
	void connected();
	void disconnected();
	void tcpConnected();
	void tcpDisconnected();
	void startedReading();
	void stoppedReading();
	void updatedRegion(String regionName);
	void updatedReadPower(Integer readPower);
	void updatedWritePower(Integer writePower);

}
