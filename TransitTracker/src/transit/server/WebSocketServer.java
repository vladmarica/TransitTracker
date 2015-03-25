package transit.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.server.WebSocketHandler;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;
import org.json.simple.JSONObject;

import transit.BusData;
import transit.Main;

public class WebSocketServer 
{
	public Server server;
	public ArrayList<Session> connections = new ArrayList<>();
	
	public WebSocketServer()
	{
		server = new Server(8080);
		
		WebSocketHandler wsHandler = new WebSocketHandler() 
		{
            @Override
            public void configure(WebSocketServletFactory factory) {
                factory.register(ConnectionHandler.class);
            }
        };
        
        server.setHandler(wsHandler);
	}
	
	public void onConnect(Session session)
	{
    	connections.add(session);
        System.out.println("Recieved connection from: " + session.getRemoteAddress().getAddress());
        sendInitialDataToClient(session);
	}
	
	public void onDisconnect(Session session)
	{
		connections.remove(session);
	}
	
	@SuppressWarnings("unchecked")
	public void sendBusUpdate(BusData data)
	{
		JSONObject obj = new JSONObject();
		obj.put(data.busID, data.toJSON());
		
		try {
			for (Session session : connections) {
				session.getRemote().sendString(obj.toJSONString());
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public void sendInitialDataToClient(Session session)
	{
		JSONObject obj = new JSONObject();
		HashMap<Short, BusData> busMap = Main.instance.busMap;
		Iterator<Short> iterator = busMap.keySet().iterator();
		
		while (iterator.hasNext()) {
			short id = iterator.next();
			BusData data = busMap.get(id);
			obj.put(id, data.toJSON());
		}
		
		try {
			session.getRemote().sendString(obj.toJSONString());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void start()
	{
		try {
			server.start();
			
			new Thread(new Runnable()
			{
				@Override
				public void run() {
					try {
						server.join();  
					}
					catch (Exception e) {
						e.printStackTrace();
					}
				}
				
			});
			 
		}
		catch (Exception  e) {
			e.printStackTrace();
		}
	}
}