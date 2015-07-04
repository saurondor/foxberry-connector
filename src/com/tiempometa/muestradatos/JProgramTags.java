/*
 * Created by JFormDesigner on Sat May 30 17:22:50 CDT 2015
 */
/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package com.tiempometa.muestradatos;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;

/**
 * @author Gerardo Tasistro gtasistro@tiempometa.com
 * Copyright 2015 Gerardo Tasistro
 * Licensed under the Mozilla Public License, v. 2.0
 * 
 */
public class JProgramTags extends JFrame {
	public JProgramTags() {
		initComponents();
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		ResourceBundle bundle = ResourceBundle.getBundle("com.tiempometa.muestradatos.muestradatos");
		dialogPane = new JPanel();
		contentPanel = new JPanel();
		label1 = new JLabel();
		textField3 = new JTextField();
		button1 = new JButton();
		checkBox1 = new JCheckBox();
		label3 = new JLabel();
		textField1 = new JTextField();
		label4 = new JLabel();
		textField2 = new JTextField();
		scrollPane1 = new JScrollPane();
		table1 = new JTable();
		buttonBar = new JPanel();
		okButton = new JButton();
		CellConstraints cc = new CellConstraints();

		//======== this ========
		setTitle(bundle.getString("JProgramTags.this.title"));
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
						new ColumnSpec(Sizes.dluX(43)),
						FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
						new ColumnSpec(Sizes.dluX(103))
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
				label1.setText(bundle.getString("JProgramTags.label1.text"));
				contentPanel.add(label1, cc.xy(3, 3));
				contentPanel.add(textField3, cc.xy(5, 3));

				//---- button1 ----
				button1.setText(bundle.getString("JProgramTags.button1.text"));
				contentPanel.add(button1, cc.xy(7, 3));

				//---- checkBox1 ----
				checkBox1.setText(bundle.getString("JProgramTags.checkBox1.text"));
				contentPanel.add(checkBox1, cc.xy(3, 5));

				//---- label3 ----
				label3.setText(bundle.getString("JProgramTags.label3.text"));
				contentPanel.add(label3, cc.xy(3, 7));
				contentPanel.add(textField1, cc.xywh(5, 7, 3, 1));

				//---- label4 ----
				label4.setText(bundle.getString("JProgramTags.label4.text"));
				contentPanel.add(label4, cc.xy(3, 9));
				contentPanel.add(textField2, cc.xywh(5, 9, 3, 1));

				//======== scrollPane1 ========
				{
					scrollPane1.setViewportView(table1);
				}
				contentPanel.add(scrollPane1, cc.xywh(3, 11, 5, 1));
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
	private JTextField textField3;
	private JButton button1;
	private JCheckBox checkBox1;
	private JLabel label3;
	private JTextField textField1;
	private JLabel label4;
	private JTextField textField2;
	private JScrollPane scrollPane1;
	private JTable table1;
	private JPanel buttonBar;
	private JButton okButton;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
