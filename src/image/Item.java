package image;

import java.awt.Point;

/*
 * Image positionn�
 */
public class Item {
	private TypeImage type;
	private Point position;
	private boolean rotated; //sens de l'image (pivot� ou non)
	
	public Item(TypeImage type, Point position, boolean rotated) {
		super();
		this.setType(type);
		this.setPosition(position);
		this.setRotated(rotated);
	}

	public TypeImage getType() {
		return type;
	}

	public void setType(TypeImage type) {
		this.type = type;
	}

	public Point getPosition() {
		return position;
	}

	public void setPosition(Point position) {
		this.position = position;
	}

	public boolean isRotated() {
		return rotated;
	}

	public void setRotated(boolean rotated) {
		this.rotated = rotated;
	}
	
}
