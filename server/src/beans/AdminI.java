//@author Marko Kabic

package beans;

import java.util.List;
import user.Game;
import user.Move;

public interface AdminI {
	List<Move> getAllMovesByUser(String username);
	List<Game> getAllGamesByUser(String username);
	public boolean removeUser(UserI me, String user);
	
}
