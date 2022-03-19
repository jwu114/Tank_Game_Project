import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;

import javax.swing.JComponent;
import javax.swing.JLabel;

public abstract class GameMode {
	protected static Canvas canvas; // the place to hold all components in the game (tank, bullet, timer, score)
	private JLabel timer; // GUI of timer
	private JLabel score1; // GUI of p1's score
	private JLabel score2; // GUI of p2's score

	private int time; // the time remained

	private Player p1;
	private Player p2;
	private BubbleTank playerTank1;
	private BubbleTank playerTank2;

	private Player winner; // winner of the game

	private KeyHandler kh = new KeyHandler(); // key listener for detecting ESC key (Pause the game)

	public GameMode() {
		time = 120; // timer is set to 120 seconds

		// GUI Components
		score1 = new JLabel("0"); // initial score is 0
		score2 = new JLabel("0");// initial score is 0
		timer = new JLabel("" + time);

		score1.setFont(new Font("", Font.BOLD, 25));
		score2.setFont(new Font("", Font.BOLD, 25));
		timer.setFont(new Font("", Font.BOLD, 25));
		score1.setForeground(Color.WHITE);
		score2.setForeground(Color.RED);
		timer.setForeground(Color.BLUE);

		// set the position and size
		score1.setBounds(60, 0, 100, 50);
		timer.setBounds(MainFrame.WIDTH / 2 - 20, 0, 100, 50);
		score2.setBounds(MainFrame.WIDTH - 70, 0, 100, 50);

		canvas = new Canvas();
		canvas.setBounds(MainFrame.X, MainFrame.Y, MainFrame.WIDTH, MainFrame.HEIGHT);

		MainFrame.frame.requestFocusInWindow(); // make the window get focus
		MainFrame.frame.addKeyListener(kh);
		MainFrame.frame.add(score1);
		MainFrame.frame.add(timer);
		MainFrame.frame.add(score2);
		MainFrame.frame.add(canvas);
		MainFrame.frame.revalidate();
	}

	// one-player mode and two-player mode have different timers, so methods about timer are abstract methods
	public abstract void initTimers();

	public abstract void startTimers();

	// setters
	public void setPlayer1(Player p1) {
		this.p1 = p1;
		playerTank1 = p1.getBubble();
	}

	public void setPlayer2(Player p2) {
		this.p2 = p2;
		playerTank2 = p2.getBubble();
	}

	public void setTank1(BubbleTank playerTank1) {
		this.playerTank1 = playerTank1;
	}

	public void setTank2(BubbleTank playerTank2) {
		this.playerTank2 = playerTank2;
	}

	// getters
	public Player getPlayer1() {
		return p1;
	}

	public Player getPlayer2() {
		return p2;
	}

	public Player getWinner() {
		return winner;
	}

	// add the listener
	public void addKeyListener() {
		MainFrame.frame.addKeyListener(kh);
	}

	// remove the listener at the end of one game or when pausing the game (since removeAll() cannot remove keyListeners)
	public void removeKeyListener() {
		MainFrame.frame.removeKeyListener(kh);
	}

	protected abstract class BubbleTankAnimation implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ae) {

		}
	}

	// the method for bullet motion
	protected class BulletAnimation implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ae) {
			HashSet<Bullet> bullets1 = playerTank1.getBullets();
			HashSet<Bullet> invalidBullets1 = new HashSet<Bullet>();
			HashSet<Bullet> bullets2 = playerTank2.getBullets();
			HashSet<Bullet> invalidBullets2 = new HashSet<Bullet>();
			// Bullets of p1
			for (Bullet bullet : bullets1) {
				// search all invalid bullets
				if (!bullet.moveBullet()) { // change the coordinate of bullet and judge whether it arrives the boundary (invalid)
					invalidBullets1.add(bullet);
				}
				if (calDistance(bullet.getX(), playerTank2.getX(), bullet.getY(),
						playerTank2.getY()) < (bullet.getRadius() + playerTank2.getRadius())) { //hits the opponent (invalid)
					invalidBullets1.add(bullet);
					p1.setScore(p1.getScore() + 1);
					score1.setText("" + p1.getScore());
				}
			}

			// remove all invalid bullets
			for (Bullet invalidBullet : invalidBullets1) {
				playerTank1.removeBullet(invalidBullet);
			}

			// Bullets of p2
			for (Bullet bullet : bullets2) {
				if (!bullet.moveBullet()) {
					invalidBullets2.add(bullet);
				}
				if (calDistance(bullet.getX(), playerTank1.getX(), bullet.getY(),
						playerTank1.getY()) < (bullet.getRadius() + playerTank1.getRadius())) {
					invalidBullets2.add(bullet);
					p2.setScore(p2.getScore() + 1);
					score2.setText("" + p2.getScore());
				}
			}

			for (Bullet invalidBullet : invalidBullets2) {
				playerTank2.removeBullet(invalidBullet);
			}
		}
	}

	// use the coordinates of two points to calculate their distance
	public double calDistance(double x1, double x2, double y1, double y2) {
		return Math.sqrt(Math.pow((x1 - x2), 2) + Math.pow((y1 - y2), 2));
	}

	public abstract void endTimers();

	protected class GameTimer implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ae) {
			timer.setText("" + --time); // count down
			if (time == 0) { // the game is end
				endTimers(); // stop timers
				// determine the winner
				if (GameMode.this.p1.getScore() > GameMode.this.p2.getScore()) {
					winner = GameMode.this.p1;
				} else if (GameMode.this.p2.getScore() > GameMode.this.p1.getScore()) {
					winner = GameMode.this.p2;
				} else {
					winner = null;
				}
				// call the menu class to show the game result
				new Menu(GameMode.this, Menu.END);
			}
		}
	}

	protected class KeyHandler implements KeyListener {

		@Override
		public void keyTyped(KeyEvent e) {

		}

		@Override
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				endTimers();
				removeKeyListener();
				playerTank1.setDirectionLR(BubbleTank.STOP);
				playerTank2.setDirectionLR(BubbleTank.STOP);
				playerTank1.setDirectionUD(BubbleTank.STOP);
				playerTank2.setDirectionUD(BubbleTank.STOP);
				new Menu(GameMode.this, Menu.PAUSE);
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {

		}

	}

	protected class Canvas extends JComponent {

		private static final long serialVersionUID = 1L;

		@Override
		public void paintComponent(Graphics g) {
			// draw p1.getBubble()
			g.setColor(Color.WHITE);
			g.drawOval((int) p1.getBubble().getX() - p1.getBubble().getRadius(),
					(int) p1.getBubble().getY() - p1.getBubble().getRadius(), p1.getBubble().getRadius() * 2,
					p1.getBubble().getRadius() * 2);
			g.drawLine(
					(int) p1.getBubble().getX()
							+ (int) (Math.cos(p1.getBubble().getDegree()) * (p1.getBubble().getRadius() / 2)),
					(int) p1.getBubble().getY()
							- (int) (Math.sin(p1.getBubble().getDegree()) * (p1.getBubble().getRadius() / 2)),
					(int) p1.getBubble().getX()
							+ (int) (Math.cos(p1.getBubble().getDegree()) * p1.getBubble().getRadius()),
					(int) p1.getBubble().getY()
							- (int) (Math.sin(p1.getBubble().getDegree()) * p1.getBubble().getRadius()));

			// draw p2.getBubble()
			g.setColor(Color.RED);
			g.drawOval((int) p2.getBubble().getX() - p2.getBubble().getRadius(),
					(int) p2.getBubble().getY() - p2.getBubble().getRadius(), p2.getBubble().getRadius() * 2,
					p2.getBubble().getRadius() * 2);
			g.drawLine(
					(int) p2.getBubble().getX()
							+ (int) (Math.cos(p2.getBubble().getDegree()) * (p2.getBubble().getRadius() / 2)),
					(int) p2.getBubble().getY()
							- (int) (Math.sin(p2.getBubble().getDegree()) * (p2.getBubble().getRadius() / 2)),
					(int) p2.getBubble().getX()
							+ (int) (Math.cos(p2.getBubble().getDegree()) * p2.getBubble().getRadius()),
					(int) p2.getBubble().getY()
							- (int) (Math.sin(p2.getBubble().getDegree()) * p2.getBubble().getRadius()));

			// draw bullets of Player1
			HashSet<Bullet> bullets1 = p1.getBubble().getBullets();
			for (Bullet bullet : bullets1) {
				g.setColor(Color.WHITE);
				g.fillOval((int) bullet.getX() - bullet.getRadius(), (int) bullet.getY() - bullet.getRadius(),
						bullet.getRadius() * 2, bullet.getRadius() * 2);
			}

			// draw bullets of Player2
			HashSet<Bullet> bullets2 = p2.getBubble().getBullets();
			for (Bullet bullet : bullets2) {
				g.setColor(Color.RED);
				g.fillOval((int) bullet.getX() - bullet.getRadius(), (int) bullet.getY() - bullet.getRadius(),
						bullet.getRadius() * 2, bullet.getRadius() * 2);
			}
		}
	}

}
