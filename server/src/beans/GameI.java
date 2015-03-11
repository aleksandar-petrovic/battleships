/*
 * @author Demjan Grubic 
 */

package beans;

import java.awt.Color;
import java.awt.Point;
import java.util.List;
import user.UserInfo;
import game.ProjectileAttackInformation;

public interface GameI {
	void Constructor( int gameId, UserInfo firstPlayer, UserInfo secondPlayer );
	
	boolean playerWon( boolean firstPlayer );
	boolean gameOver();
	
	ProjectileAttackInformation makeMove( int row, int column );
	void SetUpShips( List<Integer> rows, List<Integer> columns, boolean firstPlayer );
	
	boolean firstPlayerPlaying();
	Point getLastMove();
	
	boolean settingUpMyShipsFinished(boolean firstPlayer);
	boolean settingUpOpponentShipsFinished(boolean firstPlayer);
	String getPlayerId( boolean firstPlayer );
	void setConnected( boolean firstPlayer );
	boolean getConnected( boolean firstPlayer );
	void setExited( boolean firstPlayer );
	boolean getExited( boolean firstPlayer );
	void setLastMoveColor(Color col);
	Color getLastMoveColor();
}