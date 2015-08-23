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
	private ReadFilter filter;

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
		checkpointTextField.setBackground(Color.WHITE);
		downloadButton.setEnabled(true);
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

	private void downloadButtonActionPerformed(ActionEvent e) {
		filter = new BestReadFilter();
		filter.initialize(5000l);
		Thread filterThread = new Thread(filter);
		filterThread.start();
	}

	private void closeButtonActionPerformed(ActionEvent e) {
		dispose();
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY
		// //GEN-BEGIN:initComponents
		ResourceBundle bundle = ResourceBundle.getBundle("com.tiempometa.muestradatos.muestradatos");
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
		timeWindowTextField = new JTextField();
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
		rewindButton = new JButton();
		clearReadingsButton = new JButton();
		buttonBar = new JPanel();
		closeButton = new JButton();
		CellConstraints cc = new CellConstraints();

		//======== this ========
		setIconImage(new ImageIcon(getClass().getResource("/com/tiempometa/resources/tiempometa_icon_large_alpha.png")).getImage());
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
						new ColumnSpec(Sizes.dluX(15)),
						FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
						FormFactory.DEFAULT_COLSPEC,
						FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
						new ColumnSpec(Sizes.dluX(52)),
						FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
						new ColumnSpec(Sizes.dluX(85))
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
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.LINE_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC
					}));

				//---- label10 ----
				label10.setText(bundle.getString("JLoadTimeReadings.label10.text"));
				label10.setFont(new Font("Tahoma", Font.BOLD, 16));
				contentPanel.add(label10, cc.xy(3, 1));
				contentPanel.add(separator1, cc.xywh(5, 1, 3, 1));

				//---- label1 ----
				label1.setText(bundle.getString("JLoadTimeReadings.label1.text"));
				label1.setFont(new Font("Tahoma", Font.PLAIN, 14));
				contentPanel.add(label1, cc.xy(3, 3));

				//---- checkpointTextField ----
				checkpointTextField.setFont(new Font("Tahoma", Font.PLAIN, 14));
				checkpointTextField.setBackground(Color.yellow);
				checkpointTextField.addKeyListener(new KeyAdapter() {
					@Override
					public void keyTyped(KeyEvent e) {
						checkpointTextFieldKeyTyped(e);
					}
				});
				contentPanel.add(checkpointTextField, cc.xywh(5, 3, 3, 1));

				//---- applyCheckpointButton ----
				applyCheckpointButton.setText(bundle.getString("JLoadTimeReadings.applyCheckpointButton.text"));
				applyCheckpointButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
				applyCheckpointButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						applyCheckpointButtonActionPerformed(e);
					}
				});
				contentPanel.add(applyCheckpointButton, cc.xywh(5, 5, 3, 1));

				//---- label7 ----
				label7.setText(bundle.getString("JLoadTimeReadings.label7.text"));
				label7.setFont(new Font("Tahoma", Font.BOLD, 16));
				contentPanel.add(label7, cc.xy(3, 7));
				contentPanel.add(separator2, cc.xywh(5, 7, 3, 1));

				//---- label2 ----
				label2.setText(bundle.getString("JLoadTimeReadings.label2.text"));
				label2.setFont(new Font("Tahoma", Font.PLAIN, 14));
				contentPanel.add(label2, cc.xy(3, 9));
				contentPanel.add(timeWindowTextField, cc.xy(5, 9));

				//---- label3 ----
				label3.setText(bundle.getString("JLoadTimeReadings.label3.text"));
				label3.setFont(new Font("Tahoma", Font.PLAIN, 14));
				contentPanel.add(label3, cc.xy(7, 9));

				//---- label4 ----
				label4.setText(bundle.getString("JLoadTimeReadings.label4.text"));
				label4.setFont(new Font("Tahoma", Font.PLAIN, 14));
				contentPanel.add(label4, cc.xy(3, 11));

				//---- filterComboBox ----
				filterComboBox.setFont(new Font("Tahoma", Font.PLAIN, 14));
				contentPanel.add(filterComboBox, cc.xywh(5, 11, 3, 1));

				//---- downloadButton ----
				downloadButton.setText(bundle.getString("JLoadTimeReadings.downloadButton.text"));
				downloadButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
				downloadButton.setEnabled(false);
				downloadButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						downloadButtonActionPerformed(e);
					}
				});
				contentPanel.add(downloadButton, cc.xywh(5, 13, 3, 1));

				//---- label5 ----
				label5.setText(bundle.getString("JLoadTimeReadings.label5.text"));
				label5.setFont(new Font("Tahoma", Font.PLAIN, 14));
				contentPanel.add(label5, cc.xy(3, 15));
				contentPanel.add(totalReadingsLabel, cc.xy(5, 15));

				//---- label6 ----
				label6.setText(bundle.getString("JLoadTimeReadings.label6.text"));
				label6.setFont(new Font("Tahoma", Font.PLAIN, 14));
				contentPanel.add(label6, cc.xy(3, 17));
				contentPanel.add(lastReadingLabel, cc.xy(5, 17));

				//======== scrollPane1 ========
				{
					scrollPane1.setViewportView(readingsTable);
				}
				contentPanel.add(scrollPane1, cc.xywh(3, 19, 5, 1));

				//---- rewindButton ----
				rewindButton.setText(bundle.getString("JLoadTimeReadings.rewindButton.text"));
				rewindButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
				rewindButton.setEnabled(false);
				rewindButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						rewindButtonActionPerformed(e);
					}
				});
				contentPanel.add(rewindButton, cc.xywh(5, 21, 3, 1));

				//---- clearReadingsButton ----
				clearReadingsButton.setText(bundle.getString("JLoadTimeReadings.clearReadingsButton.text"));
				clearReadingsButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
				clearReadingsButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						clearReadingsButtonActionPerformed(e);
					}
				});
				contentPanel.add(clearReadingsButton, cc.xywh(5, 23, 3, 1));
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

				//---- closeButton ----
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
		setSize(410, 520);
		setLocationRelativeTo(getOwner());
		// //GEN-END:initComponents
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
	private JTextField timeWindowTextField;
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
	private JButton rewindButton;
	private JButton clearReadingsButton;
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
					String loadName = null;
					if (!(filter == null)) {
						filter.addReading(tagReading);
					}
					try {
						ChipReadRaw chipReading = new ChipReadRaw(null,
								tagReading.getEpc().toLowerCase(),
								tagReading.getTime(),
								tagReading.getTimeMillis() / 1000, checkPoint,
								checkPoint, null, ChipReadRaw.STATUS_RAW,
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
					} catch (NullPointerException e) {
						logger.error("Null pointer e " + e.getMessage());
						logger.error(tagReading);
					}
					// } catch (Null)
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
