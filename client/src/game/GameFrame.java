//@author Marko Kabic

package game;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import figures.AbstractFigure;
import figures.BlueLabel;
import figures.PlaceFigures;
import beans.GameI;

public class GameFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	public final static int Dimension = PlaceFigures.Dimension;
	private GameI gameOnServer;
	private String text;
	private String state;
	private JLabel label;
	private JPanel myFields;
	private JPanel opponentFields;
	private JLabel[][] myMatrix;
	private JLabel[][] opponentMatrix;
	private int length = 800;
	private int height = 500;
	
	public GameFrame(String title, List<AbstractFigure> myShips, GameI gameOnServer, boolean firstPlayer) {
		this.gameOnServer = gameOnServer;
		this.setSize(length, height);
		this.setResizable(false);
		
		this.setLayout(new BorderLayout(10, 10));
		
		myFields = new JPanel(new GridLayout(PlaceFigures.Dimension, PlaceFigures.Dimension));
		//myFields.setAutoscrolls(false);
		myMatrix = new JLabel[PlaceFigures.Dimension][];
		for (int i = 0; i < PlaceFigures.Dimension; i++) {
			myMatrix[i] = new JLabel[PlaceFigures.Dimension];
			for (int j = 0; j < PlaceFigures.Dimension; j++) {
				myMatrix[i][j] = new BlueLabel(i, j);
				myFields.add(myMatrix[i][j]);
			}
		}
		
		drawMyFigures(myShips);
		
		opponentFields = new JPanel(new GridLayout(PlaceFigures.Dimension, PlaceFigures.Dimension));
		opponentMatrix = new JLabel[PlaceFigures.Dimension][];
		for (int i = 0; i < PlaceFigures.Dimension; i++) {
			opponentMatrix[i] = new JLabel[PlaceFigures.Dimension];
			for (int j = 0; j < PlaceFigures.Dimension; j++) {
				opponentMatrix[i][j] = new BlueLabel(i, j);
				addListener(opponentMatrix[i][j]);
				opponentFields.add(opponentMatrix[i][j]);
			}
		}
		
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, myFields, opponentFields);
		splitPane.setDividerLocation(length/2);
		add(splitPane, BorderLayout.CENTER);
		
		if(firstPlayer) {
			text = "Choose fields in right screen to attack!";
			label = new JLabel(text, JLabel.CENTER);
			state = "attack";
		}
		else {
			text = "Wait for opponent to attack!";
			label = new JLabel(text, JLabel.CENTER);
			state = "wait";
		}
		
		add(label, BorderLayout.NORTH);
		
		final GameI gameOnServer1 = gameOnServer;
		final boolean firstPlayer1 = firstPlayer;
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				gameOnServer1.setExited(firstPlayer1);
			}
		});
		
		this.setTitle(title);
		this.setVisible(true);
		this.validate();
		this.repaint();
	}
	
	private void addListener(JLabel lab) {
		BlueLabel label = (BlueLabel)lab;
		final BlueLabel label1 = label;
		
		label.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				int x = (int) label1.getPosition().getX();
				int y = (int) label1.getPosition().getY();
				if(state.equals("attack") && (label1.getBackground() != Color.yellow)&& (label1.getBackground() != Color.red) ){
					attack(x, y);
				}
				
			}
			
		});
	}
	
	//Aleksandar Petrovic (ovu metodu)
	private void drawMyFigures(List<AbstractFigure> myShips){
		for(AbstractFigure fig: myShips){
			for(Point p: fig.getBoatPositions()){
				myMatrix[p.x][p.y].setIcon(fig.getImageAt(p.x, p.y));
			}
		}
		revalidate();
		repaint();
	}
	
	public void attack(int x, int y) {
		JLabel label = opponentMatrix[x][y];
		ProjectileAttackInformation result = gameOnServer.makeMove(x, y);
		
		if((result == ProjectileAttackInformation.HIT) || (result == ProjectileAttackInformation.DESTROYED)) {
			label.setBackground(Color.RED);
		}
		else {
			label.setBackground(Color.yellow);
		}
	
		gameOnServer.setLastMoveColor(label.getBackground());
	}
	
	public void colorField(int x, int y, Color col) {
		myMatrix[x][y].setIcon(null);
		myMatrix[x][y].setBackground(col);
		this.validate();
		this.repaint();
	}
	
	public void changeState() {
		if(text.equals("Choose fields in right screen to attack!")) {
			text = "Wait for opponent to attack!";
			label.setText(text);
			state = "wait";
		}
		else {
			text = "Choose fields in right screen to attack!";
			label.setText(text);
			state = "attack";
		}
		validate();
		repaint();
	}
}
