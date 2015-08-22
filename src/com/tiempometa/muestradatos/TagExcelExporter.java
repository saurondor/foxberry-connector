/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.tiempometa.muestradatos;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import com.tiempometa.timing.models.Rfid;

/**
 * @author Gerardo Tasistro
 * 
 *         Copyright 2015 Gerardo Tasistro Licensed under the Mozilla Public
 *         License, v. 2.0
 * 
 */
public class TagExcelExporter {

	WritableWorkbook workbook = null;
	String[] header = { "número", "epc" };

	public void open(File excelFile) throws IOException {
		workbook = Workbook.createWorkbook(excelFile);
	}

	public void export(List<Rfid> rfidList) throws RowsExceededException,
			WriteException, IOException {
		WritableSheet sheet = workbook.createSheet("tags", 1);
		// add header
		Label label = new Label(0, 0, header[0]);
		sheet.addCell(label);
		label = new Label(1, 0, header[1]);
		sheet.addCell(label);
		int rowCount = 1;
		for (Rfid rfid : rfidList) {
			label = new Label(0, rowCount, rfid.getBib());
			sheet.addCell(label);
			label = new Label(1, rowCount, rfid.getRfid());
			sheet.addCell(label);
			rowCount = rowCount + 1;
		}
		workbook.write();
		workbook.close();
	}
}
