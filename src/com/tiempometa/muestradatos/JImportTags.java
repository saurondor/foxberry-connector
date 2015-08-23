/*
 * Created by JFormDesigner on Sat Aug 22 11:24:53 CDT 2015
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

import jxl.read.biff.BiffException;

import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;

/**
 * @author Gerardo Esteban Tasistro Giubetic
 */
public class JImportTags extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3742832412550722249L;
	File excelFile = null;
	TagExcelImporter excelImporter = new TagExcelImporter();

	public JImportTags(Frame owner) {
		super(owner);
		initComponents();
	}

	public JImportTags(Dialog owner) {
		super(owner);
		initComponents();
	}

	private void selectImportFileButtonActionPerformed(ActionEvent e) {
		JFileChooser fc = new JFileChooser(excelFile);
		int response = fc.showOpenDialog(this);
		if (response == JFileChooser.APPROVE_OPTION) {
			excelFile = fc.getSelectedFile();
			try {
				excelImporter.open(excelFile);
				List<String> sheets = excelImporter.getSheetNames();
				DefaultComboBoxModel<String> comboModel = new DefaultComboBoxModel<String>(
						sheets.toArray(new String[sheets.size()]));
				sheetComboBox.setModel(comboModel);
				updateSheetColumns();
			} catch (BiffException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	private void updateSheetColumns() {
		List<String> columns = excelImporter.getColumnNames(sheetComboBox
				.getSelectedIndex());
		DefaultComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel<String>(
				(String[]) columns.toArray(new String[columns.size()]));
		bibComboBox.setModel(comboBoxModel);
		comboBoxModel = new DefaultComboBoxModel<String>(
				(String[]) columns.toArray(new String[columns.size()]));
		epcComboBox.setModel(comboBoxModel);

	}

	private void importButtonActionPerformed(ActionEvent e) {
		try {
			int importCount = excelImporter.importTags(
					sheetComboBox.getSelectedIndex(),
					bibComboBox.getSelectedIndex(),
					epcComboBox.getSelectedIndex());
			JOptionPane.showMessageDialog(this,
					"Se importaron los tags con éxito\nTotal importados "
							+ importCount, "Importar Tags",
					JOptionPane.PLAIN_MESSAGE);
		} catch (SQLException e1) {
			JOptionPane.showMessageDialog(this,
					"Error importar " + e1.getMessage(), "Importar Tags",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void closeButtonActionPerformed(ActionEvent e) {
		this.dispose();
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY
		// //GEN-BEGIN:initComponents
		ResourceBundle bundle = ResourceBundle.getBundle("com.tiempometa.muestradatos.muestradatos");
		dialogPane = new JPanel();
		contentPanel = new JPanel();
		label1 = new JLabel();
		selectImportFileButton = new JButton();
		label2 = new JLabel();
		sheetComboBox = new JComboBox();
		label3 = new JLabel();
		separator1 = new JSeparator();
		label4 = new JLabel();
		bibComboBox = new JComboBox();
		label5 = new JLabel();
		epcComboBox = new JComboBox();
		importButton = new JButton();
		buttonBar = new JPanel();
		closeButton = new JButton();
		CellConstraints cc = new CellConstraints();

		//======== this ========
		setTitle(bundle.getString("JImportTags.this.title"));
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
						new ColumnSpec(Sizes.dluX(140))
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
						FormFactory.DEFAULT_ROWSPEC
					}));

				//---- label1 ----
				label1.setText(bundle.getString("JImportTags.label1.text"));
				label1.setFont(new Font("Tahoma", Font.PLAIN, 14));
				contentPanel.add(label1, cc.xy(3, 1));

				//---- selectImportFileButton ----
				selectImportFileButton.setText(bundle.getString("JImportTags.selectImportFileButton.text"));
				selectImportFileButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
				selectImportFileButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						selectImportFileButtonActionPerformed(e);
					}
				});
				contentPanel.add(selectImportFileButton, cc.xy(5, 1));

				//---- label2 ----
				label2.setText(bundle.getString("JImportTags.label2.text"));
				label2.setFont(new Font("Tahoma", Font.PLAIN, 14));
				contentPanel.add(label2, cc.xy(3, 3));

				//---- sheetComboBox ----
				sheetComboBox.setFont(new Font("Tahoma", Font.PLAIN, 14));
				contentPanel.add(sheetComboBox, cc.xy(5, 3));

				//---- label3 ----
				label3.setText(bundle.getString("JImportTags.label3.text"));
				label3.setFont(new Font("Tahoma", Font.BOLD, 14));
				contentPanel.add(label3, cc.xy(3, 5));
				contentPanel.add(separator1, cc.xy(5, 5));

				//---- label4 ----
				label4.setText(bundle.getString("JImportTags.label4.text"));
				label4.setFont(new Font("Tahoma", Font.PLAIN, 14));
				contentPanel.add(label4, cc.xy(3, 7));

				//---- bibComboBox ----
				bibComboBox.setFont(new Font("Tahoma", Font.PLAIN, 14));
				contentPanel.add(bibComboBox, cc.xy(5, 7));

				//---- label5 ----
				label5.setText(bundle.getString("JImportTags.label5.text"));
				label5.setFont(new Font("Tahoma", Font.PLAIN, 14));
				contentPanel.add(label5, cc.xy(3, 9));

				//---- epcComboBox ----
				epcComboBox.setFont(new Font("Tahoma", Font.PLAIN, 14));
				contentPanel.add(epcComboBox, cc.xy(5, 9));

				//---- importButton ----
				importButton.setText(bundle.getString("JImportTags.importButton.text"));
				importButton.setFont(new Font("Tahoma", Font.PLAIN, 14));
				importButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						importButtonActionPerformed(e);
					}
				});
				contentPanel.add(importButton, cc.xywh(3, 11, 3, 1));
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
	private JLabel label1;
	private JButton selectImportFileButton;
	private JLabel label2;
	private JComboBox sheetComboBox;
	private JLabel label3;
	private JSeparator separator1;
	private JLabel label4;
	private JComboBox bibComboBox;
	private JLabel label5;
	private JComboBox epcComboBox;
	private JButton importButton;
	private JPanel buttonBar;
	private JButton closeButton;
	// JFormDesigner - End of variables declaration //GEN-END:variables
}
