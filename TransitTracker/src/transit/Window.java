package transit;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import jssc.SerialPort;
import jssc.SerialPortList;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

public class Window extends JFrame 
{
	private static final long serialVersionUID = 7043916852613662866L;
	private JPanel contentPane;
	public JButton btnOpenClosePort;
	public JComboBox<String> cmbSelectPort;
	private JPanel rawInputPanel;
	private JPanel settingsPanel;
	private JPanel portSelectorPanel;
	private JButton btnSaveSettings;
	private JCheckBox chkRXCHAR;
	private JCheckBox chkDSR;
	private JCheckBox chkCTS;
	private JComboBox<String> cmbEventBytes;
	public JList<BusData> dataList;
	public JLabel lblUniqueBuses;
	private JPanel statsPanel;
	public JLabel lblUpdatesRecieved;
	public JLabel lblTotalClients;
	
	static
	{
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public Window() 
	{	
		setTitle("Transit Tracker");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 790, 479);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		portSelectorPanel = new JPanel();
		portSelectorPanel.setBorder(new TitledBorder(null, "Port", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		portSelectorPanel.setBounds(10, 11, 175, 43);
		contentPane.add(portSelectorPanel);
		portSelectorPanel.setLayout(null);
		
		btnOpenClosePort = new JButton("Open");
		btnOpenClosePort.setBounds(107, 12, 59, 23);
		btnOpenClosePort.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent event) {
				if (Main.getSerialPort() == null || !Main.getSerialPort().isOpened()) {
					String portName = cmbSelectPort.getItemAt(cmbSelectPort.getSelectedIndex());
					int eventBytes = Integer.parseInt(cmbEventBytes.getItemAt(cmbEventBytes.getSelectedIndex()));
					
					int eventMask = 0;
					if (chkRXCHAR.isSelected()) {eventMask += SerialPort.MASK_RXCHAR;}
					if (chkDSR.isSelected()) {eventMask += SerialPort.MASK_DSR;}
					if (chkCTS.isSelected()) {eventMask += SerialPort.MASK_CTS;}
					
					if (Main.instance.openPort(portName, eventMask, eventBytes)) {
						cmbSelectPort.setEnabled(false);
						btnOpenClosePort.setText("Close");
					}
				}
				else if(Main.getSerialPort().isOpened()) {
					cmbSelectPort.setEnabled(true);
					btnOpenClosePort.setText("Open");
					Main.instance.closePort();
				}
			}
			
		});
		portSelectorPanel.add(btnOpenClosePort);
		
		cmbSelectPort = new JComboBox<String>();
		cmbSelectPort.setModel(new DefaultComboBoxModel<String>(SerialPortList.getPortNames()));
		cmbSelectPort.setBounds(10, 14, 87, 20);
		portSelectorPanel.add(cmbSelectPort);
		
		rawInputPanel = new JPanel();
		rawInputPanel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Recieved Data", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		rawInputPanel.setBounds(10, 65, 503, 265);
		contentPane.add(rawInputPanel);
		rawInputPanel.setLayout(null);
		
		dataList = new JList<BusData>();
		dataList.setBorder(new LineBorder(new Color(0, 0, 0)));
		dataList.setBounds(10, 11, 519, 232);
		JScrollPane pane = new JScrollPane(dataList);
		pane.setBounds(new Rectangle(10, 21, 483, 222));
		pane.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {  
			public void adjustmentValueChanged(AdjustmentEvent e) {  
				e.getAdjustable().setValue(e.getAdjustable().getMaximum());  
		    }
		});

		rawInputPanel.add(pane);
		
		settingsPanel = new JPanel();
		settingsPanel.setLayout(null);
		settingsPanel.setBorder(new TitledBorder(null, "Settings", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		settingsPanel.setBounds(10, 341, 254, 104);
		contentPane.add(settingsPanel);
		
		btnSaveSettings = new JButton("Save");
		btnSaveSettings.setEnabled(false);
		btnSaveSettings.setBounds(185, 70, 59, 23);
		settingsPanel.add(btnSaveSettings);
		
		cmbEventBytes = new JComboBox<String>();
		cmbEventBytes.setModel(new DefaultComboBoxModel<String>(new String[] {"10", "20", "30", "40", "50", "60", "70", "80", "90", "100"}));
		cmbEventBytes.setBackground(Color.LIGHT_GRAY);
		cmbEventBytes.setBounds(78, 21, 50, 20);
		settingsPanel.add(cmbEventBytes);
		
		JLabel lblEventBytes = new JLabel("Event Bytes:");
		lblEventBytes.setBounds(10, 24, 69, 14);
		settingsPanel.add(lblEventBytes);
		
		JLabel lblEventMask = new JLabel("Event Mask:");
		lblEventMask.setBounds(10, 49, 69, 14);
		settingsPanel.add(lblEventMask);
		
		chkRXCHAR = new JCheckBox("RXCHAR");
		chkRXCHAR.setSelected(true);
		chkRXCHAR.setBounds(78, 45, 69, 23);
		settingsPanel.add(chkRXCHAR);
		
		chkDSR = new JCheckBox("DSR");
		chkDSR.setBounds(147, 45, 45, 23);
		settingsPanel.add(chkDSR);
		
		chkCTS = new JCheckBox("CTS");
		chkCTS.setBounds(199, 45, 45, 23);
		settingsPanel.add(chkCTS);
		
		statsPanel = new JPanel();
		statsPanel.setLayout(null);
		statsPanel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Stats", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		statsPanel.setBounds(274, 341, 239, 104);
		contentPane.add(statsPanel);
		
		lblUniqueBuses = new JLabel("Unique Buses: 0");
		lblUniqueBuses.setBounds(22, 25, 222, 14);
		statsPanel.add(lblUniqueBuses);
		
		lblUpdatesRecieved = new JLabel("Updates Recieved: 0");
		lblUpdatesRecieved.setBounds(22, 42, 222, 14);
		statsPanel.add(lblUpdatesRecieved);
		
		lblTotalClients = new JLabel("Total Clients: 0");
		lblTotalClients.setBounds(22, 59, 222, 14);
		statsPanel.add(lblTotalClients);
		
		JSeparator separator = new JSeparator();
		separator.setOrientation(SwingConstants.VERTICAL);
		separator.setBounds(522, 11, 9, 429);
		contentPane.add(separator);
	}
}