/*
 * Created by JFormDesigner on Mon Jul 06 11:03:32 CDT 2015
 */

package com.tiempometa.muestradatos;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.*;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import org.apache.log4j.Logger;

import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import com.tiempometa.timing.dao.ChipReadRawDao;
import com.tiempometa.timing.dao.ParticipantsDao;
import com.tiempometa.timing.dao.RegistrationDao;
import com.tiempometa.timing.dao.RfidDao;
import com.tiempometa.timing.dao.access.ChipReadRawDaoImpl;
import com.tiempometa.timing.dao.access.ParticipantsDaoImpl;
import com.tiempometa.timing.dao.access.RegistrationDaoImpl;
import com.tiempometa.timing.dao.access.RfidDaoImpl;
import com.tiempometa.timing.models.ChipReadRaw;
import com.tiempometa.timing.models.Participants;
import com.tiempometa.timing.models.Registration;
import com.tiempometa.timing.models.Rfid;

/**
 * @author Gerardo Esteban Tasistro Giubetic
 */
public class JLoadTimeReadings extends JDialog implements TagReadListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8179757122215063343L;
	private static final Logger logger = Logger
			.getLogger(JLoadTimeReadings.class);
	private Thread watchdogThread = null;
	private Watchdog watchdog = new Watchdog();
	private String checkPoint = null;
	private SimpleDateFormat dateFormat = new SimpleDateFormat(
			"HH:mm:ss dd/MM/yyyy");
	private SimpleDateFormat hourFormat = new SimpleDateFormat("HH:mm:ss");
	private ReadFilter filter;
	private boolean downloading = false;
	private String loadName;
	private TimeReadingTableModel tableModel = new TimeReadingTableModel();
	private Map<String, Rfid> rfidMap = new HashMap<String, Rfid>();
	private Map<Integer, Registration> registrationMap = new HashMap<Integer, Registration>();
	private Map<Integer, Participants> participantMap = new HashMap<Integer, Participants>();
	private ParticipantsDao participantDao = new ParticipantsDaoImpl();
	private RegistrationDao registrationDao = new RegistrationDaoImpl();
	private RfidDao rfidDao = new RfidDaoImpl();

	class Watchdog implements Runnable {
		private Boolean warn = false;
		private Boolean runMe = true;
		private Integer counter = 0;

		private void playWarning() throws JavaLayerException {
			InputStream mp3Stream = this.getClass().getResourceAsStream(
					"/com/tiempometa/resources/keepalive.mp3");
			Player player = new Player(mp3Stream);
			player.play();
		}

		public void resetCount() {
			logger.debug("Clearing watchdog counter");
			synchronized (this) {
				counter = 0;
			}
		}

		@Override
		public void run() {
			while (runMe) {
				synchronized (this) {
					if (counter > 12) {
						warn = true;
					} else {
						warn = false;
					}
				}
				if (warn) {
					try {
						playWarning();
						Thread.sleep(2000);
					} catch (JavaLayerException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				try {
					Thread.sleep(1000);
					synchronized (this) {
						counter = counter + 1;
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			logger.info("Watchdog has stopped");
		}

		public void stop() {
			runMe = false;

		}
	}

	public JLoadTimeReadings() {
		initComponents();
		ReaderContext.addReadingListener(this);
		watchdogThread = new Thread(watchdog);
		watchdogThread.start();
		DefaultComboBoxModel<ReadFilter> filters = new DefaultComboBoxModel<ReadFilter>();
		filters.addElement(new NoReadFilter());
		filters.addElement(new BestReadFilter());
		filters.addElement(new FirstReadFilter());
		filters.addElement(new LastReadFilter());
		filterComboBox.setModel(filters);
		filter = (ReadFilter) filterComboBox.getSelectedItem();
		filterWindowTextField.setText(Float
				.valueOf(
						ReaderContext.getSettings().getFilterWindow()
								.floatValue() / 1000).toString());
		readingsTable.setModel(tableModel);
		readingsTable.setAutoCreateRowSorter(true);

	}

	private void rewindButtonActionPerformed(ActionEvent e) {
		int response = JOptionPane
				.showConfirmDialog(
						this,
						"Esto descargará todas las lecturas como del punto actual. Puede cargar el sistema. ¿Seguro que deseas continuar?",
						"Confirmar la descarga", JOptionPane.YES_NO_OPTION);
		if (response == JOptionPane.YES_OPTION) {
			logger.info("Downloading all readings");
			try {
				ReaderContext.rewindFoxberry();
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(this,
						"No se pudo recargar las lecturas. " + e1.getMessage(),
						"Error de conexión", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private void applyCheckpointButtonActionPerformed(ActionEvent e) {
		String checkPointValue = checkpointTextField.getText();
		if ((checkPointValue.length() > 0)
				&& (checkPointValue.matches("[a-zA-Z0-9]*"))) {
			checkPoint = checkPointValue;
			checkpointTextField.setBackground(Color.WHITE);
			downloadButton.setEnabled(true);
			filter.setCheckPoint(checkPoint);
			labelReadingsButton.setEnabled(true);
		} else {
			JOptionPane.showMessageDialog(this,
					"El formato del nombre del punto es inválido.",
					"Error de formato", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void clearReadingsButtonActionPerformed(ActionEvent e) {
		int response = JOptionPane
				.showConfirmDialog(
						this,
						"Esto borrará todas las lecturas del lector. ¿Seguro que deseas continuar?",
						"Confirmar la borrado", JOptionPane.YES_NO_OPTION);
		if (response == JOptionPane.YES_OPTION) {
			logger.info("Clearing all readings");
			try {
				ReaderContext.clearFoxberry();
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(this,
						"No se pudo borrar las lecturas. " + e1.getMessage(),
						"Error de conexión", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private void checkpointTextFieldKeyTyped(KeyEvent e) {
		checkpointTextField.setBackground(Color.YELLOW);
		// downloadButton.setEnabled(false);
	}

	private void loadCatalogue() throws SQLException {
		List<Participants> participants = participantDao.fetchAll();
		for (Participants participant : participants) {
			participantMap.put(participant.getId(), participant);
		}
		List<Rfid> rfids = rfidDao.findAll();
		for (Rfid rfid : rfids) {
			rfidMap.put(rfid.getRfid().toUpperCase(), rfid);
		}
		List<Registration> registrations = registrationDao.findAll();
		for (Registration registration : registrations) {
			registrationMap.put(registration.getChipNumber(), registration);
		}
		logger.info("Rfid catalogue size " + rfidMap.size());
		logger.info("Registration catalogue size " + registrationMap.size());
		logger.info("Participant catalogue size " + participantMap.size());
	}

	private void downloadButtonActionPerformed(ActionEvent e) {
		if (downloading) {
			filter.stop();
			try {
				Thread.sleep(500);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			downloadButton.setText("Iniciar Descarga");
			filterComboBox.setEnabled(true);
			rewindButton.setEnabled(true);
			downloading = false;
		} else {
			try {
				Float timeWindow = Float.valueOf(filterWindowTextField
						.getText()) + 1000;
				filter.initialize(timeWindow.longValue(), checkPoint, loadName);
			} catch (NumberFormatException e1) {
				JOptionPane.showMessageDialog(this,
						"La ventana de tiempo debe ser numérica",
						"Error de datos", JOptionPane.ERROR_MESSAGE);
				return;
			}
			try {
				logger.info("Loading catalogue");
				loadCatalogue();
				logger.info("Success loading catalogue");
				filterComboBox.setEnabled(false);
				rewindButton.setEnabled(false);
				Thread filterThread = new Thread(filter);
				filterThread.start();
				downloadButton.setText("Detener Descarga");
				downloading = true;
			} catch (SQLException e1) {
				JOptionPane.showMessageDialog(this, "Error de base de datos "
						+ e1.getMessage(), "Error de datos",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
		}
	}

	private void closeButtonActionPerformed(ActionEvent e) {
		if (downloading) {
			JOptionPane.showMessageDialog(this,
					"Debes detener la descarga antes de cerrar.",
					"Descarga activa", JOptionPane.ERROR_MESSAGE);
			return;
		}
		dispose();
	}

	private void filterComboBoxItemStateChanged(ItemEvent e) {
		if (e.getStateChange() == ItemEvent.SELECTED) {
			filter = (ReadFilter) filterComboBox.getSelectedItem();
		}
	}

	private void clearListButtonActionPerformed(ActionEvent e) {
		int response = JOptionPane.showConfirmDialog(this,
				"Se borrará la lista de lecturas actuales. No se afectarán las lecturas en la caja ni las ya descargadas",
				"Confirmar vaciado", JOptionPane.YES_NO_OPTION);
		if (response == JOptionPane.YES_OPTION) {
			tableModel.clearReadings();
			clearReadingDisplay();
		}
	}

	private void clearReadingDisplay() {
		totalReadingsLabel.setText("");
		lastReadingLabel.setText("");
	}

	private void labelReadingsButtonActionPerformed(ActionEvent e) {
		int response = JOptionPane.showConfirmDialog(this,
				"Esto marcará todas las lecturas no etiquetadas con la etiqueta '"
						+ checkPoint + "'. ¿Seguro que deseas continuar?",
				"Marcar lecturas", JOptionPane.YES_NO_OPTION);
		if (response == JOptionPane.YES_OPTION) {
			logger.info("Labeling readings");
			try {
				ReaderContext.labelReadings(checkPoint);
			} catch (IOException e1) {
				JOptionPane
						.showMessageDialog(
								this,
								"No se pudo etiquetar las lecturas. "
										+ e1.getMessage(), "Error de conexión",
								JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY
		// //GEN-BEGIN:initComponents
		ResourceBundle bundle = ResourceBundle
				.getBundle("com.tiempometa.muestradatos.muestradatos");
		dialogPane = new JPanel();
		contentPanel = new JPanel();
		label10 = new JLabel();
		separator1 = new JSeparator();
		label1 = new JLabel();
		checkpointTextField = new JTextField();
		applyCheckpointButton = new JButton();
		label7 = new JLabel();
		separator2 = new JSeparator();
		label2 = new JLabel();
		filterWindowTextField = new JTextField();
		label3 = new JLabel();
		label4 = new JLabel();
		filterComboBox = new JComboBox();
		downloadButton = new JButton();
		label5 = new JLabel();
		totalReadingsLabel = new JLabel();
		label6 = new JLabel();
		lastReadingLabel = new JLabel();
		scrollPane1 = new JScrollPane();
		readingsTable = new JTable();
		labelReadingsButton = new JButton();
		clearListButton = new JButton();
		clearReadingsButton = new JButton();
		rewindButton = new JButton();
		buttonBar = new JPanel();
		closeButton = new JButton();
		CellConstraints cc = new CellConstraints();

		// ======== this ========
		setIconImage(new ImageIcon(getClass().getResource(
				"/com/tiempometa/resources/tiempometa_icon_large_alpha.png"))
				.getImage());
		setTitle(bundle.getString("JLoadTimeReadings.this.title"));
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
						new ColumnSpec(Sizes.dluX(15)),
						FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
						new ColumnSpec(Sizes.dluX(113)),
						FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
						new ColumnSpec(Sizes.dluX(52)),
						FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
						new ColumnSpec(Sizes.dluX(124)) }, new RowSpec[] {
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
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.LINE_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC }));

				// ---- label10 ----
				label10.setText(bundle
						.getString("JLoadTimeReadings.label10.text"));
				label10.setFont(new Font("Tahoma", Font.BOLD, 16));
				contentPanel.add(label10, cc.xy(3, 1));
				contentPanel.add(separator1, cc.xywh(5, 1, 3, 1));

				// ---- label1 ----
				label1.setText(bundle
						.getString("JLoadTimeReadings.label1.text"));
				label1.setFont(new Font("Tahoma", Font.PLAIN, 14));
				contentPanel.add(label1, cc.xy(3, 3));

				// ---- checkpointTextField ----
				checkpointTextField.setFont(new Font("Tahoma", Font.PLAIN, 14));
				checkpointTextField.setBackground(Color.yellow);
				checkpointTextField.addKeyListener(new KeyAdapter() {
					@Override
					public void keyTyped(KeyEvent e) {
						checkpointTextFieldKeyTyped(e);
					}
				});
				contentPanel.add(checkpointTextField, cc.xywh(5, 3, 3, 1));

				// ---- applyCheckpointButton ----
				applyCheckpointButton
						.setText(bundle
								.getString("JLoadTimeReadings.applyCheckpointButton.text"));
				applyCheckpointButton
						.setFont(new Font("Tahoma", Font.PLAIN, 14));
				applyCheckpointButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						applyCheckpointButtonActionPerformed(e);
					}
				});
				contentPanel.add(applyCheckpointButton, cc.xywh(5, 5, 3, 1));

				// ---- label7 ----
				label7.setText(bundle
						.getString("JLoadTimeReadings.label7.text"));
				label7.setFont(new Font("Tahoma", Font.BOLD, 16));
				contentPanel.add(label7, cc.xy(3, 7));
				contentPanel.add(separator2, cc.xywh(5, 7, 3, 1));

				// ---- label2 ----
				label2.setText(bundle
						.getString("JLoadTimeReadings.label2.text"));
				label2.setFont(new Font("Tahoma", Font.PLAIN, 14));
				contentPanel.add(label2, cc.xy(3, 9));
				contentPanel.add(filterWindowTextField, cc.xy(5, 9));

				// ---- label3 ----
				label3.setText(bundle
						.getString("JLoadTimeReadings.label3.text"));
				label3.setFont(new Font("Tahoma", Font.PLAIN, 14));
				contentPanel.add(label3, cc.xy(7, 9));

				// ---- label4 ----
				label4.setText(bundle
						.getString("JLoadTimeReadings.label4.text"));
				label4.setFont(new Font("Tahoma", Font.PLAIN, 14));
				contentPanel.add(label4, cc.xy(3, 11));

				// ---- filterComboBox ----
				filterComboBox.setFont(new Font("Tahoma", Font.PLAIN, 14));
				filterComboBox.addItemListener(new ItemListener() {
					@Override
					public void itemStateChanged(ItemEvent e) {
						filterComboBoxItemStateChanged(e);
					}
				});
				contentPanel.add(filterComboBox, cc.xywh(5, 11, 3, 1));

				// ---- downloadButton ----
				downloadButton.setText(bundle
						.getString("JLoadTimeReadings.downloadButton.text"));
				downloadButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
				downloadButton.setEnabled(false);
				downloadButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						downloadButtonActionPerformed(e);
					}
				});
				contentPanel.add(downloadButton, cc.xywh(5, 13, 3, 1));

				// ---- label5 ----
				label5.setText(bundle
						.getString("JLoadTimeReadings.label5.text"));
				label5.setFont(new Font("Tahoma", Font.PLAIN, 14));
				contentPanel.add(label5, cc.xy(3, 15));
				contentPanel.add(totalReadingsLabel, cc.xywh(5, 15, 3, 1));

				// ---- label6 ----
				label6.setText(bundle
						.getString("JLoadTimeReadings.label6.text"));
				label6.setFont(new Font("Tahoma", Font.PLAIN, 14));
				contentPanel.add(label6, cc.xy(3, 17));
				contentPanel.add(lastReadingLabel, cc.xywh(5, 17, 3, 1));

				// ======== scrollPane1 ========
				{
					scrollPane1.setViewportView(readingsTable);
				}
				contentPanel.add(scrollPane1, cc.xywh(3, 19, 5, 1));

				// ---- labelReadingsButton ----
				labelReadingsButton
						.setText(bundle
								.getString("JLoadTimeReadings.labelReadingsButton.text"));
				labelReadingsButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
				labelReadingsButton.setEnabled(false);
				labelReadingsButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						labelReadingsButtonActionPerformed(e);
					}
				});
				contentPanel.add(labelReadingsButton, cc.xy(3, 21));

				// ---- clearListButton ----
				clearListButton.setText(bundle
						.getString("JLoadTimeReadings.clearListButton.text"));
				clearListButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
				clearListButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						clearListButtonActionPerformed(e);
					}
				});
				contentPanel.add(clearListButton, cc.xy(7, 21));

				// ---- clearReadingsButton ----
				clearReadingsButton
						.setText(bundle
								.getString("JLoadTimeReadings.clearReadingsButton.text"));
				clearReadingsButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
				clearReadingsButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						clearReadingsButtonActionPerformed(e);
					}
				});
				contentPanel.add(clearReadingsButton, cc.xy(3, 23));

				// ---- rewindButton ----
				rewindButton.setText(bundle
						.getString("JLoadTimeReadings.rewindButton.text"));
				rewindButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
				rewindButton.setEnabled(false);
				rewindButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						rewindButtonActionPerformed(e);
					}
				});
				contentPanel.add(rewindButton, cc.xy(7, 23));
			}
			dialogPane.add(contentPanel, BorderLayout.CENTER);

			// ======== buttonBar ========
			{
				buttonBar.setBorder(Borders.BUTTON_BAR_GAP_BORDER);
				buttonBar.setLayout(new FormLayout(new ColumnSpec[] {
						FormFactory.GLUE_COLSPEC, FormFactory.BUTTON_COLSPEC },
						RowSpec.decodeSpecs("pref")));

				// ---- closeButton ----
				closeButton.setText("Cerrar");
				closeButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
				closeButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						closeButtonActionPerformed(e);
					}
				});
				buttonBar.add(closeButton, cc.xy(2, 1));
			}
			dialogPane.add(buttonBar, BorderLayout.SOUTH);
		}
		contentPane.add(dialogPane, BorderLayout.CENTER);
		setSize(535, 520);
		setLocationRelativeTo(getOwner());
		// //GEN-END:initComponents
	}

	private void updateTagData(TagReading reading) {
		Rfid rfid = rfidMap.get(reading.getEpc());
		if (rfid == null) {
			logger.warn("No rfid for EPC " + reading.getEpc());
		} else {
			reading.setBib(rfid.getBib());
			Registration registration = registrationMap.get(rfid
					.getChipNumber());
			if (registration == null) {
				logger.warn("No registration for Rfid " + rfid.getChipNumber());
			} else {
				Participants participant = participantMap.get(registration
						.getParticipantId());
				if (participant == null) {
					logger.error("No participant for registration "
							+ registration.getId());
				} else {
					reading.setFirstName(participant.getNombres());
					reading.setLastName(participant.getPaterno());
					reading.setMiddleName(participant.getMaterno());
				}
			}
		}
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY
	// //GEN-BEGIN:variables
	private JPanel dialogPane;
	private JPanel contentPanel;
	private JLabel label10;
	private JSeparator separator1;
	private JLabel label1;
	private JTextField checkpointTextField;
	private JButton applyCheckpointButton;
	private JLabel label7;
	private JSeparator separator2;
	private JLabel label2;
	private JTextField filterWindowTextField;
	private JLabel label3;
	private JLabel label4;
	private JComboBox filterComboBox;
	private JButton downloadButton;
	private JLabel label5;
	private JLabel totalReadingsLabel;
	private JLabel label6;
	private JLabel lastReadingLabel;
	private JScrollPane scrollPane1;
	private JTable readingsTable;
	private JButton labelReadingsButton;
	private JButton clearListButton;
	private JButton clearReadingsButton;
	private JButton rewindButton;
	private JPanel buttonBar;
	private JButton closeButton;

	// JFormDesigner - End of variables declaration //GEN-END:variables
	@Override
	public void handleReadings(List<TagReading> readings) {
		if (readings.size() > 0) {
			for (Iterator<TagReading> iterator = readings.iterator(); iterator
					.hasNext();) {
				final TagReading tagReading = (TagReading) iterator.next();
				if (tagReading.isKeepAlive()) {
					watchdog.resetCount();
				} else {
					// String loadName = null;
					if (!(filter == null)) {
						filter.addReading(tagReading);
						logger.debug("Updating data");
						SwingUtilities.invokeLater(new Runnable() {
							@Override
							public void run() {
								updateTagData(tagReading);
								tableModel.addReading(tagReading);
								totalReadingsLabel.setText(String
										.valueOf(tableModel.getDataSize()));
								lastReadingLabel.setText(hourFormat
										.format(tagReading.getTime())
										+ " - "
										+ tagReading.getBib());
							}
						});
						logger.debug("tag Reading " + tagReading);
					}
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Window#dispose()
	 */
	@Override
	public void dispose() {
		if (logger.isDebugEnabled()) {
			logger.debug("Removing self from tag read listeners");
		}
		ReaderContext.removeReadingListener(this);
		watchdog.stop();
		super.dispose();
	}
}
