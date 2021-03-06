package conn;

import gui.GUIState;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

/**
 * <p>
 * Überschrift: MajorJPanel
 * </p>
 * <p>
 * Beschreibung: a JPanel, that shows a loginScreen or the MyJPanel
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Organisation: ZHR
 * </p>
 * 
 * @author rschoene
 * @version 1.0
 */
public class MajorJPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * textfield for username
	 */
	private final JTextField userName = new JTextField(20);
	/**
	 * textfield for password
	 */
	private final JPasswordField password = new JPasswordField(20);
	/**
	 * label, where errors are printed
	 */
	private final JTextPane errorLabel = new JTextPane();
	/**
	 * refference, if we can connect to server
	 */
	MyJPanel my;
	/**
	 * ip to connect to
	 */
	private final String ip;
	/**
	 * whole panel for username,password and errorlabel
	 */
	private JPanel loginPanel;
	/**
	 * referrence to gui
	 */
	private final gui.BIGGUI biggui;
	/**
	 * close connection menuitem
	 */
	private final JMenu menuClose;
	/**
	 * are we connected to db?
	 */
	private boolean loggedIn = false;

	/**
	 * creates a MajorJPanel, that can login at ip (ip)
	 * 
	 * @param ip the ip of the benchit-server
	 * @param menuClose Menu for clode connection
	 * @param biggui the BIGGUI
	 */
	public MajorJPanel(String ip, JMenu menuClose, gui.BIGGUI biggui) {
		// setting local variables and initialize layout (show login panel)
		this.ip = ip;
		initLayout(false);
		this.biggui = biggui;
		this.menuClose = menuClose;
	}

	/**
	 * repaints this
	 * 
	 * @param loggedIn whether logged in (true) or not
	 */
	private void initLayout(boolean loggedIn) {
		// if we havent logged in on server
		if (!loggedIn) {
			// if we donr have a login panel
			if (loginPanel == null) {
				// build it
				loginPanel = new JPanel();
				// connect to server
				JButton okayButton = new JButton("Okay");
				// if clicked
				okayButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						tryStart();
					}
				});
				// if enter is pressed
				userName.addKeyListener(new KeyAdapter() {
					@Override
					public void keyPressed(KeyEvent e) {
						if (e.getKeyCode() == KeyEvent.VK_ENTER) {
							tryStart();
						}
					}
				});
				password.addKeyListener(new KeyAdapter() {
					@Override
					public void keyPressed(KeyEvent e) {
						if (e.getKeyCode() == KeyEvent.VK_ENTER) {
							tryStart();
						}
					}
				});
				okayButton.addKeyListener(new KeyAdapter() {
					@Override
					public void keyPressed(KeyEvent e) {
						if (e.getKeyCode() == KeyEvent.VK_ENTER) {
							tryStart();
						}
					}
				});
				// layout-stuff . BOOOORING
				errorLabel.setText("If you want to connect to the BenchIT-Database,\n"
						+ "be sure to have a login at the BenchIT-Website.\n" + "Just visit www.benchit.org.");
				errorLabel.setEditable(false);
				errorLabel.setBackground(loginPanel.getBackground());
				GridBagLayout gb = new GridBagLayout();
				GridBagConstraints gc = new GridBagConstraints();
				loginPanel.setLayout(gb);
				gc.gridx = 0;
				gc.gridy = 0;
				loginPanel.add(new JLabel("User-Name"), gc);
				gc.gridy = 1;
				loginPanel.add(new JLabel("Password"), gc);
				gc.gridy = 2;
				gc.gridwidth = 2;
				loginPanel.add(okayButton, gc);
				gc.gridwidth = 2;
				gc.gridy = 3;
				gc.gridwidth = GridBagConstraints.REMAINDER;
				loginPanel.add(errorLabel, gc);
				gc.gridy = 1;
				gc.gridx = 1;
				loginPanel.add(password, gc);
				gc.gridy = 0;
				loginPanel.add(userName, gc);
				GridBagLayout gb2 = new GridBagLayout();
				GridBagConstraints gc2 = new GridBagConstraints();
				gc2.fill = GridBagConstraints.BOTH;
				gc2.gridx = 0;
				gc2.gridy = 0;
				setLayout(gb2);
				removeAll();
				this.add(loginPanel, gc2);
				double temp[] = {1};
				double temp2[] = {1};
				gb2.columnWeights = temp;
				gb2.rowWeights = temp2;
			} else {
				// if it was initialized
				// (disconnected from server)
				// let username as it is, but remove the password
				password.setText("");
				GridBagLayout gb2 = new GridBagLayout();
				GridBagConstraints gc2 = new GridBagConstraints();
				gc2.fill = GridBagConstraints.BOTH;
				gc2.gridx = 0;
				gc2.gridy = 0;
				setLayout(gb2);
				removeAll();
				this.add(loginPanel, gc2);
				double temp[] = {1};
				double temp2[] = {1};
				gb2.columnWeights = temp;
				gb2.rowWeights = temp2;
			}

		}
		// we are online
		else {}
	}

	/**
	 * tries to start the connection to the server
	 */
	private void tryStart() {
		// try to connect
		try {
			my = new MyJPanel(ip, userName.getText(), new String(password.getPassword()), this, biggui);
		} catch (Exception e) {
			// print exception, if failure
			errorLabel.setText(e.getMessage());
			e.printStackTrace();
			errorLabel.revalidate();
			return;
		}
		// connect succesful
		menuClose.setEnabled(true);
		loggedIn = true;
		biggui.setOnline(true, my.lowerRightPanel);
		biggui.setGUIState(GUIState.RESULTS);
	}

	/**
	 * closes the connection to the server
	 */
	public void closeConnection() {
		(new Thread() {
			@Override
			public void run() {
				if (loggedIn) {
					try {
						System.out.println("Connection closed");
						my.getSocketCon().close();
					} catch (Exception e) {}
				}
				password.setText("");
				loggedIn = false;
				initLayout(false);
				loginPanel = null;
				menuClose.setEnabled(false);
				biggui.setOnline(false);
				repaint();
				validate();
			}
		}).start();
	}
}
