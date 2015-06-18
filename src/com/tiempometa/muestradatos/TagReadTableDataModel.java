/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.tiempometa.muestradatos;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;

import com.tiempometa.timing.models.Rfid;

/**
 * @author Gerardo Tasistro gtasistro@tiempometa.com
 * Copyright 2015 Gerardo Tasistro
 * Licensed un the Mozilla Public License, v. 2.0
 * 
 */
public class TagReadTableDataModel extends AbstractTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1928600489509350596L;
	private static final String[] HEADERS = { "Número", "Código" };
	private List<Rfid> data = new ArrayList<Rfid>();

	@Override
	public int getColumnCount() {
		return HEADERS.length;
	}

	@Override
	public int getRowCount() {
		return data.size();
	}

	@Override
	public Object getValueAt(int row, int column) {
		Rfid rfid = data.get(row);
		switch (column) {
		case 0:
			return rfid.getBib();
		case 1:
			return rfid.getRfidString();
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
	public String getColumnName(int column) {
		return HEADERS[column];
	}

	/**
	 * @return the data
	 */
	public List<Rfid> getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(List<Rfid> data) {
		this.data = data;
	}

}
