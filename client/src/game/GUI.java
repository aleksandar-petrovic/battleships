//@author Marko Kabic

package game;

import java.awt.Container;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import beans.GameI;
import figures.AbstractFigure;
import figures.PlaceFigures;

public class GUI {
	
	public static JFrame MakeWindowForSettingUpShips(String title, GameI gameOnServer, boolean firstPlayer) {
		JFrame window = new PlaceFigures(gameOnServer, firstPlayer);
		
		final GameI gameOnServer1 = gameOnServer;
		final boolean firstPlayer1 = firstPlayer;
		
		window.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				gameOnServer1.setExited(firstPlayer1);
			}
		});
		window.setResizable(false);
		window.setSize(600, 520);
		window.setTitle(title);
		window.validate();
		window.repaint();
		window.setVisible(true);
		
		return window;
		
	}
	
	public static JFrame MakeWindowForGame(String title, List<AbstractFigure> myShips, GameI gameOnServer, boolean firstPlayer) {
		JFrame window = new GameFrame(title, myShips, gameOnServer, firstPlayer);
		
		final GameI gameOnServer1 = gameOnServer;
		final boolean firstPlayer1 = firstPlayer;
		window.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				gameOnServer1.setExited(firstPlayer1);
			}
		});
		return window;
	}
	
	public static JFrame MakeWindowForMessage(String title, String message, GameI gameOnServer, boolean firstPlayer ) {
		JFrame window = new JFrame();
		Container contentPane = window.getContentPane();
		
		JLabel label = new JLabel(message);
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setVerticalAlignment(SwingConstants.CENTER);
		
		contentPane.add(label);
		
		final GameI gameOnServer1 = gameOnServer;
		final boolean firstPlayer1 = firstPlayer;
		
		window.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				gameOnServer1.setExited(firstPlayer1);
			}
		});
		
		window.setTitle(title);
		window.setSize(300, 300);
		window.setVisible(true);
		
		window.revalidate();
		window.repaint();
		
		return window;
	}
	
	public static JFrame MakeWindowForMessage(String title, String message) {
		JFrame window = new JFrame();
		Container contentPane = window.getContentPane();
		
		JLabel label = new JLabel(message);
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setVerticalAlignment(SwingConstants.CENTER);
		
		contentPane.add(label);
		window.setSize(300, 300);
		window.setTitle(title);
		
		window.revalidate();
		window.repaint();
		window.setVisible(true);
		return window;
	}
	
	
	//@author Demjan Grubic
	public static void MakeWindowForMessage(JFrame window, String message) {
		Container contentPane = window.getContentPane();
		contentPane.removeAll();
		
		JLabel label = new JLabel(message);
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setVerticalAlignment(SwingConstants.CENTER);
		
		contentPane.add(label);
		window.setSize(300, 300);
		
		window.revalidate();
		window.repaint();
		window.setVisible(true);
	}
	
	
}
