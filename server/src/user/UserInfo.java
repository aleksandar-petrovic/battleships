/*
 * @author Demjan Grubic 
 */

package user;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

//Marko Kabic (query)
@NamedQuery(name = "UserInfo.getByUsername", query = "SELECT u FROM UserInfo u WHERE u.username = :user")
@Entity
public class UserInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	String username;
	
	String pass;
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "firstPlayer")
	private List<Game> firstPlayerGames;
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "secondPlayer")
	private List<Game> secondPlayerGames;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPass() {
		return pass;
	}
	public void setPass(String pass) {
		this.pass = pass;
	}
	
	public void removeGame(Game game) {
		if(game == null)
			return;
		firstPlayerGames.remove(game);
		secondPlayerGames.remove(game);
	}
	
	public void addPlayerGame(Game game, boolean firstPlayerGame) {
		if ( firstPlayerGame ) {
			this.firstPlayerGames.add(game);
		}
		else {
			this.secondPlayerGames.add(game);
		}
	}
	
	public List<Game> getFirstPlayerGames() {
		return this.firstPlayerGames;
	}
	
	public List<Game> getSecondPlayerGames() {
		return this.secondPlayerGames;
	}
	
	public List<Game> getPlayerMatches() {
		List<Game> returnList = new ArrayList<Game>( this.firstPlayerGames );
		returnList.addAll( this.secondPlayerGames );
		return returnList;
	}
}
