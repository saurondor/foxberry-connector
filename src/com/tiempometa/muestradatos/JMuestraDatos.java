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
		databaseLabel.setText(ReaderContext.getSettings().getDatabaseName());
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
		panel5 = new JPanel();
		panel3 = new JPanel();
		panel4 = new JPanel();
		tabbedPane1 = new JTabbedPane();
		panel1 = new JPanel();
		button3 = new JButton();
		button6 = new JButton();
		button4 = new JButton();
		button7 = new JButton();
		button5 = new JButton();
		panel2 = new JPanel();
		label9 = new JLabel();
		label10 = new JLabel();
		label3 = new JLabel();
		readerPortLabel = new JLabel();
		label11 = new JLabel();
		label15 = new JLabel();
		label4 = new JLabel();
		readerStatusLabel = new JLabel();
		label12 = new JLabel();
		label16 = new JLabel();
		label2 = new JLabel();
		rssiLevelLabel = new JLabel();
		label13 = new JLabel();
		label17 = new JLabel();
		label5 = new JLabel();
		regionLabel = new JLabel();
		label14 = new JLabel();
		label18 = new JLabel();
		label6 = new JLabel();
		readPowerLabel = new JLabel();
		label7 = new JLabel();
		writePowerLevel = new JLabel();
		button1 = new JButton();
		button2 = new JButton();
		panel8 = new JPanel();
		button9 = new JButton();
		button8 = new JButton();
		label20 = new JLabel();
		label19 = new JLabel();
		panel6 = new JPanel();
		label21 = new JLabel();
		label8 = new JLabel();
		databaseLabel = new JLabel();
		panel7 = new JPanel();
		CellConstraints cc = new CellConstraints();

		//======== this ========
		setTitle(bundle.getString("JMuestraDatos.this.title"));
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		setIconImage(new ImageIcon(getClass().getResource("/com/tiempometa/resources/tiempometa_icon_large_alpha.png")).getImage());
		setResizable(false);
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());

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

		//======== panel5 ========
		{
			panel5.setLayout(new FormLayout(
				new ColumnSpec[] {
					new ColumnSpec(Sizes.dluX(18)),
					FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
					new ColumnSpec(Sizes.dluX(343))
				},
				new RowSpec[] {
					new RowSpec(Sizes.dluY(17)),
					FormFactory.LINE_GAP_ROWSPEC,
					new RowSpec(Sizes.dluY(17))
				}));
		}
		contentPane.add(panel5, BorderLayout.NORTH);

		//======== panel3 ========
		{
			panel3.setLayout(new FormLayout(
				new ColumnSpec[] {
					new ColumnSpec(Sizes.dluX(16)),
					FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
					FormFactory.DEFAULT_COLSPEC
				},
				new RowSpec[] {
					FormFactory.DEFAULT_ROWSPEC,
					FormFactory.LINE_GAP_ROWSPEC,
					FormFactory.DEFAULT_ROWSPEC,
					FormFactory.LINE_GAP_ROWSPEC,
					FormFactory.DEFAULT_ROWSPEC
				}));
		}
		contentPane.add(panel3, BorderLayout.WEST);

		//======== panel4 ========
		{
			panel4.setLayout(new FormLayout(
				new ColumnSpec[] {
					new ColumnSpec(Sizes.dluX(366)),
					FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
					FormFactory.DEFAULT_COLSPEC
				},
				new RowSpec[] {
					FormFactory.DEFAULT_ROWSPEC,
					FormFactory.LINE_GAP_ROWSPEC,
					FormFactory.DEFAULT_ROWSPEC,
					FormFactory.LINE_GAP_ROWSPEC,
					FormFactory.DEFAULT_ROWSPEC
				}));

			//======== tabbedPane1 ========
			{
				tabbedPane1.setFont(new Font("Tahoma", Font.BOLD, 16));

				//======== panel1 ========
				{
					panel1.setLayout(new FormLayout(
						new ColumnSpec[] {
							new ColumnSpec(Sizes.dluX(15)),
							FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
							new ColumnSpec(Sizes.dluX(120)),
							FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
							new ColumnSpec(Sizes.dluX(47)),
							FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
							new ColumnSpec(Sizes.dluX(130)),
							FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
							new ColumnSpec(Sizes.dluX(28))
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
							FormFactory.DEFAULT_ROWSPEC
						}));

					//---- button3 ----
					button3.setText(bundle.getString("JMuestraDatos.button3.text"));
					button3.setIcon(new ImageIcon(getClass().getResource("/com/tiempometa/resources/check_64.png")));
					button3.setHorizontalAlignment(SwingConstants.LEFT);
					button3.setRolloverIcon(null);
					button3.setPressedIcon(null);
					button3.setFont(new Font("Tahoma", Font.BOLD, 16));
					panel1.add(button3, cc.xywh(3, 3, 3, 1));

					//---- button6 ----
					button6.setText(bundle.getString("JMuestraDatos.button6.text"));
					button6.setIcon(new ImageIcon(getClass().getResource("/com/tiempometa/resources/load_64.png")));
					button6.setHorizontalAlignment(SwingConstants.LEFT);
					button6.setFont(new Font("Tahoma", Font.BOLD, 16));
					panel1.add(button6, cc.xywh(7, 3, 3, 1));

					//---- button4 ----
					button4.setText(bundle.getString("JMuestraDatos.button4.text"));
					button4.setIcon(new ImageIcon(getClass().getResource("/com/tiempometa/resources/scan_64.png")));
					button4.setHorizontalAlignment(SwingConstants.LEFT);
					button4.setFont(new Font("Tahoma", Font.BOLD, 16));
					panel1.add(button4, cc.xywh(3, 5, 3, 1));

					//---- button7 ----
					button7.setText(bundle.getString("JMuestraDatos.button7.text"));
					button7.setIcon(new ImageIcon(getClass().getResource("/com/tiempometa/resources/counter_64.png")));
					button7.setHorizontalAlignment(SwingConstants.LEFT);
					button7.setFont(new Font("Tahoma", Font.BOLD, 16));
					panel1.add(button7, cc.xywh(7, 5, 3, 1));

					//---- button5 ----
					button5.setText(bundle.getString("JMuestraDatos.button5.text"));
					button5.setIcon(new ImageIcon(getClass().getResource("/com/tiempometa/resources/record_64.png")));
					button5.setHorizontalAlignment(SwingConstants.LEFT);
					button5.setFont(new Font("Tahoma", Font.BOLD, 16));
					panel1.add(button5, cc.xywh(3, 7, 3, 1));
				}
				tabbedPane1.addTab(bundle.getString("JMuestraDatos.panel1.tab.title"), panel1);

				//======== panel2 ========
				{
					panel2.setLayout(new FormLayout(
						new ColumnSpec[] {
							new ColumnSpec(Sizes.dluX(25)),
							FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
							new ColumnSpec(Sizes.dluX(69)),
							FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
							new ColumnSpec(Sizes.dluX(55)),
							FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
							FormFactory.DEFAULT_COLSPEC,
							FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
							FormFactory.DEFAULT_COLSPEC,
							FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
							new ColumnSpec(Sizes.dluX(57)),
							FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
							new ColumnSpec(Sizes.dluX(73))
						},
						new RowSpec[] {
							new RowSpec(Sizes.dluY(20)),
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

					//---- label9 ----
					label9.setText(bundle.getString("JMuestraDatos.label9.text"));
					label9.setFont(new Font("Tahoma", Font.BOLD, 16));
					label9.setIcon(new ImageIcon(getClass().getResource("/com/tiempometa/resources/record_64.png")));
					panel2.add(label9, cc.xywh(3, 3, 3, 1));

					//---- label10 ----
					label10.setText(bundle.getString("JMuestraDatos.label10.text"));
					label10.setFont(new Font("Tahoma", Font.BOLD, 16));
					label10.setIcon(new ImageIcon(getClass().getResource("/com/tiempometa/resources/check_64.png")));
					panel2.add(label10, cc.xywh(11, 3, 3, 1));

					//---- label3 ----
					label3.setText(bundle.getString("JMuestraDatos.label3.text"));
					panel2.add(label3, cc.xy(3, 5));

					//---- readerPortLabel ----
					readerPortLabel.setText(bundle.getString("JMuestraDatos.readerPortLabel.text"));
					panel2.add(readerPortLabel, cc.xy(5, 5));

					//---- label11 ----
					label11.setText(bundle.getString("JMuestraDatos.label11.text"));
					panel2.add(label11, cc.xy(11, 5));

					//---- label15 ----
					label15.setText(bundle.getString("JMuestraDatos.label15.text"));
					panel2.add(label15, cc.xy(13, 5));

					//---- label4 ----
					label4.setText(bundle.getString("JMuestraDatos.label4.text"));
					panel2.add(label4, cc.xy(3, 7));

					//---- readerStatusLabel ----
					readerStatusLabel.setText(bundle.getString("JMuestraDatos.readerStatusLabel.text"));
					panel2.add(readerStatusLabel, cc.xy(5, 7));

					//---- label12 ----
					label12.setText(bundle.getString("JMuestraDatos.label12.text"));
					panel2.add(label12, cc.xy(11, 7));

					//---- label16 ----
					label16.setText(bundle.getString("JMuestraDatos.label16.text"));
					panel2.add(label16, cc.xy(13, 7));

					//---- label2 ----
					label2.setText(bundle.getString("JMuestraDatos.label2.text"));
					panel2.add(label2, cc.xy(3, 9));

					//---- rssiLevelLabel ----
					rssiLevelLabel.setText(bundle.getString("JMuestraDatos.rssiLevelLabel.text"));
					panel2.add(rssiLevelLabel, cc.xy(5, 9));

					//---- label13 ----
					label13.setText(bundle.getString("JMuestraDatos.label13.text"));
					panel2.add(label13, cc.xy(11, 9));

					//---- label17 ----
					label17.setText(bundle.getString("JMuestraDatos.label17.text"));
					panel2.add(label17, cc.xy(13, 9));

					//---- label5 ----
					label5.setText(bundle.getString("JMuestraDatos.label5.text"));
					panel2.add(label5, cc.xy(3, 11));

					//---- regionLabel ----
					regionLabel.setText(bundle.getString("JMuestraDatos.regionLabel.text"));
					panel2.add(regionLabel, cc.xy(5, 11));

					//---- label14 ----
					label14.setText(bundle.getString("JMuestraDatos.label14.text"));
					panel2.add(label14, cc.xy(11, 11));

					//---- label18 ----
					label18.setText(bundle.getString("JMuestraDatos.label18.text"));
					panel2.add(label18, cc.xy(13, 11));

					//---- label6 ----
					label6.setText(bundle.getString("JMuestraDatos.label6.text"));
					panel2.add(label6, cc.xy(3, 13));

					//---- readPowerLabel ----
					readPowerLabel.setText(bundle.getString("JMuestraDatos.readPowerLabel.text"));
					panel2.add(readPowerLabel, cc.xy(5, 13));

					//---- label7 ----
					label7.setText(bundle.getString("JMuestraDatos.label7.text"));
					panel2.add(label7, cc.xy(3, 15));

					//---- writePowerLevel ----
					writePowerLevel.setText(bundle.getString("JMuestraDatos.writePowerLevel.text"));
					panel2.add(writePowerLevel, cc.xy(5, 15));

					//---- button1 ----
					button1.setText(bundle.getString("JMuestraDatos.button1.text"));
					panel2.add(button1, cc.xywh(3, 17, 3, 1));

					//---- button2 ----
					button2.setText(bundle.getString("JMuestraDatos.button2.text"));
					panel2.add(button2, cc.xywh(11, 17, 3, 1));
				}
				tabbedPane1.addTab(bundle.getString("JMuestraDatos.panel2.tab.title"), panel2);

				//======== panel8 ========
				{
					panel8.setLayout(new FormLayout(
						new ColumnSpec[] {
							new ColumnSpec(Sizes.dluX(20)),
							FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
							new ColumnSpec(Sizes.dluX(160)),
							FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
							new ColumnSpec(Sizes.dluX(160))
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
							FormFactory.DEFAULT_ROWSPEC
						}));

					//---- button9 ----
					button9.setText(bundle.getString("JMuestraDatos.button9.text"));
					button9.setIcon(new ImageIcon(getClass().getResource("/com/tiempometa/resources/set_time_64.png")));
					button9.setHorizontalAlignment(SwingConstants.LEFT);
					button9.setFont(new Font("Tahoma", Font.BOLD, 16));
					panel8.add(button9, cc.xy(3, 3));

					//---- button8 ----
					button8.setText(bundle.getString("JMuestraDatos.button8.text"));
					button8.setIcon(new ImageIcon(getClass().getResource("/com/tiempometa/resources/get_time_64.png")));
					button8.setHorizontalAlignment(SwingConstants.LEFT);
					button8.setFont(new Font("Tahoma", Font.BOLD, 16));
					panel8.add(button8, cc.xy(5, 3));

					//---- label20 ----
					label20.setText(bundle.getString("JMuestraDatos.label20.text"));
					label20.setFont(new Font("Tahoma", Font.BOLD, 14));
					panel8.add(label20, cc.xy(3, 5));

					//---- label19 ----
					label19.setText(bundle.getString("JMuestraDatos.label19.text"));
					label19.setFont(new Font("Tahoma", Font.BOLD, 14));
					panel8.add(label19, cc.xy(5, 5));
				}
				tabbedPane1.addTab(bundle.getString("JMuestraDatos.panel8.tab.title"), panel8);
			}
			panel4.add(tabbedPane1, cc.xy(1, 1));
		}
		contentPane.add(panel4, BorderLayout.CENTER);

		//======== panel6 ========
		{
			panel6.setLayout(new FormLayout(
				new ColumnSpec[] {
					FormFactory.DEFAULT_COLSPEC,
					FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
					new ColumnSpec(Sizes.dluX(354))
				},
				new RowSpec[] {
					FormFactory.DEFAULT_ROWSPEC,
					FormFactory.LINE_GAP_ROWSPEC,
					FormFactory.DEFAULT_ROWSPEC,
					FormFactory.LINE_GAP_ROWSPEC,
					FormFactory.DEFAULT_ROWSPEC
				}));

			//---- label21 ----
			label21.setText(bundle.getString("JMuestraDatos.label21.text"));
			panel6.add(label21, cc.xy(1, 1));

			//---- label8 ----
			label8.setText(bundle.getString("JMuestraDatos.label8.text"));
			label8.setFont(new Font("Tahoma", Font.BOLD, 16));
			panel6.add(label8, cc.xy(1, 3));

			//---- databaseLabel ----
			databaseLabel.setText(bundle.getString("JMuestraDatos.databaseLabel.text"));
			panel6.add(databaseLabel, cc.xywh(1, 5, 3, 1));
		}
		contentPane.add(panel6, BorderLayout.SOUTH);

		//======== panel7 ========
		{
			panel7.setLayout(new FormLayout(
				new ColumnSpec[] {
					FormFactory.DEFAULT_COLSPEC,
					FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
					FormFactory.DEFAULT_COLSPEC
				},
				new RowSpec[] {
					FormFactory.DEFAULT_ROWSPEC,
					FormFactory.LINE_GAP_ROWSPEC,
					FormFactory.DEFAULT_ROWSPEC,
					FormFactory.LINE_GAP_ROWSPEC,
					FormFactory.DEFAULT_ROWSPEC
				}));
		}
		contentPane.add(panel7, BorderLayout.EAST);
		setSize(705, 630);
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
	private JPanel panel5;
	private JPanel panel3;
	private JPanel panel4;
	private JTabbedPane tabbedPane1;
	private JPanel panel1;
	private JButton button3;
	private JButton button6;
	private JButton button4;
	private JButton button7;
	private JButton button5;
	private JPanel panel2;
	private JLabel label9;
	private JLabel label10;
	private JLabel label3;
	private JLabel readerPortLabel;
	private JLabel label11;
	private JLabel label15;
	private JLabel label4;
	private JLabel readerStatusLabel;
	private JLabel label12;
	private JLabel label16;
	private JLabel label2;
	private JLabel rssiLevelLabel;
	private JLabel label13;
	private JLabel label17;
	private JLabel label5;
	private JLabel regionLabel;
	private JLabel label14;
	private JLabel label18;
	private JLabel label6;
	private JLabel readPowerLabel;
	private JLabel label7;
	private JLabel writePowerLevel;
	private JButton button1;
	private JButton button2;
	private JPanel panel8;
	private JButton button9;
	private JButton button8;
	private JLabel label20;
	private JLabel label19;
	private JPanel panel6;
	private JLabel label21;
	private JLabel label8;
	private JLabel databaseLabel;
	private JPanel panel7;
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
