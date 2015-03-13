package images;

import java.awt.Point;

/*
 * Image positionné
 */
public class Item {
	private TypeImage type;
	private Point position;
	
	public Item(TypeImage type, Point position) {
		super();
		this.setType(type);
		this.setPosition(position);
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
	
	
	
}
