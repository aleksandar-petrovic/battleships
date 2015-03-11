//Aleksandar Petrovic

package figures;

import javax.swing.JLabel;

public class AbstractLabel extends JLabel {

	private static final long serialVersionUID = 1L;
	private static int NUM=0;
	private String figure;
	
	AbstractLabel(Figure fig){
		this.figure=fig.toString();
		if(Figure.FFour.toString()==figure)
			super.setIcon(FFour.getImage());
		else if(Figure.FThree.toString()==figure)
			super.setIcon(FThree.getImage());
		else if(Figure.FTwo.toString()==figure)
			super.setIcon(FTwo.getImage());
		figure+=NUM++;
	}
	
	public Figure getFigure() {
		if(figure.contains(Figure.FFour.toString()))
			return Figure.FFour;
		else if(figure.contains(Figure.FThree.toString()))
			return Figure.FThree;
		else
			return Figure.FTwo;
	}

	@Override
	public boolean equals(Object o) {
		if(this == o)
			return true;
		if(o == null)
			return false;
		if(getClass() != o.getClass())
			return false;
		
		AbstractLabel fig = (AbstractLabel) o;
		if(fig.figure.equals(figure))
			return true;
		else
			return false;
	}
}
