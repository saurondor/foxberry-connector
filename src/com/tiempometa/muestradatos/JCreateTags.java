/*
 * Created by JFormDesigner on Tue Nov 10 09:56:28 CST 2015
 */

package com.tiempometa.muestradatos;

import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.*;
import java.util.List;

import javax.swing.*;

import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import com.tiempometa.timing.dao.RfidDao;
import com.tiempometa.timing.dao.access.RfidDaoImpl;
import com.tiempometa.timing.models.Rfid;

/**
 * @author Gerardo Esteban Tasistro Giubetic
 */
public class JCreateTags extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8700935954589693983L;
	private RfidDao rfidDao = new RfidDaoImpl();

	public JCreateTags(Frame owner) {
		super(owner);
		initComponents();
	}

	public JCreateTags(Dialog owner) {
		super(owner);
		initComponents();
	}

	private void createButtonActionPerformed(ActionEvent e) {
		Integer tagAmount = null;
		Integer startBib = null;
		Integer endBib = null;
		try {
			tagAmount = Integer.valueOf(tagAmountTextField.getText());
			startBib = Integer.valueOf(startBibTextField.getText());
			endBib = startBib + tagAmount;
			List<Rfid> rfidList = new ArrayList<Rfid>();
			rfidList = rfidDao.findByChipNumber(startBib, endBib);
			if (rfidList.size() > 0) {
				JOptionPane
				.showMessageDialog(this,
						"Ya existen números en el rango especificado.",
						"Error de rango",
						JOptionPane.ERROR_MESSAGE);
			} else {

				rfidList = new ArrayList<Rfid>();
				Integer chipNumber = null;
				String bib = null;
				String rfidString = null;
				for (int i = 0; i < tagAmount; i++) {
					chipNumber = i + startBib;
					bib = chipNumber.toString();
					if (useBibAsValueCheckBox.isSelected()) {
						rfidString = bibToRfidString(chipNumber);
					} else {
						rfidString = UUID.randomUUID().toString()
								.replaceAll("-", "").substring(8);
					}
					Rfid rfid = new Rfid(null, 0, bib, rfidString, chipNumber,
							Rfid.STATUS_NOT_ASSIGNED);
					// Rfid rfid = new Rfid(null, bib, rfidString,
					// Rfid.STATUS_NOT_ASSIGNED, Rfid.PAYMENT_STATUS_UNPAID,
					// Rfid.TOKEN_STATUS_AVAILABLE, null, chipNumber);
					rfidList.add(rfid);
				}
				try {
					rfidDao.batchSave(rfidList);
					JOptionPane.showMessageDialog(this, "Se generaron "
							+ rfidList.size() + " tags",
							"Tags generados exitosamente",
							JOptionPane.INFORMATION_MESSAGE);
				} catch (SQLException e1) {
					JOptionPane
							.showMessageDialog(this,
									"No se pudieron guardar los tags",
									"Error de base de datos",
									JOptionPane.ERROR_MESSAGE);
				}
			}
		} catch (NumberFormatException e2) {
			JOptionPane.showMessageDialog(this,
					"Los valores de y a deben ser numéricos", "Error de datos",
					JOptionPane.ERROR_MESSAGE);
		} catch (SQLException e2) {
			JOptionPane.showMessageDialog(this,
					"Error SQL: " + e2.getMessage(), "Error de datos",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private String bibToRfidString(Integer chipNumber) {
		String hexValue = Integer.toHexString(chipNumber);
		StringBuffer rfidString = new StringBuffer();
		for (int i = 0; i < (24 - hexValue.length()); i++) {
			rfidString.append("0");
		}
		rfidString.append(hexValue);
		return rfidString.toString();
	}

	private void closeButtonActionPerformed(ActionEvent e) {
		this.dispose();
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY
		// //GEN-BEGIN:initComponents
		ResourceBundle bundle = ResourceBundle
				.getBundle("com.tiempometa.muestradatos.muestradatos");
		dialogPane = new JPanel();
		contentPanel = new JPanel();
		label1 = new JLabel();
		tagAmountTextField = new JTextField();
		label2 = new JLabel();
		startBibTextField = new JTextField();
		useBibAsValueCheckBox = new JCheckBox();
		createButton = new JButton();
		buttonBar = new JPanel();
		closeButton = new JButton();
		CellConstraints cc = new CellConstraints();

		// ======== this ========
		setTitle(bundle.getString("JCreateTags.this.title"));
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());

		// ======== dialogPane ========
		{
			dialogPane.setBorder(Borders.DIALOG_BORDER);
			dialogPane.setLayout(new BorderLayout());

			// ======== contentPanel ========
			{
				contentPanel.setLayout(new FormLayout(new ColumnSpec[] {
						new ColumnSpec(Sizes.dluX(18)),
						FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
						FormFactory.DEFAULT_COLSPEC,
						FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
						new ColumnSpec(Sizes.dluX(112)) }, new RowSpec[] {
						new RowSpec(Sizes.dluY(16)),
						FormFactory.LINE_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.LINE_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.LINE_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.LINE_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC }));

				// ---- label1 ----
				label1.setText(bundle.getString("JCreateTags.label1.text"));
				contentPanel.add(label1, cc.xy(3, 3));
				contentPanel.add(tagAmountTextField, cc.xy(5, 3));

				// ---- label2 ----
				label2.setText(bundle.getString("JCreateTags.label2.text"));
				contentPanel.add(label2, cc.xy(3, 5));
				contentPanel.add(startBibTextField, cc.xy(5, 5));

				// ---- useBibAsValueCheckBox ----
				useBibAsValueCheckBox.setText(bundle
						.getString("JCreateTags.useBibAsValueCheckBox.text"));
				contentPanel.add(useBibAsValueCheckBox, cc.xy(5, 7));

				// ---- createButton ----
				createButton.setText(bundle
						.getString("JCreateTags.createButton.text"));
				createButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						createButtonActionPerformed(e);
					}
				});
				contentPanel.add(createButton, cc.xy(5, 9));
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
	private JTextField tagAmountTextField;
	private JLabel label2;
	private JTextField startBibTextField;
	private JCheckBox useBibAsValueCheckBox;
	private JButton createButton;
	private JPanel buttonBar;
	private JButton closeButton;
	// JFormDesigner - End of variables declaration //GEN-END:variables
}
