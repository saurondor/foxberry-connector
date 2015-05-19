/*
 * Created by JFormDesigner on Tue May 19 08:27:31 CDT 2015
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
public class JConfigDialog extends JDialog {
	public JConfigDialog(Frame owner) {
		super(owner);
		initComponents();
	}

	public JConfigDialog(Dialog owner) {
		super(owner);
		initComponents();
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		ResourceBundle bundle = ResourceBundle.getBundle("com.tiempometa.muestradatos.muestradatos");
		dialogPane = new JPanel();
		contentPanel = new JPanel();
		label12 = new JLabel();
		comboBox4 = new JComboBox();
		label7 = new JLabel();
		separator2 = new JSeparator();
		label8 = new JLabel();
		textField2 = new JTextField();
		label9 = new JLabel();
		comboBox3 = new JComboBox();
		button4 = new JButton();
		label3 = new JLabel();
		separator1 = new JSeparator();
		label2 = new JLabel();
		comboBox2 = new JComboBox();
		button1 = new JButton();
		label1 = new JLabel();
		comboBox1 = new JComboBox();
		button2 = new JButton();
		label4 = new JLabel();
		separator3 = new JSeparator();
		label5 = new JLabel();
		textField1 = new JTextField();
		button3 = new JButton();
		label6 = new JLabel();
		label10 = new JLabel();
		label11 = new JLabel();
		buttonBar = new JPanel();
		okButton = new JButton();
		cancelButton = new JButton();
		CellConstraints cc = new CellConstraints();

		//======== this ========
		setTitle(bundle.getString("JConfigDialog.this.title"));
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
						new ColumnSpec(Sizes.dluX(115)),
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

				//---- label12 ----
				label12.setText(bundle.getString("JConfigDialog.label12.text"));
				label12.setFont(new Font("Tahoma", Font.PLAIN, 18));
				contentPanel.add(label12, cc.xy(1, 1));
				contentPanel.add(comboBox4, cc.xy(3, 1));

				//---- label7 ----
				label7.setText(bundle.getString("JConfigDialog.label7.text"));
				label7.setFont(new Font("Tahoma", Font.PLAIN, 18));
				contentPanel.add(label7, cc.xy(1, 3));
				contentPanel.add(separator2, cc.xywh(3, 3, 3, 1));

				//---- label8 ----
				label8.setText(bundle.getString("JConfigDialog.label8.text"));
				contentPanel.add(label8, cc.xy(1, 5));
				contentPanel.add(textField2, cc.xy(3, 5));

				//---- label9 ----
				label9.setText(bundle.getString("JConfigDialog.label9.text"));
				contentPanel.add(label9, cc.xy(1, 7));
				contentPanel.add(comboBox3, cc.xy(3, 7));

				//---- button4 ----
				button4.setText(bundle.getString("JConfigDialog.button4.text"));
				contentPanel.add(button4, cc.xy(5, 7));

				//---- label3 ----
				label3.setText(bundle.getString("JConfigDialog.label3.text"));
				label3.setFont(new Font("Tahoma", Font.PLAIN, 18));
				contentPanel.add(label3, cc.xy(1, 9));
				contentPanel.add(separator1, cc.xywh(3, 9, 3, 1));

				//---- label2 ----
				label2.setText(bundle.getString("JConfigDialog.label2.text"));
				contentPanel.add(label2, cc.xy(1, 11));
				contentPanel.add(comboBox2, cc.xy(3, 11));

				//---- button1 ----
				button1.setText(bundle.getString("JConfigDialog.button1.text"));
				contentPanel.add(button1, cc.xy(5, 11));

				//---- label1 ----
				label1.setText(bundle.getString("JConfigDialog.label1.text"));
				contentPanel.add(label1, cc.xy(1, 13));
				contentPanel.add(comboBox1, cc.xy(3, 13));

				//---- button2 ----
				button2.setText(bundle.getString("JConfigDialog.button2.text"));
				contentPanel.add(button2, cc.xy(5, 13));

				//---- label4 ----
				label4.setText(bundle.getString("JConfigDialog.label4.text"));
				label4.setFont(new Font("Tahoma", Font.PLAIN, 18));
				contentPanel.add(label4, cc.xy(1, 15));
				contentPanel.add(separator3, cc.xywh(3, 15, 3, 1));

				//---- label5 ----
				label5.setText(bundle.getString("JConfigDialog.label5.text"));
				contentPanel.add(label5, cc.xy(1, 17));
				contentPanel.add(textField1, cc.xy(3, 17));

				//---- button3 ----
				button3.setText(bundle.getString("JConfigDialog.button3.text"));
				contentPanel.add(button3, cc.xy(5, 17));

				//---- label6 ----
				label6.setText(bundle.getString("JConfigDialog.label6.text"));
				contentPanel.add(label6, cc.xy(1, 19));

				//---- label10 ----
				label10.setText(bundle.getString("JConfigDialog.label10.text"));
				contentPanel.add(label10, cc.xy(1, 21));

				//---- label11 ----
				label11.setText(bundle.getString("JConfigDialog.label11.text"));
				contentPanel.add(label11, cc.xy(1, 23));
			}
			dialogPane.add(contentPanel, BorderLayout.CENTER);

			//======== buttonBar ========
			{
				buttonBar.setBorder(Borders.BUTTON_BAR_GAP_BORDER);
				buttonBar.setLayout(new FormLayout(
					new ColumnSpec[] {
						FormFactory.GLUE_COLSPEC,
						FormFactory.BUTTON_COLSPEC,
						FormFactory.RELATED_GAP_COLSPEC,
						FormFactory.BUTTON_COLSPEC
					},
					RowSpec.decodeSpecs("pref")));

				//---- okButton ----
				okButton.setText("OK");
				buttonBar.add(okButton, cc.xy(2, 1));

				//---- cancelButton ----
				cancelButton.setText("Cancel");
				buttonBar.add(cancelButton, cc.xy(4, 1));
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
	private JLabel label12;
	private JComboBox comboBox4;
	private JLabel label7;
	private JSeparator separator2;
	private JLabel label8;
	private JTextField textField2;
	private JLabel label9;
	private JComboBox comboBox3;
	private JButton button4;
	private JLabel label3;
	private JSeparator separator1;
	private JLabel label2;
	private JComboBox comboBox2;
	private JButton button1;
	private JLabel label1;
	private JComboBox comboBox1;
	private JButton button2;
	private JLabel label4;
	private JSeparator separator3;
	private JLabel label5;
	private JTextField textField1;
	private JButton button3;
	private JLabel label6;
	private JLabel label10;
	private JLabel label11;
	private JPanel buttonBar;
	private JButton okButton;
	private JButton cancelButton;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
}
