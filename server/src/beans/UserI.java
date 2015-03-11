//Aleksandar Petrovic

package beans;

import java.util.Set;

public interface UserI {
	boolean login(String id, String pass);
	
	Set<String> getOnlineUsers();
	
	boolean sendGameRequest(String userId);
	String getGameRequest();
	boolean sendGameRequestResponse(String userId, GameI game);
	GameI getGameRequestResponse();

	void logout();
}
