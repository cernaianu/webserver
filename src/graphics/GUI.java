package graphics;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import webserver.WebServer;
import webserver.WebServer.ServerState;

public class GUI {

	private JFrame frame;
	private boolean isMaintenanceValid;
	private boolean isRootValid;
	private JPanel mainPanel;
	private JPanel topPanels;
	private JPanel controlPanel;
	private JPanel infoPanel;
	private JPanel configPanel;
	private JButton connectButton;
	private JCheckBox maintenanceCheckBox;
	private JLabel serverStatusLabel;
	private JLabel serverStatusValueLabel;
	private JLabel serverAddressLabel;
	private JLabel serverAddressValueLabel;
	private JLabel serverPortLabel;
	private JLabel serverPortValueLabel;
	private JLabel configuringPortLabel;
	private JTextField configuringPortTextField;
	private JLabel configuringRootDirectoryLabel;
	private JTextField configuringRootDirectoryTextField;
	private JLabel configuringMaintenanceDirectoryLabel;
	private JTextField configuringMaintenanceDirectoryTextField;
	private JButton browseRootDirectoryButton;
	private JButton browseMaintenanceDirectoryButton;
	private ImageIcon validationIcon;
	private ImageIcon invalidationIcon;
	private JLabel rootValidationLabel;
	private JLabel maintenanceValidationLabel;

	private WebServer webServer;

	public static void main(String[] args) throws IOException {

		GUI gui =new GUI();
		gui.startApplication();
	}

	public void startApplication() {
		initializeComponents();
		connectButton.setEnabled(false);
		webServer = new WebServer(this);
	}

	private void initializeComponents()
	{
		frame = new JFrame("Web Server");
		
		setupValidationIcons();
		
		instantiateComponents();

		setComponentsGeneralProperties();
		
		configureListeners();
		
		setTopPanelLayout();

		setMainPanelLayout();

		setControlPanelLayout();

		setInfoPanelLayout();

		setConfigPanelLayout();

		setupPanelFrameStructure();

		configureFrame();
		
	}

	public void configureFrame() {
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setPreferredSize(new Dimension(500, 300));
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	public void setupPanelFrameStructure() {
		topPanels.add(infoPanel);
		topPanels.add(controlPanel);

		mainPanel.add(topPanels);
		mainPanel.add(configPanel);

		frame.add(mainPanel);
	}

	public void setConfigPanelLayout() {
		configPanel.setBorder(BorderFactory.createTitledBorder("Server configuration:"));
		configPanel.setLayout(new GridBagLayout());

		GridBagConstraints configLayoutConstraints = new GridBagConstraints();
		configLayoutConstraints.insets = new Insets(5,5,5,5);
		configLayoutConstraints.fill = GridBagConstraints.HORIZONTAL;
		configLayoutConstraints.gridx = 0;
		configLayoutConstraints.gridy = 0;
		configLayoutConstraints.weightx = 1;
		configPanel.add(configuringPortLabel, configLayoutConstraints);

		configLayoutConstraints.fill = GridBagConstraints.HORIZONTAL;
		configLayoutConstraints.gridx = 1;
		configLayoutConstraints.gridy = 0;
		configLayoutConstraints.weightx = 1;
		configPanel.add(configuringPortTextField, configLayoutConstraints);

		configLayoutConstraints.fill = GridBagConstraints.HORIZONTAL;
		configLayoutConstraints.gridx = 0;
		configLayoutConstraints.gridy = 1;
		configLayoutConstraints.weightx = 0;
		configPanel.add(configuringRootDirectoryLabel, configLayoutConstraints);

		configLayoutConstraints.fill = GridBagConstraints.HORIZONTAL;
		configLayoutConstraints.gridx = 1;
		configLayoutConstraints.gridy = 1;

		configLayoutConstraints.weightx = 0;
		configPanel.add(configuringRootDirectoryTextField, configLayoutConstraints);

		configLayoutConstraints.fill = GridBagConstraints.HORIZONTAL;
		configLayoutConstraints.gridx = 2;
		configLayoutConstraints.gridy = 1;

		configLayoutConstraints.weightx = 0;
		configPanel.add(browseRootDirectoryButton, configLayoutConstraints);

		configLayoutConstraints.fill = GridBagConstraints.HORIZONTAL;
		configLayoutConstraints.gridx = 0;
		configLayoutConstraints.gridy = 2;
		configLayoutConstraints.weightx = 0;

		configPanel.add(configuringMaintenanceDirectoryLabel, configLayoutConstraints);

		configLayoutConstraints.fill = GridBagConstraints.HORIZONTAL;
		configLayoutConstraints.gridx = 1;
		configLayoutConstraints.gridy = 2;

		configLayoutConstraints.weightx = 0;
		configPanel.add(configuringMaintenanceDirectoryTextField, configLayoutConstraints);

		configLayoutConstraints.fill = GridBagConstraints.HORIZONTAL;
		configLayoutConstraints.gridx = 2;
		configLayoutConstraints.gridy = 2;

		configLayoutConstraints.weightx = 0;
		configPanel.add(browseMaintenanceDirectoryButton, configLayoutConstraints);

		configLayoutConstraints.fill = GridBagConstraints.HORIZONTAL;
		configLayoutConstraints.gridx = 3;
		configLayoutConstraints.gridy = 2;

		configLayoutConstraints.weightx = 0;
		configPanel.add(maintenanceValidationLabel, configLayoutConstraints);

		configLayoutConstraints.fill = GridBagConstraints.HORIZONTAL;
		configLayoutConstraints.gridx = 3;
		configLayoutConstraints.gridy = 1;

		configLayoutConstraints.weightx = 0;
		configPanel.add(rootValidationLabel, configLayoutConstraints);
	}

	public void setInfoPanelLayout() {
		infoPanel.setBorder(BorderFactory.createTitledBorder("Server information:"));
		infoPanel.setLayout(new GridLayout(3,2));
		infoPanel.add(serverStatusLabel);
		infoPanel.add(serverStatusValueLabel);
		infoPanel.add(serverAddressLabel);
		infoPanel.add(serverAddressValueLabel);
		infoPanel.add(serverPortLabel);
		infoPanel.add(serverPortValueLabel);
	}

	public void setControlPanelLayout() {
		controlPanel.setBorder(BorderFactory.createTitledBorder("Server control:"));
		controlPanel.setLayout(new GridLayout(2,1));
		controlPanel.add(connectButton);
		controlPanel.add(maintenanceCheckBox);
	}

	public void setMainPanelLayout() {
		mainPanel.setLayout(new GridLayout(2,1));
	}

	public void setTopPanelLayout() {
		topPanels.setLayout(new BoxLayout(topPanels, BoxLayout.X_AXIS));
	}

	public void setComponentsGeneralProperties() {
		maintenanceCheckBox.setText("Maintenance mode");
		connectButton.setToolTipText("Setup valid root and maintenance directory!");
		configuringPortTextField.setToolTipText("Enter a port number [1->65535]");
		configuringRootDirectoryTextField.setToolTipText("Enter root directory. Must contain index.html to be valid!");
		configuringMaintenanceDirectoryTextField.setToolTipText("Enter maintenance directory. Must contain maintenance.html to be valid!");
	}

	public void instantiateComponents() {
		mainPanel = new JPanel();
		topPanels = new JPanel();
		controlPanel = new JPanel();
		infoPanel = new JPanel();
		configPanel = new JPanel();
		//connectButton = new JButton("Connect"); //will add icon later
		connectButton = new JButton();

		maintenanceCheckBox = new JCheckBox();
		
		serverStatusLabel = new JLabel("Server status:");
		serverStatusValueLabel = new JLabel("not running");
		serverAddressLabel = new JLabel("Server address:");
		serverAddressValueLabel = new JLabel("not running");
		serverPortLabel = new JLabel("Server port:");
		serverPortValueLabel = new JLabel("not running");
		configuringPortLabel = new JLabel("Port:");
		configuringPortTextField = new JTextField();
		configuringRootDirectoryLabel = new JLabel("Root directory:");
		configuringRootDirectoryTextField = new JTextField();
		configuringMaintenanceDirectoryLabel = new JLabel("Maintenance directory:");
		configuringMaintenanceDirectoryTextField = new JTextField();
		browseRootDirectoryButton = new JButton("...");
		browseMaintenanceDirectoryButton = new JButton("...");
	}

	public void setupValidationIcons() {
		validationIcon = new ImageIcon("resources/ok.png");
		Image validationImage = validationIcon.getImage();
		Image newValidationImage = validationImage.getScaledInstance(32, 32, Image.SCALE_SMOOTH);
		validationIcon = new ImageIcon(newValidationImage);

		invalidationIcon = new ImageIcon("resources/notok.png");
		Image invalidationImage = invalidationIcon.getImage();
		Image newInvalidationImage = invalidationImage.getScaledInstance(32, 32, Image.SCALE_SMOOTH);
		invalidationIcon = new ImageIcon(newInvalidationImage);
		rootValidationLabel = new JLabel();
		maintenanceValidationLabel = new JLabel();
	}

	public void configureListeners() 
	{
		configureActionListeners();
		configureFocusListeners();
	}

	public void configureFocusListeners() {
		configurePortComponentFocusListener();
		configureRootComponentFocusListener();
		configureMaintenanceComponentFocusListener();
	}

	public void configureActionListeners() 
	{
		configureConnectionComponentActionListener();
		configurePortComponentActionListener();
		configureRootComponentsActionListeners();
		configureMaintenanceComponentsActionListeners();
	}
	
	public void configureMaintenanceComponentFocusListener() {
		configuringMaintenanceDirectoryTextField.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent e) {

			}

			@Override
			public void focusLost(FocusEvent e) {
				try
				{
					if(configuringMaintenanceDirectoryTextField.getText().equals(""))
						return;

					webServer.setMaintenanceDirectory(configuringMaintenanceDirectoryTextField.getText());
					if(webServer.validateMaintenance())
					{
						maintenanceValidationLabel.setIcon(validationIcon);
						isMaintenanceValid = true;
						setupValidVisualState();
					}
					else {

						maintenanceValidationLabel.setIcon(invalidationIcon);
						isMaintenanceValid = false;
						setupInvalidVisualState();
					}
				}
				catch(Exception e1)
				{
					return;
				}
			}

		});
	}

	public void configureRootComponentFocusListener() {
		configuringRootDirectoryTextField.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent e) {

			}

			@Override
			public void focusLost(FocusEvent e) {
				try
				{
					if(configuringRootDirectoryTextField.getText().equals(""))
					{
						return;
					}
					webServer.setRootDirectory(configuringRootDirectoryTextField.getText());
					if(webServer.validateRoot())
					{
						rootValidationLabel.setIcon(validationIcon);
						isRootValid = true;
						setupValidVisualState();
					}
					else {

						rootValidationLabel.setIcon(invalidationIcon);
						isRootValid = false;
						setupInvalidVisualState();
					}
				}
				catch(Exception e1)
				{
					return;
				}
			}

		});
	}

	public void configurePortComponentFocusListener() {
		configuringPortTextField.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent e) {

			}

			@Override
			public void focusLost(FocusEvent e) {
					if(configuringPortTextField.getText().equals(""))
						return;
					
					webServer.setPortNumber(Integer.parseInt(configuringPortTextField.getText()));
				
				
			}

		});
	}
	
	public void configureConnectionComponentActionListener() {
		connectButton.addActionListener(new ConnectionButtonListener());
	}

	public void configurePortComponentActionListener() {
		configuringPortTextField.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
					if(configuringPortTextField.getText().equals(""))
						return;
				
					webServer.setPortNumber(Integer.parseInt(configuringPortTextField.getText()));
			}

		});
	}

	public void configureRootComponentsActionListeners() {
		configureRootDirectoryTextFieldActionListener();
		configureBrowseRootDirectoryButtonActionListener();
	}

	public void configureBrowseRootDirectoryButtonActionListener() {
		browseRootDirectoryButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();

				fileChooser.setCurrentDirectory(new File("."));
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				fileChooser.setAcceptAllFileFilterUsed(false);
				int returnVal = fileChooser.showOpenDialog(null);
				if(returnVal == JFileChooser.APPROVE_OPTION)
				{
					configuringRootDirectoryTextField.setText(fileChooser.getSelectedFile().getName());
					webServer.setRootDirectory(configuringRootDirectoryTextField.getText());
					if(webServer.validateRoot())
					{
						rootValidationLabel.setIcon(validationIcon);
						isRootValid = true;
						setupValidVisualState();
					}
					else {

						rootValidationLabel.setIcon(invalidationIcon);
						isRootValid = false;
						setupInvalidVisualState();
					}
				}
			}

		});
	}

	public void configureRootDirectoryTextFieldActionListener() {
		configuringRootDirectoryTextField.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				try
				{
					webServer.setRootDirectory(configuringRootDirectoryTextField.getText());
					if(webServer.validateRoot())
					{
						rootValidationLabel.setIcon(validationIcon);
						isRootValid = true;
						setupValidVisualState();
					}
					else {

						rootValidationLabel.setIcon(invalidationIcon);
						isRootValid = false;
						setupInvalidVisualState();
					}
				}
				catch(Exception e1)
				{
					return;
				}

			}

		});
	}

	public void configureMaintenanceComponentsActionListeners() {
		configureMaintenanceDirectoryTextFieldActionListener();
		configureBrowseMaintenanceDirectoryButtonActionListener();
		configureMaintenanceCheckBoxActionListener();
	}

	public void configureMaintenanceCheckBoxActionListener() {
		maintenanceCheckBox.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(maintenanceCheckBox.isSelected())
					webServer.setServerState(ServerState.MAINTENANCE);
				else webServer.setServerState(ServerState.CONNECTED);

			}

		});
	}

	public void configureBrowseMaintenanceDirectoryButtonActionListener() {
		browseMaintenanceDirectoryButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();

				fileChooser.setCurrentDirectory(new File("."));
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				fileChooser.setAcceptAllFileFilterUsed(false);
				int returnVal = fileChooser.showOpenDialog(null);
				if(returnVal == JFileChooser.APPROVE_OPTION)
				{
					configuringMaintenanceDirectoryTextField.setText(fileChooser.getSelectedFile().getName());
					webServer.setMaintenanceDirectory(configuringMaintenanceDirectoryTextField.getText());
					if(webServer.validateMaintenance())
					{
						isMaintenanceValid = true;
						maintenanceValidationLabel.setIcon(validationIcon);
						setupValidVisualState();
					}
					else
					{
						isMaintenanceValid = false;
						maintenanceValidationLabel.setIcon(invalidationIcon);
						setupInvalidVisualState();
					}
				}

			}

		});
	}

	public void configureMaintenanceDirectoryTextFieldActionListener() {
		configuringMaintenanceDirectoryTextField.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				try
				{
					webServer.setMaintenanceDirectory(configuringMaintenanceDirectoryTextField.getText());
					if(webServer.validateMaintenance())
					{
						maintenanceValidationLabel.setIcon(validationIcon);
						isMaintenanceValid = true;
						setupValidVisualState();
					}
					else {

						maintenanceValidationLabel.setIcon(invalidationIcon);
						isMaintenanceValid = false;
						setupInvalidVisualState();
					}
				}
				catch(Exception e1)
				{
					return;
				}
			}

		});
	}

	public void switchState(WebServer.ServerState state)
	{
		switch(state)
		{
		case STOPPED: modifyControlsForStoppedState(); break;
		case CONNECTED:modifyControlsForConnectedState(); break;

		case MAINTENANCE:modifyControlsForMaintenanceState(); break;

		default:
			break;
		}
	}

	
	

	private void modifyControlsForStoppedState()
	{
		frame.setTitle("Web Server:[Stopped]");
		connectButton.setText("Connect");

		serverStatusValueLabel.setText("not running");
		serverAddressValueLabel.setText("not running");
		serverPortValueLabel.setText("not running");
		configuringPortTextField.setEnabled(true);
		configuringRootDirectoryTextField.setEnabled(true);
		configuringMaintenanceDirectoryTextField.setEnabled(true);
		connectButton.setActionCommand("Connect");
		maintenanceCheckBox.setEnabled(false);
		maintenanceCheckBox.setSelected(false);
	}

	private void modifyControlsForConnectedState()
	{
		frame.setTitle("Web Server:[Connected]");
		connectButton.setText("Stop");
		connectButton.setActionCommand("Stop");
		maintenanceCheckBox.setEnabled(true);
		serverStatusValueLabel.setText("running...");
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		serverAddressValueLabel.setText(webServer.getAddress());
		serverPortValueLabel.setText(Integer.toString(webServer.getPortNumber()));
		configuringPortTextField.setEnabled(false);
		configuringRootDirectoryTextField.setEnabled(false);
		configuringMaintenanceDirectoryTextField.setEnabled(true);
	}

	private void modifyControlsForMaintenanceState()
	{
		frame.setTitle("Web Server:[Maintenance]");
		connectButton.setText("Stop");
		connectButton.setActionCommand("Stop");
		maintenanceCheckBox.setEnabled(true);
		serverStatusValueLabel.setText("running...");

		serverAddressValueLabel.setText(webServer.getAddress());
		serverPortValueLabel.setText(Integer.toString(webServer.getPortNumber()));
		configuringPortTextField.setEnabled(false);
		configuringRootDirectoryTextField.setEnabled(true);
		configuringMaintenanceDirectoryTextField.setEnabled(false);
	}

	public void setupValidVisualState() {
		if(isMaintenanceValid && isRootValid)
		{
			if(webServer.getServerState() == ServerState.MAINTENANCE)
			{
				maintenanceCheckBox.setEnabled(true);
			}
			else if(webServer.getServerState() == ServerState.CONNECTED)
			{
				maintenanceCheckBox.setEnabled(true);
			}
			else if(webServer.getServerState() == ServerState.STOPPED)
			{
				maintenanceCheckBox.setEnabled(false);
				connectButton.setEnabled(true);
			}
		}
	}

	public void setupInvalidVisualState() {
		if(webServer.getServerState() == ServerState.MAINTENANCE)
		{
			maintenanceCheckBox.setEnabled(false);
		}
		else if(webServer.getServerState() == ServerState.CONNECTED)
		{
			maintenanceCheckBox.setEnabled(false);
		}
		else if(webServer.getServerState() == ServerState.STOPPED)
		{
			maintenanceCheckBox.setEnabled(false);
			connectButton.setEnabled(false);
		}
	}
	
	private class ConnectionButtonListener implements ActionListener, Runnable
	{		
		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getActionCommand().equals("Connect"))
			{

				Thread thread = new Thread(this);

				thread.start();


			}
			else if(e.getActionCommand().equals("Stop"))
			{
				webServer.closeServer();
			}
		}

		@Override
		public void run() {
			webServer.startUpServer();

		}
	}
}
