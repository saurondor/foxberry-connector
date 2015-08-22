/*
 * Created by JFormDesigner on Sat Aug 22 09:24:33 CDT 2015
 */

package com.tiempometa.muestradatos;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;

/**
 * @author Gerardo Esteban Tasistro Giubetic
 */
public class JCountTags extends JDialog {
	public JCountTags(Frame owner) {
		super(owner);
		initComponents();
	}

	public JCountTags(Dialog owner) {
		super(owner);
		initComponents();
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		ResourceBundle bundle = ResourceBundle.getBundle("com.tiempometa.muestradatos.muestradatos");
		dialogPane = new JPanel();
		contentPanel = new JPanel();
		countButton = new JButton();
		clearCountButton = new JButton();
		label1 = new JLabel();
		eventTagCountLabel = new JLabel();
		label2 = new JLabel();
		noEventTagCountLabel = new JLabel();
		label3 = new JLabel();
		totalTagCountLabel = new JLabel();
		exportCountButton = new JButton();
		countTidCheckBox = new JCheckBox();
		buttonBar = new JPanel();
		okButton = new JButton();
		CellConstraints cc = new CellConstraints();

		//======== this ========
		setTitle(bundle.getString("JCountTags.this.title"));
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
						FormFactory.DEFAULT_COLSPEC
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
						FormFactory.DEFAULT_ROWSPEC
					}));

				//---- countButton ----
				countButton.setText(bundle.getString("JCountTags.countButton.text"));
				contentPanel.add(countButton, cc.xy(1, 1));

				//---- clearCountButton ----
				clearCountButton.setText(bundle.getString("JCountTags.clearCountButton.text"));
				contentPanel.add(clearCountButton, cc.xy(1, 3));

				//---- label1 ----
				label1.setText(bundle.getString("JCountTags.label1.text"));
				contentPanel.add(label1, cc.xy(1, 5));

				//---- eventTagCountLabel ----
				eventTagCountLabel.setText(bundle.getString("JCountTags.eventTagCountLabel.text"));
				contentPanel.add(eventTagCountLabel, cc.xy(3, 5));

				//---- label2 ----
				label2.setText(bundle.getString("JCountTags.label2.text"));
				contentPanel.add(label2, cc.xy(1, 7));

				//---- noEventTagCountLabel ----
				noEventTagCountLabel.setText(bundle.getString("JCountTags.noEventTagCountLabel.text"));
				contentPanel.add(noEventTagCountLabel, cc.xy(3, 7));

				//---- label3 ----
				label3.setText(bundle.getString("JCountTags.label3.text"));
				contentPanel.add(label3, cc.xy(1, 9));

				//---- totalTagCountLabel ----
				totalTagCountLabel.setText(bundle.getString("JCountTags.totalTagCountLabel.text"));
				contentPanel.add(totalTagCountLabel, cc.xy(3, 9));

				//---- exportCountButton ----
				exportCountButton.setText(bundle.getString("JCountTags.exportCountButton.text"));
				contentPanel.add(exportCountButton, cc.xy(1, 11));

				//---- countTidCheckBox ----
				countTidCheckBox.setText(bundle.getString("JCountTags.countTidCheckBox.text"));
				contentPanel.add(countTidCheckBox, cc.xy(1, 13));
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
	private JButton countButton;
	private JButton clearCountButton;
	private JLabel label1;
	private JLabel eventTagCountLabel;
	private JLabel label2;
	private JLabel noEventTagCountLabel;
	private JLabel label3;
	private JLabel totalTagCountLabel;
	private JButton exportCountButton;
	private JCheckBox countTidCheckBox;
	private JPanel buttonBar;
	private JButton okButton;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
