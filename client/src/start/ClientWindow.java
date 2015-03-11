//Aleksandar Petrovic

package start;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Set;
import javax.ejb.EJBException;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import game.Game;
import beans.AdminI;
import beans.GameI;
import beans.GameManagementI;
import beans.UserI;
import beans.UserManagementI;

public class ClientWindow extends JFrame {
	
	private static final long serialVersionUID = 1L;

	JFrame adminWindow;

	private JTextField txtName;
	private JTextField txtPass;
	private JTextField txtName1;
	private JTextField txtPass1;
	private JTextField txtPass2;
	TableModel model;
	String myName;
	UserI me;

	ClientWindow() {
		addWindowListener(windowClosing);
		login();
	}

	private void login() {
		getContentPane().removeAll();
		setSize(300, 300);

		txtName = new JTextField();
		setSize(txtName);
		setSize(txtName);
		txtPass = new JTextField();
		setSize(txtPass);
		setSize(txtPass);
		JLabel lblName = new JLabel("Name: ");
		setSize(lblName);
		setSize(lblName);
		JLabel lblPass = new JLabel("Password: ");
		setSize(lblPass);
		setSize(lblPass);
		JButton btnRegister = new JButton("Registration");
		JButton btnLogin = new JButton("Log In");

		btnLogin.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (txtName.getText().length() > 0
						&& txtPass.getText().length() > 0) {
					if (logInUser(txtName.getText().toLowerCase(),
							txtPass.getText())) {
						start();
					}
				} else
					JOptionPane.showMessageDialog(ClientWindow.this,
							"Field name or password is empty");
			}

		});

		btnRegister.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				signUp();
			}

		});

		JPanel panelUp = new JPanel();
		BoxLayout boxUp = new BoxLayout(panelUp, BoxLayout.PAGE_AXIS);
		panelUp.setLayout(boxUp);
		btnRegister.setAlignmentX(RIGHT_ALIGNMENT);
		panelUp.add(Box.createVerticalStrut(10));
		panelUp.add(btnRegister);
		getContentPane().add(panelUp, BorderLayout.NORTH);

		JPanel panel = new JPanel();
		BoxLayout box = new BoxLayout(panel, BoxLayout.PAGE_AXIS);
		panel.setLayout(box);

		panel.add(Box.createVerticalGlue());

		JPanel row1 = new JPanel();
		BoxLayout box1 = new BoxLayout(row1, BoxLayout.LINE_AXIS);
		row1.setLayout(box1);
		row1.add(lblName);
		row1.add(txtName);
		panel.add(row1);

		panel.add(Box.createVerticalStrut(5));

		JPanel row2 = new JPanel();
		BoxLayout box2 = new BoxLayout(row2, BoxLayout.LINE_AXIS);
		row2.setLayout(box2);
		row2.add(lblPass);
		row2.add(txtPass);
		panel.add(row2);

		panel.add(Box.createVerticalStrut(10));

		panel.add(btnLogin);
		panel.add(Box.createVerticalGlue());

		getContentPane().add(panel);

		txtName.requestFocus();

		repaint();
		revalidate();
	}

	private boolean logInUser(final String username, final String pass) {
		try {
			InitialContext ctx = new InitialContext();
			String name = "ejb:/Server//UserEJB!" + UserI.class.getName()
					+ "?stateful";
			me = (UserI) ctx.lookup(name);
			if (me.login(username, pass))
				myName = txtName.getText();
			else {
				me = null;
				JOptionPane.showMessageDialog(ClientWindow.this,
						"Account is already logged on server");
				return false;
			}
		} catch (EJBException e) {
			JOptionPane.showMessageDialog(ClientWindow.this, e.getCause()
					.getMessage());
			me = null;
			return false;
		} catch (NamingException e) {
			JOptionPane.showMessageDialog(ClientWindow.this, "Login failed");
			me = null;
			return false;
		}
		return true;
	}

	private void signUp() {
		getContentPane().removeAll();

		txtName1 = new JTextField();
		setSize(txtName1);
		txtPass1 = new JTextField();
		setSize(txtPass1);
		txtPass2 = new JTextField();
		setSize(txtPass2);
		JLabel lblName = new JLabel("Name: ");
		setSize(lblName);
		JLabel lblPass1 = new JLabel("Password: ");
		setSize(lblPass1);
		JLabel lblPass2 = new JLabel("Re-enter password: ");
		setSize(lblPass2);
		JButton btnRegister = new JButton("Register");
		JButton btnBack = new JButton("Back");

		btnRegister.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (txtName1.getText().length() > 0
						&& txtPass1.getText().length() > 0
						&& txtPass2.getText().length() > 0) {
					if (txtPass1.getText().equals(txtPass2.getText())) {
						new Thread() {
							@Override
							public void run() {
								try {
									InitialContext ctx = new InitialContext();
									String name = "ejb:/Server//UserManagementEJB!"
											+ UserManagementI.class.getName();
									UserManagementI um = (UserManagementI) ctx
											.lookup(name);
									um.register(txtName1.getText()
											.toLowerCase(), txtPass1.getText());
									JOptionPane.showMessageDialog(
											ClientWindow.this,
											"Registration successful");
								} catch (NamingException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								} catch (EJBException e) {
									JOptionPane.showMessageDialog(
											ClientWindow.this,
											"Username already registered");
								}
							}
						}.start();
					} else {
						JOptionPane.showMessageDialog(ClientWindow.this,
								"Password mismatch");
					}
				} else
					JOptionPane.showMessageDialog(ClientWindow.this,
							"Field name or password is empty");
			}
		});

		btnBack.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				login();
			}
		});

		JPanel panelUp = new JPanel(new FlowLayout(FlowLayout.LEADING));
		btnBack.setAlignmentX(LEFT_ALIGNMENT);
		panelUp.add(btnBack);
		getContentPane().add(panelUp, BorderLayout.NORTH);

		JPanel panel = new JPanel();
		BoxLayout box = new BoxLayout(panel, BoxLayout.PAGE_AXIS);
		panel.setLayout(box);

		panel.add(Box.createVerticalGlue());

		JPanel row1 = new JPanel();
		BoxLayout box1 = new BoxLayout(row1, BoxLayout.LINE_AXIS);
		row1.setLayout(box1);
		row1.add(lblName);
		row1.add(txtName1);
		panel.add(row1);

		panel.add(Box.createVerticalStrut(5));

		JPanel row2 = new JPanel();
		BoxLayout box2 = new BoxLayout(row2, BoxLayout.LINE_AXIS);
		row2.setLayout(box2);
		row2.add(lblPass1);
		row2.add(txtPass1);
		panel.add(row2);

		panel.add(Box.createVerticalStrut(5));

		JPanel row3 = new JPanel(new FlowLayout());
		BoxLayout box3 = new BoxLayout(row3, BoxLayout.LINE_AXIS);
		row3.setLayout(box3);
		row3.add(lblPass2);
		row3.add(txtPass2);
		panel.add(row3);

		panel.add(Box.createVerticalStrut(10));

		panel.add(btnRegister);

		panel.add(Box.createVerticalGlue());

		getContentPane().add(panel);

		txtName1.requestFocus();

		repaint();
		revalidate();
	}

	public void getOnlineUsers() {
		new Thread() {
			@Override
			public void run() {
				try {
					if (me != null) {
						Set<String> users = me.getOnlineUsers();
						model.setUsers(users);
					}
				} catch (EJBException e) {
					JOptionPane.showMessageDialog(ClientWindow.this, e
							.getCause().getMessage());
				}
			};
		}.start();
	}

	private void start() {
		getContentPane().removeAll();
		setSize(250, 600);

		model = new TableModel();

		getOnlineUsers();

		final JTable table = new JTable(model);
		table.setRowHeight(40);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {

					@Override
					public void valueChanged(ListSelectionEvent event) {
						if (table.getSelectedRowCount() == 0)
							return;
						if (event.getValueIsAdjusting())
							return;

						String userId = model.getSelectedUser(table
								.getSelectedRow());

						// ako slucajno klikne na nekog protivnika treba potvrda
						// da zeli tu igru
						if (JOptionPane.YES_OPTION == JOptionPane
								.showConfirmDialog(ClientWindow.this,
										"Challenge " + userId, "Play?",
										JOptionPane.YES_NO_OPTION)) {
							if (ClientWindow.this.me.sendGameRequest(userId)) {
								JOptionPane.showMessageDialog(
										ClientWindow.this, "Game request sent");
							} else {
								JOptionPane.showMessageDialog(
										ClientWindow.this,
										"Game request hasn't been sent");
							}
						}
						table.clearSelection();
					}

				});

		JScrollPane scrol = new JScrollPane(table);
		JButton btnLogOut = new JButton("Log Out");
		JLabel lblName = new JLabel(myName);
		JButton btnAdmin = new JButton("Settings");
		this.setSize(btnAdmin);

		btnAdmin.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				adminWindow();
			}
		});

		setSize(btnLogOut);

		JTableHeader header = table.getTableHeader();
		TableCellRenderer renderer = header.getDefaultRenderer();
		((JLabel) renderer).setHorizontalAlignment(SwingConstants.CENTER);

		btnLogOut.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (me != null) {
					me.logout();
					me = null;
				}
				login();
			}

		});

		JPanel panelUp = new JPanel();
		panelUp.setLayout(new BoxLayout(panelUp, BoxLayout.LINE_AXIS));
		panelUp.add(Box.createHorizontalGlue());
		if (myName.toLowerCase().equals("admin")) {
			panelUp.add(btnAdmin);
			panelUp.add(Box.createHorizontalStrut(5));
		}
		panelUp.add(btnLogOut);
		panelUp.add(Box.createHorizontalGlue());
		getContentPane().add(panelUp, BorderLayout.NORTH);

		JPanel panelMain = new JPanel();
		panelMain.setLayout(new BoxLayout(panelMain, BoxLayout.PAGE_AXIS));

		JPanel panelLabel = new JPanel();
		panelLabel.setLayout(new BoxLayout(panelLabel, BoxLayout.LINE_AXIS));
		panelLabel.add(Box.createHorizontalGlue());
		panelLabel.add(lblName);
		panelLabel.add(Box.createHorizontalGlue());

		JPanel panelLabel1 = new JPanel();
		panelLabel1.setLayout(new BoxLayout(panelLabel1, BoxLayout.LINE_AXIS));
		panelLabel1.add(Box.createHorizontalGlue());
		panelLabel1.add(new JLabel("Online users:"));
		panelLabel1.add(Box.createHorizontalGlue());

		panelMain.add(Box.createVerticalStrut(10));
		panelMain.add(panelLabel);
		panelMain.add(Box.createVerticalStrut(10));
		panelMain.add(panelLabel1);
		panelMain.add(Box.createVerticalStrut(10));
		panelMain.add(scrol);

		getContentPane().add(panelMain);

		repaint();
		revalidate();

		MakeThreadForGameRequests();
	}

	private void MakeThreadForGameRequests() {
		new Thread() {
			@Override
			public void run() {
				while (!Thread.interrupted() && me != null) {
					// update online users
					ClientWindow.this.getOnlineUsers();

					// check for game requests
					String userId = ClientWindow.this.me.getGameRequest();
					if (userId != null) {
						ClientWindow.this.RespondToGameRequest(userId);
					}

					// check for game request responses
					GameI gameOnServer = ClientWindow.this.me
							.getGameRequestResponse();
					if (gameOnServer != null) {
						ClientWindow.this.StartAGame(gameOnServer);
					}

					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
	}

	private void StartAGame(GameI gameOnServer) {
		new Thread(new Game(this.myName, gameOnServer.getPlayerId(true), true,
				gameOnServer)).start();
	}

	private void RespondToGameRequest(String userId) {
		if (JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(this,
				"Do you want to play against " + userId + "?", "Game request",
				JOptionPane.YES_NO_OPTION)) {

			try {
				InitialContext ctx = new InitialContext();
				String name = "ejb:/Server//GameManagementEJB!"
						+ GameManagementI.class.getName();
				GameManagementI gameManagement = (GameManagementI) ctx
						.lookup(name);
				GameI gameOnServer = gameManagement.registerGame(this.myName,
						userId);
				this.me.sendGameRequestResponse(userId, gameOnServer);
				new Thread(new Game(this.myName, userId, false, gameOnServer))
						.start();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void setSize(JComponent cp) {
		cp.setPreferredSize(new Dimension(100, 20));
		cp.setMaximumSize(new Dimension(100, 20));
		cp.setMinimumSize(new Dimension(100, 20));
	}

	WindowListener windowClosing = new WindowAdapter() {

		@Override
		public void windowClosing(WindowEvent e) {
			if (me != null) {
				me.logout();
				me = null;
			}
		}

	};

	private void adminWindow() {
		getContentPane().removeAll();
		setSize(300, 300);

		JButton btnBack = new JButton("Back");
		btnBack.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				start();
			}

		});

		final JTextField txtUser = new JTextField();
		txtUser.setHorizontalAlignment(JTextField.CENTER);
		setSize(txtUser);

		JButton btnDelete = new JButton("Delete user");
		btnDelete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (txtUser.getText().length() > 0) {
					try {
						InitialContext ctx=new InitialContext();
						String name = "ejb:/Server//AdminEJB!" + AdminI.class.getName();
						AdminI ai = (AdminI) ctx.lookup(name);
						String user = txtUser.getText().toLowerCase();
						boolean successful = ai.removeUser(me, user);
						if(!successful)
							JOptionPane.showMessageDialog(adminWindow, "Specified user cannot be deleted!","Error!", JOptionPane.WARNING_MESSAGE);
						else
							JOptionPane.showMessageDialog(adminWindow, "Specified user deleted!", "Done!", JOptionPane.INFORMATION_MESSAGE);
					} catch (NamingException e1) {
						e1.printStackTrace();
					} catch (Exception e2) {
						e2.printStackTrace();
					}
				}
			}
		});

		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));

		JPanel panelBtnBack = new JPanel();
		panelBtnBack
				.setLayout(new BoxLayout(panelBtnBack, BoxLayout.LINE_AXIS));
		panelBtnBack.add(btnBack);
		panelBtnBack.add(Box.createHorizontalGlue());
		panel.add(panelBtnBack);

		JPanel panelTxt = new JPanel();
		panelTxt.setLayout(new BoxLayout(panelTxt, BoxLayout.LINE_AXIS));
		panelTxt.add(txtUser);

		JPanel panelBtn = new JPanel();
		panelBtn.setLayout(new BoxLayout(panelBtn, BoxLayout.LINE_AXIS));
		panelBtn.add(btnDelete);

		panel.add(Box.createVerticalGlue());
		panel.add(panelTxt);
		panel.add(Box.createVerticalStrut(10));
		panel.add(panelBtn);
		panel.add(Box.createVerticalGlue());

		getContentPane().add(panel);
		revalidate();
		repaint();
	}

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
		}

		JFrame frame = new ClientWindow();
		frame.setSize(300, 300);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}
