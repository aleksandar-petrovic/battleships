/*
 * @author Demjan Grubic 
 */

package user;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

//Marko Kabic (query)
@NamedQuery(name = "Game.getAllGamesByUsername", query = "SELECT g FROM Game g WHERE g.firstPlayer.username = :username OR g.secondPlayer.username = :username")
@Entity
public class Game implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	private int gameId;
	@ManyToOne
	private UserInfo firstPlayer;
	@ManyToOne
	private UserInfo secondPlayer;
	@OneToMany(fetch = FetchType.EAGER)
	private List<Move> moves;
	
	// Getters and Setters
	public int getGameId() {
		return this.gameId;
	}
	
	public void setGameId( int gameId ) {
		this.gameId = gameId;
	}
	
	public UserInfo getFirstPlayer() {
		return this.firstPlayer;
	}
	
	public void setFirstPlayer( UserInfo firstPlayer ) {
		this.firstPlayer = firstPlayer;
	}
	
	public UserInfo getSecondPlayer() {
		return this.secondPlayer;
	}
	
	public void setSecondPlayer( UserInfo secondPlayer ) {
		this.secondPlayer = secondPlayer;
	}
	
	public List<Move> getMoves() {
		return this.moves;
	}
	
	public void setMoves( List<Move> moves ) {
		this.moves = moves;
	}
	
	public void addMove( Move move ) {
		this.moves.add( move );
	}
	
	public int getNumberOfMoves() {
		return this.moves.size();
	}
}