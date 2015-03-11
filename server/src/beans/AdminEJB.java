//@author Marko Kabic

package beans;

import java.util.List;
import javax.ejb.Remote;
import javax.ejb.Remove;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import user.Game;
import user.Move;
import user.UserInfo;

@Stateless
@Remote(AdminI.class)
public class AdminEJB implements AdminI {
	@PersistenceContext(name = "Server")
	private EntityManager em;

	@Override
	public List<Move> getAllMovesByUser(String username) {
		TypedQuery<Move> q = em.createNamedQuery("Move.getMovesByUsername",
				Move.class);
		q.setParameter("username", username);

		return q.getResultList();
	}

	@Override
	public List<Game> getAllGamesByUser(String username) {
		TypedQuery<Game> q = em.createNamedQuery("Game.getAllGamesByUsername",
				Game.class);
		q.setParameter("username", username);

		return q.getResultList();
	}

	@Remove
	@Override
	public boolean removeUser(UserI me, String user) {
		UserInfo userinfo = em.find(UserInfo.class, user);

		if (userinfo == null) 
			return false;
		
		if(me.getOnlineUsers().contains(user))
			return false;
		
		List<Game> gamesByThisUser = getAllGamesByUser(user);
		List<Move> movesByThisUser = getAllMovesByUser(user);
		
		for(Game game : gamesByThisUser) {
			if(game == null)
				break;
			
			UserInfo firstPlayer = game.getFirstPlayer();
			UserInfo secondPlayer = game.getSecondPlayer();
			UserInfo opponentPlayer;
			
			if(firstPlayer.getUsername().equals(user))
				opponentPlayer = secondPlayer;
			else
				opponentPlayer = firstPlayer;
			
			opponentPlayer.removeGame(game);
			
			em.merge(opponentPlayer);
		}
			
		for (Move move : movesByThisUser) {
			if(move == null)
				break;
			em.remove(move);
		}
			
		for (Game game : gamesByThisUser) {
			if(game == null)
				break;
			em.remove(game);
		}
		
		em.remove(userinfo);
		
		return true;
		
	}
}
