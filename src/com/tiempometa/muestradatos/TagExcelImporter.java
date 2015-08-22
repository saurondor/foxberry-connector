/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.tiempometa.muestradatos;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.tiempometa.timing.dao.RfidDao;
import com.tiempometa.timing.dao.access.RfidDaoImpl;
import com.tiempometa.timing.models.Rfid;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

/**
 * @author Gerardo Tasistro
 * 
 *         Copyright 2015 Gerardo Tasistro Licensed under the Mozilla Public
 *         License, v. 2.0
 * 
 */
public class TagExcelImporter {

	Workbook workbook = null;
	RfidDao rfidDao = new RfidDaoImpl();

	public void open(File excelFile) throws BiffException, IOException {
		workbook = Workbook.getWorkbook(excelFile);
	}

	public List<String> getSheetNames() {
		List<String> sheetNames = new ArrayList<String>();
		for (Sheet sheetName : workbook.getSheets()) {
			sheetNames.add(sheetName.getName());
		}
		return sheetNames;
	}

	public List<String> getColumnNames(Integer sheetId) {
		List<String> columnNames = new ArrayList<String>();
		Sheet sheet = workbook.getSheet(sheetId);
		Cell[] row = sheet.getRow(0);
		for (int i = 0; i < row.length; i++) {
			columnNames.add(row[i].getContents());
		}
		return columnNames;
	}

	public int importTags(Integer sheetNumber, Integer bibColumn,
			Integer epcColumn) throws SQLException {
		Sheet sheet = workbook.getSheet(sheetNumber);
		int rows = sheet.getRows();
		for (int i = 1; i < rows; i++) {
			Cell[] row = sheet.getRow(i);
			Rfid rfid = new Rfid();
			rfid.setBib(row[bibColumn].getContents());
			rfid.setRfid(row[epcColumn].getContents());
			rfidDao.save(rfid);
		}
		return rows - 1;
	}

	@Override
	protected void finalize() throws Throwable {
		if (workbook != null) {
			workbook.close();
		}
		super.finalize();
	}
}
