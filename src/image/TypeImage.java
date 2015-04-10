package image;

public class TypeImage {
	private int id;
	private double height; //hauteur image
	private double width; //largeur image
	private double surface; 
	private int demand; //nombre d'image de ce type souhaité
	
	public TypeImage(int id, double width, double height, int demand) {
		super();
		this.id = id;
		this.height = height;
		this.width = width;
		this.demand = demand;
		calculSurface();
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
		calculSurface();
	}
	
	public double getWidth() {
		return width;
	}
	
	public void setWidth(double width) {
		this.width = width;
		calculSurface();
	}
	
	public int getDemand() {
		return demand;
	}
	
	public void setDemand(int demand) {
		this.demand = demand;
	}
	
	public double getSurface() {
		return surface;
	}
	
	private double calculSurface() 
	{
		surface = height * width;
		return surface;
	}	
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof TypeImage) {
			TypeImage ti = (TypeImage)obj;
			
			return (this.id == ti.id);
		}
		return false;
	};
}
