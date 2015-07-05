/*
 * Created by JFormDesigner on Wed May 20 15:38:17 CDT 2015
 */
/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package com.tiempometa.muestradatos;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

import javax.swing.*;

import org.apache.log4j.Logger;

import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import com.thingmagic.Reader;
import com.thingmagic.ReaderException;
import com.tiempometa.timing.dao.CategoriesDao;
import com.tiempometa.timing.dao.ParticipantsDao;
import com.tiempometa.timing.dao.RegistrationDao;
import com.tiempometa.timing.dao.RfidDao;
import com.tiempometa.timing.dao.access.CategoriesDaoImpl;
import com.tiempometa.timing.dao.access.ParticipantsDaoImpl;
import com.tiempometa.timing.dao.access.RegistrationDaoImpl;
import com.tiempometa.timing.dao.access.RfidDaoImpl;
import com.tiempometa.timing.models.Categories;
import com.tiempometa.timing.models.Participants;
import com.tiempometa.timing.models.Registration;
import com.tiempometa.timing.models.Rfid;

/**
 * @author Gerardo Tasistro gtasistro@tiempometa.com
 * Copyright 2015 Gerardo Tasistro
 * Licensed under the Mozilla Public License, v. 2.0
 * 
 */
public class JUserDataFrame extends JFrame implements TagReadListener {
	/**
	 * 
	 */
	private static final Logger logger = Logger.getLogger(JUserDataFrame.class);
	private static final long serialVersionUID = 3467103545852213612L;
	private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	private RfidDao rfidDao = new RfidDaoImpl();
	private RegistrationDao registrationDao = new RegistrationDaoImpl();
	private ParticipantsDao participantDao = new ParticipantsDaoImpl();
	private CategoriesDao categoryDao = new CategoriesDaoImpl();

	public JUserDataFrame() {
		initComponents();
		clearData();
		this.addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent we) {
				int response = JOptionPane
						.showConfirmDialog(
								null,
								"Esta ventana solo se puede cerrar desde el menú de la aplicación",
								"Cerrar Ventana", JOptionPane.WARNING_MESSAGE);
			}
		});
	}

	public void showTagInfo(TagReading reading) {
		Integer chipNumber = null;
		try {
			List<Rfid> rfids = rfidDao.findByRfid(reading.getTid());
			if (rfids.size() == 1) {
				try {
					ReaderContext.setRedOn();
				} catch (ReaderException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Rfid rfid = rfids.get(0);
				chipNumber = rfid.getChipNumber();
				Registration registration = registrationDao
						.findByChipNumber(chipNumber);
				if (registration == null) {
					clearData();
					bibLabel.setText(rfid.getBib());
				} else {
					Participants participant = participantDao
							.findById(registration.getParticipantId());
					// Categories category = categoryDao.findById();
					Categories category = null;
					if (participant == null) {
						clearData();
					} else {
						bibLabel.setText(rfid.getBib());
						nameLabel.setText(participant.getFirstName() + " "
								+ participant.getLastName() + " "
								+ participant.getMiddleName());
						birthdateLabel.setText(dateFormat.format(participant
								.getBirthDate()));
						genderLabel.setText("");
						distanceLabel.setText("");
						// categoryLabel.setText(category.getTitle());
						// colorLabel.setText(category.getExtra1());

					}
				}
				try {
					Thread.sleep(1000);
					ReaderContext.setRedOff();
				} catch (ReaderException | InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				clearData();
				noData();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY
		// //GEN-BEGIN:initComponents
		ResourceBundle bundle = ResourceBundle.getBundle("com.tiempometa.muestradatos.muestradatos");
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

		//======== this ========
		setTitle(bundle.getString("JUserDataFrame.this.title"));
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		setUndecorated(true);
		setIconImage(new ImageIcon(getClass().getResource("/com/tiempometa/resources/stopwatch_small.png")).getImage());
		Container contentPane = getContentPane();
		contentPane.setLayout(new FormLayout(
			new ColumnSpec[] {
				new ColumnSpec(ColumnSpec.FILL, Sizes.dluX(50), FormSpec.DEFAULT_GROW),
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				new ColumnSpec(Sizes.dluX(74)),
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				new ColumnSpec(ColumnSpec.FILL, Sizes.dluX(176), FormSpec.DEFAULT_GROW),
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				new ColumnSpec(Sizes.dluX(228)),
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				new ColumnSpec(Sizes.dluX(21))
			},
			new RowSpec[] {
				new RowSpec(Sizes.dluY(23)),
				FormFactory.LINE_GAP_ROWSPEC,
				new RowSpec(Sizes.dluY(179)),
				FormFactory.LINE_GAP_ROWSPEC,
				new RowSpec(Sizes.dluY(13)),
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

		//---- bibLabel ----
		bibLabel.setText(bundle.getString("JUserDataFrame.bibLabel.text"));
		bibLabel.setFont(new Font("Tahoma", Font.PLAIN, 120));
		bibLabel.setForeground(Color.red);
		contentPane.add(bibLabel, cc.xy(9, 3));

		//---- label1 ----
		label1.setText(bundle.getString("JUserDataFrame.label1.text"));
		label1.setFont(new Font("Tahoma", Font.BOLD, 48));
		contentPane.add(label1, cc.xy(3, 7));

		//---- nameLabel ----
		nameLabel.setText(bundle.getString("JUserDataFrame.nameLabel.text"));
		nameLabel.setFont(new Font("Tahoma", Font.PLAIN, 48));
		contentPane.add(nameLabel, cc.xywh(5, 7, 7, 1));

		//---- label2 ----
		label2.setText(bundle.getString("JUserDataFrame.label2.text"));
		label2.setFont(new Font("Tahoma", Font.BOLD, 48));
		contentPane.add(label2, cc.xy(3, 9));

		//---- birthdateLabel ----
		birthdateLabel.setText(bundle.getString("JUserDataFrame.birthdateLabel.text"));
		birthdateLabel.setFont(new Font("Tahoma", Font.PLAIN, 48));
		contentPane.add(birthdateLabel, cc.xywh(5, 9, 5, 1));

		//---- label3 ----
		label3.setText(bundle.getString("JUserDataFrame.label3.text"));
		label3.setFont(new Font("Tahoma", Font.BOLD, 48));
		contentPane.add(label3, cc.xy(3, 11));

		//---- genderLabel ----
		genderLabel.setText(bundle.getString("JUserDataFrame.genderLabel.text"));
		genderLabel.setFont(new Font("Tahoma", Font.PLAIN, 48));
		contentPane.add(genderLabel, cc.xywh(5, 11, 3, 1));

		//---- label4 ----
		label4.setText(bundle.getString("JUserDataFrame.label4.text"));
		label4.setFont(new Font("Tahoma", Font.BOLD, 48));
		contentPane.add(label4, cc.xy(3, 13));

		//---- distanceLabel ----
		distanceLabel.setText(bundle.getString("JUserDataFrame.distanceLabel.text"));
		distanceLabel.setFont(new Font("Tahoma", Font.PLAIN, 48));
		contentPane.add(distanceLabel, cc.xywh(5, 13, 3, 1));

		//---- label5 ----
		label5.setText(bundle.getString("JUserDataFrame.label5.text"));
		label5.setFont(new Font("Tahoma", Font.BOLD, 48));
		contentPane.add(label5, cc.xy(3, 15));

		//---- categoryLabel ----
		categoryLabel.setText(bundle.getString("JUserDataFrame.categoryLabel.text"));
		categoryLabel.setFont(new Font("Tahoma", Font.PLAIN, 48));
		contentPane.add(categoryLabel, cc.xywh(5, 15, 5, 1));

		//---- label6 ----
		label6.setText(bundle.getString("JUserDataFrame.label6.text"));
		label6.setFont(new Font("Tahoma", Font.BOLD, 48));
		contentPane.add(label6, cc.xy(3, 17));

		//---- colorLabel ----
		colorLabel.setText(bundle.getString("JUserDataFrame.colorLabel.text"));
		colorLabel.setFont(new Font("Tahoma", Font.PLAIN, 48));
		contentPane.add(colorLabel, cc.xywh(5, 17, 3, 1));
		setSize(1380, 800);
		setLocationRelativeTo(getOwner());
		// //GEN-END:initComponents
	}

	private void clearData() {
		bibLabel.setText("");
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

	@Override
	public void handleReadings(List<TagReading> readings) {
		if (readings.size() > 0) {
			if (readings.size() == 1) {
				for (TagReading tagReading : readings) {
					if (tagReading.isKeepAlive()) {
						logger.debug("Keep alive tag read");
					} else {
						if (tagReading.getTid() == null) {
							try {
								tagReading.setTid(ReaderContext.readTid(
										tagReading.getEpc(), 12));
								if (logger.isDebugEnabled()) {
									logger.debug("Got tag "
											+ tagReading.getEpc() + " - "
											+ tagReading.getTid());
								}
							} catch (ReaderException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						showTagInfo(tagReading);
					}
				}
			} else {
			}
		} else {
		}

	}

	private void noData() {
		nameLabel.setText("No Data");

	}
}
