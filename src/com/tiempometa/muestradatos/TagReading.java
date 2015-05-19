/**
 * 
 */
package com.tiempometa.muestradatos;

import java.util.Date;

/**
 * @author Gerardo Tasistro
 *
 */
public class TagReading {
	public static final String KEEP_ALIVE = "*";
	
	private String reader;
	private String antenna;
	private String epc;
	private String tid;
	private String userData;
	private Long timeMillis;
	private Date time;
	private Integer peakRssi;
	private Boolean valid = false;
	
	public TagReading() {
		super();
	}
	
	public TagReading(String data) {
		super();
		String[] fields = data.split(",");
		switch (fields.length) {
		case 0:
			break;
		case 1:
			valid = true;
			// single field data packet is keep alive
			antenna = fields[0];
			break;
		case 6:
			// six field data packet includes user data
			userData = fields[5];
		case 5:
			// five field data packet lacks user data
			valid = true;
			antenna = fields[0];
			epc = fields[1];
			try {
				timeMillis = Long.valueOf(fields[2]);
				time = new Date(timeMillis/1000);
				peakRssi = Integer.valueOf(fields[3]);
			} catch (NumberFormatException e) {
				valid = false;
			}
			tid = fields[4];
			break;
		default:
			
		}
	}


	/**
	 * Returns true if reading is a keepalive package
	 * @return
	 */
	public boolean isKeepAlive() {
		return antenna.equals(KEEP_ALIVE);
	}

	/**
	 * @return the reader
	 */
	public String getReader() {
		return reader;
	}

	/**
	 * @param reader the reader to set
	 */
	public void setReader(String reader) {
		this.reader = reader;
	}

	/**
	 * @return the antenna
	 */
	public String getAntenna() {
		return antenna;
	}

	/**
	 * @param antenna the antenna to set
	 */
	public void setAntenna(String antenna) {
		this.antenna = antenna;
	}

	/**
	 * @return the epc
	 */
	public String getEpc() {
		return epc;
	}

	/**
	 * @param epc the epc to set
	 */
	public void setEpc(String epc) {
		this.epc = epc;
	}

	/**
	 * @return the tid
	 */
	public String getTid() {
		return tid;
	}

	/**
	 * @param tid the tid to set
	 */
	public void setTid(String tid) {
		this.tid = tid;
	}

	/**
	 * @return the userData
	 */
	public String getUserData() {
		return userData;
	}

	/**
	 * @param userData the userData to set
	 */
	public void setUserData(String userData) {
		this.userData = userData;
	}

	/**
	 * @return the timeMillis
	 */
	public Long getTimeMillis() {
		return timeMillis;
	}

	/**
	 * @param timeMillis the timeMillis to set
	 */
	public void setTimeMillis(Long timeMillis) {
		this.timeMillis = timeMillis;
	}

	/**
	 * @return the time
	 */
	public Date getTime() {
		return time;
	}

	/**
	 * @param time the time to set
	 */
	public void setTime(Date time) {
		this.time = time;
	}

	/**
	 * @return the peakRssi
	 */
	public Integer getPeakRssi() {
		return peakRssi;
	}

	/**
	 * @param peakRssi the peakRssi to set
	 */
	public void setPeakRssi(Integer peakRssi) {
		this.peakRssi = peakRssi;
	}

	/**
	 * @return the valid
	 */
	public Boolean isValid() {
		return valid;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TagReading [reader=" + reader + ", antenna=" + antenna
				+ ", epc=" + epc + ", tid=" + tid + ", userData=" + userData
				+ ", timeMillis=" + timeMillis + ", time=" + time
				+ ", peakRssi=" + peakRssi + ", isKeepAlive()=" + isKeepAlive()
				+ ", isValid()=" + isValid() + "]";
	}

}
