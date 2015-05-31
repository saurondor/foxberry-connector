/*
 * Created by JFormDesigner on Sat May 30 17:19:21 CDT 2015
 */

package com.tiempometa.muestradatos;

import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.*;
import java.util.List;

import javax.swing.*;

import org.apache.log4j.Logger;

import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import com.thingmagic.ReaderException;
import com.tiempometa.timing.dao.RfidDao;
import com.tiempometa.timing.dao.access.RfidDaoImpl;
import com.tiempometa.timing.models.Rfid;

/**
 * @author Gerardo Esteban Tasistro Giubetic
 */
public class JReadTags extends JDialog implements TagReadListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2938446310896947013L;
	private static final Logger logger = Logger.getLogger(JReadTags.class);
	private RfidDao rfidDao = new RfidDaoImpl();

	public JReadTags(Frame frame, boolean modal) {
		super(frame, modal);
		initComponents();
		logger.debug("Adding self to tag read listeners");
		ReaderContext.addReadingListener(this);
	}

	public JReadTags() {
		initComponents();
		ReaderContext.addReadingListener(this);
	}

	private void startReadingButtonActionPerformed(ActionEvent e) {
		try {
			ReaderContext.startReading();
		} catch (ReaderException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
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
		scrollPane1 = new JScrollPane();
		table1 = new JTable();
		label3 = new JLabel();
		tidTextField = new JTextField();
		label4 = new JLabel();
		epcTextField = new JTextField();
		label1 = new JLabel();
		comboBox1 = new JComboBox<>();
		startReadingButton = new JButton();
		buttonBar = new JPanel();
		okButton = new JButton();
		CellConstraints cc = new CellConstraints();

		//======== this ========
		setTitle(bundle.getString("JReadTags.this.title"));
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

				//======== scrollPane1 ========
				{
					scrollPane1.setViewportView(table1);
				}
				contentPanel.add(scrollPane1, cc.xywh(3, 9, 5, 1));

				//---- label3 ----
				label3.setText(bundle.getString("JReadTags.label3.text"));
				contentPanel.add(label3, cc.xy(3, 11));
				contentPanel.add(tidTextField, cc.xywh(5, 11, 3, 1));

				//---- label4 ----
				label4.setText(bundle.getString("JReadTags.label4.text"));
				contentPanel.add(label4, cc.xy(3, 13));
				contentPanel.add(epcTextField, cc.xywh(5, 13, 3, 1));

				//---- label1 ----
				label1.setText(bundle.getString("JReadTags.label1.text"));
				contentPanel.add(label1, cc.xy(3, 15));

				//---- comboBox1 ----
				comboBox1.setModel(new DefaultComboBoxModel<>(new String[] {
					"TID",
					"EPC"
				}));
				contentPanel.add(comboBox1, cc.xy(5, 15));

				//---- startReadingButton ----
				startReadingButton.setText(bundle.getString("JReadTags.startReadingButton.text"));
				startReadingButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						startReadingButtonActionPerformed(e);
					}
				});
				contentPanel.add(startReadingButton, cc.xy(7, 15));
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
	private JLabel label2;
	private JTextField nextBibTextField;
	private JLabel statusLabel;
	private JScrollPane scrollPane1;
	private JTable table1;
	private JLabel label3;
	private JTextField tidTextField;
	private JLabel label4;
	private JTextField epcTextField;
	private JLabel label1;
	private JComboBox<String> comboBox1;
	private JButton startReadingButton;
	private JPanel buttonBar;
	private JButton okButton;
	// JFormDesigner - End of variables declaration //GEN-END:variables
	@Override
	public void handleReadings(List<TagReading> readings) {
		if (readings.size() > 0) {
			if (readings.size() == 1) {
				statusLabel.setBackground(Color.cyan);
				statusLabel.setText("Leyendo tag");
				for (TagReading tagReading : readings) {
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
								String rfidString = tagReading.getTid();
								List<Rfid> rfids = rfidDao
										.findByRfid(rfidString);
								if (rfids.size() == 0) {
									String bib = nextBibTextField.getText();
									Integer chipNumber = null;
									try {
										chipNumber = Integer.valueOf(bib);
										Rfid rfid = new Rfid(null, null, bib,
												rfidString,
												Rfid.STATUS_NOT_ASSIGNED,
												Rfid.PAYMENT_STATUS_UNPAID,
												Rfid.TOKEN_STATUS_AVAILABLE, null,
												chipNumber);
										rfidDao.save(rfid);
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
								} else {
									statusLabel.setBackground(Color.red);
									statusLabel.setText("Tag ya leido");
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
