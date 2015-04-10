package p;

import image.TypeImage;

import java.util.ArrayList;
import java.util.List;

/**
 * Execution general
 *
 */
public class Execute {

	public static void main(String[] args) {
		System.out.println("Welcome on board ! .. Someone !");

		List<TypeImage> imagesToPlace = new ArrayList<>();
		//TODO  Charger les images depuis les fichiers de données
		imagesToPlace.add(new TypeImage(0, 30, 24, 246));
		imagesToPlace.add(new TypeImage(1, 56, 13, 562));
		imagesToPlace.add(new TypeImage(2, 22, 14, 1000));
		imagesToPlace.add(new TypeImage(3, 23, 9, 3498));
		
		Pattern.setSize(40, 60); 
	
	}

}
