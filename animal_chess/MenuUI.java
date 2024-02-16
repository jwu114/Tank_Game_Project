package animal_chess;
/**
 * File: MenuUI.java
 * 
 * @author  Jiarui Wu
 * @version 2.0
 * @since   2020-06-09
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MenuUI extends JFrame {
	private Container contents;

	private JLabel gameImage;

	// top buttons
	private JButton btnRule;
	private JButton btnQuit;

	// center buttons
	private JButton btnStart;


	public MenuUI() {
		super("Menu");
		contents = getContentPane();
		contents.setLayout(null);

		// Top Pane:
		btnRule = new JButton("");
		btnQuit = new JButton("");

		btnRule.setIcon(new ImageIcon("./Picture/RuleButton.png"));
		btnQuit.setIcon(new ImageIcon("./Picture/QuitButton.png"));

		contents.add(btnRule);
		contents.add(btnQuit);

		btnRule.setBounds(5, 5, 40, 40);
		btnQuit.setBounds(370, 5, 40, 40);

		// Center Pane:
		gameImage = new JLabel(new ImageIcon("./Picture/Game.png"));
		contents.add(gameImage);
		gameImage.setBounds(0, 150, 420, 160);

		btnStart = new JButton("");
		btnStart.setIcon(new ImageIcon("./Picture/StartButton.png"));
		contents.add(btnStart);
		btnStart.setBounds(120, 400, 180, 80);

		// ActionListener:
		ButtonHandler bh = new ButtonHandler();
		btnRule.addActionListener(bh);
		btnQuit.addActionListener(bh);
		btnStart.addActionListener(bh);

		// The container:
		Color menuColor = new Color(57, 19, 0); // background color
		this.setBackground(menuColor); // color of the top Frame
		this.getContentPane().setBackground(menuColor); // real menu background color
		setSize(420, 600);
		this.setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	public JFrame getMenu() {
		return this;
	}

	private class ButtonHandler implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			if (ae.getSource() == btnRule) {
				new Rule();
			} else if (ae.getSource() == btnQuit) {
				System.exit(0);
			} else if (ae.getSource() == btnStart) {
				dispose();
				new GameFrame();
			}
		}
	}

	private class Rule extends JDialog {
		private Container contents;
		private JTextArea textA;
		private String text;

		public Rule() {
			super(getMenu(), "Rule", true);
			contents = getContentPane();
			contents.setLayout(new FlowLayout());
			textA = new JTextArea("");

			// rule description
			text = "This is an animal battle chess game. In the game, there is a 4 x 4 chess board with 16 covered checkers.\n"
					+ "Players need to uncover checkers and eat all checkers of the opponent to win the game.\n"
					+ "Two players move alternatively. In each turn, they have 30 seconds to consider.\n"
					+ "Each player has 8 animal checkers: Elephant, Lion, Tiger, Leopard, Wolf, Dog, Cat, Rat.\n"
					+ "In usual, the relationship between animals is: Elephant > Lion > Tiger > Leopard > Wolf > Dog > Cat > Rat.\n"
					+ "Animals on the left of '>' can eat all animals on the right. For example, dog can eat cat and rat when touching.\n"
					+ "An exception is: when the rat and the elephant touches, the rat will eat the elephant.\n"
					+ "Magma will spread after 20 turns, that can eat any checker.\n"
					+ "To avoid the negative competition, if no chekcer is eaten in 10 turns, magma will immediately begin spreading.";

			textA.setText(text);
			textA.setEditable(false);
			textA.setBackground(null);

			contents.add(textA);

			setSize(730, 180);
			this.setLocationRelativeTo(null);
			setResizable(false);
			setVisible(true);
			this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		}

	}
}
