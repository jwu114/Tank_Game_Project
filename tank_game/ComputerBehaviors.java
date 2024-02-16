package tank_game;
/**
 * File: ComputerBehaviors.java
 * 
 * @author  Jiarui Wu
 * @version 1.0
 * @since   2021-11-08
 */

import java.awt.event.ActionListener;

public interface ComputerBehaviors {
	// overall method for computer tank moving
	public void moveComputerTank();

	// regular motion mechanism (keep the distance between computer tank and
	// player's tank in a safe range)
	public void moveKeepingSafetyDistance();

	// motion mechanism when computer tank is near to the boundary (adjust the
	// direction and pass through or come out from the boundary )
	public void moveNearWall(int situation);

	// motion mechanism when escaping bullet
	public void escapeBullet(Bullet bullet);

	// one method of adjusting degree (LR Direction)
	public boolean adjustDegree(double degree, double degreeTarget);

	// the other method of adjusting degree (LR Direction) (Overload)
	public void adjustDegree(double x, double y, double degree, double xTarget, double yTarget);

	// method to determine whether computer tank is close to boundary. Also, determine which boundary the tank is close to (top, bottom, left, right).)
	public int getCloseToWallType();

	// method for shooting bullets
	public abstract class ComputerBulletShooting implements ActionListener {
	}
}