/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.tiempometa.muestradatos;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.apache.log4j.Logger;

import com.tiempometa.timing.dao.ChipReadRawDao;
import com.tiempometa.timing.dao.access.ChipReadRawDaoImpl;
import com.tiempometa.timing.models.ChipReadRaw;

/**
 * @author Gerardo Tasistro
 * 
 *         Copyright 2015 Gerardo Tasistro Licensed un the Mozilla Public
 *         License, v. 2.0
 * 
 */
public class DataDownloadWorker implements Runnable {
	private static final Logger logger = Logger
			.getLogger(DataDownloadWorker.class);
	TiempoMetaClient client;
	TagDownloadListener downloadListener = null;
	ChipReadRawDao rDao = new ChipReadRawDaoImpl();
	private boolean runMe = true;

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		boolean run = runMe;
		while (run) {
			List<ChipReadRaw> chipReadList;
			try {
				chipReadList = client.downloadReadings();
				if (logger.isDebugEnabled()) {
					logger.debug("Downloaded chip readings : "
							+ chipReadList.size());
				}
				rDao.batchSave(chipReadList);
				if (chipReadList.size() > 0) {
					downloadListener.notifyDownload(chipReadList.size(),
							new Date(), client.getReadLog());
				}
				downloadListener.notifyDataRequest(new Date());
			} catch (SQLException e) {
				logger.error("SQL Exception " + e.getMessage());
			} catch (ClientProtocolException e) {
				logger.error("Client Protocol Exception " + e.getMessage());
			} catch (IOException e) {
				logger.error("IO Exception " + e.getMessage());
			}
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				logger.error(e.getMessage());
			}
			synchronized (this) {
				run = runMe;
			}
		}
	}

	/**
	 * @return the runMe
	 */
	public boolean isRunMe() {
		return runMe;
	}

	/**
	 * @param runMe
	 *            the runMe to set
	 */
	public void setRunMe(boolean runMe) {
		this.runMe = runMe;
	}

	/**
	 * @return the client
	 */
	public TiempoMetaClient getClient() {
		return client;
	}

	/**
	 * @param client
	 *            the client to set
	 */
	public void setClient(TiempoMetaClient client) {
		this.client = client;
	}

	/**
	 * @return the downloadListener
	 */
	public TagDownloadListener getDownloadListener() {
		return downloadListener;
	}

	/**
	 * @param downloadListener
	 *            the downloadListener to set
	 */
	public void setDownloadListener(TagDownloadListener downloadListener) {
		this.downloadListener = downloadListener;
	}

}
