package p;

import images.Item;

import java.util.List;

public class Pattern {
	private double height;
	private double width;
	private double surface;
	private List<Item> images;
	private int[] nbItem; //index correspond à l'index dans la liste de type d'image
		
	
	public double getHeight() {
		return height;
	}

	public double getWidth() {
		return width;
	}

	public double getSurface() {
		return surface;
	}

	public List<Item> getImages() {
		return images;
	}

	public void setImages(List<Item> images) {
		this.images = images;
	}
	
}
