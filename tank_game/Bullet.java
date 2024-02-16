package tank_game;
/**
 * File: Bullet.java
 * 
 * @author  Jiarui Wu
 * @version 1.0
 * @since   2021-11-08
 */

public class Bullet {
	private int radius; // radius of bullet
	private double x; // x coordinate of the center of bullet
	private double y; // y coordinate of the center of bullet
	private double degree; // radius (direction) of bubble

	public Bullet(double x, double y, double degree) {
		radius = 4; // radius of bullet (4 pixels)
		this.x = x;
		this.y = y;

		// direction of bullet flying (convert the degree into the range 0 <= degree <
		// 2*Math.PI)
		if (degree >= 0) {
			degree = degree % (2 * Math.PI);
		} else {
			degree = 2 * Math.PI + (degree % (2 * Math.PI));
		}
		this.degree = degree;
	}

	// getters
	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getDegree() {
		return degree;
	}

	public int getRadius() {
		return radius;
	}

	public boolean moveBullet() {
		boolean valid = true; // represent whether the bullet touches the boundary (it will be invalid if
								// touching the boundary)
		double newX = x + Math.cos(degree); // new x coordinate
		double newY = y - Math.sin(degree); // new y coordinate
		// judge whether bullet touches the boundary
		if (newX - radius >= MainClass.X && newY - radius >= MainClass.Y
				&& newX + radius <= MainClass.X + MainClass.WIDTH && newY + radius <= MainClass.Y + MainClass.HEIGHT) {
			x = newX;
			y = newY;
		} else {
			valid = false;
		}
		return valid;
	}
}
