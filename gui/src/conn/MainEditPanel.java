package conn;

import java.awt.*;

import javax.swing.*;

import conn.SocketCon.ComboBoxContent;

/**
 * <p>
 * Überschrift: The MainEditPanel
 * </p>
 * <p>
 * Beschreibung: contains all special EditPanels as Tabs
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
public class MainEditPanel extends JTabbedPane {
	/**
	 * default serialVersionUID to get rid of warnings
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * select from all -> use NewAbstractEditPanel
	 */
	final static int SELECT_FROM_ALL = 0;

	/**
	 * select from kernel -> use KernelEditPanel
	 */
	final static int SELECT_FROM_KERNEL = 1;

	/**
	 * select from architecture -> use ArchitectureEditPanel
	 */
	final static int SELECT_FROM_ARCHITECTURE = 2;

	/**
	 * available identifiers. got only once in initialization!
	 */
	public String identifiers[] = new String[0];

	/**
	 * contains an info about online mode
	 */
	private final JPanel infoPanel = new JPanel();
	/**
	 * for SELECT_ALL
	 */
	private NewAbstractEditPanel selectAllPanel = null;
	/**
	 * for SELECT_KERNEL
	 */
	private final KernelEditPanel kernelEdit;

	/**
	 * used to communicate with the server
	 */
	SocketCon socketCon;
	/**
	 * access to all conn-data
	 */
	MyJPanel root;
	/**
	 * access to all gui-data
	 */
	private gui.BIGGUI gui = null;
	/**
	 * Reference to this
	 */
	MainEditPanel content;
	/**
	 * for SELECT_ARCHITECTURE
	 */
	private final ArchitectureEditPanel architectureEdit;

	/**
	 * constructor
	 * 
	 * @param s the (initialized) socketCon of the program
	 * @param g the GraphData (which contains all Graphs)
	 * @param root the myJPanel of the program
	 * @param gui reference to the Benchit GUI
	 */
	public MainEditPanel(SocketCon s, GraphData g, MyJPanel root, gui.BIGGUI gui) {
		// setting local variables
		content = this;
		this.root = root;
		socketCon = s;
		this.gui = gui;
		// helptext
		JTextArea tf = new JTextArea(
				"Hello and welcome to the network-database connection for the BenchIT-GUI\n" + "\n"
						+ "If you want to add a Graph Click the \"Select\"-Tab in this Panel.\n"
						+ "But if you would like to select an other way of selecting a graph,\n"
						+ "you can set it in this Panels\' \"Config Tab\"");
		tf.setEditable(false);
		tf.setBackground(Color.lightGray);
		infoPanel.add(tf, BorderLayout.CENTER);
		initializeSettings();
		kernelEdit = new KernelEditPanel(socketCon, content, identifiers);

		architectureEdit = new ArchitectureEditPanel(socketCon, content, identifiers);
		String[] allExceptTheLast = new String[identifiers.length - 1];
		System.arraycopy(identifiers, 0, allExceptTheLast, 0, allExceptTheLast.length);
		selectAllPanel = new NewAbstractEditPanel(socketCon, content, allExceptTheLast,
				identifiers[identifiers.length - 1]);

		this.addTab("Select free", selectAllPanel);
		this.addTab("Select by architecture/runtime-settings", architectureEdit);
		this.addTab("Select by kernel", kernelEdit);
		gui.getRightSplitPane().remove(gui.getConsoleScrollPane());
		gui.getRightSplitPane().revalidate();
		gui.getConsoleScrollPane().revalidate();
		gui.getRightSplitPane().setBottomComponent(this);
		revalidate();
		setSelectedIndex(0);

		addTab("Console", system.BIGInterface.getInstance().getConsole().getDisplayPanel());

		// get the console asap (just adding doesnt work :( )
		// also, the setConsolePane must be run!
		// why?????
		/*
		 * ( new Thread() { public void run() { while ( getTabCount() < 4 ) { } } } ).start() ;
		 */
		// show free
		setSelectedIndex(0);

	}

	/**
	 * resets / repaints the layout of this
	 */
	public void initLayout() {
		// remove all existing objects from this panel
		/*
		 * this.removeAll() ; // add the select-panel, depending on which way is selected in EditPanelComboSelect switch (
		 * whichSelect ) { case 0: this.addTab( "Select" , selectAllPanel ) ; break ; case 1: this.addTab( "Select" ,
		 * kernelEdit ) ; break ; case 2: this.addTab( "Select" , architectureEdit ) ; break ; } // add other panels
		 * this.addTab( "Config" , configurationPanel ) ; this.addTab( "Info" , infoPanel ) ; if ( consolePane != null )
		 * this.addTab( "Console" , consolePane ) ; this.setTabPlacement( 1 ) ; // if a tab was clicked / the selected is
		 * changed this.addChangeListener( new ChangeListener() { public void stateChanged( ChangeEvent e ) { // if the new
		 * selected is the EditPanelComboSelect if ( getSelectedIndex() == 1 ) { // get identifiers if we don't have them if
		 * ( identifiers.length == 0 ) { initializeSettings() ; removeChangeListener( this ) ; } } else { // if the new one
		 * is an EditPanel if ( getSelectedIndex() == 0 ) { // which is the selected???? if ( configurationPanel != null )
		 * whichSelect = configurationPanel.getSelectedConfiguration() ; else whichSelect = 1 ; switch ( whichSelect ) {
		 * case SELECT_FROM_ALL: // if we don't have initialized the panel yet if ( selectAllPanel == null ) // do it now
		 * selectAllPanel = new NewAbstractEditPanel( socketCon , content , configurationPanel.getUnselectedRadioButton(),
		 * configurationPanel.getSelectedRadioButton()[0] ) ; removeChangeListener( this ) ; whichSelect = SELECT_FROM_ALL ;
		 * // switch to the panel and repaint it initLayout() ; break ; case SELECT_FROM_KERNEL: // if we don't have
		 * initialized the panel yet if ( kernelEdit == null ) { // do it now kernelEdit = new KernelEditPanel( socketCon ,
		 * content , identifiers , null ) ; } removeChangeListener( this ) ; whichSelect = SELECT_FROM_KERNEL ; // switch to
		 * the panel and repaint it initLayout() ; break ; case SELECT_FROM_ARCHITECTURE: // if we don't have initialized
		 * the panel yet if ( architectureEdit == null ) { // do it now architectureEdit = new ArchitectureEditPanel(
		 * socketCon , content , identifiers , null ) ; } removeChangeListener( this ) ; whichSelect =
		 * SELECT_FROM_ARCHITECTURE ; // switch to the panel and repaint it initLayout() ; break ; } } else // Info! { //
		 * not needed } } } } ) ;
		 */
	}

	/**
	 * initializes this panel by getting some stuff from the db
	 */
	private void initializeSettings() {
		// do as thread
		/*
		 * ( new Thread() { public void run() {
		 */
		// get identifiers
		ComboBoxContent v;
		try {
			v = socketCon.startInitializing();
		} catch (java.io.IOException e) {
			System.err.println(e);
			return;
		}
		identifiers = v.identifiers;
		// configurationPanel = new EditPanelComboSelect( content ) ;
		// initLayout() ;
		// setTabPlacement( 1 ) ;
		// setSelectedIndex( 1 ) ;
		/*
		 * } } ).start() ;
		 */
	}

	/**
	 * adds a graph to GraphData by getting it from the server
	 * 
	 * @param identifiers the identifiers, that specify the graph
	 * @param settings the settings to the identifiers
	 */
	protected void addGraph(final String[] identifiers, final String settings[]) {
		addGraphWithoutReloading(identifiers, settings);
	}

	/**
	 * adds a graph to GraphData by getting it from the server
	 * 
	 * @param identifiers the identifiers, that specify the graph
	 * @param settings the settings to the identifiers
	 */
	protected void addGraphWithoutReloading(final String[] identifiers, final String settings[]) {
		Graph[] gs = new Graph[0];
		// get all from server, which can be meant by this description
		System.err.println("\n\nIdentifiers");
		/*
		 * for(int i = 0 ; i < identifiers.length ; i++) { System.err.println( identifiers[ i ] ); }
		 * System.err.println("\n\nSettings"); for(int j = 0 ; j < settings.length ; j++) { System.err.println( settings[ j
		 * ] ); }
		 */
		try {
			gs = socketCon.receiveGraphsFromServer(identifiers, settings);
		} catch (Exception e) {
			if (e instanceof NumberFormatException) {
				JOptionPane.showMessageDialog(socketCon.gui,
						"The data within the file seems to be not correct, because of\n" + e.getMessage());
			} else {
				e.printStackTrace();
				JOptionPane.showMessageDialog(socketCon.gui, "An error occured while transmitting data");
			}
		}
		// add it to the mixer
		for (int i = 0; i < gs.length; i++) {
			// System.err.println( "Description of graph from server is " + gs[i].getSetting(
			// "Description" ) );
			gui.getResultTree().getOnlineMixer().addFunction(gs[i]);
		}

	}

	/**
	 * set the console
	 * 
	 * @param console Container the BIGConsole's panel
	 */
	public void setConsolePanel(Container console) {
		if (getTabCount() == 4) {
			this.remove(3);
		}
		this.addTab("Console", console);
		revalidate();
	}
}
