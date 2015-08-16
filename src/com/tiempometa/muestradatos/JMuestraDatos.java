/*
 * Created by JFormDesigner on Tue May 19 08:24:05 CDT 2015
 */
/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package com.tiempometa.muestradatos;

import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.*;
import java.util.List;
import java.util.Timer;

import javax.swing.*;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.log4j.Logger;
import org.llrp.ltk.generated.parameters.GPOWriteData;

import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import com.sun.org.apache.bcel.internal.generic.CPInstruction;
import com.thingmagic.Gen2;
import com.thingmagic.Reader;
import com.thingmagic.ReaderCodeException;
import com.thingmagic.ReaderException;
import com.thingmagic.TagData;
import com.thingmagic.Reader.GpioPin;
import com.tiempometa.timing.dao.CategoriesDao;
import com.tiempometa.timing.dao.ParticipantRegistrationDao;
import com.tiempometa.timing.dao.ParticipantsDao;
import com.tiempometa.timing.dao.RegistrationDao;
import com.tiempometa.timing.dao.RfidDao;
import com.tiempometa.timing.dao.access.CategoriesDaoImpl;
import com.tiempometa.timing.dao.access.ParticipantRegistrationDaoImpl;
import com.tiempometa.timing.dao.access.ParticipantsDaoImpl;
import com.tiempometa.timing.dao.access.RegistrationDaoImpl;
import com.tiempometa.timing.dao.access.RfidDaoImpl;
import com.tiempometa.timing.models.Categories;
import com.tiempometa.timing.models.Participants;
import com.tiempometa.timing.models.Registration;
import com.tiempometa.timing.models.Rfid;

/**
 * @author Gerardo Tasistro gtasistro@tiempometa.com
 * Copyright 2015 Gerardo Tasistro
 * Licensed under the Mozilla Public License, v. 2.0
 * 
 */
public class JMuestraDatos extends JFrame implements TagReadListener,
		ReaderStatusListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2902573870520237847L;
	private static final Logger logger = Logger.getLogger(JMuestraDatos.class);
	private JUserDataFrame userDataFrame = new JUserDataFrame();

	private Integer lastTagCount = 0;

	public JMuestraDatos() {
		initComponents();
		this.addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent we) {
				int response = JOptionPane.showConfirmDialog(null,
						"¿Seguro que deseas cerrar la aplicación?",
						"Cerrar Programa", JOptionPane.WARNING_MESSAGE);
				if (response == JOptionPane.YES_OPTION) {
					ReaderContext.stopReading();
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						ReaderContext.disconnectUsbReader();
					} catch (ReaderException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.exit(0);
				}
			}
		});
		readerPortLabel.setText("");
		readerStatusLabel.setText("Desconectado");
		readPowerLabel.setText("0");
		writePowerLevel.setText("0");
		regionLabel.setText("");
		rssiLevelLabel.setText("ND");
		ReaderContext.loadSettings();
		ReaderContext.addReadingListener(this);
		ReaderContext.addReaderStatusListener(this);
	}

	/**
	 * Application entry point
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			// Set System L&F
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (UnsupportedLookAndFeelException e) {
			// handle exception
		} catch (ClassNotFoundException e) {
			// handle exception
		} catch (InstantiationException e) {
			// handle exception
		} catch (IllegalAccessException e) {
			// handle exception
		}

		JMuestraDatos reader = new JMuestraDatos();
		reader.setVisible(true);
	}

	private void verifyDataMenuItemActionPerformed(ActionEvent e) {
		if (userDataFrame.isVisible()) {
			userDataFrame.setVisible(false);
			ReaderContext.removeReadingListener(userDataFrame);
			ReaderContext.stopReading();
		} else {
			try {
				ReaderContext.startReading();
				ReaderContext.addReadingListener(userDataFrame);
				userDataFrame.setVisible(true);
			} catch (ReaderException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	private void programTagsMenuItemActionPerformed(ActionEvent e) {
		JProgramTags programTag = new JProgramTags(this, true);
		programTag.setVisible(true);
	}

	private void configMenuItemActionPerformed(ActionEvent e) {
		JConfigDialog configDialog = new JConfigDialog(this);
		configDialog.setVisible(true);
	}

	private void menuItem2ActionPerformed(ActionEvent e) {
		int response = JOptionPane.showConfirmDialog(null,
				"¿Seguro que deseas cerrar la aplicación?", "Cerrar Programa",
				JOptionPane.WARNING_MESSAGE);
		if (response == JOptionPane.YES_OPTION) {
			ReaderContext.stopReading();
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				ReaderContext.disconnectUsbReader();
			} catch (ReaderException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			this.dispose();
		}
	}

	private void aboutUsMenuItemActionPerformed(ActionEvent e) {
		JAboutUs aboutUs = new JAboutUs(this);
		aboutUs.setVisible(true);
	}

	private void readTagsMenuItemActionPerformed(ActionEvent e) {
		JReadTags readTag = new JReadTags(this, true);
		readTag.setVisible(true);
		// mode = MODE_READ;
		// chipNumber = 1;
		// try {
		// ReaderContext.startReading();
		// } catch (ReaderException e1) {
		// // TODO Auto-generated catch block
		// e1.printStackTrace();
		// }
	}

	private void loadReadingsMenuItemActionPerformed(ActionEvent e) {
		JLoadTimeReadings loadReadings = new JLoadTimeReadings();
		loadReadings.setVisible(true);
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY
		// //GEN-BEGIN:initComponents
		ResourceBundle bundle = ResourceBundle.getBundle("com.tiempometa.muestradatos.muestradatos");
		menuBar1 = new JMenuBar();
		menu1 = new JMenu();
		configMenuItem = new JMenuItem();
		exitMenuItem = new JMenuItem();
		readingModeMenu = new JMenu();
		verifyDataMenuItem = new JMenuItem();
		readTagsMenuItem = new JMenuItem();
		programTagsMenuItem = new JMenuItem();
		loadReadingsMenuItem = new JMenuItem();
		menu3 = new JMenu();
		aboutUsMenuItem = new JMenuItem();
		label8 = new JLabel();
		label1 = new JLabel();
		label9 = new JLabel();
		label3 = new JLabel();
		readerPortLabel = new JLabel();
		label4 = new JLabel();
		readerStatusLabel = new JLabel();
		label2 = new JLabel();
		rssiLevelLabel = new JLabel();
		label5 = new JLabel();
		regionLabel = new JLabel();
		label6 = new JLabel();
		readPowerLabel = new JLabel();
		label7 = new JLabel();
		writePowerLevel = new JLabel();
		CellConstraints cc = new CellConstraints();

		//======== this ========
		setTitle(bundle.getString("JMuestraDatos.this.title"));
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		setIconImage(new ImageIcon(getClass().getResource("/com/tiempometa/resources/tiempometa_icon_large_alpha.png")).getImage());
		Container contentPane = getContentPane();
		contentPane.setLayout(new FormLayout(
			new ColumnSpec[] {
				new ColumnSpec(Sizes.dluX(18)),
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				new ColumnSpec(Sizes.dluX(160))
			},
			new RowSpec[] {
				new RowSpec(Sizes.dluY(27)),
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

		//======== menuBar1 ========
		{

			//======== menu1 ========
			{
				menu1.setText(bundle.getString("JMuestraDatos.menu1.text"));

				//---- configMenuItem ----
				configMenuItem.setText(bundle.getString("JMuestraDatos.configMenuItem.text"));
				configMenuItem.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						configMenuItemActionPerformed(e);
					}
				});
				menu1.add(configMenuItem);
				menu1.addSeparator();

				//---- exitMenuItem ----
				exitMenuItem.setText(bundle.getString("JMuestraDatos.exitMenuItem.text"));
				exitMenuItem.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						menuItem2ActionPerformed(e);
					}
				});
				menu1.add(exitMenuItem);
			}
			menuBar1.add(menu1);

			//======== readingModeMenu ========
			{
				readingModeMenu.setText(bundle.getString("JMuestraDatos.readingModeMenu.text"));
				readingModeMenu.setEnabled(false);

				//---- verifyDataMenuItem ----
				verifyDataMenuItem.setText(bundle.getString("JMuestraDatos.verifyDataMenuItem.text"));
				verifyDataMenuItem.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						verifyDataMenuItemActionPerformed(e);
					}
				});
				readingModeMenu.add(verifyDataMenuItem);

				//---- readTagsMenuItem ----
				readTagsMenuItem.setText(bundle.getString("JMuestraDatos.readTagsMenuItem.text"));
				readTagsMenuItem.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						readTagsMenuItemActionPerformed(e);
					}
				});
				readingModeMenu.add(readTagsMenuItem);

				//---- programTagsMenuItem ----
				programTagsMenuItem.setText(bundle.getString("JMuestraDatos.programTagsMenuItem.text"));
				programTagsMenuItem.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						programTagsMenuItemActionPerformed(e);
					}
				});
				readingModeMenu.add(programTagsMenuItem);

				//---- loadReadingsMenuItem ----
				loadReadingsMenuItem.setText(bundle.getString("JMuestraDatos.loadReadingsMenuItem.text"));
				loadReadingsMenuItem.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						loadReadingsMenuItemActionPerformed(e);
					}
				});
				readingModeMenu.add(loadReadingsMenuItem);
			}
			menuBar1.add(readingModeMenu);

			//======== menu3 ========
			{
				menu3.setText(bundle.getString("JMuestraDatos.menu3.text"));

				//---- aboutUsMenuItem ----
				aboutUsMenuItem.setText(bundle.getString("JMuestraDatos.aboutUsMenuItem.text"));
				aboutUsMenuItem.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						aboutUsMenuItemActionPerformed(e);
					}
				});
				menu3.add(aboutUsMenuItem);
			}
			menuBar1.add(menu3);
		}
		setJMenuBar(menuBar1);

		//---- label8 ----
		label8.setText(bundle.getString("JMuestraDatos.label8.text"));
		label8.setFont(new Font("Tahoma", Font.PLAIN, 16));
		contentPane.add(label8, cc.xy(3, 3));

		//---- label1 ----
		label1.setText(bundle.getString("JMuestraDatos.label1.text"));
		contentPane.add(label1, cc.xy(3, 5));

		//---- label9 ----
		label9.setText(bundle.getString("JMuestraDatos.label9.text"));
		label9.setFont(new Font("Tahoma", Font.PLAIN, 16));
		contentPane.add(label9, cc.xy(3, 7));

		//---- label3 ----
		label3.setText(bundle.getString("JMuestraDatos.label3.text"));
		contentPane.add(label3, cc.xy(3, 9));

		//---- readerPortLabel ----
		readerPortLabel.setText(bundle.getString("JMuestraDatos.readerPortLabel.text"));
		contentPane.add(readerPortLabel, cc.xy(5, 9));

		//---- label4 ----
		label4.setText(bundle.getString("JMuestraDatos.label4.text"));
		contentPane.add(label4, cc.xy(3, 11));

		//---- readerStatusLabel ----
		readerStatusLabel.setText(bundle.getString("JMuestraDatos.readerStatusLabel.text"));
		contentPane.add(readerStatusLabel, cc.xy(5, 11));

		//---- label2 ----
		label2.setText(bundle.getString("JMuestraDatos.label2.text"));
		contentPane.add(label2, cc.xy(3, 13));

		//---- rssiLevelLabel ----
		rssiLevelLabel.setText(bundle.getString("JMuestraDatos.rssiLevelLabel.text"));
		contentPane.add(rssiLevelLabel, cc.xy(5, 13));

		//---- label5 ----
		label5.setText(bundle.getString("JMuestraDatos.label5.text"));
		contentPane.add(label5, cc.xy(3, 15));

		//---- regionLabel ----
		regionLabel.setText(bundle.getString("JMuestraDatos.regionLabel.text"));
		contentPane.add(regionLabel, cc.xy(5, 15));

		//---- label6 ----
		label6.setText(bundle.getString("JMuestraDatos.label6.text"));
		contentPane.add(label6, cc.xy(3, 17));

		//---- readPowerLabel ----
		readPowerLabel.setText(bundle.getString("JMuestraDatos.readPowerLabel.text"));
		contentPane.add(readPowerLabel, cc.xy(5, 17));

		//---- label7 ----
		label7.setText(bundle.getString("JMuestraDatos.label7.text"));
		contentPane.add(label7, cc.xy(3, 19));

		//---- writePowerLevel ----
		writePowerLevel.setText(bundle.getString("JMuestraDatos.writePowerLevel.text"));
		contentPane.add(writePowerLevel, cc.xy(5, 19));
		setSize(425, 335);
		setLocationRelativeTo(getOwner());
		// //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY
	// //GEN-BEGIN:variables
	private JMenuBar menuBar1;
	private JMenu menu1;
	private JMenuItem configMenuItem;
	private JMenuItem exitMenuItem;
	private JMenu readingModeMenu;
	private JMenuItem verifyDataMenuItem;
	private JMenuItem readTagsMenuItem;
	private JMenuItem programTagsMenuItem;
	private JMenuItem loadReadingsMenuItem;
	private JMenu menu3;
	private JMenuItem aboutUsMenuItem;
	private JLabel label8;
	private JLabel label1;
	private JLabel label9;
	private JLabel label3;
	private JLabel readerPortLabel;
	private JLabel label4;
	private JLabel readerStatusLabel;
	private JLabel label2;
	private JLabel rssiLevelLabel;
	private JLabel label5;
	private JLabel regionLabel;
	private JLabel label6;
	private JLabel readPowerLabel;
	private JLabel label7;
	private JLabel writePowerLevel;
	// JFormDesigner - End of variables declaration //GEN-END:variables

	private void redLedOn() {
		Reader.GpioPin[] pins = new Reader.GpioPin[1];
		pins[0] = new GpioPin(2, true);
		try {
			ReaderContext.gpoSet(pins);
		} catch (ReaderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void redLedOff() {
		Reader.GpioPin[] pins = new Reader.GpioPin[1];
		pins[0] = new GpioPin(2, false);
		try {
			ReaderContext.gpoSet(pins);
		} catch (ReaderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void handleReadings(List<TagReading> readings) {
		// try {
		// // Rfid rfid = rfidDao.fetchByChipNumber(chipNumber);
		// // if (rfid == null) {
		// // logger.warn("No such rfid chipnumber:" + chipNumber);
		// // } else {
		// // chipNumber = chipNumber + 1;
		// // logger.info("Programming rfid tag "
		// // + rfid.getRfidString());
		// // epcBytes = Hex.decodeHex(rfid.getRfidString()
		// // .toCharArray());
		// // epc = new Gen2.TagData(epcBytes);
		// // }
		// } catch (SQLException e1) {
		// // TODO Auto-generated catch block
		// e1.printStackTrace();
		// } catch (DecoderException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// System.out.println("Requesting write EPC...");
		// Gen2.WriteTag tagop = new Gen2.WriteTag(epc);
		// }

	}

	@Override
	public void connected() {
		readerPortLabel.setText("Conectado USB");
		readerStatusLabel.setText("No leyendo");
		readingModeMenu.setEnabled(true);

	}

	@Override
	public void disconnected() {
		readerPortLabel.setText("Desconectado");
		readerStatusLabel.setText("Desconectado");
		readingModeMenu.setEnabled(false);

	}

	@Override
	public void startedReading() {
		readerStatusLabel.setText("Leyendo");

	}

	@Override
	public void stoppedReading() {
		readerStatusLabel.setText("No leyendo");

	}

	@Override
	public void updatedRegion(String regionName) {
		regionLabel.setText(regionName);

	}

	@Override
	public void updatedReadPower(Integer readPower) {
		readPowerLabel.setText(readPower.toString());

	}

	@Override
	public void updatedWritePower(Integer writePower) {
		writePowerLevel.setText(writePower.toString());

	}

	@Override
	public void tcpConnected() {
		readerPortLabel.setText("Conectado TCP");
		readingModeMenu.setEnabled(true);
		
	}

	@Override
	public void tcpDisconnected() {
		readerPortLabel.setText("Conectado");
		readingModeMenu.setEnabled(true);
		
	}
}
