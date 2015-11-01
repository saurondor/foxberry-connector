/*
 * Created by JFormDesigner on Fri May 22 12:04:41 CDT 2015
 */
/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package com.tiempometa.muestradatos;

import java.awt.*;
import java.awt.event.*;
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
public class JAboutUs extends JDialog {
	public JAboutUs(Frame owner) {
		super(owner);
		initComponents();
	}

	public JAboutUs(Dialog owner) {
		super(owner);
		initComponents();
	}

	private void okButtonActionPerformed(ActionEvent e) {
		this.dispose();
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		ResourceBundle bundle = ResourceBundle.getBundle("com.tiempometa.muestradatos.muestradatos");
		dialogPane = new JPanel();
		contentPanel = new JPanel();
		label4 = new JLabel();
		label1 = new JLabel();
		label2 = new JLabel();
		label3 = new JLabel();
		buttonBar = new JPanel();
		okButton = new JButton();
		CellConstraints cc = new CellConstraints();

		//======== this ========
		setTitle(bundle.getString("JAboutUs.this.title"));
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
						new ColumnSpec(Sizes.DLUX5),
						FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
						FormFactory.DEFAULT_COLSPEC,
						FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
						FormFactory.DEFAULT_COLSPEC,
						FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
						FormFactory.DEFAULT_COLSPEC
					},
					new RowSpec[] {
						new RowSpec(Sizes.dluY(26)),
						FormFactory.LINE_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.LINE_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC,
						FormFactory.LINE_GAP_ROWSPEC,
						FormFactory.DEFAULT_ROWSPEC
					}));

				//---- label4 ----
				label4.setIcon(new ImageIcon(getClass().getResource("/com/tiempometa/resources/tiempometa_icon_large_alpha.png")));
				contentPanel.add(label4, cc.xy(3, 3));

				//---- label1 ----
				label1.setText(bundle.getString("JAboutUs.label1.text"));
				label1.setFont(new Font("Tahoma", Font.BOLD, 24));
				contentPanel.add(label1, cc.xy(5, 3));

				//---- label2 ----
				label2.setText(bundle.getString("JAboutUs.label2.text"));
				contentPanel.add(label2, cc.xy(5, 5));

				//---- label3 ----
				label3.setText(bundle.getString("JAboutUs.label3.text"));
				contentPanel.add(label3, cc.xy(5, 7));
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
				okButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						okButtonActionPerformed(e);
					}
				});
				buttonBar.add(okButton, cc.xy(2, 1));
			}
			dialogPane.add(buttonBar, BorderLayout.SOUTH);
		}
		contentPane.add(dialogPane, BorderLayout.CENTER);
		setSize(690, 300);
		setLocationRelativeTo(getOwner());
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	private JPanel dialogPane;
	private JPanel contentPanel;
	private JLabel label4;
	private JLabel label1;
	private JLabel label2;
	private JLabel label3;
	private JPanel buttonBar;
	private JButton okButton;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
