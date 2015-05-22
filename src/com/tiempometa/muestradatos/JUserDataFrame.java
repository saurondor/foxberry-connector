/*
 * Created by JFormDesigner on Wed May 20 15:38:17 CDT 2015
 */

package com.tiempometa.muestradatos;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.swing.*;

import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import com.tiempometa.timing.models.Categories;
import com.tiempometa.timing.models.ParticipantRegistration;
import com.tiempometa.timing.models.Participants;
import com.tiempometa.timing.models.Registration;
import com.tiempometa.timing.models.Rfid;

/**
 * @author Gerardo Esteban Tasistro Giubetic
 */
public class JUserDataFrame extends JFrame implements UserDisplayPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3467103545852213612L;
	private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

	public JUserDataFrame() {
		initComponents();
		clearData();
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY
		// //GEN-BEGIN:initComponents
		ResourceBundle bundle = ResourceBundle
				.getBundle("com.tiempometa.muestradatos.muestradatos");
		bibLabel = new JLabel();
		label1 = new JLabel();
		nameLabel = new JLabel();
		label2 = new JLabel();
		birthdateLabel = new JLabel();
		label3 = new JLabel();
		genderLabel = new JLabel();
		label4 = new JLabel();
		distanceLabel = new JLabel();
		label5 = new JLabel();
		categoryLabel = new JLabel();
		label6 = new JLabel();
		colorLabel = new JLabel();
		CellConstraints cc = new CellConstraints();

		// ======== this ========
		setTitle(bundle.getString("JUserDataFrame.this.title"));
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		Container contentPane = getContentPane();
		contentPane.setLayout(new FormLayout(new ColumnSpec[] {
				new ColumnSpec(Sizes.dluX(50)),
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				new ColumnSpec(Sizes.dluX(221)),
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				new ColumnSpec(Sizes.dluX(176)),
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				new ColumnSpec(Sizes.dluX(228)) }, new RowSpec[] {
				new RowSpec(Sizes.dluY(23)), FormFactory.LINE_GAP_ROWSPEC,
				new RowSpec(Sizes.dluY(179)), FormFactory.LINE_GAP_ROWSPEC,
				new RowSpec(Sizes.dluY(13)), FormFactory.LINE_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC, FormFactory.LINE_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC, FormFactory.LINE_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC, FormFactory.LINE_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC, FormFactory.LINE_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC, FormFactory.LINE_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC }));

		// ---- bibLabel ----
		bibLabel.setText(bundle.getString("JUserDataFrame.bibLabel.text"));
		bibLabel.setFont(new Font("Tahoma", Font.PLAIN, 120));
		bibLabel.setForeground(Color.red);
		contentPane.add(bibLabel, cc.xy(9, 3));

		// ---- label1 ----
		label1.setText(bundle.getString("JUserDataFrame.label1.text"));
		label1.setFont(new Font("Tahoma", Font.BOLD, 48));
		contentPane.add(label1, cc.xy(3, 7));

		// ---- nameLabel ----
		nameLabel.setText(bundle.getString("JUserDataFrame.nameLabel.text"));
		nameLabel.setFont(new Font("Tahoma", Font.PLAIN, 48));
		contentPane.add(nameLabel, cc.xywh(5, 7, 3, 1));

		// ---- label2 ----
		label2.setText(bundle.getString("JUserDataFrame.label2.text"));
		label2.setFont(new Font("Tahoma", Font.BOLD, 48));
		contentPane.add(label2, cc.xy(3, 9));

		// ---- birthdateLabel ----
		birthdateLabel.setText(bundle
				.getString("JUserDataFrame.birthdateLabel.text"));
		birthdateLabel.setFont(new Font("Tahoma", Font.PLAIN, 48));
		contentPane.add(birthdateLabel, cc.xy(5, 9));

		// ---- label3 ----
		label3.setText(bundle.getString("JUserDataFrame.label3.text"));
		label3.setFont(new Font("Tahoma", Font.BOLD, 48));
		contentPane.add(label3, cc.xy(3, 11));

		// ---- genderLabel ----
		genderLabel
				.setText(bundle.getString("JUserDataFrame.genderLabel.text"));
		genderLabel.setFont(new Font("Tahoma", Font.PLAIN, 48));
		contentPane.add(genderLabel, cc.xywh(5, 11, 3, 1));

		// ---- label4 ----
		label4.setText(bundle.getString("JUserDataFrame.label4.text"));
		label4.setFont(new Font("Tahoma", Font.BOLD, 48));
		contentPane.add(label4, cc.xy(3, 13));

		// ---- distanceLabel ----
		distanceLabel.setText(bundle
				.getString("JUserDataFrame.distanceLabel.text"));
		distanceLabel.setFont(new Font("Tahoma", Font.PLAIN, 48));
		contentPane.add(distanceLabel, cc.xywh(5, 13, 3, 1));

		// ---- label5 ----
		label5.setText(bundle.getString("JUserDataFrame.label5.text"));
		label5.setFont(new Font("Tahoma", Font.BOLD, 48));
		contentPane.add(label5, cc.xy(3, 15));

		// ---- categoryLabel ----
		categoryLabel.setText(bundle
				.getString("JUserDataFrame.categoryLabel.text"));
		categoryLabel.setFont(new Font("Tahoma", Font.PLAIN, 48));
		contentPane.add(categoryLabel, cc.xywh(5, 15, 3, 1));

		// ---- label6 ----
		label6.setText(bundle.getString("JUserDataFrame.label6.text"));
		label6.setFont(new Font("Tahoma", Font.BOLD, 48));
		contentPane.add(label6, cc.xy(3, 17));

		// ---- colorLabel ----
		colorLabel.setText(bundle.getString("JUserDataFrame.colorLabel.text"));
		colorLabel.setFont(new Font("Tahoma", Font.PLAIN, 48));
		contentPane.add(colorLabel, cc.xywh(5, 17, 3, 1));
		setSize(1200, 800);
		setLocationRelativeTo(getOwner());
		// JFormDesigner - End of component initialization
		// //GEN-END:initComponents
	}

	@Override
	public ParticipantRegistration getParticipant() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setParticipant(Participants participant,
			Registration registration, Rfid rfid, Categories category) {
		clearData();
		nameLabel
				.setText(participant.getFirstName() + " "
						+ participant.getLastName() + " "
						+ participant.getMiddleName());
		birthdateLabel.setText(dateFormat.format(participant.getBirthDate()));
		genderLabel.setText("");
		distanceLabel.setText("");
		categoryLabel.setText(category.getTitle());
		colorLabel.setText(category.getExtra1());
		

	}

	private void clearData() {
		nameLabel.setText("");
		birthdateLabel.setText("");
		genderLabel.setText("");
		distanceLabel.setText("");
		categoryLabel.setText("");
		colorLabel.setText("");

	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY
	// //GEN-BEGIN:variables
	private JLabel bibLabel;
	private JLabel label1;
	private JLabel nameLabel;
	private JLabel label2;
	private JLabel birthdateLabel;
	private JLabel label3;
	private JLabel genderLabel;
	private JLabel label4;
	private JLabel distanceLabel;
	private JLabel label5;
	private JLabel categoryLabel;
	private JLabel label6;
	private JLabel colorLabel;
	// JFormDesigner - End of variables declaration //GEN-END:variables
}
