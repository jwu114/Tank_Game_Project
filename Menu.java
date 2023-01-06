/**
 * File: Menu.java
 * 
 * @author  Jiarui Wu
 * @version 1.0
 * @since   2021-11-08
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Menu implements ActionListener {
	// the GUI of pausing game and finishing the game is just slightly different, so
	// their components are declared in the same class
	protected static final int PAUSE = 0;
	protected static final int END = 1;
	private GameMode gameMode;

	private JPanel panel; // hold title
	private JPanel outerPanel; // hold other panels

	private JPanel centerPanel;// hold buttons (the CENTER of BorderLayout)
	private JButton btnContinue;
	private JButton btnQuit;
	private JLabel title;

	public Menu(GameMode gameMode, int menuMode) {
		this.gameMode = gameMode;
		outerPanel = new JPanel();
		outerPanel.setLayout(new BorderLayout());
		panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.setBorder(new LineBorder(Color.WHITE, 3, false));
		panel.setBackground(Color.BLACK);

		btnContinue = new JButton(" ▶ ");
		btnQuit = new JButton(" ↩ ");

		btnQuit.setForeground(Color.WHITE);
		btnQuit.setBackground(Color.BLACK);
		btnQuit.setBorder(BorderFactory.createLineBorder(Color.WHITE));
		btnQuit.setFont(new Font("", Font.BOLD, 37));
		btnQuit.addActionListener(this);

		title = new JLabel("", JLabel.CENTER);
		title.setFont(new Font("", Font.BOLD, 40));
		title.setForeground(Color.WHITE);
		title.setPreferredSize(new Dimension(500, 170));

		// pause page and game result page have different components
		switch (menuMode) {
		case PAUSE:
			centerPanel = new JPanel();
			centerPanel.setBackground(Color.BLACK);

			title.setText("Pause"); // title is "Pause"
			panel.add(title, BorderLayout.NORTH);

			// pause page has one more button (for continuing the game)
			btnContinue.setForeground(Color.WHITE);
			btnContinue.setBackground(Color.BLACK);
			btnContinue.setBorder(BorderFactory.createLineBorder(Color.WHITE));
			btnContinue.setFont(new Font("", Font.BOLD, 37));
			btnContinue.addActionListener(this);

			centerPanel.add(btnQuit);
			centerPanel.add(btnContinue);
			panel.add(centerPanel, BorderLayout.CENTER);
			panel.add(centerPanel);
			break;
		case END:
			String text;
			Color textColor;
			Player winner = gameMode.getWinner();

			// the color and text of title will change with the game result (winner)
			if (winner == null) {
				text = "Tie";
				textColor = Color.blue;
			} else {
				if (winner.equals(gameMode.getPlayer1())) {
					text = "P1 Wins";
					textColor = Color.white;
				} else {
					text = "P2 Wins";
					textColor = Color.red;
				}
			}

			title.setForeground(textColor);
			title.setText(text + "\n" + gameMode.getPlayer1().getScore() + " : " + gameMode.getPlayer2().getScore());
			panel.add(title, BorderLayout.NORTH);
			panel.add(btnQuit, BorderLayout.CENTER);
			break;
		}

		outerPanel.add(panel);
		outerPanel.setBounds(250, 200, 500, 300);
		MainClass.frame.add(outerPanel);
		MainClass.frame.validate();
	}

	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource() == btnContinue) {
			// remove the pause menu
			MainClass.frame.remove(outerPanel);
			
			//refresh the frame
			MainClass.frame.repaint();
			MainClass.frame.revalidate();
			
			// continue the game
			MainClass.frame.requestFocusInWindow();
			gameMode.addKeyListener();
			gameMode.startTimers();
		} else if (ae.getSource() == btnQuit) {
			// remove the game result menu and other components and listeners
			MainClass.frame.remove(outerPanel);
			MainClass.frame.getContentPane().removeAll();
			gameMode.removeKeyListener();
			gameMode.getPlayer1().removeAllListeners();
			gameMode.getPlayer2().removeAllListeners();
			

			//refresh the frame
			MainClass.frame.repaint();
			
			new MainPage(); //go back to the title page
		}
	}

}
