package transit.reciever;
import java.util.ArrayList;

import transit.Main;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;

public class SerialPortListener implements SerialPortEventListener 
{
	private int eventBytes;
	private String totalData = "";
	private ArrayList<ValidDataListener> listeners;
	
	public SerialPortListener(int eventBytes)
	{
		this.eventBytes = eventBytes;
		this.listeners = new ArrayList<ValidDataListener>();
	}
	
	public void addValidDataListener(ValidDataListener listener)
	{
		this.listeners.add(listener);
	}
	
	public void setEventBytes(int eventBytes)
	{
		this.eventBytes = eventBytes;
	}
	
	@Override
	public void serialEvent(SerialPortEvent event) 
	{
		try {
			if (event.isRXCHAR() && event.getEventValue() > this.eventBytes) {
				
				byte[] b = Main.getSerialPort().readBytes(this.eventBytes);
				for (int i = 0; i < b.length; i++) {
					if (b[i] == 13 || b[i] == 10) {
						b[i] = 0;
					}
				}
				
				String data = new String(b);
				totalData += data;
				String[] split = totalData.split("\\$PKNDS,");
				if (split.length == 2) {
					if (isValidData(split[0])) {
						onCompleteDataStringRecieved(split[0]);
					}
					totalData = split[1];
				}
			}
			else if(event.isCTS()) {
				System.out.println("[CTS] Event recieved"); 
			}
			else if (event.isDSR()) {
				System.out.println("[DSR] Event recieved");
			}
		}
		
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void onCompleteDataStringRecieved(String data)
	{
		System.out.println(data);
		for (ValidDataListener l : this.listeners) {
			l.onValidDataRecieved(data);
		}
	}
	
	public boolean isValidData(String data)
	{
		return data.length() > 0 && data.split(",")[1].equals("A");
	}
	
	public interface ValidDataListener
	{
		public void onValidDataRecieved(String data);
	}
}