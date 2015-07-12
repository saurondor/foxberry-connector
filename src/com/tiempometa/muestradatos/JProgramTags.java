/*
 * Created by JFormDesigner on Sat May 30 17:22:50 CDT 2015
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

import javax.swing.*;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.log4j.Logger;

import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import com.thingmagic.Gen2.LockAction;
import com.thingmagic.ReaderCodeException;
import com.thingmagic.ReaderException;
import com.thingmagic.TagData;
import com.tiempometa.timing.dao.RfidDao;
import com.tiempometa.timing.dao.access.RfidDaoImpl;
import com.tiempometa.timing.models.Rfid;

/**
 * @author Gerardo Tasistro gtasistro@tiempometa.com Copyright 2015 Gerardo
 *         Tasistro Licensed under the Mozilla Public License, v. 2.0
 * 
 */
public class JProgramTags extends JDialog implements TagReadListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = -947822386284311068L;
	private static final Logger logger = Logger.getLogger(JProgramTags.class);
	private RfidDao rfidDao = new RfidDaoImpl();

	private Map<String, Rfid> rfidMap = new HashMap<String, Rfid>();
	private Map<String, Rfid> totalRfidMap = new HashMap<String, Rfid>();
	private TagReadTableDataModel tagTableModel = new TagReadTableDataModel();

	private void programButtonActionPerformed(ActionEvent e) {
		if (ReaderContext.isUsbConnected()) {
			if (ReaderContext.isUsbReading()) {
				ReaderContext.stopReading();
				programButton.setText("Iniciar programación");
			} else {
				try {
					ReaderContext.startReading();
					programButton.setText("Detener programación");
				} catch (ReaderException e1) {
					JOptionPane.showMessageDialog(this,
							"No se pudo iniciar la lectura.",
							"Iniciar lectura", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}

	public JProgramTags(Frame owner, boolean modal) {
		super(owner, modal);
		initComponents();
		logger.debug("Adding self to tag read listeners");
		ReaderContext.addReadingListener(this);
		tagReadTable.setModel(tagTableModel);
		tagReadTable.setAutoCreateRowSorter(true);
		TableRowSorter<TableModel> sorter = new TableRowSorter<>(
				tagReadTable.getModel());
		sorter.setComparator(0, new BibComparator());
		tagReadTable.setRowSorter(sorter);
		List<Rfid> rfidList;
		try {
			rfidList = rfidDao.findAll();
			for (Rfid rfid : rfidList) {
				totalRfidMap.put(rfid.getRfidString().toUpperCase(), rfid);
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
		if (ReaderContext.isUsbConnected()) {
			if (ReaderContext.isUsbReading()) {
				JOptionPane.showMessageDialog(this,
						"Se debe detener la programación primero", "Alerta",
						JOptionPane.WARNING_MESSAGE);
				return;
			}
		}
		this.dispose();
	}

	private void checkBox2ItemStateChanged(ItemEvent e) {
		if (e.getStateChange() == ItemEvent.SELECTED) {
			accessPasswordTextField.setEnabled(true);
			killPasswordTextField.setEnabled(true);
		} else if (e.getStateChange() == ItemEvent.DESELECTED) {
			accessPasswordTextField.setEnabled(false);
			killPasswordTextField.setEnabled(false);
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
		statusLabel = new JLabel();
		nextChipnumberTextField = new JTextField();
		programButton = new JButton();
		lockCheckbox = new JCheckBox();
		bibLabel = new JLabel();
		label2 = new JLabel();
		accessPasswordTextField = new JTextField();
		label3 = new JLabel();
		tidTextField = new JTextField();
		label6 = new JLabel();
		killPasswordTextField = new JTextField();
		label4 = new JLabel();
		epcTextField = new JTextField();
		checkBox1 = new JCheckBox();
		label5 = new JLabel();
		programmedEpcTextField = new JTextField();
		scrollPane1 = new JScrollPane();
		tagReadTable = new JTable();
		buttonBar = new JPanel();
		closeButton = new JButton();
		CellConstraints cc = new CellConstraints();

		// ======== this ========
		setTitle(bundle.getString("JProgramTags.this.title"));
		setIconImage(new ImageIcon(getClass().getResource(
				"/com/tiempometa/resources/stopwatch_small.png")).getImage());
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
						new ColumnSpec(Sizes.dluX(12)),
						FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
						new ColumnSpec(Sizes.dluX(62)),
						FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
						new ColumnSpec(Sizes.dluX(101)),
						FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
						new ColumnSpec(Sizes.dluX(71)),
						FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
						new ColumnSpec(Sizes.dluX(68)),
						FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
						new ColumnSpec(Sizes.dluX(97)) }, new RowSpec[] {
						new RowSpec(Sizes.dluY(10)),
						FormFactory.LINE_GAP_ROWSPEC,
						new RowSpec(Sizes.dluY(15)),
						FormFactory.LINE_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.LINE_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.LINE_GAP_ROWSPEC,
						new RowSpec(Sizes.dluY(17)),
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
				label1.setText(bundle.getString("JProgramTags.label1.text"));
				label1.setFont(new Font("Tahoma", Font.PLAIN, 36));
				contentPanel.add(label1, cc.xywh(3, 5, 3, 1));

				// ---- statusLabel ----
				statusLabel.setText(bundle
						.getString("JProgramTags.statusLabel.text"));
				statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
				statusLabel.setBackground(Color.yellow);
				statusLabel.setOpaque(true);
				statusLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
				contentPanel.add(statusLabel, cc.xywh(9, 3, 3, 5));

				// ---- nextChipnumberTextField ----
				nextChipnumberTextField.setFont(new Font("Tahoma", Font.PLAIN,
						36));
				contentPanel.add(nextChipnumberTextField, cc.xy(7, 5));

				// ---- programButton ----
				programButton.setText(bundle
						.getString("JProgramTags.programButton.text"));
				programButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						programButtonActionPerformed(e);
					}
				});
				contentPanel.add(programButton, cc.xywh(3, 7, 3, 1));

				// ---- lockCheckbox ----
				lockCheckbox.setText(bundle
						.getString("JProgramTags.lockCheckbox.text"));
				lockCheckbox.setSelected(true);
				lockCheckbox.addItemListener(new ItemListener() {
					@Override
					public void itemStateChanged(ItemEvent e) {
						checkBox2ItemStateChanged(e);
					}
				});
				contentPanel.add(lockCheckbox, cc.xy(3, 9));

				// ---- bibLabel ----
				bibLabel.setForeground(Color.red);
				bibLabel.setFont(new Font("Tahoma", Font.BOLD, 36));
				bibLabel.setHorizontalAlignment(SwingConstants.CENTER);
				contentPanel.add(bibLabel, cc.xy(11, 9));

				// ---- label2 ----
				label2.setText(bundle.getString("JProgramTags.label2.text"));
				contentPanel.add(label2, cc.xy(3, 11));
				contentPanel.add(accessPasswordTextField, cc.xy(5, 11));

				// ---- label3 ----
				label3.setText(bundle.getString("JProgramTags.label3.text"));
				contentPanel.add(label3, cc.xy(7, 11));

				// ---- tidTextField ----
				tidTextField.setEditable(false);
				contentPanel.add(tidTextField, cc.xywh(9, 11, 3, 1));

				// ---- label6 ----
				label6.setText(bundle.getString("JProgramTags.label6.text"));
				contentPanel.add(label6, cc.xy(3, 13));
				contentPanel.add(killPasswordTextField, cc.xy(5, 13));

				// ---- label4 ----
				label4.setText(bundle.getString("JProgramTags.label4.text"));
				contentPanel.add(label4, cc.xy(7, 13));

				// ---- epcTextField ----
				epcTextField.setEditable(false);
				contentPanel.add(epcTextField, cc.xywh(9, 13, 3, 1));

				// ---- checkBox1 ----
				checkBox1.setText(bundle
						.getString("JProgramTags.checkBox1.text"));
				checkBox1.setEnabled(false);
				contentPanel.add(checkBox1, cc.xy(3, 15));

				// ---- label5 ----
				label5.setText(bundle.getString("JProgramTags.label5.text"));
				contentPanel.add(label5, cc.xy(7, 15));

				// ---- programmedEpcTextField ----
				programmedEpcTextField.setEditable(false);
				contentPanel.add(programmedEpcTextField, cc.xywh(9, 15, 3, 1));

				// ======== scrollPane1 ========
				{
					scrollPane1.setViewportView(tagReadTable);
				}
				contentPanel.add(scrollPane1, cc.xywh(3, 17, 9, 1));
			}
			dialogPane.add(contentPanel, BorderLayout.EAST);

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
	private JLabel statusLabel;
	private JTextField nextChipnumberTextField;
	private JButton programButton;
	private JCheckBox lockCheckbox;
	private JLabel bibLabel;
	private JLabel label2;
	private JTextField accessPasswordTextField;
	private JLabel label3;
	private JTextField tidTextField;
	private JLabel label6;
	private JTextField killPasswordTextField;
	private JLabel label4;
	private JTextField epcTextField;
	private JCheckBox checkBox1;
	private JLabel label5;
	private JTextField programmedEpcTextField;
	private JScrollPane scrollPane1;
	private JTable tagReadTable;
	private JPanel buttonBar;
	private JButton closeButton;

	// JFormDesigner - End of variables declaration //GEN-END:variables

	@Override
	public void handleReadings(List<TagReading> readings) {
		bibLabel.setText("");
		if (readings.size() > 0) {
			if (readings.size() == 1) {
				statusLabel.setBackground(Color.cyan);
				statusLabel.setText("Leyendo tag");
				for (TagReading tagReading : readings) {
					logger.info("Tag data dump");
					logger.info(tagReading.getTagReadData().getTag()
							.epcString());
					logger.info(String.valueOf(tagReading.getTagReadData()
							.getData().length));
					logger.info(Hex.encodeHexString(tagReading.getTagReadData()
							.getData()));
					logger.info(String.valueOf(tagReading.getTagReadData()
							.getEPCMemData().length));
					logger.info(Hex.encodeHexString(tagReading.getTagReadData()
							.getEPCMemData()));
					logger.info(String.valueOf(tagReading.getTagReadData()
							.getTIDMemData().length));
					logger.info(Hex.encodeHexString(tagReading.getTagReadData()
							.getTIDMemData()));
					logger.info(String.valueOf(tagReading.getTagReadData()
							.getReservedMemData().length));
					logger.info(Hex.encodeHexString(tagReading.getTagReadData()
							.getReservedMemData()));
					logger.info(String.valueOf(tagReading.getTagReadData()
							.getUserMemData().length));
					logger.info(Hex.encodeHexString(tagReading.getTagReadData()
							.getUserMemData()));
					if (tagReading.getTid() == null) {
						try {
							tagReading.setTid(ReaderContext.readTid(
									tagReading.getEpc(), 12));
							if (logger.isDebugEnabled()) {
								logger.debug("Got tag " + tagReading.getEpc()
										+ " - " + tagReading.getTid());
							}
							// try {
							statusLabel.setBackground(Color.green);
							statusLabel.setText("Tag leido");
							tidTextField.setText(tagReading.getTid());
							epcTextField.setText(tagReading.getEpc());
							programmedEpcTextField.setText("");
							// find tag by EPC/TID in database
							logger.debug("Looking up rfid by epc "
									+ tagReading.getEpc()
									+ " epc char "
									+ Hex.encodeHexString(tagReading.getEpc()
											.getBytes()));
							Rfid rfid = totalRfidMap.get(tagReading.getEpc()
									.toUpperCase());
							if (rfid == null) {
								logger.debug("Rfid string not in database tag list. Programming tag.");
								// if in DB, warn

								// if not then program with next chipnumber
								programTag(tagReading);

							} else {
								logger.debug("Rfid string IN database tag list.");
								Rfid batchRfid = rfidMap.get(tagReading
										.getEpc());
								if (batchRfid == null) {
									logger.debug("Rfid string IN current program batch");
									int response = JOptionPane
											.showConfirmDialog(
													this,
													"Este tag tiene un código que existe en el evento actual.\nDesea sobreescribir este tag?",
													"Tag ya programado",
													JOptionPane.YES_NO_OPTION,
													JOptionPane.WARNING_MESSAGE);
									if (response == JOptionPane.YES_OPTION) {
										programTag(tagReading);
									}
								} else {
									JOptionPane
											.showMessageDialog(
													this,
													"Este tag tiene un código que ya ha sido programado en este lote.",
													"Tag ya programado",
													JOptionPane.ERROR_MESSAGE);
								}
							}

							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							statusLabel.setBackground(Color.white);
							statusLabel.setText("Remover tag");
							try {
								Thread.sleep(500);
							} catch (InterruptedException e1) {
								e1.printStackTrace();
							}
						} catch (ReaderException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							statusLabel.setBackground(Color.red);
							statusLabel.setText("Error");
							try {
								Thread.sleep(500);
							} catch (InterruptedException e1) {
								e1.printStackTrace();
							}
						}
					}
				}
			} else {
				statusLabel.setBackground(Color.orange);
				statusLabel.setText("Dos o más tags");
			}
		} else {
			statusLabel.setBackground(Color.yellow);
			statusLabel.setText("Sin tag");

		}

	}

	private void programTag(TagReading tagReading) throws ReaderException {
		Rfid rfid;
		Integer chipNumber = null;
		try {

			chipNumber = Integer.valueOf(nextChipnumberTextField.getText());
			List<Rfid> rfidList = rfidDao.findByChipNumber(chipNumber);
			if (rfidList.size() == 0) {
				JOptionPane.showMessageDialog(this,
						"No existe un chip con numero " + chipNumber,
						"Error de datos", JOptionPane.ERROR_MESSAGE);
			} else {
				rfid = rfidList.get(0);
				String rfidString = rfid.getRfidString();
				// program

				try {
					String accessPassword = null;
					String killPassword = null;
					ReaderContext.writeEpc(tagReading.getTagReadData(),
							rfidString);
					logger.info("Tag programmed");
					statusLabel.setBackground(Color.green);
					statusLabel.setText("Tag programado");
					tagTableModel.getData().add(rfid);
					tagTableModel.fireTableDataChanged();
					rfidMap.put(rfid.getRfidString(), rfid);
					programmedEpcTextField.setText(rfidString);
					chipNumber = chipNumber + 1;
					nextChipnumberTextField.setText(chipNumber.toString());
					if (lockCheckbox.isSelected()) {
						logger.debug("Lock tag enabled");
						if (accessPasswordTextField.getText().matches(
								"[0-9a-fA-F]{8}?")) {
							accessPassword = accessPasswordTextField.getText();
						} else {
							JOptionPane
									.showMessageDialog(
											this,
											"Se debe especificar una contraseña de accesso de 8 caracteres (0-9, A-F)",
											"Error de contraseña",
											JOptionPane.ERROR_MESSAGE);
							return;
						}
						if (killPasswordTextField.getText().matches(
								"[0-9a-fA-F]{8}?")) {
							killPassword = killPasswordTextField.getText();
						} else {
							JOptionPane
									.showMessageDialog(
											this,
											"Se debe especificar una contraseña de desactivación de 8 caracteres (0-9, A-F)",
											"Error de contraseña",
											JOptionPane.ERROR_MESSAGE);
							return;
						}
						try {
							short[] access = new short[2];
							logger.debug("Building acces array:"
									+ accessPassword.substring(0, 4) + ":"
									+ accessPassword.substring(4, 8));
							access[0] = Integer.valueOf(
									accessPassword.substring(0, 4), 16)
									.shortValue();
							access[1] = Integer.valueOf(
									accessPassword.substring(4, 8), 16)
									.shortValue();
							logger.debug("Built access array");
							short[] killword = new short[2];
							killword[0] = Integer.valueOf(
									killPassword.substring(0, 4), 16)
									.shortValue();
							killword[1] = Integer.valueOf(
									killPassword.substring(4, 8), 16)
									.shortValue();
							logger.debug("Built killword array");
							try {
								TagData data = new TagData(rfidString);
								logger.debug("Writing access...");
								ReaderContext.writeAccess(access, data);
								logger.debug("Writing kill...");
								ReaderContext.writeKill(killword, data);
								Integer password = Long.valueOf(accessPassword,
										16).intValue();
								logger.debug("Locking access...");
								ReaderContext.lockTag(password, new LockAction(
										LockAction.EPC_LOCK), data);
								statusLabel.setBackground(Color.yellow);
								statusLabel.setText("EPC locked");
								ReaderContext.lockTag(password, new LockAction(
										LockAction.ACCESS_LOCK), data);
								statusLabel.setBackground(Color.yellow);
								statusLabel.setText("ACCESS locked");
								ReaderContext.lockTag(password, new LockAction(
										LockAction.KILL_LOCK), data);
								statusLabel.setBackground(Color.yellow);
								statusLabel.setText("KILL locked");
								statusLabel.setBackground(Color.green);
								statusLabel.setText("Tag bloqueado");
							} catch (ReaderCodeException e) {
								statusLabel.setBackground(Color.red);
								statusLabel.setText("Error bloqueando");
								JOptionPane.showMessageDialog(this,
										"Error de bloqueo " + e.getMessage(),
										"Error de bloqueo",
										JOptionPane.ERROR_MESSAGE);
							}
						} catch (NumberFormatException e) {
							logger.warn("Invalid access code format "
									+ e.getMessage());
						}
					} else {

					}
				} catch (DecoderException e) {
					statusLabel.setBackground(Color.red);
					statusLabel.setText("Tag no programado");
					e.printStackTrace();
				}
			}
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(this,
					"El valor de chip debe ser numérico", "Error de datos",
					JOptionPane.ERROR_MESSAGE);
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(),
					"Error de base de datos", JOptionPane.ERROR_MESSAGE);
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
