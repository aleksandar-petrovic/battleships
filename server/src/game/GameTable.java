/*
 * @author Demjan Grubic 
 */

package game;

import java.util.List;

public class GameTable {
	private static final int NumberOfRows = 10;
	private static final int NumberOfColumns = 10;
	
	private int[][] table;
	private int numberOfShipParts;
	
	public GameTable( List<Integer> rows, List<Integer> columns ) {
		this.table = new int[NumberOfRows][];
		for (int i = 0; i < NumberOfRows; ++i) {
			this.table[i] = new int[NumberOfColumns];
			
			for (int j = 0; j < NumberOfColumns; ++j) {
				this.table[i][j] = 0;
			}
		}
		
		this.numberOfShipParts = 0;
		for (int i = 0; i < rows.size(); ++i) {
			int row = rows.get(i);
			int column = columns.get(i);
			
			this.table[row][column] = 1;
			this.numberOfShipParts++;
		}
	}
	
	public boolean gameOver() {
		return this.numberOfShipParts == 0;
	}
	
	public ProjectileAttackInformation attack( int row, int column ) {
		boolean missed = this.table[row][column] == 0;
		if ( missed ) {
			return ProjectileAttackInformation.MISSED;
		}
		
		this.table[row][column] = 2;
		this.numberOfShipParts--;
		
		if ( this.shipDestroyed( row, column ) ) {
			return ProjectileAttackInformation.DESTROYED;
		}
		
		return ProjectileAttackInformation.HIT;
	}

	private boolean shipDestroyed( int row, int column ) {
		int left = column - 1;
		while ( left >= 0 && this.table[row][left] == 2 ) {
			left--;
		}
		
		if ( left >= 0 && this.table[row][left] == 1 ) {
			return false;
		}
		
		int right = column + 1;
		while ( right < GameTable.NumberOfColumns && this.table[row][right] == 1 ) {
			right++;
		}
		
		if ( right < GameTable.NumberOfColumns && this.table[row][right] == 1 ) {
			return false;
		}
		
		return true;
	}
}