/**
 * 
 */
package com.tiempometa.muestradatos;

/**
 * @author Gerardo Tasistro
 *
 */
public interface ReaderStatusListener {
	
	void connected();
	void disconnected();
	void startedReading();
	void stoppedReading();
	void updatedRegion(String regionName);
	void updatedReadPower(Integer readPower);
	void updatedWritePower(Integer writePower);

}
