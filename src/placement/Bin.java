package placement;

import java.awt.Point;

/*
 * Rectangle espace vide
 */
public class Bin {
	// private int mId; //num bin
	private double width, height, surface;
	// private int idPattern;
	private Point position; // left bottom point
	// private boolean rotated; //pivot� ou non

	// private Bin mBinCutting1; //Bin � desactiver apr�s la coupure
	// private Bin mBinCutting2; //Bin � desactiver apr�s la coupure

	private boolean enabled;

	public Bin(double width, double height, Point position) {
		this.width = width;
		this.height = height;
		this.position = position;

		this.surface = width * height;
		this.enabled = true;
	}

	public double getSurface() {
		return surface;
	}

	public double getHeight() {
		return height;
	}

	public double getWidth() {
		return width;
	}

	public void setEnabled(boolean e) {
		this.enabled = e;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public Point getPosition() {
		return position;
	}

	public void setPosition(Point position) {
		this.position = position;
	}

}
