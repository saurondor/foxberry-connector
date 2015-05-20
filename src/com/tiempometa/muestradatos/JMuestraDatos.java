/*
 * Created by JFormDesigner on Tue May 19 08:24:05 CDT 2015
 */

package com.tiempometa.muestradatos;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

import javax.swing.*;

import org.apache.log4j.Logger;

import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;

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

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY
		// //GEN-BEGIN:initComponents
		ResourceBundle bundle = ResourceBundle.getBundle("com.tiempometa.muestradatos.muestradatos");
		configButton = new JButton();
		textPane1 = new JTextField();
		powerLevel = new JTextField();
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
				new RowSpec(Sizes.dluY(89)),
				FormFactory.LINE_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC
			}));

		//---- configButton ----
		configButton.setText(bundle.getString("JMuestraDatos.configButton.text"));
		configButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				configButtonActionPerformed(e);
			}
		});
		contentPane.add(configButton, cc.xy(3, 3));
		contentPane.add(textPane1, cc.xywh(3, 5, 3, 1));
		contentPane.add(powerLevel, cc.xy(3, 7));
		setSize(400, 300);
		setLocationRelativeTo(getOwner());
		// //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY
	// //GEN-BEGIN:variables
	private JButton configButton;
	private JTextField textPane1;
	private JTextField powerLevel;
	// JFormDesigner - End of variables declaration //GEN-END:variables

	@Override
	public void handleReadings(List<TagReading> readings) {
		logger.info("Handling readings " + readings.size());
		for (TagReading tagReading : readings) {
			logger.info("Got EPC " + tagReading.getEpc());
			textPane1.setText(tagReading.getEpc());
			powerLevel.setText(String.valueOf(tagReading.getPeakRssi()));
		}
	}
}
