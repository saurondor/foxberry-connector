/*
 * Created by JFormDesigner on Sat May 30 17:19:21 CDT 2015
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

import org.apache.commons.codec.binary.Hex;
import org.apache.log4j.Logger;

import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import com.thingmagic.ReaderException;
import com.tiempometa.timing.dao.RfidDao;
import com.tiempometa.timing.dao.access.RfidDaoImpl;
import com.tiempometa.timing.models.Rfid;

/**
 * @author Gerardo Tasistro gtasistro@tiempometa.com
 * Copyright 2015 Gerardo Tasistro
 * Licensed under the Mozilla Public License, v. 2.0
 * 
 */
public class JReadTags extends JDialog implements TagReadListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2938446310896947013L;
	private static final Logger logger = Logger.getLogger(JReadTags.class);
	private RfidDao rfidDao = new RfidDaoImpl();
	private TagReadTableDataModel tagTableModel = new TagReadTableDataModel();

	public JReadTags(Frame frame, boolean modal) {
		super(frame, modal);
		initComponents();
		logger.debug("Adding self to tag read listeners");
		ReaderContext.addReadingListener(this);
		tagReadTable.setModel(tagTableModel);
		tagReadTable.setAutoCreateRowSorter(true);
		TableRowSorter<TableModel> sorter = new TableRowSorter<>(
				tagReadTable.getModel());
		sorter.setComparator(0, new BibComparator());
		tagReadTable.setRowSorter(sorter);

	}

	public JReadTags() {
		initComponents();
		ReaderContext.addReadingListener(this);
		tagReadTable.setModel(tagTableModel);
		tagReadTable.setAutoCreateRowSorter(true);
		TableRowSorter<TableModel> sorter = new TableRowSorter<>(
				tagReadTable.getModel());
		sorter.setComparator(0, new BibComparator());
		tagReadTable.setRowSorter(sorter);
	}

	private void startReadingButtonActionPerformed(ActionEvent e) {
		if (ReaderContext.isUsbConnected()) {
			if (ReaderContext.isUsbReading()) {
				ReaderContext.stopReading();
				startReadingButton.setText("Iniciar Lectura");
			} else {
				try {
					ReaderContext.startReading();
					startReadingButton.setText("Detener Lectura");
				} catch (ReaderException e1) {
					JOptionPane.showMessageDialog(this,
							"No se pudo iniciar la lectura.",
							"Iniciar lectura", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}

	private void deleteAllButtonActionPerformed(ActionEvent e) {
		int response = JOptionPane
				.showConfirmDialog(
						this,
						"Se borrarán todos los tags de la base de datos.\nEsta operación no se puede deshacer.\n¿Continuar?",
						"Borrar todos los tags", JOptionPane.WARNING_MESSAGE);
		if (response == JOptionPane.YES_OPTION) {
			try {
				rfidDao.deleteAll();
				tagTableModel.setData(new ArrayList<Rfid>());
				tagTableModel.fireTableDataChanged();
			} catch (SQLException e1) {
				JOptionPane.showMessageDialog(
						this,
						"No se pudieron borrar todos los tags. "
								+ e1.getMessage(), "Error borrando tags",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private void deleteReadButtonActionPerformed(ActionEvent e) {
		int response = JOptionPane
				.showConfirmDialog(
						this,
						"Se borrará el tag seleccionado.\nEsta operación no se puede deshacer.\n¿Continuar?",
						"Borrar tags", JOptionPane.WARNING_MESSAGE);
		if (response == JOptionPane.YES_OPTION) {
			try {
				List<Rfid> rfids = tagTableModel.getData();
				for (Rfid rfid : rfids) {
					rfidDao.delete(rfid);
				}
				tagTableModel.setData(new ArrayList<Rfid>());
				tagTableModel.fireTableDataChanged();
			} catch (SQLException e1) {
				JOptionPane.showMessageDialog(
						this,
						"No se pudieron borrar todos los tags. "
								+ e1.getMessage(), "Error borrando tags",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private void deleteSelectedButtonActionPerformed(ActionEvent e) {
		int response = JOptionPane
				.showConfirmDialog(
						this,
						"Se borrarán los tags seleccionados.\nEsta operación no se puede deshacer.\n¿Continuar?",
						"Borrar tags seleccionados",
						JOptionPane.WARNING_MESSAGE);
		if (response == JOptionPane.YES_OPTION) {
			int[] rows = tagReadTable.getSelectedRows();
			List<Rfid> removedItems = new ArrayList<Rfid>();
			for (int i = 0; i < rows.length; i++) {
				Rfid rfid = tagTableModel.getData().get(
						tagReadTable.convertRowIndexToModel(rows[i]));
				try {
					rfidDao.delete(rfid);
					removedItems.add(rfid);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			for (int i = 0; i < removedItems.size(); i++) {
				Rfid rfid = removedItems.get(i);
				tagTableModel.getData().remove(rfid);
			}
			tagTableModel.fireTableDataChanged();
		}
	}

	private void closeButtonActionPerformed(ActionEvent e) {
		// TODO add your code here
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY
		// //GEN-BEGIN:initComponents
		ResourceBundle bundle = ResourceBundle.getBundle("com.tiempometa.muestradatos.muestradatos");
		dialogPane = new JPanel();
		contentPanel = new JPanel();
		label2 = new JLabel();
		nextBibTextField = new JTextField();
		statusLabel = new JLabel();
		bibLabel = new JLabel();
		scrollPane1 = new JScrollPane();
		tagReadTable = new JTable();
		label1 = new JLabel();
		dataToStoreComboBox = new JComboBox<>();
		allowDuplicateBibsCheckBox = new JCheckBox();
		startReadingButton = new JButton();
		label3 = new JLabel();
		tidTextField = new JTextField();
		label4 = new JLabel();
		epcTextField = new JTextField();
		deleteSelectedButton = new JButton();
		deleteReadButton = new JButton();
		deleteAllButton = new JButton();
		buttonBar = new JPanel();
		closeButton = new JButton();
		CellConstraints cc = new CellConstraints();

		//======== this ========
		setTitle(bundle.getString("JReadTags.this.title"));
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setResizable(false);
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
						new ColumnSpec(Sizes.dluX(85)),
						FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
						new ColumnSpec(Sizes.dluX(143))
					},
					new RowSpec[] {
						new RowSpec(Sizes.dluY(17)),
						FormFactory.LINE_GAP_ROWSPEC,
						new RowSpec(Sizes.dluY(20)),
						FormFactory.LINE_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.LINE_GAP_ROWSPEC,
						new RowSpec(Sizes.dluY(20)),
						FormFactory.LINE_GAP_ROWSPEC,
						new RowSpec(Sizes.dluY(25)),
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

				//---- label2 ----
				label2.setText(bundle.getString("JReadTags.label2.text"));
				label2.setFont(new Font("Tahoma", Font.PLAIN, 36));
				contentPanel.add(label2, cc.xy(3, 5));

				//---- nextBibTextField ----
				nextBibTextField.setFont(new Font("Tahoma", Font.PLAIN, 36));
				nextBibTextField.setHorizontalAlignment(SwingConstants.RIGHT);
				nextBibTextField.setText(bundle.getString("JReadTags.nextBibTextField.text"));
				contentPanel.add(nextBibTextField, cc.xy(5, 5));

				//---- statusLabel ----
				statusLabel.setText(bundle.getString("JReadTags.statusLabel.text"));
				statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
				statusLabel.setBackground(Color.yellow);
				statusLabel.setOpaque(true);
				statusLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
				contentPanel.add(statusLabel, cc.xywh(7, 3, 1, 5));

				//---- bibLabel ----
				bibLabel.setForeground(Color.red);
				bibLabel.setFont(new Font("Tahoma", Font.BOLD, 36));
				bibLabel.setHorizontalAlignment(SwingConstants.CENTER);
				contentPanel.add(bibLabel, cc.xy(7, 9));

				//======== scrollPane1 ========
				{
					scrollPane1.setViewportView(tagReadTable);
				}
				contentPanel.add(scrollPane1, cc.xywh(3, 11, 5, 1));

				//---- label1 ----
				label1.setText(bundle.getString("JReadTags.label1.text"));
				label1.setHorizontalAlignment(SwingConstants.RIGHT);
				contentPanel.add(label1, cc.xy(3, 13));

				//---- dataToStoreComboBox ----
				dataToStoreComboBox.setModel(new DefaultComboBoxModel<>(new String[] {
					"EPC",
					"TID"
				}));
				contentPanel.add(dataToStoreComboBox, cc.xy(5, 13));

				//---- allowDuplicateBibsCheckBox ----
				allowDuplicateBibsCheckBox.setText(bundle.getString("JReadTags.allowDuplicateBibsCheckBox.text"));
				contentPanel.add(allowDuplicateBibsCheckBox, cc.xy(7, 13));

				//---- startReadingButton ----
				startReadingButton.setText(bundle.getString("JReadTags.startReadingButton.text"));
				startReadingButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						startReadingButtonActionPerformed(e);
					}
				});
				contentPanel.add(startReadingButton, cc.xywh(3, 15, 5, 1));

				//---- label3 ----
				label3.setText(bundle.getString("JReadTags.label3.text"));
				label3.setHorizontalAlignment(SwingConstants.RIGHT);
				contentPanel.add(label3, cc.xy(3, 17));
				contentPanel.add(tidTextField, cc.xywh(5, 17, 3, 1));

				//---- label4 ----
				label4.setText(bundle.getString("JReadTags.label4.text"));
				label4.setHorizontalAlignment(SwingConstants.RIGHT);
				contentPanel.add(label4, cc.xy(3, 19));
				contentPanel.add(epcTextField, cc.xywh(5, 19, 3, 1));

				//---- deleteSelectedButton ----
				deleteSelectedButton.setText(bundle.getString("JReadTags.deleteSelectedButton.text"));
				deleteSelectedButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						deleteSelectedButtonActionPerformed(e);
					}
				});
				contentPanel.add(deleteSelectedButton, cc.xy(3, 21));

				//---- deleteReadButton ----
				deleteReadButton.setText(bundle.getString("JReadTags.deleteReadButton.text"));
				deleteReadButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						deleteReadButtonActionPerformed(e);
					}
				});
				contentPanel.add(deleteReadButton, cc.xy(3, 23));

				//---- deleteAllButton ----
				deleteAllButton.setText(bundle.getString("JReadTags.deleteAllButton.text"));
				deleteAllButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						deleteAllButtonActionPerformed(e);
					}
				});
				contentPanel.add(deleteAllButton, cc.xy(3, 25));
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
	private JLabel label2;
	private JTextField nextBibTextField;
	private JLabel statusLabel;
	private JLabel bibLabel;
	private JScrollPane scrollPane1;
	private JTable tagReadTable;
	private JLabel label1;
	private JComboBox<String> dataToStoreComboBox;
	private JCheckBox allowDuplicateBibsCheckBox;
	private JButton startReadingButton;
	private JLabel label3;
	private JTextField tidTextField;
	private JLabel label4;
	private JTextField epcTextField;
	private JButton deleteSelectedButton;
	private JButton deleteReadButton;
	private JButton deleteAllButton;
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
							try {
								statusLabel.setBackground(Color.green);
								statusLabel.setText("Tag leido");
								tidTextField.setText(tagReading.getTid());
								epcTextField.setText(tagReading.getEpc());
								String rfidString = null;
								switch (dataToStoreComboBox.getSelectedIndex()) {
								case 0:
									rfidString = tagReading.getEpc();
									break;
								case 1:
									rfidString = tagReading.getTid();
									break;
								default:
									break;
								}
								List<Rfid> rfids = rfidDao
										.findByRfid(rfidString);
								if (rfids.size() == 0) {
									String bib = nextBibTextField.getText();
									Rfid bibRfid = rfidDao.fetchByBib(bib);
									if ((bibRfid != null)
											& (!allowDuplicateBibsCheckBox
													.isSelected())) {
										JOptionPane.showMessageDialog(this, "Ese número ya ha sido capturado", "Número duplicado", JOptionPane.ERROR_MESSAGE);
									} else {
										Integer chipNumber = null;
										try {
											chipNumber = Integer.valueOf(bib);
											Rfid rfid = new Rfid(
													null,
													null,
													bib,
													rfidString,
													Rfid.STATUS_NOT_ASSIGNED,
													Rfid.PAYMENT_STATUS_UNPAID,
													Rfid.TOKEN_STATUS_AVAILABLE,
													null, chipNumber);
											rfidDao.save(rfid);
											tagTableModel.getData().add(rfid);
											tagTableModel
													.fireTableDataChanged();
											statusLabel.setText("Tag guardado");
											chipNumber = chipNumber + 1;
											nextBibTextField.setText(String
													.valueOf(chipNumber));
										} catch (NumberFormatException e) {
											JOptionPane
													.showMessageDialog(
															this,
															"El valor de número debe ser numérico",
															"Error de datos",
															JOptionPane.ERROR_MESSAGE);
										}
									}
								} else {
									statusLabel.setBackground(Color.red);
									statusLabel.setText("Tag ya leido");
									Rfid rfid = rfids.get(0);
									bibLabel.setText(rfid.getBib());
									try {
										Thread.sleep(1000);
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}

								}
							} catch (SQLException e1) {
								JOptionPane.showMessageDialog(
										this,
										"Error guardando tag: "
												+ e1.getMessage(),
										"Error de base de datos",
										JOptionPane.ERROR_MESSAGE);
							}

							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							statusLabel.setBackground(Color.white);
							statusLabel.setText("Remover tag");
						} catch (ReaderException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							statusLabel.setBackground(Color.red);
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
