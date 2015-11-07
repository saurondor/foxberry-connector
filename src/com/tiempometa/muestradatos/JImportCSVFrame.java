/*
 * Created by JFormDesigner on Sat Nov 07 04:38:36 CST 2015
 */

package com.tiempometa.muestradatos;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.List;

import javax.swing.*;

import org.apache.log4j.Logger;

import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import com.tiempometa.timing.dao.ChipReadRawDao;
import com.tiempometa.timing.dao.access.ChipReadRawDaoImpl;
import com.tiempometa.timing.models.ChipReadRaw;

/**
 * @author Gerardo Esteban Tasistro Giubetic
 */
public class JImportCSVFrame extends JFrame {
	/**
	 * 
	 */
	private static final Logger logger = Logger
			.getLogger(JImportCSVFrame.class);
	private static final long serialVersionUID = 7725609553632768890L;
	private File currentImportFile = null;
	private List<ChipReadRaw> readList = new ArrayList<ChipReadRaw>();
	private String checkPoint = null;
	private String phase = null;

	public JImportCSVFrame() {
		initComponents();
	}

	private void selectFileButtonActionPerformed(ActionEvent e) {
		JFileChooser fc = new JFileChooser(currentImportFile);
		int response = fc.showOpenDialog(this);
		if (response == JFileChooser.APPROVE_OPTION) {
			currentImportFile = fc.getSelectedFile();
			importFileTextField.setText(currentImportFile.getAbsolutePath());
		}

	}

	private void importFileButtonActionPerformed(ActionEvent e) {
		if (currentImportFile == null) {

		} else {
			try {
				readList = new ArrayList<ChipReadRaw>();
				BufferedReader reader = new BufferedReader(new FileReader(
						currentImportFile));
				String line = null;
				checkPoint = checkPointTextField.getText();
				phase = phaseTextField.getText();
				try {
					while ((line = reader.readLine()) != null) {
						processCsvLine(line);
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				logger.debug("Loaded " + readList.size() + " readings");
				logger.debug("Saving readings to database");
				ChipReadRawDao chipReadRawDao = new ChipReadRawDaoImpl();
				for (ChipReadRaw chipRead : readList) {
					try {
						chipReadRawDao.save(chipRead);
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				logger.debug("Finished importing");
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	private void processCsvLine(String line) {
		String[] fields = line.split(",");
		ChipReadRaw readRaw = new ChipReadRaw(null, fields[2], new Date(
				Long.valueOf(fields[3])/1000), Long.valueOf(fields[3]) / 1000,
				phase, checkPoint, null, ChipReadRaw.STATUS_RAW,
				ChipReadRaw.UNFILTERED_READER, "", null);

		readList.add(readRaw);
		logger.debug("Added tag " + readRaw.getRfid());
	}

	private void cancelButtonActionPerformed(ActionEvent e) {
		this.dispose();
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY
		// //GEN-BEGIN:initComponents
		ResourceBundle bundle = ResourceBundle
				.getBundle("com.tiempometa.muestradatos.muestradatos");
		dialogPane = new JPanel();
		contentPanel = new JPanel();
		label1 = new JLabel();
		importFileTextField = new JTextField();
		selectFileButton = new JButton();
		label2 = new JLabel();
		checkPointTextField = new JTextField();
		label3 = new JLabel();
		phaseTextField = new JTextField();
		scrollPane1 = new JScrollPane();
		csvImportTable = new JTable();
		importFileButton = new JButton();
		buttonBar = new JPanel();
		cancelButton = new JButton();
		CellConstraints cc = new CellConstraints();

		// ======== this ========
		setTitle(bundle.getString("JImportCSVFrame.this.title"));
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());

		// ======== dialogPane ========
		{
			dialogPane.setBorder(Borders.DIALOG_BORDER);
			dialogPane.setLayout(new BorderLayout());

			// ======== contentPanel ========
			{
				contentPanel.setLayout(new FormLayout(new ColumnSpec[] {
						FormFactory.DEFAULT_COLSPEC,
						FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
						FormFactory.DEFAULT_COLSPEC,
						FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
						new ColumnSpec(Sizes.dluX(143)),
						FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
						FormFactory.DEFAULT_COLSPEC,
						FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
						FormFactory.DEFAULT_COLSPEC }, new RowSpec[] {
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.LINE_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.LINE_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.LINE_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.LINE_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.LINE_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.LINE_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.LINE_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.LINE_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.LINE_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC }));

				// ---- label1 ----
				label1.setText(bundle.getString("JImportCSVFrame.label1.text"));
				contentPanel.add(label1, cc.xy(3, 3));
				contentPanel.add(importFileTextField, cc.xywh(5, 3, 3, 1));

				// ---- selectFileButton ----
				selectFileButton.setText(bundle
						.getString("JImportCSVFrame.selectFileButton.text"));
				selectFileButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						selectFileButtonActionPerformed(e);
					}
				});
				contentPanel.add(selectFileButton, cc.xy(7, 5));

				// ---- label2 ----
				label2.setText(bundle.getString("JImportCSVFrame.label2.text"));
				contentPanel.add(label2, cc.xy(3, 7));
				contentPanel.add(checkPointTextField, cc.xy(5, 7));

				// ---- label3 ----
				label3.setText(bundle.getString("JImportCSVFrame.label3.text"));
				contentPanel.add(label3, cc.xy(3, 9));
				contentPanel.add(phaseTextField, cc.xy(5, 9));

				// ======== scrollPane1 ========
				{
					scrollPane1.setViewportView(csvImportTable);
				}
				contentPanel.add(scrollPane1, cc.xywh(3, 11, 5, 7));

				// ---- importFileButton ----
				importFileButton.setText(bundle
						.getString("JImportCSVFrame.importFileButton.text"));
				importFileButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						importFileButtonActionPerformed(e);
					}
				});
				contentPanel.add(importFileButton, cc.xy(7, 19));
			}
			dialogPane.add(contentPanel, BorderLayout.CENTER);

			// ======== buttonBar ========
			{
				buttonBar.setBorder(Borders.BUTTON_BAR_GAP_BORDER);
				buttonBar.setLayout(new FormLayout(new ColumnSpec[] {
						FormFactory.GLUE_COLSPEC, FormFactory.BUTTON_COLSPEC,
						FormFactory.RELATED_GAP_COLSPEC,
						FormFactory.BUTTON_COLSPEC }, RowSpec
						.decodeSpecs("pref")));

				// ---- cancelButton ----
				cancelButton.setText("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						cancelButtonActionPerformed(e);
					}
				});
				buttonBar.add(cancelButton, cc.xy(4, 1));
			}
			dialogPane.add(buttonBar, BorderLayout.SOUTH);
		}
		contentPane.add(dialogPane, BorderLayout.CENTER);
		pack();
		setLocationRelativeTo(getOwner());
		// //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY
	// //GEN-BEGIN:variables
	private JPanel dialogPane;
	private JPanel contentPanel;
	private JLabel label1;
	private JTextField importFileTextField;
	private JButton selectFileButton;
	private JLabel label2;
	private JTextField checkPointTextField;
	private JLabel label3;
	private JTextField phaseTextField;
	private JScrollPane scrollPane1;
	private JTable csvImportTable;
	private JButton importFileButton;
	private JPanel buttonBar;
	private JButton cancelButton;
	// JFormDesigner - End of variables declaration //GEN-END:variables
}
