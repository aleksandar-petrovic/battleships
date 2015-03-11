//Aleksandar Petrovic

package figures;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;

//brod duzine 4
public class FFour extends AbstractFigure {
	
	final static int Dimension=4;
	Map<Point, Integer> list;
	private final Figure figure=Figure.FFour;

	public Figure getFigure() {
		return figure;
	}

	FFour(int x, int y) {
		super(x, y);
		list = new HashMap<Point, Integer>();
		list.put(new Point(x, y), 1);
		list.put(new Point(x, y+1), 2);
		list.put(new Point(x, y+2), 3);
		list.put(new Point(x, y+3), 4);
	}

	FFour(Point pos) {
		super(pos);
		list = new HashMap<Point, Integer>();
		list.put(pos, 1);
		list.put(new Point((int) pos.getX(), (int) pos.getY()+1), 2);
		list.put(new Point((int) pos.getX(), (int) pos.getY()+2), 3);
		list.put(new Point((int) pos.getX(), (int) pos.getY()+3), 4);
	}
	
	public List<Point> getBoatPositions(){
		return new ArrayList<Point>(list.keySet());
	}

	@Override
	public
	void removeBoatPosition(int x, int y) {
		list.remove(new Point(x,y));
	}

	@Override
	public
	ImageIcon getImageAt(int x, int y) {
		Integer br=list.get(new Point(x, y));
		if(br!=null)
			return new ImageIcon(getClass().getResource("res/four-" + br + ".png"));
		return null;
	}
	
	public static ImageIcon getImage(int x){
		x++;
		if(x<0 || x>FFour.Dimension)
			return null;
		return new ImageIcon(FTwo.class.getResource("res/four-" + x + ".png"));
	}
	
	public static ImageIcon getImage(){
		return new ImageIcon(FTwo.class.getResource("res/four.jpg"));
	}
	
	
	@Override
	public boolean equals(Object o) {
		if(this == o)
			return true;
		if(o == null)
			return false;
		if(getClass() != o.getClass())
			return false;
		
		FFour fig = (FFour) o;
		if(fig.getBoatPositions().equals(getBoatPositions()))
			return true;
		else
			return false;
	}

	@Override
	boolean isBoatSunk() {
		return list.size()==0;
	}
}
