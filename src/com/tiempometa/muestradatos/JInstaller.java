/*
 * Created by JFormDesigner on Wed Aug 26 09:17:16 CDT 2015
 */

package com.tiempometa.muestradatos;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.*;

import javax.swing.*;

import org.apache.log4j.Logger;

import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;

/**
 * @author Gerardo Esteban Tasistro Giubetic
 */
public class JInstaller extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2481725143415385012L;
	private static SystemProperties systemProperties = new SystemProperties();
	private static final Logger logger = Logger.getLogger(JInstaller.class);

	private void loadJavaSettings() {
		logger.info("Architecture is " + systemProperties.getArch_data_model());
		javaArchitectureLabel.setText(systemProperties.getArch_data_model());
		javaInstallPathLabel.setText(systemProperties.getJava_home());
		javaVersionLabel.setText(systemProperties.getJava_version());
		appInstallPathLabel.setText(systemProperties.getUser_dir());
		return;
	}

	public JInstaller(Frame owner) {
		super(owner);
		initComponents();
		loadJavaSettings();
		verifySettings();
	}

	private void selectJavaButtonActionPerformed(ActionEvent e) {
		JFileChooser fc = new JFileChooser(javaInstallPathLabel.getText());
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int response = fc.showOpenDialog(this);
		if (response == JFileChooser.APPROVE_OPTION) {
			javaInstallPathLabel
					.setText(fc.getSelectedFile().getAbsolutePath());
			verifySettings();
		}
	}

	private void verifySettings() {
		logger.info("Verifying java settings");
		try {
			if (InstallUtils.isJava32Bit(javaInstallPathLabel.getText()
					+ "\\bin\\java")) {
				logger.info("Java is 32bit");
				javaVersionLabel.setText(InstallUtils
						.getJavaVersion(javaInstallPathLabel.getText()
								+ "\\bin\\java"));
				if (javaVersionLabel.getText().startsWith("1.6")
						|| javaVersionLabel.getText().startsWith("1.7")) {
					logger.info("Java is version 6 o 7");
					JOptionPane.showMessageDialog(this,
							"Se está usando Java 6 o Java 7 de 32 bits",
							"Versión de Java Correcta",
							JOptionPane.INFORMATION_MESSAGE);
					okButton.setEnabled(true);
					javaArchitectureLabel.setText("32");
				} else {
					logger.error("Java is not version 6 o 7");
					JOptionPane
							.showMessageDialog(
									this,
									"Se requiere Java 6 o Java 7 de 32 bits para usar Access como base de datos.\nFavor de indicar la instalación de Java 6 de 32bits",
									"Versión de Java Incorrecta",
									JOptionPane.ERROR_MESSAGE);
					okButton.setEnabled(false);
					javaArchitectureLabel.setText("32");
				}
			} else {
				logger.error("Java is not 32 bit");
				JOptionPane
						.showMessageDialog(
								this,
								"Se ha seleccionado Java 64 bits, se requiere Java 6 de 32 bits para usar Access como base de datos.\nFavor de indicar la instalación de Java 6 de 32bits",
								"Versión de Java Incorrecta",
								JOptionPane.ERROR_MESSAGE);
				okButton.setEnabled(false);
				javaArchitectureLabel.setText("64");
				javaVersionLabel.setText(InstallUtils
						.getJavaVersion(javaInstallPathLabel.getText()
								+ "\\bin\\java"));
			}
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "Error " + e.getMessage(),
					"Error verificando Java", JOptionPane.ERROR_MESSAGE);
			okButton.setEnabled(false);
			javaArchitectureLabel.setText("NA");
			javaVersionLabel.setText("NA");
		} catch (InterruptedException e) {
			JOptionPane.showMessageDialog(this, "Error " + e.getMessage(),
					"Error verificando Java", JOptionPane.ERROR_MESSAGE);
			okButton.setEnabled(false);
			javaArchitectureLabel.setText("NA");
			javaVersionLabel.setText("NA");
		}
	}

	private void cancelButtonActionPerformed(ActionEvent e) {
		dispose();
	}

	private void okButtonActionPerformed(ActionEvent e) {
		String installPath = appInstallPathLabel.getText();
		String java_home = javaInstallPathLabel.getText();
		int response = JOptionPane.showConfirmDialog(this, "Se instalará en "
				+ installPath, "Confirmar instalación",
				JOptionPane.YES_NO_OPTION);
		if (response == JOptionPane.YES_OPTION) {
			try {
				if (systemProperties.getArch_data_model().equals("32")) {
					InstallUtils.unpackDll(InstallUtils.X86,
							systemProperties.getUser_dir());
				}
				if (systemProperties.getArch_data_model().equals("64")) {
					InstallUtils.unpackDll(InstallUtils.AMD64,
							systemProperties.getUser_dir());
				}
				// create symbolic links
				if (createDesktopShortcut.isSelected()) {
					JOptionPane.showMessageDialog(this,
							"Se crearán ligas de acceso en el escritorio ",
							"Ligas", JOptionPane.INFORMATION_MESSAGE);
					createDesktopShortcuts(java_home, installPath);
					JOptionPane.showMessageDialog(this,
							"Las ligas se crearon con éxito", "Ligas",
							JOptionPane.INFORMATION_MESSAGE);
				}
				JOptionPane.showMessageDialog(this,
						"El sofware de cronometraje se instaló con éxito",
						"Instalación exitosa", JOptionPane.INFORMATION_MESSAGE);
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(this,
						"No se pudo extraer el instalador " + e1.getMessage(),
						"Error de instalación", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private void createDesktopShortcuts(String java_home, String app_home) {
		String jarFileName = java_home + "\\bin\\java.exe";
		String argument = "-jar \"" + app_home + "\\foxberry_reader_1_0.jar"
				+ "\"";
		logger.info("Creating shortcut in " + app_home + " to " + argument);
		InstallUtils.createShortcut(jarFileName, argument, app_home,
				"Foxberry Connector");
	}

	private void appInstallPathSelectButtonActionPerformed(ActionEvent e) {
		JFileChooser fc = new JFileChooser(appInstallPathLabel.getText());
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int response = fc.showOpenDialog(this);
		if (response == JFileChooser.APPROVE_OPTION) {
			appInstallPathLabel.setText(fc.getSelectedFile().getAbsolutePath());
			verifySettings();
		}
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY
		// //GEN-BEGIN:initComponents
		ResourceBundle bundle = ResourceBundle
				.getBundle("com.tiempometa.muestradatos.muestradatos");
		dialogPane = new JPanel();
		contentPanel = new JPanel();
		label1 = new JLabel();
		javaLabel = new JLabel();
		label2 = new JLabel();
		javaVersionLabel = new JLabel();
		label3 = new JLabel();
		javaArchitectureLabel = new JLabel();
		label4 = new JLabel();
		javaInstallPathLabel = new JLabel();
		selectJavaButton = new JButton();
		label5 = new JLabel();
		appInstallPathLabel = new JLabel();
		appInstallPathSelectButton = new JButton();
		createDesktopShortcut = new JCheckBox();
		buttonBar = new JPanel();
		okButton = new JButton();
		cancelButton = new JButton();
		CellConstraints cc = new CellConstraints();

		// ======== this ========
		setTitle(bundle.getString("JInstaller.this.title"));
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
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
						new ColumnSpec(Sizes.dluX(185)),
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
						FormFactory.DEFAULT_ROWSPEC }));

				// ---- label1 ----
				label1.setText(bundle.getString("JInstaller.label1.text"));
				contentPanel.add(label1, cc.xy(3, 3));
				contentPanel.add(javaLabel, cc.xy(5, 3));

				// ---- label2 ----
				label2.setText(bundle.getString("JInstaller.label2.text"));
				contentPanel.add(label2, cc.xy(3, 5));
				contentPanel.add(javaVersionLabel, cc.xy(5, 5));

				// ---- label3 ----
				label3.setText(bundle.getString("JInstaller.label3.text"));
				contentPanel.add(label3, cc.xy(3, 7));
				contentPanel.add(javaArchitectureLabel, cc.xy(5, 7));

				// ---- label4 ----
				label4.setText(bundle.getString("JInstaller.label4.text"));
				contentPanel.add(label4, cc.xy(3, 9));
				contentPanel.add(javaInstallPathLabel, cc.xy(5, 9));

				// ---- selectJavaButton ----
				selectJavaButton.setText(bundle
						.getString("JInstaller.selectJavaButton.text"));
				selectJavaButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						selectJavaButtonActionPerformed(e);
					}
				});
				contentPanel.add(selectJavaButton, cc.xy(7, 9));

				// ---- label5 ----
				label5.setText(bundle.getString("JInstaller.label5.text"));
				contentPanel.add(label5, cc.xy(3, 11));
				contentPanel.add(appInstallPathLabel, cc.xy(5, 11));

				// ---- appInstallPathSelectButton ----
				appInstallPathSelectButton
						.setText(bundle
								.getString("JInstaller.appInstallPathSelectButton.text"));
				appInstallPathSelectButton
						.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								appInstallPathSelectButtonActionPerformed(e);
							}
						});
				contentPanel.add(appInstallPathSelectButton, cc.xy(7, 11));

				// ---- createDesktopShortcut ----
				createDesktopShortcut.setText(bundle
						.getString("JInstaller.createDesktopShortcut.text"));
				contentPanel.add(createDesktopShortcut, cc.xy(3, 13));
			}
			dialogPane.add(contentPanel, BorderLayout.CENTER);

			// ======== buttonBar ========
			{
				buttonBar.setBorder(Borders.BUTTON_BAR_GAP_BORDER);
				buttonBar.setLayout(new FormLayout(new ColumnSpec[] {
						FormFactory.GLUE_COLSPEC, FormFactory.BUTTON_COLSPEC,
						FormFactory.RELATED_GAP_COLSPEC,
						FormFactory.BUTTON_COLSPEC }, RowSpec
						.decodeSpecs("pref")));

				// ---- okButton ----
				okButton.setText("OK");
				okButton.setEnabled(false);
				okButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						okButtonActionPerformed(e);
					}
				});
				buttonBar.add(okButton, cc.xy(2, 1));

				// ---- cancelButton ----
				cancelButton.setText("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						cancelButtonActionPerformed(e);
					}
				});
				buttonBar.add(cancelButton, cc.xy(4, 1));
			}
			dialogPane.add(buttonBar, BorderLayout.SOUTH);
		}
		contentPane.add(dialogPane, BorderLayout.CENTER);
		pack();
		setLocationRelativeTo(getOwner());
		// //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY
	// //GEN-BEGIN:variables
	private JPanel dialogPane;
	private JPanel contentPanel;
	private JLabel label1;
	private JLabel javaLabel;
	private JLabel label2;
	private JLabel javaVersionLabel;
	private JLabel label3;
	private JLabel javaArchitectureLabel;
	private JLabel label4;
	private JLabel javaInstallPathLabel;
	private JButton selectJavaButton;
	private JLabel label5;
	private JLabel appInstallPathLabel;
	private JButton appInstallPathSelectButton;
	private JCheckBox createDesktopShortcut;
	private JPanel buttonBar;
	private JButton okButton;
	private JButton cancelButton;
	// JFormDesigner - End of variables declaration //GEN-END:variables
}
