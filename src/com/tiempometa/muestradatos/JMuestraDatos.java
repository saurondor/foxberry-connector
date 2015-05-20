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

import org.apache.commons.codec.binary.Hex;
import org.apache.log4j.Logger;

import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import com.thingmagic.Gen2;
import com.thingmagic.Reader;
import com.thingmagic.ReaderCodeException;
import com.thingmagic.ReaderException;
import com.thingmagic.TagData;
import com.thingmagic.Reader.GpioPin;
import com.tiempometa.timing.dao.RfidDao;
import com.tiempometa.timing.dao.access.RfidDaoImpl;
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

	public JMuestraDatos() {
		initComponents();
		ReaderContext.addReadingListener(this);
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

	private void configButtonActionPerformed(ActionEvent e) {
		// TODO add your code here
		JConfigDialog configDialog = new JConfigDialog(this);
		configDialog.setVisible(true);
	}

	private void verifyDataMenuItemActionPerformed(ActionEvent e) {
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
		menuBar1 = new JMenuBar();
		menu1 = new JMenu();
		menuItem1 = new JMenuItem();
		menuItem2 = new JMenuItem();
		menu2 = new JMenu();
		verifyDataMenuItem = new JMenuItem();
		menuItem3 = new JMenuItem();
		menuItem5 = new JMenuItem();
		menu3 = new JMenu();
		configButton = new JButton();
		label1 = new JLabel();
		epcTextField = new JTextField();
		label2 = new JLabel();
		rssiTextField = new JTextField();
		label3 = new JLabel();
		tidTextField = new JTextField();
		CellConstraints cc = new CellConstraints();

		//======== this ========
		setTitle(bundle.getString("JMuestraDatos.this.title"));
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		Container contentPane = getContentPane();
		contentPane.setLayout(new FormLayout(
			new ColumnSpec[] {
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				new ColumnSpec(Sizes.dluX(86)),
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC
			},
			new RowSpec[] {
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.LINE_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.LINE_GAP_ROWSPEC,
				new RowSpec(Sizes.dluY(27)),
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

				//---- menuItem1 ----
				menuItem1.setText(bundle.getString("JMuestraDatos.menuItem1.text"));
				menu1.add(menuItem1);
				menu1.addSeparator();

				//---- menuItem2 ----
				menuItem2.setText(bundle.getString("JMuestraDatos.menuItem2.text"));
				menu1.add(menuItem2);
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

				//---- menuItem3 ----
				menuItem3.setText(bundle.getString("JMuestraDatos.menuItem3.text"));
				menu2.add(menuItem3);

				//---- menuItem5 ----
				menuItem5.setText(bundle.getString("JMuestraDatos.menuItem5.text"));
				menu2.add(menuItem5);
			}
			menuBar1.add(menu2);

			//======== menu3 ========
			{
				menu3.setText(bundle.getString("JMuestraDatos.menu3.text"));
			}
			menuBar1.add(menu3);
		}
		setJMenuBar(menuBar1);

		//---- configButton ----
		configButton.setText(bundle.getString("JMuestraDatos.configButton.text"));
		configButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				configButtonActionPerformed(e);
			}
		});
		contentPane.add(configButton, cc.xy(3, 3));

		//---- label1 ----
		label1.setText(bundle.getString("JMuestraDatos.label1.text"));
		contentPane.add(label1, cc.xy(3, 7));
		contentPane.add(epcTextField, cc.xywh(5, 7, 3, 1));

		//---- label2 ----
		label2.setText(bundle.getString("JMuestraDatos.label2.text"));
		contentPane.add(label2, cc.xy(3, 9));
		contentPane.add(rssiTextField, cc.xy(5, 9));

		//---- label3 ----
		label3.setText(bundle.getString("JMuestraDatos.label3.text"));
		contentPane.add(label3, cc.xy(3, 11));
		contentPane.add(tidTextField, cc.xy(5, 11));
		setSize(425, 335);
		setLocationRelativeTo(getOwner());
		// //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY
	// //GEN-BEGIN:variables
	private JMenuBar menuBar1;
	private JMenu menu1;
	private JMenuItem menuItem1;
	private JMenuItem menuItem2;
	private JMenu menu2;
	private JMenuItem verifyDataMenuItem;
	private JMenuItem menuItem3;
	private JMenuItem menuItem5;
	private JMenu menu3;
	private JButton configButton;
	private JLabel label1;
	private JTextField epcTextField;
	private JLabel label2;
	private JTextField rssiTextField;
	private JLabel label3;
	private JTextField tidTextField;
	// JFormDesigner - End of variables declaration //GEN-END:variables

	@Override
	public void handleReadings(List<TagReading> readings) {
		logger.info("Handling readings " + readings.size());
		for (TagReading tagReading : readings) {
			logger.info("Got EPC " + tagReading.getEpc());
			epcTextField.setText(tagReading.getEpc());
			rssiTextField.setText(String.valueOf(tagReading.getPeakRssi()));
			RfidDao rfidDao = new RfidDaoImpl();
			String bib = "1";
			Integer chipNumber = 0;
			String tid = null;
			String userData = null;
			TagData target = new TagData(tagReading.getEpc());
			logger.info(tagReading.getClass().getCanonicalName());
			Reader.GpioPin[] pins = new Reader.GpioPin[1];
			pins[0] = new GpioPin(1,true);
			try {
				ReaderContext.gpoSet(pins);
			} catch (ReaderException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			pins[0] = new GpioPin(1,false);
			try {
				ReaderContext.gpoSet(pins);
			} catch (ReaderException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
//				userData = String.valueOf(Hex.encodeHex(ReaderContext.readTagMemBytes(
//						target, Gen2.Bank.USER.ordinal(), 0, 32)));
				tid = String.valueOf(Hex.encodeHex(ReaderContext.readTagMemBytes(
						target, Gen2.Bank.TID.ordinal(), 0, 4)));
				tidTextField.setText(tid);
			} catch (ReaderCodeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				tidTextField.setText("");

			} catch (ReaderException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				tidTextField.setText("");
			}

			Rfid rfid = new Rfid(null, null, bib, tagReading.getEpc(),
					Rfid.STATUS_NOT_ASSIGNED, Rfid.PAYMENT_STATUS_UNPAID,
					Rfid.TOKEN_STATUS_AVAILABLE, tid, chipNumber);
			try {
				rfidDao.save(rfid);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
