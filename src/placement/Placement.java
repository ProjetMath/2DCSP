package placement;

import image.Item;
import image.TypeImage;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import main.Pattern;

public class Placement {
	private Map<TypeImage, Integer> imagesToPlace; //Type, nombre du type à placer
	private List<Item> images; //Liste de toutes les images placées
	private List<Bin> freeSpace; //Liste des rectanges d'espaces libres
	
	public Placement(Map<TypeImage, Integer> imagesToPlace) {
		this.imagesToPlace = imagesToPlace;
	}
	
	/**
	 * Try to place all the images
	 * @return Pattern or null if pattern not found
	 */
	public Pattern place() {
		//Transform type image in images with null position 
		images = new ArrayList<Item>();
		for (Entry<TypeImage, Integer> e : imagesToPlace.entrySet())
			for (int i = 0; i < e.getValue(); i++)
				images.add(new Item(e.getKey(), null, false)); //type, no position, not rotated
		
		//We maintain a list of rectangles F = {F1, .., Fn }; that represent the free space of the bin
		freeSpace = new ArrayList<Bin>();
		
		//The algorithm starts with a single free rectangle
		freeSpace.add(new Bin(Pattern.getWidth(), Pattern.getHeight(), new Point(0, 0)));
		
		Random rand = new Random();
		for (Item i : images)
		{
			//decide the free rectangle Fi € F to pack the rectangle into
			//Pick the free rectangle Fi of smallest area in which the next rectangle fits.
			Bin b = smallestArea(i.getType().getWidth(), i.getType().getHeight());
			
			//If no such rectangle is found, restart with a new bin ?
			if (b != null)
			{
				b.setEnabled(false); //choosen -> disable it
				
				//Decide the orientation for the rectangle and place it at the bootom-left of Fi
				if (b.getHeight() < i.getType().getHeight() || b.getWidth() < i.getType().getWidth())
				{ //too small in height or width
					i.setRotated(true);
				} else if (b.getHeight() >= i.getType().getWidth() && b.getWidth() >= i.getType().getHeight()) 
				{ //Same => random
					i.setRotated(rand.nextBoolean()); //random orientation
				} 
				
				i.setPosition(b.getPosition());
				
				//Faire le calcul des positions, etc ... on fonctions de si l'image est tournée ou non
				//Pareil pour la vérification en bas
				
				//Splitting
				Bin b1 = null, b2 = null;
				double w = 0, h = 0;
				if (i.isRotated()) {
					w = i.getType().getHeight();
					h = i.getType().getWidth();
				} else {
					w = i.getType().getWidth();
					h = i.getType().getHeight();	
				}
				
				Point p1 = new Point(b.getPosition().x + (int)w, b.getPosition().y);
				Point p2 = new Point(b.getPosition().x, b.getPosition().y + (int)h);
				if (b.getWidth() < b.getHeight())
				{ //Horizontally if wf < hf
					b1 = new Bin(b.getWidth() - w, h, p1);	
					b2 = new Bin(b.getWidth(), b.getHeight() - h, p2);
				} else 
				{ //Vertically if wf >= hf
					b1 = new Bin(b.getWidth() - w, b.getHeight(), p1);
					b2 = new Bin(w, b.getHeight() - h, p2);
				}
				
				freeSpace.add(b1);
				freeSpace.add(b2);
			} else {
				return null;
			}
		}
		
		return new Pattern(images, imagesToPlace);
	}
	
	/**
	 * Return the smallest area but superior to the surface in parameter
	 * @param surface : min surface
	 * @return Bin
	 */
	private Bin smallestArea(double w, double h) {
		Bin smallest = null;
		for (Bin b : freeSpace) 
			 //rentre dans au moins un sens
			if (b.isEnabled() && ((b.getHeight() >= h && b.getWidth() >= w) || (b.getHeight() >= w && b.getWidth() >= h))
				&& (smallest == null || 
					(smallest != null && smallest.getSurface() > b.getSurface()))
				)
			{
				smallest = b;
			}
		
		return smallest;
	}
	
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		/**
		 * TEST PLACEMENT
		 */
		Pattern.setSize(1400, 700); //pattern size
		//final int nbMaxPat = 3;
		
		//list types images
		List<TypeImage> tImages = new ArrayList<>();
		tImages.add(new TypeImage(0, 933, 372, 179));
		tImages.add(new TypeImage(1, 893, 307, 192));
		tImages.add(new TypeImage(2, 727, 333, 121));		
		
		Map<TypeImage, Integer> imgsNb = new HashMap<TypeImage, Integer>();
		imgsNb.put(tImages.get(0), 1);
		imgsNb.put(tImages.get(1), 1);
		imgsNb.put(tImages.get(2), 0);
		
		Placement pl = new Placement(imgsNb);
		Pattern p = pl.place();
		System.out.println(p);
	}

}
