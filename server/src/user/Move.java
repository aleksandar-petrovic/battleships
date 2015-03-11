/*
 * @author Demjan Grubic 
 */

package user;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

//Marko Kabic (queries)
@NamedQueries( {
@NamedQuery(name = "Move.getAllById", query = "SELECT m FROM Move m WHERE m.id = :id"),
@NamedQuery(name = "Move.getMovesByUsername", query = "SELECT m FROM Game g, Move m WHERE (g.firstPlayer.username = :username OR g.secondPlayer.username = :username) AND m MEMBER OF g.moves")
})
@Entity
public class Move implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private int id;
	
	private boolean firstPlayer;
	private int row;
	private int column;
	
	// Default constructor
	public Move() {}
	
	// Getters and setters
	public boolean getFirstPlayer() {
		return this.firstPlayer;
	}
	
	public void setFirstPlayer( boolean firstPlayer ) {
		this.firstPlayer = firstPlayer;
	}
	
	public int getRow() {
		return this.row;
	}
	
	public void setRow( int row ) {
		this.row = row;
	}
	
	public int getColumn() {
		return this.column;
	}
	
	public void setColumn( int column ) {
		this.column = column;
	}
}