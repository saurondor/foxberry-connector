/*
 * Created by JFormDesigner on Tue May 19 08:24:05 CDT 2015
 */

package com.tiempometa.muestradatos;

import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.*;
import java.util.List;

import javax.swing.*;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.log4j.Logger;
import org.llrp.ltk.generated.parameters.GPOWriteData;

import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import com.sun.org.apache.bcel.internal.generic.CPInstruction;
import com.thingmagic.Gen2;
import com.thingmagic.Reader;
import com.thingmagic.ReaderCodeException;
import com.thingmagic.ReaderException;
import com.thingmagic.TagData;
import com.thingmagic.Reader.GpioPin;
import com.tiempometa.timing.dao.CategoriesDao;
import com.tiempometa.timing.dao.ParticipantRegistrationDao;
import com.tiempometa.timing.dao.ParticipantsDao;
import com.tiempometa.timing.dao.RegistrationDao;
import com.tiempometa.timing.dao.RfidDao;
import com.tiempometa.timing.dao.access.CategoriesDaoImpl;
import com.tiempometa.timing.dao.access.ParticipantRegistrationDaoImpl;
import com.tiempometa.timing.dao.access.ParticipantsDaoImpl;
import com.tiempometa.timing.dao.access.RegistrationDaoImpl;
import com.tiempometa.timing.dao.access.RfidDaoImpl;
import com.tiempometa.timing.models.Categories;
import com.tiempometa.timing.models.Participants;
import com.tiempometa.timing.models.Registration;
import com.tiempometa.timing.models.Rfid;

/**
 * @author Gerardo Esteban Tasistro Giubetic
 */
public class JMuestraDatos extends JFrame implements TagReadListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2902573870520237847L;
	private static final Logger logger = Logger.getLogger(JMuestraDatos.class);
	private JUserDataFrame userDataFrame = new JUserDataFrame();

	private Integer lastTagCount = 0;

	public JMuestraDatos() {
		initComponents();
		this.addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent we) {
				int response = JOptionPane.showConfirmDialog(null,
						"¿Seguro que deseas cerrar la aplicación?",
						"Cerrar Programa", JOptionPane.WARNING_MESSAGE);
				if (response == JOptionPane.YES_OPTION) {
					System.exit(0);
				}
			}
		});
	}

	/**
	 * Application entry point
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			// Set System L&F
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (UnsupportedLookAndFeelException e) {
			// handle exception
		} catch (ClassNotFoundException e) {
			// handle exception
		} catch (InstantiationException e) {
			// handle exception
		} catch (IllegalAccessException e) {
			// handle exception
		}

		JMuestraDatos reader = new JMuestraDatos();
		reader.setVisible(true);
	}

	private void verifyDataMenuItemActionPerformed(ActionEvent e) {
		if (userDataFrame.isVisible()) {
			userDataFrame.setVisible(false);
			ReaderContext.removeReadingListener(userDataFrame);
			ReaderContext.stopReading();
		} else {
			try {
				ReaderContext.startReading();
				ReaderContext.addReadingListener(userDataFrame);
				userDataFrame.setVisible(true);
			} catch (ReaderException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	private void programTagsMenuItemActionPerformed(ActionEvent e) {
	}

	private void configMenuItemActionPerformed(ActionEvent e) {
		JConfigDialog configDialog = new JConfigDialog(this);
		configDialog.setVisible(true);
	}

	private void menuItem2ActionPerformed(ActionEvent e) {
		this.dispose();
	}

	private void aboutUsMenuItemActionPerformed(ActionEvent e) {
		JAboutUs aboutUs = new JAboutUs(this);
		aboutUs.setVisible(true);
	}

	private void readTagsMenuItemActionPerformed(ActionEvent e) {
		JReadTags readTag = new JReadTags(this, true);
		readTag.setVisible(true);
		// mode = MODE_READ;
		// chipNumber = 1;
		// try {
		// ReaderContext.startReading();
		// } catch (ReaderException e1) {
		// // TODO Auto-generated catch block
		// e1.printStackTrace();
		// }
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY
		// //GEN-BEGIN:initComponents
		ResourceBundle bundle = ResourceBundle.getBundle("com.tiempometa.muestradatos.muestradatos");
		menuBar1 = new JMenuBar();
		menu1 = new JMenu();
		configMenuItem = new JMenuItem();
		exitMenuItem = new JMenuItem();
		menu2 = new JMenu();
		verifyDataMenuItem = new JMenuItem();
		readTagsMenuItem = new JMenuItem();
		programTagsMenuItem = new JMenuItem();
		menu3 = new JMenu();
		aboutUsMenuItem = new JMenuItem();
		label8 = new JLabel();
		label1 = new JLabel();
		label9 = new JLabel();
		label3 = new JLabel();
		label4 = new JLabel();
		label2 = new JLabel();
		label5 = new JLabel();
		label6 = new JLabel();
		label7 = new JLabel();
		CellConstraints cc = new CellConstraints();

		//======== this ========
		setTitle(bundle.getString("JMuestraDatos.this.title"));
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		Container contentPane = getContentPane();
		contentPane.setLayout(new FormLayout(
			new ColumnSpec[] {
				new ColumnSpec(Sizes.dluX(18)),
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				new ColumnSpec(Sizes.dluX(160))
			},
			new RowSpec[] {
				new RowSpec(Sizes.dluY(27)),
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

		//======== menuBar1 ========
		{

			//======== menu1 ========
			{
				menu1.setText(bundle.getString("JMuestraDatos.menu1.text"));

				//---- configMenuItem ----
				configMenuItem.setText(bundle.getString("JMuestraDatos.configMenuItem.text"));
				configMenuItem.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						configMenuItemActionPerformed(e);
					}
				});
				menu1.add(configMenuItem);
				menu1.addSeparator();

				//---- exitMenuItem ----
				exitMenuItem.setText(bundle.getString("JMuestraDatos.exitMenuItem.text"));
				exitMenuItem.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						menuItem2ActionPerformed(e);
					}
				});
				menu1.add(exitMenuItem);
			}
			menuBar1.add(menu1);

			//======== menu2 ========
			{
				menu2.setText(bundle.getString("JMuestraDatos.menu2.text"));

				//---- verifyDataMenuItem ----
				verifyDataMenuItem.setText(bundle.getString("JMuestraDatos.verifyDataMenuItem.text"));
				verifyDataMenuItem.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						verifyDataMenuItemActionPerformed(e);
					}
				});
				menu2.add(verifyDataMenuItem);

				//---- readTagsMenuItem ----
				readTagsMenuItem.setText(bundle.getString("JMuestraDatos.readTagsMenuItem.text"));
				readTagsMenuItem.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						readTagsMenuItemActionPerformed(e);
					}
				});
				menu2.add(readTagsMenuItem);

				//---- programTagsMenuItem ----
				programTagsMenuItem.setText(bundle.getString("JMuestraDatos.programTagsMenuItem.text"));
				programTagsMenuItem.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						programTagsMenuItemActionPerformed(e);
					}
				});
				menu2.add(programTagsMenuItem);
			}
			menuBar1.add(menu2);

			//======== menu3 ========
			{
				menu3.setText(bundle.getString("JMuestraDatos.menu3.text"));

				//---- aboutUsMenuItem ----
				aboutUsMenuItem.setText(bundle.getString("JMuestraDatos.aboutUsMenuItem.text"));
				aboutUsMenuItem.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						aboutUsMenuItemActionPerformed(e);
					}
				});
				menu3.add(aboutUsMenuItem);
			}
			menuBar1.add(menu3);
		}
		setJMenuBar(menuBar1);

		//---- label8 ----
		label8.setText(bundle.getString("JMuestraDatos.label8.text"));
		label8.setFont(new Font("Tahoma", Font.PLAIN, 16));
		contentPane.add(label8, cc.xy(3, 3));

		//---- label1 ----
		label1.setText(bundle.getString("JMuestraDatos.label1.text"));
		contentPane.add(label1, cc.xy(3, 5));

		//---- label9 ----
		label9.setText(bundle.getString("JMuestraDatos.label9.text"));
		label9.setFont(new Font("Tahoma", Font.PLAIN, 16));
		contentPane.add(label9, cc.xy(3, 7));

		//---- label3 ----
		label3.setText(bundle.getString("JMuestraDatos.label3.text"));
		contentPane.add(label3, cc.xy(3, 9));

		//---- label4 ----
		label4.setText(bundle.getString("JMuestraDatos.label4.text"));
		contentPane.add(label4, cc.xy(3, 11));

		//---- label2 ----
		label2.setText(bundle.getString("JMuestraDatos.label2.text"));
		contentPane.add(label2, cc.xy(3, 13));

		//---- label5 ----
		label5.setText(bundle.getString("JMuestraDatos.label5.text"));
		contentPane.add(label5, cc.xy(3, 15));

		//---- label6 ----
		label6.setText(bundle.getString("JMuestraDatos.label6.text"));
		contentPane.add(label6, cc.xy(3, 17));

		//---- label7 ----
		label7.setText(bundle.getString("JMuestraDatos.label7.text"));
		contentPane.add(label7, cc.xy(3, 19));
		setSize(425, 335);
		setLocationRelativeTo(getOwner());
		// //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY
	// //GEN-BEGIN:variables
	private JMenuBar menuBar1;
	private JMenu menu1;
	private JMenuItem configMenuItem;
	private JMenuItem exitMenuItem;
	private JMenu menu2;
	private JMenuItem verifyDataMenuItem;
	private JMenuItem readTagsMenuItem;
	private JMenuItem programTagsMenuItem;
	private JMenu menu3;
	private JMenuItem aboutUsMenuItem;
	private JLabel label8;
	private JLabel label1;
	private JLabel label9;
	private JLabel label3;
	private JLabel label4;
	private JLabel label2;
	private JLabel label5;
	private JLabel label6;
	private JLabel label7;
	// JFormDesigner - End of variables declaration //GEN-END:variables

	private void redLedOn() {
		Reader.GpioPin[] pins = new Reader.GpioPin[1];
		pins[0] = new GpioPin(2, true);
		try {
			ReaderContext.gpoSet(pins);
		} catch (ReaderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void redLedOff() {
		Reader.GpioPin[] pins = new Reader.GpioPin[1];
		pins[0] = new GpioPin(2, false);
		try {
			ReaderContext.gpoSet(pins);
		} catch (ReaderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void handleReadings(List<TagReading> readings) {
		// logger.debug("Last tag count = " + lastTagCount);
		GpioPin[] pins;
		try {
			pins = ReaderContext.getGpo();
			for (int i = 0; i < pins.length; i++) {
				GpioPin gpioPin = pins[i];
				// logger.info("Input pin " + gpioPin);
				// logger.info("high " + gpioPin.high);
				// logger.info("output " + gpioPin.output);
			}
		} catch (ReaderException e4) {
			// TODO Auto-generated catch block
			e4.printStackTrace();
		}
		if ((readings.size() == 1) && (lastTagCount != 1)) {
			logger.info("New tag in field");
			TagReading tagReading = readings.get(0);
			logger.info("Got EPC " + tagReading.getEpc());
			String tid = null;
			String userData = null;
			try {
				tid = ReaderContext.readTid(tagReading.getEpc(), 12);
			} catch (ReaderException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			redLedOn();
			Gen2.TagData epc = null;
			byte[] epcBytes = null;
			// try {
			// // Rfid rfid = rfidDao.fetchByChipNumber(chipNumber);
			// // if (rfid == null) {
			// // logger.warn("No such rfid chipnumber:" + chipNumber);
			// // } else {
			// // chipNumber = chipNumber + 1;
			// // logger.info("Programming rfid tag "
			// // + rfid.getRfidString());
			// // epcBytes = Hex.decodeHex(rfid.getRfidString()
			// // .toCharArray());
			// // epc = new Gen2.TagData(epcBytes);
			// // }
			// } catch (SQLException e1) {
			// // TODO Auto-generated catch block
			// e1.printStackTrace();
			// } catch (DecoderException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			System.out.println("Requesting write EPC...");
			Gen2.WriteTag tagop = new Gen2.WriteTag(epc);
			// try {
			// // ReaderContext.executeTagOp(tagop, target);
			// System.out.println("Wrote tag!");
			// redLedOff();
			// } catch (ReaderException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
		}
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		lastTagCount = readings.size();

	}
}
