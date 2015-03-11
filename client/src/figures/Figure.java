//Aleksandar Petrovic

package figures;

public enum Figure {
	FTwo {
		int getDimension() { return 2; }
		AbstractFigure getFigureObject( int row, int column ) { return new FTwo( row, column ); } 
	},
	FThree {
		int getDimension() { return 3; }
		AbstractFigure getFigureObject( int row, int column ) { return new FThree( row, column ); } 
	},
	FFour {
		int getDimension() { return 4; }
		AbstractFigure getFigureObject( int row, int column ) { return new FFour( row, column ); } 
	},
	FBomb {
		int getDimension() { return 1; }
		AbstractFigure getFigureObject( int row, int column ) { return null; } 
	};
	
	abstract int getDimension();
	abstract AbstractFigure getFigureObject( int row, int column ); 
}
