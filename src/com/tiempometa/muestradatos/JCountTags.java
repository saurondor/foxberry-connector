/*
 * Created by JFormDesigner on Sat Aug 22 09:24:33 CDT 2015
 */

package com.tiempometa.muestradatos;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.List;

import javax.swing.*;

import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.apache.log4j.Logger;

import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import com.tiempometa.timing.dao.RfidDao;
import com.tiempometa.timing.dao.access.RfidDaoImpl;
import com.tiempometa.timing.models.Rfid;

/**
 * @author Gerardo Esteban Tasistro Giubetic
 */
public class JCountTags extends JDialog implements TagReadListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = -929753077836195071L;
	private static final Logger logger = Logger.getLogger(JCountTags.class);
	private Map<String, TagReading> eventTagMap = new HashMap<String, TagReading>();
	private Map<String, TagReading> nonEventTagMap = new HashMap<String, TagReading>();
	private Map<String, Rfid> totalRfidMap = new HashMap<String, Rfid>();
	private RfidDao rfidDao = new RfidDaoImpl();
	private boolean isCounting = false;
	private File exportFile = null;

	public JCountTags(Frame owner) {
		super(owner);
		initComponents();
		ReaderContext.addReadingListener(this);
		loadDatabaseTags();
		updateCountDisplay(0, 0);
	}

	public JCountTags(Dialog owner) {
		super(owner);
		initComponents();
		ReaderContext.addReadingListener(this);
		loadDatabaseTags();
		updateCountDisplay(0, 0);
	}

	private void clearCountButtonActionPerformed(ActionEvent e) {
		eventTagMap = new HashMap<String, TagReading>();
		nonEventTagMap = new HashMap<String, TagReading>();
		updateCountDisplay(eventTagMap.size(), nonEventTagMap.size());
	}

	private void updateCountDisplay(Integer eventCount, Integer nonEventCount) {
		Integer total = eventCount + nonEventCount;
		eventTagCountLabel.setText(eventCount.toString());
		nonEventTagCountLabel.setText(nonEventCount.toString());
		totalTagCountLabel.setText(total.toString());
	}

	private void loadDatabaseTags() {
		List<Rfid> rfidList;
		try {
			rfidList = rfidDao.findAll();
			for (Rfid rfid : rfidList) {
				totalRfidMap.put(rfid.getRfid().toUpperCase(), rfid);
			}
			JOptionPane.showMessageDialog(this,
					"Se cargaron " + rfidList.size()
							+ " tags de la base de datos",
					"Cantidad de tags disponibles",
					JOptionPane.INFORMATION_MESSAGE);
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(
					this,
					"No se pudo cargar la lista de tags a programar.\n"
							+ e.getMessage(), "Error de base de datos",
					JOptionPane.ERROR_MESSAGE);
		}

	}

	private void closeButtonActionPerformed(ActionEvent e) {
		dispose();
	}

	private void countButtonActionPerformed(ActionEvent e) {
		if (isCounting) {
			isCounting = false;
			countButton.setText("Iniciar Conteo");
		} else {
			isCounting = true;
			countButton.setText("Detener Conteo");
		}
	}

	private void exportCountButtonActionPerformed(ActionEvent e) {
		JFileChooser fc = new JFileChooser(exportFile);
		int response = fc.showOpenDialog(this);
		if (response == JFileChooser.APPROVE_OPTION) {
			exportFile = fc.getSelectedFile();
			if (!exportFile.getAbsolutePath().endsWith(".xls")) {
				exportFile = new File(exportFile.getAbsolutePath() + ".xls");
			}
			if (exportFile.exists()) {
				int overwriteResponse = JOptionPane.showConfirmDialog(this,
						"El archivo ya existe. ¿Desea reemplazarlo?",
						"Archivo ya existe", JOptionPane.WARNING_MESSAGE);
				if (overwriteResponse == JOptionPane.NO_OPTION) {
					return;
				}
			}
			try {
				populateBibs();
				ExportTagsToExcel exporter = new ExportTagsToExcel();
				exporter.open(exportFile);
				Map<String, Map<String, TagReading>> readings = new HashMap<String, Map<String, TagReading>>();
				readings.put("en evento", eventTagMap);
				readings.put("no en evento", nonEventTagMap);
				exporter.export(readings);
				JOptionPane
						.showMessageDialog(this, "Se exportó con exito",
								"Exportación de datos",
								JOptionPane.INFORMATION_MESSAGE);
			} catch (SQLException e1) {
				JOptionPane.showMessageDialog(this,
						"Error de SQL " + e1.getMessage(),
						"Exportación de datos", JOptionPane.ERROR_MESSAGE);
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(this,
						"Error de datos " + e1.getMessage(),
						"Exportación de datos", JOptionPane.ERROR_MESSAGE);
			} catch (RowsExceededException e1) {
				JOptionPane.showMessageDialog(this,
						"Error de Excel " + e1.getMessage(),
						"Exportación de datos", JOptionPane.ERROR_MESSAGE);
			} catch (WriteException e1) {
				JOptionPane.showMessageDialog(this,
						"Error de datos " + e1.getMessage(),
						"Exportación de datos", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private void populateBibs() throws SQLException {
		Set<String> keys = eventTagMap.keySet();
		for (String scanCode : keys) {
			TagReading reading = eventTagMap.get(scanCode);			
			List<Rfid> rfid = rfidDao.findByRfid(reading.getEpc());
			if (rfid.size() > 0) {
				reading.setBib(rfid.get(0).getBib());
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
		label1 = new JLabel();
		eventTagCountLabel = new JLabel();
		label2 = new JLabel();
		nonEventTagCountLabel = new JLabel();
		label3 = new JLabel();
		totalTagCountLabel = new JLabel();
		countButton = new JButton();
		clearCountButton = new JButton();
		exportCountButton = new JButton();
		countTidCheckBox = new JCheckBox();
		buttonBar = new JPanel();
		closeButton = new JButton();
		CellConstraints cc = new CellConstraints();

		// ======== this ========
		setTitle(bundle.getString("JCountTags.this.title"));
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());

		// ======== dialogPane ========
		{
			dialogPane.setBorder(Borders.DIALOG_BORDER);
			dialogPane.setLayout(new BorderLayout());

			// ======== contentPanel ========
			{
				contentPanel.setLayout(new FormLayout(new ColumnSpec[] {
						new ColumnSpec(Sizes.dluX(88)),
						FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
						new ColumnSpec(Sizes.dluX(37)) }, new RowSpec[] {
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
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.LINE_GAP_ROWSPEC,
						new RowSpec(Sizes.dluY(17)),
						FormFactory.LINE_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.LINE_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC }));

				// ---- label1 ----
				label1.setText(bundle.getString("JCountTags.label1.text"));
				label1.setFont(new Font("Tahoma", Font.PLAIN, 14));
				contentPanel.add(label1, cc.xy(1, 3));

				// ---- eventTagCountLabel ----
				eventTagCountLabel.setText(bundle
						.getString("JCountTags.eventTagCountLabel.text"));
				eventTagCountLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
				eventTagCountLabel.setHorizontalAlignment(SwingConstants.RIGHT);
				contentPanel.add(eventTagCountLabel, cc.xy(3, 3));

				// ---- label2 ----
				label2.setText(bundle.getString("JCountTags.label2.text"));
				label2.setFont(new Font("Tahoma", Font.PLAIN, 14));
				contentPanel.add(label2, cc.xy(1, 5));

				// ---- nonEventTagCountLabel ----
				nonEventTagCountLabel.setText(bundle
						.getString("JCountTags.nonEventTagCountLabel.text"));
				nonEventTagCountLabel
						.setFont(new Font("Tahoma", Font.PLAIN, 14));
				nonEventTagCountLabel
						.setHorizontalAlignment(SwingConstants.RIGHT);
				contentPanel.add(nonEventTagCountLabel, cc.xy(3, 5));

				// ---- label3 ----
				label3.setText(bundle.getString("JCountTags.label3.text"));
				label3.setFont(new Font("Tahoma", Font.PLAIN, 14));
				contentPanel.add(label3, cc.xy(1, 7));

				// ---- totalTagCountLabel ----
				totalTagCountLabel.setText(bundle
						.getString("JCountTags.totalTagCountLabel.text"));
				totalTagCountLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
				totalTagCountLabel.setHorizontalAlignment(SwingConstants.RIGHT);
				contentPanel.add(totalTagCountLabel, cc.xy(3, 7));

				// ---- countButton ----
				countButton.setText(bundle
						.getString("JCountTags.countButton.text"));
				countButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
				countButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						countButtonActionPerformed(e);
					}
				});
				contentPanel.add(countButton, cc.xywh(1, 9, 3, 1));

				// ---- clearCountButton ----
				clearCountButton.setText(bundle
						.getString("JCountTags.clearCountButton.text"));
				clearCountButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
				clearCountButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						clearCountButtonActionPerformed(e);
					}
				});
				contentPanel.add(clearCountButton, cc.xywh(1, 11, 3, 1));

				// ---- exportCountButton ----
				exportCountButton.setText(bundle
						.getString("JCountTags.exportCountButton.text"));
				exportCountButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
				exportCountButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						exportCountButtonActionPerformed(e);
					}
				});
				contentPanel.add(exportCountButton, cc.xywh(1, 15, 3, 1));

				// ---- countTidCheckBox ----
				countTidCheckBox.setText(bundle
						.getString("JCountTags.countTidCheckBox.text"));
				contentPanel.add(countTidCheckBox, cc.xy(1, 17));
			}
			dialogPane.add(contentPanel, BorderLayout.NORTH);

			// ======== buttonBar ========
			{
				buttonBar.setBorder(Borders.BUTTON_BAR_GAP_BORDER);
				buttonBar.setLayout(new FormLayout(new ColumnSpec[] {
						FormFactory.GLUE_COLSPEC, FormFactory.BUTTON_COLSPEC },
						RowSpec.decodeSpecs("pref")));

				// ---- closeButton ----
				closeButton.setText("Cerrar");
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
		pack();
		setLocationRelativeTo(getOwner());
		// //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY
	// //GEN-BEGIN:variables
	private JPanel dialogPane;
	private JPanel contentPanel;
	private JLabel label1;
	private JLabel eventTagCountLabel;
	private JLabel label2;
	private JLabel nonEventTagCountLabel;
	private JLabel label3;
	private JLabel totalTagCountLabel;
	private JButton countButton;
	private JButton clearCountButton;
	private JButton exportCountButton;
	private JCheckBox countTidCheckBox;
	private JPanel buttonBar;
	private JButton closeButton;

	// JFormDesigner - End of variables declaration //GEN-END:variables
	@Override
	public void handleReadings(List<TagReading> readings) {
		if (isCounting) {
			for (TagReading tagReading : readings) {
				if (!tagReading.isKeepAlive()) {
					String scanCode = null;
					if (countTidCheckBox.isSelected()) {
						scanCode = tagReading.getTid();
					} else {
						scanCode = tagReading.getEpc();
					}
					Rfid rfid = totalRfidMap.get(tagReading.getEpc());
					if (rfid == null) {
						TagReading reading = nonEventTagMap.get(scanCode);
						if (reading == null) {
							nonEventTagMap.put(scanCode, tagReading);
							updateCountDisplay(eventTagMap.size(),
									nonEventTagMap.size());
						}
					} else {
						TagReading reading = eventTagMap.get(scanCode);
						if (reading == null) {
							eventTagMap.put(scanCode, tagReading);
							updateCountDisplay(eventTagMap.size(),
									nonEventTagMap.size());
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
		super.dispose();
	}
}
