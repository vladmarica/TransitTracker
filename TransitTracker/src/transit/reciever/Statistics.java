package transit.reciever;
import java.util.ArrayList;

public class Statistics 
{
	public static Stat TotalClients = new Stat();
	public static Stat UpdatesRecieved = new Stat();
	
	public static class Stat
	{
		private int value = 0;
		private ArrayList<StatListener> listeners = new ArrayList<>();
		
		public void addListener(StatListener listener)
		{
			this.listeners.add(listener);
		}
		
		public void incrementValue()
		{
			this.value++;
			update();
		}
		
		public void decrementValue()
		{
			this.value--;
			update();
		}
		
		public void setValue(int value)
		{
			this.value = value;
			update();
		}
		
		public int getValue()
		{
			return this.value;
		}
		
		public void update()
		{
			for (StatListener listener : listeners) {
				listener.onStatUpdate(this);
			}
		}
	}
	
	public static interface StatListener
	{
		public void onStatUpdate(Stat stat);
	}
}