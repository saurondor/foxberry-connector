/**
 * 
 */
package com.tiempometa.muestradatos;

import java.util.List;

/**
 * @author Gerardo Tasistro
 *
 */
public interface TagReadListener {
	
	void handleReadings(List<TagReading> readings);

}
