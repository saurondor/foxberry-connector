/*
 * Created by JFormDesigner on Mon Jul 06 11:03:32 CDT 2015
 */

package com.tiempometa.muestradatos;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.sql.SQLException;
import java.util.*;
import java.util.List;

import javax.swing.*;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import org.apache.log4j.Logger;

import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import com.tiempometa.timing.dao.ChipReadRawDao;
import com.tiempometa.timing.dao.access.ChipReadRawDaoImpl;
import com.tiempometa.timing.models.ChipReadRaw;

/**
 * @author Gerardo Esteban Tasistro Giubetic
 */
public class JLoadTimeReadings extends JDialog implements TagReadListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8179757122215063343L;
	private ChipReadRawDao chipReadRawDao = new ChipReadRawDaoImpl();
	private static final Logger logger = Logger
			.getLogger(JLoadTimeReadings.class);

	public JLoadTimeReadings() {
		initComponents();
		ReaderContext.addReadingListener(this);
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY
		// //GEN-BEGIN:initComponents
		ResourceBundle bundle = ResourceBundle
				.getBundle("com.tiempometa.muestradatos.muestradatos");
		dialogPane = new JPanel();
		contentPanel = new JPanel();
		label10 = new JLabel();
		label1 = new JLabel();
		checkpointTextField = new JTextField();
		applyCheckpointButton = new JButton();
		label2 = new JLabel();
		timeWindowTextField = new JTextField();
		label3 = new JLabel();
		label4 = new JLabel();
		comboBox1 = new JComboBox();
		label5 = new JLabel();
		totalReadingsLabel = new JLabel();
		label6 = new JLabel();
		lastReadingLabel = new JLabel();
		label9 = new JLabel();
		scrollPane1 = new JScrollPane();
		readingsTable = new JTable();
		buttonBar = new JPanel();
		okButton = new JButton();
		CellConstraints cc = new CellConstraints();

		// ======== this ========
		setIconImage(new ImageIcon(getClass().getResource(
				"/com/tiempometa/resources/stopwatch_small.png")).getImage());
		setTitle(bundle.getString("JLoadTimeReadings.this.title"));
		Container contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());

		// ======== dialogPane ========
		{
			dialogPane.setBorder(Borders.DIALOG_BORDER);
			dialogPane.setLayout(new BorderLayout());

			// ======== contentPanel ========
			{
				contentPanel.setLayout(new FormLayout(new ColumnSpec[] {
						FormFactory.DEFAULT_COLSPEC,
						FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
						FormFactory.DEFAULT_COLSPEC,
						FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
						new ColumnSpec(Sizes.dluX(115)),
						FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
						FormFactory.DEFAULT_COLSPEC }, new RowSpec[] {
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
						FormFactory.DEFAULT_ROWSPEC }));

				// ---- label10 ----
				label10.setText(bundle
						.getString("JLoadTimeReadings.label10.text"));
				label10.setFont(new Font("Tahoma", Font.PLAIN, 18));
				contentPanel.add(label10, cc.xy(3, 1));

				// ---- label1 ----
				label1.setText(bundle
						.getString("JLoadTimeReadings.label1.text"));
				contentPanel.add(label1, cc.xy(3, 3));
				contentPanel.add(checkpointTextField, cc.xy(5, 3));

				// ---- applyCheckpointButton ----
				applyCheckpointButton
						.setText(bundle
								.getString("JLoadTimeReadings.applyCheckpointButton.text"));
				contentPanel.add(applyCheckpointButton, cc.xy(7, 3));

				// ---- label2 ----
				label2.setText(bundle
						.getString("JLoadTimeReadings.label2.text"));
				contentPanel.add(label2, cc.xy(3, 5));
				contentPanel.add(timeWindowTextField, cc.xy(5, 5));

				// ---- label3 ----
				label3.setText(bundle
						.getString("JLoadTimeReadings.label3.text"));
				contentPanel.add(label3, cc.xy(7, 5));

				// ---- label4 ----
				label4.setText(bundle
						.getString("JLoadTimeReadings.label4.text"));
				contentPanel.add(label4, cc.xy(3, 7));
				contentPanel.add(comboBox1, cc.xy(5, 7));

				// ---- label5 ----
				label5.setText(bundle
						.getString("JLoadTimeReadings.label5.text"));
				contentPanel.add(label5, cc.xy(3, 9));

				// ---- totalReadingsLabel ----
				totalReadingsLabel
						.setText(bundle
								.getString("JLoadTimeReadings.totalReadingsLabel.text"));
				contentPanel.add(totalReadingsLabel, cc.xy(5, 9));

				// ---- label6 ----
				label6.setText(bundle
						.getString("JLoadTimeReadings.label6.text"));
				contentPanel.add(label6, cc.xy(3, 11));

				// ---- lastReadingLabel ----
				lastReadingLabel.setText(bundle
						.getString("JLoadTimeReadings.lastReadingLabel.text"));
				contentPanel.add(lastReadingLabel, cc.xy(5, 11));

				// ---- label9 ----
				label9.setText(bundle
						.getString("JLoadTimeReadings.label9.text"));
				label9.setFont(new Font("Tahoma", Font.PLAIN, 18));
				contentPanel.add(label9, cc.xy(3, 13));

				// ======== scrollPane1 ========
				{
					scrollPane1.setViewportView(readingsTable);
				}
				contentPanel.add(scrollPane1, cc.xywh(3, 15, 5, 1));
			}
			dialogPane.add(contentPanel, BorderLayout.CENTER);

			// ======== buttonBar ========
			{
				buttonBar.setBorder(Borders.BUTTON_BAR_GAP_BORDER);
				buttonBar.setLayout(new FormLayout(new ColumnSpec[] {
						FormFactory.GLUE_COLSPEC, FormFactory.BUTTON_COLSPEC },
						RowSpec.decodeSpecs("pref")));

				// ---- okButton ----
				okButton.setText("OK");
				buttonBar.add(okButton, cc.xy(2, 1));
			}
			dialogPane.add(buttonBar, BorderLayout.SOUTH);
		}
		contentPane.add(dialogPane, BorderLayout.CENTER);
		pack();
		setLocationRelativeTo(getOwner());
		// JFormDesigner - End of component initialization
		// //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY
	// //GEN-BEGIN:variables
	private JPanel dialogPane;
	private JPanel contentPanel;
	private JLabel label10;
	private JLabel label1;
	private JTextField checkpointTextField;
	private JButton applyCheckpointButton;
	private JLabel label2;
	private JTextField timeWindowTextField;
	private JLabel label3;
	private JLabel label4;
	private JComboBox comboBox1;
	private JLabel label5;
	private JLabel totalReadingsLabel;
	private JLabel label6;
	private JLabel lastReadingLabel;
	private JLabel label9;
	private JScrollPane scrollPane1;
	private JTable readingsTable;
	private JPanel buttonBar;
	private JButton okButton;

	// JFormDesigner - End of variables declaration //GEN-END:variables
	@Override
	public void handleReadings(List<TagReading> readings) {
		if (readings.size() > 0) {
			for (Iterator iterator = readings.iterator(); iterator.hasNext();) {
				TagReading tagReading = (TagReading) iterator.next();
				if (tagReading.isKeepAlive()) {

				} else {
					String checkPoint = null;
					String loadName = null;
					ChipReadRaw chipReading = new ChipReadRaw(null,
							tagReading.getEpc(), tagReading.getTime(),
							tagReading.getTimeMillis(), checkPoint, null,
							ChipReadRaw.STATUS_RAW,
							ChipReadRaw.FILTERED_READER, loadName, null);
					try {
						logger.debug("Saving chip reading");
						chipReadRawDao.save(chipReading);
						InputStream mp3Stream = this.getClass().getResourceAsStream("/com/tiempometa/resources/keepalive.mp3");
						FileInputStream fis = new FileInputStream("C:/Users/Gerardo Tasistro/Downloads/pullup.mp3");
						Player player = new Player(mp3Stream);
						player.play();
						
//						String bip = "C:/Users/Gerardo Tasistro/Downloads/pullup.mp3";
//						Media hit = new Media(bip);
//						MediaPlayer mediaPlayer = new MediaPlayer(hit);
//						mediaPlayer.play();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (JavaLayerException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Window#dispose()
	 */
	@Override
	public void dispose() {
		if (logger.isDebugEnabled()) {
			logger.debug("Removing self from tag read listeners");
		}
		ReaderContext.removeReadingListener(this);
		super.dispose();
	}
}
