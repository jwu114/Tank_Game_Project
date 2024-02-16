package mine_sweeper;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Frame extends JFrame {
	private Container contents;
	private Game game;

	private JPanel top;
	private JPanel bottom;

	private JRadioButton size5;
	private JRadioButton size7;
	private JRadioButton size9;
	private JRadioButton dif1;
	private JRadioButton dif2;
	private JRadioButton dif3;
	private JButton restartButton;
	private JButton exitButton;

	public Frame() {
		super("mine sweeping");
		contents = getContentPane();

		BorderLayout bl = new BorderLayout();

		contents.setLayout(bl);

		game = new Game(5, 1);
		contents.add(game, SwingConstants.CENTER);

		top = new JPanel();
		bottom = new JPanel();

		top = TopPane();
		bottom = BottomPane();

		KeyHandler kh = new KeyHandler();
		restartButton.addKeyListener(kh);

		contents.add(top, bl.NORTH);
		contents.add(bottom, bl.SOUTH);

		setSize(390, 390);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	public JPanel TopPane() {
		JPanel p = new JPanel();
		JPanel button = new JPanel();

		ButtonHandler bh = new ButtonHandler();

		p.setLayout(new BorderLayout());

		restartButton = new JButton("Restart");
		exitButton = new JButton("Exit");

		restartButton.addActionListener(bh);
		exitButton.addActionListener(bh);

		button.add(restartButton);
		button.add(exitButton);

		p.add(game.getPanel(), BorderLayout.WEST);
		p.add(button, BorderLayout.EAST);

		return p;
	}

	public JPanel BottomPane() {
		JPanel p = new JPanel();

		size5 = new JRadioButton("5×5");
		size7 = new JRadioButton("7×7");
		size9 = new JRadioButton("9×9");
		dif1 = new JRadioButton("primary");
		dif2 = new JRadioButton("middle");
		dif3 = new JRadioButton("high");
		/*
		 * p.add(size5); p.add(size7); p.add(size9); p.add(dif1); p.add(dif2);
		 * p.add(dif3);
		 */

		return p;
	}

	public JFrame getFrame() {
		return this;
	}

	public static void main(String[] args) {
		Frame f = new Frame();
		f.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	private class ButtonHandler implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			if (ae.getSource() == exitButton) {
				System.exit(0);
			} else if (ae.getSource() == restartButton) {
				game.end();
				game.setUpGame(15, 1);
				game.restartTimeL();
				game.restartRemain();

				validate();
			}
		}
	}

	private class KeyHandler implements KeyListener {
		public void keyReleased(KeyEvent ke) {

		}

		public void keyPressed(KeyEvent ke) {
			if (ke.getKeyCode() == 70) {
				Rank rank = new Rank();
				rank.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
			}
		}

		public void keyTyped(KeyEvent ke) {

		}
	}

	private class Rank extends JDialog {
		private Container contents;
		private String text;
		private JTextArea textA;
		private ArrayList<Score> scores;

	}
}
