/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.tiempometa.muestradatos;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;

import com.tiempometa.timing.models.Rfid;

/**
 * @author Gerardo Tasistro
 * 
 *         Copyright 2015 Gerardo Tasistro Licensed un the Mozilla Public
 *         License, v. 2.0
 * 
 */
public class TimeReadingTableModel extends AbstractTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5677560589908956972L;
	private static final String[] HEADERS = { "Número", "Nombre", "Tiempo" };
	private List<TagReading> data = new ArrayList<TagReading>();
	private SimpleDateFormat dateFormat = new SimpleDateFormat(
			"HH:mm:ss");

	public void clearReadings() {
		data = new ArrayList<TagReading>();
		fireTableDataChanged();
	}
	
	public int getDataSize() {
		return data.size();
	}

	public void addReading(TagReading reading) {
		data.add(reading);
		fireTableDataChanged();
	}

	@Override
	public int getColumnCount() {
		return HEADERS.length;
	}

	@Override
	public int getRowCount() {
		return data.size();
	}

	@Override
	public Object getValueAt(int row, int col) {
		TagReading reading = data.get(row);
		switch (col) {
		case 0:
			if (reading.getBib() == null) {
				return reading.getEpc();
			}
			return reading.getBib();
		case 1:
			return reading.getParticipantFullName();
		case 2:
			return dateFormat.format(reading.getTime());
		default:
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.AbstractTableModel#getColumnName(int)
	 */
	@Override
	public String getColumnName(int col) {
		return HEADERS[col];
	}

}
