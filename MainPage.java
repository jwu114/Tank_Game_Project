import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class MainPage implements ActionListener {
	private final String TITLE = "Bubble Tank Game";
	private JLabel title; // label for the title of game
	private Box box1; // a container with BoxLayout (hold components of Title Page)
	private Box box2; // a container with BoxLayout (hold components of Difficulty Selection Page)
	private JButton btnSuperEasy; // button for beginner mode
	private JButton btnEasy; // button for easy mode
	private JButton btnMedium; // button for medium mode
	private JButton btnHard; // button for difficult mode
	private JButton btnSuperHard; // button for expert mode
	private JButton btnBack1; // button for going back from difficulty selection page to title page
	private JButton btnBack2; // button for going back from help page to title page
	private JButton btnHelp; // button for accessing help page
	private JButton btnOnePlayer; // button for one player mode
	private JButton btnTwoPlayer; // button for two players mode

	private JPanel helpPage; // hold components of Help Page

	public MainPage() {
		drawMainPage();
	}

	// Creating the Title Page and add it to the frame
	public void drawMainPage() {
		box1 = Box.createVerticalBox();
		box1.add(Box.createVerticalGlue()); // add a bunch of blank space
		title = new JLabel(TITLE);
		btnOnePlayer = new JButton("One-Player Mode");
		btnTwoPlayer = new JButton("Two-Player Mode");
		btnHelp = new JButton("?");

		title.setFont(new Font("", Font.BOLD, 40));
		title.setForeground(Color.WHITE);
		title.setAlignmentX(JLabel.CENTER_ALIGNMENT);

		// There are plenty of similar codes for other GUI components, I will omit their
		// comments.
		btnOnePlayer.setForeground(Color.WHITE); // set font color
		btnOnePlayer.setBackground(Color.BLACK); // set background color
		btnOnePlayer.setBorder(BorderFactory.createLineBorder(Color.WHITE)); // add a border with color
		btnOnePlayer.setPreferredSize(new Dimension(50, 50)); // adjust the size of border
		btnOnePlayer.setAlignmentX(JButton.CENTER_ALIGNMENT); // align center
		btnOnePlayer.setFont(new Font("", Font.BOLD, 20)); // change font
		btnOnePlayer.addActionListener(this); // add action listener (button)

		btnTwoPlayer.setForeground(Color.WHITE);
		btnTwoPlayer.setBackground(Color.BLACK);
		btnTwoPlayer.setBorder(BorderFactory.createLineBorder(Color.WHITE));
		btnTwoPlayer.setPreferredSize(new Dimension(50, 50));
		btnTwoPlayer.setAlignmentX(JButton.CENTER_ALIGNMENT);
		btnTwoPlayer.setFont(new Font("", Font.BOLD, 20));
		btnTwoPlayer.addActionListener(this);

		btnHelp.setForeground(Color.WHITE);
		btnHelp.setBackground(Color.BLACK);
		btnHelp.setBorder(BorderFactory.createLineBorder(Color.WHITE));
		btnHelp.setPreferredSize(new Dimension(50, 50));
		btnHelp.setAlignmentX(JButton.CENTER_ALIGNMENT);
		btnHelp.setFont(new Font("", Font.BOLD, 20));
		btnHelp.addActionListener(this);
		btnHelp.setBounds(MainFrame.WIDTH - 45, MainFrame.Y + 15, 30, 30); // set the position and size of component in
																			// the frame
		// add components in the box container
		box1.add(title);
		box1.add(Box.createVerticalGlue());// add a bunch of blank space
		box1.add(btnOnePlayer);
		box1.add(Box.createVerticalStrut(10));// add a little blank space
		box1.add(btnTwoPlayer);
		box1.add(Box.createVerticalGlue());// add a bunch of blank space

		// add components in the frame
		MainFrame.frame.add(box1);
		MainFrame.frame.add(btnHelp);
		box1.setBounds(MainFrame.X, MainFrame.Y, MainFrame.WIDTH, MainFrame.HEIGHT);
		MainFrame.frame.revalidate(); // update the frame after adding new components
	}

	// Creating the Difficulty Selection Page and add it to the frame (detailed code is similar to the previous one)
	public void drawDifficultySelectionPage() {
		box2 = Box.createVerticalBox();
		box2.add(Box.createVerticalGlue());
		btnSuperEasy = new JButton("    Beginner    ");
		btnEasy = new JButton("       Easy       ");
		btnMedium = new JButton("     Medium     ");
		btnHard = new JButton("       Hard       ");
		btnSuperHard = new JButton("      Expert      ");
		btnBack1 = new JButton(" ↩ ");

		btnSuperEasy.setForeground(Color.WHITE);
		btnSuperEasy.setBackground(Color.BLACK);
		btnSuperEasy.setBorder(BorderFactory.createLineBorder(Color.WHITE));
		btnSuperEasy.setPreferredSize(new Dimension(70, 70));
		btnSuperEasy.setAlignmentX(JButton.CENTER_ALIGNMENT);
		btnSuperEasy.setFont(new Font("", Font.BOLD, 37)); // change font of button
		btnSuperEasy.addActionListener(this);

		btnEasy.setForeground(Color.WHITE);
		btnEasy.setBackground(Color.BLACK);
		btnEasy.setBorder(BorderFactory.createLineBorder(Color.WHITE));
		btnEasy.setPreferredSize(new Dimension(70, 70));
		btnEasy.setAlignmentX(JButton.CENTER_ALIGNMENT);
		btnEasy.setFont(new Font("", Font.BOLD, 38)); // change font of button
		btnEasy.addActionListener(this);

		btnMedium.setForeground(Color.WHITE);
		btnMedium.setBackground(Color.BLACK);
		btnMedium.setBorder(BorderFactory.createLineBorder(Color.WHITE));
		btnMedium.setPreferredSize(new Dimension(70, 70));
		btnMedium.setAlignmentX(JButton.CENTER_ALIGNMENT);
		btnMedium.setFont(new Font("", Font.BOLD, 37));
		btnMedium.addActionListener(this);

		btnHard.setForeground(Color.WHITE);
		btnHard.setBackground(Color.BLACK);
		btnHard.setBorder(BorderFactory.createLineBorder(Color.WHITE));
		btnHard.setPreferredSize(new Dimension(70, 70));
		btnHard.setAlignmentX(JButton.CENTER_ALIGNMENT);
		btnHard.setFont(new Font("", Font.BOLD, 38));
		btnHard.addActionListener(this);

		btnSuperHard.setForeground(Color.WHITE);
		btnSuperHard.setBackground(Color.BLACK);
		btnSuperHard.setBorder(BorderFactory.createLineBorder(Color.WHITE));
		btnSuperHard.setPreferredSize(new Dimension(70, 70));
		btnSuperHard.setAlignmentX(JButton.CENTER_ALIGNMENT);
		btnSuperHard.setFont(new Font("", Font.BOLD, 38));
		btnSuperHard.addActionListener(this);

		btnBack1.setForeground(Color.WHITE);
		btnBack1.setBackground(Color.BLACK);
		btnBack1.setBorder(BorderFactory.createLineBorder(Color.WHITE));
		btnBack1.setAlignmentX(JButton.CENTER_ALIGNMENT);
		btnBack1.setFont(new Font("", Font.BOLD, 27));
		btnBack1.addActionListener(this);

		box2.add(Box.createVerticalGlue());
		box2.add(btnSuperEasy);
		box2.add(Box.createVerticalStrut(30));
		box2.add(btnEasy);
		box2.add(Box.createVerticalStrut(30));
		box2.add(btnMedium);
		box2.add(Box.createVerticalStrut(30));
		box2.add(btnHard);
		box2.add(Box.createVerticalStrut(30));
		box2.add(btnSuperHard);
		box2.add(Box.createVerticalStrut(90));
		box2.add(btnBack1);
		box2.add(Box.createVerticalGlue());

		box2.setBounds(MainFrame.X, MainFrame.Y, MainFrame.WIDTH, MainFrame.HEIGHT);
		MainFrame.frame.add(box2);
		MainFrame.frame.repaint();
		MainFrame.frame.revalidate();
	}

	// Creating the Help Page and add it to the frame (detailed code is similar to the previous one)
	public void drawHelpPage() {
		helpPage = new JPanel();
		helpPage.setLayout(null);
		helpPage.setBackground(Color.BLACK);

		// the text content of help page
		String helpContent = "\nHelp Page\n " + "\n" + "Key Mapping: \n" + "One-Player Mode: \n"
				+ "Direction Key: ↑ ↓ ← →\tShooting Key: Left SHIFT \n" + "\n" + "Two-Player Mode: \n"
				+ "P1: Direction Key: ↑ ↓ ← →\tShooting Key: Right SHIFT \n"
				+ "P2: Direction Key: W S A D\tShooting Key: Left ALT (OPTION) \n" + "\n" + "Pause Key: ESC \n" + "\n"
				+ "Game Rule: \n" + "Players operate tank to shoot the opponent. \n"
				+ "Each time of accurate shoot can earn one point. \n"
				+ "After 120 seconds, the player with higher score will win the game.\n"
				+ "\nWhen shooting bullets, one complete press and release of the key counts as one shoot.\n"
				+ "\nTank can go through one side of the boundary and come out from the opposite side.";

		JTextArea text = new JTextArea(helpContent);
		text.setEditable(false);
		text.setBackground(Color.BLACK);
		text.setForeground(Color.WHITE);
		text.setAlignmentX(JButton.CENTER_ALIGNMENT);
		text.setFont(new Font("", Font.BOLD, 20));
		text.setBounds(MainFrame.X + 50, MainFrame.Y + 25, MainFrame.WIDTH, MainFrame.HEIGHT - 150);

		btnBack2 = new JButton(" ↩ ");
		btnBack2.setForeground(Color.WHITE);
		btnBack2.setBackground(Color.BLACK);
		btnBack2.setBorder(BorderFactory.createLineBorder(Color.WHITE));
		// btnBack1.setPreferredSize(new Dimension(50, 50));
		btnBack2.setAlignmentX(JButton.CENTER_ALIGNMENT);
		btnBack2.setFont(new Font("", Font.BOLD, 27)); // change font of button
		btnBack2.addActionListener(this);
		btnBack2.setBounds(MainFrame.WIDTH / 2 - 25, MainFrame.HEIGHT - 75, 50, 50);

		helpPage.add(text);
		helpPage.add(btnBack2);
		helpPage.setBounds(MainFrame.X, MainFrame.Y, MainFrame.WIDTH, MainFrame.HEIGHT);
		MainFrame.frame.add(helpPage);
		MainFrame.frame.repaint(); // update the frame after removing components from the ContentPane
		MainFrame.frame.revalidate(); // update the frame after adding new component to the Content Pane of frame
	}

	// Listener for buttons (call method to remove components from old page, call
	// method to add components of new page)
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource() == btnOnePlayer) {
			MainFrame.frame.remove(box1);
			MainFrame.frame.remove(btnHelp);
			drawDifficultySelectionPage();

		} else if (ae.getSource() == btnTwoPlayer) {
			MainFrame.frame.remove(box1);
			MainFrame.frame.remove(btnHelp);
			new TwoPlayersMode();

		} else if (ae.getSource() == btnHelp) {
			MainFrame.frame.getContentPane().removeAll();
			MainFrame.frame.getContentPane().remove(btnHelp);
			drawHelpPage();

		} else if (ae.getSource() == btnSuperEasy) {
			MainFrame.frame.remove(box2);
			new OnePlayerMode(OnePlayerMode.SUPEREASY);

		} else if (ae.getSource() == btnEasy) {
			MainFrame.frame.remove(box2);
			new OnePlayerMode(OnePlayerMode.EASY);

		} else if (ae.getSource() == btnMedium) {
			MainFrame.frame.remove(box2);
			new OnePlayerMode(OnePlayerMode.MEDIUM);

		} else if (ae.getSource() == btnHard) {
			MainFrame.frame.remove(box2);
			new OnePlayerMode(OnePlayerMode.HARD);
		} else if (ae.getSource() == btnSuperHard) {
			MainFrame.frame.remove(box2);
			new OnePlayerMode(OnePlayerMode.SUPERHARD);
		} else if (ae.getSource() == btnBack1) {
			MainFrame.frame.remove(box2);
			drawMainPage();
		} else if (ae.getSource() == btnBack2) {
			MainFrame.frame.remove(helpPage);
			drawMainPage();
		}
	}
}
