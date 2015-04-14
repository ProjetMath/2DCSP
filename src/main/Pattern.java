package main;

import image.Item;
import image.TypeImage;

import java.util.List;
import java.util.Map;

public class Pattern {
	private int id;
	private List<Item> images;
	private Map<TypeImage, Integer> imgsNb; //TypeImage / nombre dans pattern
	
	private static double height = 0.0, width = 0.0, surface = 0.0; //tous les patterns ont la même taille
	private static int cpt = 0;

	/* STATIC METHODS */
	public static double getHeight() {
		return height;
	}

	public static double getWidth() {
		return width;
	}
	
	public static double getSurface() {
		return surface;
	}
	
	public static void setSize(double newWidth, double newHeight)
	{
		height = newHeight;
		width = newWidth;
		surface = height * width;
	}
	
	public static void resetCpt()
	{
		cpt = 0;
	}
	
	/* OBJECT METHODS */
	public Pattern(List<Item> images, Map<TypeImage, Integer> imgsNb) 
	{
		this.images = images;
		this.imgsNb = imgsNb;
		this.id = cpt++;
	}

	public List<Item> getImages() {
		return images;
	}

	public void setImages(List<Item> images) {
		this.images = images;
	}
	
	public Map<TypeImage, Integer> getImgsNb() {
		return imgsNb;
	}

	public void setImgsNb(Map<TypeImage, Integer> imgsNb) {
		this.imgsNb = imgsNb;
	}
	
	public int getId() {
		return this.id;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Pattern)
		{
			Pattern p = (Pattern)obj;
			return this.getId() == p.getId();
		}
		return false;
	}
	
	@Override
	public String toString() {
		String s = "pattern "+id+" = {\r\n";
		
		for (Item i : images)
		{
			s += "   [";
			s += i.getType().getId()+", dem="+i.getType().getDemand()+", x="+i.getPosition().getX()+", y="+i.getPosition().getY();
			s += ", w="+i.getType().getWidth()+", h="+i.getType().getHeight();
			s += ", rot="+i.isRotated();
			s += "]\r\n";
		}
		
		s += "}";
		
		return s;
	}
}
