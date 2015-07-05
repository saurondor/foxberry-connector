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
public class JProgramTags extends JDialog implements TagReadListener  {
	/**
	 * 
	 */
	private static final long serialVersionUID = -947822386284311068L;
	private static final Logger logger = Logger.getLogger(JProgramTags.class);
	private RfidDao rfidDao = new RfidDaoImpl();

	private TagReadTableDataModel tagTableModel = new TagReadTableDataModel();
	
	private void programButtonActionPerformed(ActionEvent e) {
		if (ReaderContext.isUsbConnected()) {
			if (ReaderContext.isUsbReading()) {
				ReaderContext.stopReading();
				programButton.setText("Iniciar Lectura");
			} else {
				try {
					ReaderContext.startReading();
					programButton.setText("Detener Lectura");
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
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		ResourceBundle bundle = ResourceBundle.getBundle("com.tiempometa.muestradatos.muestradatos");
		dialogPane = new JPanel();
		contentPanel = new JPanel();
		label1 = new JLabel();
		nextChipnumberTextField = new JTextField();
		statusLabel = new JLabel();
		programButton = new JButton();
		checkBox1 = new JCheckBox();
		bibLabel = new JLabel();
		label3 = new JLabel();
		tidTextField = new JTextField();
		label4 = new JLabel();
		epcTextField = new JTextField();
		scrollPane1 = new JScrollPane();
		tagReadTable = new JTable();
		buttonBar = new JPanel();
		okButton = new JButton();
		CellConstraints cc = new CellConstraints();

		//======== this ========
		setTitle(bundle.getString("JProgramTags.this.title"));
		setIconImage(new ImageIcon(getClass().getResource("/com/tiempometa/resources/stopwatch_small.png")).getImage());
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
						new ColumnSpec(Sizes.dluX(71)),
						FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
						new ColumnSpec(Sizes.dluX(68)),
						FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
						FormFactory.DEFAULT_COLSPEC
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
						FormFactory.DEFAULT_ROWSPEC
					}));

				//---- label1 ----
				label1.setText(bundle.getString("JProgramTags.label1.text"));
				label1.setFont(new Font("Tahoma", Font.PLAIN, 36));
				contentPanel.add(label1, cc.xy(3, 5));

				//---- nextChipnumberTextField ----
				nextChipnumberTextField.setFont(new Font("Tahoma", Font.PLAIN, 36));
				contentPanel.add(nextChipnumberTextField, cc.xy(5, 5));

				//---- statusLabel ----
				statusLabel.setText(bundle.getString("JProgramTags.statusLabel.text"));
				statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
				statusLabel.setBackground(Color.yellow);
				statusLabel.setOpaque(true);
				statusLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
				contentPanel.add(statusLabel, cc.xywh(7, 3, 3, 5));

				//---- programButton ----
				programButton.setText(bundle.getString("JProgramTags.programButton.text"));
				programButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						programButtonActionPerformed(e);
					}
				});
				contentPanel.add(programButton, cc.xy(3, 7));

				//---- checkBox1 ----
				checkBox1.setText(bundle.getString("JProgramTags.checkBox1.text"));
				contentPanel.add(checkBox1, cc.xy(3, 9));

				//---- bibLabel ----
				bibLabel.setForeground(Color.red);
				bibLabel.setFont(new Font("Tahoma", Font.BOLD, 36));
				bibLabel.setHorizontalAlignment(SwingConstants.CENTER);
				contentPanel.add(bibLabel, cc.xy(9, 9));

				//---- label3 ----
				label3.setText(bundle.getString("JProgramTags.label3.text"));
				contentPanel.add(label3, cc.xy(3, 11));
				contentPanel.add(tidTextField, cc.xywh(5, 11, 3, 1));

				//---- label4 ----
				label4.setText(bundle.getString("JProgramTags.label4.text"));
				contentPanel.add(label4, cc.xy(3, 13));
				contentPanel.add(epcTextField, cc.xywh(5, 13, 3, 1));

				//======== scrollPane1 ========
				{
					scrollPane1.setViewportView(tagReadTable);
				}
				contentPanel.add(scrollPane1, cc.xywh(3, 15, 5, 1));
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
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	private JPanel dialogPane;
	private JPanel contentPanel;
	private JLabel label1;
	private JTextField nextChipnumberTextField;
	private JLabel statusLabel;
	private JButton programButton;
	private JCheckBox checkBox1;
	private JLabel bibLabel;
	private JLabel label3;
	private JTextField tidTextField;
	private JLabel label4;
	private JTextField epcTextField;
	private JScrollPane scrollPane1;
	private JTable tagReadTable;
	private JPanel buttonBar;
	private JButton okButton;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
	
	
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
//							try {
								statusLabel.setBackground(Color.green);
								statusLabel.setText("Tag leido");
								tidTextField.setText(tagReading.getTid());
								epcTextField.setText(tagReading.getEpc());
								// find tag by EPC/TID in database
								
								// if in DB, warn
								
								// if not then program with next chipnumber
								Integer chipNumber = null;
								try {

									chipNumber = Integer.valueOf(nextChipnumberTextField.getText());
									List<Rfid> rfidList = rfidDao.findByChipNumber(chipNumber);
									if (rfidList.size() == 0){
										
									} else {
										Rfid rfid = rfidList.get(0);
										String rfidString = rfid.getRfidString();
										// program 
										
										try {
											ReaderContext.writeEpc(tagReading.getTagReadData(), rfidString);
											logger.info("Tag programmed");
										} catch (DecoderException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									}
								} catch (NumberFormatException e) {
									// TODO: handle exception
								} catch (SQLException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
								
								
//								String rfidString = null;
//								List<Rfid> rfids = rfidDao
//										.findByRfid(rfidString);
//								if (rfids.size() == 0) {
//									String bib = nextBibTextField.getText();
//									Rfid bibRfid = rfidDao.fetchByBib(bib);
//									if ((bibRfid != null)
//											& (!allowDuplicateBibsCheckBox
//													.isSelected())) {
//										JOptionPane.showMessageDialog(this, "Ese número ya ha sido capturado", "Número duplicado", JOptionPane.ERROR_MESSAGE);
//									} else {
//										Integer chipNumber = null;
//										try {
//											chipNumber = Integer.valueOf(bib);
//											Rfid rfid = new Rfid(
//													null,
//													null,
//													bib,
//													rfidString,
//													Rfid.STATUS_NOT_ASSIGNED,
//													Rfid.PAYMENT_STATUS_UNPAID,
//													Rfid.TOKEN_STATUS_AVAILABLE,
//													null, chipNumber);
//											rfidDao.save(rfid);
//											tagTableModel.getData().add(rfid);
//											tagTableModel
//													.fireTableDataChanged();
//											statusLabel.setText("Tag guardado");
//											chipNumber = chipNumber + 1;
//											nextBibTextField.setText(String
//													.valueOf(chipNumber));
//										} catch (NumberFormatException e) {
//											JOptionPane
//													.showMessageDialog(
//															this,
//															"El valor de número debe ser numérico",
//															"Error de datos",
//															JOptionPane.ERROR_MESSAGE);
//										}
//									}
//								} else {
//									statusLabel.setBackground(Color.red);
//									statusLabel.setText("Tag ya leido");
//									Rfid rfid = rfids.get(0);
//									bibLabel.setText(rfid.getBib());
//									try {
//										Thread.sleep(1000);
//									} catch (InterruptedException e) {
//										// TODO Auto-generated catch block
//										e.printStackTrace();
//									}
//
//								}
//							} catch (SQLException e1) {
//								JOptionPane.showMessageDialog(
//										this,
//										"Error guardando tag: "
//												+ e1.getMessage(),
//										"Error de base de datos",
//										JOptionPane.ERROR_MESSAGE);
//							}

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
