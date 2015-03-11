//Aleksandar Petrovic

package figures;

import java.awt.Color;
import java.awt.Point;

import javax.swing.BorderFactory;
import javax.swing.JLabel;

public class BlueLabel extends JLabel {
	
	private static final long serialVersionUID = 1L;
	private Color color = new Color(201, 230, 255);
	private Point position;
	
	public BlueLabel(int x, int y) {
		position=new Point(x,y);
		setOpaque(true);
		setBackground(color);
		setBorder(BorderFactory.createLineBorder(Color.white, 1));
	}

	public Point getPosition() {
		return position;
	}

	public void setPosition(Point position) {
		this.position = position;
	}

}
