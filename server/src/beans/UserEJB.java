//Aleksandar Petrovic

package beans;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Remote;
import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import user.UserInfo;

@Stateful
@Remote(UserI.class)
@LocalBean
public class UserEJB implements UserI {

	@PersistenceContext(name = "Server")
	private EntityManager em;

	@EJB
	private OnlineUsers onlineUsers;
	private String myId;
	
	private List<String> gameRequests;
	private List<GameI> gameRequestResponses;
	
	public UserEJB() {
		this.gameRequests = Collections.synchronizedList( new LinkedList<String>() );
		this.gameRequestResponses = Collections.synchronizedList( new LinkedList<GameI>() );
	}

	@Override
	public boolean login(String id, String pass) {
		UserInfo user = em.find(UserInfo.class, id);
		if (user == null || !user.getPass().equals(pass))
			throw new IllegalArgumentException("Invalid id or password.");
		
		if(onlineUsers.getOnline().contains(id))
			return false;
		
		myId = id;
		onlineUsers.onLogin(myId, this);
		return true;
	}
	
	public UserInfo getUserInfo() {
		return em.find(UserInfo.class, this.myId);
	}

	@Override
	public Set<String> getOnlineUsers() {
		if (myId == null)
			return null;
		Set<String> online = onlineUsers.getOnline();
		online.remove(myId);
		return online;
	}

	@Override
	@Remove
	public void logout() {
		if (myId != null)
		{
			onlineUsers.onLogout(myId);
			myId = null;
		}
	}

	@Override
	public boolean sendGameRequest(String userId) {
		UserEJB user = onlineUsers.getEjb(userId);
		
		if ( user == null ) {
			return false;
		}
		
		user.addGameRequestToQueue(this.myId);
		return true;
	}
	
	private void addGameRequestToQueue(String userId) {
		this.gameRequests.add(userId);
	}
	
	private void addGameRequestResponse(GameI game) {
		this.gameRequestResponses.add(game);
	}

	@Override
	public String getGameRequest() {
		if ( this.gameRequests.isEmpty() ) {
			return null;
		}
		
		return this.gameRequests.remove(0);
	}

	@Override
	public boolean sendGameRequestResponse(String userId, GameI game) {
		UserEJB user = onlineUsers.getEjb(userId);
		
		if ( user == null ) {
			return false;
		}
		
		user.addGameRequestResponse(game);
		return true;
	}

	@Override
	public GameI getGameRequestResponse() {
		if ( this.gameRequestResponses.isEmpty() ) {
			return null;
		}
		
		return this.gameRequestResponses.remove(0);
	}
}
