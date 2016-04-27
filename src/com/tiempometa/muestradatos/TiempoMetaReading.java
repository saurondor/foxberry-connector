/**
 * 
 */
package com.tiempometa.muestradatos;

import java.util.Date;

import com.tiempometa.timing.models.ChipReadRaw;

/**
 * @author Gerardo Tasistro
 * 
 */
public class TiempoMetaReading {
	private String rfid_string;
	private Long time;
	private String checkpoint;
	private String message;
	private String created_at;

	public ChipReadRaw toChipReadRaw() {
		ChipReadRaw reading = new ChipReadRaw();
		reading.setCheckPoint(checkpoint);
		Date tagTime = new Date(time / 1000);
		reading.setTime(tagTime);
		reading.setTimeMillis(time / 1000);
		reading.setRfid(rfid_string);
		return reading;
	}

	/**
	 * @return the rfid_string
	 */
	public String getRfid_string() {
		return rfid_string;
	}

	/**
	 * @param rfid_string
	 *            the rfid_string to set
	 */
	public void setRfid_string(String rfid_string) {
		this.rfid_string = rfid_string;
	}

	/**
	 * @return the time
	 */
	public Long getTime() {
		return time;
	}

	/**
	 * @param time
	 *            the time to set
	 */
	public void setTime(Long time) {
		this.time = time;
	}

	/**
	 * @return the checkpoint
	 */
	public String getCheckpoint() {
		return checkpoint;
	}

	/**
	 * @param checkpoint
	 *            the checkpoint to set
	 */
	public void setCheckpoint(String checkpoint) {
		this.checkpoint = checkpoint;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message
	 *            the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TiempoMetaReading [rfid_string=" + rfid_string + ", time="
				+ time + ", checkpoint=" + checkpoint + ", message=" + message
				+ ", created_at=" + created_at + "]";
	}

	/**
	 * @return the created_at
	 */
	public String getCreated_at() {
		return created_at;
	}

	/**
	 * @param created_at the created_at to set
	 */
	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

}
