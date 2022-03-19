import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Player {
	private BubbleTank tank; // tank of the player
	private int score; // score of the player
	private int keyUp; // key mapping for upward direction
	private int keyDown; // key mapping for downward direction
	private int keyLeft; // key mapping for left direction
	private int keyRight; // key mapping for right direction

	// Since ALT and SHIFT are used as the key for shooting. There are two ALT and
	// SHIFT separately on the keyboard, so it is necessary to distinguish the left
	// and right key (keyLocationShoot achieves this function)
	private int keyShoot; // key mapping 1 for shooting
	private int keyLocationShoot; // key mapping 2 for shooting

	// KeyListeners
	private KeyHandler1 kh1 = new KeyHandler1();
	private KeyHandler2 kh2 = new KeyHandler2();
	private KeyHandler3 kh3 = new KeyHandler3();

	// the constructor for computer (computer does not need to use keyboard)
	public Player(int initPositionX, int initPositionY, double initDegree) {
		// instantiate the tank (determine the initial position and direction)
		tank = new BubbleTank(initPositionX, initPositionY, initDegree);
		score = 0;
	}

	// the constructor of users
	public Player(int keyUp, int keyDown, int keyLeft, int keyRight, int keyShoot, int keyLocationShoot,
			int initPositionX, int initPositionY, double initDegree) {
		this.keyUp = keyUp;
		this.keyDown = keyDown;
		this.keyLeft = keyLeft;
		this.keyRight = keyRight;
		this.keyShoot = keyShoot;
		this.keyLocationShoot = keyLocationShoot;
		tank = new BubbleTank(initPositionX, initPositionY, initDegree);
		score = 0;
		MainFrame.frame.requestFocusInWindow(); // make the window get focus
		MainFrame.frame.addKeyListener(kh1);
		MainFrame.frame.addKeyListener(kh2);
		MainFrame.frame.addKeyListener(kh3);
	}

	// getters
	public BubbleTank getBubble() {
		return tank;
	}

	public int getScore() {
		return score;
	}

	// setter
	public void setScore(int score) {
		this.score = score;
	}

	// KeyListeners only change the value of variables but do not actually repaint objects
	
	// KeyListener for upward and downward direction
	private class KeyHandler1 implements KeyListener { // KeyListener for up and down direction key
		@Override
		public void keyTyped(KeyEvent e) {

		}

		@Override
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == keyUp) { // up key
				tank.setDirectionUD(BubbleTank.FORWARD);
			} else if (e.getKeyCode() == keyDown) { // down key
				tank.setDirectionUD(BubbleTank.BACKWARD);
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			if (e.getKeyCode() == keyUp) { // up key
				tank.setDirectionUD(BubbleTank.STOP);
			} else if (e.getKeyCode() == keyDown) { // down key
				tank.setDirectionUD(BubbleTank.STOP);
			}
		}
	}

	// KeyListener for left and right direction
	private class KeyHandler2 implements KeyListener { // KeyListener for left and right direction key
		@Override
		public void keyTyped(KeyEvent e) {

		}

		@Override
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == keyLeft) { // left key
				tank.setDirectionLR(BubbleTank.LEFT);
			} else if (e.getKeyCode() == keyRight) { // right key
				tank.setDirectionLR(BubbleTank.RIGHT);
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			if (e.getKeyCode() == keyLeft) { // left key
				tank.setDirectionLR(BubbleTank.STOP);
			} else if (e.getKeyCode() == keyRight) { // down key
				tank.setDirectionLR(BubbleTank.STOP);
			}
		}
	}

	// KeyListener for shooting key
	private class KeyHandler3 implements KeyListener {
		@Override
		public void keyTyped(KeyEvent e) {

		}

		@Override
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == keyShoot && e.getKeyLocation() == keyLocationShoot) { // Shooting key
				tank.shoot();
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {

		}
	}

	// remove all listener at the end of one game (since removeAll() cannot remove keyListeners)
	public void removeAllListeners() {
		MainFrame.frame.removeKeyListener(kh1);
		MainFrame.frame.removeKeyListener(kh2);
		MainFrame.frame.removeKeyListener(kh3);
	}
}
