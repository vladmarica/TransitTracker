package transit;

import static jssc.SerialPort.BAUDRATE_9600;
import static jssc.SerialPort.DATABITS_8;
import static jssc.SerialPort.PARITY_NONE;
import static jssc.SerialPort.STOPBITS_1;

import java.awt.Desktop;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JOptionPane;

import transit.reciever.SerialPortListener;
import transit.server.*;

import jssc.SerialPort;
import jssc.SerialPortList;

public class Main implements SerialPortListener.ValidDataListener
{
	public static Main instance;
	public SerialPort port;
	public boolean guiEnabled;
	public Window window;
	public WebSocketServer server;
	public HashMap<Short, BusData> busMap = new HashMap<Short, BusData>();
	
	public DefaultListModel<BusData> dataModel;
	
	public Main(boolean guiEnabled)
	{
		this(guiEnabled, null);
	}
	
	public Main(boolean guiEnabled, String portId)
	{
		this.guiEnabled = guiEnabled;
		
		//create and start the WebSocket server
		server = new WebSocketServer();
		server.start();
		
		if (this.guiEnabled) {
			dataModel = new DefaultListModel<BusData>();
			
			//create and show the window
			window = new Window(this);
			window.setLocationRelativeTo(null);
			window.setVisible(true);
			
			window.dataList.setModel(dataModel);
			window.connectionsList.setModel(server.connections);
			
			if (SerialPortList.getPortNames().length == 0) {
				System.out.println("No serial ports found!");
				JOptionPane.showMessageDialog(null, "No serial ports found!", "Error!", JOptionPane.ERROR_MESSAGE);
				System.exit(0);
			}
			
			window.dataList.addMouseListener(new MouseAdapter() {

				public void mouseClicked(MouseEvent evt) {
					@SuppressWarnings("unchecked")
					JList<BusData> list = (JList<BusData>)evt.getSource();
					if (evt.getClickCount() == 2) {
						try {
							BusData data = dataModel.get(list.locationToIndex(evt.getPoint()));
							String url = "https://www.google.ca/maps/place/" + data.coords.toStringNorth() + "+" + data.coords.toStringWest();
							url = url.replaceAll("º", "%C2%B0");
							url = url.replaceAll("\"", "%22");
							System.out.println(url);
							
							URI uri = new URL(url).toURI();
							Desktop.getDesktop().browse(uri);
						}
						catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				
			});
		}
		else if (portId != null) {
			boolean success = openPort(portId, SerialPort.MASK_RXCHAR, 50);
			if (success) {
				System.out.println("Listening to port " + portId);
			}
			else {
				System.out.println("Failed to open port " + portId);
				server.stop();
			}
		}
	}
	
	public boolean openPort(String portName, int eventMask, int eventBytes) 
	{
		if (port == null || !port.isOpened()) {
			try {
				port = new SerialPort(portName);
				port.openPort();
				port.setParams(BAUDRATE_9600, DATABITS_8, STOPBITS_1, PARITY_NONE);
				
				SerialPortListener spl = new SerialPortListener(eventBytes);
				spl.addValidDataListener(this);
				
				port.addEventListener(spl, eventMask);
				return port.isOpened();
			}
			catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		return false;
	}
	
	public void closePort()
	{
		if (port != null && port.isOpened()) {
			try {
				port.closePort();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static SerialPort getSerialPort()
	{
		return Main.instance.port;
	}
	
	public void onValidDataRecieved(String data)
	{
		BusData busData = BusData.parseBusData(data);
		busMap.put(busData.busID, busData);
		server.sendBusUpdate(busData);
		
		if (this.guiEnabled) {
			dataModel.addElement(BusData.parseBusData(data));
			window.dataList.ensureIndexIsVisible(dataModel.size() - 1);
			window.lblUniqueBuses.setText("Unique Buses: " + busMap.keySet().size());
		}
	}
	
	public static void printPorts()
	{
		String[] portNames = SerialPortList.getPortNames();
		
		String ports = "[";
		for (int i = 0; i < portNames.length; i++) {
			ports += portNames[i];
			if (i < portNames.length - 1) {
				ports += ", ";
			}
		}
		ports += "]";
		
		System.out.println("Found " + portNames.length + " COM ports: " + ports);
	}
	
	public static void main(String[] args) 
	{
		if (args.length == 0) {
			instance = new Main(true);
		}
		else if (args.length == 1 && args[0].equals("-nogui")) {
			System.out.println("Please launch like 'java -jar TransitTracker.jar -nogui <port_id>'");
			printPorts();
		}
		else if (args.length == 2 && args[0].equals("-nogui")) {
			instance = new Main(false, args[1]);
		}
		else {
			System.out.println("Invalid launch options");
		}
	}
}