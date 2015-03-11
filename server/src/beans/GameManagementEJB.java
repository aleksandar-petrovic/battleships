/*
 * @author Demjan Grubic 
 */

package beans;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
@Remote(GameManagementI.class)
public class GameManagementEJB implements GameManagementI {
	private static int gameId;
	
	@PersistenceContext(name = "Server")
	private EntityManager em;
	
	@EJB
	private OnlineUsers onlineUsers;
	
	static {
		gameId = 0;
	}

	@Override
	public GameI registerGame( String firstPlayerId, String secondPlayerId ) {
		UserEJB firstPlayer = onlineUsers.getEjb(firstPlayerId);
		UserEJB secondPlayer = onlineUsers.getEjb(secondPlayerId);
		
		// check if users exist and if they are online
		if ( firstPlayer == null || secondPlayer == null ) {
			return null;
		}
		
		// register new Game
		try {
			InitialContext ctx = new InitialContext();
			String name = "ejb:/Server//GameEJB!" + GameI.class.getName()+ "?stateful";
			GameI gameOnServer = (GameI) ctx.lookup(name);
			gameOnServer.Constructor( ++gameId, firstPlayer.getUserInfo(), secondPlayer.getUserInfo() );
			
			return gameOnServer;
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
}