//@authors Demjan Grubic, Marko Kabic

package game;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import javax.swing.JFrame;
import figures.AbstractFigure;
import figures.PlaceFigures;
import beans.GameI;

public class Game implements Runnable {
	private static final long SleepTime = 10;

	private String opponentUser;
	private boolean firstPlayer;
	private GameI gameOnServer;

	private GameState gameState;

	private JFrame window;
	private String title;
	JFrame end;

	public Game(String user, String opponentUser, boolean firstPlayer,
			GameI gameOnServer) {
		this.opponentUser = opponentUser;
		this.firstPlayer = firstPlayer;
		this.gameOnServer = gameOnServer;

		this.gameOnServer.setConnected(this.firstPlayer);
		gameState = GameState.WaitingForOpponentToConnect;

		this.window = new JFrame();
		this.window.setTitle("Playing against " + this.opponentUser);
		this.window.setVisible(true);
		this.window.setSize(300, 300);

		title = "Playing against " + this.opponentUser;

		this.window.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				Game.this.gameState = GameState.GameOver;
				Game.this.GameOver(false, "You have lost");
			}
		});

		GUI.MakeWindowForMessage(this.window, "Waiting for opponent to connect");
	}

	@Override
	public void run() {
		while (!Thread.interrupted()) {
			if (this.gameOnServer.getExited(this.firstPlayer)) {
				return;
			}
			
			if (this.gameOnServer.getExited(!this.firstPlayer)) {
				this.GameOver(true, "Opponent left the game, you won!");
				return;
			}

			switch (this.gameState) {

			// WaitingForOpponentToConnect => SettingUpShips
			case WaitingForOpponentToConnect:
				if (this.gameOnServer.getConnected(!this.firstPlayer)) {
					this.gameState = GameState.SettingUpShips;
					window.dispose();
					window = GUI.MakeWindowForSettingUpShips("Playing against "
							+ this.opponentUser, gameOnServer, firstPlayer);
				}
				break;

			// SettingUpShips => WaitingForOpponentToSetUpShips
			case SettingUpShips:
				if (gameOnServer.settingUpMyShipsFinished(firstPlayer)) {
					gameState = GameState.WaitingForOpponentToSetUpShips;
				}
				break;

			// WaitingForOpponentToSetUpShips => ChoosingAttackCell OR
			// WaitingForOpponentToAttack
			case WaitingForOpponentToSetUpShips:
				if (gameOnServer.settingUpOpponentShipsFinished(firstPlayer)) {
					List<AbstractFigure> myShips=((PlaceFigures) window).getMyFigures();
					window.dispose();
					window = GUI.MakeWindowForGame(title, myShips, gameOnServer,
							firstPlayer);

					if (firstPlayer)
						gameState = GameState.ChoosingAttackCell;
					else
						gameState = GameState.WaitingForOpponentToAttack;
				}
				break;

			// ChoosingAttackCell => Attack (listeners)
			case ChoosingAttackCell:
				if (gameOnServer.getLastMove() != null) {
					Point lastMove = gameOnServer.getLastMove();
					Color col = gameOnServer.getLastMoveColor();
					((GameFrame) window).colorField((int) lastMove.getX(),
							(int) lastMove.getY(), col);
				}

				gameState = GameState.Attack;
				break;

			// Attack => WaitingForOpponentToAttack OR GameOver
			case Attack:

				if ((firstPlayer && (!gameOnServer.firstPlayerPlaying()))
						|| (!firstPlayer && (gameOnServer.firstPlayerPlaying())))
					if (gameOnServer.gameOver())
						gameState = GameState.GameOver;
					else {
						gameState = GameState.WaitingForOpponentToAttack;
						((GameFrame) window).changeState();
					}
				break;
			// WaitingForOpponentToAttack => ChoosingAttackCell OR GameOver
			case WaitingForOpponentToAttack:
				if ((firstPlayer && (gameOnServer.firstPlayerPlaying()))
						|| (!firstPlayer && (!gameOnServer.firstPlayerPlaying())))
					if (gameOnServer.gameOver())
						gameState = GameState.GameOver;
					else {
						gameState = GameState.ChoosingAttackCell;
						((GameFrame) window).changeState();
					}
				break;

			case GameOver:
				boolean won = this.gameOnServer.playerWon(this.firstPlayer);
				String message = null;
				if (!won) {
					message = "You lost!";
				} else {
					message = "You won!";
				}

				this.GameOver(won, message);
				return;
			}

			try {
				Thread.sleep(Game.SleepTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void GameOver(boolean won, String message) {
		end = GUI.MakeWindowForMessage("Game over!", message);
		this.gameOnServer.setExited(this.firstPlayer);
	}

}
