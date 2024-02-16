package tank_game;
/**
 * File: MainClass.java
 * 
 * @author  Jiarui Wu
 * @version 1.0
 * @since   2021-11-08
 */

import javax.swing.JFrame;

import java.awt.Color;


public class MainClass{

	/**
	 * @author Jiarui Wu
	 * @email jwu114@u.rochester.edu
	 */
	
	protected static JFrame frame;

	protected static final int X = 0;
	protected static final int Y = 0;
	protected static final int WIDTH = 1000; // width of frame
	protected static final int HEIGHT = 700; // height of frame without counting the height of title bar

	public MainClass() {
		// Basic settings of the main frame
		frame = new JFrame("Bubble Tank");
		frame.setLayout(null);
		
		new MainPage(); //Add more components to the main frame
		
		// Basic settings of the main frame
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(WIDTH, HEIGHT + 23); // height of title is 23
		frame.setResizable(false);
		frame.getContentPane().setBackground(Color.BLACK);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

	}

	

	public static void main(String[] args) {
		new MainClass();
	}
}
