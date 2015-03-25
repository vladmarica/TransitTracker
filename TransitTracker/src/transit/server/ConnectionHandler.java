package transit.server;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import transit.Main;

@WebSocket
public class ConnectionHandler 
{
	public WebSocketServer server;
	
	public ConnectionHandler()
	{
		this.server = Main.instance.server;
	}
	
    @OnWebSocketClose
    public void onClose(Session session, int statusCode, String reason)
    {
    	server.onDisconnect(session);
    }

    @OnWebSocketError
    public void onError(Throwable t)
    {
        System.out.println("Error: " + t.getMessage());
    }

    @OnWebSocketConnect
    public void onConnect(Session session)
    {
    	server.onConnect(session);
    }
}