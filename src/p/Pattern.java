package p;

import image.Item;
import image.TypeImage;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Pattern {
	private static double height = 0.0, width = 0.0, surface = 0.0; //tous les patterns ont la même taille
	private List<Item> images;
	private Map<TypeImage, Integer> imgsNb; //TypeImage / nombre dans pattern

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
	
	/* OBJECT METHODS */
	public Pattern(List<Item> images, Map<TypeImage, Integer> imgsNb) 
	{
		this.images = images;
		this.imgsNb = imgsNb;
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
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Pattern)
		{
			Pattern p = (Pattern)obj;
			Map<TypeImage, Integer> map = p.getImgsNb();
			
			if (imgsNb.size() == map.size())
			{ //meme taille
				//Chaque type d'image a le même nombre d'images
				for(Entry<TypeImage, Integer> entry : imgsNb.entrySet())  
				{
					Integer v = map.get(entry.getKey()); //renvoi la valeur ou null
					if (v != entry.getValue()) 
						return false;
				}
				return true;
			}
		}
		return false;
	}
	
	@Override
	public String toString() {
		String s = "pattern = {\r\n";
		
		for (Item i : images)
		{
			s += "   [";
			s += i.getType().getId()+", x="+i.getPosition().getX()+", y="+i.getPosition().getY();
			s += ", w="+i.getType().getWidth()+", h="+i.getType().getHeight();
			s += "]\r\n";
		}
		
		s += "}";
		
		return s;
	}
}
