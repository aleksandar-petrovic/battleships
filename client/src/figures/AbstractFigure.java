//Aleksandar Petrovic

package figures;

import java.awt.Point;
import java.util.List;

import javax.swing.ImageIcon;

public abstract class AbstractFigure {
	
	Point position;
	
	AbstractFigure(int x, int y){
		position=new Point(x,y);
	}
	
	AbstractFigure(Point position){
		this.position=position;
	}
	
	abstract Figure getFigure();
	
	public abstract List<Point> getBoatPositions();
	
	public abstract void removeBoatPosition(int x, int y);
	
	public abstract ImageIcon getImageAt(int x, int y);
	
	abstract public boolean equals(Object obj);
	
	abstract boolean isBoatSunk();
}
