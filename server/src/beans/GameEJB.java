//@author Demjan Grubic

package beans;

import java.awt.Color;
import java.awt.Point;
import java.util.List;

import game.GameTable;
import game.ProjectileAttackInformation;

import javax.ejb.LocalBean;
import javax.ejb.Remote;
import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import user.Game;
import user.Move;
import user.UserInfo;

@Stateful
@Remote(GameI.class)
@LocalBean
public class GameEJB implements GameI {
	@PersistenceContext(name = "Server")
	private EntityManager entityManager;
	
	private int gameId;
	private String firstPlayerId;
	private String secondPlayerId;
	
	private GameTable firstPlayerGameTable;
	private GameTable secondPlayerGameTable;
	private boolean firstPlayerPlaying;
	
	private boolean firstPlayerConnected;
	private boolean secondPlayerConnected;
	private boolean firstPlayerExited;
	private boolean secondPlayerExited;
	
	Point lastMove;
	Color lastMoveColor;
	
	public GameEJB() {}
	
	@Override
	public void Constructor( int gameId, UserInfo firstPlayer, UserInfo secondPlayer ) {
		this.gameId = gameId;
		this.firstPlayerId = firstPlayer.getUsername();
		this.secondPlayerId = secondPlayer.getUsername();
		
		Game game = new Game();
		game.setGameId(this.gameId);
		game.setFirstPlayer(firstPlayer);
		game.setSecondPlayer(secondPlayer);
		entityManager.merge(game);
		
		// add game to user's list
		firstPlayer.addPlayerGame(game, true);
		entityManager.merge(firstPlayer);
		secondPlayer.addPlayerGame(game, false);
		entityManager.merge(secondPlayer);
		
		this.firstPlayerPlaying = true;
		this.lastMove = null;
		this.firstPlayerConnected = false;
		this.secondPlayerConnected = false;
		this.firstPlayerExited = false;
		this.secondPlayerExited = false;
	}
	
	@Override
	public boolean playerWon( boolean firstPlayer ) {
		if ( firstPlayer ) {
			return this.secondPlayerGameTable.gameOver();
		}
		else {
			return this.firstPlayerGameTable.gameOver();
		}
	}

	@Override
	public boolean gameOver() {
		return this.playerWon(true) || this.playerWon(false) || (firstPlayerExited) || (secondPlayerExited);
	}
	
	@Override
	public void SetUpShips( List<Integer> rows, List<Integer> columns, boolean firstPlayer ) {
		this.updateMoves( rows, columns, firstPlayer );
		
		if ( firstPlayer ) {
			this.firstPlayerGameTable = new GameTable( rows, columns );
		}
		else {
			this.secondPlayerGameTable = new GameTable( rows, columns );
		}
	}
	
	//Marko Kabic (metod)
	@Override
	public boolean settingUpMyShipsFinished(boolean firstPlayer) {
		if(firstPlayer)
			return firstPlayerGameTable != null;
		else
			return secondPlayerGameTable != null;
	}
	
	//Marko Kabic (metod)
	@Override
	public boolean settingUpOpponentShipsFinished(boolean firstPlayer) {
		if(firstPlayer)
			return secondPlayerGameTable != null;
		else
			return firstPlayerGameTable != null;
	}
	
	

	@Override
	public ProjectileAttackInformation makeMove( int row, int column ) {
		boolean firstPlayerAttacks = this.firstPlayerPlaying;
		this.updateMove(row, column, firstPlayerAttacks);
		lastMove = new Point( row, column );
		
		// always switch current player or switch only when missed?
		this.firstPlayerPlaying = !this.firstPlayerPlaying;
		
		if ( firstPlayerAttacks ) {
			return secondPlayerGameTable.attack( row, column );
		}
		else {
			return firstPlayerGameTable.attack( row, column );
		}
	}
	
	private void updateMoves( List<Integer> rows, List<Integer> columns, boolean firstPlayer ) {
		for (int i = 0; i < rows.size(); ++i) {
			int row = rows.get(i);
			int column = rows.get(i);
			
			this.updateMove(row, column, firstPlayer);
		}
	}

	private void updateMove( int row, int column, boolean firstPlayer ) {
		Move move = new Move();
		move.setRow(row);
		move.setColumn(column);
		move.setFirstPlayer(firstPlayer);
		entityManager.persist(move);
		
		Game game = entityManager.find(Game.class, this.gameId);
		game.addMove(move);
		entityManager.persist(game);
	}

	@Override
	public boolean firstPlayerPlaying() {
		return this.firstPlayerPlaying;
	}

	@Override
	public Point getLastMove() {
		return this.lastMove;
	}

	@Override
	public String getPlayerId(boolean firstPlayer) {
		if ( firstPlayer ) {
			return this.firstPlayerId;
		}
		else {
			return this.secondPlayerId;
		}
	}

	@Override
	public void setConnected(boolean firstPlayer) {
		if ( firstPlayer ) {
			this.firstPlayerConnected = true;
		}
		else {
			this.secondPlayerConnected = true;
		}
	}

	@Override
	public void setExited(boolean firstPlayer) {
		if ( firstPlayer ) {
			this.firstPlayerExited = true;
		}
		else {
			this.secondPlayerExited = true;
		}
		
		if ( this.firstPlayerExited && this.secondPlayerExited ) {
			this.RemoveThisBean();
		}
	}
	
	@Remove
	private void RemoveThisBean() {
	}

	@Override
	public boolean getConnected(boolean firstPlayer) {
		if ( firstPlayer ) {
			return this.firstPlayerConnected;
		}
		else {
			return this.secondPlayerConnected;
		}
	}

	@Override
	public boolean getExited(boolean firstPlayer) {
		if ( firstPlayer ) {
			return this.firstPlayerExited;
		}
		else {
			return this.secondPlayerExited;
		}
	}
	
	//Marko Kabic
	@Override
	public Color getLastMoveColor() {
		return lastMoveColor;
	}
	
	@Override
	public void setLastMoveColor(Color col) {
		lastMoveColor = col;
	}
	
}
	
