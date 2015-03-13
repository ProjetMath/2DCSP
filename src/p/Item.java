package p;

/**
 * Image
 *
 */
public class Item {
	private int id;
	private double height; //hauteur image
	private double width; //largeur image
	private int demand; //nombre d'image de ce type souhaité
	
	public Item(int id, double height, double width, int demand) {
		super();
		this.id = id;
		this.height = height;
		this.width = width;
		this.demand = demand;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public double getHeight() {
		return height;
	}
	public void setHeight(double height) {
		this.height = height;
	}
	public double getWidth() {
		return width;
	}
	public void setWidth(double width) {
		this.width = width;
	}
	public int getDemand() {
		return demand;
	}
	public void setDemand(int demand) {
		this.demand = demand;
	}
	
	
}
