//@author Demjan Grubic

package beans;

public interface GameManagementI {
	// returns Pointer to registered game which is stateful bean
	public GameI registerGame(String firstPlayerId, String secondPlayerId);
}