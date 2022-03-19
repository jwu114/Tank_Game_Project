
import javax.swing.Timer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class TwoPlayersMode extends GameMode {
	private Player p1;
	private Player p2;
	private BubbleTank playerTank1;
	private BubbleTank playerTank2;

	private Timer t1; // timer for repainting tank
	private Timer t2;// timer for repainting bullets
	private Timer t3;// timer for the game (120 seconds count down)

	public TwoPlayersMode() {
		p1 = new Player(KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_SHIFT,
				KeyEvent.KEY_LOCATION_RIGHT, MainFrame.frame.getWidth() / 4, MainFrame.frame.getHeight() / 2, 0);
		p2 = new Player(KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_ALT,
				KeyEvent.KEY_LOCATION_LEFT, MainFrame.frame.getWidth() * 3 / 4, MainFrame.frame.getHeight() / 2,
				Math.PI);
		playerTank1 = p1.getBubble();
		playerTank2 = p2.getBubble();
		setPlayer1(p1);
		setPlayer2(p2);

		initTimers();
		startTimers();
	}

	// Initialize timers
	@Override
	public void initTimers() {
		t1 = new Timer(1000 / 75, new BubbleTankAnimation());
		t2 = new Timer(1000 / 300, new BulletAnimation());
		t3 = new Timer(1000, new GameTimer());
	}

	@Override
	public void endTimers() {
		t1.stop();
		t2.stop();
		t3.stop();
	}

	@Override
	public void startTimers() {
		t1.start();
		t2.start();
		t3.start();
	}

	// class for moving tanks and refreshing the coordinate and direction of tanks
	private class BubbleTankAnimation implements ActionListener {
		// basically same to the BubbleTankAnimation class of OnePlayerMode class
		@Override
		public void actionPerformed(ActionEvent ae) {
			if (calDistance(playerTank1.getNewX(), playerTank2.getX(), playerTank1.getNewY(),
					playerTank2.getY()) >= (playerTank1.getRadius() + playerTank2.getRadius())) {
				playerTank1.moveBubble();
			}
			if (calDistance(playerTank1.getX(), playerTank2.getNewX(), playerTank1.getY(),
					playerTank2.getNewY()) >= (playerTank1.getRadius() + playerTank2.getRadius())) {
				playerTank2.moveBubble();
			}

			GameMode.canvas.repaint();
		}
	}
}
