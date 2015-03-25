package transit.server;

import org.eclipse.jetty.websocket.api.Session;

public class SessionWrapper 
{
	public Session session;
	
	public SessionWrapper(Session session)
	{
		this.session = session;
	}
	
	public String toString()
	{
		return this.session.getRemoteAddress().getAddress().toString();
	}
}