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

import javax.swing.*;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import org.apache.log4j.Logger;

import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import com.tiempometa.timing.dao.ChipReadRawDao;
import com.tiempometa.timing.dao.access.ChipReadRawDaoImpl;
import com.tiempometa.timing.models.ChipReadRaw;

/**
 * @author Gerardo Esteban Tasistro Giubetic
 */
public class JLoadTimeReadings extends JDialog implements TagReadListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8179757122215063343L;
	private ChipReadRawDao chipReadRawDao = new ChipReadRawDaoImpl();
	private static final Logger logger = Logger
			.getLogger(JLoadTimeReadings.class);
	private Thread watchdogThread = null;
	private Watchdog watchdog = new Watchdog();
	private String checkPoint = null;
	private SimpleDateFormat dateFormat = new SimpleDateFormat(
			"HH:mm:ss dd/MM/yyyy");

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
		checkPoint = checkpointTextField.getText();
	}

	private void clearReadingsButtonActionPerformed(ActionEvent e) {
		int response = JOptionPane
				.showConfirmDialog(
						this,
						"Esto borrará todas las lecturas del lectorr. ¿Seguro que deseas continuar?",
						"Confirmar la borrado", JOptionPane.YES_NO_OPTION);
		if (response == JOptionPane.YES_OPTION) {
			logger.info("Clearing all readings");
			try {
				ReaderContext.clearFoxberry();
			} catch (IOException e1) {JOptionPane.showMessageDialog(this,
					"No se pudo borrar las lecturas. " + e1.getMessage(),
						"Error de conexión", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY
		// //GEN-BEGIN:initComponents
		ResourceBundle bundle = ResourceBundle.getBundle("com.tiempometa.muestradatos.muestradatos");
		dialogPane = new JPanel();
		contentPanel = new JPanel();
		label10 = new JLabel();
		label1 = new JLabel();
		checkpointTextField = new JTextField();
		applyCheckpointButton = new JButton();
		label2 = new JLabel();
		timeWindowTextField = new JTextField();
		label3 = new JLabel();
		label4 = new JLabel();
		comboBox1 = new JComboBox<>();
		label5 = new JLabel();
		totalReadingsLabel = new JLabel();
		label6 = new JLabel();
		lastReadingLabel = new JLabel();
		rewindButton = new JButton();
		clearReadingsButton = new JButton();
		label9 = new JLabel();
		scrollPane1 = new JScrollPane();
		readingsTable = new JTable();
		buttonBar = new JPanel();
		okButton = new JButton();
		CellConstraints cc = new CellConstraints();

		//======== this ========
		setIconImage(new ImageIcon(getClass().getResource("/com/tiempometa/resources/stopwatch_small.png")).getImage());
		setTitle(bundle.getString("JLoadTimeReadings.this.title"));
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
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
						FormFactory.DEFAULT_COLSPEC,
						FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
						new ColumnSpec(Sizes.dluX(115)),
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
						FormFactory.DEFAULT_ROWSPEC
					}));

				//---- label10 ----
				label10.setText(bundle.getString("JLoadTimeReadings.label10.text"));
				label10.setFont(new Font("Tahoma", Font.PLAIN, 18));
				contentPanel.add(label10, cc.xy(3, 1));

				//---- label1 ----
				label1.setText(bundle.getString("JLoadTimeReadings.label1.text"));
				contentPanel.add(label1, cc.xy(3, 3));
				contentPanel.add(checkpointTextField, cc.xy(5, 3));

				//---- applyCheckpointButton ----
				applyCheckpointButton.setText(bundle.getString("JLoadTimeReadings.applyCheckpointButton.text"));
				applyCheckpointButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						applyCheckpointButtonActionPerformed(e);
					}
				});
				contentPanel.add(applyCheckpointButton, cc.xy(7, 3));

				//---- label2 ----
				label2.setText(bundle.getString("JLoadTimeReadings.label2.text"));
				contentPanel.add(label2, cc.xy(3, 5));
				contentPanel.add(timeWindowTextField, cc.xy(5, 5));

				//---- label3 ----
				label3.setText(bundle.getString("JLoadTimeReadings.label3.text"));
				contentPanel.add(label3, cc.xy(7, 5));

				//---- label4 ----
				label4.setText(bundle.getString("JLoadTimeReadings.label4.text"));
				contentPanel.add(label4, cc.xy(3, 7));

				//---- comboBox1 ----
				comboBox1.setModel(new DefaultComboBoxModel<>(new String[] {
					"Si",
					"No"
				}));
				contentPanel.add(comboBox1, cc.xy(5, 7));

				//---- label5 ----
				label5.setText(bundle.getString("JLoadTimeReadings.label5.text"));
				contentPanel.add(label5, cc.xy(3, 9));
				contentPanel.add(totalReadingsLabel, cc.xy(5, 9));

				//---- label6 ----
				label6.setText(bundle.getString("JLoadTimeReadings.label6.text"));
				contentPanel.add(label6, cc.xy(3, 11));
				contentPanel.add(lastReadingLabel, cc.xy(5, 11));

				//---- rewindButton ----
				rewindButton.setText(bundle.getString("JLoadTimeReadings.rewindButton.text"));
				rewindButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						rewindButtonActionPerformed(e);
					}
				});
				contentPanel.add(rewindButton, cc.xy(5, 13));

				//---- clearReadingsButton ----
				clearReadingsButton.setText(bundle.getString("JLoadTimeReadings.clearReadingsButton.text"));
				clearReadingsButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						clearReadingsButtonActionPerformed(e);
					}
				});
				contentPanel.add(clearReadingsButton, cc.xy(5, 15));

				//---- label9 ----
				label9.setText(bundle.getString("JLoadTimeReadings.label9.text"));
				label9.setFont(new Font("Tahoma", Font.PLAIN, 18));
				contentPanel.add(label9, cc.xy(3, 17));

				//======== scrollPane1 ========
				{
					scrollPane1.setViewportView(readingsTable);
				}
				contentPanel.add(scrollPane1, cc.xywh(3, 19, 5, 1));
			}
			dialogPane.add(contentPanel, BorderLayout.CENTER);

			//======== buttonBar ========
			{
				buttonBar.setBorder(Borders.BUTTON_BAR_GAP_BORDER);
				buttonBar.setLayout(new FormLayout(
					new ColumnSpec[] {
						FormFactory.GLUE_COLSPEC,
						FormFactory.BUTTON_COLSPEC
					},
					RowSpec.decodeSpecs("pref")));

				//---- okButton ----
				okButton.setText("OK");
				buttonBar.add(okButton, cc.xy(2, 1));
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
	private JLabel label10;
	private JLabel label1;
	private JTextField checkpointTextField;
	private JButton applyCheckpointButton;
	private JLabel label2;
	private JTextField timeWindowTextField;
	private JLabel label3;
	private JLabel label4;
	private JComboBox<String> comboBox1;
	private JLabel label5;
	private JLabel totalReadingsLabel;
	private JLabel label6;
	private JLabel lastReadingLabel;
	private JButton rewindButton;
	private JButton clearReadingsButton;
	private JLabel label9;
	private JScrollPane scrollPane1;
	private JTable readingsTable;
	private JPanel buttonBar;
	private JButton okButton;
	// JFormDesigner - End of variables declaration //GEN-END:variables
	@Override
	public void handleReadings(List<TagReading> readings) {
		if (readings.size() > 0) {
			for (Iterator iterator = readings.iterator(); iterator.hasNext();) {
				final TagReading tagReading = (TagReading) iterator.next();
				if (tagReading.isKeepAlive()) {
					watchdog.resetCount();
				} else {
					String loadName = null;
//					public ChipReadRaw(Integer id, String rfid, Date time, Long timeMillis,
//							String phase, String checkPoint, Integer eventId, Integer cooked, Byte filtered, String loadName, Integer chipNumber) {
					ChipReadRaw chipReading = new ChipReadRaw(null,
							tagReading.getEpc(), tagReading.getTime(),
							tagReading.getTimeMillis(), checkPoint, checkPoint, null,
							ChipReadRaw.STATUS_RAW,
							ChipReadRaw.FILTERED_READER, loadName, null);
					if (tagReading.getTime() == null) {
						lastReadingLabel.setText("ND");
					} else {
						try {
							logger.debug("Saving chip reading");
							chipReadRawDao.save(chipReading);
							SwingUtilities.invokeLater(new Runnable() {

								@Override
								public void run() {
									lastReadingLabel.setText(dateFormat
											.format(tagReading.getTime()));

								}
							});
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
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
