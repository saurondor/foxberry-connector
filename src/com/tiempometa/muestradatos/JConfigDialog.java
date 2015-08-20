/*
 * Created by JFormDesigner on Tue May 19 08:27:31 CDT 2015
 */
/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package com.tiempometa.muestradatos;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.*;

import javax.swing.*;

import org.apache.log4j.Logger;

import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import com.thingmagic.ReaderException;
import com.tiempometa.foxberry.FoxberryReader;
import com.tiempometa.thingmagic.UsbReader;
import com.tiempometa.timing.dao.JdbcConnector;

/**
 * @author Gerardo Tasistro gtasistro@tiempometa.com Copyright 2015 Gerardo
 *         Tasistro Licensed under the Mozilla Public License, v. 2.0
 * 
 */
public class JConfigDialog extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2477824460530549287L;
	private static final Logger logger = Logger.getLogger(JConfigDialog.class);
	private boolean databaseConnected = false;

	private void loadSettings() {
		String databaseName = ReaderContext.getSettings().getDatabaseName();
		File databaseFile = new File(databaseName);
		if (databaseFile.exists()) {
			setDatabase(databaseFile);
		} else {
			JOptionPane.showMessageDialog(this,
					"La base de datos no es válida o no existe",
					"Error de base de datos", JOptionPane.ERROR_MESSAGE);
			setDatabase(null);
		}
		readerBoxAddressTextField.setText(ReaderContext.getSettings()
				.getFoxberryReaderAddress());
		readerTypeComboBox.setSelectedItem(ReaderContext.getSettings()
				.getTcpIpReaderType());
		readerComboBox.setSelectedItem(ReaderContext.getSettings()
				.getPreferredReader());
		antennaComboBox.setSelectedItem(ReaderContext.getSettings()
				.getPreferredAntenna());
		commPortComboBox.setSelectedItem(ReaderContext.getSettings()
				.getUsbPort());
		regionComboBox.setSelectedItem(ReaderContext.getSettings()
				.getUsbRegion());
	}

	private void saveSettings() {
		ReaderContext.getSettings()
				.setDatabaseName(databaseTextField.getText());
		ReaderContext.getSettings()
				.setFoxberryReaderAddress(readerBoxAddressTextField.getText());
		ReaderContext.getSettings()
				.setPreferredAntenna(antennaComboBox.getSelectedItem().toString());
		ReaderContext.getSettings()
				.setPreferredReader(readerComboBox.getSelectedItem().toString());
		ReaderContext.getSettings()
				.setTcpIpReaderType(readerTypeComboBox.getSelectedItem().toString());
		ReaderContext.getSettings()
				.setUsbPort(commPortComboBox.getSelectedItem().toString());
		ReaderContext.getSettings()
				.setUsbRegion(regionComboBox.getSelectedItem().toString());
		ReaderContext.saveSettings();
	}

	public JConfigDialog(Frame owner) {
		super(owner);
		initComponents();
		loadSettings();
		setReaders();
	}

	public JConfigDialog(Dialog owner) {
		super(owner);
		initComponents();
		loadSettings();
		setReaders();
	}

	private void setReaders() {
		if (databaseConnected) {
			enableReaders();
		} else {
			disableReaders();
		}
	}

	private void disableReaders() {
		readerBoxAddressTextField.setEnabled(false);
		antennaComboBox.setEnabled(false);
		commPortComboBox.setEnabled(false);
		regionComboBox.setEnabled(false);
		readerTypeComboBox.setEnabled(false);
		readerComboBox.setEnabled(false);
	}

	private void enableReaders() {
		readerBoxAddressTextField.setEnabled(true);
		antennaComboBox.setEnabled(true);
		commPortComboBox.setEnabled(true);
		regionComboBox.setEnabled(true);
		readerTypeComboBox.setEnabled(true);
		readerComboBox.setEnabled((readerTypeComboBox.getSelectedIndex() == 0));
	}

	private void setDatabase(File database) {
		if (database == null) {
			databaseTextField.setText("");
			ReaderContext.setDatabaseFile(null);
		} else {
			ReaderContext.setDatabaseFile(database);
			databaseTextField.setText(ReaderContext.getDatabaseFile()
					.getAbsolutePath());
			try {
				JdbcConnector.connect(ReaderContext.getDatabaseFile()
						.getAbsolutePath(), null, null);
				JOptionPane.showMessageDialog(this,
						"Se abrió exitosamente la base de datos: "
								+ ReaderContext.getDatabaseFile().getName(),
						"Conexión a base de datos",
						JOptionPane.INFORMATION_MESSAGE);
				databaseConnected = true;
			} catch (InstantiationException | IllegalAccessException
					| ClassNotFoundException | SQLException e1) {
				JOptionPane.showMessageDialog(
						this,
						"Error conectando a la base de datos "
								+ e1.getMessage(), "Error de base de datos",
						JOptionPane.ERROR_MESSAGE);
				databaseConnected = true;
			}
		}
	}

	private void databaseSelectButtonActionPerformed(ActionEvent e) {
		JFileChooser fc = new JFileChooser(ReaderContext.getDatabaseFile());
		int response = fc.showOpenDialog(this);
		if (response == JFileChooser.APPROVE_OPTION) {
			setDatabase(fc.getSelectedFile());
		}
		setReaders();
	}

	private void cancelButtonActionPerformed(ActionEvent e) {
		this.dispose();
	}

	private void okButtonActionPerformed(ActionEvent e) {
		saveSettings();
		this.dispose();
	}

	private void readerTypeComboBoxItemStateChanged(ItemEvent e) {
		if (e.getStateChange() == ItemEvent.SELECTED) {
			if (readerTypeComboBox.getSelectedIndex() == 0) {
				readerComboBox.setEnabled(true);
			} else {
				readerComboBox.setEnabled(false);
			}
		}
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY
		// //GEN-BEGIN:initComponents
		ResourceBundle bundle = ResourceBundle.getBundle("com.tiempometa.muestradatos.muestradatos");
		dialogPane = new JPanel();
		contentPanel = new JPanel();
		label4 = new JLabel();
		separator3 = new JSeparator();
		label5 = new JLabel();
		databaseTextField = new JTextField();
		databaseSelectButton = new JButton();
		label7 = new JLabel();
		separator2 = new JSeparator();
		label8 = new JLabel();
		readerBoxAddressTextField = new JTextField();
		label6 = new JLabel();
		readerTypeComboBox = new JComboBox<>();
		label10 = new JLabel();
		readerComboBox = new JComboBox<>();
		label9 = new JLabel();
		antennaComboBox = new JComboBox<>();
		label3 = new JLabel();
		separator1 = new JSeparator();
		label2 = new JLabel();
		commPortComboBox = new JComboBox<>();
		label1 = new JLabel();
		regionComboBox = new JComboBox<>();
		buttonBar = new JPanel();
		okButton = new JButton();
		cancelButton = new JButton();
		CellConstraints cc = new CellConstraints();

		//======== this ========
		setTitle(bundle.getString("JConfigDialog.this.title"));
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());

		//======== dialogPane ========
		{
			dialogPane.setBorder(Borders.DIALOG_BORDER);
			dialogPane.setLayout(new BorderLayout());

			//======== contentPanel ========
			{
				contentPanel.setLayout(new FormLayout(
					new ColumnSpec[] {
						FormFactory.DEFAULT_COLSPEC,
						FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
						new ColumnSpec(Sizes.dluX(115)),
						FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
						new ColumnSpec(Sizes.dluX(73)),
						FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
						FormFactory.DEFAULT_COLSPEC
					},
					new RowSpec[] {
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
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.LINE_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC
					}));

				//---- label4 ----
				label4.setText(bundle.getString("JConfigDialog.label4.text"));
				label4.setFont(new Font("Tahoma", Font.PLAIN, 18));
				contentPanel.add(label4, cc.xy(1, 1));
				contentPanel.add(separator3, cc.xywh(3, 1, 3, 1));

				//---- label5 ----
				label5.setText(bundle.getString("JConfigDialog.label5.text"));
				contentPanel.add(label5, cc.xy(1, 3));
				contentPanel.add(databaseTextField, cc.xywh(3, 3, 3, 1));

				//---- databaseSelectButton ----
				databaseSelectButton.setText(bundle.getString("JConfigDialog.databaseSelectButton.text"));
				databaseSelectButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						databaseSelectButtonActionPerformed(e);
					}
				});
				contentPanel.add(databaseSelectButton, cc.xy(5, 5));

				//---- label7 ----
				label7.setText(bundle.getString("JConfigDialog.label7.text"));
				label7.setFont(new Font("Tahoma", Font.PLAIN, 18));
				contentPanel.add(label7, cc.xy(1, 7));
				contentPanel.add(separator2, cc.xywh(3, 7, 3, 1));

				//---- label8 ----
				label8.setText(bundle.getString("JConfigDialog.label8.text"));
				contentPanel.add(label8, cc.xy(1, 9));
				contentPanel.add(readerBoxAddressTextField, cc.xywh(3, 9, 3, 1));

				//---- label6 ----
				label6.setText(bundle.getString("JConfigDialog.label6.text"));
				contentPanel.add(label6, cc.xy(1, 11));

				//---- readerTypeComboBox ----
				readerTypeComboBox.setModel(new DefaultComboBoxModel<>(new String[] {
					"Foxberry",
					"Speedway"
				}));
				readerTypeComboBox.addItemListener(new ItemListener() {
					@Override
					public void itemStateChanged(ItemEvent e) {
						readerTypeComboBoxItemStateChanged(e);
					}
				});
				contentPanel.add(readerTypeComboBox, cc.xywh(3, 11, 3, 1));

				//---- label10 ----
				label10.setText(bundle.getString("JConfigDialog.label10.text"));
				contentPanel.add(label10, cc.xy(1, 13));

				//---- readerComboBox ----
				readerComboBox.setModel(new DefaultComboBoxModel<>(new String[] {
					"Todos",
					"Lector 1",
					"Lector 2"
				}));
				contentPanel.add(readerComboBox, cc.xywh(3, 13, 3, 1));

				//---- label9 ----
				label9.setText(bundle.getString("JConfigDialog.label9.text"));
				contentPanel.add(label9, cc.xy(1, 15));

				//---- antennaComboBox ----
				antennaComboBox.setModel(new DefaultComboBoxModel<>(new String[] {
					"Todas",
					"Antena 1",
					"Antena 2",
					"Antena 3",
					"Antena 4"
				}));
				contentPanel.add(antennaComboBox, cc.xywh(3, 15, 3, 1));

				//---- label3 ----
				label3.setText(bundle.getString("JConfigDialog.label3.text"));
				label3.setFont(new Font("Tahoma", Font.PLAIN, 18));
				contentPanel.add(label3, cc.xy(1, 17));
				contentPanel.add(separator1, cc.xywh(3, 17, 3, 1));

				//---- label2 ----
				label2.setText(bundle.getString("JConfigDialog.label2.text"));
				contentPanel.add(label2, cc.xy(1, 19));

				//---- commPortComboBox ----
				commPortComboBox.setModel(new DefaultComboBoxModel<>(new String[] {
					"COM1",
					"COM2",
					"COM3",
					"COM4",
					"COM5",
					"COM6",
					"COM7",
					"COM8",
					"COM9",
					"COM10",
					"COM11",
					"COM12",
					"COM13",
					"COM14",
					"COM15",
					"COM16",
					"COM17",
					"COM18",
					"COM19",
					"COM20"
				}));
				contentPanel.add(commPortComboBox, cc.xywh(3, 19, 3, 1));

				//---- label1 ----
				label1.setText(bundle.getString("JConfigDialog.label1.text"));
				contentPanel.add(label1, cc.xy(1, 21));

				//---- regionComboBox ----
				regionComboBox.setModel(new DefaultComboBoxModel<>(new String[] {
					"NA",
					"EU",
					"KR",
					"IN",
					"PRC",
					"EU2",
					"EU3",
					"KR2",
					"AU",
					"NZ",
					"OPEN"
				}));
				contentPanel.add(regionComboBox, cc.xywh(3, 21, 3, 1));
			}
			dialogPane.add(contentPanel, BorderLayout.NORTH);

			//======== buttonBar ========
			{
				buttonBar.setBorder(Borders.BUTTON_BAR_GAP_BORDER);
				buttonBar.setLayout(new FormLayout(
					new ColumnSpec[] {
						FormFactory.GLUE_COLSPEC,
						FormFactory.BUTTON_COLSPEC,
						FormFactory.RELATED_GAP_COLSPEC,
						FormFactory.BUTTON_COLSPEC
					},
					RowSpec.decodeSpecs("pref")));

				//---- okButton ----
				okButton.setText("OK");
				okButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						okButtonActionPerformed(e);
					}
				});
				buttonBar.add(okButton, cc.xy(2, 1));

				//---- cancelButton ----
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
	private JLabel label4;
	private JSeparator separator3;
	private JLabel label5;
	private JTextField databaseTextField;
	private JButton databaseSelectButton;
	private JLabel label7;
	private JSeparator separator2;
	private JLabel label8;
	private JTextField readerBoxAddressTextField;
	private JLabel label6;
	private JComboBox<String> readerTypeComboBox;
	private JLabel label10;
	private JComboBox<String> readerComboBox;
	private JLabel label9;
	private JComboBox<String> antennaComboBox;
	private JLabel label3;
	private JSeparator separator1;
	private JLabel label2;
	private JComboBox<String> commPortComboBox;
	private JLabel label1;
	private JComboBox<String> regionComboBox;
	private JPanel buttonBar;
	private JButton okButton;
	private JButton cancelButton;
	// JFormDesigner - End of variables declaration //GEN-END:variables
}
