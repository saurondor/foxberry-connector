/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.tiempometa.muestradatos;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import javax.swing.table.AbstractTableModel;

/**
 * @author Gerardo Tasistro
 * 
 *         Copyright 2015 Gerardo Tasistro Licensed un the Mozilla Public
 *         License, v. 2.0
 * 
 */
public class DownloadReadingsTableModel extends AbstractTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6938139840480950800L;
	private static final String[] HEADERS = { "Punto", "Lecturas", "Última lectura" };
	private List<TagReadingLog> data = new ArrayList<TagReadingLog>();
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
	@Override
	public int getRowCount() {
		return data.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.TableModel#getColumnCount()
	 */
	@Override
	public int getColumnCount() {
		return HEADERS.length;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		dateFormat.setTimeZone(TimeZone.getDefault());
		TagReadingLog readingLog = data.get(rowIndex);
		switch (columnIndex) {
		case 0:
			return readingLog.getCheckpoint();
		case 1:
			return readingLog.getCount();
		case 2:
			if ((readingLog == null) || (readingLog.getLastDownload() == null)) {
				return null;
			}
			return dateFormat.format(readingLog.getLastDownload());
		default:
			return null;
		}
	}

	/**
	 * @return the data
	 */
	public List<TagReadingLog> getData() {
		return data;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	public void setData(List<TagReadingLog> data) {
		this.data = data;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.AbstractTableModel#getColumnName(int)
	 */
	@Override
	public String getColumnName(int column) {
		return HEADERS[column];
	}

}
