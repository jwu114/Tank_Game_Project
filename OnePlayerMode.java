
import javax.swing.Timer;

import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.awt.event.ActionEvent;

public class OnePlayerMode extends GameMode implements ComputerBehaviors {
	// integer corresponded to different difficulty mode
	protected static final int SUPEREASY = 0;
	protected static final int EASY = 1;
	protected static final int MEDIUM = 2;
	protected static final int HARD = 3;
	protected static final int SUPERHARD = 4;
	private int difficulty;

	private Player p;
	private Player computer;
	private BubbleTank playerTank;
	private BubbleTank computerTank;

	private Timer t1; // timer for repainting tank
	private Timer t2; // timer for repainting bullets
	private Timer t3; // timer for the game (120 seconds count down)
	private Timer t4; // timer for computer tank to shoot bullets

	public OnePlayerMode(int difficulty) {
		// Initialize field variables
		this.difficulty = difficulty;

		p = new Player(KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_SHIFT,
				KeyEvent.KEY_LOCATION_LEFT, MainFrame.frame.getWidth() / 4, MainFrame.frame.getHeight() / 2, 0);
		playerTank = p.getBubble();
		computer = new Player(MainFrame.frame.getWidth() * 3 / 4, MainFrame.frame.getHeight() / 2, Math.PI);
		computerTank = computer.getBubble();
		super.setPlayer1(p);
		super.setPlayer2(computer);
		initTimers();
		startTimers();
	}

	// Initialize timers
	@Override
	public void initTimers() {
		// Timers
		t1 = new Timer(1000 / 75, new BubbleTankAnimation()); // the delay of timer control the speed of tank
		t2 = new Timer(1000 / 300, new BulletAnimation()); // the delay of timer control the speed of bullet
		t3 = new Timer(1000, new GameTimer());
		switch (difficulty) {
		case SUPEREASY:
			// Beginner mode has slowest shooting speed 1.2 seconds per bullet
			t4 = new Timer(1200, new BulletShooting()); // the delay of timer control the speed of computer bullet
														// shooting
			break;
		case EASY:
			// CD is 1.2 seconds
			t4 = new Timer(1200, new BulletShooting());
			break;
		case MEDIUM:
			// CD is 0.9 second
			t4 = new Timer(900, new BulletShooting());
			break;
		case HARD:
			// CD is 0.6 second
			t4 = new Timer(600, new BulletShooting());
			break;
		case SUPERHARD:
			// CD is 0.25 second
			t4 = new Timer(250, new BulletShooting());
			break;
		}
	}

	// start all timers
	@Override
	public void startTimers() {
		t1.start();
		t2.start();
		t3.start();
		t4.start();
	}

	// end all timers at the end of the game
	@Override
	public void endTimers() {
		t1.stop();
		t2.stop();
		t3.stop();
		t4.stop();
	}

	// class for moving and refreshing the coordinate and direction of tanks
	private class BubbleTankAnimation implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent ae) {
			moveComputerTank(); // call the special motion mechanism of computer tank

			// Player's tank cannot move if the distance between player's tank and
			// computer's tank is larger than 57
			// Since, for some specific case, computer tank cannot automatically escape
			// player's tank (e.x when passing through the boundary)
			if (calDistance(playerTank.getNewX(), computerTank.getX(), playerTank.getNewY(),
					computerTank.getY()) >= (playerTank.getRadius() + computerTank.getRadius() + 57)) {
				playerTank.moveBubble();
			}
			// Computer's tank cannot move if two tanks touch each other (collision
			// detection between two tanks)
			if (calDistance(playerTank.getX(), computerTank.getNewX(), playerTank.getY(),
					computerTank.getNewY()) >= (playerTank.getRadius() + computerTank.getRadius())) {
				computerTank.moveBubble();
			}

			// The only place for calling the canvas repaint()
			GameMode.canvas.repaint();
		}
	}

	// class for computer tank shooting
	private class BulletShooting extends ComputerBulletShooting {
		@Override
		public void actionPerformed(ActionEvent ae) {
			computerTank.shoot();
		}
	}

	// after judging that it's necessary for tank to escape, call this method to
	// move the computer tank towards the perpendicular direction
	@Override
	public void escapeBullet(Bullet bullet) {
		// adjust the degree to the perpendicular direction
		adjustDegree(computerTank.getX(), computerTank.getY(), computerTank.getDegree(),
				computerTank.getX() + (bullet.getY() - computerTank.getY()),
				computerTank.getY() - (bullet.getX() - computerTank.getX()));

		// the computer tank always go backwards when escaping bullets
		computerTank.setDirectionUD(BubbleTank.BACKWARD);
	}

	// determine that which boundary the computer tank is close to (top boundary,
	// bottom boundary, left boundary, or right boundary?)
	// also determine whether the computer tank just pass through a boundary and
	// come out.
	@Override
	public int getCloseToWallType() {
		int situation = -2;

		// if the computer tank will collide the player tank after coming out from the
		// opposite side of boundary
		if (calDistance(playerTank.getX(), computerTank.getNewX(), playerTank.getY(),
				computerTank.getNewY()) < (playerTank.getRadius() + computerTank.getRadius() + 2)) {
			situation = -1; // cannot pass through the boundary
		} else {
			// computer tank didn't pass through the boundary just now
			if (!computerTank.getFlag()) {
				situation = -1;
			}

			if (computerTank.getX() <= MainFrame.X + 10) {
				if (!computerTank.getFlag()) {
					// computer tank is close to left boundary (didn't pass through the boundary
					// just now)
					situation = 0;
				}
			} else if (computerTank.getX() >= MainFrame.X + MainFrame.WIDTH - 10) {
				if (!computerTank.getFlag()) {
					// computer tank is close to right boundary (didn't pass through the boundary
					// just now)
					situation = 1;
				}
			} else if (computerTank.getY() <= MainFrame.Y + 10) {
				if (!computerTank.getFlag()) {
					// computer tank is close to top boundary (didn't pass through the boundary just
					// now)
					situation = 2;
				}
			} else if (computerTank.getY() >= MainFrame.Y + MainFrame.HEIGHT - 10) {
				if (!computerTank.getFlag()) {
					// computer tank is close to bottom boundary (didn't pass through the boundary
					// just now)
					situation = 3;
				}
			} else if (computerTank.getX() >= MainFrame.X + 10 && computerTank.getX() < MainFrame.WIDTH / 2) {
				if (computerTank.getFlag()) {
					// computer tank is close to left boundary (just passed through the boundary)
					situation = 4;

				}
			} else if (computerTank.getX() <= MainFrame.X + MainFrame.WIDTH - 10
					&& computerTank.getX() > MainFrame.WIDTH / 2) {
				if (computerTank.getFlag()) {
					// computer tank is close to right boundary (just passed through the boundary)
					situation = 5;
				}
			} else if (computerTank.getY() >= MainFrame.Y + 10 && computerTank.getY() < MainFrame.HEIGHT / 2) {
				if (computerTank.getFlag()) {
					// computer tank is close to top boundary (just passed through the boundary)
					situation = 6;
				}
			} else if (computerTank.getY() <= MainFrame.Y + MainFrame.HEIGHT - 10
					&& computerTank.getY() > MainFrame.HEIGHT / 2) {
				if (computerTank.getFlag()) {
					// computer tank is close to bottom boundary (just passed through the boundary)
					situation = 7;
				}
			}
		}
		return situation;
	}

	// the overall method for controlling the motion of computer tank
	@Override
	public void moveComputerTank() {
		int situation = getCloseToWallType();

		// use the (situation variable) to determine that whether computer tank needs to
		// pass through or come out from the boundary
		if (situation != -1) { // need to pass through or come out from the boundary
			moveNearWall(situation);
		} else { // do not need to do so
			HashSet<Bullet> bullets = playerTank.getBullets();

			// start the part of escaping the bullets
			Bullet minBullet = null;
			double minDistance = 5000;

			// find the closest bullet
			for (Bullet bullet : bullets) {
				double distance = calDistance(bullet.getX(), computerTank.getX(), bullet.getY(), computerTank.getY());
				if (distance < minDistance) {
					minDistance = distance;
					minBullet = bullet;
				}
			}

			// different difficulty modes have different dangerRanges
			int dangerRange;
			if (difficulty == SUPEREASY) {
				// has the smallest danger range, so it's very possible that computer tank cannot successfully escape
				dangerRange = 25; 
			} else if (difficulty == EASY) {
				dangerRange = 30;
			} else if (difficulty == MEDIUM) {
				dangerRange = 70;
			} else if (difficulty == HARD) {
				dangerRange = 70;
			} else {
				dangerRange = 70;
			}

			if (minDistance <= dangerRange) { // if the distance between computer tank and the closest bullet is smaller
												// than the dangerRange
				// further predict whether the bullet will collide the computer tank
				long k = (long) Math.tan(minBullet.getDegree());
				long b = (long) (-k * minBullet.getX() + minBullet.getY());
				long predictDistance = (long) (Math.abs(-k * computerTank.getX() + computerTank.getY() - b)
						/ Math.sqrt(Math.pow(k, 2) + 1));

				if (predictDistance <= minBullet.getRadius() + computerTank.getRadius() + 5) { // very possible to
																								// collide
					escapeBullet(minBullet); // escape the bullet
				} else {
					moveKeepingSafetyDistance(); // move regularly
				}
			} else {
				moveKeepingSafetyDistance();// move regularly
			}

		}
	}

	// control the motion of computer tank when it is near the boundary
	@Override
	public void moveNearWall(int situation) {
		switch (situation) {
		case 0:
			// move backwards and pass through the left boundary
			computerTank.setDirectionUD(BubbleTank.BACKWARD);
			adjustDegree(computerTank.getDegree(), 0);
			break;
		case 1:
			// move backwards and pass through the right boundary
			computerTank.setDirectionUD(BubbleTank.BACKWARD);
			adjustDegree(computerTank.getDegree(), Math.PI);
			break;
		case 2:
			// move backwards and pass through the top boundary
			computerTank.setDirectionUD(BubbleTank.BACKWARD);
			adjustDegree(computerTank.getDegree(), 3 * Math.PI / 2);
			break;
		case 3:
			// move backwards and pass through the bottom boundary
			computerTank.setDirectionUD(BubbleTank.BACKWARD);
			adjustDegree(computerTank.getDegree(), Math.PI / 2);
			break;
		case 4:
			// move forward and come out from the right boundary
			computerTank.setDirectionUD(BubbleTank.FORWARD);
			if (adjustDegree(computerTank.getDegree(), 0)) {
				computerTank.setFlag(false);
			}
			break;
		case 5:
			// move forward and come out from the left boundary
			computerTank.setDirectionUD(BubbleTank.FORWARD);
			if (adjustDegree(computerTank.getDegree(), Math.PI)) {
				computerTank.setFlag(false);
			}
			break;
		case 6:
			// move forward and come out from the bottom boundary
			computerTank.setDirectionUD(BubbleTank.FORWARD);
			if (adjustDegree(computerTank.getDegree(), 3 * Math.PI / 2)) {
				computerTank.setFlag(false);
			}
			break;
		case 7:
			// move forward and come out from the top boundary
			computerTank.setDirectionUD(BubbleTank.FORWARD);
			if (adjustDegree(computerTank.getDegree(), Math.PI / 2)) {
				computerTank.setFlag(false);
			}
			break;
		default:
			break;
		}
	}

	// control the motion of computer when it is not near the wall (regular motion)
	@Override
	public void moveKeepingSafetyDistance() {
		double xAI = computerTank.getX();
		double yAI = computerTank.getY();
		double degreeAI = computerTank.getDegree();
		double x = playerTank.getX();
		double y = playerTank.getY();
		double distance = calDistance(xAI, x, yAI, y);

		// if the distance is too large
		if (distance > 350) {
			// move towards the player's tank
			adjustDegree(xAI, yAI, degreeAI, x, y);
			computerTank.setDirectionUD(BubbleTank.FORWARD);

			// if the distance is too small
		} else if (distance < 100) {
			// move away from the player's tank
			adjustDegree(xAI, yAI, degreeAI, x, y);
			computerTank.setDirectionUD(BubbleTank.BACKWARD);

			// the distance is proper
		} else {
			// stop and just shooting bullets
			adjustDegree(xAI, yAI, degreeAI, x, y);
			computerTank.setDirectionUD(BubbleTank.STOP);
		}
	}

	// adjust the degree (parameter is current degree and target degree)
	// used when computer needs to pass adjust degree and pass through the boundary
	@Override
	public boolean adjustDegree(double degree, double degreeTarget) {
		boolean finished = false;

		double difference = degreeTarget - degree;

		if (difference > 0.1) {
			computerTank.setDirectionUD(BubbleTank.STOP);
			if (difference <= Math.PI)
				computerTank.setDirectionLR(BubbleTank.LEFT);
			else
				computerTank.setDirectionLR(BubbleTank.RIGHT);
		} else if (difference < -0.1) {
			computerTank.setDirectionUD(BubbleTank.STOP);
			if (Math.abs(difference) >= Math.PI)
				computerTank.setDirectionLR(BubbleTank.LEFT);
			else
				computerTank.setDirectionLR(BubbleTank.RIGHT);
		} else {
			finished = true;
			computerTank.setDirectionLR(BubbleTank.STOP);
		}

		// return whether the degree is already adjusted to the target degree
		// (immediately stop)
		return finished;
	}

	// adjust the degree (parameter is current coordinate, current degree and target
	// coordinate)
	// used when computer escape bullets, escape player's tank, and chase player's
	// tank
	@Override
	public void adjustDegree(double x, double y, double degree, double xTarget, double yTarget) {
		double degreeTarget = 0;

		// using the current coordinate and target coordinate to calculate the target
		// degree
		if (y - yTarget >= 0) { // treating AI as center, the degree of the position of player
			if (xTarget - x > 0) {
				degreeTarget = Math.atan((y - yTarget) / (xTarget - x));
			} else if (xTarget - x < 0) {
				degreeTarget = Math.PI + Math.atan((y - yTarget) / (xTarget - x));
			} else {
				degreeTarget = Math.PI / 2;
			}
		} else {
			if (xTarget - x > 0) {
				degreeTarget = 2 * Math.PI + Math.atan((y - yTarget) / (xTarget - x));
			} else if (xTarget - x < 0) {
				degreeTarget = Math.atan((y - yTarget) / (xTarget - x)) + Math.PI;
			} else {
				degreeTarget = 3 * Math.PI / 2;
			}
		}

		double difference = degreeTarget - degree;

		// adjust the moving direction (LEFT and RIGHT) according to the degree
		// difference
		if (difference > 0) {
			if (difference <= Math.PI)
				computerTank.setDirectionLR(BubbleTank.LEFT);
			else
				computerTank.setDirectionLR(BubbleTank.RIGHT);
		} else if (difference < 0) {
			if (Math.abs(difference) >= Math.PI)
				computerTank.setDirectionLR(BubbleTank.LEFT);
			else
				computerTank.setDirectionLR(BubbleTank.RIGHT);
		} else {
			computerTank.setDirectionLR(BubbleTank.STOP);
		}

		// do not need to return boolean value (won't stop)
	}
}
