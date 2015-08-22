/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package com.tiempometa.muestradatos;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

/**
 * @author Gerardo Tasistro
 * 
 *         Copyright 2015 Gerardo Tasistro Licensed un the Mozilla Public
 *         License, v. 2.0
 * 
 */
public class ExportTagsToExcel {

	WritableWorkbook workbook = null;
	String[] header = { "bib", "epc", "tid" };

	public void open(File excelFile) throws IOException {
		workbook = Workbook.createWorkbook(excelFile);
	}

	public void export(Map<String, Map<String, TagReading>> readings)
			throws RowsExceededException, WriteException, IOException {
		if (workbook == null) {

		} else {
			Set<String> sheetNames = readings.keySet();
			for (String sheetName : sheetNames) {
				WritableSheet sheet = workbook.createSheet(sheetName,
						workbook.getNumberOfSheets() + 1);
				fillSheet(sheet, readings.get(sheetName));
			}
			workbook.write();
			workbook.close();
		}
	}

	private void fillSheet(WritableSheet sheet,
			Map<String, TagReading> readingMap) throws RowsExceededException,
			WriteException {
		// add header
		Label label = new Label(0, 0, header[0]);
		sheet.addCell(label);
		label = new Label(1, 0, header[1]);
		sheet.addCell(label);
		label = new Label(2, 0, header[2]);
		sheet.addCell(label);
		Set<String> keys = readingMap.keySet();
		int rowCount = 1;
		for (String string : keys) {
			TagReading reading = readingMap.get(string);
			label = new Label(0, rowCount, reading.getBib());
			sheet.addCell(label);
			label = new Label(1, rowCount, reading.getEpc());
			sheet.addCell(label);
			label = new Label(2, rowCount, reading.getTid());
			sheet.addCell(label);
			rowCount = rowCount + 1;
		}
	}

}
