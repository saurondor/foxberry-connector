/*
 * Created by JFormDesigner on Tue May 19 08:24:05 CDT 2015
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
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.Timer;

import javax.swing.*;

import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.http.client.ClientProtocolException;
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
import com.tiempometa.foxberry.FoxberryReader;
import com.tiempometa.timing.dao.CategoriesDao;
import com.tiempometa.timing.dao.JdbcConnector;
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
 * @author Gerardo Tasistro gtasistro@tiempometa.com Copyright 2015 Gerardo
 *         Tasistro Licensed under the Mozilla Public License, v. 2.0
 * 
 */
public class JMuestraDatos extends JFrame implements TagReadListener,
		ReaderStatusListener, TagDownloadListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2902573870520237847L;
	private static final Logger logger = Logger.getLogger(JMuestraDatos.class);
	private JUserDataFrame userDataFrame = new JUserDataFrame();
	private TimeRunner systemTime = new TimeRunner();
	private final SimpleDateFormat dateFormat = new SimpleDateFormat(
			"dd/MM/yyyy HH:mm:ss");

	private Integer lastTagCount = 0;
	private RfidDao rfidDao = new RfidDaoImpl();
	private static SystemProperties systemProperties = new SystemProperties();
	private DataDownloadWorker downloadWorker = null;
	private Thread downloadThread = null;
	private DownloadReadingsTableModel downloadReadingsTableModel = new DownloadReadingsTableModel();

	class TimeRunner implements Runnable {

		private boolean runMe = true;

		@Override
		public void run() {
			while (runMe) {
				final Date time = new Date();
				SwingUtilities.invokeLater(new Runnable() {

					@Override
					public void run() {
						systemTimeLabel.setText(dateFormat.format(time));

					}
				});
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		public void stop() {
			runMe = false;

		}

	}

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
		loadSettings();
		if (ReaderContext.getSettings().getDatabaseName() == null) {
			JOptionPane
					.showMessageDialog(
							this,
							"No se ha configurado la aplicación.\nFavor de configurar antes de continuar",
							"Sin configuración", JOptionPane.WARNING_MESSAGE);
		} else {
			setDatabase(new File(ReaderContext.getSettings().getDatabaseName()));
		}
		ReaderContext.addReadingListener(this);
		ReaderContext.addReaderStatusListener(this);
		Thread thread = new Thread(systemTime);
		thread.start();
		readingLogTable.setModel(downloadReadingsTableModel);
	}

	private void setDatabase(File database) {
		ReaderContext.setDatabaseFile(database);
		try {
			JdbcConnector.connect(ReaderContext.getDatabaseFile()
					.getAbsolutePath(), null, null);
			JOptionPane
					.showMessageDialog(
							this,
							"Se abrió exitosamente la base de datos: "
									+ ReaderContext.getDatabaseFile().getName(),
							"Conexión a base de datos",
							JOptionPane.INFORMATION_MESSAGE);
		} catch (InstantiationException | IllegalAccessException
				| ClassNotFoundException | SQLException e1) {
			JOptionPane.showMessageDialog(this,
					"Error conectando a la base de datos " + e1.getMessage(),
					"Error de base de datos", JOptionPane.ERROR_MESSAGE);
		}
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

		logger.info(systemProperties.java_version);
		logger.info(systemProperties.arch_data_model);
		logger.info(systemProperties.java_home);
		logger.info(systemProperties.user_dir);
		JMuestraDatos reader = new JMuestraDatos();
		reader.setVisible(true);
	}

	private void configMenuItemActionPerformed(ActionEvent e) {
		if (ReaderContext.isUsbConnected()
				|| ReaderContext.isFoxberryConnected()) {
			JOptionPane
					.showMessageDialog(
							this,
							"No se puede cambiar la configuración mientras uno o más lectores estén conectados",
							"Cambio de configuración",
							JOptionPane.WARNING_MESSAGE);
		} else {
			JConfigDialog configDialog = new JConfigDialog(this, true);
			configDialog.setVisible(true);
			loadSettings();
		}
	}

	private void loadSettings() {
		readerStatusLabel.setText("Desconectado");
		readPowerLabel.setText("ND");
		writePowerLevel.setText("ND");
		rssiLevelLabel.setText("ND");
		try {
			ReaderContext.loadSettings();
			regionLabel.setText(ReaderContext.getSettings().getUsbRegion());
			readerPortLabel.setText(ReaderContext.getSettings().getUsbPort());
			databaseLabel
					.setText(ReaderContext.getSettings().getDatabaseName());
			boxIpAddressLabel.setText(ReaderContext.getSettings()
					.getFoxberryReaderAddress());
			preferredAntenaLabel.setText(ReaderContext.getSettings()
					.getPreferredAntenna());
			preferredReaderLabel.setText(ReaderContext.getSettings()
					.getPreferredReader());
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Error cargando configuración "
					+ e.getMessage(), "Error configuración",
					JOptionPane.ERROR_MESSAGE);
		}
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
			if (ReaderContext.isUsbConnected()) {
				try {
					ReaderContext.disconnectUsbReader();
				} catch (ReaderException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			this.dispose();
		}
	}

	private void aboutUsMenuItemActionPerformed(ActionEvent e) {
		JAboutUs aboutUs = new JAboutUs(this);
		aboutUs.setVisible(true);
	}

	private void usbConnectButtonActionPerformed(ActionEvent e) {
		if (ReaderContext.isUsbConnected()) {
			if (ReaderContext.isUsbReading()) {
				JOptionPane
						.showMessageDialog(
								this,
								"Debes detener todas las lecturas antes de desconectar el lector",
								"Lectura activa", JOptionPane.WARNING_MESSAGE);
			} else {
				try {
					ReaderContext.disconnectUsbReader();
					usbConnectButton.setText("Conectar");
					usbStatusLabel.setForeground(Color.RED);
					JOptionPane.showMessageDialog(this,
							"Se desconectó con éxito del lector usb",
							"Desconexión exitosa",
							JOptionPane.INFORMATION_MESSAGE);
				} catch (ReaderException e1) {
					JOptionPane.showMessageDialog(this,
							"Error de desconexión: " + e1.getMessage(),
							"Error USB", JOptionPane.ERROR_MESSAGE);
				}

			}
		} else {
			try {
				logger.info("Connecting to USB reader...");
				ReaderContext.connectUsbReader(ReaderContext.getSettings()
						.getUsbPort());
				usbConnectButton.setText("Desonectar");
				usbStatusLabel.setForeground(Color.GREEN);
				JOptionPane.showMessageDialog(this,
						"Se conectó con éxito al lector usb",
						"Conexión exitosa", JOptionPane.INFORMATION_MESSAGE);
			} catch (ReaderException e1) {
				usbConnectButton.setText("Conectar");
				JOptionPane.showMessageDialog(this,
						"Error de conexión: " + e1.getMessage(), "Error USB",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private void boxConnectButtonActionPerformed(ActionEvent e) {
		if (ReaderContext.isFoxberryConnected()) {
			try {
				ReaderContext.disconnectFoxberry();
				boxConnectButton.setText("Conectar");
				tcpStatusLabel.setForeground(Color.RED);
				JOptionPane.showMessageDialog(this,
						"Se desconectó con éxito de la caja",
						"Desconexión exitosa", JOptionPane.INFORMATION_MESSAGE);
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(this, "Error de desconexión: "
						+ e1.getMessage(), "Error TCP",
						JOptionPane.ERROR_MESSAGE);
			}
		} else {
			try {
				ReaderContext.connectFoxberry();
				boxConnectButton.setText("Desconectar");
				tcpStatusLabel.setForeground(Color.GREEN);
				JOptionPane.showMessageDialog(this,
						"Se conectó con éxito a la caja", "Conexión exitosa",
						JOptionPane.INFORMATION_MESSAGE);
			} catch (UnknownHostException e1) {
				JOptionPane.showMessageDialog(this,
						"Error de conexión: " + e1.getMessage(), "Error TCP",
						JOptionPane.ERROR_MESSAGE);
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(this,
						"Error de conexión: " + e1.getMessage(), "Error TCP",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private void verifyDataButtonActionPerformed(ActionEvent e) {
		if (ReaderContext.isFoxberryConnected()
				|| (ReaderContext.isUsbConnected())) {
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
		} else {
			JOptionPane.showConfirmDialog(this,
					"Se debe conectar a un lector primero",
					"Sin conexión a lectores", JOptionPane.WARNING_MESSAGE);
		}
	}

	private void readTagButtonActionPerformed(ActionEvent e) {
		if (ReaderContext.isFoxberryConnected()
				|| (ReaderContext.isUsbConnected())) {
			JReadTags readTag = new JReadTags(this, true);
			readTag.setVisible(true);
		} else {
			JOptionPane.showConfirmDialog(this,
					"Se debe conectar a un lector primero",
					"Sin conexión a lectores", JOptionPane.WARNING_MESSAGE);
		}
	}

	private void programTagButtonActionPerformed(ActionEvent e) {
		if (ReaderContext.isFoxberryConnected()
				|| (ReaderContext.isUsbConnected())) {
			JProgramTags programTag = new JProgramTags(this, true);
			programTag.setVisible(true);
		} else {
			JOptionPane.showConfirmDialog(this,
					"Se debe conectar a un lector primero",
					"Sin conexión a lectores", JOptionPane.WARNING_MESSAGE);
		}
	}

	private void loadReadingsButtonActionPerformed(ActionEvent e) {
		if (ReaderContext.isFoxberryConnected()
				|| (ReaderContext.isUsbConnected())) {
			JLoadTimeReadings loadReadings = new JLoadTimeReadings();
			loadReadings.setVisible(true);
		} else {
			JOptionPane.showConfirmDialog(this,
					"Se debe conectar a un lector primero",
					"Sin conexión a lectores", JOptionPane.WARNING_MESSAGE);
		}
	}

	private void countTagsButtonActionPerformed(ActionEvent e) {
		if (ReaderContext.isFoxberryConnected()) {
			JCountTags countTags = new JCountTags(this);
			countTags.setVisible(true);
		} else {
			JOptionPane.showConfirmDialog(this,
					"Se debe conectar a un lector primero",
					"Sin conexión a lectores", JOptionPane.WARNING_MESSAGE);
		}
	}

	private void getBoxTimeButtonActionPerformed(ActionEvent e) {
		try {
			ReaderContext.getFoxberryTime();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	private void setBoxTimeButtonActionPerformed(ActionEvent e) {
		try {
			ReaderContext.setFoxberryTime();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	private void importTagsMenuItemActionPerformed(ActionEvent e) {
		JImportTags importTags = new JImportTags(this);
		importTags.setVisible(true);
	}

	private void exportTagsMenuItemActionPerformed(ActionEvent e) {
		JFileChooser fc = new JFileChooser();
		int response = fc.showSaveDialog(this);
		if (response == JFileChooser.APPROVE_OPTION) {
			TagExcelExporter exporter = new TagExcelExporter();
			try {
				exporter.open(fc.getSelectedFile());
				List<Rfid> rfidList = rfidDao.findAll();
				exporter.export(rfidList);
				JOptionPane.showMessageDialog(this,
						"Se exportaron los tags con éxito", "Exportar Tags",
						JOptionPane.PLAIN_MESSAGE);
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(this,
						"Error exportando " + e1.getMessage(), "Exportar Tags",
						JOptionPane.ERROR_MESSAGE);
			} catch (SQLException e1) {
				JOptionPane.showMessageDialog(this,
						"Error exportando " + e1.getMessage(), "Exportar Tags",
						JOptionPane.ERROR_MESSAGE);
			} catch (RowsExceededException e1) {
				JOptionPane.showMessageDialog(this,
						"Error exportando " + e1.getMessage(), "Exportar Tags",
						JOptionPane.ERROR_MESSAGE);
			} catch (WriteException e1) {
				JOptionPane.showMessageDialog(this,
						"Error exportando " + e1.getMessage(), "Exportar Tags",
						JOptionPane.ERROR_MESSAGE);
			}

		}
	}

	private void clearTagsMenuItemActionPerformed(ActionEvent e) {
		int response = JOptionPane
				.showConfirmDialog(
						this,
						"¿Seguro que desea borrar todos los tags? Esta operación no se puede deshacer",
						"Borrar tags", JOptionPane.YES_NO_OPTION,
						JOptionPane.WARNING_MESSAGE);
		if (response == JOptionPane.YES_OPTION) {
			try {
				rfidDao.deleteAll();
				JOptionPane.showMessageDialog(this,
						"Se borraron todos los tags", "Despejar Tags",
						JOptionPane.PLAIN_MESSAGE);
			} catch (SQLException e1) {
				JOptionPane.showMessageDialog(this, "Error despejando tags "
						+ e1.getMessage(), "Despejar Tags",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private void configureJavaMenuItemActionPerformed(ActionEvent e) {
		JInstaller installer = new JInstaller(this);
		installer.setVisible(true);
	}

	private void importCSVReadingsMenuItemActionPerformed(ActionEvent e) {
		JImportCSVFrame csvImporter = new JImportCSVFrame();
		csvImporter.setVisible(true);
	}

	private void createRfidCodesMenuItemActionPerformed(ActionEvent e) {
		JCreateTags createTags = new JCreateTags(this);
		createTags.setVisible(true);
	}

	private void downloadButtonActionPerformed(ActionEvent e) {
		if ((downloadWorker != null) && (downloadWorker.isRunMe())) {
			logger.info("Stopping download worker!");
			downloadWorker.setRunMe(false);
			downloadButton.setText("Descargar");
			downloadButton.setBackground(Color.RED);
		} else {
			downloadWorker = new DataDownloadWorker();
			downloadThread = new Thread(downloadWorker);
			TiempoMetaClient client = new TiempoMetaClient();
			client.setPageSize(200);
			client.setApiKey(tiempoMetaApiKey.getText());
			downloadWorker.setClient(client);
			downloadWorker.setDownloadListener(this);
			logger.info("Starting download worker!");
			downloadThread.start();
			downloadButton.setText("Descargando datos...");
			downloadButton.setBackground(Color.GREEN);
		}
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY
		// //GEN-BEGIN:initComponents
		ResourceBundle bundle = ResourceBundle.getBundle("com.tiempometa.muestradatos.muestradatos");
		menuBar1 = new JMenuBar();
		menu1 = new JMenu();
		configMenuItem = new JMenuItem();
		createRfidCodesMenuItem = new JMenuItem();
		importTagsMenuItem = new JMenuItem();
		exportTagsMenuItem = new JMenuItem();
		clearTagsMenuItem = new JMenuItem();
		importCSVReadingsMenuItem = new JMenuItem();
		exitMenuItem = new JMenuItem();
		menu3 = new JMenu();
		aboutUsMenuItem = new JMenuItem();
		panel5 = new JPanel();
		panel3 = new JPanel();
		panel4 = new JPanel();
		tabbedPane1 = new JTabbedPane();
		panel2 = new JPanel();
		label9 = new JLabel();
		label10 = new JLabel();
		label3 = new JLabel();
		readerPortLabel = new JLabel();
		label11 = new JLabel();
		boxIpAddressLabel = new JLabel();
		label4 = new JLabel();
		readerStatusLabel = new JLabel();
		label2 = new JLabel();
		rssiLevelLabel = new JLabel();
		label13 = new JLabel();
		preferredReaderLabel = new JLabel();
		label5 = new JLabel();
		regionLabel = new JLabel();
		label14 = new JLabel();
		preferredAntenaLabel = new JLabel();
		label6 = new JLabel();
		readPowerLabel = new JLabel();
		label7 = new JLabel();
		writePowerLevel = new JLabel();
		usbConnectButton = new JButton();
		boxConnectButton = new JButton();
		panel1 = new JPanel();
		verifyDataButton = new JButton();
		loadReadingsButton = new JButton();
		readTagButton = new JButton();
		countTagsButton = new JButton();
		programTagButton = new JButton();
		panel8 = new JPanel();
		setBoxTimeButton = new JButton();
		getBoxTimeButton = new JButton();
		label20 = new JLabel();
		label19 = new JLabel();
		systemTimeLabel = new JLabel();
		foxberryTimeLabel = new JLabel();
		foxberryTimeDiffLabel = new JLabel();
		panel9 = new JPanel();
		label18 = new JLabel();
		label12 = new JLabel();
		tiempoMetaApiKey = new JTextField();
		downloadButton = new JButton();
		separator4 = new JSeparator();
		label22 = new JLabel();
		lastRequestLabel = new JLabel();
		label15 = new JLabel();
		lastDownloadLabel = new JLabel();
		label17 = new JLabel();
		downloadCountLabel = new JLabel();
		separator3 = new JSeparator();
		label24 = new JLabel();
		scrollPane1 = new JScrollPane();
		readingLogTable = new JTable();
		panel6 = new JPanel();
		label21 = new JLabel();
		label1 = new JLabel();
		usbStatusLabel = new JLabel();
		label16 = new JLabel();
		tcpStatusLabel = new JLabel();
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

				//---- createRfidCodesMenuItem ----
				createRfidCodesMenuItem.setText(bundle.getString("JMuestraDatos.createRfidCodesMenuItem.text"));
				createRfidCodesMenuItem.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						createRfidCodesMenuItemActionPerformed(e);
					}
				});
				menu1.add(createRfidCodesMenuItem);

				//---- importTagsMenuItem ----
				importTagsMenuItem.setText(bundle.getString("JMuestraDatos.importTagsMenuItem.text"));
				importTagsMenuItem.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						importTagsMenuItemActionPerformed(e);
					}
				});
				menu1.add(importTagsMenuItem);

				//---- exportTagsMenuItem ----
				exportTagsMenuItem.setText(bundle.getString("JMuestraDatos.exportTagsMenuItem.text"));
				exportTagsMenuItem.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						exportTagsMenuItemActionPerformed(e);
					}
				});
				menu1.add(exportTagsMenuItem);

				//---- clearTagsMenuItem ----
				clearTagsMenuItem.setText(bundle.getString("JMuestraDatos.clearTagsMenuItem.text"));
				clearTagsMenuItem.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						clearTagsMenuItemActionPerformed(e);
					}
				});
				menu1.add(clearTagsMenuItem);
				menu1.addSeparator();

				//---- importCSVReadingsMenuItem ----
				importCSVReadingsMenuItem.setText(bundle.getString("JMuestraDatos.importCSVReadingsMenuItem.text"));
				importCSVReadingsMenuItem.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						importCSVReadingsMenuItemActionPerformed(e);
					}
				});
				menu1.add(importCSVReadingsMenuItem);
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
					new ColumnSpec(Sizes.dluX(410)),
					FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
					FormFactory.DEFAULT_COLSPEC
				},
				RowSpec.decodeSpecs("245dlu")));

			//======== tabbedPane1 ========
			{
				tabbedPane1.setFont(new Font("Tahoma", Font.BOLD, 16));

				//======== panel2 ========
				{
					panel2.setLayout(new FormLayout(
						new ColumnSpec[] {
							new ColumnSpec(Sizes.dluX(25)),
							FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
							new ColumnSpec(Sizes.dluX(89)),
							FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
							new ColumnSpec(Sizes.dluX(73)),
							FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
							new ColumnSpec(Sizes.dluX(17)),
							FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
							new ColumnSpec(Sizes.dluX(84)),
							FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
							new ColumnSpec(Sizes.dluX(80))
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
							FormFactory.DEFAULT_ROWSPEC,
							FormFactory.LINE_GAP_ROWSPEC,
							new RowSpec(Sizes.DLUY8)
						}));

					//---- label9 ----
					label9.setText(bundle.getString("JMuestraDatos.label9.text"));
					label9.setFont(new Font("Tahoma", Font.BOLD, 16));
					label9.setIcon(new ImageIcon(getClass().getResource("/com/tiempometa/resources/usb_128.png")));
					label9.setHorizontalAlignment(SwingConstants.CENTER);
					panel2.add(label9, cc.xywh(3, 3, 3, 1));

					//---- label10 ----
					label10.setText(bundle.getString("JMuestraDatos.label10.text"));
					label10.setFont(new Font("Tahoma", Font.BOLD, 16));
					label10.setIcon(new ImageIcon(getClass().getResource("/com/tiempometa/resources/briefcase_128.png")));
					label10.setHorizontalAlignment(SwingConstants.CENTER);
					panel2.add(label10, cc.xywh(9, 3, 3, 1));

					//---- label3 ----
					label3.setText(bundle.getString("JMuestraDatos.label3.text"));
					label3.setFont(new Font("Tahoma", Font.BOLD, 14));
					panel2.add(label3, cc.xy(3, 5));

					//---- readerPortLabel ----
					readerPortLabel.setText(bundle.getString("JMuestraDatos.readerPortLabel.text"));
					panel2.add(readerPortLabel, cc.xy(5, 5));

					//---- label11 ----
					label11.setText(bundle.getString("JMuestraDatos.label11.text"));
					label11.setFont(new Font("Tahoma", Font.BOLD, 14));
					panel2.add(label11, cc.xy(9, 5));

					//---- boxIpAddressLabel ----
					boxIpAddressLabel.setText(bundle.getString("JMuestraDatos.boxIpAddressLabel.text"));
					panel2.add(boxIpAddressLabel, cc.xy(11, 5));

					//---- label4 ----
					label4.setText(bundle.getString("JMuestraDatos.label4.text"));
					label4.setFont(new Font("Tahoma", Font.BOLD, 14));
					panel2.add(label4, cc.xy(3, 7));

					//---- readerStatusLabel ----
					readerStatusLabel.setText(bundle.getString("JMuestraDatos.readerStatusLabel.text"));
					panel2.add(readerStatusLabel, cc.xy(5, 7));

					//---- label2 ----
					label2.setText(bundle.getString("JMuestraDatos.label2.text"));
					label2.setFont(new Font("Tahoma", Font.BOLD, 14));
					panel2.add(label2, cc.xy(3, 9));

					//---- rssiLevelLabel ----
					rssiLevelLabel.setText(bundle.getString("JMuestraDatos.rssiLevelLabel.text"));
					panel2.add(rssiLevelLabel, cc.xy(5, 9));

					//---- label13 ----
					label13.setText(bundle.getString("JMuestraDatos.label13.text"));
					label13.setFont(new Font("Tahoma", Font.BOLD, 14));
					panel2.add(label13, cc.xy(9, 9));

					//---- preferredReaderLabel ----
					preferredReaderLabel.setText(bundle.getString("JMuestraDatos.preferredReaderLabel.text"));
					panel2.add(preferredReaderLabel, cc.xy(11, 9));

					//---- label5 ----
					label5.setText(bundle.getString("JMuestraDatos.label5.text"));
					label5.setFont(new Font("Tahoma", Font.BOLD, 14));
					panel2.add(label5, cc.xy(3, 11));

					//---- regionLabel ----
					regionLabel.setText(bundle.getString("JMuestraDatos.regionLabel.text"));
					panel2.add(regionLabel, cc.xy(5, 11));

					//---- label14 ----
					label14.setText(bundle.getString("JMuestraDatos.label14.text"));
					label14.setFont(new Font("Tahoma", Font.BOLD, 14));
					panel2.add(label14, cc.xy(9, 11));

					//---- preferredAntenaLabel ----
					preferredAntenaLabel.setText(bundle.getString("JMuestraDatos.preferredAntenaLabel.text"));
					panel2.add(preferredAntenaLabel, cc.xy(11, 11));

					//---- label6 ----
					label6.setText(bundle.getString("JMuestraDatos.label6.text"));
					label6.setFont(new Font("Tahoma", Font.BOLD, 14));
					panel2.add(label6, cc.xy(3, 13));

					//---- readPowerLabel ----
					readPowerLabel.setText(bundle.getString("JMuestraDatos.readPowerLabel.text"));
					panel2.add(readPowerLabel, cc.xy(5, 13));

					//---- label7 ----
					label7.setText(bundle.getString("JMuestraDatos.label7.text"));
					label7.setFont(new Font("Tahoma", Font.BOLD, 14));
					panel2.add(label7, cc.xy(3, 15));

					//---- writePowerLevel ----
					writePowerLevel.setText(bundle.getString("JMuestraDatos.writePowerLevel.text"));
					panel2.add(writePowerLevel, cc.xy(5, 15));

					//---- usbConnectButton ----
					usbConnectButton.setText(bundle.getString("JMuestraDatos.usbConnectButton.text"));
					usbConnectButton.setFont(new Font("Tahoma", Font.BOLD, 14));
					usbConnectButton.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							usbConnectButtonActionPerformed(e);
						}
					});
					panel2.add(usbConnectButton, cc.xywh(3, 17, 3, 1));

					//---- boxConnectButton ----
					boxConnectButton.setText(bundle.getString("JMuestraDatos.boxConnectButton.text"));
					boxConnectButton.setFont(new Font("Tahoma", Font.BOLD, 14));
					boxConnectButton.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							boxConnectButtonActionPerformed(e);
						}
					});
					panel2.add(boxConnectButton, cc.xywh(9, 17, 3, 1));
				}
				tabbedPane1.addTab(bundle.getString("JMuestraDatos.panel2.tab.title"), panel2);

				//======== panel1 ========
				{
					panel1.setLayout(new FormLayout(
						new ColumnSpec[] {
							new ColumnSpec(Sizes.dluX(35)),
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
							new RowSpec(Sizes.dluY(15)),
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

					//---- verifyDataButton ----
					verifyDataButton.setText(bundle.getString("JMuestraDatos.verifyDataButton.text"));
					verifyDataButton.setIcon(new ImageIcon(getClass().getResource("/com/tiempometa/resources/check_64.png")));
					verifyDataButton.setHorizontalAlignment(SwingConstants.LEFT);
					verifyDataButton.setRolloverIcon(null);
					verifyDataButton.setPressedIcon(null);
					verifyDataButton.setFont(new Font("Tahoma", Font.BOLD, 16));
					verifyDataButton.setEnabled(false);
					verifyDataButton.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							verifyDataButtonActionPerformed(e);
						}
					});
					panel1.add(verifyDataButton, cc.xywh(3, 3, 3, 1));

					//---- loadReadingsButton ----
					loadReadingsButton.setText(bundle.getString("JMuestraDatos.loadReadingsButton.text"));
					loadReadingsButton.setIcon(new ImageIcon(getClass().getResource("/com/tiempometa/resources/load_64.png")));
					loadReadingsButton.setHorizontalAlignment(SwingConstants.LEFT);
					loadReadingsButton.setFont(new Font("Tahoma", Font.BOLD, 16));
					loadReadingsButton.setEnabled(false);
					loadReadingsButton.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							loadReadingsButtonActionPerformed(e);
						}
					});
					panel1.add(loadReadingsButton, cc.xywh(7, 3, 3, 1));

					//---- readTagButton ----
					readTagButton.setText(bundle.getString("JMuestraDatos.readTagButton.text"));
					readTagButton.setIcon(new ImageIcon(getClass().getResource("/com/tiempometa/resources/scan_64.png")));
					readTagButton.setHorizontalAlignment(SwingConstants.LEFT);
					readTagButton.setFont(new Font("Tahoma", Font.BOLD, 16));
					readTagButton.setEnabled(false);
					readTagButton.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							readTagButtonActionPerformed(e);
						}
					});
					panel1.add(readTagButton, cc.xywh(3, 5, 3, 1));

					//---- countTagsButton ----
					countTagsButton.setText(bundle.getString("JMuestraDatos.countTagsButton.text"));
					countTagsButton.setIcon(new ImageIcon(getClass().getResource("/com/tiempometa/resources/counter_64.png")));
					countTagsButton.setHorizontalAlignment(SwingConstants.LEFT);
					countTagsButton.setFont(new Font("Tahoma", Font.BOLD, 16));
					countTagsButton.setEnabled(false);
					countTagsButton.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							countTagsButtonActionPerformed(e);
						}
					});
					panel1.add(countTagsButton, cc.xywh(7, 5, 3, 1));

					//---- programTagButton ----
					programTagButton.setText(bundle.getString("JMuestraDatos.programTagButton.text"));
					programTagButton.setIcon(new ImageIcon(getClass().getResource("/com/tiempometa/resources/record_64.png")));
					programTagButton.setHorizontalAlignment(SwingConstants.LEFT);
					programTagButton.setFont(new Font("Tahoma", Font.BOLD, 16));
					programTagButton.setEnabled(false);
					programTagButton.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							programTagButtonActionPerformed(e);
						}
					});
					panel1.add(programTagButton, cc.xywh(3, 7, 3, 1));
				}
				tabbedPane1.addTab(bundle.getString("JMuestraDatos.panel1.tab.title"), panel1);

				//======== panel8 ========
				{
					panel8.setLayout(new FormLayout(
						new ColumnSpec[] {
							new ColumnSpec(Sizes.dluX(35)),
							FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
							new ColumnSpec(Sizes.dluX(160)),
							FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
							new ColumnSpec(Sizes.dluX(160))
						},
						new RowSpec[] {
							new RowSpec(Sizes.dluY(15)),
							FormFactory.LINE_GAP_ROWSPEC,
							FormFactory.DEFAULT_ROWSPEC,
							FormFactory.LINE_GAP_ROWSPEC,
							FormFactory.DEFAULT_ROWSPEC,
							FormFactory.LINE_GAP_ROWSPEC,
							FormFactory.DEFAULT_ROWSPEC,
							FormFactory.LINE_GAP_ROWSPEC,
							FormFactory.DEFAULT_ROWSPEC
						}));

					//---- setBoxTimeButton ----
					setBoxTimeButton.setText(bundle.getString("JMuestraDatos.setBoxTimeButton.text"));
					setBoxTimeButton.setIcon(new ImageIcon(getClass().getResource("/com/tiempometa/resources/set_time_64.png")));
					setBoxTimeButton.setHorizontalAlignment(SwingConstants.LEFT);
					setBoxTimeButton.setFont(new Font("Tahoma", Font.BOLD, 16));
					setBoxTimeButton.setEnabled(false);
					setBoxTimeButton.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							setBoxTimeButtonActionPerformed(e);
						}
					});
					panel8.add(setBoxTimeButton, cc.xy(3, 3));

					//---- getBoxTimeButton ----
					getBoxTimeButton.setText(bundle.getString("JMuestraDatos.getBoxTimeButton.text"));
					getBoxTimeButton.setIcon(new ImageIcon(getClass().getResource("/com/tiempometa/resources/get_time_64.png")));
					getBoxTimeButton.setHorizontalAlignment(SwingConstants.LEFT);
					getBoxTimeButton.setFont(new Font("Tahoma", Font.BOLD, 16));
					getBoxTimeButton.setEnabled(false);
					getBoxTimeButton.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							getBoxTimeButtonActionPerformed(e);
						}
					});
					panel8.add(getBoxTimeButton, cc.xy(5, 3));

					//---- label20 ----
					label20.setText(bundle.getString("JMuestraDatos.label20.text"));
					label20.setFont(new Font("Tahoma", Font.BOLD, 14));
					panel8.add(label20, cc.xy(3, 5));

					//---- label19 ----
					label19.setText(bundle.getString("JMuestraDatos.label19.text"));
					label19.setFont(new Font("Tahoma", Font.BOLD, 14));
					panel8.add(label19, cc.xy(5, 5));
					panel8.add(systemTimeLabel, cc.xy(3, 7));
					panel8.add(foxberryTimeLabel, cc.xy(5, 7));
					panel8.add(foxberryTimeDiffLabel, cc.xy(5, 9));
				}
				tabbedPane1.addTab(bundle.getString("JMuestraDatos.panel8.tab.title"), panel8);

				//======== panel9 ========
				{
					panel9.setLayout(new FormLayout(
						new ColumnSpec[] {
							new ColumnSpec(Sizes.dluX(10)),
							FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
							FormFactory.DEFAULT_COLSPEC,
							FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
							new ColumnSpec(Sizes.dluX(284))
						},
						new RowSpec[] {
							new RowSpec(Sizes.dluY(10)),
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

					//---- label18 ----
					label18.setText(bundle.getString("JMuestraDatos.label18.text"));
					label18.setFont(new Font("Tahoma", Font.BOLD, 14));
					panel9.add(label18, cc.xywh(3, 3, 3, 1));

					//---- label12 ----
					label12.setText(bundle.getString("JMuestraDatos.label12.text"));
					panel9.add(label12, cc.xy(3, 5));
					panel9.add(tiempoMetaApiKey, cc.xy(5, 5));

					//---- downloadButton ----
					downloadButton.setText(bundle.getString("JMuestraDatos.downloadButton.text"));
					downloadButton.setBackground(Color.red);
					downloadButton.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							downloadButtonActionPerformed(e);
						}
					});
					panel9.add(downloadButton, cc.xy(5, 7));
					panel9.add(separator4, cc.xywh(3, 9, 3, 1));

					//---- label22 ----
					label22.setText(bundle.getString("JMuestraDatos.label22.text"));
					panel9.add(label22, cc.xy(3, 11));
					panel9.add(lastRequestLabel, cc.xy(5, 11));

					//---- label15 ----
					label15.setText(bundle.getString("JMuestraDatos.label15.text"));
					panel9.add(label15, cc.xy(3, 13));
					panel9.add(lastDownloadLabel, cc.xy(5, 13));

					//---- label17 ----
					label17.setText(bundle.getString("JMuestraDatos.label17.text"));
					panel9.add(label17, cc.xy(3, 15));
					panel9.add(downloadCountLabel, cc.xy(5, 15));
					panel9.add(separator3, cc.xywh(3, 17, 3, 1));

					//---- label24 ----
					label24.setText(bundle.getString("JMuestraDatos.label24.text"));
					label24.setFont(new Font("Tahoma", Font.BOLD, 14));
					panel9.add(label24, cc.xywh(3, 19, 3, 1));

					//======== scrollPane1 ========
					{
						scrollPane1.setViewportView(readingLogTable);
					}
					panel9.add(scrollPane1, cc.xy(5, 21));
				}
				tabbedPane1.addTab(bundle.getString("JMuestraDatos.panel9.tab.title"), panel9);
			}
			panel4.add(tabbedPane1, cc.xy(1, 1));
		}
		contentPane.add(panel4, BorderLayout.CENTER);

		//======== panel6 ========
		{
			panel6.setLayout(new FormLayout(
				new ColumnSpec[] {
					new ColumnSpec(Sizes.dluX(17)),
					FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
					FormFactory.DEFAULT_COLSPEC,
					FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
					FormFactory.DEFAULT_COLSPEC,
					FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
					new ColumnSpec(Sizes.dluX(65)),
					FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
					FormFactory.DEFAULT_COLSPEC,
					FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
					new ColumnSpec(Sizes.dluX(65)),
					FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
					new ColumnSpec(Sizes.dluX(199))
				},
				new RowSpec[] {
					FormFactory.DEFAULT_ROWSPEC,
					FormFactory.LINE_GAP_ROWSPEC,
					FormFactory.DEFAULT_ROWSPEC,
					FormFactory.LINE_GAP_ROWSPEC,
					FormFactory.DEFAULT_ROWSPEC,
					FormFactory.LINE_GAP_ROWSPEC,
					new RowSpec(Sizes.dluY(10))
				}));

			//---- label21 ----
			label21.setText(bundle.getString("JMuestraDatos.label21.text"));
			label21.setFont(new Font("Tahoma", Font.BOLD, 16));
			panel6.add(label21, cc.xy(3, 1));

			//---- label1 ----
			label1.setText(bundle.getString("JMuestraDatos.label1.text"));
			label1.setFont(new Font("Tahoma", Font.PLAIN, 14));
			panel6.add(label1, cc.xy(5, 1));

			//---- usbStatusLabel ----
			usbStatusLabel.setText(bundle.getString("JMuestraDatos.usbStatusLabel.text"));
			usbStatusLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
			usbStatusLabel.setForeground(Color.red);
			panel6.add(usbStatusLabel, cc.xy(7, 1));

			//---- label16 ----
			label16.setText(bundle.getString("JMuestraDatos.label16.text"));
			label16.setFont(new Font("Tahoma", Font.PLAIN, 14));
			panel6.add(label16, cc.xy(9, 1));

			//---- tcpStatusLabel ----
			tcpStatusLabel.setText(bundle.getString("JMuestraDatos.tcpStatusLabel.text"));
			tcpStatusLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
			tcpStatusLabel.setForeground(Color.red);
			panel6.add(tcpStatusLabel, cc.xy(11, 1));

			//---- label8 ----
			label8.setText(bundle.getString("JMuestraDatos.label8.text"));
			label8.setFont(new Font("Tahoma", Font.BOLD, 16));
			panel6.add(label8, cc.xy(3, 3));

			//---- databaseLabel ----
			databaseLabel.setText(bundle.getString("JMuestraDatos.databaseLabel.text"));
			panel6.add(databaseLabel, cc.xywh(3, 5, 11, 1));
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
		setSize(740, 620);
		setLocationRelativeTo(getOwner());
		// //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY
	// //GEN-BEGIN:variables
	private JMenuBar menuBar1;
	private JMenu menu1;
	private JMenuItem configMenuItem;
	private JMenuItem createRfidCodesMenuItem;
	private JMenuItem importTagsMenuItem;
	private JMenuItem exportTagsMenuItem;
	private JMenuItem clearTagsMenuItem;
	private JMenuItem importCSVReadingsMenuItem;
	private JMenuItem exitMenuItem;
	private JMenu menu3;
	private JMenuItem aboutUsMenuItem;
	private JPanel panel5;
	private JPanel panel3;
	private JPanel panel4;
	private JTabbedPane tabbedPane1;
	private JPanel panel2;
	private JLabel label9;
	private JLabel label10;
	private JLabel label3;
	private JLabel readerPortLabel;
	private JLabel label11;
	private JLabel boxIpAddressLabel;
	private JLabel label4;
	private JLabel readerStatusLabel;
	private JLabel label2;
	private JLabel rssiLevelLabel;
	private JLabel label13;
	private JLabel preferredReaderLabel;
	private JLabel label5;
	private JLabel regionLabel;
	private JLabel label14;
	private JLabel preferredAntenaLabel;
	private JLabel label6;
	private JLabel readPowerLabel;
	private JLabel label7;
	private JLabel writePowerLevel;
	private JButton usbConnectButton;
	private JButton boxConnectButton;
	private JPanel panel1;
	private JButton verifyDataButton;
	private JButton loadReadingsButton;
	private JButton readTagButton;
	private JButton countTagsButton;
	private JButton programTagButton;
	private JPanel panel8;
	private JButton setBoxTimeButton;
	private JButton getBoxTimeButton;
	private JLabel label20;
	private JLabel label19;
	private JLabel systemTimeLabel;
	private JLabel foxberryTimeLabel;
	private JLabel foxberryTimeDiffLabel;
	private JPanel panel9;
	private JLabel label18;
	private JLabel label12;
	private JTextField tiempoMetaApiKey;
	private JButton downloadButton;
	private JSeparator separator4;
	private JLabel label22;
	private JLabel lastRequestLabel;
	private JLabel label15;
	private JLabel lastDownloadLabel;
	private JLabel label17;
	private JLabel downloadCountLabel;
	private JSeparator separator3;
	private JLabel label24;
	private JScrollPane scrollPane1;
	private JTable readingLogTable;
	private JPanel panel6;
	private JLabel label21;
	private JLabel label1;
	private JLabel usbStatusLabel;
	private JLabel label16;
	private JLabel tcpStatusLabel;
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
		for (TagReading tagReading : readings) {
			if (tagReading.getReadingType().equals(
					TagReading.TYPE_COMMAND_RESPONSE)) {
				foxberryTimeLabel.setText(dateFormat.format(tagReading
						.getTime()));
				foxberryTimeDiffLabel.setText("Diferencia : "
						+ tagReading.getEpc() + " ms");
			}
		}
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

	private void enableUsbFunctions() {
		verifyDataButton.setEnabled(true);
		programTagButton.setEnabled(true);
		readTagButton.setEnabled(true);
	}

	private void disableUsbFunctions() {
		if (!ReaderContext.isFoxberryConnected()) {
			verifyDataButton.setEnabled(false);
		}
		programTagButton.setEnabled(false);
		readTagButton.setEnabled(false);
	}

	private void enableTcpFunctions() {
		verifyDataButton.setEnabled(true);
		loadReadingsButton.setEnabled(true);
		countTagsButton.setEnabled(true);
		getBoxTimeButton.setEnabled(true);
		setBoxTimeButton.setEnabled(true);
	}

	private void disableTcpFunctions() {
		if (!ReaderContext.isUsbConnected()) {
			verifyDataButton.setEnabled(false);
		}
		loadReadingsButton.setEnabled(false);
		countTagsButton.setEnabled(false);
		getBoxTimeButton.setEnabled(false);
		setBoxTimeButton.setEnabled(false);
	}

	@Override
	public void usbConnected() {
		usbStatusLabel.setText("Conectado");
		readerStatusLabel.setText("No leyendo");
		enableUsbFunctions();
	}

	@Override
	public void usbDisconnected() {
		usbStatusLabel.setText("Desconectado");
		readerStatusLabel.setText("Desconectado");
		disableUsbFunctions();
	}

	@Override
	public void usbStartedReading() {
		usbStatusLabel.setText("Leyendo");
	}

	@Override
	public void usbStoppedReading() {
		usbStatusLabel.setText("No leyendo");
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
		tcpStatusLabel.setText("Conectada");
		enableTcpFunctions();
	}

	@Override
	public void tcpDisconnected() {
		tcpStatusLabel.setText("Desconectada");
		disableTcpFunctions();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Window#dispose()
	 */
	@Override
	public void dispose() {
		systemTime.stop();
		super.dispose();
	}

	@Override
	public void notifyDownload(final Integer downloadCount,
			final Date latestDownload, final List<TagReadingLog> readingLog) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				lastDownloadLabel.setText(dateFormat.format(latestDownload));
				downloadCountLabel.setText(downloadCount.toString());
				downloadReadingsTableModel.setData(readingLog);
				downloadReadingsTableModel.fireTableDataChanged();
			}
		});

	}

	@Override
	public void notifyDataRequest(Date date) {
		lastRequestLabel.setText(dateFormat.format(date));
		
	}
}
