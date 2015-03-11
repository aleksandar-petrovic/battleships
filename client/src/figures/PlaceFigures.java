//Aleksandar Petrovic

package figures;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import beans.GameI;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.awt.Dimension;

public class PlaceFigures extends JFrame {
	private static final long serialVersionUID = 1L;
	public final static int Dimension = 10;
	public final static int DimensionX = 4;
	public final static int DimensionY = 6;

	// glasspane prozora
	JPanel glass;
	AbstractLabel movingBoat;
	Point point;

	private JPanel panelMain;
	private JPanel panelEastBoats;
	JButton btnNext;
	// JLabel label;

	List<AbstractFigure> myFigures;

	private JLabel[][] matrix;

	private boolean[][] freeFields;
	private List<AbstractLabel> freeBoats;
	private AbstractLabel previousFigure;
	final private boolean firstPlayer;
	final private GameI gameOnServer;
	private List<Integer> rows;
	private List<Integer> columns;

	GameState state;

	private void whenSetFigures() {
		glass.removeAll();
		JPanel panel = new JPanel();
		JLabel label = new JLabel(
				"Waiting for opponent to set up ships! Please wait...");
		label.setFont(new Font(null, Font.BOLD, 16));
		label.setAlignmentX(CENTER_ALIGNMENT);
		label.setAlignmentY(CENTER_ALIGNMENT);
		panel.add(label);
		panel.setBackground(Color.WHITE);
		panel.setSize(400, 30);
		panel.setLocation(100, 200);
		panel.setVisible(true);
		glass.add(panel);
		revalidate();
		repaint();

		glass.removeMouseMotionListener(glass.getMouseMotionListeners()[0]);
		glass.removeMouseListener(glass.getMouseListeners()[0]);
	}

	public PlaceFigures(GameI gameOnServer, boolean first) {
		this.gameOnServer = gameOnServer;
		firstPlayer = first;
		rows = new ArrayList<Integer>(16);
		columns = new ArrayList<Integer>(16);
		glass = (JPanel) this.getGlassPane();
		glass.setLayout(null);
		glass.setVisible(true);
		btnNext = new JButton("Next");
		btnNext.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				for (AbstractFigure fig : myFigures) {
					for (Point p : fig.getBoatPositions()) {
						rows.add(p.x);
						columns.add(p.y);
					}
				}
				whenSetFigures();
				PlaceFigures.this.gameOnServer.SetUpShips(rows, columns,
						firstPlayer);
				// System.out.println(firstPlayer);
				// System.out.println(PlaceFigures.this.gameOnServer.settingUpMyShipsFinished(firstPlayer));
			}

		});

		setFigures();

		state = GameState.ChosingFigure;

		myFigures = new ArrayList<>();

		panelEastBoats = new JPanel();
		panelEastBoats.setLayout(new BoxLayout(panelEastBoats,
				BoxLayout.PAGE_AXIS));
		for (JLabel label : freeBoats) {
			panelEastBoats.add(label);
		}

		glass.addMouseMotionListener(new MouseAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				if (state == GameState.ChosingPlace) {
					mouseMoved(e);
				}
			}

			@Override
			public void mouseMoved(MouseEvent me) {
				if (state == GameState.ChosingPlace) {
					point = me.getPoint();
					movingBoat.setLocation(point.x - 20, point.y - 25);
				}
			}
		});

		glass.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				glass.setVisible(false);
				glass.removeAll();
				Component cp = getContentPane().findComponentAt(e.getPoint());
				// levi klik za dodavanje broda
				if (e.getButton() == MouseEvent.BUTTON1) {
					switch (state) {
					case ChosingFigure:
						// ako je kliknuta komponenta neki od slobodnih brodova
						// desno
						// onda omoguci njegovo pomeranje
						if (cp instanceof AbstractLabel) {
							AbstractLabel clickedBoat = (AbstractLabel) cp;
							panelEastBoats.remove(clickedBoat);
							freeBoats.remove(clickedBoat);
							clickedBoat.setSize(170, 50);
							clickedBoat.setLocation(e.getPoint().x - 20,
									e.getPoint().y - 25);
							movingBoat = clickedBoat;
							glass.add(movingBoat);
							glass.setVisible(true);
							previousFigure = clickedBoat;
							state = GameState.ChosingPlace;
						}
						// ako je komponenta vec postavljeni brod
						// onda omoguci njegovo premestanje
						else if (cp instanceof BlueLabel) {
							BlueLabel clicked = (BlueLabel) cp;
							if (!isFree(clicked.getPosition())) {
								AbstractFigure boat = getBoatAt(clicked
										.getPosition());
								myFigures.remove(boat);
								AbstractLabel freeboat = new AbstractLabel(boat
										.getFigure());
								previousFigure = freeboat;
								freeboat.setSize(170, 50);
								freeboat.setLocation(e.getPoint().x - 20,
										e.getPoint().y - 25);
								movingBoat = freeboat;
								glass.add(movingBoat);
								glass.setVisible(true);
								state = GameState.ChosingPlace;
							}
						}
						break;
					case ChosingPlace:
						// biranje pozicije za smestanje broda
						if (cp instanceof BlueLabel) {
							BlueLabel label = (BlueLabel) cp;
							if (!addBoat(label.getPosition(),
									previousFigure.getFigure())) {
								AbstractLabel freeboat = new AbstractLabel(
										previousFigure.getFigure());
								panelEastBoats.add(freeboat);
								freeBoats.add(freeboat);
							}
						} else {
							freeBoats.add(previousFigure);
							panelEastBoats.add(previousFigure);
						}
						state = GameState.ChosingFigure;
						break;
					default:
						break;
					}
				}
				// desni klik za sklanjanje broda
				else if (e.getButton() == MouseEvent.BUTTON3) {
					// ako je kliknuta komponenta vec postavljeni brod
					// onda se on premesta u slobodne brodove i brise iz tabele
					if (cp instanceof BlueLabel) {
						BlueLabel clicked = (BlueLabel) cp;
						if (!isFree(clicked.getPosition())) {
							AbstractFigure boat = getBoatAt(clicked
									.getPosition());
							AbstractLabel freeboat = new AbstractLabel(boat
									.getFigure());
							freeBoats.add(freeboat);
							panelEastBoats.add(freeboat);
							myFigures.remove(boat);
						}
					}
					// desni klik u toku trazenja nove pozicije brodu
					// vraca brod ponovo u listu slobodnih brodova u desnom
					// panelu
					if (state == GameState.ChosingPlace) {
						freeBoats.add(previousFigure);
						panelEastBoats.add(previousFigure);
					}
					state = GameState.ChosingFigure;
				}
				draw();
				glass.setVisible(true);
			}
		});

		getContentPane().add(panelEastBoats, BorderLayout.EAST);

		panelMain = new JPanel(new GridLayout(Dimension, Dimension));
		panelMain.setAutoscrolls(false);
		matrix = new JLabel[Dimension][];
		for (int i = 0; i < Dimension; i++) {
			matrix[i] = new JLabel[Dimension];
			for (int j = 0; j < Dimension; j++) {
				matrix[i][j] = new BlueLabel(i, j);
				panelMain.add(matrix[i][j]);
			}
		}
		getContentPane().add(panelMain, BorderLayout.CENTER);
		panelEastBoats.setSize(new Dimension(175, 285));
		panelEastBoats.setPreferredSize(new Dimension(175, 285));
		panelEastBoats.setMaximumSize(new Dimension(175, 285));
		panelEastBoats.setMinimumSize(new Dimension(175, 285));
	}

	AbstractFigure getBoatAt(Point pos) {
		for (AbstractFigure figure : myFigures) {
			if (figure.getBoatPositions().contains(pos)) {
				for (Point p : figure.getBoatPositions()) {
					freeFields[p.x][p.y] = false;
					matrix[p.x][p.y].setIcon(null);
					matrix[p.x][p.y].setBorder(BorderFactory.createLineBorder(
							Color.white, 1));
				}
				return figure;
			}
		}
		return null;
	}

	boolean addBoat(Point p, Figure fig) {
		AbstractFigure boat = null;
		boolean ok = true;

		int row = p.x;
		int column = p.y;
		
		if (!isFree(row, column) || column + fig.getDimension() - 1  >= PlaceFigures.Dimension) {
			ok = false;
		}
		
		for (int checkColumn = column - 1; checkColumn < column + fig.getDimension() + 1 && ok; ++checkColumn) {
			if (checkColumn < 0 || checkColumn >= Dimension) {
				continue;
			}

			if (!isFree(row, checkColumn)) {
				ok = false;
			}

			int upRow = row - 1;
			if (upRow >= 0 && !isFree(upRow, checkColumn)) {
				ok = false;
			}

			int downRow = row + 1;
			if (downRow < Dimension && !isFree(downRow, checkColumn)) {
				ok = false;
			}
		}

		if (!ok) {
			JOptionPane.showMessageDialog(this, "Invalid location");
		} else {
			boat = fig.getFigureObject(p.x, p.y);
			myFigures.add(boat);
			for (Point pos : boat.getBoatPositions())
				freeFields[pos.x][pos.y] = true;
		}

		return ok;
	}

	boolean isFree(Point p) {
		return p.x >= 0 && p.x < Dimension && p.y >= 0 && p.y < Dimension
				&& !freeFields[p.x][p.y];
	}

	boolean isFree(int x, int y) {
		return x >= 0 && x < Dimension && y >= 0 && y < Dimension
				&& !freeFields[x][y];
	}

	void draw() {
		for (AbstractFigure figure : myFigures) {
			List<Point> positions = figure.getBoatPositions();
			for (Point p : positions) {
				matrix[p.x][p.y].setIcon(figure.getImageAt(p.x, p.y));
				matrix[p.x][p.y].setBorder(null);
			}
		}

		if (panelEastBoats.getComponentCount() == 0
				&& state == GameState.ChosingFigure) {
			btnNext.setSize(80, 30);
			btnNext.setLocation(getContentPane().getSize().width - 130,
					getContentPane().getSize().height - 50);
			glass.add(btnNext);
		}

		revalidate();
		repaint();
	}

	public List<AbstractFigure> getMyFigures() {
		return myFigures;
	}

	void setFigures() {
		freeFields = new boolean[Dimension][Dimension];
		freeBoats = new ArrayList<>();
		freeBoats.add(new AbstractLabel(Figure.FFour));
		freeBoats.add(new AbstractLabel(Figure.FThree));
		freeBoats.add(new AbstractLabel(Figure.FThree));
		freeBoats.add(new AbstractLabel(Figure.FTwo));
		freeBoats.add(new AbstractLabel(Figure.FTwo));
		freeBoats.add(new AbstractLabel(Figure.FTwo));
	}

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
		}
		/*
		 * JFrame frame = new PlaceFigures(null, false);
		 * frame.setTitle("Arrange ships"); frame.setSize(600, 520);
		 * frame.setResizable(false); frame.setVisible(true);
		 * frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		 */
	}

}
