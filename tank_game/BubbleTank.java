package tank_game;
/**
 * File: BubbleTank.java
 * 
 * @author  Jiarui Wu
 * @version 1.0
 * @since   2021-11-08
 */

import java.util.HashSet;

public class BubbleTank {
	protected static final int FORWARD = 1;
	protected static final int LEFT = 1;
	protected static final int BACKWARD = -1;
	protected static final int RIGHT = -1;
	protected static final int STOP = 0;

	private int radius; // radius of bubble
	private double x; // x coordinate of the center of bubble
	private double y; // y coordinate of the center of bubble
	private int directionUD; // go forward, go backward, stop
	private int directionLR; // go left, go right, stop
	private double degree; // radius (direction) of bubble
	private HashSet<Bullet> bullets; // store all valid bullets of the tank

	private boolean flag; // used to judge whether the tank just pass through the boundary (this variable
							// is important for computer tank in one-player mode)

	public BubbleTank(int x, int y, double degree) {
		// the initial motion status is stop
		directionUD = STOP;
		directionLR = STOP;
		bullets = new HashSet<Bullet>();
		radius = 16;
		this.x = x;
		this.y = y;
		this.degree = degree;
		flag = false;
	}

	// getters
	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getDegree() {
		convertDegree();
		return degree;
	}

	public int getRadius() {
		return radius;
	}

	public HashSet<Bullet> getBullets() {
		return bullets;
	}

	// the predicted x coordinate, y coordinate, and degree for the next moment is
	// important for collision detection
	public double getNewX() {
		double newX;
		newX = x + Math.cos(getNewDegree()) * directionUD * 3;
		if (newX <= MainClass.X || newX >= MainClass.X + MainClass.WIDTH) {
			newX = MainClass.WIDTH / 2 - (x - MainClass.WIDTH / 2 + ((x - MainClass.WIDTH / 2 > 0) ? -2 : 2));
		} else {
			newX = x + Math.cos(getNewDegree()) * directionUD * 3;
		}
		return newX;
	}

	public double getNewY() {
		double newY;
		newY = y - Math.sin(getNewDegree()) * directionUD * 3;
		if (newY <= MainClass.Y || newY >= MainClass.Y + MainClass.HEIGHT) {
			newY = MainClass.HEIGHT / 2 - (y - MainClass.HEIGHT / 2 + ((y - MainClass.HEIGHT / 2 > 0) ? -2 : 2));
		} else {
			newY = y - Math.sin(getNewDegree()) * directionUD * 3;
		}
		return newY;
	}

	public double getNewDegree() {
		double newDegree = (degree + 2 * Math.PI / 360 * directionLR * 3) % (Math.PI * 2);
		return newDegree;
	}

	public boolean getFlag() {
		return flag;
	}

	// setters
	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public void setDirectionUD(int direction) {
		this.directionUD = direction;
	}

	public void setDirectionLR(int direction) {
		this.directionLR = direction;
	}

	// convert the degree into the range 0 <= degree < 2*Math.PI
	public void convertDegree() {
		if (degree >= 0) {
			degree = degree % (2 * Math.PI);
		} else {
			degree = 2 * Math.PI + (degree % (2 * Math.PI));
		}
	}

	// remove invalid bullet (the bullet which touches the boundary or opponent's
	// tank)
	public void removeBullet(Bullet bullet) {
		bullets.remove(bullet);
	}

	// method for refreshing the x, y coordinate of tank after moving
	public void moveBubble() {
		double newX = getNewX();
		double newY = getNewY();
		// if the tank has past through the boundary
		if (newX <= MainClass.X + 2 || newX >= MainClass.X + MainClass.WIDTH - 2) {
			flag = true;
		} else if (newY <= MainClass.Y + 2 || newY >= MainClass.Y + MainClass.HEIGHT - 2) {
			flag = true;
		}

		// update the value
		x = newX;
		y = newY;
		// calculate the new degree using the x, y coordinate
		degree = getNewDegree();

	}

	// create a new bullet after shooting key is pressed
	public void shoot() {
		Bullet bullet = new Bullet((int) x + (int) (Math.cos(degree) * radius),
				(int) y - (int) (Math.sin(degree) * radius), degree);
		// store the bullet in the Hash Set
		bullets.add(bullet);
	}
}
