package transit;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.simple.JSONObject;

public class BusData 
{
	public Coordinates coords;
	public float speed;
	public Date timeRecieved;
	public short busID;
	public long updateTime;
	
	public static SimpleDateFormat sdf = new SimpleDateFormat("KK:mm:ss a");
	
	public String toString()
	{ 
		return "[Bus " + this.busID + "] [" + sdf.format(this.timeRecieved) + "] "+ this.coords.toString() + " @ " + String.format("%.2f", this.speed) + " km/h";
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject toJSON()
	{
		JSONObject obj = new JSONObject();
		obj.put("id", this.busID);
		obj.put("degreesNorth", this.coords.toDecimalDegreesNorth());
		obj.put("degreesWest", this.coords.toDecimalDegreesWest() * -1);
		obj.put("lastUpdateTime", this.getSecondsSinceUpdate());
		return obj;
	}
	
	public int getSecondsSinceUpdate()
	{
		return Math.round((System.currentTimeMillis() - this.updateTime) / 1000F);
	}
	
	public static BusData parseBusData(String stringData)
	{
		String[] data = stringData.split(",");
		String northData = data[2];
		String westData = data[4];
		
		int degreesN = Integer.parseInt(northData.substring(0, 2));
		int minutesN = Integer.parseInt(northData.substring(2, 4));
		float secondsN = Float.parseFloat(northData.substring(4, northData.length())) * 60;
		
		int multiplier = 1;
		if (westData.indexOf(".") == 5) {
			westData = westData.substring(1);
			//multiplier = -1;
		}
		
		int degreesW = Integer.parseInt(westData.substring(0, 2)) * multiplier;
		int minutesW = Integer.parseInt(westData.substring(2, 4));
		float secondsW = Float.parseFloat(westData.substring(4, westData.length())) * 60;
		
		float speed = Float.parseFloat(data[6]) * 1.852F;
		
		BusData busData = new BusData();
		busData.coords = new Coordinates(degreesN, minutesN, secondsN, degreesW, minutesW, secondsW);
		busData.speed = speed;
		busData.timeRecieved = new Date();
		
		busData.busID = Short.parseShort(data[11].substring(1));
		busData.updateTime = System.currentTimeMillis();
		
		//System.out.println(busData.toString());
		return busData;
	}
	
	static class Coordinates
	{
		public int degreesN, degreesW;
		public int minutesN, minutesW;
		public float secondsN, secondsW;
		
		public Coordinates(int degreesN, int minutesN, float secondsN, int degreesW, int minutesW, float secondsW)
		{
			this.degreesN = degreesN;
			this.minutesN = minutesN;
			this.secondsN = secondsN;
			
			this.degreesW = degreesW;
			this.minutesW = minutesW;
			this.secondsW = secondsW;
		}
		
		public float toDecimalDegreesNorth()
		{
			return (float)this.degreesN + (this.minutesN / 60F) + (this.secondsN / 3600F);
		}
		
		public float toDecimalDegreesWest()
		{
			return (float)this.degreesW + (this.minutesW / 60F) + (this.secondsW / 3600F);
		}
		
		
		public String toString()
		{
			return toStringNorth() + " " + toStringWest();
		}
		
		public String toStringNorth()
		{
			return degreesN + "º" + minutesN + "'" + secondsN + "\"N";
		}
		
		public String toStringWest()
		{
			return degreesW + "º" + minutesW + "'" + secondsW + "\"W";
		}
	}
}